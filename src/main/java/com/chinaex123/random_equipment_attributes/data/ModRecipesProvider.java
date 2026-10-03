package com.chinaex123.random_equipment_attributes.data;

import com.chinaex123.random_equipment_attributes.init.REABlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, REABlocks.REFORGING_STATION.get())
                .pattern("DBD")
                .pattern("CAC")
                .pattern("DBD")
                .define('A', Items.SMITHING_TABLE)
                .define('B', Tags.Items.GEMS_AMETHYST)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .define('D', Tags.Items.GEMS_EMERALD)
                .unlockedBy("has_reforging_station_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_reforging_station_diamond", has(Tags.Items.GEMS_DIAMOND))
                .unlockedBy("has_reforging_station_emerald", has(Tags.Items.GEMS_EMERALD))
                .save(recipeOutput);

    }
}
