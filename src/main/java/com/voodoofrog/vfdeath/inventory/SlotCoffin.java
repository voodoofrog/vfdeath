package com.voodoofrog.vfdeath.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.voodoofrog.vfdeath.item.ItemCoffin;

public class SlotCoffin extends Slot
{
	public SlotCoffin(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack item)
	{
		return item.getItem() instanceof ItemCoffin;
	}
	
	@Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }
}
