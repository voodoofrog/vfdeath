package com.voodoofrog.vfdeath.graveyard;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Graveyard {

	//TODO: Not the real method, just here for reference
	/*public void floodFill(World world, int x, int y, int z, EntityPlayer player, int depth) {
		if(depth > 50) return;
		
		for(int sx=-1; sx<=1; sx++) {
			for(int sy=-1; sy<=1; sy++) {
				for(int sz=-1; sz<=1; sz++) {
					int nx=x+sx, ny=y+sy, nz=z+sz;
					
					if(world.getBlockMaterial(nx, ny, nz) == Material.wood) {
						//doEntityDropAndDestroyBlock(player, world, nx, ny, nz);
						world.setBlock(nx, ny, nz, 0);
						floodFill(world, nx, ny, nz, player, depth + 1);
					}
				}
			}
		}
	}*/
	
    /*public ChunkCoordinates getSpawnGraveSite(World world, int x, int y, int z)
    {
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(x, y, z);

        int spawnFuzz = 10;
        int spawnFuzzHalf = spawnFuzz / 2;

        chunkcoordinates.posX += world.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
        chunkcoordinates.posZ += world.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
        chunkcoordinates.posY = world.getTopSolidOrLiquidBlock(chunkcoordinates.posX, chunkcoordinates.posZ);

        return chunkcoordinates;
    }*/
	
	public static void spawnGrave(WorldServer worldserver, ChunkCoordinates coords) {
		int x = coords.posX;
		int y = coords.posY;
		int z = coords.posZ;

		worldserver.setBlock(x, y, z, Block.obsidian.blockID);
		worldserver.notifyBlocksOfNeighborChange(x, y, z, worldserver.getBlockId(x, y, z));
	}
	
}
