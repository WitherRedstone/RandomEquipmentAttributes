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

public final class AttributeApplier {

    private AttributeApplier() {}

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