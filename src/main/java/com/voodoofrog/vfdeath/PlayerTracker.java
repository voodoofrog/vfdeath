package com.voodoofrog.vfdeath;

import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.graveyard.Graveyard;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerTracker implements IPlayerTracker {
	public void onPlayerLogin(EntityPlayer player) {
		int healthModifier = 0;
		boolean setData = false;
		NBTTagCompound compound;

		if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			compound = new NBTTagCompound();
			player.getEntityData().setCompoundTag(EntityPlayer.PERSISTED_NBT_TAG, compound);
		} else {
			compound = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}

		if (compound.getTag("MaxHP") != null) {
			int maxHP = compound.getInteger("MaxHP");

			if (compound.getTag("MaxHP") == null) {
				compound.setInteger("MaxHP", 0);
			}
			
			if (maxHP > 0) {
				healthModifier = -(20 - maxHP);
				compound.setInteger("MaxHP", healthModifier);
				setData = true;
			}

			if (maxHP == 0 && compound.getBoolean("IsDead")) {
				healthModifier = -19;
				compound.setInteger("MaxHP", healthModifier);
				setData = true;
			}

		}


		if (compound.getTag("IsDead") == null) {
			compound.setBoolean("IsDead", false);
		}


		if (setData) {
			AttributeInstance maxHealthAttrib = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);

			try {
				maxHealthAttrib.removeModifier(maxHealthAttrib.getModifier(ConfigHandler.HEALTH_MOD_UUID));
			} catch (Exception ex) {}

			maxHealthAttrib.applyModifier(new AttributeModifier(ConfigHandler.HEALTH_MOD_UUID, ModInfo.ID.toLowerCase() + ".healthmod", (double) healthModifier, 0));
			// player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
			maxHealthAttrib.setAttribute(20.0D);

			if (player.getHealth() > player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
			}

			setData = false;
		}

		if (compound.getBoolean("IsDead")) {
			this.setGhost(player);
		}
	}

	public void onPlayerLogout(EntityPlayer player) {
	}

	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	public void onPlayerRespawn(EntityPlayer player) {
		NBTTagCompound compound = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		int healthModifier = compound.getInteger("MaxHP");
		int heartLoss = ConfigHandler.HEART_LOSS_ON_DEATH * 2;

		if (player.getMaxHealth() + (float) healthModifier - (float) heartLoss <= 0.0F) {
			this.setGhost(player);
			WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(player.dimension);
			Graveyard.spawnGrave(worldserver, player.worldObj.provider.getRandomizedSpawnPoint());
			player.addChatMessage("You have died and are now a ghost!");
			compound.setBoolean("IsDead", true);
			healthModifier = (int) (1.0F - player.getMaxHealth());
		} else {
			compound.setBoolean("IsDead", false);
			healthModifier -= heartLoss;
		}

		compound.setInteger("MaxHP", healthModifier);
		AttributeInstance maxHealthAttrib = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);

		try {
			maxHealthAttrib.removeModifier(maxHealthAttrib.getModifier(ConfigHandler.HEALTH_MOD_UUID));
		} catch (Exception ex) {}

		maxHealthAttrib.applyModifier(new AttributeModifier(ConfigHandler.HEALTH_MOD_UUID, ModInfo.ID.toLowerCase() + ".healthmod", (double) healthModifier, 0));

		if (player.getHealth() > player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}
	}

	public void setGhost(EntityPlayer player) {
		player.capabilities.allowFlying = true;
		player.capabilities.disableDamage = true;
		player.addPotionEffect((new PotionEffect(Potion.invisibility.getId(), 1000)));
		player.sendPlayerAbilities();
	}
}