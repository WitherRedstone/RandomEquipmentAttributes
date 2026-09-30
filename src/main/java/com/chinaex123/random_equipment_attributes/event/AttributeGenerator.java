package com.chinaex123.random_equipment_attributes.event;

import com.chinaex123.random_equipment_attributes.config.REAConfig;
import com.chinaex123.random_equipment_attributes.event.AttributePool.AttributeEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class AttributeGenerator {

    private AttributeGenerator() {}

    public static void generate(ItemStack stack, EquipmentSlot targetSlot, EquipmentSlotGroup equipGroup) {
        Random random = new Random();

        // 按物品类型选池
        List<AttributeEntry> typePool = AttributePool.selectFor(stack, targetSlot);
        // 再按槽位过滤
        List<AttributeEntry> filteredPool = filterBySlot(typePool, equipGroup);
        if (filteredPool.isEmpty()) return;

        // 遍历所有候选，尝试生成有效 tag（带 minMagnitude 过滤）
        // 成功生成的 tag 先全收集起来
        List<CompoundTag> candidates = new ArrayList<>();
        for (AttributeEntry entry : filteredPool) {
            CompoundTag tag = buildAttrTag(entry, random);
            if (tag != null) candidates.add(tag);
        }
        if (candidates.isEmpty()) return;  // 全部被筛空了，放弃

        // 从有效的 tag 里随机挑配置范围内的条数（不重复）
        int cfgMin = REAConfig.MIN_ATTR_COUNT.get();
        int cfgMax = Math.min(REAConfig.MAX_ATTR_COUNT.get(), candidates.size());
        if (cfgMax < cfgMin) cfgMax = cfgMin;  // candidates 不够时收缩
        int count = cfgMin + random.nextInt(cfgMax - cfgMin + 1);
        ListTag listTag = new ListTag();
        for (int i = 0; i < count; i++) {
            listTag.add(candidates.remove(random.nextInt(candidates.size())));
        }

        CompoundTag tag = SlotHelper.getCustomDataTag(stack);
        tag.put(SlotHelper.NBT_KEY_RANDOM_ATTRS, listTag);
        tag.putString(SlotHelper.NBT_KEY_SLOT_GROUP, equipGroup.toString().toLowerCase());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private static List<AttributeEntry> filterBySlot(List<AttributeEntry> pool, EquipmentSlotGroup equipGroup) {
        List<AttributeEntry> filtered = new ArrayList<>();
        for (AttributeEntry entry : pool) {
            if (entry.allowedSlots().contains(EquipmentSlotGroup.ANY)) {
                filtered.add(entry);
            } else {
                for (EquipmentSlotGroup group : entry.allowedSlots()) {
                    if (SlotHelper.groupMatches(group, Set.of(equipGroup))) {
                        filtered.add(entry);
                        break;
                    }
                }
            }
        }
        return filtered;
    }

    private static CompoundTag buildAttrTag(AttributeEntry entry, Random random) {
        ResourceLocation attrRl = BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value());
        if (attrRl == null) return null;

        CompoundTag tag = new CompoundTag();
        tag.putString(SlotHelper.NBT_KEY_ATTR_ID, attrRl.toString());

        double min = entry.min();
        double max = entry.max();

        // 循环抽样：有 minMagnitude 就反复抽直到 |val| 够大（最多 20 次，防止死循环）
        // 这是"事前保证"而不是"事后筛"——从根源杜绝因值太小导致的 null
        double val;
        int attempts = 0;
        do {
            // nextBoolean() 硬决定正负 → 永远 50/50，与 min/max 是否对称无关
            if (random.nextBoolean()) {
                // 负值：在 [min, 0] 区间内，用绝对值正态 → 密集在 0 附近
                double negSigma = Math.abs(min) / 2.5;
                val = Math.clamp(-Math.abs(random.nextGaussian()) * negSigma, min, 0);
            } else {
                // 正值：在 [0, max] 区间内，用绝对值正态 → 密集在 0 附近
                double posSigma = max / 2.5;
                val = Math.clamp(Math.abs(random.nextGaussian()) * posSigma, 0, max);
            }
            // step 量化（量化后可能变成 0，也要参与 minMagnitude 判断）
            if (entry.step() > 0) {
                val = Math.round(val / entry.step()) * entry.step();
            }
            attempts++;
        } while (entry.minMagnitude() > 0 && Math.abs(val) < entry.minMagnitude() && attempts < 20);

        if (attempts >= 20) return null;  // 极端情况下放弃（概率极低）
        tag.putDouble(SlotHelper.NBT_KEY_AMOUNT, Math.round(val * 1000.0) / 1000.0);

        tag.putInt(SlotHelper.NBT_KEY_OPERATION, entry.att().ordinal());
        return tag;
    }
}