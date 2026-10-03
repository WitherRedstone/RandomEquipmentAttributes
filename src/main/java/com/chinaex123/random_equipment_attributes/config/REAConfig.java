package com.chinaex123.random_equipment_attributes.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class REAConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue MIN_ATTR_COUNT;
    public static final ModConfigSpec.IntValue MAX_ATTR_COUNT;
    public static final ModConfigSpec.DoubleValue REFORGE_BASE_POSITIVE_PROB;
    public static final ModConfigSpec.DoubleValue REFORGE_PROBABILITY_INCREMENT;
    public static final ModConfigSpec.BooleanValue SHOW_REFORGE_COUNT_TOOLTIP;

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
        REFORGE_BASE_POSITIVE_PROB = builder
                .comment("重铸时正面属性的起始概率（%）")
                .comment("Base probability of positive attributes when reforging (%).")
                .defineInRange("reforgeBasePositiveProb", 0.5, 0.0, 1.0);
        REFORGE_PROBABILITY_INCREMENT = builder
                .comment("每次重铸后正面概率的增量，单位为 %")
                .comment("Increment of positive probability per reforge, in %.")
                .defineInRange("reforgeProbabilityIncrement", 0.01, 0.0, 100.0);
        SHOW_REFORGE_COUNT_TOOLTIP = builder
                .comment("是否在装备 tooltip 上显示重铸次数")
                .comment("Whether to show reforge count in item tooltip.")
                .define("showReforgeCountTooltip", true);
        builder.pop();

        SPEC = builder.build();
    }

}