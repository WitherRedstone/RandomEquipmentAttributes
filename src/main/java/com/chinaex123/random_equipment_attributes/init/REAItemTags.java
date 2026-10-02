package com.chinaex123.random_equipment_attributes.init;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface REAItemTags {

    TagKey<Item> REFORGE_MATERIALS = bind("reforge_materials");

    private static TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(RandomEquipmentAttributes.MODID, name));
    }
}
