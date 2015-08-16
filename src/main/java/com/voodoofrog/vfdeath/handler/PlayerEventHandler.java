package com.voodoofrog.vfdeath.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;

public class PlayerEventHandler
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);

		VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage((EntityPlayer)event.player), (EntityPlayerMP)event.player);

		if (props.getIsDead())
		{
			VFDeath.ghostHandler.enableGhostAbilities((EntityPlayerMP)event.player, true);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);

		//props.loseHearts(1);

		if (props.getIsDead())
		{
			VFDeath.ghostHandler.enableGhostAbilities((EntityPlayerMP)event.player, true);
		}
	}

	@SubscribeEvent
	public void onPlayerDimensionChange(PlayerChangedDimensionEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);

		VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage((EntityPlayer)event.player), (EntityPlayerMP)event.player);
	}

	//@SubscribeEvent
	public void onTickPlayer(PlayerTickEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);

		if (props.getIsDead())
		{
			event.player.setInvisible(true);
		}
		else
		{
			if (event.player.isInvisible() && !event.player.isPotionActive(Potion.invisibility))
			{
				event.player.setInvisible(false);
			}
		}
	}
}