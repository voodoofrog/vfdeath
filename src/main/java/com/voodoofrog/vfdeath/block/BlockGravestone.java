package com.voodoofrog.vfdeath.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.init.VFDeathItems;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

public class BlockGravestone extends BlockContainer
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockGravestone()
	{
		super(Material.rock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getSelectedBoundingBox(worldIn, pos);
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return 2;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		int meta = this.getMetaFromState(worldIn.getBlockState(pos));

		this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);

		if (meta == 4)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F); // WEST
		}

		if (meta == 3)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F); // NORTH
		}

		if (meta == 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F); // SOUTH
		}

		if (meta == 5)
		{
			this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F); // EAST
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{

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

		worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 3);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityGravestone)
		{
			TileEntityGravestone teGravestone = (TileEntityGravestone)tileentity;

			if (placer instanceof EntityPlayer)
			{
				teGravestone.setOwner(((EntityPlayer)placer).getUUID(((EntityPlayer)placer).getGameProfile()));
				teGravestone.markForUpdate();
			}
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
	
	// TODO: May need this from chest block class
	// public boolean canPlaceBlockAt(World worldIn, BlockPos pos)

	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityGravestone();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING });
	}

	// TODO: Remove this
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY,
			float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			VFDeath.graveyard.spawnGrave(playerIn);
			return true;
		}
	}

	public void digGrave(World world, BlockPos pos)
	{
		this.digGrave(world, pos, VFDeathBlocks.coffin);
	}
	
	public BlockPos digGrave(World world, BlockPos pos, BlockCoffin coffin)
	{
		int meta = this.getMetaFromState(world.getBlockState(pos));

		if (meta == 2) // SOUTH
		{
			world.setBlockState(pos.down().north(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().north(), Blocks.grass);
			world.setBlockState(pos.down().north().north(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().north().north(), Blocks.grass);

			world.setBlockState(pos.down().down().north().north(), coffin.getDefaultState().withProperty(FACING, EnumFacing.WEST));
			world.notifyNeighborsOfStateChange(pos.down().down().north().north(), coffin);
			
			return pos.down().down().north().north();
		}

		if (meta == 3) // NORTH
		{
			world.setBlockState(pos.down().south(), Blocks.air.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().south(), Blocks.grass);
			world.setBlockState(pos.down().south().south(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().south().south(), Blocks.grass);
			
			world.setBlockState(pos.down().down().south().south(), coffin.getDefaultState().withProperty(FACING, EnumFacing.EAST));
			world.notifyNeighborsOfStateChange(pos.down().down().south().south(), coffin);
			
			return pos.down().down().south().south();
		}

		if (meta == 4) // WEST
		{
			world.setBlockState(pos.down().east(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().east(), Blocks.grass);
			world.setBlockState(pos.down().east().east(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().east().east(), Blocks.grass);
			
			world.setBlockState(pos.down().down().east().east(), coffin.getDefaultState().withProperty(FACING, EnumFacing.NORTH));
			world.notifyNeighborsOfStateChange(pos.down().down().east().east(), coffin);
			
			return pos.down().down().east().east();
		}

		if (meta == 5) // EAST
		{
			world.setBlockState(pos.down().west(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().west(), Blocks.grass);
			world.setBlockState(pos.down().west().west(), Blocks.grass.getDefaultState());
			world.notifyNeighborsOfStateChange(pos.down().west().west(), Blocks.grass);
			
			world.setBlockState(pos.down().down().west().west(), coffin.getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
			world.notifyNeighborsOfStateChange(pos.down().down().west().west(), coffin);
			
			return pos.down().down().west().west();
		}
		return null;
	}
	
	public BlockPos getCoffinBlockPos(World world, BlockPos pos)
	{
		int meta = this.getMetaFromState(world.getBlockState(pos));

		if (meta == 2) // SOUTH
		{
			if (world.getBlockState(pos.down().down().north().north()).getBlock() instanceof BlockCoffin)
				return pos.down().down().north().north();
		}

		if (meta == 3) // NORTH
		{
			if (world.getBlockState(pos.down().down().south().south()).getBlock() instanceof BlockCoffin)
				return pos.down().down().south().south();
		}

		if (meta == 4) // WEST
		{
			if (world.getBlockState(pos.down().down().east().east()).getBlock() instanceof BlockCoffin)
				return pos.down().down().east().east();
		}

		if (meta == 5) // EAST
		{
			if (world.getBlockState(pos.down().down().west().west()).getBlock() instanceof BlockCoffin)
				return pos.down().down().west().west();
		}
		
		return null;
	}
	
	public BlockCoffin getCoffinBlock(World world, BlockPos pos)
	{
		if (this.getCoffinBlockPos(world, pos) != null)
		{
			return (BlockCoffin)world.getBlockState(this.getCoffinBlockPos(world, pos)).getBlock();
		}
		
		return null;
	}
}