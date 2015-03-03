package com.voodoofrog.vfdeath.network.packet.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class SendResButtonMessage implements IMessage
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
	public void fromBytes(ByteBuf buffer)
	{
		this.id = buffer.readByte();
		this.ankhs = buffer.readByte();
		this.playerName = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeByte(this.id);
		buffer.writeByte(this.ankhs);
		ByteBufUtils.writeUTF8String(buffer, this.playerName);
	}

	public static class Handler extends AbstractServerMessageHandler<SendResButtonMessage>
	{
		@Override
		public IMessage handleServerMessage(EntityPlayer player, SendResButtonMessage message, MessageContext ctx)
		{
			Container container = player.openContainer;

			if (container != null && container instanceof ContainerResurrectionAltar)
			{
				TileEntityResurrectionAltar altar = ((ContainerResurrectionAltar)container).getTileEntityAltar();
				altar.receiveResButtonEvent(message.id, message.ankhs, player, message.playerName);
			}
			return null;
		}
	}
}
