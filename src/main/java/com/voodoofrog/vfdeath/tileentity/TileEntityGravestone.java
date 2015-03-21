package com.voodoofrog.vfdeath.tileentity;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.gson.JsonParseException;

public class TileEntityGravestone extends TileEntity
{
	public final IChatComponent[] epitaph = new IChatComponent[] { new ChatComponentText(""), new ChatComponentText(""),
			new ChatComponentText(""), new ChatComponentText("") };
	public int lineBeingEdited = -1;
	private boolean isEditable = true;
	private EntityPlayer player;

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		for (int i = 0; i < 4; ++i)
		{
			String s = IChatComponent.Serializer.componentToJson(this.epitaph[i]);
			compound.setString("Text" + (i + 1), s);
		}
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.isEditable = false;
		super.readFromNBT(compound);
		ICommandSender icommandsender = new ICommandSender()
		{
			public String getName()
			{
				return "Gravestone";
			}

			public IChatComponent getDisplayName()
			{
				return new ChatComponentText(this.getName());
			}

			public void addChatMessage(IChatComponent message)
			{
			}

			public boolean canUseCommand(int permLevel, String commandName)
			{
				return true;
			}

			public BlockPos getPosition()
			{
				return TileEntityGravestone.this.pos;
			}

			public Vec3 getPositionVector()
			{
				return new Vec3((double)TileEntityGravestone.this.pos.getX() + 0.5D, (double)TileEntityGravestone.this.pos.getY() + 0.5D,
						(double)TileEntityGravestone.this.pos.getZ() + 0.5D);
			}

			public World getEntityWorld()
			{
				return TileEntityGravestone.this.worldObj;
			}

			public Entity getCommandSenderEntity()
			{
				return null;
			}

			public boolean sendCommandFeedback()
			{
				return false;
			}

			public void setCommandStat(CommandResultStats.Type type, int amount)
			{
			}
		};

		for (int i = 0; i < 4; ++i)
		{
			String s = compound.getString("Text" + (i + 1));

			try
			{
				IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);

				try
				{
					this.epitaph[i] = ChatComponentProcessor.func_179985_a(icommandsender, ichatcomponent, (Entity)null);
				}
				catch (CommandException commandexception)
				{
					this.epitaph[i] = ichatcomponent;
				}
			}
			catch (JsonParseException jsonparseexception)
			{
				this.epitaph[i] = new ChatComponentText(s);
			}
		}
	}

	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.pos, 3, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	public boolean getIsEditable()
	{
		return this.isEditable;
	}

	@SideOnly(Side.CLIENT)
	public void setEditable(boolean isEditableIn)
	{
		this.isEditable = isEditableIn;

		if (!isEditableIn)
		{
			this.player = null;
		}
	}

	public void setPlayer(EntityPlayer playerIn)
	{
		this.player = playerIn;
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}

	public void markForUpdate()
	{
		this.worldObj.markBlockForUpdate(pos);
	}
}