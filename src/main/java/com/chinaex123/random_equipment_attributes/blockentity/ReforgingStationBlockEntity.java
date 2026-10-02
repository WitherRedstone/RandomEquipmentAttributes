package com.chinaex123.random_equipment_attributes.blockentity;

import com.chinaex123.random_equipment_attributes.client.menu.ReforgingStationMenu;
import com.chinaex123.random_equipment_attributes.init.REABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 重铸台方块实体。
 * <p>
 * 持有 2 格物品存储，作为菜单提供者对外打开重铸台界面，
 * 并将物品数据以 NBT 形式持久化。
 */
public class ReforgingStationBlockEntity extends BlockEntity implements MenuProvider {

    /** NBT 中记录物品栏数据的键名 */
    public static final String NBT_INVENTORY = "Inventory";

    /** 重铸台的物品存储（2 格） */
    public final ItemStackHandler inventory = new ItemStackHandler(2) {

        /**
         * 槽位内容变化时标记方块实体已变更。
         *
         * @param slot 发生变化的槽位索引
         */
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    /**
     * 构造重铸台方块实体。
     *
     * @param pos   方块位置
     * @param state 方块状态
     */
    public ReforgingStationBlockEntity(BlockPos pos, BlockState state) {
        super(REABlockEntities.REFORGING_STATION.get(), pos, state);
    }

    /**
     * 以指定方块实体类型构造重铸台方块实体。
     *
     * @param type  方块实体类型
     * @param pos   方块位置
     * @param state 方块状态
     */
    public ReforgingStationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * 获取界面的显示名称。
     *
     * @return 显示名称组件
     */
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.random_equipment_attributes.reforging_station");
    }

    /**
     * 创建容器菜单。
     * <p>
     * 将物品存储与方块位置传入菜单，以便界面读写物品并定位方块。
     *
     * @param containerId     菜单 ID
     * @param playerInventory 玩家物品栏
     * @param player          打开菜单的玩家
     * @return 重铸台菜单实例
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ReforgingStationMenu(
                containerId,
                playerInventory,
                inventory,
                this.worldPosition
        );
    }

    /**
     * 保存方块实体的额外数据。
     * <p>
     * 将物品栏序列化后写入 NBT。
     *
     * @param tag        要写入的 NBT 标签
     * @param registries 注册表提供者
     */
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(NBT_INVENTORY, inventory.serializeNBT(registries));
    }

    /**
     * 从 NBT 加载方块实体的额外数据。
     * <p>
     * 若存在物品栏字段则读取并反序列化。
     *
     * @param tag        包含数据的 NBT 标签
     * @param registries 注册表提供者
     */
    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(NBT_INVENTORY)) {
            inventory.deserializeNBT(registries, tag.getCompound(NBT_INVENTORY));
        }
    }
}