package com.voodoofrog.vfdeath.tileentity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.gson.JsonParseException;
import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.vfdeath.misc.Strings;

public class TileEntityGravestone extends TileEntity
{
	public final IChatComponent[] epitaph = new IChatComponent[] { new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""),
			new ChatComponentText("") };
	public int lineBeingEdited = -1;
	private boolean isEditable = true;
	private EntityPlayer player;
	private UUID ownerUUID;

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		for (int i = 0; i < 4; ++i)
		{
			String s = IChatComponent.Serializer.componentToJson(this.epitaph[i]);
			compound.setString("Text" + (i + 1), s);
		}

		if (this.ownerUUID != null)
		{
			compound.setString("Owner", this.ownerUUID.toString());
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

			public boolean canCommandSenderUseCommand(int permLevel, String commandName)
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
					this.epitaph[i] = ChatComponentProcessor.processComponent(icommandsender, ichatcomponent, (Entity)null);
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

		if (compound.hasKey("Owner", 8))
			this.ownerUUID = UUID.fromString(compound.getString("Owner"));
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.pos, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		this.updateOwnerEpitaph();
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
		this.worldObj.markBlockForUpdate(this.pos);
	}

	public UUID getOwner()
	{
		return this.ownerUUID;
	}
	
	public void setOwner(UUID playerUUID)
	{
		this.ownerUUID = playerUUID;
		this.markForUpdate();
	}

	public void updateOwnerEpitaph()
	{
		if (this.ownerUUID != null)
		{
			this.epitaph[0] = new ChatComponentText(Ribbit.playerUtils.getUserNameFromUUID(this.ownerUUID));
		}
	}

	public void updateEpitaph(DamageSource source)
	{
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
		Date date = Ribbit.dateTime.getCurrentDate(this.worldObj);

		this.epitaph[1] = new ChatComponentTranslation(Strings.GRAVE_DOD, dateFormat.format(date));
		this.epitaph[2] = new ChatComponentTranslation(Strings.GRAVE_KILLED_BY);
		this.epitaph[3] = this.processEpitaph(source);

		this.markForUpdate();
	}
	
	public void updateEpitaph(String text, String text2)
	{
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
		Date date = Ribbit.dateTime.getCurrentDate(this.worldObj);

		this.epitaph[1] = new ChatComponentTranslation(Strings.GRAVE_DOD, dateFormat.format(date));
		this.epitaph[2] = new ChatComponentText(text);
		this.epitaph[3] = new ChatComponentText(text2);

		this.markForUpdate();
	}
	
	private IChatComponent processEpitaph(DamageSource source)
	{
		if (source.damageType == "mob" || source.damageType == "player")
		{
			return new ChatComponentTranslation(Strings.GRAVE_DEATH_CAUSE + "." + source.damageType, new Object[] { source.getSourceOfDamage()
					.getDisplayName() });
		}
		else if (source.damageType == "arrow" || source.damageType == "arrow.item" || source.damageType == "fireball"
				|| source.damageType == "fireball.item" || source.damageType == "thrown" || source.damageType == "thrown.item"
				|| source.damageType == "indirectMagic" || source.damageType == "indirectMagic.item" || source.damageType == "thorns")
		{
			return new ChatComponentTranslation(Strings.GRAVE_DEATH_CAUSE + "." + source.damageType, new Object[] { source.getEntity()
					.getDisplayName() });
		}
		else
		{
			return new ChatComponentTranslation(Strings.GRAVE_DEATH_CAUSE + "." + source.damageType);
		}
	}
}