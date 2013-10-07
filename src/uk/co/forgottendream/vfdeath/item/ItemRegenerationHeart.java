package uk.co.forgottendream.vfdeath.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRegenerationHeart extends Item {

	public ItemRegenerationHeart(int id) {
		super(id);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		player.curePotionEffects(item);

        if (player.experienceLevel >= ConfigHandler.REGEN_HEART_XP_COST)
        {
        	player.experienceLevel -= ConfigHandler.REGEN_HEART_XP_COST;
        	item.damageItem(-1, player);
        }

        //return item.getItemDamage() == 0 ? new ItemStack(SoftHardcore.RegenHeartFull, 1) : item;
        return item.getItemDamage() == 0 ? new ItemStack(this, 1) : item;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(ConfigHandler.RESOURCE_PATH + "regen_heart");
	}

}
