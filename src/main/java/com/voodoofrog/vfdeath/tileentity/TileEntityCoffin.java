package com.voodoofrog.vfdeath.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.voodoofrog.ribbit.world.IBasicContainer;
import com.voodoofrog.vfdeath.block.BlockCoffin;
import com.voodoofrog.vfdeath.inventory.ContainerCoffin;
import com.voodoofrog.vfdeath.inventory.InventoryFullCoffin;
import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCoffin extends TileEntity implements ITickable, IInventory, IInteractionObject, IBasicContainer
{
	private static final int[] slotsItems = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
	private static final int[] slotsAnkhs = new int[] { 18, 19, 20, 21, 22 };
	private ItemStack[] coffinContents = new ItemStack[23];
	public boolean adjacentCoffinChecked;
	public TileEntityCoffin adjacentCoffinZNeg;
	public TileEntityCoffin adjacentCoffinXPos;
	public TileEntityCoffin adjacentCoffinXNeg;
	public TileEntityCoffin adjacentCoffinZPos;
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;
	private String customName;
	private UUID occupantUUID;
	private HashMap<Integer, Integer> bindingProgress;

	public TileEntityCoffin()
	{
		this.bindingProgress = new HashMap<Integer, Integer>();

		for (int i = 0; i < slotsAnkhs.length; i++)
		{
			this.bindingProgress.put(slotsAnkhs[i], 0);
		}
	}

	public int getSizeInventory()
	{
		return 23;
	}

	public ItemStack getStackInSlot(int index)
	{
		return this.coffinContents[index];
	}

	public ItemStack decrStackSize(int index, int count)
	{
		if (this.coffinContents[index] != null)
		{
			ItemStack itemstack;

			if (this.coffinContents[index].stackSize <= count)
			{
				itemstack = this.coffinContents[index];
				this.coffinContents[index] = null;
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.coffinContents[index].splitStack(count);

				if (this.coffinContents[index].stackSize == 0)
				{
					this.coffinContents[index] = null;
				}

				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	public ItemStack removeStackFromSlot(int index)
	{
		if (this.coffinContents[index] != null)
		{
			ItemStack itemstack = this.coffinContents[index];
			this.coffinContents[index] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.coffinContents[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		if (this.bindingProgress.containsKey(index))
			this.bindingProgress.put(index, 0);

		this.markDirty();
	}

	public String getName()
	{
		return this.hasCustomName() ? this.customName : "container.vfdeath.coffin";
	}

	public boolean hasCustomName()
	{
		return this.customName != null && this.customName.length() > 0;
	}

	public void setCustomName(String name)
	{
		this.customName = name;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (!compound.getString("Occupant").isEmpty())
			this.occupantUUID = UUID.fromString(compound.getString("Occupant"));

		this.coffinContents = new ItemStack[this.getSizeInventory()];

		NBTTagList itemsTagList = compound.getTagList("Items", 10);

		for (int i = 0; i < itemsTagList.tagCount(); ++i)
		{
			NBTTagCompound itemCompound = itemsTagList.getCompoundTagAt(i);
			int j = itemCompound.getByte("Slot") & 255;

			if (j >= 0 && j < this.coffinContents.length)
			{
				this.coffinContents[j] = ItemStack.loadItemStackFromNBT(itemCompound);

				if (this.bindingProgress.containsKey(j))
					this.bindingProgress.put(j, itemCompound.getInteger("BindingProgress"));
			}
		}

		if (compound.hasKey("CustomName", 8))
		{
			this.customName = compound.getString("CustomName");
		}
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		if (this.occupantUUID != null)
		{
			compound.setString("Occupant", this.occupantUUID.toString());
		}
		else
		{
			compound.setString("Occupant", "");
		}

		NBTTagList itemsTagList = new NBTTagList();

		for (int i = 0; i < this.coffinContents.length; ++i)
		{
			if (this.coffinContents[i] != null)
			{
				NBTTagCompound itemCompound = new NBTTagCompound();
				itemCompound.setByte("Slot", (byte)i);

				if (this.bindingProgress.containsKey(i))
				{
					itemCompound.setInteger("BindingProgress", this.bindingProgress.get(i));
				}

				this.coffinContents[i].writeToNBT(itemCompound);
				itemsTagList.appendTag(itemCompound);
			}
		}

		compound.setTag("Items", itemsTagList);

		if (this.hasCustomName())
		{
			compound.setString("CustomName", this.customName);
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.pos, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.getNbtCompound());
		System.out.println("got data packet");
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D,
				(double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		this.adjacentCoffinChecked = false;
	}

	private void setChecked(TileEntityCoffin teCoffin, EnumFacing facing)
	{
		if (teCoffin.isInvalid())
		{
			this.adjacentCoffinChecked = false;
		}
		else if (this.adjacentCoffinChecked)
		{
			switch (TileEntityCoffin.SwitchEnumFacing.ordinals[facing.ordinal()])
			{
			case 1:
				if (this.adjacentCoffinZNeg != teCoffin)
				{
					this.adjacentCoffinChecked = false;
				}

				break;
			case 2:
				if (this.adjacentCoffinZPos != teCoffin)
				{
					this.adjacentCoffinChecked = false;
				}

				break;
			case 3:
				if (this.adjacentCoffinXPos != teCoffin)
				{
					this.adjacentCoffinChecked = false;
				}

				break;
			case 4:
				if (this.adjacentCoffinXNeg != teCoffin)
				{
					this.adjacentCoffinChecked = false;
				}
			}
		}
	}

	public void checkForAdjacentCoffins()
	{
		if (!this.adjacentCoffinChecked)
		{
			this.adjacentCoffinChecked = true;
			this.adjacentCoffinXNeg = this.getAdjacentCoffin(EnumFacing.WEST);
			this.adjacentCoffinXPos = this.getAdjacentCoffin(EnumFacing.EAST);
			this.adjacentCoffinZNeg = this.getAdjacentCoffin(EnumFacing.NORTH);
			this.adjacentCoffinZPos = this.getAdjacentCoffin(EnumFacing.SOUTH);
		}
	}

	protected TileEntityCoffin getAdjacentCoffin(EnumFacing facing)
	{
		BlockPos blockpos = this.pos.offset(facing);

		if (this.isCoffin(blockpos))
		{
			TileEntity tileentity = this.worldObj.getTileEntity(blockpos);

			if (tileentity instanceof TileEntityCoffin)
			{
				TileEntityCoffin tileentitycoffin = (TileEntityCoffin)tileentity;
				tileentitycoffin.setChecked(this, facing.getOpposite());
				return tileentitycoffin;
			}
		}

		return null;
	}

	private boolean isCoffin(BlockPos pos)
	{
		if (this.worldObj == null)
		{
			return false;
		}
		else
		{
			Block block = this.worldObj.getBlockState(pos).getBlock();
			return block instanceof BlockCoffin;
		}
	}

	/**
	 * Updates the JList with a new model.
	 */
	public void update()
	{
		this.checkForAdjacentCoffins();
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;
		float f;

		if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
		{
			this.numPlayersUsing = 0;
			f = 5.0F;
			List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)i - f), (double)((float)j - f),
					(double)((float)k - f), (double)((float)(i + 1) + f), (double)((float)(j + 1) + f), (double)((float)(k + 1) + f)));
			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				EntityPlayer entityplayer = (EntityPlayer)iterator.next();

				if (entityplayer.openContainer instanceof ContainerCoffin)
				{
					IInventory iinventory = ((ContainerCoffin)entityplayer.openContainer).getLowerCoffinInventory();

					if (iinventory == this || iinventory instanceof InventoryFullCoffin
							&& ((InventoryFullCoffin)iinventory).isPartOfFullCoffin(this))
					{
						++this.numPlayersUsing;
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		f = 0.1F;
		double d2;

		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentCoffinZNeg == null && this.adjacentCoffinXNeg == null)
		{
			double d1 = (double)i + 0.5D;
			d2 = (double)k + 0.5D;

			if (this.adjacentCoffinZPos != null)
			{
				d2 += 0.5D;
			}

			if (this.adjacentCoffinXPos != null)
			{
				d1 += 0.5D;
			}

			this.worldObj.playSoundEffect(d1, (double)j + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
		{
			float f1 = this.lidAngle;

			if (this.numPlayersUsing > 0)
			{
				this.lidAngle += f;
			}
			else
			{
				this.lidAngle -= f;
			}

			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if (this.lidAngle < f2 && f1 >= f2 && this.adjacentCoffinZNeg == null && this.adjacentCoffinXNeg == null)
			{
				d2 = (double)i + 0.5D;
				double d0 = (double)k + 0.5D;

				if (this.adjacentCoffinZPos != null)
				{
					d0 += 0.5D;
				}

				if (this.adjacentCoffinXPos != null)
				{
					d2 += 0.5D;
				}

				this.worldObj.playSoundEffect(d2, (double)j + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}

		if (this.canBindAnkhs())
		{
			this.updateAnkhs();
		}
	}

	private boolean canBindAnkhs()
	{
		if (this.occupantUUID == null)
		{
			return false;
		}
		else
		{
			for (int i = 0; i < slotsAnkhs.length; i++)
			{
				if (this.coffinContents[slotsAnkhs[i]] != null)
				{
					if (this.coffinContents[slotsAnkhs[i]].getItem() instanceof ItemResurrectionAnkh)
					{
						ItemResurrectionAnkh ankh = (ItemResurrectionAnkh)this.coffinContents[slotsAnkhs[i]].getItem();

						if (!ankh.hasOwner(this.coffinContents[slotsAnkhs[i]]))
						{
							return true;
						}
					}
				}
			}

			return false;
		}
	}

	private void updateAnkhs()
	{
		Iterator<Entry<Integer, Integer>> bindingProgressIterator = this.bindingProgress.entrySet().iterator();
		boolean flag = false;

		while (bindingProgressIterator.hasNext())
		{
			Entry<Integer, Integer> pair = bindingProgressIterator.next();

			if (this.coffinContents[pair.getKey()] != null)
			{
				ItemStack stack = this.coffinContents[pair.getKey()];

				if (stack.getItem() instanceof ItemResurrectionAnkh)
				{
					ItemResurrectionAnkh ankh = (ItemResurrectionAnkh)stack.getItem();

					if (ankh.getOwner(stack) == null)
					{
						if (pair.getValue() >= 1000) // TODO: Make this configurable
						{
							ankh.setOwner(stack, this.occupantUUID);
							flag = true;
						}
						else
						{
							int progress = pair.getValue();
							progress++;
							pair.setValue(progress);
						}
					}
				}
			}
		}

		if (flag)
			this.markDirty();
	}

	public boolean receiveClientEvent(int id, int type)
	{
		if (id == 1)
		{
			this.numPlayersUsing = type;
			return true;
		}
		else
		{
			return super.receiveClientEvent(id, type);
		}
	}

	public void openInventory(EntityPlayer player)
	{
		if (!player.isSpectator())
		{
			if (this.numPlayersUsing < 0)
			{
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
			this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		}
	}

	public void closeInventory(EntityPlayer player)
	{
		if (!player.isSpectator() && this.getBlockType() instanceof BlockCoffin)
		{
			--this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
			this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		}
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	/**
	 * invalidates a tile entity
	 */
	public void invalidate()
	{
		super.invalidate();
		this.updateContainingBlockInfo();
		this.checkForAdjacentCoffins();
	}

	public String getGuiID()
	{
		return null;
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerCoffin(playerInventory, this, playerIn);
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

	public void clear()
	{
		for (int i = 0; i < this.coffinContents.length; ++i)
		{
			this.coffinContents[i] = null;
		}
	}

	static final class SwitchEnumFacing
	{
		static final int[] ordinals = new int[EnumFacing.values().length];

		static
		{
			try
			{
				ordinals[EnumFacing.NORTH.ordinal()] = 1;
			}
			catch (NoSuchFieldError var4)
			{
				;
			}

			try
			{
				ordinals[EnumFacing.SOUTH.ordinal()] = 2;
			}
			catch (NoSuchFieldError var3)
			{
				;
			}

			try
			{
				ordinals[EnumFacing.EAST.ordinal()] = 3;
			}
			catch (NoSuchFieldError var2)
			{
				;
			}

			try
			{
				ordinals[EnumFacing.WEST.ordinal()] = 4;
			}
			catch (NoSuchFieldError var1)
			{
				;
			}
		}
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(getPos().add(-1, 0, -1), getPos().add(2, 2, 2));
	}

	public boolean hasOccupant()
	{
		return this.occupantUUID != null;
	}

	public UUID getOccupant()
	{
		return this.occupantUUID;
	}

	public void setOccupant(UUID playerUUID, boolean withAdjacent)
	{
		this.occupantUUID = playerUUID;

		if (withAdjacent)
		{
			Iterator facingIterator = EnumFacing.Plane.HORIZONTAL.iterator();

			while (facingIterator.hasNext())
			{
				TileEntityCoffin coffin = getAdjacentCoffin((EnumFacing)facingIterator.next());

				if (coffin != null)
				{
					coffin.setOccupant(playerUUID, false);
					this.worldObj.markBlockForUpdate(coffin.pos);
				}
			}
		}
		this.worldObj.markBlockForUpdate(this.pos);
	}

	public void clearOccupant(boolean withAdjacent)
	{
		this.occupantUUID = null;

		if (withAdjacent)
		{
			Iterator facingIterator = EnumFacing.Plane.HORIZONTAL.iterator();

			while (facingIterator.hasNext())
			{
				TileEntityCoffin coffin = getAdjacentCoffin((EnumFacing)facingIterator.next());

				if (coffin != null)
				{
					coffin.setOccupant(null, false);
					this.worldObj.markBlockForUpdate(coffin.pos);
				}
			}
		}
		this.worldObj.markBlockForUpdate(this.pos);
	}
}