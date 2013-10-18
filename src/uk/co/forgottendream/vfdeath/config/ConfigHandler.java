package uk.co.forgottendream.vfdeath.config;

import java.io.File;
import java.util.UUID;

import net.minecraftforge.common.Configuration;
import uk.co.forgottendream.vfdeath.ModInfo;

public class ConfigHandler {

	//Paths
	public static final String RESOURCE_PATH = ModInfo.ID.toLowerCase() + ":";
	public static final String GUI_PATH = "textures/gui/";
	public static final String GUI_CONTAINER_PATH = "textures/gui/container/";
	
	//Categories
	private static final String generalcat = "General";
	private static final String itemidscat = "ItemIDs";
	private static final String blockidscat = "BlockIDs";
	
	//ItemIDs
	public static int RES_ANKH_ID; //Default: 17070
	
	//BlockIDs
	public static int ALTAR_ID; //Default: 1700
	
	//GUIIDs
	public static final int ALTAR_GUI_ID = 0;
	
	//UUIDs
	public static final UUID HEALTH_MOD_UUID = UUID.fromString("5A4F4F80-37F1-11E3-AA6E-0800200C9A66");
	
	//General
	public static int RES_ANKH_XP_COST; //Default: 10
	public static int HEART_LOSS_ON_DEATH; //Default: 1
	
	
	public static void initialize(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		HEART_LOSS_ON_DEATH = config.get(generalcat, "HeartLossOnDeath", 1).getInt();
		RES_ANKH_XP_COST = config.get(generalcat, "ResAnkhXPCost", 10).getInt();
		RES_ANKH_ID = config.getItem(itemidscat, "ResAnkh", 17070).getInt() -256;
		ALTAR_ID = config.getBlock(blockidscat, "ResAltar", 1700).getInt();
		
		config.save();
	}
	
}
