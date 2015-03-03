package com.voodoofrog.vfdeath.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class Items
{
	public static Item resankh;

	public static void initialize()
	{
		resankh = new ItemResurrectionAnkh().setUnlocalizedName("resAnkh").setCreativeTab(CreativeTabs.tabMisc).setMaxStackSize(1)
				.setMaxDamage(4);
	}

	public static void addNames()
	{
		LanguageRegistry.addName(resankh, "Resurrection Ankh");
	}

	public static void addRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(resankh, 1, 4), new Object[] { " g ", "ggg", " g ", 'g', net.minecraft.init.Items.gold_ingot });
	}
}