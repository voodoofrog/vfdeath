package com.voodoofrog.vfdeath.handler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.misc.Strings;
import com.voodoofrog.vfdeath.network.server.OpenGraveInventoryMessage;

public class KeyHandler
{
	public KeyHandler()
	{
		ClientRegistry.registerKeyBinding(ConfigHandler.GRAVE_KEY);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event)
	{
		if (event.side == Side.SERVER)
			return;
		if (event.phase == Phase.START)
		{
			if (ConfigHandler.GRAVE_KEY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus)
			{
				VFDeath.packetDispatcher.sendToServer(new OpenGraveInventoryMessage());
			}
		}
	}
}