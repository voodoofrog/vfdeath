package com.voodoofrog.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.inventory.ContainerCoffin;
import com.voodoofrog.vfdeath.misc.ModInfo;

@SideOnly(Side.CLIENT)
public class GuiCoffin extends GuiContainer
{
	private static final ResourceLocation COFFIN_GUI_TEXTURE = new ResourceLocation(ModInfo.ID, "textures/gui/container/gui_coffin.png");
	private IInventory playerInventory;
	private IInventory coffinInventory;

	public GuiCoffin(IInventory playerInventory, IInventory coffinInventory)
	{
		super(new ContainerCoffin(playerInventory, coffinInventory, Minecraft.getMinecraft().thePlayer));
		this.playerInventory = playerInventory;
		this.coffinInventory = coffinInventory;
		this.allowUserInput = false;
		// this.inventoryRows = lowerInv.getSizeInventory() / 9;
		this.ySize = 222;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRendererObj.drawString(this.coffinInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		// this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(COFFIN_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		// this.drawTexturedModalRect(k, l + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}