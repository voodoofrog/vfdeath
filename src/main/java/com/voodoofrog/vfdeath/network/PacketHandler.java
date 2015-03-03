package com.voodoofrog.vfdeath.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
		EntityPlayer entityPlayer = (EntityPlayer) player;
		
		byte packetID = reader.readByte();
		
		switch(packetID) {
		case 0:
			byte buttonID = reader.readByte();
			byte ankhs = reader.readByte();
			String playerName = reader.readUTF();
			Container container = entityPlayer.openContainer;
			
			if(container != null && container instanceof ContainerResurrectionAltar) {
				TileEntityResurrectionAltar altar = ((ContainerResurrectionAltar) container).getTileEntityAltar();
				altar.receiveResButtonEvent(buttonID, ankhs, entityPlayer, playerName);
			}
			break;
		case 1:
			break;
		}
	}
	
	public static void sendResButtonPacket(byte id, byte ankhs, String playerName) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		
		try {
			dataStream.writeByte((byte) 0);
			dataStream.writeByte(id);
			dataStream.writeByte(ankhs);
			dataStream.writeUTF(playerName);
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, byteStream.toByteArray()));
		} catch(IOException ex) {
			System.err.append("Failed to send button clicked packet for id" + id + ".");
		}
	}

}
