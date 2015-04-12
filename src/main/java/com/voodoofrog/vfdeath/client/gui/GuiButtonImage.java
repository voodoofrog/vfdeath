package com.voodoofrog.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import com.voodoofrog.vfdeath.misc.ModInfo;

public class GuiButtonImage extends GuiButton
{
	protected static final ResourceLocation buttonTextures = new ResourceLocation(ModInfo.ID, "textures/gui/widgets2.png");
	private int iconX;
	private int iconY;
	private int iconWidth;
	private int iconHeight;
	private int iconOffset;

	public GuiButtonImage(int id, int x, int y, int iconX, int iconY, int iconWidth, int iconHeight, int iconOffset)
	{
		super(id, x, y, 16, 16, "");
		this.width = 16;
		this.height = 16;
		this.enabled = true;
		this.visible = true;
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.iconX = iconX;
		this.iconY = iconY;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		this.iconOffset = iconOffset;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY)
	{
		if (this.visible)
		{
			minecraft.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
					&& mouseY < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0 + k * 16, this.width, this.height);
			this.drawTexturedModalRect(this.xPosition + this.iconOffset, this.yPosition + this.iconOffset, this.iconX, this.iconY, this.iconWidth,
					this.iconHeight);
		}
	}
}
