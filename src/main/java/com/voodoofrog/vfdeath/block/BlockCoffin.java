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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.ribbit.world.IBasicContainer;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.inventory.InventoryFullCoffin;
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

			if (meta == 2) // NORTH
			{
				worldIn.setBlockState(pos.west(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.NORTH));
				worldIn.notifyNeighborsOfStateChange(pos.west(), VFDeathBlocks.coffin);
			}

			if (meta == 3) // SOUTH
			{
				worldIn.setBlockState(pos.east(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
				worldIn.notifyNeighborsOfStateChange(pos.east(), VFDeathBlocks.coffin);
			}

			if (meta == 4) // WEST
			{
				worldIn.setBlockState(pos.south(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.WEST));
				worldIn.notifyNeighborsOfStateChange(pos.south(), VFDeathBlocks.coffin);
			}

			if (meta == 5) // EAST
			{
				worldIn.setBlockState(pos.north(), VFDeathBlocks.coffin.getDefaultState().withProperty(FACING, EnumFacing.EAST));
				worldIn.notifyNeighborsOfStateChange(pos.north(), VFDeathBlocks.coffin);
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

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos, EnumFacing facing)
	{
		boolean flag = this.canPlaceBlockAt(worldIn, pos);

		if (facing == EnumFacing.WEST)
		{
			if (worldIn.getBlockState(pos.south().south()).getBlock() == this)
			{
				return false;
			}
		}

		if (facing == EnumFacing.EAST)
		{
			if (worldIn.getBlockState(pos.north().north()).getBlock() == this)
			{
				return false;
			}
		}

		if (facing == EnumFacing.NORTH)
		{
			if (worldIn.getBlockState(pos.west().west()).getBlock() == this)
			{
				return false;
			}
		}

		if (facing == EnumFacing.SOUTH)
		{
			if (worldIn.getBlockState(pos.east().east()).getBlock() == this)
			{
				return false;
			}
		}

		return flag;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		BlockPos westPos = pos.west();
		BlockPos eastPos = pos.east();
		BlockPos northPos = pos.north();
		BlockPos southPos = pos.south();

		if (worldIn.getBlockState(westPos).getBlock() == this)
		{
			return false;
		}

		if (worldIn.getBlockState(eastPos).getBlock() == this)
		{
			return false;
		}

		if (worldIn.getBlockState(northPos).getBlock() == this)
		{
			return false;
		}

		if (worldIn.getBlockState(southPos).getBlock() == this)
		{
			return false;
		}

		return true;
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

		Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

		while (iterator.hasNext())
		{
			EnumFacing enumfacing = (EnumFacing)iterator.next();
			BlockPos blockpos1 = pos.offset(enumfacing);
			IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

			if (iblockstate1.getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
				worldIn.notifyNeighborsOfStateChange(blockpos1, Blocks.air);
			}
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
			// TODO: Remove this
			if (playerIn.isSneaking())
			{
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if (!(tileentity instanceof TileEntityCoffin))
				{
					return false;
				}
				else
				{
					((TileEntityCoffin)tileentity).setOccupant(playerIn.getUUID(playerIn.getGameProfile()), this.isFullSize(worldIn, pos));
					Ribbit.playerUtils.sendPlayerPopupMessage(playerIn, new ChatComponentText("DEBUG: You have occupied this coffin"));
					return true;
				}
			}
			else
			{
				IBasicContainer container = this.getContainer(worldIn, pos);

				if (this.isFullSize(worldIn, pos))
				{
					FMLNetworkHandler.openGui(playerIn, VFDeath.instance, ConfigHandler.COFFIN_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}

				return true;
			}
		}
	}

	public IBasicContainer getContainer(World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!(tileentity instanceof TileEntityCoffin))
		{
			return null;
		}
		else
		{
			Object object = (TileEntityCoffin)tileentity;

			if (this.isBlocked(worldIn, pos))
			{
				return null;
			}
			else
			{
				Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

				while (iterator.hasNext())
				{
					EnumFacing enumfacing = (EnumFacing)iterator.next();
					BlockPos blockpos1 = pos.offset(enumfacing);
					Block block = worldIn.getBlockState(blockpos1).getBlock();

					if (block == this)
					{
						if (this.isBlocked(worldIn, blockpos1))
						{
							return null;
						}

						TileEntity tileentity1 = worldIn.getTileEntity(blockpos1);

						if (tileentity1 instanceof TileEntityCoffin)
						{
							TileEntityCoffin coffinTE = (TileEntityCoffin)tileentity1;

							if (coffinTE.hasOccupant())
							{
								String occupantName = Ribbit.playerUtils.getUserNameFromUUID(coffinTE.getOccupant());
								VFDeath.logger.info("Coffin occupied by " + occupantName);

								if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH)
								{
									object = new InventoryFullCoffin("container.vfdeath.coffinOccupied", (IBasicContainer)object,
											(TileEntityCoffin)tileentity1, occupantName);
								}
								else
								{
									object = new InventoryFullCoffin("container.vfdeath.coffinOccupied", (TileEntityCoffin)tileentity1,
											(IBasicContainer)object, occupantName);
								}
							}
							else
							{
								VFDeath.logger.info("Coffin unoccupied");
								if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH)
								{
									object = new InventoryFullCoffin("container.vfdeath.coffin", (IBasicContainer)object,
											(TileEntityCoffin)tileentity1);
								}
								else
								{
									object = new InventoryFullCoffin("container.vfdeath.coffin", (TileEntityCoffin)tileentity1,
											(IBasicContainer)object);
								}
							}
						}
					}
				}

				return (IBasicContainer)object;
			}
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