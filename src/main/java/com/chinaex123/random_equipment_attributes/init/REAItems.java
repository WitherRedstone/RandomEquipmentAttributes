package com.chinaex123.random_equipment_attributes.init;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface REAItems {
    DeferredRegister.Items ITEMS_REGISTER = DeferredRegister.createItems(RandomEquipmentAttributes.MODID);

    static void register(IEventBus eventBus) {
        ITEMS_REGISTER.register(eventBus);
    }
}