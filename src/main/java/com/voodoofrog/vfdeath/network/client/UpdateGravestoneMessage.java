package com.voodoofrog.vfdeath.network.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.voodoofrog.ribbit.network.AbstractMessage.AbstractClientMessage;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;

public class UpdateGravestoneMessage extends AbstractClientMessage<UpdateGravestoneMessage>
{
	private World world;
	private BlockPos pos;
	private IChatComponent[] epitaphText;

	public UpdateGravestoneMessage()
	{
	}

	public UpdateGravestoneMessage(World worldIn, BlockPos posIn, IChatComponent[] textIn)
	{
		this.world = worldIn;
		this.pos = posIn;
		this.epitaphText = textIn;
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException
	{
		this.pos = buffer.readBlockPos();
		this.epitaphText = new IChatComponent[4];

		for (int i = 0; i < 4; ++i)
		{
			this.epitaphText[i] = buffer.readChatComponent();
		}
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException
	{
		buffer.writeBlockPos(this.pos);

		for (int i = 0; i < 4; ++i)
		{
			buffer.writeChatComponent(this.epitaphText[i]);
		}
	}

	@Override
	public void process(EntityPlayer player, Side side)
	{
		Minecraft mc = Minecraft.getMinecraft();
		boolean flag = false;

		if (mc.theWorld.isBlockLoaded(this.pos))
		{
			TileEntity tileentity = mc.theWorld.getTileEntity(this.pos);

			if (tileentity instanceof TileEntityGravestone)
			{
				TileEntityGravestone teGravestone = (TileEntityGravestone)tileentity;

				if (teGravestone.getIsEditable())
				{
					System.arraycopy(this.epitaphText, 0, teGravestone.epitaph, 0, 4);
					teGravestone.markDirty();
				}

				flag = true;
			}
		}

		if (!flag && mc.thePlayer != null)
		{
			mc.thePlayer.addChatMessage(new ChatComponentText("Unable to locate gravestone at " + this.pos.getX() + ", " + this.pos.getY()
					+ ", " + this.pos.getZ()));
		}
	}

}
