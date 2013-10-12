package uk.co.forgottendream.vfdeath.config;

import java.io.File;

import net.minecraftforge.common.Configuration;
import uk.co.forgottendream.vfdeath.ModInfo;

public class ConfigHandler {

	//Paths
	public static final String RESOURCE_PATH = ModInfo.ID.toLowerCase() + ":";
	
	//Categories
	private static final String generalcat = "General";
	private static final String itemidscat = "ItemIDs";
	private static final String blockidscat = "BlockIDs";
	
	//ItemIDs
	public static int RES_ANKH_ID; //Default: 17070
	
	//General
	public static int RES_ANKH_XP_COST; //Default: 10
	
	public static void initialize(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		RES_ANKH_XP_COST = config.get(generalcat, "ResAnkhXPCost", 10).getInt();
		RES_ANKH_ID = config.getItem(itemidscat, "ResAnkh", 17070).getInt() -256;
		
		config.save();
	}
	
}
