package com.voodoofrog.vfdeath.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class Blocks
{
	public static Block altar;

	public static void initialize()
	{
		altar = new BlockResurrectionAltar().setHardness(3.5F).setStepSound(Block.soundTypePiston).setUnlocalizedName("resAltar")
				.setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerBlock(altar, "ResAltar");
	}

	public static void addNames()
	{
		LanguageRegistry.addName(altar, "Resurrection Altar");
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
