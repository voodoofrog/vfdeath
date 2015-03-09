package com.voodoofrog.vfdeath.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.voodoofrog.vfdeath.TeleportationHelper;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.entity.effect.EntityVisualLightningBolt;
import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;
import com.voodoofrog.vfdeath.item.Items;
import com.voodoofrog.vfdeath.server.handler.PlayerEventHandler;

public class TileEntityResurrectionAltar extends TileEntity implements IInventory
{
	private ItemStack[] inventory;
	private String name = "InventoryAltar";

	public TileEntityResurrectionAltar()
	{
		this.inventory = new ItemStack[10];
	}

	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		ItemStack item = getStackInSlot(slot);

		if (item != null)
		{
			if (item.stackSize <= count)
			{
				this.setInventorySlotContents(slot, null);
			}
			else
			{
				item = item.splitStack(count);
				this.markDirty();
			}
		}

		return item;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack item = getStackInSlot(slot);
		this.setInventorySlotContents(slot, null);
		return item;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.inventory[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) < 64;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack)
	{
		if (stack.getItem() == Items.resankh)
		{
			ItemResurrectionAnkh ankh = (ItemResurrectionAnkh)stack.getItem();

			if (ankh.isCharged(stack.getItemDamage()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagList items = new NBTTagList();

		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack stack = getStackInSlot(i);

			if (stack != null)
			{
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte)i);
				stack.writeToNBT(item);
				items.appendTag(item);
			}
		}

		compound.setTag("Items", items);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		NBTTagList items = compound.getTagList(("Items"), compound.getId());

		for (int i = 0; i < items.tagCount(); i++)
		{
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getByte("Slot");

			if (slot >= 0 && slot < this.getSizeInventory())
			{
				this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}
	}

	private void removeAnkhs()
	{
		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack item = getStackInSlot(i);

			if (item != null)
			{
				if (item.getItem() instanceof ItemResurrectionAnkh)
				{
					ItemResurrectionAnkh ankh = (ItemResurrectionAnkh)item.getItem();

					if (ankh.hasEffect(item))
					{
						this.setInventorySlotContents(i, null);
					}
				}
			}
		}
	}

	public void receiveResButtonEvent(byte buttonID, byte ankhs, EntityPlayer player, String playerName)
	{
		switch (buttonID)
		{
		case 0:
			EntityPlayer resPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playerName);

			if (resPlayer != null)
			{
				ExtendedPlayer props = ExtendedPlayer.get(resPlayer);
				
				if (props.getIsDead() && ankhs > 0)
				{
					PlayerEventHandler.ghostPlayer(resPlayer, false);
					props.gainHearts(ankhs);
					this.removeAnkhs();
					player.closeScreen();
					
					// TODO: add new death screen before ghost respawn
					if (player.dimension == 0)
					{
						BlockPos coords = getRandomAltarSpawnPoint();
						// Teleportation.teleportEntity(this.worldObj,
						// resPlayer, this.worldObj.provider.dimensionId,
						// coords, resPlayer.rotationYaw);
						new TeleportationHelper(MinecraftServer.getServer().worldServerForDimension(this.worldObj.provider.getDimensionId()))
								.teleport(player, this.worldObj, coords);
						this.worldObj
								.addWeatherEffect(new EntityVisualLightningBolt(this.worldObj, coords.getX(), coords.getY(), coords.getZ()));
					}
					else
					{
						// player is not in the overworld
					}
				}
			}
			break;
		}
	}

	private BlockPos getRandomAltarSpawnPoint()
	{
		int spawnFuzz = 10;
		int spawnFuzzHalf = spawnFuzz / 2;
		int x = this.pos.getX() + this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
		int z = this.pos.getZ() + this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;

		return this.worldObj.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(name);
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
		for (int i = 0; i < this.inventory.length; ++i)
		{
			this.inventory[i] = null;
		}
	}
}