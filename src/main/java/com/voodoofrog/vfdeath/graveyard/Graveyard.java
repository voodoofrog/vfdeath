package com.voodoofrog.vfdeath.graveyard;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.block.BlockGravestone;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class Graveyard
{
	public static BlockPos spawnGrave(EntityPlayer player)
	{
		WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(player.dimension);
		BlockPos pos = getRandomizedSpawnPoint(player.worldObj);

		if (pos != null)
		{
			BlockGravestone graveStone = VFDeathBlocks.gravestone;
			
			worldserver.setBlockState(pos, graveStone.getDefaultState());
			worldserver.notifyNeighborsOfStateChange(pos, VFDeathBlocks.gravestone);
			graveStone.digGrave(player.worldObj, pos);
			TileEntity tileentity = player.worldObj.getTileEntity(pos);

			if (tileentity instanceof TileEntityGravestone)
			{
				TileEntityGravestone teGravestone = (TileEntityGravestone)tileentity;
				teGravestone.epitaph[0] = new ChatComponentText(player.getName());
				teGravestone.epitaph[1] = new ChatComponentText("Died 01/01/01");
				teGravestone.epitaph[2] = new ChatComponentText("Killed by");
				teGravestone.epitaph[3] = new ChatComponentText("deathcause");
				teGravestone.markForUpdate();
			}
		}
		else
		{
			player.addChatMessage(new ChatComponentText("Couldn't get a valid position"));
		}

		return pos;
	}

	public static BlockPos getRandomizedSpawnPoint(World world)
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

				if (!block.equals(Blocks.water) && !blockBelow.equals(VFDeathBlocks.gravestone) && !isPositionInvalid(world, pos))
				{
					return pos;
				}
			}
		}

		return null;
	}

	public static boolean isPositionInvalid(World world, BlockPos pos)
	{
		BlockPos startPos = pos.north(2).west(1);
		Block block;
		boolean flag = false;

		//south is z+
		//east is x+
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
}
