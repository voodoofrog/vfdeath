package com.voodoofrog.vfdeath;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import org.apache.logging.log4j.Logger;

import com.voodoofrog.ribbit.network.PacketDispatcher;
import com.voodoofrog.vfdeath.block.Blocks;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.handler.ForgeEventHandler;
import com.voodoofrog.vfdeath.handler.GuiHandler;
import com.voodoofrog.vfdeath.item.Items;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;
import com.voodoofrog.vfdeath.network.server.SendResButtonMessage;
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

	public static PacketDispatcher packetDispatcher;
	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		if (Loader.isModLoaded("ribbit"))
		{
			packetDispatcher = new PacketDispatcher(ModInfo.ID);
		}
		
		logger = event.getModLog();
		
		ConfigHandler.configFile = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.configFile.load();
		ConfigHandler.syncConfig();

		Items.initialize();
		Blocks.initialize();
		
		packetDispatcher.registerMessage(SyncPlayerPropsMessage.class);
		packetDispatcher.registerMessage(SendResButtonMessage.class);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.initRenderers();
		
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		
		Items.addRecipes();
		Blocks.addRecipes();
		Blocks.registerTileEntities();
		
		new GuiHandler();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		//FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		//FMLCommonHandler.instance().bus().register(new EventHandlerGhost());
	}
}
