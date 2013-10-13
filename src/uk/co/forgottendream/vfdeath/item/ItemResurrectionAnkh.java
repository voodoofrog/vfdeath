package uk.co.forgottendream.vfdeath.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemResurrectionAnkh extends Item {
	
	public ItemResurrectionAnkh(int id) {
		super(id);
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
