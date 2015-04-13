package com.voodoofrog.vfdeath.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.voodoofrog.vfdeath.block.BlockCoffin;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;

public class ItemCoffin extends Item
{
	/**
	 * Called when a Block is right-clicked with this Item
	 * 
	 * @param pos
	 *            The block being right-clicked
	 * @param side
	 *            The side being right-clicked
	 */
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else if (side != EnumFacing.UP)
		{
			return false;
		}
		else
		{
			IBlockState groundBlockState = worldIn.getBlockState(pos);
			Block block = groundBlockState.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag)
			{
				pos = pos.up();
			}

			int i = MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing facing = EnumFacing.getHorizontal(i);
			EnumFacing rotatedFacing = facing.rotateY();
			BlockPos headPos = pos.offset(facing);
			boolean flag1 = block.isReplaceable(worldIn, headPos);
			boolean flag2 = worldIn.isAirBlock(pos) || flag;
			boolean flag3 = worldIn.isAirBlock(headPos) || flag1;

			if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(headPos, side, stack))
			{
				if (flag2 && flag3 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down())
						&& World.doesBlockHaveSolidTopSurface(worldIn, headPos.down()))
				{
					int j = facing.getHorizontalIndex();
					IBlockState coffinBlockState = VFDeathBlocks.coffin.getDefaultState().withProperty(BlockBed.FACING, rotatedFacing);

					if (((BlockCoffin)coffinBlockState.getBlock()).canPlaceBlockAt(worldIn, pos, rotatedFacing))
					{
						worldIn.setBlockState(pos, coffinBlockState, 3);
					}
					else
					{
						return false;
					}
					--stack.stackSize;
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
}