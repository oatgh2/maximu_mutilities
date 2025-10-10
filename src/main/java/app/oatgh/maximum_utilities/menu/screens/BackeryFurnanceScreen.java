package app.oatgh.maximum_utilities.menu.screens;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.menu.containers.BackeryFurnanceContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import org.jetbrains.annotations.NotNull;

public class BackeryFurnanceScreen extends AbstractContainerScreen<BackeryFurnanceContainer> {

    private final ResourceLocation GUI = new ResourceLocation(MaximumUtilities.MODID,
            "textures/gui/backery.png");

    public BackeryFurnanceScreen(BackeryFurnanceContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 110;

    }

    private void renderEnergyStorage(GuiGraphics gui, int relX, int relY) {
        int energy = menu.getEnergyProgress();
        int energyMax = menu.getEnergyProgressMax();
        if (energy <= 0 || energyMax <= 0)
            return;

        // proporção entre 0 e 1
        float percent = (float) energy / (float) energyMax;
        int fullHeight = 35;
        int filled = (int) (percent * fullHeight);
        int width = 16;
        int height = 35;
        int imgCopyX = 180;
        int imgCopyY = 31;

        int posX = relX + 45 - width;
        int posY = relY + 65 - height;

        int yOffset = fullHeight - filled;
        gui.blit(GUI, posX, posY + yOffset, imgCopyX, imgCopyY + yOffset, width, filled);
    }

    private void renderFuelBurn(GuiGraphics gui, int relX, int relY) {
        int fuelBurnTime = menu.getFuelBurnTime();
        int fuelBurnTotal = menu.getTotalFuelBurnTime();
        if (fuelBurnTime == 0 || fuelBurnTotal == 0)
            return;
        float percent = (float) fuelBurnTime / (float) fuelBurnTotal;
        int fullHeight = 14;
        int filled = (int) (percent * fullHeight);

        int width = 14;
        int height = 14;
        int imgCopyX = 180;
        int imgCopyY = 16;

        int posX = relX + 25 - width;
        int posY = relY + 45 - height;
        int yOffset = fullHeight - filled;
        gui.blit(GUI, posX, posY, imgCopyX, imgCopyY, width, height);
        // gui.blit(GUI, posX, posY + yOffset, imgCopyX, imgCopyY + yOffset, width,
        // filled);
        posX = relX + 25 - width;
        posY = relY + 45 - height;
        gui.blit(GUI, posX, posY, posX, posY - 15, width, filled);
    }

    public void renderBurnProgress(GuiGraphics gui, int relX, int relY) {

        int progress = menu.getItemBurnTime();
        int maxProgress = menu.getItemBurnTimeMax();
        if(progress == 0 || maxProgress == 0) return;

        float progressPercent =(float)progress / (float)maxProgress;
        int height = 17;
        int width = 26;
        int filled = (int)(progressPercent * width);

        int xPos = relX + 108 - width;
        int yPos = relY + 41 - height;

        gui.blit(GUI, xPos, yPos, 180, 0, filled, height);
        // gui.blit(GUI, )
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int relY = (this.height - this.imageHeight) / 2;
        int relX = (this.width - this.imageWidth) / 2;
        this.inventoryLabelX = imageWidth - 50;

        guiGraphics.blit(GUI, relX, relY, 0, 0, imageWidth + 4, imageHeight);
        renderEnergyStorage(guiGraphics, relX, relY);
        renderFuelBurn(guiGraphics, relX, relY);
        renderBurnProgress(guiGraphics, relX, relY);
    }
}