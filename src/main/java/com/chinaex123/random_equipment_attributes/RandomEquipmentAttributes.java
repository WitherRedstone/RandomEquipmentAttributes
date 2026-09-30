package com.chinaex123.random_equipment_attributes;

import com.chinaex123.random_equipment_attributes.config.REAConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(RandomEquipmentAttributes.MODID)
public class RandomEquipmentAttributes {
    public static final String MODID = "random_equipment_attributes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RandomEquipmentAttributes(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, REAConfig.SPEC);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(RandomEquipmentAttributes.MODID, name);
    }
}