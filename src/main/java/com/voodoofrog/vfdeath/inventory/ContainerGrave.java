package com.voodoofrog.vfdeath.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGrave extends Container
{
	private IInventory graveInventory;
	private int numRows;
	private boolean isFullCoffin;

	public ContainerGrave(IInventory playerInventory, IInventory graveInventory, EntityPlayer player)
	{
		this.graveInventory = graveInventory;
		this.numRows = 1;
		this.isFullCoffin = false;

		graveInventory.openInventory(player);
		int i = (this.numRows - 2) * 18;
		int j;
		int k;

		this.addSlotToContainer(new SlotCoffin(graveInventory, 0, 30, 35));

		for (j = 0; j < 3; ++j)
		{
			for (k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + i));
			}
		}

		for (j = 0; j < 9; ++j)
		{
			this.addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 160 + i));
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return null;
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		this.graveInventory.closeInventory(playerIn);
	}
}