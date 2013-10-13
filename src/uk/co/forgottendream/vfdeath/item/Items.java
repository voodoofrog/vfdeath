package uk.co.forgottendream.vfdeath.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;

public class Items {

	public static Item resankh;
	
	public static void initialize() {
		resankh = new ItemResurrectionAnkh(ConfigHandler.RES_ANKH_ID).setUnlocalizedName("resAnkh").setCreativeTab(CreativeTabs.tabMisc).setMaxStackSize(1).setMaxDamage(4);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(resankh, "Resurrection Ankh");
	}
	
	public static void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(resankh, 1, 4), new Object[] {" D ", "DDD", " D ", 'D', Block.dirt});
	}
	
}
