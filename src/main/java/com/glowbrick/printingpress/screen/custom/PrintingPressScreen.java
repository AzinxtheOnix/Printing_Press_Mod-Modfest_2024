package com.glowbrick.printingpress.screen.custom;

import com.glowbrick.printingpress.PrintingPress;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class PrintingPressScreen extends AbstractContainerScreen<PrintingPressMenu> {
    private static final ResourceLocation GUI_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/printing_press/printingpress_gui.png");
    private static final ResourceLocation ARROW_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/arrow_progress.png");
    private static final ResourceLocation INK_LEVEL_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/printing_press/ink_progress.png");
        private static final ResourceLocation MAGIC_INK_LEVEL_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/printing_press/magic_ink_progress.png");
    private static final ResourceLocation PRESS_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/printing_press/press.png");
    private static final ResourceLocation DUMP_BUTTON =
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/printing_press/dump_button.png");


    @Override
    protected void init() {
        super.init();
        
        ImageButton dumpButton = new ImageButton(this.leftPos + 25, this.topPos + 48, 8, 10, new WidgetSprites(DUMP_BUTTON, DUMP_BUTTON), e -> {
            /**if (true) {
				PacketDistributor.sendToServer(new CustomPacketPayload());      // new TestGUIButtonMessage(0, x, y, z)
				TestGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}**/

		}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};

		this.addRenderableWidget(dumpButton);

        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    public PrintingPressScreen(PrintingPressMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pGuiGraphics, x, y);
        renderInkLevel(pGuiGraphics, x, y);
        renderPressAnimation(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(ARROW_TEXTURE, x+73, y+35, 0, 0, menu.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    private void renderInkLevel(GuiGraphics guiGraphics, int x, int y) {
        int inkMode = menu.getInkMode();
        if(inkMode == 1) {
            guiGraphics.blit(INK_LEVEL_TEXTURE, x+7, y+6+52 - menu.getScaledInkProgress(), 0, 52 - menu.getScaledInkProgress(), 18, menu.getScaledInkProgress(), 18, 52);
        } else if(inkMode == 2) {
            guiGraphics.blit(MAGIC_INK_LEVEL_TEXTURE, x+7, y+6+52 - menu.getScaledInkProgress(), 0, 52 - menu.getScaledInkProgress(), 18, menu.getScaledInkProgress(), 18, 52);
        }

    }

    private void renderPressAnimation(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(PRESS_TEXTURE, x+48, y+9, 0, 0, 28, menu.getScaledPressProgress(), 28, 15);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
