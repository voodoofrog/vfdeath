package com.voodoofrog.vfdeath.network.server;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractServerMessage;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class SendResButtonMessage extends AbstractServerMessage<SendResButtonMessage>
{
	private byte ankhs;
	UUID playerUUID;

	public SendResButtonMessage()
	{
	}

	public SendResButtonMessage(byte ankhs, UUID playerUUID)
	{
		this.ankhs = ankhs;
		this.playerUUID = playerUUID;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException
	{
		this.ankhs = buffer.readByte();
		this.playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buffer));
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException
	{
		buffer.writeByte(this.ankhs);
		ByteBufUtils.writeUTF8String(buffer, this.playerUUID.toString());
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		Container container = player.openContainer;

		if (container != null && container instanceof ContainerResurrectionAltar)
		{
			TileEntityResurrectionAltar altar = ((ContainerResurrectionAltar)container).getTileEntityAltar();
			altar.receiveResButtonEvent(this.ankhs, player, this.playerUUID);
		}
	}
}
