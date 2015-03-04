package com.voodoofrog.vfdeath.proxy;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.block.BlockResurrectionAltar;
import com.voodoofrog.vfdeath.block.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initSounds()
	{
		// load sounds
	}

	@Override
	public void initRenderers()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		// renderItem.getItemModelMesher().register(Blocks.altar, 0, new
		// ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" +
		// ((BlockResurrectionAltar)Blocks.altar).getName(), "inventory"));
		ModelResourceLocation resource = new ModelResourceLocation(ModInfo.ID + ":" + ((BlockResurrectionAltar)Blocks.altar).getName(), "inventory");
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Blocks.altar), 0, resource);
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}
