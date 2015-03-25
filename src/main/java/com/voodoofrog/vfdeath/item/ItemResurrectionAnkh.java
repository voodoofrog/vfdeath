package com.voodoofrog.vfdeath.item;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.misc.Strings;

public class ItemResurrectionAnkh extends Item
{
	private ChatComponentTranslation bindFailMsg = new ChatComponentTranslation(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "."
			+ Strings.ANKH_BOUND_FAIL);

	@Override
	public ItemStack onItemUseFinish(ItemStack item, World world, EntityPlayer player)
	{
		if (!this.isCharged(item.getItemDamage()))
		{
			if (player.experienceLevel >= ConfigHandler.RES_ANKH_LEVEL_COST)
			{
				if (!item.getTagCompound().getString("owner").isEmpty())
				{
					UUID playerUUID = player.getUUID(player.getGameProfile());
					UUID ownerUUID = UUID.fromString(item.getTagCompound().getString("owner"));

					/*if (!playerUUID.equals(ownerUUID))
					{
						player.addChatMessage(this.bindFailMsg);
						return item;
					}*/

					player.experienceLevel -= ConfigHandler.RES_ANKH_LEVEL_COST;
					item.damageItem(-1, player);
					item.getTagCompound().setString("owner", player.getUUID(player.getGameProfile()).toString());
					return item;
				}
				else
				{
					player.experienceLevel -= ConfigHandler.RES_ANKH_LEVEL_COST;
					item.damageItem(-1, player);
					item.getTagCompound().setString("owner", player.getUUID(player.getGameProfile()).toString());
					return item;
				}
			}
		}

		if (!world.isRemote && this.isCharged(item.getItemDamage()))
		{
			if (!item.getTagCompound().getString("owner").isEmpty())
			{
				UUID playerUUID = player.getUUID(player.getGameProfile());
				UUID ownerUUID = UUID.fromString(item.getTagCompound().getString("owner"));

				if (!playerUUID.equals(ownerUUID))
				{
					player.addChatMessage(this.bindFailMsg);
					return item;
				}
			}

			ExtendedPlayer props = ExtendedPlayer.get(player);

			if (!props.getIsDead())
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
		player.setItemInUse(item, this.getMaxItemUseDuration(item));
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
		String msg = "";

		if (this.isCharged(item.getItemDamage()))
		{
			msg = StatCollector.translateToLocal(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "." + Strings.ANKH_READY_TO_SLOT);
			Ribbit.textUtils.wrapStringToList(msg, 35, false, info);

			if (item.getTagCompound() != null)
			{
				if (!item.getTagCompound().getString("owner").isEmpty())
				{
					String ownerName = Ribbit.playerUtils.getUserNameFromUUID(UUID.fromString(item.getTagCompound().getString("owner")));

					if (!ownerName.isEmpty())
					{
						msg = StatCollector.translateToLocalFormatted(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "."
								+ Strings.ANKH_GAIN_HEARTS, ownerName);
						Ribbit.textUtils.wrapStringToList(msg, 35, false, info);
					}
				}
			}
		}
		else
		{
			int levels = item.getItemDamage() * ConfigHandler.RES_ANKH_LEVEL_COST;

			msg = StatCollector.translateToLocalFormatted(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "." + Strings.ANKH_LVLS_LEFT, levels);
			Ribbit.textUtils.wrapStringToList(msg, 35, false, info);

			if (item.getTagCompound() != null)
			{
				if (!item.getTagCompound().getString("owner").isEmpty())
				{
					String ownerName = Ribbit.playerUtils.getUserNameFromUUID(UUID.fromString(item.getTagCompound().getString("owner")));

					if (!ownerName.isEmpty())
					{
						msg = StatCollector.translateToLocalFormatted(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "."
								+ Strings.ANKH_OWNER_CHARGE, ownerName);
						Ribbit.textUtils.wrapStringToList(msg, 35, false, info);
					}
				}
			}
		}
	}

	@Override
	public void onCreated(ItemStack item, World world, EntityPlayer player)
	{
		item.setTagCompound(new NBTTagCompound());
	}
}
