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
 * <p>
 * 在 Apothic Attributes 模组加载时，按其提供的属性标识构建各分组的属性条目，
 * 供随机属性生成器选用；模组未加载时各分组均为空列表。
 * 属性通过标识符在运行时查找，避免直接依赖该模组。
 */
public final class ApothicAttributesIntegration {

    /** Apothic Attributes 的模组标识符 */
    private static final String APOTHIC_ATTRIBUTES_MODID = "apothic_attributes";

    /** 攻击类属性条目 */
    public static final List<AttributeEntry> ATTACK_ATTRS;
    /** 交互类属性条目 */
    public static final List<AttributeEntry> INTERACT_ATTRS;
    /** 剑专属属性条目 */
    public static final List<AttributeEntry> SWORD_ATTRS;
    /** 挖掘工具专属属性条目 */
    public static final List<AttributeEntry> DIGGER_ATTRS;
    /** 护甲属性条目 */
    public static final List<AttributeEntry> ARMOR_ATTRS;
    /** 远程武器专属属性条目 */
    public static final List<AttributeEntry> RANGED_ATTRS;
    /** 通用属性条目 */
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

    /**
     * 构建属性条目（不设置步长与最小幅值）。
     *
     * @param path         属性路径
     * @param min          数值下限
     * @param max          数值上限
     * @param allowedSlots 允许的装备槽位组集合
     * @param att          属性运算方式
     * @return 属性条目，属性不存在时返回 null
     */
    @Nullable
    private static AttributeEntry attr(String path, double min, double max,
                                       Set<EquipmentSlotGroup> allowedSlots, ATT att) {
        return attr(path, min, max, allowedSlots, 0, 0, att);
    }

    /**
     * 构建属性条目（不设置最小幅值）。
     *
     * @param path         属性路径
     * @param min          数值下限
     * @param max          数值上限
     * @param allowedSlots 允许的装备槽位组集合
     * @param step         数值量化步长
     * @param att          属性运算方式
     * @return 属性条目，属性不存在时返回 null
     */
    @Nullable
    private static AttributeEntry attr(String path, double min, double max,
                                       Set<EquipmentSlotGroup> allowedSlots,
                                       double step, ATT att) {
        return attr(path, min, max, allowedSlots, step, 0, att);
    }

    /**
     * 构建属性条目。
     * <p>
     * 以 Apothic Attributes 命名空间与给定路径拼接标识符，
     * 在属性注册表中查找对应属性并包装为持有者；
     * 属性不存在时返回 null。
     *
     * @param path         属性路径
     * @param min          数值下限
     * @param max          数值上限
     * @param allowedSlots 允许的装备槽位组集合
     * @param step         数值量化步长
     * @param minMagnitude 最小幅值要求
     * @param att          属性运算方式
     * @return 属性条目，属性不存在时返回 null
     */
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

    /**
     * 将若干属性条目（可含 null）收集为不可变列表。
     *
     * @param entries 属性条目数组，允许包含 null
     * @return 过滤 null 后的不可变列表
     */
    private static List<AttributeEntry> listOf(@Nullable AttributeEntry... entries) {
        var result = new ArrayList<AttributeEntry>(entries.length);
        for (AttributeEntry e : entries) {
            if (e != null) result.add(e);
        }
        return List.copyOf(result);
    }

    /**
     * 私有构造函数，防止实例化。
     * <p>
     * 该类只提供静态字段与方法，不需要实例。
     */
    private ApothicAttributesIntegration() {}
}