package com.voodoofrog.vfdeath.block;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.tileentity.TileEntityCoffin;

public class BlockCoffin extends BlockContainer
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockCoffin()
	{
		super(Material.wood);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	public int getRenderType()
	{
		return 2;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos.north()).getBlock() == this)
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
		}
		else if (worldIn.getBlockState(pos.south()).getBlock() == this)
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
		}
		else if (worldIn.getBlockState(pos.west()).getBlock() == this)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		}
		else if (worldIn.getBlockState(pos.east()).getBlock() == this)
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
		}
		else
		{
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.checkForSurroundingCoffins(worldIn, pos, state);
		Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

		while (iterator.hasNext())
		{
			EnumFacing enumfacing = (EnumFacing)iterator.next();
			BlockPos blockpos1 = pos.offset(enumfacing);
			IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

			if (iblockstate1.getBlock() == this)
			{
				this.checkForSurroundingCoffins(worldIn, blockpos1, iblockstate1);
			}
		}
		
		if (!this.isFullSize(worldIn, pos))
		{
			int meta = this.getMetaFromState(worldIn.getBlockState(pos));
			
			if (meta == 2) // SOUTH
			{
				worldIn.setBlockState(pos.east(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
				worldIn.notifyNeighborsOfStateChange(pos.east(), VFDeathBlocks.coffin);
			}

			if (meta == 3) // NORTH
			{
				worldIn.setBlockState(pos.west(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.NORTH));
				worldIn.notifyNeighborsOfStateChange(pos.west(), VFDeathBlocks.coffin);
			}

			if (meta == 4) // WEST
			{
				worldIn.setBlockState(pos.north(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.WEST));
				worldIn.notifyNeighborsOfStateChange(pos.north(), VFDeathBlocks.coffin);
			}

			if (meta == 5) // EAST
			{
				worldIn.setBlockState(pos.south(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.EAST));
				worldIn.notifyNeighborsOfStateChange(pos.south(), VFDeathBlocks.coffin);
			}
		}
	}

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3)
				.getOpposite();
		state = state.withProperty(FACING, enumfacing);
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.south();
		BlockPos blockpos3 = pos.west();
		BlockPos blockpos4 = pos.east();
		boolean flag = this == worldIn.getBlockState(blockpos1).getBlock();
		boolean flag1 = this == worldIn.getBlockState(blockpos2).getBlock();
		boolean flag2 = this == worldIn.getBlockState(blockpos3).getBlock();
		boolean flag3 = this == worldIn.getBlockState(blockpos4).getBlock();

		if (!flag && !flag1 && !flag2 && !flag3)
		{
			worldIn.setBlockState(pos, state, 3);
		}
		else if (enumfacing.getAxis() == EnumFacing.Axis.X && (flag || flag1))
		{
			if (flag)
			{
				worldIn.setBlockState(blockpos1, state, 3);
			}
			else
			{
				worldIn.setBlockState(blockpos2, state, 3);
			}

			worldIn.setBlockState(pos, state, 3);
		}
		else if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3))
		{
			if (flag2)
			{
				worldIn.setBlockState(blockpos3, state, 3);
			}
			else
			{
				worldIn.setBlockState(blockpos4, state, 3);
			}

			worldIn.setBlockState(pos, state, 3);
		}

		if (stack.hasDisplayName())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityCoffin)
			{
				((TileEntityCoffin)tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}

	public IBlockState checkForSurroundingCoffins(World worldIn, BlockPos pos, IBlockState state)
	{
		if (worldIn.isRemote)
		{
			return state;
		}
		else
		{
			IBlockState iblockstate1 = worldIn.getBlockState(pos.north());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate4 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
			Block block = iblockstate1.getBlock();
			Block block1 = iblockstate2.getBlock();
			Block block2 = iblockstate3.getBlock();
			Block block3 = iblockstate4.getBlock();

			if (block != this && block1 != this)
			{
				boolean flag = block.isFullBlock();
				boolean flag1 = block1.isFullBlock();

				if (block2 == this || block3 == this)
				{
					BlockPos blockpos2 = block2 == this ? pos.west() : pos.east();
					IBlockState iblockstate7 = worldIn.getBlockState(blockpos2.north());
					IBlockState iblockstate8 = worldIn.getBlockState(blockpos2.south());
					enumfacing = EnumFacing.SOUTH;
					EnumFacing enumfacing2;

					if (block2 == this)
					{
						enumfacing2 = (EnumFacing)iblockstate3.getValue(FACING);
					}
					else
					{
						enumfacing2 = (EnumFacing)iblockstate4.getValue(FACING);
					}

					if (enumfacing2 == EnumFacing.NORTH)
					{
						enumfacing = EnumFacing.NORTH;
					}

					Block block6 = iblockstate7.getBlock();
					Block block7 = iblockstate8.getBlock();

					if ((flag || block6.isFullBlock()) && !flag1 && !block7.isFullBlock())
					{
						enumfacing = EnumFacing.SOUTH;
					}

					if ((flag1 || block7.isFullBlock()) && !flag && !block6.isFullBlock())
					{
						enumfacing = EnumFacing.NORTH;
					}
				}
			}
			else
			{
				BlockPos blockpos1 = block == this ? pos.north() : pos.south();
				IBlockState iblockstate5 = worldIn.getBlockState(blockpos1.west());
				IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.east());
				enumfacing = EnumFacing.EAST;
				EnumFacing enumfacing1;

				if (block == this)
				{
					enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
				}
				else
				{
					enumfacing1 = (EnumFacing)iblockstate2.getValue(FACING);
				}

				if (enumfacing1 == EnumFacing.WEST)
				{
					enumfacing = EnumFacing.WEST;
				}

				Block block4 = iblockstate5.getBlock();
				Block block5 = iblockstate6.getBlock();

				if ((block2.isFullBlock() || block4.isFullBlock()) && !block3.isFullBlock() && !block5.isFullBlock())
				{
					enumfacing = EnumFacing.EAST;
				}

				if ((block3.isFullBlock() || block5.isFullBlock()) && !block2.isFullBlock() && !block4.isFullBlock())
				{
					enumfacing = EnumFacing.WEST;
				}
			}

			state = state.withProperty(FACING, enumfacing);
			worldIn.setBlockState(pos, state, 3);
			return state;
		}
	}

	public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = null;
		Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

		while (iterator.hasNext())
		{
			EnumFacing enumfacing1 = (EnumFacing)iterator.next();
			IBlockState iblockstate1 = worldIn.getBlockState(pos.offset(enumfacing1));

			if (iblockstate1.getBlock() == this)
			{
				return state;
			}

			if (iblockstate1.getBlock().isFullBlock())
			{
				if (enumfacing != null)
				{
					enumfacing = null;
					break;
				}

				enumfacing = enumfacing1;
			}
		}

		if (enumfacing != null)
		{
			return state.withProperty(FACING, enumfacing.getOpposite());
		}
		else
		{
			EnumFacing enumfacing2 = (EnumFacing)state.getValue(FACING);

			if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
			{
				enumfacing2 = enumfacing2.getOpposite();
			}

			if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
			{
				enumfacing2 = enumfacing2.rotateY();
			}

			if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
			{
				enumfacing2 = enumfacing2.getOpposite();
			}

			return state.withProperty(FACING, enumfacing2);
		}
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		int i = 0;
		BlockPos blockpos1 = pos.west();
		BlockPos blockpos2 = pos.east();
		BlockPos blockpos3 = pos.north();
		BlockPos blockpos4 = pos.south();

		if (worldIn.getBlockState(blockpos1).getBlock() == this)
		{
			if (this.isFullSize(worldIn, blockpos1))
			{
				return false;
			}

			++i;
		}

		if (worldIn.getBlockState(blockpos2).getBlock() == this)
		{
			if (this.isFullSize(worldIn, blockpos2))
			{
				return false;
			}

			++i;
		}

		if (worldIn.getBlockState(blockpos3).getBlock() == this)
		{
			if (this.isFullSize(worldIn, blockpos3))
			{
				return false;
			}

			++i;
		}

		if (worldIn.getBlockState(blockpos4).getBlock() == this)
		{
			if (this.isFullSize(worldIn, blockpos4))
			{
				return false;
			}

			++i;
		}

		return i <= 1;
	}

	private boolean isFullSize(World worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos).getBlock() != this)
		{
			return false;
		}
		else
		{
			Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
			EnumFacing enumfacing;

			do
			{
				if (!iterator.hasNext())
				{
					return false;
				}

				enumfacing = (EnumFacing)iterator.next();
			}
			while (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this);

			return true;
		}
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityCoffin)
		{
			tileentity.updateContainingBlockInfo();
		}
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY,
			float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof IInventory)
			{
				playerIn.displayGUIChest((IInventory)tileentity);
				//Need our own GUI
			}
			return true;
		}
	}

	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCoffin();
	}

	private boolean isBlocked(World worldIn, BlockPos pos)
	{
		return this.isBelowSolidBlock(worldIn, pos);
	}

	private boolean isBelowSolidBlock(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos.up(), EnumFacing.DOWN, false);
	}

	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING });
	}
}