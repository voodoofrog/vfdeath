package com.voodoofrog.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.config.ConfigHandler;

public class GuiButtonResurrect extends GuiButton
{
	protected static final ResourceLocation buttonTextures = new ResourceLocation(ModInfo.ID.toLowerCase(), ConfigHandler.GUI_PATH
			+ "widgets.png");

	public GuiButtonResurrect(int id, int x, int y)
	{
		super(id, x, y, 16, 16, "");
		this.width = 16;
		this.height = 16;
		this.enabled = true;
		this.visible = true;
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.displayString = "";
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
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width, 0 + k * 16, this.width / 2,
					this.height);
			this.mouseDragged(minecraft, mouseX, mouseY);
			int l = 14737632;

			if (!this.enabled)
			{
				l = -6250336;
			}
			else if (this.hovered)
			{
				l = 16777120;
			}
		}
	}
}
