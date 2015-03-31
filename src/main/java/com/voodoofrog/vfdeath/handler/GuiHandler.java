package com.voodoofrog.vfdeath.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.voodoofrog.ribbit.world.IBasicContainer;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.block.BlockCoffin;
import com.voodoofrog.vfdeath.client.gui.GuiCoffin;
import com.voodoofrog.vfdeath.client.gui.GuiGraveInventory;
import com.voodoofrog.vfdeath.client.gui.GuiResurrectionAltar;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.inventory.ContainerCoffin;
import com.voodoofrog.vfdeath.inventory.ContainerGrave;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.inventory.InventoryGrave;
import com.voodoofrog.vfdeath.tileentity.TileEntityCoffin;
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
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		switch (guiId)
		{
		case 0:
			if (tileEntity != null && tileEntity instanceof TileEntityResurrectionAltar)
			{
				return new ContainerResurrectionAltar(player.inventory, (TileEntityResurrectionAltar)tileEntity);
			}
			break;
		case 1:
			if (tileEntity != null && tileEntity instanceof TileEntityCoffin)
			{
				Block block = world.getBlockState(pos).getBlock();
				
				if (block instanceof BlockCoffin)
				{
					IBasicContainer container = ((BlockCoffin)block).getContainer(world, pos);
					return new ContainerCoffin(player.inventory, container, player);
				}
			}
			break;
		case 2:
			ExtendedPlayer props = ExtendedPlayer.get(player);
			return new ContainerGrave(player.inventory, props.getGraveInventory(), player);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		switch (guiId)
		{
		case 0:
			if (tileEntity != null && tileEntity instanceof TileEntityResurrectionAltar)
			{
				return new GuiResurrectionAltar(player.inventory, (TileEntityResurrectionAltar)tileEntity);
			}
			break;
		case 1:
			if (tileEntity != null && tileEntity instanceof TileEntityCoffin)
			{
				Block block = world.getBlockState(pos).getBlock();
				
				if (block instanceof BlockCoffin)
				{
					IBasicContainer container = ((BlockCoffin)block).getContainer(world, pos);
					return new GuiCoffin(player.inventory, container);
				}
			}
			break;
		case 2:
			ExtendedPlayer props = ExtendedPlayer.get(player);
			return new GuiGraveInventory(player.inventory, props.getGraveInventory());
		}

		return null;
	}
}
