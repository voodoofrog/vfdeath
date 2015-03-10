package com.voodoofrog.vfdeath.creativetab;

import com.voodoofrog.vfdeath.item.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class VFDeathCreativeTab extends CreativeTabs
{
	public VFDeathCreativeTab(int index, String label)
	{
		super(index, label);
	}

	@Override
	public Item getTabIconItem()
	{
		return Items.resankh;
	}
}
