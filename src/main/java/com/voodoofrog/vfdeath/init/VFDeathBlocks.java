package com.voodoofrog.vfdeath.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.block.BlockCoffin;
import com.voodoofrog.vfdeath.block.BlockGravestone;
import com.voodoofrog.vfdeath.block.BlockResurrectionAltar;
import com.voodoofrog.vfdeath.misc.Strings;
import com.voodoofrog.vfdeath.tileentity.TileEntityCoffin;
import com.voodoofrog.vfdeath.tileentity.TileEntityGravestone;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

public class VFDeathBlocks
{
	public static BlockResurrectionAltar altar = new BlockResurrectionAltar();
	public static BlockGravestone gravestone = new BlockGravestone();
	public static BlockCoffin coffin = new BlockCoffin();

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(altar, Strings.ALTAR_NAME).setHardness(3.5F).setStepSound(Block.soundTypePiston)
				.setUnlocalizedName(Strings.ALTAR_UNLOCALIZED).setCreativeTab(VFDeath.vfdeathTab);
		GameRegistry.registerBlock(gravestone, Strings.GRAVESTONE_NAME).setHardness(3.5F).setStepSound(Block.soundTypePiston)
				.setUnlocalizedName(Strings.GRAVESTONE_UNLOCALIZED).setCreativeTab(VFDeath.vfdeathTab);
		GameRegistry.registerBlock(coffin, Strings.COFFIN_NAME).setHardness(2.5F).setStepSound(Block.soundTypeWood)
				.setUnlocalizedName(Strings.COFFIN_UNLOCALIZED).setCreativeTab(VFDeath.vfdeathTab);
	}

	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityResurrectionAltar.class, "tileEntityAltar");
		GameRegistry.registerTileEntity(TileEntityGravestone.class, "tileEntityGravestone");
		GameRegistry.registerTileEntity(TileEntityCoffin.class, "tileEntityCoffin");
	}
}
