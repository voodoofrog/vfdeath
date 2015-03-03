package com.voodoofrog.vfdeath.handler;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.client.gui.GuiResurrectionAltar;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
