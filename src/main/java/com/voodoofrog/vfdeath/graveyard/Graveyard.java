package com.voodoofrog.vfdeath.graveyard;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.ribbit.world.IBasicContainer;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.block.BlockCoffin;
import com.voodoofrog.vfdeath.block.BlockGravestone;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.item.ItemCoffin;
import com.voodoofrog.vfdeath.misc.Strings;
import com.voodoofrog.vfdeath.tileentity.TileEntityCoffin;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

public class Graveyard
{
	private int worldId = 0;
	
	public BlockPos spawnGrave(EntityPlayer player)
	{
		WorldServer graveWorld = DimensionManager.getWorld(this.worldId);
		BlockPos pos = this.getRandomizedSpawnPoint(graveWorld);

		if (pos != null)
		{
			BlockGravestone graveStone = VFDeathBlocks.gravestone;

			graveWorld.setBlockState(pos, graveStone.getDefaultState());
			graveWorld.notifyNeighborsOfStateChange(pos, VFDeathBlocks.gravestone);
			graveStone.digGrave(graveWorld, pos);
			TileEntity tileentity = graveWorld.getTileEntity(pos);

			if (tileentity instanceof TileEntityGravestone)
			{
				TileEntityGravestone teGravestone = (TileEntityGravestone)tileentity;

				teGravestone.setOwner(Ribbit.playerUtils.getUUIDFromUsername("voodoofrog"));
				teGravestone.updateEpitaph("");
			}
		}
		else
		{
			Ribbit.playerUtils.sendPlayerPopupMessage(player, new ChatComponentTranslation(Strings.GRAVE_NO_VALID_LOC));
		}

		return pos;
	}

	public BlockPos getRandomizedSpawnPoint(World world)
	{
		BlockPos pos = world.getSpawnPoint();

		int radius = 16; // to become configurable
		int border = MathHelper.floor_double(world.getWorldBorder().getClosestDistance(pos.getX(), pos.getZ()));
		if (border < radius)
			radius = border;
		int radiusHalf = radius / 2;
		int maxAttempts = 10;

		if (!world.provider.getHasNoSky())
		{
			for (int i = 0; i < maxAttempts; i++)
			{
				pos = world.getTopSolidOrLiquidBlock(pos.add(world.rand.nextInt(radiusHalf) - radius, 0, world.rand.nextInt(radiusHalf) - radius));

				Block block = world.getBlockState(pos).getBlock();
				Block blockBelow = world.getBlockState(pos.down()).getBlock();

				if (!block.equals(Blocks.water) && !blockBelow.equals(VFDeathBlocks.gravestone) && !this.isPositionInvalid(world, pos))
				{
					return pos;
				}
			}
		}

		return null;
	}

	private boolean isPositionInvalid(World world, BlockPos pos)
	{
		BlockPos startPos = pos.north(2).west(1);
		Block block;
		boolean flag = false;

		// south is z+
		// east is x+
		for (int z = 0; z < 5; z++)
		{
			for (int x = 0; x < 3; x++)
			{
				block = world.getBlockState(startPos.south(z).east(x)).getBlock();

				if (block.equals(VFDeathBlocks.gravestone))
				{
					flag = true;
				}
			}
		}

		// Start checking north blocks
		block = world.getBlockState(pos.north()).getBlock();

		if (!block.getMaterial().blocksMovement())
		{
			block = world.getBlockState(pos.north(2)).getBlock();

			if (block.getMaterial().blocksMovement())
			{
				flag = true;
			}
		}
		else
		{
			flag = true;
		}
		// End checking north blocks

		// Start checking below north blocks
		block = world.getBlockState(pos.north().down()).getBlock();

		if (block.getMaterial().blocksMovement())
		{
			block = world.getBlockState(pos.north(2).down()).getBlock();

			if (!block.getMaterial().blocksMovement())
			{
				flag = true;
			}
		}
		else
		{
			flag = true;
		}
		// End checking below north blocks

		return flag;
	}
	
	public int getWorldId()
	{
		return this.worldId;
	}
	
	public boolean addDropsToGrave(EntityPlayer player, World world, BlockPos pos, List<EntityItem> drops)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof BlockGravestone)
		{
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof TileEntityGravestone)
			{
				if (((TileEntityGravestone)te).getOwner().equals(player.getUUID(player.getGameProfile())))
				{
					if (((BlockGravestone)block).getCoffinPos(world, pos) != null)
					{
						//TODO: This is the new baseline
						VFDeath.logger.info("Adding drops to existing coffin...");
						// should check to see if coffin is occupied
						BlockPos coffinPos = ((BlockGravestone)block).getCoffinPos(world, pos);
						
						if (coffinPos != null)
						{
							IBasicContainer container = ((BlockCoffin)world.getBlockState(coffinPos).getBlock()).getContainer(world, coffinPos, true);
							if (container != null)
							{
								this.fillCoffin(container, drops);
							}
							else
							{
								//TODO: This is the fuck up point
								VFDeath.logger.info("Null container!");
								return false;
							}
						}
						else
						{
							VFDeath.logger.info("Null coffinPos!");
							return false;
						}

						return true;
					}
					else
					{
						VFDeath.logger.info("Adding coffin...");
						// default coffin
						BlockCoffin coffin = VFDeathBlocks.coffin;

						if (ExtendedPlayer.get(player).getGraveInventory().getInventory().length > 0)
						{
							ItemCoffin coffinItem = (ItemCoffin)ExtendedPlayer.get(player).getGraveInventory().getStackInSlot(0).getItem();
							coffin = coffinItem.getCoffinBlock();
						}

						BlockPos coffinPos = ((BlockGravestone)block).digGrave(world, pos, coffin);
						IBasicContainer container = coffin.getContainer(world, coffinPos);

						VFDeath.logger.info("Adding drops to added coffin...");
						ExtendedPlayer.get(player).getGraveInventory().decrStackSize(0, 1);
						this.fillCoffin(container, drops);
						coffin.setOccupant(world, coffinPos, player);
						return true;
					}
				}
				else
				{
					VFDeath.logger.info("No gravestone TE found!");
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		VFDeath.logger.info("No gravestone found!");
		return false;
	}
	
	private void fillCoffin(IBasicContainer container, List<EntityItem> drops)
	{
		Iterator<EntityItem> dropsIterator = drops.iterator();
		int index = 0;

		while (dropsIterator.hasNext())
		{
			ItemStack drop = dropsIterator.next().getEntityItem();
			//TODO: Fix this
			container.setInventorySlotContents(index, drop);
			index++;
		}
	}
}
