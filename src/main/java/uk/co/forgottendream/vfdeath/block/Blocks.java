package uk.co.forgottendream.vfdeath.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import uk.co.forgottendream.vfdeath.item.ItemResurrectionAnkh;
import uk.co.forgottendream.vfdeath.item.Items;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {

	public static Block altar;
	
	public static void initialize() {
		altar = new BlockResurrectionAltar(ConfigHandler.ALTAR_ID).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("resAltar").setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerBlock(altar, "ResAltar");
	}
	
	public static void addNames() {
		LanguageRegistry.addName(altar, "Resurrection Altar");
	}
	
	public static void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(altar), new Object[] {"DDD ", "D D", "DDD", 'D', Block.dirt});
	}
	
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityResurrectionAltar.class, "tileEntityAltar");
	}
	
}
