package com.voodoofrog.vfdeath.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.block.BlockCoffin;
import com.voodoofrog.vfdeath.client.model.ModelCoffin;
import com.voodoofrog.vfdeath.misc.ModInfo;
import com.voodoofrog.vfdeath.tileentity.TileEntityCoffin;

@SideOnly(Side.CLIENT)
public class TileEntityCoffinRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation textureNormalDouble = new ResourceLocation(ModInfo.ID, "textures/entity/coffin.png");
	private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
	private ModelChest simpleChest = new ModelChest();
	private ModelChest fullCoffin = new ModelCoffin();

	public void render(TileEntityCoffin teCoffin, double posX, double posY, double posZ, float p_180538_8_, int p_180538_9_)
	{
		int j;

		if (!teCoffin.hasWorldObj())
		{
			j = 0;
		}
		else
		{
			Block block = teCoffin.getBlockType();
			j = teCoffin.getBlockMetadata();

			if (block instanceof BlockCoffin && j == 0)
			{
				((BlockCoffin)block).checkForSurroundingCoffins(teCoffin.getWorld(), teCoffin.getPos(),
						teCoffin.getWorld().getBlockState(teCoffin.getPos()));
				j = teCoffin.getBlockMetadata();
			}

			teCoffin.checkForAdjacentCoffins();
		}

		if (teCoffin.adjacentCoffinZNeg == null && teCoffin.adjacentCoffinXNeg == null)
		{
			ModelChest modelchest;

			if (teCoffin.adjacentCoffinXPos == null && teCoffin.adjacentCoffinZPos == null)
			{
				modelchest = this.simpleChest;

				if (p_180538_9_ >= 0)
				{
					this.bindTexture(DESTROY_STAGES[p_180538_9_]);
					GlStateManager.matrixMode(5890);
					GlStateManager.pushMatrix();
					GlStateManager.scale(4.0F, 4.0F, 1.0F);
					GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
					GlStateManager.matrixMode(5888);
				}
				else
				{
					this.bindTexture(textureNormal);
				}
			}
			else
			{
				modelchest = this.fullCoffin;

				if (p_180538_9_ >= 0)
				{
					this.bindTexture(DESTROY_STAGES[p_180538_9_]);
					GlStateManager.matrixMode(5890);
					GlStateManager.pushMatrix();
					GlStateManager.scale(8.0F, 4.0F, 1.0F);
					GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
					GlStateManager.matrixMode(5888);
				}
				else
				{
					this.bindTexture(textureNormalDouble);
				}
			}

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();

			if (p_180538_9_ < 0)
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

			GlStateManager.translate((float)posX, (float)posY + 1.1875F, (float)posZ + 1.0F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.translate(0.5F, 0.5F, 0.5F);
			short short1 = 0;

			if (j == 2)
			{
				short1 = 180;
			}

			if (j == 3)
			{
				short1 = 0;
			}

			if (j == 4)
			{
				short1 = 90;
			}

			if (j == 5)
			{
				short1 = -90;
			}

			if (j == 2 && teCoffin.adjacentCoffinXPos != null)
			{
				GlStateManager.translate(1.0F, 0.0F, 0.0F);
			}

			if (j == 5 && teCoffin.adjacentCoffinZPos != null)
			{
				GlStateManager.translate(0.0F, 0.0F, -1.0F);
			}

			GlStateManager.rotate((float)short1, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			float f1 = teCoffin.prevLidAngle + (teCoffin.lidAngle - teCoffin.prevLidAngle) * p_180538_8_;
			float f2;

			if (teCoffin.adjacentCoffinZNeg != null)
			{
				f2 = teCoffin.adjacentCoffinZNeg.prevLidAngle + (teCoffin.adjacentCoffinZNeg.lidAngle - teCoffin.adjacentCoffinZNeg.prevLidAngle)
						* p_180538_8_;

				if (f2 > f1)
				{
					f1 = f2;
				}
			}

			if (teCoffin.adjacentCoffinXNeg != null)
			{
				f2 = teCoffin.adjacentCoffinXNeg.prevLidAngle + (teCoffin.adjacentCoffinXNeg.lidAngle - teCoffin.adjacentCoffinXNeg.prevLidAngle)
						* p_180538_8_;

				if (f2 > f1)
				{
					f1 = f2;
				}
			}

			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
			modelchest.renderAll();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			if (p_180538_9_ >= 0)
			{
				GlStateManager.matrixMode(5890);
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
			}
		}
	}

	public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
	{
		this.render((TileEntityCoffin)p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
	}
}