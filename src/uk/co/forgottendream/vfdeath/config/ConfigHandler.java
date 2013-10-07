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
	public static int REGEN_HEART_ID; //Default: 17070
	
	//General
	public static int REGEN_HEART_XP_COST; //Default: 10
	
	public static void initialize(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		REGEN_HEART_XP_COST = config.get(generalcat, "RegenHeartXPCost", 10).getInt();
		REGEN_HEART_ID = config.getItem(itemidscat, "RegenHeart", 17070).getInt() -256;
		
		config.save();
	}
	
}
