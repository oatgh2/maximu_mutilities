package app.oatgh.maximum_utilities.menu.screens;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.menu.containers.BackeryFurnanceContainer;
import app.oatgh.maximum_utilities.menu.containers.BowlContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import org.jetbrains.annotations.NotNull;

public class BowlMenuScreen extends AbstractContainerScreen<BowlContainer> {

    private final ResourceLocation GUI = new ResourceLocation(MaximumUtilities.MODID,
            "textures/gui/bowl_crafting.png");

    public BowlMenuScreen(BowlContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 110;

    }

    private void renderWaterStorage(GuiGraphics gui, int relX, int relY) {
        int water = menu.getWaterAmount();
        int waterMax = menu.getWaterAmountMax();
        if (water <= 0 || waterMax <= 0)
            return;

        // proporção entre 0 e 1
        float percent = (float) water / (float) waterMax;
        int fullHeight = 35;
        int filled = (int) (percent * fullHeight);
        int width = 16;
        int height = 35;
        int imgCopyX = 180;
        int imgCopyY = 31;

        int posX = relX + 25 - width;
        int posY = relY + 51 - height;

        int yOffset = fullHeight - filled;
        gui.blit(GUI, posX, posY + yOffset, imgCopyX, imgCopyY + yOffset, width, filled);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int relY = (this.height - this.imageHeight) / 2;
        int relX = (this.width - this.imageWidth) / 2;
        this.inventoryLabelX = imageWidth - 50;

        guiGraphics.blit(GUI, relX, relY, 0, 0, imageWidth + 4, imageHeight);
        renderWaterStorage(guiGraphics, relX, relY);
    }
}