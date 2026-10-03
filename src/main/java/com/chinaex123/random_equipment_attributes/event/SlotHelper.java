package com.chinaex123.random_equipment_attributes.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Set;

/**
 * 装备槽位与自定义数据辅助工具。
 * <p>
 * 提供装备槽位与槽位组之间的转换、物品自定义数据的读写，
 * 以及随机属性数据的判断、清除与槽位组匹配等静态方法。
 */
public final class SlotHelper {

    /** 自定义数据中随机属性列表的键名 */
    public static final String NBT_KEY_RANDOM_ATTRS = "rae_attributes";
    /** 自定义数据中属性标识的键名 */
    public static final String NBT_KEY_ATTR_ID = "attribute_id";
    /** 自定义数据中数值的键名 */
    public static final String NBT_KEY_AMOUNT = "amount";
    /** 自定义数据中运算方式的键名 */
    public static final String NBT_KEY_OPERATION = "operation";
    /** 自定义数据中装备槽位组的键名 */
    public static final String NBT_KEY_SLOT_GROUP = "rae_slot_group";
    /** 自定义数据中重铸次数的键名 */
    public static final String NBT_KEY_REFORGE_COUNT = "rae_reforge_count";

    /**
     * 私有构造函数，防止实例化。
     * <p>
     * 该类只提供静态方法，不需要实例。
     */
    private SlotHelper() {}

    /**
     * 将装备槽位转换为对应的槽位组。
     *
     * @param slot 装备槽位
     * @return 对应的装备槽位组
     */
    public static EquipmentSlotGroup slotToGroup(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> EquipmentSlotGroup.MAINHAND;
            case OFFHAND  -> EquipmentSlotGroup.OFFHAND;
            case HEAD     -> EquipmentSlotGroup.HEAD;
            case CHEST    -> EquipmentSlotGroup.CHEST;
            case LEGS     -> EquipmentSlotGroup.LEGS;
            case FEET     -> EquipmentSlotGroup.FEET;
            case BODY     -> EquipmentSlotGroup.BODY;
        };
    }

    /**
     * 获取物品自定义数据的可写副本。
     * <p>
     * 若物品无自定义数据组件，则以空数据为起点返回副本。
     *
     * @param stack 物品堆
     * @return 自定义数据标签的副本
     */
    public static CompoundTag getCustomDataTag(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.copyTag();
    }

    /**
     * 判断物品是否携带随机属性数据。
     *
     * @param stack 物品堆
     * @return 携带随机属性数据返回 true
     */
    public static boolean hasRandomAttrs(ItemStack stack) {
        CompoundTag tag = getCustomDataTag(stack);
        return tag.contains(NBT_KEY_RANDOM_ATTRS);
    }

    /**
     * 清除物品的随机属性数据。
     * <p>
     * 移除随机属性列表与装备槽位组两个字段，并将结果写回物品。
     *
     * @param stack 物品堆
     */
    public static void clearRandomAttrs(ItemStack stack) {
        CompoundTag tag = getCustomDataTag(stack);
        tag.remove(NBT_KEY_RANDOM_ATTRS);
        tag.remove(NBT_KEY_SLOT_GROUP);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /**
     * 读取物品记录的装备槽位组。
     * <p>
     * 字段缺失或名称非法时回退为主手槽位组。
     *
     * @param stack 物品堆
     * @return 物品记录的装备槽位组
     */
    public static EquipmentSlotGroup readSlotGroup(ItemStack stack) {
        CompoundTag tag = getCustomDataTag(stack);
        if (!tag.contains(NBT_KEY_SLOT_GROUP)) return EquipmentSlotGroup.MAINHAND;
        String name = tag.getString(NBT_KEY_SLOT_GROUP).toUpperCase();
        try {
            return EquipmentSlotGroup.valueOf(name);
        } catch (IllegalArgumentException e) {
            return EquipmentSlotGroup.MAINHAND;
        }
    }

    /**
     * 读取物品的重铸次数。
     *
     * @param stack 物品堆
     * @return 重铸次数，未记录时返回 0
     */
    public static int readReforgeCount(ItemStack stack) {
        CompoundTag tag = getCustomDataTag(stack);
        return tag.getInt(NBT_KEY_REFORGE_COUNT);
    }

    /**
     * 将物品的重铸次数递增 1 并写回。
     *
     * @param stack 物品堆
     */
    public static void incrementReforgeCount(ItemStack stack) {
        CompoundTag tag = getCustomDataTag(stack);
        int count = tag.getInt(NBT_KEY_REFORGE_COUNT);
        tag.putInt(NBT_KEY_REFORGE_COUNT, count + 1);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /**
     * 判断允许的槽位组是否与物品的槽位组集合匹配。
     * <p>
     * 先做直接包含判断，再逐一检查允许的槽位组是否覆盖物品的某个槽位组。
     *
     * @param allowedSlot 允许的槽位组
     * @param itemSlots   物品的槽位组集合
     * @return 匹配返回 true
     */
    public static boolean groupMatches(EquipmentSlotGroup allowedSlot, Set<EquipmentSlotGroup> itemSlots) {
        if (itemSlots.contains(allowedSlot)) return true;
        for (EquipmentSlotGroup itemSlot : itemSlots) {
            if (groupCovers(allowedSlot, itemSlot)) return true;
        }
        return false;
    }

    /**
     * 判断允许的槽位组是否覆盖指定的物品槽位组。
     * <p>
     * ANY 覆盖全部；HAND 覆盖主手与副手；ARMOR 覆盖头、胸、腿、脚；
     * 其余情况仅在同一槽位组时视为覆盖。
     *
     * @param allowedSlot 允许的槽位组
     * @param itemSlot    物品的槽位组
     * @return 覆盖返回 true
     */
    public static boolean groupCovers(EquipmentSlotGroup allowedSlot, EquipmentSlotGroup itemSlot) {
        if (allowedSlot.equals(itemSlot)) return true;
        return switch (allowedSlot) {
            case ANY -> true;
            case HAND -> itemSlot == EquipmentSlotGroup.MAINHAND
                    || itemSlot == EquipmentSlotGroup.OFFHAND;
            case ARMOR -> itemSlot == EquipmentSlotGroup.HEAD
                    || itemSlot == EquipmentSlotGroup.CHEST
                    || itemSlot == EquipmentSlotGroup.LEGS
                    || itemSlot == EquipmentSlotGroup.FEET;
            default -> false;
        };
    }
}