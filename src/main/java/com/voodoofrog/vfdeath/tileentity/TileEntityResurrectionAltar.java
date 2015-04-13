package com.voodoofrog.vfdeath.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.entity.effect.EntityVisualLightningBolt;
import com.voodoofrog.vfdeath.init.VFDeathItems;
import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;
import com.voodoofrog.vfdeath.network.client.ResurrectionResponseMessage;
import com.voodoofrog.vfdeath.util.TeleportationHelper;

public class TileEntityResurrectionAltar extends TileEntity implements IInventory
{
	private ItemStack[] inventory;
	private String name = "InventoryAltar";
	private List<UUID> playerList;

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
				this.removeAnkhOwnerFromList(slot);
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
			this.addAnkhOwnerToList(slot);
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
		this.markForUpdate();
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack)
	{
		if (stack.getItem() == VFDeathItems.ankh)
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
		this.playerList = this.getPlayerUUIDListFromAnkhs();
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.pos, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	public void markForUpdate()
	{
		this.worldObj.markBlockForUpdate(this.pos);
	}

	public void receiveResurrectionButtonEvent(EntityPlayer player, UUID playerUUID)
	{
		EntityPlayer resPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(playerUUID);

		if (resPlayer != null)
		{
			ExtendedPlayer props = ExtendedPlayer.get(resPlayer);

			if (props.getIsDead())
			{
				BlockPos coords = getRandomAltarSpawnPoint();

				VFDeath.ghostHandler.ghostPlayer(resPlayer, false);
				props.gainHearts(this.removeAnkhsForPlayerUUID(playerUUID), false);
				player.closeScreen();

				new TeleportationHelper(MinecraftServer.getServer().worldServerForDimension(this.worldObj.provider.getDimensionId())).teleport(
						resPlayer, this.worldObj, coords);
				this.worldObj.addWeatherEffect(new EntityVisualLightningBolt(this.worldObj, coords.getX(), coords.getY(), coords.getZ()));
			}
			else
			{
				VFDeath.packetDispatcher.sendTo(new ResurrectionResponseMessage(0), (EntityPlayerMP)player);
			}
		}
		else
		{
			// player may be offline
			VFDeath.packetDispatcher.sendTo(new ResurrectionResponseMessage(1), (EntityPlayerMP)player);
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

	public List<UUID> getPlayerUUIDList()
	{
		return this.playerList;
	}

	private List<UUID> getPlayerUUIDListFromAnkhs()
	{
		List<UUID> result = new ArrayList<UUID>();

		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack stack = this.getStackInSlot(i);

			if (stack != null)
			{
				if (stack.getItem() instanceof ItemResurrectionAnkh)
				{
					ItemResurrectionAnkh item = (ItemResurrectionAnkh)stack.getItem();

					if (item.hasOwner(stack) && !result.contains(item.getOwner(stack)))
					{
						result.add(item.getOwner(stack));
					}
				}
			}
		}

		return result;
	}

	private void removeAnkhOwnerFromList(int slot)
	{
		ItemStack stack = this.getStackInSlot(slot);

		if (stack != null)
		{
			if (stack.getItem() instanceof ItemResurrectionAnkh)
			{
				ItemResurrectionAnkh item = (ItemResurrectionAnkh)stack.getItem();

				if (item.hasOwner(stack) && this.playerList.contains(item.getOwner(stack)))
				{
					this.playerList.remove(item.getOwner(stack));
				}
			}
		}
	}

	private void addAnkhOwnerToList(int slot)
	{
		ItemStack stack = this.getStackInSlot(slot);

		if (stack != null)
		{
			if (stack.getItem() instanceof ItemResurrectionAnkh)
			{
				ItemResurrectionAnkh item = (ItemResurrectionAnkh)stack.getItem();

				if (item.hasOwner(stack) && !this.playerList.contains(item.getOwner(stack)))
				{
					this.playerList.add(item.getOwner(stack));
				}
			}
		}
	}

	private int removeAnkhsForPlayerUUID(UUID playerUUID)
	{
		int count = 0;

		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack stack = getStackInSlot(i);

			if (stack != null)
			{
				if (stack.getItem() instanceof ItemResurrectionAnkh)
				{
					ItemResurrectionAnkh item = (ItemResurrectionAnkh)stack.getItem();

					if (item.hasEffect(stack) && item.hasOwner(stack))
					{
						if (item.getOwner(stack).equals(playerUUID))
						{
							this.setInventorySlotContents(i, null);
							count++;
						}
					}
				}
			}
		}

		// TODO: May not need this
		this.markForUpdate();
		return count;
	}
}