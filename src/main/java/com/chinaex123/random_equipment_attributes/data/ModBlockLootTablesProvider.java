package com.chinaex123.random_equipment_attributes.data;

import com.chinaex123.random_equipment_attributes.init.REABlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {
    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(REABlocks.REFORGING_STATION.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return REABlocks.BLOCK_REGISTER.getEntries().stream().map(Holder::value)::iterator;
    }
}
