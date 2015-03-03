package com.voodoofrog.vfdeath.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;

public class SlotResurrectionAnkh extends Slot
{
	public SlotResurrectionAnkh(IInventory inventory, int id, int x, int y)
	{
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack item)
	{
		return item.getItem() instanceof ItemResurrectionAnkh;
	}
}
