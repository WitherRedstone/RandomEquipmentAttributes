package com.chinaex123.random_equipment_attributes.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Set;

public final class SlotHelper {

    public static final String NBT_KEY_RANDOM_ATTRS = "rae_attributes";
    public static final String NBT_KEY_ATTR_ID = "attribute_id";
    public static final String NBT_KEY_AMOUNT = "amount";
    public static final String NBT_KEY_OPERATION = "operation";
    public static final String NBT_KEY_SLOT_GROUP = "rae_slot_group";

    private SlotHelper() {}

    public static EquipmentSlotGroup slotToGroup(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> EquipmentSlotGroup.MAINHAND;
            case OFFHAND  -> EquipmentSlotGroup.OFFHAND;
            case HEAD     -> EquipmentSlotGroup.HEAD;
            case CHEST    -> EquipmentSlotGroup.CHEST;
            case LEGS     -> EquipmentSlotGroup.LEGS;
            case FEET     -> EquipmentSlotGroup.FEET;
            case BODY     -> EquipmentSlotGroup.BODY;
        };
    }

    public static CompoundTag getCustomDataTag(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.copyTag();
    }

    public static boolean groupMatches(EquipmentSlotGroup allowedSlot, Set<EquipmentSlotGroup> itemSlots) {
        if (itemSlots.contains(allowedSlot)) return true;
        for (EquipmentSlotGroup itemSlot : itemSlots) {
            if (groupCovers(allowedSlot, itemSlot)) return true;
        }
        return false;
    }

    public static boolean groupCovers(EquipmentSlotGroup allowedSlot, EquipmentSlotGroup itemSlot) {
        if (allowedSlot.equals(itemSlot)) return true;
        return switch (allowedSlot) {
            case ANY -> true;
            case HAND -> itemSlot == EquipmentSlotGroup.MAINHAND
                    || itemSlot == EquipmentSlotGroup.OFFHAND;
            case ARMOR -> itemSlot == EquipmentSlotGroup.HEAD
                    || itemSlot == EquipmentSlotGroup.CHEST
                    || itemSlot == EquipmentSlotGroup.LEGS
                    || itemSlot == EquipmentSlotGroup.FEET;
            default -> false;
        };
    }
}