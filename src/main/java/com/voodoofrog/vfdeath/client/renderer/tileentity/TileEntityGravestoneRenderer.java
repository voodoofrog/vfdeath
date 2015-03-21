package com.voodoofrog.vfdeath.client.renderer.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.voodoofrog.vfdeath.client.model.ModelGravestone;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

@SideOnly(Side.CLIENT)
public class TileEntityGravestoneRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation texture = new ResourceLocation("textures/blocks/stone.png");
	private final ModelGravestone model = new ModelGravestone();

	public void render(TileEntityGravestone teGravestone, double posX, double posY, double posZ, float p_180541_8_, int p_180541_9_)
	{
		int meta = 0;

		if (teGravestone.hasWorldObj())
		{
			meta = teGravestone.getBlockMetadata();
		}

		Block block = teGravestone.getBlockType();
		GlStateManager.pushMatrix();
		float f1 = 1.0F;
		float f3;

		GlStateManager.translate((float)posX + 0.5F, (float)posY + 0.5, (float)posZ + 0.5F);

		short short1 = 0;

		if (meta == 2)
		{
			short1 = 180;
		}

		if (meta == 3)
		{
			short1 = 0;
		}

		if (meta == 4)
		{
			short1 = -90;
		}

		if (meta == 5)
		{
			short1 = 90;
		}

		GlStateManager.rotate((float)short1, 0.0F, 1.0F, 0.0F);

		if (p_180541_9_ >= 0)
		{
			this.bindTexture(DESTROY_STAGES[p_180541_9_]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 2.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}
		else
		{
			this.bindTexture(texture);
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		GlStateManager.scale(f1, -f1, -f1);
		this.model.renderAll();
		GlStateManager.popMatrix();
		FontRenderer fontrenderer = this.getFontRenderer();
		f3 = 0.015625F * f1;
		GlStateManager.translate(0.0F, 0.125F, 0.44F * f1);
		GlStateManager.scale(f3, -f3, f3);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GlStateManager.depthMask(false);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		byte b0 = 0;

		if (p_180541_9_ < 0)
		{
			for (int j = 0; j < teGravestone.epitaph.length; ++j)
			{
				if (teGravestone.epitaph[j] != null)
				{
					IChatComponent ichatcomponent = teGravestone.epitaph[j];
					List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 95, fontrenderer, false, true);
					String s = list != null && list.size() > 0 ? ((IChatComponent)list.get(0)).getFormattedText() : "";

					if (j == teGravestone.lineBeingEdited)
					{
						s = "> " + s + " <";
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - teGravestone.epitaph.length * 5, b0);
					}
					else
					{
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - teGravestone.epitaph.length * 5, b0);
					}
				}
			}
		}

		GlStateManager.depthMask(true);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();

		if (p_180541_9_ >= 0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

	public void renderTileEntityAt(TileEntity teGravestone, double posX, double posY, double posZ, float p_180535_8_, int p_180535_9_)
	{
		this.render((TileEntityGravestone)teGravestone, posX, posY, posZ, p_180535_8_, p_180535_9_);
	}
}