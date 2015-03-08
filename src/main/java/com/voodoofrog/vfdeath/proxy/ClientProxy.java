package com.voodoofrog.vfdeath.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.block.BlockResurrectionAltar;
import com.voodoofrog.vfdeath.block.Blocks;
import com.voodoofrog.vfdeath.client.gui.GuiInGameHealthLoss;

public class ClientProxy extends CommonProxy
{
	private final Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void initRenderers()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation resource = new ModelResourceLocation(ModInfo.ID + ":" + ((BlockResurrectionAltar)Blocks.altar).getName(),
				"inventory");

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Blocks.altar), 0, resource);

		MinecraftForge.EVENT_BUS.register(new GuiInGameHealthLoss(this.mc));
	}
}
