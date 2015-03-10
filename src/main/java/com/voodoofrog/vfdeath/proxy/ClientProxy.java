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
import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;
import com.voodoofrog.vfdeath.item.Items;

public class ClientProxy extends CommonProxy
{
	private final Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void initRenderers()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation altarResource = new ModelResourceLocation(ModInfo.ID + ":" + ((BlockResurrectionAltar)Blocks.altar).getName(),
				"inventory");
		ModelResourceLocation ankhResource = new ModelResourceLocation(ModInfo.ID + ":" + ((ItemResurrectionAnkh)Items.resankh).getName(),
				"inventory");

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Blocks.altar), 0, altarResource);
		renderItem.getItemModelMesher().register(Items.resankh, 0, ankhResource);

		MinecraftForge.EVENT_BUS.register(new GuiInGameHealthLoss(this.mc));
	}
}
