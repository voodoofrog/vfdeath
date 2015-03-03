package com.voodoofrog.vfdeath.graveyard;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class Graveyard
{
	public static void spawnGrave(WorldServer worldserver, BlockPos pos)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		worldserver.setBlockState(pos, Blocks.obsidian.getDefaultState());
		worldserver.notifyNeighborsOfStateChange(pos, Blocks.obsidian);
	}
}
