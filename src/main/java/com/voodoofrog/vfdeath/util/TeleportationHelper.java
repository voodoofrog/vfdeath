package com.voodoofrog.vfdeath.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleportationHelper extends Teleporter
{
	private WorldServer worldserver;

	public TeleportationHelper(WorldServer worldserver)
	{
		super(worldserver);
		this.worldserver = worldserver;
	}

	public void teleport(Entity entity, World destinationWorld, BlockPos destinationPos)
	{
		EntityPlayerMP playerMP = (EntityPlayerMP)entity;
		// World world = instance.functions_common().find_world(0);

		double dx = destinationPos.getX() + 0.5d;
		double dy = destinationPos.getY() + 1.0d;
		double dz = destinationPos.getZ() + 0.5d;

		playerMP.motionX = playerMP.motionY = playerMP.motionZ = 0.0D;
		playerMP.fallDistance = 0;
		playerMP.setLocationAndAngles(dx, dy, dz, entity.rotationYaw, entity.rotationPitch);

		if (playerMP.worldObj.provider.getDimensionId() != destinationWorld.provider.getDimensionId())
			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, destinationWorld.provider.getDimensionId(), this);
	}

	@Override
	public boolean placeInExistingPortal(Entity par1Entity, float rotationYaw)
	{
		return false;
	}

	@Override
	public void removeStalePortalLocations(long par1)
	{
		return;
	}

	@Override
	public void placeInPortal(Entity par1Entity, float rotationYaw)
	{
		return;
	}
}
