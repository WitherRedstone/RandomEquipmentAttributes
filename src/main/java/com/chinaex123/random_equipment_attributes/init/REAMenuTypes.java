package com.chinaex123.random_equipment_attributes.init;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.client.menu.ReforgingStationMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface REAMenuTypes {

    DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, RandomEquipmentAttributes.MODID);

    /** 重铸台菜单类型 */
    Supplier<MenuType<ReforgingStationMenu>> REFORGING_STATION =
            MENU_TYPES.register("reforging_station",
                    () -> IMenuTypeExtension.create(ReforgingStationMenu::new));

    static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}