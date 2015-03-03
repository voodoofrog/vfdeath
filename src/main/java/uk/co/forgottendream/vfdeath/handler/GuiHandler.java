package uk.co.forgottendream.vfdeath.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.forgottendream.vfdeath.VFDeath;
import uk.co.forgottendream.vfdeath.client.gui.GuiResurrectionAltar;
import uk.co.forgottendream.vfdeath.inventory.ContainerResurrectionAltar;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {
		NetworkRegistry.instance().registerGuiHandler(VFDeath.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case 0:
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity != null && tileEntity instanceof TileEntityResurrectionAltar) {
				return new ContainerResurrectionAltar(player.inventory, (TileEntityResurrectionAltar) tileEntity);
			}
			break;
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case 0:
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity != null && tileEntity instanceof TileEntityResurrectionAltar) {
				return new GuiResurrectionAltar(player.inventory, (TileEntityResurrectionAltar) tileEntity);
			}
			break;
		}
		
		return null;
	}

}
