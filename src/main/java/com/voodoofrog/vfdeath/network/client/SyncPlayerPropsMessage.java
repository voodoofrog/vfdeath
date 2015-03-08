package com.voodoofrog.vfdeath.network.client;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractClientMessage;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;

public class SyncPlayerPropsMessage extends AbstractClientMessage<SyncPlayerPropsMessage>
{
	private NBTTagCompound data;

	public SyncPlayerPropsMessage()
	{
	}

	public SyncPlayerPropsMessage(EntityPlayer player)
	{
		this.data = new NBTTagCompound();

		ExtendedPlayer.get(player).saveNBTData(data);
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException
	{
		this.data = buffer.readNBTTagCompoundFromBuffer();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException
	{
		buffer.writeNBTTagCompoundToBuffer(this.data);
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		ExtendedPlayer.get(player).loadNBTData(this.data);
		//ExtendedPlayer.get(player).updateHealthAttribMod();
	}
}
