package com.glowbrick.printingpress.screen.custom;

import com.glowbrick.printingpress.PrintingPress;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PrintingPressScreen extends AbstractContainerScreen<PrintingPressMenu> {
    private static final ResourceLocation GUI_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/printing_press/printingpress_gui.png");
    private static final ResourceLocation ARROW_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "textures/gui/arrow_progress.png");
    
    @Override
    protected void init() {
        super.init();

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
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(ARROW_TEXTURE, x+73, y+35, 176, 0, 8, menu.getScaledArrowProgress());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}