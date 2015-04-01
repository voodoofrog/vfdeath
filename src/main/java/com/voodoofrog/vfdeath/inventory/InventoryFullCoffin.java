package com.voodoofrog.vfdeath.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import com.voodoofrog.ribbit.world.IBasicContainer;

public class InventoryFullCoffin implements IBasicContainer
{
	private String name;
	private IBasicContainer upperCoffin;
	private IBasicContainer lowerCoffin;
	private String occupantName;

	public InventoryFullCoffin(String name, IBasicContainer upperCoffin, IBasicContainer lowerCoffin, String occupantName)
	{
		this.name = name;

		if (upperCoffin == null)
		{
			upperCoffin = lowerCoffin;
		}

		if (lowerCoffin == null)
		{
			lowerCoffin = upperCoffin;
		}

		this.upperCoffin = upperCoffin;
		this.lowerCoffin = lowerCoffin;
		this.occupantName = occupantName;
	}

	public InventoryFullCoffin(String name, IBasicContainer upperCoffin, IBasicContainer lowerCoffin)
	{
		this.name = name;

		if (upperCoffin == null)
		{
			upperCoffin = lowerCoffin;
		}

		if (lowerCoffin == null)
		{
			lowerCoffin = upperCoffin;
		}

		this.upperCoffin = upperCoffin;
		this.lowerCoffin = lowerCoffin;
		this.occupantName = "";
	}

	public int getSizeInventory()
	{
		return this.upperCoffin.getSizeInventory() + this.lowerCoffin.getSizeInventory();
	}

	public boolean isPartOfFullCoffin(IInventory inventoryIn)
	{
		return this.upperCoffin == inventoryIn || this.lowerCoffin == inventoryIn;
	}

	public String getName()
	{
		return this.upperCoffin.hasCustomName() ? this.upperCoffin.getName() : (this.lowerCoffin.hasCustomName() ? this.lowerCoffin.getName()
				: this.name);
	}

	public boolean hasCustomName()
	{
		return this.upperCoffin.hasCustomName() || this.lowerCoffin.hasCustomName();
	}

	public IChatComponent getDisplayName()
	{
		if (!this.occupantName.isEmpty())
		{
			return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(),
					this.occupantName));
		}

		return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(),
				new Object[0]));
	}

	public ItemStack getStackInSlot(int index)
	{
		return index >= this.upperCoffin.getSizeInventory() ? this.lowerCoffin.getStackInSlot(index - this.upperCoffin.getSizeInventory())
				: this.upperCoffin.getStackInSlot(index);
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return index >= this.upperCoffin.getSizeInventory() ? this.lowerCoffin.decrStackSize(index - this.upperCoffin.getSizeInventory(), count)
				: this.upperCoffin.decrStackSize(index, count);
	}

	public ItemStack getStackInSlotOnClosing(int index)
	{
		return index >= this.upperCoffin.getSizeInventory() ? this.lowerCoffin.getStackInSlotOnClosing(index - this.upperCoffin.getSizeInventory())
				: this.upperCoffin.getStackInSlotOnClosing(index);
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (index >= this.upperCoffin.getSizeInventory())
		{
			this.lowerCoffin.setInventorySlotContents(index - this.upperCoffin.getSizeInventory(), stack);
		}
		else
		{
			this.upperCoffin.setInventorySlotContents(index, stack);
		}
	}

	public int getInventoryStackLimit()
	{
		return this.upperCoffin.getInventoryStackLimit();
	}

	public void markDirty()
	{
		this.upperCoffin.markDirty();
		this.lowerCoffin.markDirty();
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.upperCoffin.isUseableByPlayer(player) && this.lowerCoffin.isUseableByPlayer(player);
	}

	public void openInventory(EntityPlayer player)
	{
		this.upperCoffin.openInventory(player);
		this.lowerCoffin.openInventory(player);
	}

	public void closeInventory(EntityPlayer player)
	{
		this.upperCoffin.closeInventory(player);
		this.lowerCoffin.closeInventory(player);
	}

	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	public int getField(int id)
	{
		return 0;
	}

	public void setField(int id, int value)
	{
	}

	public int getFieldCount()
	{
		return 0;
	}

	public String getGuiID()
	{
		return this.upperCoffin.getGuiID();
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerCoffin(playerInventory, this, playerIn);
	}

	public void clear()
	{
		this.upperCoffin.clear();
		this.lowerCoffin.clear();
	}
}