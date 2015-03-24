package com.voodoofrog.vfdeath.client.model;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCoffin extends ModelChest
{
	public ModelCoffin()
	{
		this.chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
		this.chestLid.addBox(0.0F, -3.0F, -14.0F, 30, 3, 14, 0.0F);
		this.chestLid.setRotationPoint(1.0F, 7.0F, 15.0F);

		this.chestBelow = new ModelRenderer(this, 0, 17).setTextureSize(128, 64);
		this.chestBelow.addBox(0.0F, 2.0F, 0.0F, 30, 12, 14, 0.0F);
		this.chestBelow.setRotationPoint(1.0F, 5.0F, 1.0F);
	}
}