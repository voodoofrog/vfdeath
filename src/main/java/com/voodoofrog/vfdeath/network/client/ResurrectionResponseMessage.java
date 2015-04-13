package com.voodoofrog.vfdeath.network.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractClientMessage;
import com.voodoofrog.vfdeath.client.gui.GuiResurrectionAltar;
import com.voodoofrog.vfdeath.misc.Strings;

public class ResurrectionResponseMessage extends AbstractClientMessage<ResurrectionResponseMessage>
{
	private int messageID;

	public ResurrectionResponseMessage()
	{
	}

	public ResurrectionResponseMessage(int messageID)
	{
		this.messageID = messageID;
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException
	{
		this.messageID = buffer.readInt();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException
	{
		buffer.writeInt(this.messageID);
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.currentScreen instanceof GuiResurrectionAltar)
		{
			GuiResurrectionAltar screen = (GuiResurrectionAltar)mc.currentScreen;

			switch (this.messageID)
			{
			case 0:
				screen.outputText = Strings.GUI_KEY + "." + Strings.ALTAR_NAME + "." + Strings.ALTAR_GUI_NOT_DEAD;
				break;
			case 1:
				screen.outputText = Strings.GUI_KEY + "." + Strings.ALTAR_NAME + "." + Strings.ALTAR_GUI_NOT_FOUND;
				break;
			}
		}
	}
}
