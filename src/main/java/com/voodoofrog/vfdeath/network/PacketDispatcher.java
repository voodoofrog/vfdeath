package com.voodoofrog.vfdeath.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.network.packet.client.AbstractClientMessageHandler;
import com.voodoofrog.vfdeath.network.packet.server.SendResButtonMessage;

public class PacketDispatcher
{
	private static byte packetId = 0;
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.ID);

	/**
	 * Call this during pre-init or loading and register all of your packets
	 * (messages) here
	 */
	public static final void registerPackets()
	{
		PacketDispatcher.registerMessage(SendResButtonMessage.Handler.class, SendResButtonMessage.class);
	}

	/**
	 * Registers a message and message handler
	 * @param <REQ>
	 * @param <REPLY>
	 */
	private static final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass)
	{
		Side side = AbstractClientMessageHandler.class.isAssignableFrom(handlerClass) ? Side.CLIENT : Side.SERVER;
		PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
	}

	/**
	 * Send this message to the specified player. See
	 * {@link SimpleNetworkWrapper#sendTo(IMessage, EntityPlayerMP)}
	 */
	public static final void sendTo(IMessage message, EntityPlayerMP player)
	{
		PacketDispatcher.dispatcher.sendTo(message, player);
	}

	/**
	 * Send this message to everyone within a certain range of a point. See
	 * {@link SimpleNetworkWrapper#sendToDimension(IMessage, NetworkRegistry.TargetPoint)}
	 */
	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		PacketDispatcher.dispatcher.sendToAllAround(message, point);
	}

	/**
	 * Sends a message to everyone within a certain range of the coordinates in
	 * the same dimension.
	 */
	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range)
	{
		PacketDispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	/**
	 * Sends a message to everyone within a certain range of the player
	 * provided.
	 */
	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range)
	{
		PacketDispatcher.sendToAllAround(message, player.worldObj.provider.getDimensionId(), player.posX, player.posY, player.posZ, range);
	}

	/**
	 * Send this message to everyone within the supplied dimension. See
	 * {@link SimpleNetworkWrapper#sendToDimension(IMessage, int)}
	 */
	public static final void sendToDimension(IMessage message, int dimensionId)
	{
		PacketDispatcher.dispatcher.sendToDimension(message, dimensionId);
	}

	/**
	 * Send this message to the server. See
	 * {@link SimpleNetworkWrapper#sendToServer(IMessage)}
	 */
	public static final void sendToServer(IMessage message)
	{
		PacketDispatcher.dispatcher.sendToServer(message);
	}
}
