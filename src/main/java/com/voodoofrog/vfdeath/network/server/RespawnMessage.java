package com.voodoofrog.vfdeath.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractServerMessage;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.misc.ModInfo;

public class RespawnMessage extends AbstractServerMessage<RespawnMessage>
{
	public RespawnMessage()
	{
	}

	@Override
	public void read(PacketBuffer buffer) throws IOException
	{
	}

	@Override
	public void write(PacketBuffer buffer) throws IOException
	{
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		NetHandlerPlayServer pnh = ((EntityPlayerMP)player).playerNetServerHandler;
		pnh.playerEntity.markPlayerActive();
		pnh.playerEntity = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager()
				.recreatePlayerEntity(pnh.playerEntity, pnh.playerEntity.dimension, false);
	}
}
