package com.voodoofrog.vfdeath.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.client.gui.GuiResurrectionAltar;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class GuiHandler implements IGuiHandler
{
	public GuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(VFDeath.instance, this);
	}

	@Override
	public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (guiId)
		{
		case 0:
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity != null && tileEntity instanceof TileEntityResurrectionAltar)
			{
				return new ContainerResurrectionAltar(player.inventory, (TileEntityResurrectionAltar)tileEntity);
			}
			break;
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (guiId)
		{
		case 0:
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity != null && tileEntity instanceof TileEntityResurrectionAltar)
			{
				return new GuiResurrectionAltar(player.inventory, (TileEntityResurrectionAltar)tileEntity);
			}
			break;
		}

		return null;
	}
}
