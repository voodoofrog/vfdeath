package com.voodoofrog.vfdeath.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.item.ItemCoffin;
import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;
import com.voodoofrog.vfdeath.misc.Strings;

public class VFDeathItems
{
	public static ItemResurrectionAnkh ankh = new ItemResurrectionAnkh();
	public static ItemCoffin coffinItem = new ItemCoffin();

	public static void registerItems()
	{
		GameRegistry.registerItem(ankh, Strings.ANKH_NAME, null).setUnlocalizedName(Strings.ANKH_UNLOCALIZED).setCreativeTab(VFDeath.vfdeathTab)
				.setMaxStackSize(1).setMaxDamage(4);
		GameRegistry.registerItem(coffinItem, Strings.COFFIN_ITEM_NAME, null).setUnlocalizedName(Strings.COFFIN_UNLOCALIZED)
				.setCreativeTab(VFDeath.vfdeathTab).setMaxStackSize(1);
	}
}
