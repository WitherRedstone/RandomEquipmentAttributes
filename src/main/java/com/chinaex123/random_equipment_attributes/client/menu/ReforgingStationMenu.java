package com.chinaex123.random_equipment_attributes.client.menu;

import com.chinaex123.random_equipment_attributes.init.REAItemTags;
import com.chinaex123.random_equipment_attributes.init.REAMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * 重铸台菜单。
 * <p>
 * 包含装备槽与材料槽两个输入槽位，以及玩家物品栏与快捷栏槽位。
 * 支持通过 Shift 点击在输入槽与玩家物品栏之间快速移动物品。
 */
public class ReforgingStationMenu extends AbstractContainerMenu {

    /** 输入槽位数量（装备 + 材料） */
    public static final int SLOT_COUNT = 2;

    /** 装备槽 X 坐标 */
    private static final int EQUIPMENT_SLOT_X = 98;
    /** 装备槽 Y 坐标 */
    private static final int EQUIPMENT_SLOT_Y = 26;
    /** 材料槽 X 坐标 */
    private static final int MATERIAL_SLOT_X  = 62;
    /** 材料槽 Y 坐标 */
    private static final int MATERIAL_SLOT_Y  = 42;

    /** 玩家物品栏起始 X 坐标 */
    private static final int PLAYER_INV_X = 8;
    /** 玩家物品栏起始 Y 坐标 */
    private static final int PLAYER_INV_Y = 84;

    /** 方块物品存储 */
    private final IItemHandler inventory;
    /** 方块位置 */
    private final BlockPos blockPos;

    /**
     * 构造函数（通过网络缓冲区创建）。
     * <p>
     * 用于客户端接收服务端发送的菜单数据时创建菜单实例，
     * 从缓冲区读取方块位置，并创建一个空的物品存储。
     *
     * @param containerId     菜单 ID
     * @param playerInventory 玩家物品栏
     * @param buf             网络缓冲区（包含方块位置数据）
     */
    public ReforgingStationMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory,
                new net.neoforged.neoforge.items.ItemStackHandler(2),
                buf.readBlockPos());
    }

    /**
     * 构造函数（服务端创建）。
     * <p>
     * 添加装备槽与材料槽，以及玩家物品栏与快捷栏槽位。
     *
     * @param containerId     菜单 ID
     * @param playerInventory 玩家物品栏
     * @param blockInventory  方块物品存储
     * @param blockPos        方块位置
     */
    public ReforgingStationMenu(int containerId, Inventory playerInventory, IItemHandler blockInventory, BlockPos blockPos) {
        super(REAMenuTypes.REFORGING_STATION.get(), containerId);
        this.inventory = blockInventory;
        this.blockPos  = blockPos;

        this.addSlot(new InputSlot(blockInventory, 0, EQUIPMENT_SLOT_X, EQUIPMENT_SLOT_Y));
        this.addSlot(new MaterialSlot(blockInventory, 1, MATERIAL_SLOT_X, MATERIAL_SLOT_Y));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        PLAYER_INV_X + col * 18,
                        PLAYER_INV_Y + row * 18
                ));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(
                    playerInventory,
                    col,
                    PLAYER_INV_X + col * 18,
                    PLAYER_INV_Y + 3 * 18 + 4
            ));
        }
    }

    /**
     * 获取方块位置。
     *
     * @return 方块位置
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }

    /**
     * 检查玩家是否仍然可以访问此菜单。
     * <p>
     * 该菜单始终有效。
     *
     * @param player 要检查的玩家
     * @return 始终返回 true
     */
    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    /**
     * 快速移动物品（Shift + 点击）。
     * <p>
     * 处理逻辑：
     * - 从输入槽移到玩家物品栏（index 小于 SLOT_COUNT）；
     * - 从玩家物品栏移到输入槽（index 不小于 SLOT_COUNT）。
     *
     * @param player 执行操作的玩家
     * @param index  被点击的槽位索引
     * @return 移动后的物品堆栈（空表示移动失败）
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            result = itemStack.copy();

            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(itemStack, SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack, 0, SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    /**
     * 装备输入槽。
     * <p>
     * 仅允许放入可附魔的物品。
     */
    public static class InputSlot extends SlotItemHandler {

        /**
         * 构造装备输入槽。
         *
         * @param itemHandler 物品处理器
         * @param index       槽位索引
         * @param xPosition   槽位 X 坐标
         * @param yPosition   槽位 Y 坐标
         */
        public InputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        /**
         * 检查是否允许放入该物品。
         *
         * @param stack 待放入的物品堆
         * @return 可附魔的物品返回 true
         */
        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.isEnchantable();
        }
    }

    /**
     * 材料输入槽。
     * <p>
     * 仅允许放入重铸材料标签内的物品。
     */
    public static class MaterialSlot extends SlotItemHandler {

        /**
         * 构造材料输入槽。
         *
         * @param itemHandler 物品处理器
         * @param index       槽位索引
         * @param xPosition   槽位 X 坐标
         * @param yPosition   槽位 Y 坐标
         */
        public MaterialSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        /**
         * 检查是否允许放入该物品。
         *
         * @param stack 待放入的物品堆
         * @return 属于重铸材料标签的物品返回 true
         */
        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(REAItemTags.REFORGE_MATERIALS);
        }
    }
}