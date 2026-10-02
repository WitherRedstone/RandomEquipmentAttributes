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

/**
 * 随机属性事件处理器。
 * <p>
 * 监听装备变更事件，为未携带随机属性的可附魔装备生成随机属性；
 * 监听物品属性修饰符事件，将物品自定义数据中的随机属性还原为修饰符。
 */
@EventBusSubscriber(modid = RandomEquipmentAttributes.MODID)
public class RandomAttributeHandler {

    /**
     * 处理装备变更事件。
     * <p>
     * 仅在装备非空、槽位为手部或人形护甲、物品可附魔且尚未携带随机属性时生效：
     * 护甲按其自身装备槽位、武器与工具按当前槽位确定目标槽位与槽位组，
     * 随后调用生成器为其生成随机属性。
     *
     * @param event 装备变更事件
     */
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

    /**
     * 处理物品属性修饰符事件。
     * <p>
     * 若物品携带随机属性数据，则将其逐条还原为属性修饰符并应用到事件中。
     *
     * @param event 物品属性修饰符事件
     */
    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!SlotHelper.getCustomDataTag(stack).contains(SlotHelper.NBT_KEY_RANDOM_ATTRS)) return;
        AttributeApplier.apply(event, stack);
    }
}