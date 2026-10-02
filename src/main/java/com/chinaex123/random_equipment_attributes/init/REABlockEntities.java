package com.chinaex123.random_equipment_attributes.init;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.blockentity.ReforgingStationBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class REABlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RandomEquipmentAttributes.MODID);

    public static final Supplier<BlockEntityType<ReforgingStationBlockEntity>> REFORGING_STATION =
            BLOCK_ENTITIES.register("reforging_station", () -> BlockEntityType.Builder.of(
                    ReforgingStationBlockEntity::new,
                    REABlocks.REFORGING_STATION.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}