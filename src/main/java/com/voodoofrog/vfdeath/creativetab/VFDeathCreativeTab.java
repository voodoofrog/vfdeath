package com.voodoofrog.vfdeath.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.voodoofrog.vfdeath.init.VFDeathItems;

public class VFDeathCreativeTab extends CreativeTabs
{
	public VFDeathCreativeTab(int index, String label)
	{
		super(index, label);
	}

	@Override
	public Item getTabIconItem()
	{
		return VFDeathItems.ankh;
	}
}
