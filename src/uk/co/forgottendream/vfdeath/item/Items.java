package uk.co.forgottendream.vfdeath.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;

public class Items {

	public static Item regenheart;
	
	public static void initialize() {
		regenheart = new ItemRegenerationHeart(ConfigHandler.REGEN_HEART_ID).setUnlocalizedName("regenHeart").setCreativeTab(CreativeTabs.tabMisc).setMaxStackSize(1);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(regenheart, "Heart of Regeneration");
	}
	
}
