package com.voodoofrog.vfdeath.handler;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.VFDeath;
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
			EntityPlayerMP player = (EntityPlayerMP)event.entity;
			ExtendedPlayer props = ExtendedPlayer.get(player);

			props.updateHealthAttribMod();
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

	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.entityPlayer);

		if (props.isOnLastLife())
		{
			props.loseHearts(1);

			if (props.getCustomGravePos() != null)
			{
				VFDeath.logger.info("Attempting to use existing custom grave...");
				if (VFDeath.graveyard.addDropsToGrave(event.entityPlayer, DimensionManager.getWorld(props.getCustomGraveWorldId()), props.getCustomGravePos(),
						event.drops))
				{
					VFDeath.logger.info("Successfully used existing custom grave!");
					event.setCanceled(true);
				}
				// custom grave was no longer valid, try default
				else if (props.getGravePos() != null)
				{
					VFDeath.logger.info("Attempting to use existing default grave...");
					
					if (VFDeath.graveyard.addDropsToGrave(event.entityPlayer, DimensionManager.getWorld(VFDeath.graveyard.getWorldId()), props.getGravePos(),
							event.drops))
					{
						VFDeath.logger.info("Successfully used existing default grave!");
						event.setCanceled(true);
					}
					// custom and default grave filling has failed, spawn new grave
					else
					{
						VFDeath.logger.info("Setting up new default grave...");
						BlockPos gravePos = VFDeath.graveyard.spawnGrave(event.entityPlayer);
						
						if (VFDeath.graveyard.addDropsToGrave(event.entityPlayer, DimensionManager.getWorld(VFDeath.graveyard.getWorldId()), gravePos,
								event.drops))
						{
							VFDeath.logger.info("Successfully used new default grave!");
							event.setCanceled(true);
						}
					}
				}
			}
			else if (props.getGravePos() != null)
			{
				VFDeath.logger.info("Attempting to use existing default grave...");
				
				if (VFDeath.graveyard.addDropsToGrave(event.entityPlayer, DimensionManager.getWorld(VFDeath.graveyard.getWorldId()), props.getGravePos(),
						event.drops))
				{
					VFDeath.logger.info("Successfully used existing default grave!");
					event.setCanceled(true);
				}
				else
				{
					VFDeath.logger.info("Setting up new default grave...");
					BlockPos gravePos = VFDeath.graveyard.spawnGrave(event.entityPlayer);
					
					if (VFDeath.graveyard.addDropsToGrave(event.entityPlayer, DimensionManager.getWorld(VFDeath.graveyard.getWorldId()), gravePos,
							event.drops))
					{
						VFDeath.logger.info("Successfully used new default grave!");
						event.setCanceled(true);
					}
				}
			}
			else
			{
				BlockPos gravePos = VFDeath.graveyard.spawnGrave(event.entityPlayer);
				
				if (VFDeath.graveyard.addDropsToGrave(event.entityPlayer, DimensionManager.getWorld(VFDeath.graveyard.getWorldId()), gravePos,
						event.drops))
				{
					event.setCanceled(true);
				}
			}
		}
		else
		{
			// player is not on last life, just dock a heart
			props.loseHearts(1);
		}
	}
}