package com.voodoofrog.vfdeath.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.client.gui.GuiInGameHealthLoss;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.init.VFDeathItems;
import com.voodoofrog.vfdeath.misc.Strings;

public class ClientProxy extends CommonProxy
{
	private final Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void initRenderers()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation altarResource = new ModelResourceLocation(ModInfo.ID + ":" + Strings.ALTAR_NAME, "inventory");
		ModelResourceLocation ankhResource = new ModelResourceLocation(ModInfo.ID + ":" + Strings.ANKH_NAME, "inventory");

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(VFDeathBlocks.altar), 0, altarResource);
		renderItem.getItemModelMesher().register(VFDeathItems.ankh, 0, ankhResource);

		MinecraftForge.EVENT_BUS.register(new GuiInGameHealthLoss(this.mc));
	}
}
