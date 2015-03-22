package com.voodoofrog.vfdeath.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.voodoofrog.vfdeath.client.gui.GuiInGameHealthLoss;
import com.voodoofrog.vfdeath.client.renderer.tileentity.TileEntityCoffinRenderer;
import com.voodoofrog.vfdeath.client.renderer.tileentity.TileEntityGravestoneRenderer;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.init.VFDeathItems;
import com.voodoofrog.vfdeath.misc.ModInfo;
import com.voodoofrog.vfdeath.misc.Strings;
import com.voodoofrog.vfdeath.tileentity.TileEntityCoffin;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

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
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGravestone.class, new TileEntityGravestoneRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoffin.class, new TileEntityCoffinRenderer());
		
		//Item itemGravestoneSimple = GameRegistry.findItem(ModInfo.ID, Strings.GRAVESTONE_NAME);
		//ModelResourceLocation gravestoneModelResourceLocation = new ModelResourceLocation(ModInfo.ID + ":" + Strings.GRAVESTONE_NAME, "inventory");
		//final int DEFAULT_ITEM_SUBTYPE = 0;
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemGravestoneSimple, DEFAULT_ITEM_SUBTYPE, gravestoneModelResourceLocation);
	}
}
