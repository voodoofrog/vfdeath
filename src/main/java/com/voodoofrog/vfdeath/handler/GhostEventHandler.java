package com.voodoofrog.vfdeath.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;

public class GhostEventHandler
{
	public static List<String> allowedBlocks = new ArrayList();

	@SubscribeEvent
	public void disableDamage(LivingHurtEvent event)
	{
		if (event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer attackingPlayer = (EntityPlayer)event.source.getEntity();
			ExtendedPlayer props = ExtendedPlayer.get(attackingPlayer);

			if (props.getIsDead() && !attackingPlayer.capabilities.isCreativeMode)
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void disableInteraction(PlayerInteractEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		ExtendedPlayer props = ExtendedPlayer.get(player);

		if (props.getIsDead() && !player.capabilities.isCreativeMode)
		{
			String blockName = player.worldObj.getBlockState(event.pos).getBlock().getUnlocalizedName();

			if (allowedBlocks.size() == 0)
			{
				this.buildAllowedBlocks();
			}

			if (!allowedBlocks.contains(blockName))
			{
				event.setCanceled(true);
			}

			/*
			 * if (event.action != Action.LEFT_CLICK_BLOCK &&
			 * player.getHeldItem() != null && player.getHeldItem().itemID ==
			 * Item.appleGold.itemID) { event.setCanceled(false); }
			 */
		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			ExtendedPlayer props = ExtendedPlayer.get(player);

			if (props.getIsDead() && !player.capabilities.isCreativeMode)
			{
				//Probably don't need this, but just in case
				player.getFoodStats().setFoodLevel(20);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityPickup(EntityItemPickupEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			ExtendedPlayer props = ExtendedPlayer.get(player);

			if (props.getIsDead() && !player.capabilities.isCreativeMode)
			{
				event.setCanceled(true);
			}
		}
	}

	private void buildAllowedBlocks()
	{
		String allowedBlocksProp = ConfigHandler.GHOST_ALLOWED_BLOCKS.getString();
		allowedBlocks.add(Blocks.acacia_door.getUnlocalizedName());
		allowedBlocks.add(Blocks.acacia_fence_gate.getUnlocalizedName());
		allowedBlocks.add(Blocks.birch_door.getUnlocalizedName());
		allowedBlocks.add(Blocks.birch_fence_gate.getUnlocalizedName());
		allowedBlocks.add(Blocks.dark_oak_door.getUnlocalizedName());
		allowedBlocks.add(Blocks.dark_oak_fence_gate.getUnlocalizedName());
		allowedBlocks.add(Blocks.jungle_door.getUnlocalizedName());
		allowedBlocks.add(Blocks.jungle_fence_gate.getUnlocalizedName());
		allowedBlocks.add(Blocks.oak_door.getUnlocalizedName());
		allowedBlocks.add(Blocks.oak_fence_gate.getUnlocalizedName());
		allowedBlocks.add(Blocks.spruce_door.getUnlocalizedName());
		allowedBlocks.add(Blocks.spruce_fence_gate.getUnlocalizedName());
		allowedBlocks.add(Blocks.trapdoor.getUnlocalizedName());
		allowedBlocks.add(Blocks.lever.getUnlocalizedName());
		allowedBlocks.add(Blocks.stone_button.getUnlocalizedName());
		allowedBlocks.add(Blocks.wooden_button.getUnlocalizedName());

		if (allowedBlocksProp != null)
		{
			String[] blockArray = allowedBlocksProp.split(",");

			for (int i = 0; i < blockArray.length; i++)
			{
				String blockName = blockArray[i];

				// was surrounded by try catch
				blockName = blockName.trim();
				allowedBlocks.add(blockName);
			}
		}
	}
}
