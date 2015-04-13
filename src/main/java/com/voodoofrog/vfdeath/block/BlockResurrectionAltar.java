package com.voodoofrog.vfdeath.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class BlockResurrectionAltar extends BlockContainer
{
	public BlockResurrectionAltar()
	{
		super(Material.rock);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY,
			float hitZ)
	{
		if (!world.isRemote)
		{
			FMLNetworkHandler.openGui(player, VFDeath.instance, ConfigHandler.ALTAR_GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		this.dropItems(world, pos);
		super.breakBlock(world, pos, state);
	}

	private void dropItems(World world, BlockPos pos)
	{
		Random rand = new Random();
		TileEntity tileEntity = world.getTileEntity(pos);

		if (!(tileEntity instanceof IInventory))
		{
			return;
		}

		IInventory inventory = (IInventory)tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0)
			{
				float randomX = rand.nextFloat() * 0.8F + 0.1F;
				float randomY = rand.nextFloat() * 0.8F + 0.1F;
				float randomZ = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, pos.getX() + randomX, pos.getY() + randomY, pos.getZ() + randomZ, new ItemStack(
						item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound())
				{
					entityItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityResurrectionAltar();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}
}