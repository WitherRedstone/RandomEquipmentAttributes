package com.chinaex123.random_equipment_attributes.data;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.init.REAItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, RandomEquipmentAttributes.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // 重铸属性材料
        tag(REAItemTags.REFORGE_MATERIALS)
                .add(Items.DIAMOND);
//        // 删除随机附魔材料
//        tag(REAItemTags.REFRESH_PERK_MATERIAL)
//                .add(Items.DIAMOND);
    }

}
