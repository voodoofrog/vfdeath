package com.voodoofrog.vfdeath.item;

import com.voodoofrog.vfdeath.config.ConfigHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Items {

	public static Item resankh;
	
	public static void initialize() {
		resankh = new ItemResurrectionAnkh(ConfigHandler.RES_ANKH_ID).setUnlocalizedName("resAnkh").setCreativeTab(CreativeTabs.tabMisc).setMaxStackSize(1).setMaxDamage(4);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(resankh, "Resurrection Ankh");
	}
	
	public static void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(resankh, 1, 4), new Object[] {" g ", "ggg", " g ", 'g', Item.ingotGold});
	}
	
}
