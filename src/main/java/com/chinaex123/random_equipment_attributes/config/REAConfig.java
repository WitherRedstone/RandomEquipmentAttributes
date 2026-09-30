package com.chinaex123.random_equipment_attributes.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class REAConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue MIN_ATTR_COUNT;
    public static final ModConfigSpec.IntValue MAX_ATTR_COUNT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("通用配置").push("Common Config");
        MIN_ATTR_COUNT = builder
                .comment("每件装备随机的最少属性数量")
                .comment("The minimum number of random attributes per piece of equipment.")
                .defineInRange("minAttrCount", 1, 0, Integer.MAX_VALUE);
        MAX_ATTR_COUNT = builder
                .comment("每件装备随机的最多属性数量")
                .comment("The maximum number of random attributes per piece of equipment.")
                .defineInRange("maxAttrCount", 4, 1, Integer.MAX_VALUE);
        builder.pop();




        SPEC = builder.build();
    }

}