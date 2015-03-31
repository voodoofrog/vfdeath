package com.voodoofrog.vfdeath.inventory;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.item.ItemCoffin;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;

public class InventoryGrave implements IInventory
{
	private ItemStack[] inventory;
	private WeakReference<EntityPlayer> player;

	public InventoryGrave(EntityPlayer player)
	{
		this.inventory = new ItemStack[1];
		this.player = new WeakReference<EntityPlayer>(player);
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return null;
	}

	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack stack = this.getStackInSlot(index);

		if (stack != null)
		{
			if (stack.stackSize <= count)
			{
				this.setInventorySlotContents(index, null);
			}
			else
			{
				stack = stack.splitStack(count);
				this.markDirty();
			}
		}

		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		ItemStack stack = this.getStackInSlot(index);
		this.setInventorySlotContents(index, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.inventory[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void markDirty()
	{
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		if (!player.worldObj.isRemote)
			VFDeath.packetDispatcher.sendTo(new SyncPlayerPropsMessage(player), (EntityPlayerMP)player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (stack.getItem() instanceof ItemCoffin)
			return true;

		return false;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.inventory.length; i++)
		{
			this.inventory[i] = null;
		}
	}

	public ItemStack[] getInventory()
	{
		return this.inventory;
	}

}
