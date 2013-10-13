package uk.co.forgottendream.vfdeath.inventory;

import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerResurrectionAltar extends Container {
	
	TileEntityResurrectionAltar altar;

	public ContainerResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar) {
		this.altar = altar;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return altar.isUseableByPlayer(player);
	}

}
