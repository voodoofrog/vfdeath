package com.voodoofrog.vfdeath.block;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class Blocks
{
	public static BlockResurrectionAltar altar;

	public static void initialize()
	{
		altar = new BlockResurrectionAltar();
		//GameRegistry.registerBlock(altar, altar.getName());
	}

	public static void addRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(altar), new Object[] { "DDD ", "D D", "DDD", 'D', net.minecraft.init.Blocks.dirt });
	}

	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityResurrectionAltar.class, "tileEntityAltar");
	}
}
