package com.chinaex123.random_equipment_attributes.event;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

/**
 * 属性修饰符应用器。
 * <p>
 * 从物品的自定义数据中读取随机属性记录，
 * 在物品属性修饰符事件中逐条还原为对应的属性修饰符，
 * 并按记录的装备槽位组应用。
 */
public final class AttributeApplier {

    /**
     * 私有构造函数，防止实例化。
     * <p>
     * 该类只提供静态方法，不需要实例。
     */
    private AttributeApplier() {}

    /**
     * 将物品自定义数据中的随机属性应用到属性修饰符事件。
     * <p>
     * 读取随机属性列表与目标装备槽位组，逐条解析属性标识、数值与运算方式，
     * 并为每条记录生成唯一修饰符 ID 后加入事件。
     * 数据缺失、槽位组非法或属性不存在时跳过相应内容。
     *
     * @param event 物品属性修饰符事件
     * @param stack 待处理的物品堆
     */
    public static void apply(ItemAttributeModifierEvent event, ItemStack stack) {
        CompoundTag tag = SlotHelper.getCustomDataTag(stack);
        if (!tag.contains(SlotHelper.NBT_KEY_RANDOM_ATTRS)) return;

        ListTag attrsList = tag.getList(SlotHelper.NBT_KEY_RANDOM_ATTRS, CompoundTag.TAG_COMPOUND);
        if (attrsList.isEmpty()) return;

        EquipmentSlotGroup targetSlot;
        try {
            targetSlot = EquipmentSlotGroup.valueOf(tag.getString(SlotHelper.NBT_KEY_SLOT_GROUP).toUpperCase());
        } catch (IllegalArgumentException e) {
            return;
        }

        // 逐条还原修饰符
        for (int i = 0; i < attrsList.size(); i++) {
            CompoundTag attrTag = attrsList.getCompound(i);

            String attrId = attrTag.getString(SlotHelper.NBT_KEY_ATTR_ID);
            double amount = attrTag.getDouble(SlotHelper.NBT_KEY_AMOUNT);
            int operation = attrTag.getInt(SlotHelper.NBT_KEY_OPERATION);

            ResourceLocation attrRl = ResourceLocation.parse(attrId);
            Holder<Attribute> attrHolder = BuiltInRegistries.ATTRIBUTE.getHolder(attrRl).orElse(null);
            if (attrHolder == null) continue;

            AttributeModifier.Operation op = operation == 2
                    ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    : AttributeModifier.Operation.ADD_VALUE;

            ResourceLocation modifierId = RandomEquipmentAttributes.id(
                    "random_" + targetSlot.toString().toLowerCase() + "_" + attrRl.getPath() + "_" + i
            );
            event.addModifier(attrHolder, new AttributeModifier(modifierId, amount, op), targetSlot);
        }
    }
}