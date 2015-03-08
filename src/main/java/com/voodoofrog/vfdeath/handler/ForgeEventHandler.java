package com.voodoofrog.vfdeath.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;

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
			VFDeath.logger.info("Player rejoined world");
			// update on the server
			ExtendedPlayer props = ExtendedPlayer.get((EntityPlayerMP)event.entity);
			props.updateHealthAttribMod();

			// update on the client
			//VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage((EntityPlayer)event.entity), (EntityPlayerMP)event.entity);
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event)
	{
		ExtendedPlayer.get(event.entityPlayer).copy(ExtendedPlayer.get(event.original));
	}
}