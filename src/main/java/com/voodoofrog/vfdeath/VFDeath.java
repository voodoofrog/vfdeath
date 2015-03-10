package com.voodoofrog.vfdeath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.voodoofrog.ribbit.network.PacketDispatcher;
import com.voodoofrog.vfdeath.command.AddLifeCommand;
import com.voodoofrog.vfdeath.config.ConfigHandler;
import com.voodoofrog.vfdeath.creativetab.VFDeathCreativeTab;
import com.voodoofrog.vfdeath.handler.ForgeEventHandler;
import com.voodoofrog.vfdeath.handler.GhostEventHandler;
import com.voodoofrog.vfdeath.handler.GuiHandler;
import com.voodoofrog.vfdeath.handler.PlayerEventHandler;
import com.voodoofrog.vfdeath.init.VFDeathBlocks;
import com.voodoofrog.vfdeath.init.VFDeathItems;
import com.voodoofrog.vfdeath.item.crafting.VFDeathRecipesCrafting;
import com.voodoofrog.vfdeath.misc.ModInfo;
import com.voodoofrog.vfdeath.network.client.SyncPlayerPropsMessage;
import com.voodoofrog.vfdeath.network.server.SendResButtonMessage;
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
		packetDispatcher.registerMessage(SendResButtonMessage.class);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.initRenderers();

		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		MinecraftForge.EVENT_BUS.register(new GhostEventHandler());

		VFDeathRecipesCrafting.addRecipes();

		VFDeathBlocks.registerTileEntities();

		new GuiHandler();
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

	//TODO: Add this to Ribbit and cache results
	@SideOnly(Side.CLIENT)
	public static String userNameFromUUID(UUID uuid)
	{
		Entity entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
		String name = "";

		if (entity != null)
		{
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)entity;
				name = player.getName();
			}
		}
		else
		{
			try
			{
				String convUUID = uuid.toString().replaceAll("-", "");
				URL url = new URL("https://api.mojang.com/user/profiles/" + convUUID + "/names");
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String line = reader.readLine();
				JsonParser parser = new JsonParser();
				JsonElement json = parser.parse(line.trim());
				JsonArray jsonArray = json.getAsJsonArray();
				name = jsonArray.get(jsonArray.size() - 1).getAsJsonObject().get("name").getAsString();
				reader.close();

			}
			catch (Exception ex)
			{

				ex.printStackTrace();
				name = ex.toString();

			}
		}
		return name;
	}
}
