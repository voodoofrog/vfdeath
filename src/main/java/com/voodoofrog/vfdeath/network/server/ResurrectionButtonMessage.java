package com.voodoofrog.vfdeath.network.server;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractServerMessage;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class ResurrectionButtonMessage extends AbstractServerMessage<ResurrectionButtonMessage>
{
	private UUID playerUUID;

	public ResurrectionButtonMessage()
	{
	}

	public ResurrectionButtonMessage(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException
	{
		this.playerUUID = buffer.readUuid();
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException
	{
		buffer.writeUuid(this.playerUUID);
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		Container container = player.openContainer;

		if (container != null && container instanceof ContainerResurrectionAltar)
		{
			TileEntityResurrectionAltar altar = ((ContainerResurrectionAltar)container).getTileEntityAltar();
			altar.receiveResurrectionButtonEvent(player, this.playerUUID);
		}
	}
}
