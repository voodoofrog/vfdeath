package uk.co.forgottendream.vfdeath.graveyard;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Graveyard {

	//TODO: Not the real method, just here for reference
	public void floodFill(World world, int x, int y, int z, EntityPlayer player, int depth) {
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
	}
	
}
