package com.voodoofrog.vfdeath.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.misc.Strings;

public class AddLifeCommand extends CommandBase implements ICommand
{
	private List aliases;

	public AddLifeCommand()
	{
		this.aliases = new ArrayList();
		this.aliases.add(Strings.ADDLIFE_NAME);
	}

	@Override
	public int compareTo(ICommand command)
	{
		return 0;
	}

	@Override
	public String getCommandName()
	{
		return Strings.ADDLIFE_NAME;
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return Strings.ADDLIFE_KEY + "." + Strings.COMMAND_USEAGE;
	}

	@Override
	public List getCommandAliases()
	{
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length <= 0)
		{
			if (sender instanceof EntityPlayerMP)
			{
				EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);

				ExtendedPlayer.get(entityPlayerMP).gainHearts(1, false);
				notifyOperators(sender, this, Strings.ADDLIFE_KEY + "." + Strings.COMMAND_SUCCESS, new Object[] { Integer.valueOf(1),
						entityPlayerMP.getName() });
				return;
			}
			else
			{
				throw new WrongUsageException("commands.vfdeath.addlife.usage", new Object[0]);
			}
		}
		else
		{
			int amount = Integer.parseInt(args[0]);
			EntityPlayerMP entityPlayerMP = args.length > 1 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
			boolean flag = amount < 0;

			if (flag)
			{
				throw new CommandException(Strings.ADDLIFE_KEY + "." + Strings.COMMAND_FAILURE, new Object[0]);
			}
			else
			{
				ExtendedPlayer.get(entityPlayerMP).gainHearts(amount, false);
				notifyOperators(sender, this, Strings.ADDLIFE_KEY + "." + Strings.COMMAND_USEAGE, new Object[] { Integer.valueOf(amount),
						entityPlayerMP.getName() });
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 2 ? getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : null;
	}

	protected String[] getAllUsernames()
	{
		return MinecraftServer.getServer().getAllUsernames();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 1;
	}
}
