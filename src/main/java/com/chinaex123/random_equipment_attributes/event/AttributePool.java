package com.chinaex123.random_equipment_attributes.event;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.chinaex123.random_equipment_attributes.event.integrations.ApothicAttributesIntegration;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class AttributePool {

    // ================== 通用属性 ==================
    public static final List<AttributeEntry> COMMON_ATTRS = List.of(
            // 移动速度（%）[百分制计算]
            new AttributeEntry(Attributes.MOVEMENT_SPEED, -0.25, 0.25,
                    Set.of(EquipmentSlotGroup.ANY), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL),
            // 移动效率（%）[百分制计算]
            new AttributeEntry(Attributes.MOVEMENT_EFFICIENCY, -0.10, 0.10,
                    Set.of(EquipmentSlotGroup.ANY), 0, 0.1, ATT.ADD_VALUE),
            // 幸运[数字计算]
            new AttributeEntry(Attributes.LUCK, -5.0, 10.0,
                    Set.of(EquipmentSlotGroup.ANY), 0.5, 0, ATT.ADD_VALUE)
    );

    // ================== 攻击类 ==================
    public static final List<AttributeEntry> ATTACK_ATTRS = List.of(
            // 攻击伤害[百分制计算]
            new AttributeEntry(Attributes.ATTACK_DAMAGE, -0.35, 0.35,
                    Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.15, ATT.ADD_MULTIPLIED_TOTAL),
            // 攻击速度[百分制计算]
            new AttributeEntry(Attributes.ATTACK_SPEED, -0.5, 0.5,
                    Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_MULTIPLIED_TOTAL)
    );

    // ================== 交互类（所有主手物品都能用） ==================
    public static final List<AttributeEntry> INTERACT_ATTRS = List.of(
            // 方块交互距离[数字计算]
            new AttributeEntry(Attributes.BLOCK_INTERACTION_RANGE, -3.0, 3.0,
                    Set.of(EquipmentSlotGroup.MAINHAND), 0.5, 0.5, ATT.ADD_VALUE),
            // 实体交互距离[数字计算]
            new AttributeEntry(Attributes.ENTITY_INTERACTION_RANGE, -3.0, 3.0,
                    Set.of(EquipmentSlotGroup.MAINHAND), 0.5, 0.5, ATT.ADD_VALUE)
    );

    // ================== 剑专属 ==================
    public static final List<AttributeEntry> SWORD_ATTRS = List.of(
            // 横扫伤害比例（%）[数字计算]
            new AttributeEntry(Attributes.SWEEPING_DAMAGE_RATIO, -0.05, 0.05,
                    Set.of(EquipmentSlotGroup.MAINHAND), 0, 0.1, ATT.ADD_VALUE)
    );

    // ================== 挖掘工具专属 ==================
    public static final List<AttributeEntry> DIGGER_ATTRS = List.of(
            // 方块破坏速度[数字计算]
            new AttributeEntry(Attributes.BLOCK_BREAK_SPEED, -1.0, 1.0,
                    Set.of(EquipmentSlotGroup.MAINHAND),0, 0.1, ATT.ADD_VALUE)
    );

    // ================== 护甲 ==================
    public static final List<AttributeEntry> ARMOR_ATTRS = List.of(
            // 护甲值[百分制计算]
            new AttributeEntry(Attributes.ARMOR, -1.0, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.05, ATT.ADD_MULTIPLIED_TOTAL),
            // 护甲韧性[数字计算]
            new AttributeEntry(Attributes.ARMOR_TOUGHNESS, -10, 10,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.25, ATT.ADD_VALUE),
            // 击退抗性（%）[数字计算]
            new AttributeEntry(Attributes.KNOCKBACK_RESISTANCE, -0.10, 0.10,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 燃烧时间[数字计算]
            new AttributeEntry(Attributes.BURNING_TIME, -1.0, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_MULTIPLIED_TOTAL),
            // 爆炸击退抗性（%）[数字计算]
            new AttributeEntry(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, -0.10, 0.10,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 最大生命值[数字计算]
            new AttributeEntry(Attributes.MAX_HEALTH, -10, 20,
                    Set.of(EquipmentSlotGroup.ARMOR), 0.5, 0.5, ATT.ADD_VALUE),
            // 摔落伤害倍率（%）[数字计算]
            new AttributeEntry(Attributes.FALL_DAMAGE_MULTIPLIER, -0.10, 0.10,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 安全摔落距离[数字计算]
            new AttributeEntry(Attributes.SAFE_FALL_DISTANCE, -3.0, 3.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 台阶高度[数字计算]
            new AttributeEntry(Attributes.STEP_HEIGHT, -0.6, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 跳跃力度[数字计算]
            new AttributeEntry(Attributes.JUMP_STRENGTH, -0.42, 0.58,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.01, ATT.ADD_VALUE),
            // 氧气加成[数字计算]
            new AttributeEntry(Attributes.OXYGEN_BONUS, -0.5, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 重力[数字计算]
            new AttributeEntry(Attributes.GRAVITY, -0.08, 0.2,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 潜行速度[数字计算]
            new AttributeEntry(Attributes.SNEAKING_SPEED, -0.5, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0, ATT.ADD_VALUE),
            // 水下移动效率[数字计算]
            new AttributeEntry(Attributes.WATER_MOVEMENT_EFFICIENCY, -0.2, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE),
            // 水下挖掘速度[数字计算]
            new AttributeEntry(Attributes.SUBMERGED_MINING_SPEED, -0.2, 1.0,
                    Set.of(EquipmentSlotGroup.ARMOR), 0, 0.02, ATT.ADD_VALUE)
    );

    private AttributePool() {}

    public static List<AttributeEntry> selectFor(ItemStack stack, EquipmentSlot targetSlot) {
        List<AttributeEntry> pools = new ArrayList<>();
        var item = stack.getItem();

        if (item instanceof ArmorItem) {
            pools.addAll(ARMOR_ATTRS);
            pools.addAll(ApothicAttributesIntegration.ARMOR_ATTRS);
        } else if (item instanceof SwordItem) {
            // 剑：攻击 + 交互 + 剑专属
            pools.addAll(ATTACK_ATTRS);
            pools.addAll(INTERACT_ATTRS);
            pools.addAll(SWORD_ATTRS);
            pools.addAll(ApothicAttributesIntegration.ATTACK_ATTRS);
            pools.addAll(ApothicAttributesIntegration.INTERACT_ATTRS);
            pools.addAll(ApothicAttributesIntegration.SWORD_ATTRS);
        } else if (item instanceof DiggerItem) {
            // 挖掘工具：交互 + 挖掘专属
            pools.addAll(INTERACT_ATTRS);
            pools.addAll(DIGGER_ATTRS);
            pools.addAll(ApothicAttributesIntegration.INTERACT_ATTRS);
            pools.addAll(ApothicAttributesIntegration.DIGGER_ATTRS);
            // 斧（AxeItem）既是挖掘工具也是武器 → 额外加攻击类
            if (item instanceof AxeItem) {
                pools.addAll(ATTACK_ATTRS);
                pools.addAll(ApothicAttributesIntegration.ATTACK_ATTRS);
            }
        } else {
            // 其他主手物品（盾等）：只用交互
            pools.addAll(INTERACT_ATTRS);
            pools.addAll(ApothicAttributesIntegration.INTERACT_ATTRS);
            // 远程武器（弓/弩/三叉戟）+ 重锤：交互 + 攻击类
            if (item instanceof BowItem || item instanceof CrossbowItem
                    || item instanceof TridentItem || item instanceof MaceItem) {
                pools.addAll(ATTACK_ATTRS);
                pools.addAll(ApothicAttributesIntegration.ATTACK_ATTRS);
                // 远程武器额外加远程专属池（重锤不算远程，跳过）
                if (!(item instanceof MaceItem)) {
                    pools.addAll(ApothicAttributesIntegration.RANGED_ATTRS);
                }
            }
        }

        // 所有类型都加通用池（主池 + 集成）
        pools.addAll(COMMON_ATTRS);
        pools.addAll(ApothicAttributesIntegration.COMMON_ATTRS);
        return pools;
    }

    public enum ATT {
        ADD_VALUE,             // 加算
        ADD_MULTIPLIED_BASE,   // 基础值乘算
        ADD_MULTIPLIED_TOTAL   // 当前总值乘算
    }

    public record AttributeEntry(Holder<Attribute> attribute, double min, double max,
                                 Set<EquipmentSlotGroup> allowedSlots, double step, double minMagnitude, ATT att) {
        public AttributeEntry(Holder<Attribute> attribute, double min, double max,
                              Set<EquipmentSlotGroup> allowedSlots, ATT att) {
            this(attribute, min, max, allowedSlots, 0, 0, att);
        }
        public AttributeEntry(Holder<Attribute> attribute, double min, double max,
                              Set<EquipmentSlotGroup> allowedSlots, double step, ATT att) {
            this(attribute, min, max, allowedSlots, step, 0, att);
        }
    }
}