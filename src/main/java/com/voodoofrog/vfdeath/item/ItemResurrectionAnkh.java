package com.voodoofrog.vfdeath.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;

public class ItemResurrectionAnkh extends Item
{
	@Override
	public ItemStack onItemUseFinish(ItemStack item, World world, EntityPlayer player)
	{
		if (!world.isRemote && this.isCharged(item.getItemDamage()))
		{
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			ExtendedPlayer props = ExtendedPlayer.get(player);

			if (props.getHealthMod() != 0D)
			{
				props.gainHearts(1);
				item.stackSize--;
			}
		}
		return item;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack item)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack item)
	{
		return EnumAction.BOW;
	}

	public boolean isCharged(int damage)
	{
		if (damage == 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
	{
		if (!this.isCharged(item.getItemDamage()))
		{
			if (player.experienceLevel >= ConfigHandler.RES_ANKH_XP_COST)
			{
				player.experienceLevel -= ConfigHandler.RES_ANKH_XP_COST;
				item.damageItem(-1, player);
			}
		}
		else
		{
			player.setItemInUse(item, this.getMaxItemUseDuration(item));
		}

		return item;
	}

	@Override
	public boolean hasEffect(ItemStack item)
	{
		if (this.isCharged(item.getItemDamage()))
		{
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List info, boolean useExtraInformation)
	{
		if (this.isCharged(item.getItemDamage()))
		{
			info.add("This ankh is ready to be slotted");
			info.add("into a resurrection altar.");
		}
		else
		{
			int levels = item.getItemDamage() * 10;

			info.add("You must add another " + levels + " levels");
			info.add("before this item is charged.");
		}
	}
}
