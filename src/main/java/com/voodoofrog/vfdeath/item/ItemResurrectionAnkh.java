package com.voodoofrog.vfdeath.item;

import java.util.List;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.config.ConfigHandler;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemResurrectionAnkh extends Item {
	
	public ItemResurrectionAnkh(int id) {
		super(id);
	}
	
	@Override
	public ItemStack onEaten(ItemStack item, World world, EntityPlayer player) {
		if (!world.isRemote && isCharged(item.getItemDamage())) {
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			int healthModifier = nbt.getInteger("MaxHP");

			if (healthModifier != 0) {
				if (healthModifier + 2 > 0) {
					healthModifier = 0;
				} else {
					healthModifier += 2;
				}

				nbt.setInteger("MaxHP", healthModifier);
				nbt.setBoolean("IsDead", false);
				AttributeInstance attributeinstance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);

				try {
					attributeinstance.removeModifier(attributeinstance.getModifier(ConfigHandler.HEALTH_MOD_UUID));
				} catch (Exception var8) {}

				attributeinstance.applyModifier(new AttributeModifier(ConfigHandler.HEALTH_MOD_UUID, ModInfo.ID.toLowerCase() + ".healthmod", (double) healthModifier, 0));
				player.setHealth(player.getMaxHealth());
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
        return EnumAction.bow;
    }
	
	public boolean isCharged(int damage) {
		if(damage == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		if(!isCharged(item.getItemDamage())) {
			if (player.experienceLevel >= ConfigHandler.RES_ANKH_XP_COST)
			{
				player.experienceLevel -= ConfigHandler.RES_ANKH_XP_COST;
				item.damageItem(-1, player);
			}
		} else {
			player.setItemInUse(item, this.getMaxItemUseDuration(item));
		}

        return item;
	}
	
	@Override
	public boolean hasEffect(ItemStack item){
		if(isCharged(item.getItemDamage())) {
			return true;
		}
		return false;
	}
		
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List info, boolean useExtraInformation) {
		if(isCharged(item.getItemDamage())) {
			info.add("This ankh is ready to be slotted");
			info.add("into a resurrection altar.");
		} else {
			int levels = item.getItemDamage() * 10;
			
			info.add("You must add another " + levels + " levels");
			info.add("before this item is charged.");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ConfigHandler.RESOURCE_PATH + "res_ankh");
	}

}
