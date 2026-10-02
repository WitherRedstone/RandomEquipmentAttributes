package com.chinaex123.random_equipment_attributes.network;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

/**
 * 重铸请求数据包（客户端→服务端）。
 * <p>
 * 客户端点击重铸按钮后发送此数据包，携带重铸台方块位置，
 * 供服务端定位方块实体并执行重铸逻辑。
 *
 * @param blockPos 重铸台方块位置
 */
public record ReforgePacket(BlockPos blockPos) implements CustomPacketPayload {

    /** 网络包类型标识 */
    public static final Type<ReforgePacket> TYPE = new Type<>(RandomEquipmentAttributes.id("reforge_request")
    );

    /** 网络包编解码器，仅携带方块位置字段 */
    public static final StreamCodec<FriendlyByteBuf, ReforgePacket> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ReforgePacket::blockPos,
                    ReforgePacket::new
            );

    /**
     * 获取网络包类型。
     *
     * @return 网络包类型标识
     */
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}