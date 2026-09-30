package com.chinaex123.random_equipment_attributes.event;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = RandomEquipmentAttributes.MODID)
public class RandomAttributeHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        ItemStack newStack = event.getTo();
        EquipmentSlot currentSlot = event.getSlot();

        if (newStack.isEmpty()) return;
        if (currentSlot.getType() != EquipmentSlot.Type.HAND && currentSlot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) return;
        if (!newStack.isEnchantable()) return;
        if (SlotHelper.getCustomDataTag(newStack).contains(SlotHelper.NBT_KEY_RANDOM_ATTRS)) return;

        EquipmentSlot targetSlot;
        EquipmentSlotGroup targetGroup;
        if (newStack.getItem() instanceof ArmorItem armorItem) {
            // 护甲
            targetSlot = armorItem.getEquipmentSlot();
            targetGroup = SlotHelper.slotToGroup(targetSlot);
        } else {
            // 武器/工具
            targetSlot = currentSlot;
            targetGroup = SlotHelper.slotToGroup(targetSlot);
        }

        AttributeGenerator.generate(newStack, targetSlot, targetGroup);
    }

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!SlotHelper.getCustomDataTag(stack).contains(SlotHelper.NBT_KEY_RANDOM_ATTRS)) return;
        AttributeApplier.apply(event, stack);
    }
}