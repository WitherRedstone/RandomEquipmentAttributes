package com.chinaex123.random_equipment_attributes.init;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.block.ReforgingStationBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface REABlocks {
    DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(RandomEquipmentAttributes.MODID);

    /** 重铸台 */
    DeferredBlock<Block> REFORGING_STATION = registerBlocks("reforging_station",
            () -> new ReforgingStationBlock(BlockBehaviour.Properties.of()
                    .strength(1.5F, 6.0F)
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        REAItems.ITEMS_REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCK_REGISTER.register(name, block);
        registerBlockItems(name, blocks);
        return blocks;
    }

    static void register(IEventBus eventBus){
        BLOCK_REGISTER.register(eventBus);
    }
}
