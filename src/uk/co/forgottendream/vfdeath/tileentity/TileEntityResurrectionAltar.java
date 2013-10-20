package uk.co.forgottendream.vfdeath.tileentity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import uk.co.forgottendream.vfdeath.ModInfo;
import uk.co.forgottendream.vfdeath.Teleportation;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
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

		if (item != null) {
			if (item.stackSize <= count) {
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

		if (item != null && item.stackSize > getInventoryStackLimit()) {
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
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack item) {
		if (item.itemID == Items.resankh.itemID) {
			ItemResurrectionAnkh ankh = (ItemResurrectionAnkh) item.getItem();

			if (ankh.isCharged(item.getItemDamage())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);

			if (stack != null) {
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte) i);
				stack.writeToNBT(item);
				items.appendTag(item);
			}
		}

		compound.setTag("Items", items);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList items = compound.getTagList("Items");

		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound) items.tagAt(i);
			int slot = item.getByte("Slot");

			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}
	}

	private void removeAnkhs() {
		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack item = getStackInSlot(i);

			if(item != null) {
				if(item.getItem() instanceof ItemResurrectionAnkh) {
					ItemResurrectionAnkh ankh = (ItemResurrectionAnkh) item.getItem();

					if (ankh.hasEffect(item)) {
						setInventorySlotContents(i, null);
					}
				}
			}
		}
	}
	
	public void receiveResButtonEvent(byte buttonID, byte ankhs, EntityPlayer player, String playerName) {
		switch (buttonID) {
		case 0:
			EntityPlayer resPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playerName);
			int healthGained = ankhs * 2;
			int healthMod = -20 + healthGained;

			if (resPlayer != null) {
				NBTTagCompound compound = resPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				compound.setInteger("MaxHP", healthMod);
				compound.setBoolean("IsDead", false);
				AttributeInstance attributeinstance = resPlayer.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);

				try {
					attributeinstance.removeModifier(attributeinstance.getModifier(ConfigHandler.HEALTH_MOD_UUID));
				} catch (Exception ex) {
				}

				attributeinstance.applyModifier(new AttributeModifier(ConfigHandler.HEALTH_MOD_UUID, ModInfo.ID.toLowerCase() + ".healthmod", (double) healthMod, 0));
				resPlayer.setHealth(healthGained);

				if (!resPlayer.capabilities.isCreativeMode) {
					resPlayer.capabilities.allowFlying = false;
					resPlayer.capabilities.disableDamage = false;
					resPlayer.removePotionEffect(Potion.invisibility.getId());
					resPlayer.sendPlayerAbilities();
				}
				
				removeAnkhs();
				player.closeScreen();
				
				//TODO: add lightning strike, add new death screen before ghost respawn
				if (player.dimension == 0) {
					Teleportation.teleportEntity(this.worldObj, resPlayer, this.worldObj.provider.dimensionId, getRandomAltarSpawnPoint(), resPlayer.rotationYaw);
				} else {
					//cannot res
				}
			}
			break;
		}
	}
	
    private ChunkCoordinates getRandomAltarSpawnPoint()
    {
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);

        int spawnFuzz = 10;
        int spawnFuzzHalf = spawnFuzz / 2;

        chunkcoordinates.posX += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
        chunkcoordinates.posZ += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
        chunkcoordinates.posY = this.worldObj.getTopSolidOrLiquidBlock(chunkcoordinates.posX, chunkcoordinates.posZ);

        return chunkcoordinates;
    }

}