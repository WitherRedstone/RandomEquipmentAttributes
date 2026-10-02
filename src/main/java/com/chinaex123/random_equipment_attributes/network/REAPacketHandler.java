package com.chinaex123.random_equipment_attributes.network;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.blockentity.ReforgingStationBlockEntity;
import com.chinaex123.random_equipment_attributes.event.AttributeGenerator;
import com.chinaex123.random_equipment_attributes.event.SlotHelper;
import com.chinaex123.random_equipment_attributes.init.REAItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 重铸台网络包处理器。
 * <p>
 * 注册重铸请求数据包并处理其服务端逻辑：
 * 校验装备与材料槽，消耗一份材料后重新生成装备的随机属性，
 * 并同步方块状态与播放音效。
 */
public final class REAPacketHandler {

    /**
     * 私有构造函数，防止实例化。
     * <p>
     * 该类只提供静态方法，不需要实例。
     */
    private REAPacketHandler() {}

    /**
     * 注册网络数据包。
     * <p>
     * 在事件总线上监听负载处理器注册事件，
     * 将重铸请求数据包注册为客户端到服务端的通信。
     *
     * @param eventBus 模组事件总线
     */
    public static void register(IEventBus eventBus) {
        eventBus.addListener(RegisterPayloadHandlersEvent.class, event ->
                event.registrar(RandomEquipmentAttributes.MODID)
                        .playToServer(ReforgePacket.TYPE, ReforgePacket.CODEC, REAPacketHandler::handleReforge)
        );
    }

    /**
     * 发送重铸请求。
     * <p>
     * 若位置非空，则向服务端发送携带该位置的重铸请求数据包。
     *
     * @param pos 重铸台方块位置
     */
    public static void sendReforgeRequest(BlockPos pos) {
        if (pos != null) {
            PacketDistributor.sendToServer(new ReforgePacket(pos));
        }
    }

    /**
     * 处理重铸请求。
     * <p>
     * 在主线程中校验目标方块实体、装备槽与材料槽：
     * 装备为空、材料为空、装备尚无随机属性或材料不属于重铸材料标签时，
     * 向玩家发送对应提示并中止；校验通过后消耗一份材料并重新生成随机属性，
     * 生成失败时提示失败，成功则标记方块实体变更、同步方块更新并播放铁砧音效。
     *
     * @param packet  重铸请求数据包
     * @param context 上下文
     */
    private static void handleReforge(ReforgePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var serverPlayer = context.player();
            BlockPos pos = packet.blockPos();
            var level = serverPlayer.level();
            var be = level.getBlockEntity(pos);
            if (!(be instanceof ReforgingStationBlockEntity reforge)) return;

            var item = reforge.inventory.getStackInSlot(0);
            var mat  = reforge.inventory.getStackInSlot(1);

            if (item.isEmpty() || mat.isEmpty()) {
                serverPlayer.sendSystemMessage(Component.translatable("msg.rea.reforge.need_item_and_material"));
                return;
            }
            if (!SlotHelper.hasRandomAttrs(item)) {
                serverPlayer.sendSystemMessage(Component.translatable("msg.rea.reforge.need_equipped_first"));
                return;
            }
            if (!mat.is(REAItemTags.REFORGE_MATERIALS)) {
                serverPlayer.sendSystemMessage(Component.translatable("msg.rea.reforge.bad_material"));
                return;
            }

            mat.shrink(1);
            if (mat.isEmpty()) {
                reforge.inventory.setStackInSlot(1, net.minecraft.world.item.ItemStack.EMPTY);
            } else {
                reforge.inventory.setStackInSlot(1, mat);
            }

            boolean success = AttributeGenerator.regenerate(item);
            if (!success) {
                serverPlayer.sendSystemMessage(Component.translatable("msg.rea.reforge.failed"));
                return;
            }

            reforge.setChanged();
            level.sendBlockUpdated(pos, be.getBlockState(), be.getBlockState(), 2);

            level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS, 0.5F, 1.0F);
        });
    }
}