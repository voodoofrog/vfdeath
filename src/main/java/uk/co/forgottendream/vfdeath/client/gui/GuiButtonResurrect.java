package uk.co.forgottendream.vfdeath.client.gui;

import org.lwjgl.opengl.GL11;

import uk.co.forgottendream.vfdeath.ModInfo;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonResurrect extends GuiButton {

	protected static final ResourceLocation buttonTextures = new ResourceLocation(ModInfo.ID.toLowerCase(), ConfigHandler.GUI_PATH + "widgets.png");
	
	public GuiButtonResurrect(int id, int x, int y) {
		super(id, x, y, 16, 16, "");
        this.width = 16;
        this.height = 16;
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = "";
	}
	
	@Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            par1Minecraft.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int k = this.getHoverState(this.field_82253_i);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0 + k * 16, this.width, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width, 0 + k * 16, this.width / 2, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            int l = 14737632;

            if (!this.enabled)
            {
                l = -6250336;
            }
            else if (this.field_82253_i)
            {
                l = 16777120;
            }
        }
    }

}
