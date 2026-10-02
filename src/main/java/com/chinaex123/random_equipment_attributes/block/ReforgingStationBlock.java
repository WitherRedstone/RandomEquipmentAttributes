package com.chinaex123.random_equipment_attributes.block;

import com.chinaex123.random_equipment_attributes.blockentity.ReforgingStationBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * 重铸台方块。
 * <p>
 * 继承自带方块实体的方块，具有水平朝向属性，
 * 放置时正面朝向玩家，右键打开对应的重铸台界面。
 */
public class ReforgingStationBlock extends BaseEntityBlock {

    /** 方块编解码器 */
    public static final MapCodec<ReforgingStationBlock> CODEC = simpleCodec(ReforgingStationBlock::new);

    /** 水平朝向属性 */
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    /**
     * 构造重铸台方块。
     *
     * @param properties 方块属性
     */
    public ReforgingStationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * 定义方块状态。
     * <p>
     * 添加水平朝向属性。
     *
     * @param builder 状态定义构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /**
     * 确定方块放置时的状态。
     * <p>
     * 朝向取玩家水平视线方向的反方向，使正面朝向玩家。
     *
     * @param context 放置上下文
     * @return 带有最终朝向的方块状态
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * 处理玩家右键方块且手中无物品的交互。
     * <p>
     * 仅在服务端执行：若目标方块实体为重铸台方块实体，
     * 则通过缓冲区传入方块位置并打开界面。
     *
     * @param state  方块状态
     * @param level  世界实例
     * @param pos    方块位置
     * @param player 交互的玩家
     * @param hit    方块点击结果
     * @return 交互结果
     */
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ReforgingStationBlockEntity reforgeEntity) {
                serverPlayer.openMenu(reforgeEntity, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 获取方块编解码器。
     *
     * @return 方块编解码器
     */
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /**
     * 创建方块实体实例。
     *
     * @param pos   方块位置
     * @param state 方块状态
     * @return 重铸台方块实体
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReforgingStationBlockEntity(pos, state);
    }

    /**
     * 获取方块的渲染形状。
     *
     * @param state 方块状态
     * @return 渲染形状，此处返回 MODEL 表示使用模型渲染
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    /**
     * 获取方块实体的刻更新器。
     * <p>
     * 该方块无需周期性更新，返回 null。
     *
     * @param level 世界实例
     * @param state 方块状态
     * @param type  方块实体类型
     * @param <T>   方块实体类型参数
     * @return 始终返回 null
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}