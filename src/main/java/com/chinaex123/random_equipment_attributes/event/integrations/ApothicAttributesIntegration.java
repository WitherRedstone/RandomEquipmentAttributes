package com.chinaex123.random_equipment_attributes.event.integrations;

import com.chinaex123.random_equipment_attributes.event.AttributePool.AttributeEntry;
import com.chinaex123.random_equipment_attributes.event.AttributePool.ATT;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Apothic Attributes 模组联动。
 */
public final class ApothicAttributesIntegration {

    private static final String APOTHIC_ATTRIBUTES_MODID = "apothic_attributes";

    public static final List<AttributeEntry> ATTACK_ATTRS;
    public static final List<AttributeEntry> INTERACT_ATTRS;
    public static final List<AttributeEntry> SWORD_ATTRS;
    public static final List<AttributeEntry> DIGGER_ATTRS;
    public static final List<AttributeEntry> ARMOR_ATTRS;
    public static final List<AttributeEntry> RANGED_ATTRS;
    public static final List<AttributeEntry> COMMON_ATTRS;

    static {
        List<AttributeEntry> attackAttrs   = List.of();
        List<AttributeEntry> interactAttrs = List.of();
        List<AttributeEntry> swordAttrs    = List.of();
        List<AttributeEntry> diggerAttrs   = List.of();
        List<AttributeEntry> armorAttrs    = List.of();
        List<AttributeEntry> rangedAttrs   = List.of();
        List<AttributeEntry> commonAttrs   = List.of();

        if (ModList.get().isLoaded(APOTHIC_ATTRIBUTES_MODID)) {
            // ================== 通用属性 ==================
            commonAttrs = listOf(
                    // 经验获取（%）[数字计算]
                    attr("experience_gained", -1.0, 2.0,
                            Set.of(EquipmentSlotGroup.ANY), 0.25, 0.25, ATT.ADD_VALUE)
            );

            // ================== 攻击类 ==================
            attackAttrs = listOf(
                    // 暴击率（%）[数字计算]
                    attr("crit_chance", -0.05, 0.5,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0, ATT.ADD_VALUE),
                    // 暴击伤害（%）[数字计算]
                    attr("crit_damage", -1.5, 2.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0, ATT.ADD_VALUE)
            );

            // ================== 交互类 ==================

            // ================== 剑专属 ==================
            swordAttrs = listOf(
                    // 冰冻伤害（%）[数字计算]
                    attr("cold_damage", 0, 5.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0.25, 0.25, ATT.ADD_VALUE),
                    // 火焰伤害（%）[数字计算]
                    attr("fire_damage", 0, 5.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0.25, 0.25, ATT.ADD_VALUE),
                    // 生命偷取（%）[数字计算]
                    attr("life_steal", 0, 0.25,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0.005, 0.005, ATT.ADD_VALUE),
                    // 当前生命值伤害（%）[数字计算]
                    attr("current_hp_damage", 0, 0.1,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.01, ATT.ADD_VALUE)
            );

            // ================== 挖掘工具专属 ==================
            diggerAttrs = listOf(
                    // 挖掘速度（%）[百分制计算]
                    attr("mining_speed", -0.5, 2.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL)
            );

            // ================== 远程武器专属 ==================
            rangedAttrs = listOf(
                    // 蓄力速度（%）[百分制计算]
                    attr("draw_speed", -0.5, 1.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL),
                    // 箭矢伤害（%）[百分制计算]
                    attr("arrow_damage", -0.5, 1.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL),
                    // 箭矢速度（%）[百分制计算]
                    attr("arrow_velocity", -0.5, 1.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL),
                    // 弹射物伤害（%）[百分制计算]
                    attr("projectile_damage", -0.5, 1.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL)
            );

            // ================== 护甲 ==================
            armorAttrs = listOf(
                    // 闪避率（%）[百分制计算]
                    attr("dodge_chance", -0.5, 1.0,
                            Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL)
            );
        }

        ATTACK_ATTRS   = attackAttrs;
        INTERACT_ATTRS = interactAttrs;
        SWORD_ATTRS    = swordAttrs;
        DIGGER_ATTRS   = diggerAttrs;
        ARMOR_ATTRS    = armorAttrs;
        RANGED_ATTRS   = rangedAttrs;
        COMMON_ATTRS   = commonAttrs;
    }

    @Nullable
    private static AttributeEntry attr(String path, double min, double max,
                                       Set<EquipmentSlotGroup> allowedSlots, ATT att) {
        return attr(path, min, max, allowedSlots, 0, 0, att);
    }

    @Nullable
    private static AttributeEntry attr(String path, double min, double max,
                                       Set<EquipmentSlotGroup> allowedSlots,
                                       double step, ATT att) {
        return attr(path, min, max, allowedSlots, step, 0, att);
    }

    @Nullable
    private static AttributeEntry attr(String path, double min, double max,
                                       Set<EquipmentSlotGroup> allowedSlots,
                                       double step, double minMagnitude, ATT att) {
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(APOTHIC_ATTRIBUTES_MODID, path);
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(rl);
        if (attribute == null) return null;
        Holder<Attribute> holder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
        return new AttributeEntry(holder, min, max, allowedSlots, step, minMagnitude, att);
    }

    private static List<AttributeEntry> listOf(@Nullable AttributeEntry... entries) {
        var result = new ArrayList<AttributeEntry>(entries.length);
        for (AttributeEntry e : entries) {
            if (e != null) result.add(e);
        }
        return List.copyOf(result);
    }

    private ApothicAttributesIntegration() {}
}