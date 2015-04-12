package com.voodoofrog.vfdeath.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.ribbit.Ribbit;
import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.inventory.ContainerResurrectionAltar;
import com.voodoofrog.vfdeath.misc.ModInfo;
import com.voodoofrog.vfdeath.network.server.ResurrectionButtonMessage;
import com.voodoofrog.vfdeath.tileentity.TileEntityResurrectionAltar;

@SideOnly(Side.CLIENT)
public class GuiResurrectionAltar extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID, "textures/gui/container/gui_altar.png");
	private TileEntityResurrectionAltar tileAltar;
	private GuiTextField playerText;
	private GuiButtonImage btnResurrect;
	private GuiButtonImage btnLeft;
	private GuiButtonImage btnRight;
	public String outputText;
	private int selectedEntry;
	private int counter;

	public GuiResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar)
	{
		super(new ContainerResurrectionAltar(playerInventory, altar));
		this.tileAltar = altar;

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.outputText = "";
		this.selectedEntry = 0;
		this.counter = 0;

		this.buttonList.clear();
		this.playerText = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 7, this.guiTop + 57, 111, 16);
		this.buttonList.add(this.btnResurrect = new GuiButtonImage(0, this.guiLeft + 137, this.guiTop + 57, 0, 48, 12, 11, 2));
		this.buttonList.add(this.btnLeft = new GuiButtonImage(1, this.guiLeft + 121, this.guiTop + 57, 12, 48, 12, 11, 2));
		this.buttonList.add(this.btnRight = new GuiButtonImage(2, this.guiLeft + 153, this.guiTop + 57, 24, 48, 12, 11, 2));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		super.drawScreen(mouseX, mouseY, partialTick);
		this.playerText.drawTextBox();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				VFDeath.packetDispatcher.sendToServer(new ResurrectionButtonMessage(this.tileAltar.getPlayerUUIDList().get(this.selectedEntry)));
			}
			else if (button.id == 1)
			{
				this.cycleSelectedEntryBack();
			}
			else
			{
				this.cycleSelectedEntryForward();
			}
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

		if (!this.tileAltar.getPlayerUUIDList().isEmpty())
		{
			this.btnResurrect.enabled = true;
			this.btnLeft.enabled = true;
			this.btnRight.enabled = true;

			if (this.outputText.isEmpty())
			{
				this.playerText.setTextColor(14737632);
				this.playerText.setText(this.getNameFromList(this.selectedEntry));
			}
			else if (this.counter < 60)
			{
				this.counter++;
				this.playerText.setTextColor(16711680);
				this.playerText.setText(I18n.format(this.outputText));
			}
			else
			{
				this.counter = 0;
				this.outputText = "";
			}
		}
		else
		{
			this.btnResurrect.enabled = false;
			this.btnLeft.enabled = false;
			this.btnRight.enabled = false;

			if (this.outputText.isEmpty())
			{
				this.playerText.setText("");
			}
			else if (this.counter < 60)
			{
				this.counter++;
				this.playerText.setTextColor(16711680);
				this.playerText.setText(I18n.format(this.outputText));
			}
			else
			{
				this.counter = 0;
				this.playerText.setTextColor(14737632);
				this.outputText = "";
			}
		}
	}

	private String getNameFromList(int index)
	{
		String result = "";

		if (!this.tileAltar.getPlayerUUIDList().isEmpty())
		{
			if ((index >= 0) && (index < this.tileAltar.getPlayerUUIDList().size()))
			{
				if (this.tileAltar.getPlayerUUIDList().get(index) != null)
				{
					result = Ribbit.playerUtils.getUserNameFromUUID(this.tileAltar.getPlayerUUIDList().get(index));
				}
			}
			else
			{
				this.cycleSelectedEntryBack();
				result = Ribbit.playerUtils.getUserNameFromUUID(this.tileAltar.getPlayerUUIDList().get(this.selectedEntry));
			}
		}

		return result;
	}

	private void cycleSelectedEntryForward()
	{
		if (this.selectedEntry + 1 <= (this.tileAltar.getPlayerUUIDList().size() - 1))
		{
			this.selectedEntry++;
		}
		else
		{
			this.selectedEntry = 0;
		}
	}

	private void cycleSelectedEntryBack()
	{
		if (this.selectedEntry - 1 >= 0)
		{
			this.selectedEntry--;
		}
		else
		{
			this.selectedEntry = this.tileAltar.getPlayerUUIDList().size() - 1;
		}
	}
}
