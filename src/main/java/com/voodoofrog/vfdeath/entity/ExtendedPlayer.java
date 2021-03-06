package com.voodoofrog.vfdeath.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.block.BlockGravestone;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.inventory.InventoryGrave;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;

public class ExtendedPlayer implements IExtendedEntityProperties
{
	public final static String EXT_PROP_NAME = "VFDeath";

	private final EntityPlayer player;
	private double healthMod;
	private boolean isDead;
	private BlockPos gravePos;
	private BlockPos customGravePos;
	private int customGraveWorldId;
	public final static UUID healthModUUID = UUID.fromString("b70b11c6-3690-4ff6-b284-2d626929c6da");
	private InventoryGrave inventoryGrave;

	public ExtendedPlayer(EntityPlayer player)
	{
		this.player = player;
		this.healthMod = 0;
		this.isDead = false;
		this.gravePos = null;
		this.customGravePos = null;
		this.customGraveWorldId = 0;
		this.inventoryGrave = new InventoryGrave(player);
	}

	/**
	 * Used to register these extended properties for the player during EntityConstructing event
	 */
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}

	/**
	 * Returns ExtendedPlayer properties for player
	 */
	public static final ExtendedPlayer get(EntityPlayer player)
	{
		return (ExtendedPlayer)player.getExtendedProperties(EXT_PROP_NAME);
	}

	/**
	 * Copies additional player data from the given ExtendedPlayer instance Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ExtendedPlayer props)
	{
		this.healthMod = props.healthMod;
		this.isDead = props.isDead;
		this.gravePos = props.gravePos;
		this.customGravePos = props.customGravePos;
		this.customGraveWorldId = props.customGraveWorldId;
	}

	@Override
	public final void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		NBTTagCompound slot;
		ItemStack[] inventory = this.inventoryGrave.getInventory();

		properties.setDouble("HealthModifier", this.healthMod);
		properties.setBoolean("IsDead", this.isDead);
		
		if (this.gravePos != null)
		{
			properties.setInteger("GravePosX", this.gravePos.getX());
			properties.setInteger("GravePosY", this.gravePos.getY());
			properties.setInteger("GravePosZ", this.gravePos.getZ());
		}

		if (this.customGravePos != null)
		{
			properties.setInteger("CustomGravePosX", this.customGravePos.getX());
			properties.setInteger("CustomGravePosY", this.customGravePos.getY());
			properties.setInteger("CustomGravePosZ", this.customGravePos.getZ());
		}
		
		properties.setInteger("CustomGraveWorldID", this.customGraveWorldId);
		
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				slot = new NBTTagCompound();
				slot.setByte("Slot", (byte)i);
				inventory[i].writeToNBT(slot);
				tagList.appendTag(slot);
			}
		}

		properties.setTag("GraveInventory", tagList);

		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public final void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = (NBTTagCompound)compound.getTag(EXT_PROP_NAME);

		this.healthMod = properties.getDouble("HealthModifier");
		this.isDead = properties.getBoolean("IsDead");
		
		if (properties.hasKey("GravePosX") && properties.hasKey("GravePosY") && properties.hasKey("GravePosZ"))
		{
			int gravePosX = properties.getInteger("GravePosX");
			int gravePosY = properties.getInteger("GravePosY");
			int gravePosZ = properties.getInteger("GravePosZ");
			this.gravePos = new BlockPos(gravePosX, gravePosY, gravePosZ);
		}
		
		if (properties.hasKey("CustomGravePosX") && properties.hasKey("CustomGravePosY") && properties.hasKey("CustomGravePosZ"))
		{
			int customGravePosX = properties.getInteger("CustomGravePosX");
			int customGravePosY = properties.getInteger("CustomGravePosY");
			int customGravePosZ = properties.getInteger("CustomGravePosZ");
			this.customGravePos = new BlockPos(customGravePosX, customGravePosY, customGravePosZ);
		}
		
		this.customGraveWorldId = properties.getInteger("CustomGraveWorldID");

		NBTTagList tagList = properties.getTagList("GraveInventory", 10);

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound slot = (NBTTagCompound)tagList.getCompoundTagAt(i);
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(slot);
			int j = slot.getByte("Slot") & 255;

			if (itemstack != null)
			{
				ItemStack[] inventory = this.inventoryGrave.getInventory();
				inventory[i] = itemstack;
			}
		}
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public void updateHealthAttribMod()
	{
		IAttributeInstance maxHealthAttrib = this.player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
		// VFDeath.logger.info("Max Health (pre mod): " + maxHealthAttrib.getAttributeValue());

		try
		{
			maxHealthAttrib.removeModifier(maxHealthAttrib.getModifier(healthModUUID));
		}
		catch (Exception ex)
		{
		}

		if (this.healthMod != 0.0D)
		{
			maxHealthAttrib.applyModifier(new AttributeModifier(healthModUUID, EXT_PROP_NAME + ".healthmod", (0 - this.healthMod), 0));
			// VFDeath.logger.info("Max Health (post mod): " + maxHealthAttrib.getAttributeValue());
		}

		if (this.player.getHealth() > this.player.getMaxHealth())
		{
			this.player.setHealth(this.player.getMaxHealth());
		}
		// VFDeath.logger.info("Max Health (post mod): " + maxHealthAttrib.getAttributeValue());
	}

	public void loseHearts(int num)
	{
		if (this.isDead)
			return;

		IAttributeInstance iattributeinstance = this.player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		double baseMax = iattributeinstance.getBaseValue();
		double currentMax = iattributeinstance.getAttributeValue();
		double amount = num * 2D;

		if (amount >= currentMax)
		{
			VFDeath.logger.info("Losing last life");
			this.isDead = true;

			/*
			 * if (!this.hasGrave) { this.gravePos = VFDeath.graveyard.spawnGrave(player); this.hasGrave = true; }
			 */

			this.healthMod = baseMax - 2D; // TODO: remove this later?
		}
		else
		{
			// VFDeath.logger.info("Losing a life");
			// VFDeath.logger.info("HEALTH INFO[Losing: " + amount + "|Current: " + currentMax + "]");
			this.healthMod += amount;
		}

		this.updateHealthAttribMod();
		VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage(this.player), (EntityPlayerMP)this.player);
	}

	public boolean canGainHearts()
	{
		return this.healthMod > 0;
	}

	public void gainHearts(int num, boolean setIsDead)
	{
		if (this.isDead)
			num -= 1;

		this.isDead = setIsDead;
		this.gainHearts(num);
	}

	public void gainHearts(int num)
	{
		IAttributeInstance maxHealth = this.player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		double amount = num * 2D;

		if (this.healthMod != 0)
		{
			if (amount > this.healthMod)
			{
				this.healthMod = 0;
			}
			else
			{
				this.healthMod -= amount;
			}

			this.updateHealthAttribMod();
			VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage(this.player), (EntityPlayerMP)this.player);
		}
	}

	public double getHealthMod()
	{
		return this.healthMod;
	}

	public boolean getIsDead()
	{
		return this.isDead;
	}

	public InventoryGrave getGraveInventory()
	{
		return this.inventoryGrave;
	}

	public boolean isOnLastLife()
	{
		return this.player.getMaxHealth() <= ConfigHandler.HEART_LOSS_ON_DEATH * 2;
	}


	public BlockPos getGravePos()
	{
		return this.gravePos;
	}
	
	public void setGravePos(BlockPos pos)
	{
		this.gravePos = pos;
	}

	public BlockPos getCustomGravePos()
	{
		return this.customGravePos;
	}
	
	public int getCustomGraveWorldId()
	{
		return this.customGraveWorldId;
	}
	
	public void setCustomGravePos(World world, BlockPos pos)
	{
		this.customGraveWorldId = world.provider.getDimensionId();
		this.customGravePos = pos;
	}
	
	public void spawnNewGrave()
	{
		this.setGravePos(VFDeath.graveyard.spawnGrave(player));
	}
}