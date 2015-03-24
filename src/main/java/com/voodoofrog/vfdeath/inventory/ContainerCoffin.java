package com.voodoofrog.vfdeath.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCoffin extends Container
{
	private IInventory coffinInventory;
	private int numRows;
	private boolean isFullCoffin;

	public ContainerCoffin(IInventory playerInventory, IInventory coffinInventory, EntityPlayer player)
	{
		this.coffinInventory = coffinInventory;
		this.numRows = 0;
		this.isFullCoffin = false;

		if (coffinInventory.getSizeInventory() > 23)
		{
			this.numRows = (coffinInventory.getSizeInventory() - 10) / 9;
			this.isFullCoffin = true;
		}
		else
		{
			this.numRows = (coffinInventory.getSizeInventory() - 5) / 9;
		}

		coffinInventory.openInventory(player);
		int i = (this.numRows - 2) * 18;
		int j;
		int k;

		this.addSlots(coffinInventory, 0, 0, 94);

		if (this.isFullCoffin)
		{
			this.addSlots(coffinInventory, 23, 2, 117);
		}

		for (j = 0; j < 3; ++j)
		{
			for (k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 104 + j * 18 + i));
			}
		}

		for (j = 0; j < 9; ++j)
		{
			this.addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 162 + i));
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.coffinInventory.isUseableByPlayer(playerIn);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < this.numRows * 9)
			{
				if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		this.coffinInventory.closeInventory(playerIn);
	}

	public IInventory getLowerCoffinInventory()
	{
		return this.coffinInventory;
	}

	public void addSlots(IInventory coffinInventory, int offset, int PosYOffset, int ankhsPosY)
	{
		for (int j = 0; j < 2; ++j)
		{
			for (int k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new Slot(coffinInventory, (k + j * 9) + offset, 8 + k * 18, 18 + (j + PosYOffset) * 18));
			}
		}

		for (int l = 0; l < 5; ++l)
		{
			this.addSlotToContainer(new SlotResurrectionAnkh(coffinInventory, ((2 * 9) + l) + offset, 52 + 23 * l, ankhsPosY));
		}
	}
}