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

/**
 * 随机属性生成器。
 * <p>
 * 依据属性池与配置，为物品生成若干条随机属性记录并写入其自定义数据中。
 * 生成过程按物品类型选池、按装备槽位过滤，再从有效候选中随机挑选
 * 配置范围内的条数，数值采用正态分布抽样并按步长量化。
 */
public final class AttributeGenerator {

    /**
     * 私有构造函数，防止实例化。
     * <p>
     * 该类只提供静态方法，不需要实例。
     */
    private AttributeGenerator() {}

    /**
     * 重新生成物品的随机属性。
     * <p>
     * 仅当物品已存在随机属性时执行：读取原装备槽位组、清空旧属性后重新生成，
     * 返回重新生成后物品是否仍带有随机属性。
     *
     * @param stack 待处理的物品堆
     * @return 重新生成后物品仍带有随机属性返回 true
     */
    public static boolean regenerate(ItemStack stack) {
        if (!SlotHelper.hasRandomAttrs(stack)) return false;

        EquipmentSlotGroup group = SlotHelper.readSlotGroup(stack);
        SlotHelper.clearRandomAttrs(stack);

        generate(stack, EquipmentSlot.MAINHAND, group);

        return SlotHelper.hasRandomAttrs(stack);
    }

    /**
     * 为物品生成随机属性。
     * <p>
     * 先按物品类型选取属性池，再按装备槽位组过滤，随后为每个候选构建属性标签；
     * 从有效候选中随机挑选配置范围内的条数，写入自定义数据并记录装备槽位组。
     *
     * @param stack       待处理的物品堆
     * @param targetSlot  目标装备槽位
     * @param equipGroup  目标装备槽位组
     */
    public static void generate(ItemStack stack, EquipmentSlot targetSlot, EquipmentSlotGroup equipGroup) {
        Random random = new Random();

        // 按物品类型选池
        List<AttributeEntry> typePool = AttributePool.selectFor(stack, targetSlot);
        // 再按槽位过滤
        List<AttributeEntry> filteredPool = filterBySlot(typePool, equipGroup);
        if (filteredPool.isEmpty()) return;

        // 遍历所有候选，尝试生成有效 tag
        List<CompoundTag> candidates = new ArrayList<>();
        for (AttributeEntry entry : filteredPool) {
            CompoundTag tag = buildAttrTag(entry, random);
            if (tag != null) candidates.add(tag);
        }
        if (candidates.isEmpty()) return;

        // 从有效的 tag 里随机挑配置范围内的条数（不重复）
        int cfgMin = REAConfig.MIN_ATTR_COUNT.get();
        int cfgMax = Math.min(REAConfig.MAX_ATTR_COUNT.get(), candidates.size());
        if (cfgMax < cfgMin) cfgMax = cfgMin;
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

    /**
     * 按装备槽位组过滤属性池。
     * <p>
     * 允许任意槽位（ANY）的条目直接保留，否则在其允许的槽位组中
     * 查找与目标槽位组匹配的项，匹配成功则保留。
     *
     * @param pool      待过滤的属性池
     * @param equipGroup 目标装备槽位组
     * @return 过滤后的属性池
     */
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

    /**
     * 为单个属性条目构建随机数值的 NBT 标签。
     * <p>
     * 数值采用正态分布抽样：正负方向各占一半概率，分别在半区间内取绝对值正态分布，
     * 再按步长量化并做最小幅值约束的循环抽样（最多 20 次）。
     * 属性标识缺失或抽样失败时返回 null。
     *
     * @param entry  属性条目
     * @param random 随机源
     * @return 属性 NBT 标签，无法生成时返回 null
     */
    private static CompoundTag buildAttrTag(AttributeEntry entry, Random random) {
        ResourceLocation attrRl = BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value());
        if (attrRl == null) return null;

        CompoundTag tag = new CompoundTag();
        tag.putString(SlotHelper.NBT_KEY_ATTR_ID, attrRl.toString());

        double min = entry.min();
        double max = entry.max();

        // 循环抽样：有 minMagnitude 就反复抽直到 |val| 够大（最多 20 次，防止死循环）
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
            // step 量化
            if (entry.step() > 0) {
                val = Math.round(val / entry.step()) * entry.step();
            }
            attempts++;
        } while (entry.minMagnitude() > 0 && Math.abs(val) < entry.minMagnitude() && attempts < 20);

        if (attempts >= 20) return null;
        tag.putDouble(SlotHelper.NBT_KEY_AMOUNT, Math.round(val * 1000.0) / 1000.0);

        tag.putInt(SlotHelper.NBT_KEY_OPERATION, entry.att().ordinal());
        return tag;
    }
}