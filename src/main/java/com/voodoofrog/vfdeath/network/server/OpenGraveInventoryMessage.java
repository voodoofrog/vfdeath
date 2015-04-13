package com.voodoofrog.vfdeath.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractServerMessage;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.config.ConfigHandler;

public class OpenGraveInventoryMessage extends AbstractServerMessage<OpenGraveInventoryMessage>
{
	public OpenGraveInventoryMessage()
	{
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException
	{
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException
	{
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		player.openGui(VFDeath.instance, ConfigHandler.INVENTORY_GRAVE_GUI_ID, player.worldObj, (int)player.posX, (int)player.posY,
				(int)player.posZ);
	}

}
