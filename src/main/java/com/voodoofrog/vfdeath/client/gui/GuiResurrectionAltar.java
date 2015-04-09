package com.voodoofrog.vfdeath.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.item.ItemResurrectionAnkh;
import com.voodoofrog.vfdeath.misc.ModInfo;
import com.voodoofrog.vfdeath.misc.Strings;
import com.voodoofrog.vfdeath.network.server.SendResButtonMessage;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

@SideOnly(Side.CLIENT)
public class GuiResurrectionAltar extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID, "textures/gui/container/gui_altar.png");
	private ContainerResurrectionAltar altarContainer;
	private GuiButton btnPlayer;
	private GuiButtonResurrect btnResurrect;
	private String outputText;
	private List<UUID> playerList;
	private int selectedEntry;

	public GuiResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar)
	{
		super(new ContainerResurrectionAltar(playerInventory, altar));
		this.altarContainer = (ContainerResurrectionAltar)this.inventorySlots;

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.playerList = this.altarContainer.getTileEntityAltar().getPlayerUUIDListFromAnkhs();
		this.selectedEntry = 0;

		this.buttonList.clear();
		this.buttonList
				.add(this.btnPlayer = new GuiButton(1, this.guiLeft + 7, this.guiTop + 55, 143, 20, this.getNameFromList(this.selectedEntry)));
		this.buttonList.add(this.btnResurrect = new GuiButtonResurrect(0, this.guiLeft + 153, this.guiTop + 57));
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				//TODO: This might be wrong
				if (!this.playerList.isEmpty())
				{
					VFDeath.packetDispatcher.sendToServer(new SendResButtonMessage((byte)getChargedAnkhCount(), this.playerList.get(this.selectedEntry)));
				}

				EntityPlayerMP resPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername("");

				if (resPlayer != null)
				{
					ExtendedPlayer props = ExtendedPlayer.get(resPlayer);

					if (!props.getIsDead())
					{
						this.outputText = Strings.GUI_KEY + "." + Strings.ALTAR_NAME + "." + Strings.ALTAR_GUI_NOT_DEAD;
					}
				}
				else
				{
					this.outputText = Strings.GUI_KEY + "." + Strings.ALTAR_NAME + "." + Strings.ALTAR_GUI_NOT_FOUND;
				}
			}
			else
			{
				this.playerList = this.altarContainer.getTileEntityAltar().getPlayerUUIDListFromAnkhs();
				this.cycleSelectedEntry();
				this.btnPlayer.displayString = this.getNameFromList(this.selectedEntry);
			}
		}
	}

	private int getChargedAnkhCount()
	{
		int count = 0;

		for (int i = 0; i < 10; i++)
		{
			ItemStack item = altarContainer.getSlot(i + 36).getStack();

			if (item != null)
			{
				if (item.getItem() instanceof ItemResurrectionAnkh)
				{
					ItemResurrectionAnkh ankh = (ItemResurrectionAnkh)item.getItem();

					if (ankh.hasEffect(item))
					{
						count++;
					}
				}
			}
		}
		return count;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		if (this.outputText != null && this.outputText != "")
		{
			this.fontRendererObj.drawString(StatCollector.translateToLocal(this.outputText),
					this.xSize / 2 - this.fontRendererObj.getStringWidth(this.outputText) / 2, this.guiTop + 34, 0xFF0000);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (this.getChargedAnkhCount() > 0)
		{
			this.btnResurrect.enabled = true;// false;
			this.btnPlayer.enabled = true;
		}
		else
		{
			this.btnResurrect.enabled = false;
			this.btnPlayer.enabled = false;
		}
	}

	private String getNameFromList(int index)
	{
		String result = "";

		if (!this.playerList.isEmpty())
		{
			if (this.playerList.get(index) != null)
			{
				result = Ribbit.playerUtils.getUserNameFromUUID(this.playerList.get(index));
			}
		}

		return result;
	}

	private void cycleSelectedEntry()
	{
		if (this.selectedEntry + 1 <= (this.playerList.size() - 1))
		{
			this.selectedEntry++;
		}
		else
		{
			this.selectedEntry = 0;
		}
	}
}
