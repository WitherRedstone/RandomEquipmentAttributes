package com.chinaex123.random_equipment_attributes.data;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.block.ReforgingStationBlock;
import com.chinaex123.random_equipment_attributes.init.REABlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStatesProvider extends BlockStateProvider {
    public ModBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RandomEquipmentAttributes.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        var model = models().withExistingParent("reforging_station", "minecraft:block/cube")
                .texture("up", modLoc("block/reforging_station_top"))
                .texture("down", modLoc("block/reforging_station_bottom"))
                .texture("east", modLoc("block/reforging_station_side"))
                .texture("north", modLoc("block/reforging_station_front"))
                .texture("south", modLoc("block/reforging_station_front"))
                .texture("west", modLoc("block/reforging_station_side"))
                .texture("particle", modLoc("block/reforging_station_front"));


        getVariantBuilder(REABlocks.REFORGING_STATION.get())
                .forAllStates(state -> {
                    var direction = state.getValue(ReforgingStationBlock.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(direction.get2DDataValue() * 90)
                            .build();
                });

        simpleBlockItem(REABlocks.REFORGING_STATION.get(), model);
    }
}
