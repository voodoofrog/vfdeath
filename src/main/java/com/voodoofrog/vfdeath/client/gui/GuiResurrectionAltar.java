package com.voodoofrog.vfdeath.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiRenameWorld;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
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
	//private GuiTextField playerNameField;
	private GuiButtonResurrect resButton;
	private String outputText;
	private List<UUID> playerList;
	private int selectedEntry;

	/*
	 * private GuiButton button1; private GuiButton button2; private GuiButton button3; private GuiButton button4;
	 */

	// private InventoryPlayer playerInventory;

	public GuiResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar)
	{
		super(new ContainerResurrectionAltar(playerInventory, altar));
		// this.playerInventory = playerInventory;
		this.altarContainer = (ContainerResurrectionAltar)this.inventorySlots;

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		//int i = (this.width - this.xSize) / 2;
		//int j = (this.height - this.ySize) / 2;
		// TODO: Work out whatever the fuck this starting int is!
		//this.playerNameField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 44, this.guiTop + 56, 103, 12);
		//this.playerNameField.setTextColor(-1);
		//this.playerNameField.setDisabledTextColour(-1);
		//this.playerNameField.setEnableBackgroundDrawing(false);
		//this.playerNameField.setMaxStringLength(40);
		
		this.playerList = this.altarContainer.getTileEntityAltar().getPlayerUUIDListFromAnkhs();

		this.buttonList.clear();
		this.buttonList.add(resButton = new GuiButtonResurrect(0, this.guiLeft + 25, this.guiTop + 52));
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	/*@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if (this.playerNameField.textboxKeyTyped(par1, par2))
		{
			this.updateTextField();
		}
		else
		{
			super.keyTyped(par1, par2);
		}
	}*/

	/*private void updateTextField()
	{
		String s = this.playerNameField.getText();
	}*/

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
        		VFDeath.packetDispatcher.sendToServer(new SendResButtonMessage((byte)getChargedAnkhCount(), ""/*playerNameField.getText()*/));

        		EntityPlayerMP resPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(""/*this.playerNameField.getText()*/);

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
            }
        }
    }

	/*@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		super.mouseClicked(par1, par2, par3);
		this.playerNameField.mouseClicked(par1, par2, par3);
	}*/

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
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		//GlStateManager.disableLighting();
		//GlStateManager.disableDepth();
		//this.playerNameField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
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

		//this.drawTexturedModalRect(this.guiLeft + 41, this.guiTop + 52, 0, this.ySize + (this.getChargedAnkhCount() > 0 ? 0 : 16), 110, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (this.getChargedAnkhCount() > 0)
		{
			//this.playerNameField.setEnabled(true);
			//if (!this.playerNameField.getText().isEmpty())
			//{
			//	this.resButton.enabled = true;
			//}
			//else
			//{
				this.resButton.enabled = true;//false;
			//}
		}
		else
		{
			//this.playerNameField.setEnabled(false);
			this.resButton.enabled = false;
		}
	}
}
