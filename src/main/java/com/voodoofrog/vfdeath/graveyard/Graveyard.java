package com.voodoofrog.vfdeath.graveyard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class Graveyard
{
	public static BlockPos spawnGrave(EntityPlayer player)
	{
		WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(player.dimension);
		BlockPos pos = player.worldObj.provider.getRandomizedSpawnPoint();
		worldserver.setBlockState(pos, Blocks.obsidian.getDefaultState());
		worldserver.notifyNeighborsOfStateChange(pos, Blocks.obsidian);
		return pos;
	}
}
