package com.voodoofrog.vfdeath.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.init.VFDeathItems;

public class VFDeathRecipesCrafting
{
	public static void addRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(VFDeathItems.ankh, 1, 4), new Object[] {
			" g ", 
			"ggg", 
			" g ", 'g', Items.gold_ingot });
		GameRegistry.addRecipe(new ItemStack(VFDeathBlocks.altar), new Object[] { 
			"DDD", 
			"D D", 
			"DDD", 'D', Blocks.dirt });
	}
}
