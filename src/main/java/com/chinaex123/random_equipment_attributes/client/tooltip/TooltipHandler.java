package com.chinaex123.random_equipment_attributes.client.tooltip;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.config.REAConfig;
import com.chinaex123.random_equipment_attributes.event.SlotHelper;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * 物品 tooltip 事件处理器。
 * <p>
 * 监听 ItemTooltipEvent，在携带随机属性的装备上显示重铸次数。
 */
@EventBusSubscriber(modid = RandomEquipmentAttributes.MODID, value = Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!REAConfig.SHOW_REFORGE_COUNT_TOOLTIP.get()) return;

        var stack = event.getItemStack();
        if (!SlotHelper.hasRandomAttrs(stack)) return;

        int count = SlotHelper.readReforgeCount(stack);
        var tooltip = event.getToolTip();

        if (!tooltip.isEmpty()) {
            tooltip.add(Component.empty());
        }
        tooltip.add(Component.translatable("tooltip.rea.reforge_count", count));
    }
}