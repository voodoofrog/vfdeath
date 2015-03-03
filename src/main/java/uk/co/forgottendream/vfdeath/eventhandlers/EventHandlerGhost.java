package uk.co.forgottendream.vfdeath.eventhandlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;

public class EventHandlerGhost {
	public static List<Integer> allowedBlockIDs = new ArrayList();

	@ForgeSubscribe
	public void disableDamage(LivingHurtEvent event) {
		if (event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer attackingPlayer = (EntityPlayer) event.source.getEntity();
			NBTTagCompound compound = attackingPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

			if (compound.getBoolean("IsDead") && !attackingPlayer.capabilities.isCreativeMode) {
				event.setCanceled(true);
			}
		}
	}

	@ForgeSubscribe
	public void disableInteraction(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		NBTTagCompound compound = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

		if (compound.getBoolean("IsDead") && !player.capabilities.isCreativeMode) {
			int blockID = player.worldObj.getBlockId(event.x, event.y, event.z);

			if (allowedBlockIDs == null) {
				buildAllowedBlocks();
			}

			if (!allowedBlockIDs.contains(Integer.valueOf(blockID))) {
				event.setCanceled(true);
			}

			/*if (event.action != Action.LEFT_CLICK_BLOCK	&& player.getHeldItem() != null	&& player.getHeldItem().itemID == Item.appleGold.itemID) {
				event.setCanceled(false);
			}*/
		}
	}

	@ForgeSubscribe
	public void onEntityUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

			if (nbt.getBoolean("IsDead") && !player.capabilities.isCreativeMode) {
				int invisTimeLeft = player.getActivePotionEffect(Potion.invisibility).getDuration();
				if (invisTimeLeft < 100) {
					player.getActivePotionEffect(Potion.invisibility).duration = 1000;
				}
			}
		}
	}

	public static void buildAllowedBlocks() {
		String allowedBlocks = ConfigHandler.GHOST_ALLOWED_BLOCKS.getString();
		allowedBlockIDs.add(Integer.valueOf(Block.doorWood.blockID));
		allowedBlockIDs.add(Integer.valueOf(Block.trapdoor.blockID));
		allowedBlockIDs.add(Integer.valueOf(Block.fenceGate.blockID));
		allowedBlockIDs.add(Integer.valueOf(Block.lever.blockID));
		allowedBlockIDs.add(Integer.valueOf(Block.stoneButton.blockID));
		allowedBlockIDs.add(Integer.valueOf(Block.woodenButton.blockID));

		if (allowedBlocks != null) {
			String[] blockArray = allowedBlocks.split(",");

			for (int i = 0; i < blockArray.length; i++) {
				String blockID = blockArray[i];

				try {
					blockID = blockID.trim();
					allowedBlockIDs.add(Integer.valueOf(Integer.parseInt(blockID)));
				} catch (Exception var6) {}
			}
		}
	}
}
