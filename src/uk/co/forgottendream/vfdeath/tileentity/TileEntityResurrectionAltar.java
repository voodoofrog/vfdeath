package uk.co.forgottendream.vfdeath.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import uk.co.forgottendream.vfdeath.item.ItemResurrectionAnkh;
import uk.co.forgottendream.vfdeath.item.Items;

public class TileEntityResurrectionAltar extends TileEntity implements IInventory {
	
	private ItemStack[] slots;
	
	public TileEntityResurrectionAltar() {
		slots = new ItemStack[10];
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack item = getStackInSlot(slot);
		
		if(item != null) {
			if(item.stackSize <= count) {
				setInventorySlotContents(slot, null);
			} else {
				item = item.splitStack(count);
				onInventoryChanged();
			}
		}
		
		return item;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack item = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return item;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack item) {
		slots[slot] = item;
		
		if(item != null && item.stackSize > getInventoryStackLimit()) {
			item.stackSize = getInventoryStackLimit();
		}
		
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "InventoryAltar";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack item) {
		if(item.itemID == Items.resankh.itemID) {
			ItemResurrectionAnkh ankh = (ItemResurrectionAnkh) item.getItem();
			
			if(ankh.isCharged(item.getItemDamage())) {
				return true;
			}
		}
		return false;
	}

}