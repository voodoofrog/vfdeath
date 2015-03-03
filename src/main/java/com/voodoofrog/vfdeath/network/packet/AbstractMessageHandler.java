package com.voodoofrog.vfdeath.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.VFDeath;

public abstract class AbstractMessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage>
{
	/**
	 * Handle a message received on the client side
	 * 
	 * @return a message to send back to the Server, or null if no reply is
	 *         necessary
	 */
	@SideOnly(Side.CLIENT)
	public abstract IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx);

	/**
	 * Handle a message received on the server side
	 * 
	 * @return a message to send back to the Client, or null if no reply is
	 *         necessary
	 */
	public abstract IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx);

	@Override
	public IMessage onMessage(T message, MessageContext ctx)
	{
		if (ctx.side.isClient())
		{
			return this.handleClientMessage(VFDeath.proxy.getPlayerEntity(ctx), message, ctx);
		}
		else
		{
			return this.handleServerMessage(VFDeath.proxy.getPlayerEntity(ctx), message, ctx);
		}
	}
}