package uk.co.forgottendream.vfdeath.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import uk.co.forgottendream.vfdeath.ModInfo;
import uk.co.forgottendream.vfdeath.inventory.ContainerResurrectionAltar;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

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
			String playerName = reader.readUTF();
			Container container = entityPlayer.openContainer;
			
			if(container != null && container instanceof ContainerResurrectionAltar) {
				TileEntityResurrectionAltar altar = ((ContainerResurrectionAltar) container).getTileEntityAltar();
				altar.receiveButtonEvent(buttonID, playerName);
			}
		}
	}
	
	public static void sendButtonPacket(byte id) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		
		try {
			dataStream.writeByte((byte) 0);
			dataStream.writeByte(id);
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, byteStream.toByteArray()));
		} catch(IOException ex) {
			System.err.append("Failed to send button clicked packet for id" + id + ".");
		}
	}
	
	public static void sendButtonPacket(byte id, String text) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		
		try {
			dataStream.writeByte((byte) 0);
			dataStream.writeByte(id);
			dataStream.writeUTF(text);
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, byteStream.toByteArray()));
		} catch(IOException ex) {
			System.err.append("Failed to send button clicked packet for id" + id + ".");
		}
	}

}
