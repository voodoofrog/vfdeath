package com.voodoofrog.vfdeath.network.server;

import java.io.IOException;

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
	private byte id;
	private byte ankhs;
	String playerName;

	public SendResButtonMessage()
	{
	}

	public SendResButtonMessage(byte id, byte ankhs, String playerName)
	{
		this.id = id;
		this.ankhs = ankhs;
		this.playerName = playerName;
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException
	{
		this.id = buffer.readByte();
		this.ankhs = buffer.readByte();
		this.playerName = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException
	{
		buffer.writeByte(this.id);
		buffer.writeByte(this.ankhs);
		ByteBufUtils.writeUTF8String(buffer, this.playerName);
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		Container container = player.openContainer;

		if (container != null && container instanceof ContainerResurrectionAltar)
		{
			TileEntityResurrectionAltar altar = ((ContainerResurrectionAltar)container).getTileEntityAltar();
			altar.receiveResButtonEvent(this.id, this.ankhs, player, this.playerName);
		}
	}
}
