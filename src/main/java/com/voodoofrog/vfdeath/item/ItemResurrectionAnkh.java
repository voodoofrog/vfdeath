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
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
	{
		if (!this.isCharged(stack.getItemDamage()) && player.experienceLevel >= ConfigHandler.RES_ANKH_LEVEL_COST)
		{
			if (stack.hasTagCompound())
			{
				if (!this.hasOwner(stack))
					this.setOwner(stack, player.getUUID(player.getGameProfile()));
			}
			else
			{
				this.setOwner(stack, player.getUUID(player.getGameProfile()));
			}

			this.chargeAnkh(stack, player);
			return stack;
		}

		if (!world.isRemote && this.isCharged(stack.getItemDamage()))
		{
			if (stack.hasTagCompound())
			{
				if (this.hasOwner(stack))
				{
					UUID playerUUID = player.getUUID(player.getGameProfile());

					if (!playerUUID.equals(this.getOwner(stack)))
					{
						Ribbit.playerUtils.sendPlayerPopupMessage(player, this.bindFailMsg);
						return stack;
					}
				}
			}

			ExtendedPlayer props = ExtendedPlayer.get(player);

			if (!props.getIsDead() && props.canGainHearts())
			{
				props.gainHearts(1);
				stack.stackSize--;
			}
			else
			{
				ChatComponentTranslation cannotGain = new ChatComponentTranslation(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "."
						+ Strings.ANKH_CANNOT_GAIN);
				Ribbit.playerUtils.sendPlayerPopupMessage(player, cannotGain);
			}
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
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
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		if (this.isCharged(stack.getItemDamage()))
		{
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean useExtraInformation)
	{
		String msg = "";

		if (this.isCharged(stack.getItemDamage()))
		{
			msg = StatCollector.translateToLocal(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "." + Strings.ANKH_READY_TO_SLOT);
			Ribbit.textUtils.wrapStringToList(msg, 35, false, info);

			if (stack.hasTagCompound())
			{
				if (this.hasOwner(stack))
				{
					String ownerName = Ribbit.playerUtils.getUserNameFromUUID(this.getOwner(stack));

					if (!ownerName.isEmpty())
					{
						msg = StatCollector.translateToLocalFormatted(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "." + Strings.ANKH_GAIN_HEARTS,
								ownerName);
						Ribbit.textUtils.wrapStringToList(msg, 35, false, info);
					}
				}
			}
		}
		else
		{
			int levels = stack.getItemDamage() * ConfigHandler.RES_ANKH_LEVEL_COST;

			msg = StatCollector.translateToLocalFormatted(Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "." + Strings.ANKH_LVLS_LEFT, levels);
			Ribbit.textUtils.wrapStringToList(msg, 35, false, info);

			if (stack.hasTagCompound())
			{
				if (!stack.getTagCompound().getString("owner").isEmpty())
				{
					String ownerName = Ribbit.playerUtils.getUserNameFromUUID(this.getOwner(stack));

					if (!ownerName.isEmpty())
					{
						msg = StatCollector.translateToLocalFormatted(
								Strings.ITEMS_KEY + "." + Strings.ANKH_NAME + "." + Strings.ANKH_OWNER_CHARGE, ownerName);
						Ribbit.textUtils.wrapStringToList(msg, 35, false, info);
					}
				}
			}
		}
	}

	/*
	 * @Override public void onCreated(ItemStack stack, World world, EntityPlayer player) { stack.setTagCompound(new NBTTagCompound()); }
	 */

	public void chargeAnkh(ItemStack stack, EntityPlayer player)
	{
		player.experienceLevel -= ConfigHandler.RES_ANKH_LEVEL_COST;
		stack.damageItem(-1, player);
	}

	public boolean hasOwner(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return false;

		return !stack.getTagCompound().getString("owner").isEmpty();
	}

	public UUID getOwner(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return null;
					
		if (!stack.getTagCompound().getString("owner").isEmpty())
		{
			UUID ownerUUID = UUID.fromString(stack.getTagCompound().getString("owner"));
			return ownerUUID;
		}

		return null;
	}

	public void setOwner(ItemStack stack, UUID ownerUUID)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if (stack.getTagCompound().getString("owner").isEmpty())
		{
			stack.getTagCompound().setString("owner", ownerUUID.toString());
		}
	}
}
