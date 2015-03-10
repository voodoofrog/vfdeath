package com.voodoofrog.vfdeath.config;

import java.util.UUID;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.voodoofrog.vfdeath.misc.ModInfo;

public class ConfigHandler
{

	public static Configuration configFile;

	// Paths
	public static final String RESOURCE_PATH = ModInfo.ID + ":";
	public static final String GUI_PATH = "textures/gui/";
	public static final String GUI_CONTAINER_PATH = "textures/gui/container/";

	// GUIIDs
	public static final int ALTAR_GUI_ID = 0;

	// UUIDs
	public static final UUID HEALTH_MOD_UUID = UUID.fromString("5A4F4F80-37F1-11E3-AA6E-0800200C9A66");

	// General
	public static int RES_ANKH_XP_COST; // Default: 10
	public static int HEART_LOSS_ON_DEATH; // Default: 1
	public static Property GHOST_ALLOWED_BLOCKS;

	public static void syncConfig()
	{
		HEART_LOSS_ON_DEATH = configFile
				.get(Configuration.CATEGORY_GENERAL, "Heart Loss On Death", 1, "How many hearts a player loses on death").getInt();
		RES_ANKH_XP_COST = configFile.get(Configuration.CATEGORY_GENERAL, "Resurrection Ankh XP Cost", 10,
				"Cost in player levels to charge an ankh by one point").getInt();
		GHOST_ALLOWED_BLOCKS = configFile.get(Configuration.CATEGORY_GENERAL, "GhostAllowedBlocks", "",
				"Comma seperated list of block ids (no metadata) that you can interact with as a ghost.");

		if (configFile.hasChanged())
			configFile.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equals(ModInfo.ID))
			syncConfig();
	}
}
