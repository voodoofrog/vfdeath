package com.voodoofrog.vfdeath;

import net.minecraft.creativetab.CreativeTabs;
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
import com.voodoofrog.vfdeath.command.AddLifeCommand;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.creativetab.VFDeathCreativeTab;
import com.voodoofrog.vfdeath.graveyard.Graveyard;
import com.voodoofrog.vfdeath.handler.ForgeEventHandler;
import com.voodoofrog.vfdeath.handler.GhostHandler;
import com.voodoofrog.vfdeath.handler.GuiHandler;
import com.voodoofrog.vfdeath.handler.PlayerEventHandler;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.init.VFDeathItems;
import com.voodoofrog.vfdeath.item.crafting.VFDeathRecipesCrafting;
import com.voodoofrog.vfdeath.misc.ModInfo;
import com.voodoofrog.vfdeath.network.client.ResurrectionResponseMessage;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;
import com.voodoofrog.vfdeath.network.server.OpenGraveInventoryMessage;
import com.voodoofrog.vfdeath.network.server.RespawnMessage;
import com.voodoofrog.vfdeath.network.server.ResurrectionButtonMessage;
import com.voodoofrog.vfdeath.proxy.CommonProxy;

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
	public static CreativeTabs vfdeathTab = new VFDeathCreativeTab(CreativeTabs.getNextID(), "vfDeathTab");
	public static Graveyard graveyard;
	public static GhostHandler ghostHandler;

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

		VFDeathItems.registerItems();
		VFDeathBlocks.registerBlocks();

		packetDispatcher.registerMessage(SyncPlayerPropsMessage.class);
		packetDispatcher.registerMessage(ResurrectionButtonMessage.class);
		packetDispatcher.registerMessage(ResurrectionResponseMessage.class);
		packetDispatcher.registerMessage(OpenGraveInventoryMessage.class);
		packetDispatcher.registerMessage(RespawnMessage.class);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.initRenderers();

		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		MinecraftForge.EVENT_BUS.register(ghostHandler = new GhostHandler());

		VFDeathRecipesCrafting.addRecipes();

		VFDeathBlocks.registerTileEntities();

		new GuiHandler();

		graveyard = new Graveyard();

		proxy.registerKeyBindings();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new AddLifeCommand());
	}
}
