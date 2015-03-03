package uk.co.forgottendream.vfdeath.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;

public class ContainerResurrectionAltar extends Container {
	
	private TileEntityResurrectionAltar altar;

	public ContainerResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar) {
		this.altar = altar;
		
		//Player's Hotbar
		for(int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(playerInventory, x, 8 + 18 * x, 142));
		}
		
		//Player's Main Inventory
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + 18 * x, 84 + y * 18));
			}
		}
		
		for(int x = 0; x < 10; x++) {
			if(x <= 4) {
				addSlotToContainer(new SlotResurrectionAnkh(altar, x, 52 + 23 * x, 8));
			}
			
			if(x > 4) {
				addSlotToContainer(new SlotResurrectionAnkh(altar, x, 52 + 23 * x - 115, 31));
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return altar.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		//return super.transferStackInSlot(player, slot);
		return null;
	}
	
	public TileEntityResurrectionAltar getTileEntityAltar() {
		return this.altar;
	}

}
