package com.voodoofrog.vfdeath.handler;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.client.gui.GuiDeathScreen;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;

public class ForgeEventHandler
{
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			if (ExtendedPlayer.get((EntityPlayer)event.entity) == null)
			{
				ExtendedPlayer.register((EntityPlayer)event.entity);
			}
		}
	}

	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayerMP)
		{
			ExtendedPlayer props = ExtendedPlayer.get((EntityPlayerMP)event.entity);
			props.updateHealthAttribMod();

			// VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage((EntityPlayer)event.entity), (EntityPlayerMP)event.entity);
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event)
	{
		ExtendedPlayer.get(event.entityPlayer).copy(ExtendedPlayer.get(event.original));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiOpen(GuiOpenEvent event)
	{
		if (event.gui instanceof GuiGameOver && FMLClientHandler.instance().getClient().currentScreen == null)
		{
			event.gui = new GuiDeathScreen();
		}
	}
}