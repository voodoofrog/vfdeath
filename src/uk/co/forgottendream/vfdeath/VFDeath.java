package uk.co.forgottendream.vfdeath;

import uk.co.forgottendream.vfdeath.block.Blocks;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import uk.co.forgottendream.vfdeath.handler.GuiHandler;
import uk.co.forgottendream.vfdeath.item.Items;
import uk.co.forgottendream.vfdeath.network.PacketHandler;
import uk.co.forgottendream.vfdeath.proxies.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION)
@NetworkMod(channels = {ModInfo.CHANNEL}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class VFDeath {

	//TODO: Look into making this more OO oriented with getters/setters etc.
	
	@Instance(ModInfo.ID)
	public static VFDeath instance;
	
	@SidedProxy(clientSide = "uk.co.forgottendream.vfdeath.proxies.ClientProxy", serverSide = "uk.co.forgottendream.vfdeath.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.initialize(event.getSuggestedConfigurationFile());
		Items.initialize();
		Blocks.initialize();
		
		proxy.initSounds();
		proxy.initRenderers();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Items.addNames();
		Blocks.addNames();
		Items.addRecipes();
		Blocks.addRecipes();
		
		new GuiHandler();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
