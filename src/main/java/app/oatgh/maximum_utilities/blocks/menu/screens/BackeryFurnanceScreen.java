package app.oatgh.maximum_utilities.blocks.menu.screens;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.blocks.menu.containers.BackeryFurnanceContainer;
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


    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int relX = (this.height - this.imageHeight) / 2;
        int relY = (this.width - this.imageWidth) / 2;

        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
