package uk.co.forgottendream.vfdeath.item;

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

	@Override
	public void onCreated(ItemStack item, World world, EntityPlayer player) {
		this.setDamage(item, 4);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		player.curePotionEffects(item);

        if (player.experienceLevel >= ConfigHandler.RES_ANKH_XP_COST)
        {
        	player.experienceLevel -= ConfigHandler.RES_ANKH_XP_COST;
        	item.damageItem(-1, player);
        }

        return item;
	}
	
	@Override
	public boolean hasEffect(ItemStack item){
		if(item.getItemDamage() == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ConfigHandler.RESOURCE_PATH + "res_ankh");
	}

}
