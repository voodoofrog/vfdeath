package com.voodoofrog.vfdeath.graveyard;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.vfdeath.block.BlockGravestone;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.misc.Strings;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

public class Graveyard
{
	public BlockPos spawnGrave(EntityPlayer player)
	{
		WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(player.dimension);
		BlockPos pos = this.getRandomizedSpawnPoint(player.worldObj);

		if (pos != null)
		{
			World world = player.worldObj;
			BlockGravestone graveStone = VFDeathBlocks.gravestone;

			worldserver.setBlockState(pos, graveStone.getDefaultState());
			worldserver.notifyNeighborsOfStateChange(pos, VFDeathBlocks.gravestone);
			graveStone.digGrave(world, pos);
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityGravestone)
			{
				DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
				Date date = Ribbit.dateTime.getCurrentDate(world);
				TileEntityGravestone teGravestone = (TileEntityGravestone)tileentity;

				teGravestone.epitaph[0] = new ChatComponentText(player.getName());
				teGravestone.epitaph[1] = new ChatComponentTranslation(Strings.GRAVE_DOD, dateFormat.format(date));
				teGravestone.epitaph[2] = new ChatComponentText("Killed by");
				teGravestone.epitaph[3] = new ChatComponentText("deathcause");
				teGravestone.markForUpdate();
			}
		}
		else
		{
			player.addChatMessage(new ChatComponentTranslation(Strings.GRAVE_NO_VALID_LOC));
		}

		return pos;
	}

	public BlockPos getRandomizedSpawnPoint(World world)
	{
		// TODO: May need to make sure this only happens in overworld
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
}
