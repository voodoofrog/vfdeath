package uk.co.forgottendream.vfdeath.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.forgottendream.vfdeath.VFDeath;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockResurrectionAltar extends BlockContainer {
	
	@SideOnly(Side.CLIENT)
	private Icon topBottomIcon;
	private Icon sideIcon;
	
	protected BlockResurrectionAltar (int id) {
		super(id, Material.rock);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float var1, float var2, float var3) {
		if(!world.isRemote) {
			FMLNetworkHandler.openGui(player, VFDeath.instance, ConfigHandler.ALTAR_GUI_ID, world, x, y, z);
		}
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, id, meta);
	}
	
	private void dropItems(World world, int x, int y, int z) {
		Random rand = new Random();
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (!(tileEntity instanceof IInventory)) {
			return;
		}

		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float randomX = rand.nextFloat() * 0.8F + 0.1F;
				float randomY = rand.nextFloat() * 0.8F + 0.1F;
				float randomZ = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, x + randomX, y + randomY, z + randomZ, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}
				
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityResurrectionAltar();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		topBottomIcon = register.registerIcon(ConfigHandler.RESOURCE_PATH + "altar_top_bottom");
		sideIcon = register.registerIcon("minecraft:stonebrick");
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		if(side == 0 || side == 1) {
			return topBottomIcon;
		} else {
			return sideIcon;
		}
	}

}