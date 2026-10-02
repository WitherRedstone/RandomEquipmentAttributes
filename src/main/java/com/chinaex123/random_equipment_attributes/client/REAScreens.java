package com.chinaex123.random_equipment_attributes.client;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.client.gui.ReforgingStationScreen;
import com.chinaex123.random_equipment_attributes.init.REAMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = RandomEquipmentAttributes.MODID, value = Dist.CLIENT)
public class REAScreens {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(REAMenuTypes.REFORGING_STATION.get(), ReforgingStationScreen::new);
    }
}