package com.voodoofrog.vfdeath.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;

public class ExtendedPlayer implements IExtendedEntityProperties
{
	public final static String EXT_PROP_NAME = "VFDeath";

	private final EntityPlayer player;
	private double healthMod;
	private boolean isDead;
	public final static UUID healthModUUID = UUID.fromString("b70b11c6-3690-4ff6-b284-2d626929c6da");

	public ExtendedPlayer(EntityPlayer player)
	{
		this.player = player;
		this.healthMod = 0;
		this.isDead = false;
	}

	/**
	 * Used to register these extended properties for the player during
	 * EntityConstructing event
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
	 * Copies additional player data from the given ExtendedPlayer instance
	 * Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ExtendedPlayer props)
	{
		this.healthMod = props.healthMod;
		this.isDead = props.isDead;
	}

	@Override
	public final void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = new NBTTagCompound();

		properties.setDouble("HealthModifier", this.healthMod);
		properties.setBoolean("IsDead", this.isDead);

		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public final void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = (NBTTagCompound)compound.getTag(EXT_PROP_NAME);
		this.healthMod = properties.getDouble("HealthModifier");
		this.isDead = properties.getBoolean("IsDead");
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public void updateHealthAttribMod()
	{
		IAttributeInstance maxHealthAttrib = this.player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
		VFDeath.logger.info("Max Health (pre mod): " + maxHealthAttrib.getAttributeValue());
		try
		{
			maxHealthAttrib.removeModifier(maxHealthAttrib.getModifier(healthModUUID));
		}
		catch (Exception ex)
		{
		}

		maxHealthAttrib.applyModifier(new AttributeModifier(healthModUUID, EXT_PROP_NAME + ".healthmod", (0 - this.healthMod), 0));
		VFDeath.logger.info("Max Health (post mod): " + maxHealthAttrib.getAttributeValue());
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
			this.healthMod = baseMax - 2D; //TODO: remove this later?
		}
		else
		{
			VFDeath.logger.info("Losing a life");
			VFDeath.logger.info("HEALTH INFO[Losing: " + amount + "|Current: " + currentMax + "]");
			this.healthMod += amount;
		}
		
		this.updateHealthAttribMod();
		VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage(this.player), (EntityPlayerMP)this.player);
	}

	public void gainHearts(int num, boolean setIsDead)
	{
		this.isDead = setIsDead;
		this.gainHearts(num);
	}
	
	public void gainHearts(int num)
	{
		IAttributeInstance iattributeinstance = this.player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		double baseMax = iattributeinstance.getBaseValue();
		double currentMax = iattributeinstance.getAttributeValue();
		double amount = num * 2D;
		
		if (amount > baseMax)
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
	
	public double getHealthMod()
	{
		return this.healthMod;
	}
	
	public boolean getIsDead()
	{
		return this.isDead;
	}
}