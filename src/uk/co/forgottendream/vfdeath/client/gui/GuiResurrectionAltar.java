package uk.co.forgottendream.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.forgottendream.vfdeath.ModInfo;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import uk.co.forgottendream.vfdeath.inventory.ContainerResurrectionAltar;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiResurrectionAltar extends GuiContainer{

	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), ConfigHandler.GUI_CONTAINER_PATH + "gui_altar.png");
	
	public GuiResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar) {
		super(new ContainerResurrectionAltar(playerInventory, altar));
		
		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
