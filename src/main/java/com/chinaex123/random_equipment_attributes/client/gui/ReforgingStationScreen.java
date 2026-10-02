package com.chinaex123.random_equipment_attributes.client.gui;

import com.chinaex123.random_equipment_attributes.RandomEquipmentAttributes;
import com.chinaex123.random_equipment_attributes.client.menu.ReforgingStationMenu;
import com.chinaex123.random_equipment_attributes.network.REAPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * 重铸台界面。
 * <p>
 * 继承自容器界面，绘制重铸台背景纹理，
 * 在界面中提供一个自定义样式的重铸按钮，
 * 点击后发送重铸请求。
 */
public class ReforgingStationScreen extends AbstractContainerScreen<ReforgingStationMenu> {

    /** 界面背景纹理位置 */
    private static final ResourceLocation TEXTURE = RandomEquipmentAttributes.id("textures/gui/reforging_station.png");

    /**
     * 构造重铸台界面。
     *
     * @param menu            重铸台菜单
     * @param playerInventory 玩家物品栏
     * @param title           界面标题
     */
    public ReforgingStationScreen(ReforgingStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 175;
        this.imageHeight = 165;
    }

    /**
     * 初始化界面组件。
     * <p>
     * 除父类初始化外，在指定位置添加一个自定义样式的重铸按钮，
     * 点击时发送携带方块位置的重铸请求。
     */
    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new CustomButton(
                this.leftPos + 98, this.topPos + 58, 16, 16,
                Component.literal("🔨"),
                button -> REAPacketHandler.sendReforgeRequest(this.menu.getBlockPos())
        ));
    }

    /**
     * 自定义样式按钮。
     * <p>
     * 以纯色填充绘制背景与边框，悬停时背景与文字颜色发生变化。
     */
    private class CustomButton extends Button {

        /** 按钮常态背景色 */
        private static final int NORMAL_COLOR = new Color(90, 94, 120).getRGB();
        /** 按钮悬停背景色 */
        private static final int HOVER_COLOR  = new Color(122, 126, 152).getRGB();
        /** 按钮文字颜色 */
        private static final int TEXT_COLOR   = new Color(224, 228, 248).getRGB();
        /** 按钮边框颜色 */
        private static final int BORDER_COLOR = new Color(58, 62, 88).getRGB();

        /**
         * 构造自定义按钮。
         *
         * @param x       按钮 X 坐标
         * @param y       按钮 Y 坐标
         * @param width   按钮宽度
         * @param height  按钮高度
         * @param message 按钮文本
         * @param onPress 点击回调
         */
        public CustomButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
        }

        /**
         * 渲染按钮。
         * <p>
         * 先以背景色填充整体，再绘制四周边框，
         * 最后在按钮中心绘制文本，悬停时使用高亮颜色。
         *
         * @param guiGraphics 图形上下文
         * @param mouseX      鼠标 X 坐标
         * @param mouseY      鼠标 Y 坐标
         * @param partialTick 部分 tick 插值
         */
        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int bg = isHovered ? HOVER_COLOR : NORMAL_COLOR;
            guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, bg);
            guiGraphics.fill(getX(), getY(), getX() + 1, getY() + height, BORDER_COLOR);
            guiGraphics.fill(getX() + width - 1, getY(), getX() + width, getY() + height, BORDER_COLOR);
            guiGraphics.fill(getX(), getY(), getX() + width, getY() + 1, BORDER_COLOR);
            guiGraphics.fill(getX(), getY() + height - 1, getX() + width, getY() + height, BORDER_COLOR);
            int tc = isHovered ? 0xFFFFFF : TEXT_COLOR;
            guiGraphics.drawCenteredString(font, getMessage(),
                    getX() + width / 2, getY() + (height - 8) / 2, tc);
        }
    }

    /**
     * 渲染界面背景。
     * <p>
     * 设置纹理着色器与颜色后，按界面尺寸将背景纹理绘制到居中位置。
     *
     * @param guiGraphics 图形上下文
     * @param partialTick 部分 tick 插值
     * @param mouseX      鼠标 X 坐标
     * @param mouseY      鼠标 Y 坐标
     */
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    /**
     * 主渲染方法。
     * <p>
     * 先调用父类渲染基础内容，再渲染鼠标悬停提示。
     *
     * @param guiGraphics 图形上下文
     * @param mouseX      鼠标 X 坐标
     * @param mouseY      鼠标 Y 坐标
     * @param partialTick 部分 tick 插值
     */
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    /**
     * 渲染界面标签。
     * <p>
     * 在顶部居中绘制标题，在左侧绘制玩家物品栏标题。
     *
     * @param guiGraphics 图形上下文
     * @param mouseX      鼠标 X 坐标
     * @param mouseY      鼠标 Y 坐标
     */
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, (imageWidth - this.font.width(this.title)) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 72, 4210752, false);
    }
}