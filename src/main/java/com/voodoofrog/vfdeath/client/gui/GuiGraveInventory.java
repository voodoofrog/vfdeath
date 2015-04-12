package com.voodoofrog.vfdeath.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.inventory.ContainerGrave;
import com.voodoofrog.vfdeath.inventory.InventoryGrave;
import com.voodoofrog.vfdeath.misc.ModInfo;

@SideOnly(Side.CLIENT)
public class GuiGraveInventory extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID, "textures/gui/container/gui_grave.png");
	private ContainerGrave graveContainer;

	public GuiGraveInventory(InventoryPlayer playerInventory, InventoryGrave graveInventory)
	{
		super(new ContainerGrave(playerInventory, graveInventory, Minecraft.getMinecraft().thePlayer));

		this.graveContainer = (ContainerGrave)this.inventorySlots;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// this.drawTexturedModalRect(this.guiLeft + 41, this.guiTop + 52, 0, this.ySize + (this.getChargedAnkhCount() > 0 ? 0 : 16), 110, 16);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

		if (keyCode == 1 || keyCode == ConfigHandler.GRAVE_KEY.getKeyCode())
		{
			this.mc.thePlayer.closeScreen();
		}
	}
}
