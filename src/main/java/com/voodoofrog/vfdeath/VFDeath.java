package com.voodoofrog.vfdeath;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.voodoofrog.vfdeath.block.Blocks;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.eventhandlers.EventHandlerGhost;
import com.voodoofrog.vfdeath.handler.GuiHandler;
import com.voodoofrog.vfdeath.item.Items;
import com.voodoofrog.vfdeath.network.PacketDispatcher;
import com.voodoofrog.vfdeath.proxy.CommonProxy;
import com.voodoofrog.vfdeath.server.handler.PlayerEventHandler;

@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class VFDeath
{
	// TODO: Look into making this more OO oriented with getters/setters etc.

	@Instance(ModInfo.ID)
	public static VFDeath instance;

	@SidedProxy(clientSide = "com.voodoofrog.vfdeath.proxy.ClientProxy", serverSide = "com.voodoofrog.vfdeath.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		PacketDispatcher.registerPackets();

		ConfigHandler.configFile = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.configFile.load();
		ConfigHandler.syncConfig();

		Items.initialize();
		Blocks.initialize();

		proxy.initSounds();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Items.addRecipes();
		Blocks.addRecipes();
		Blocks.registerTileEntities();
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		FMLCommonHandler.instance().bus().register(new EventHandlerGhost());

		proxy.initRenderers();
		
		new GuiHandler();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}
}
