package com.voodoofrog.vfdeath.server.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

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
			this.ghostPlayer(event.player, true);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);
		
		props.loseHearts(1);
		
		if (props.getIsDead())
		{
			this.ghostPlayer(event.player, true);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDimensionChange(PlayerChangedDimensionEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);
		
		VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage((EntityPlayer)event.player), (EntityPlayerMP)event.player);
	}
	
	public void ghostPlayer(EntityPlayer player, boolean makeGhost)
	{
		if (makeGhost)
		{
			player.capabilities.allowFlying = true;
			player.capabilities.disableDamage = true;
			//player.addPotionEffect((new PotionEffect(Potion.invisibility.getId(), 1000)));
			player.setInvisible(true);
			player.sendPlayerAbilities();
		}
		else
		{
			if (!player.capabilities.isCreativeMode)
			{
				player.capabilities.allowFlying = false;
				player.capabilities.disableDamage = false;
			}
			player.setInvisible(false);
			player.sendPlayerAbilities();
		}
	}
}