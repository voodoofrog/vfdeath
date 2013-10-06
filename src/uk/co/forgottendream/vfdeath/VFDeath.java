package uk.co.forgottendream.vfdeath;

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

@Mod(modid = "VFDeath", name = "VoodooFrog's Death Mod", version = "0.1")
@NetworkMod(channels = {"vfdeath"}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class VFDeath {

	@Instance("VFDeath")
	public static VFDeath instance;
	
	@SidedProxy(clientSide = "uk.co.forgottendream.vfdeath.proxies.ClientProxy", serverSide = "uk.co.forgottendream.vfdeath.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.initSounds();
		proxy.initRenderers();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
