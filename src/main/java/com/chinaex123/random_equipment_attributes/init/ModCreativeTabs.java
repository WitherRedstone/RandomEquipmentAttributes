package com.chinaex123.random_equipment_attributes.init;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RandomEquipmentAttributes.MODID);

    public static final Supplier<CreativeModeTab> REFORGING_STATION_TAB =
            CREATIVE_MODE_TAB.register("reforging_station_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(REABlocks.REFORGING_STATION.get()))
                    .title(Component.translatable("itemGroup.reforging_station_tab"))
                    .displayItems((parameters, output) -> {

                        output.accept(REABlocks.REFORGING_STATION.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
