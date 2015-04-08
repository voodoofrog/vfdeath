package com.voodoofrog.vfdeath.client.gui;

import java.io.IOException;
import java.util.Iterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.voodoofrog.vfdeath.VFDeath;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.network.server.RespawnMessage;

@SideOnly(Side.CLIENT)
public class GuiDeathScreen extends GuiScreen implements GuiYesNoCallback
{
	private int enableButtonsTimer;
	private boolean field_146346_f = false;

	public void initGui()
	{
		this.buttonList.clear();

		if (this.lostAllHardcoreLives())
		{
			if (this.mc.isIntegratedServerRunning())
			{
				this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld",
						new Object[0])));
			}
			else
			{
				this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer",
						new Object[0])));
			}
		}
		else
		{
			ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);
			String respawnButtonText = props.isOnLastLife() ? I18n.format("gui.vfdeath.deathScreen.respawnGhost", new Object[0]) : I18n.format(
					"gui.vfdeath.deathScreen.recover", new Object[0]);
			
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format(respawnButtonText, new Object[0])));
			this.buttonList
					.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));

			if (this.mc.getSession() == null)
			{
				((GuiButton)this.buttonList.get(1)).enabled = false;
			}
		}

		GuiButton guibutton;

		for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = false)
		{
			guibutton = (GuiButton)iterator.next();
		}
	}

	/**
	 * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
	}

	protected void actionPerformed(GuiButton button) throws IOException
	{
		switch (button.id)
		{
		case 0:
			this.respawnPlayer();
			this.mc.displayGuiScreen((GuiScreen)null);
			break;
		case 1:
			GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format(
					"deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
			this.mc.displayGuiScreen(guiyesno);
			guiyesno.setButtonDelay(20);
		}
	}

	public void confirmClicked(boolean result, int id)
	{
		if (result)
		{
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld((WorldClient)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}
		else
		{
			this.respawnPlayer();
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);
		String titleText = this.lostAllHardcoreLives() ? I18n.format("gui.vfdeath.deathScreen.title.hardcore", new Object[0]) : (props.isOnLastLife() ? I18n.format(
				"gui.vfdeath.deathScreen.title.lastLife", new Object[0]) : I18n.format("gui.vfdeath.deathScreen.title", new Object[0]));
		
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		this.drawCenteredString(this.fontRendererObj, titleText, this.width / 2 / 2, 30, 16777215);
		GlStateManager.popMatrix();

		if (this.lostAllHardcoreLives())
		{
			this.drawCenteredString(this.fontRendererObj, I18n.format("gui.vfdeath.deathScreen.hardcoreInfo", new Object[0]), this.width / 2, 144,
					16777215);
		}

		this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + EnumChatFormatting.YELLOW
				+ this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();
		++this.enableButtonsTimer;
		GuiButton guibutton;

		if (this.enableButtonsTimer == 20)
		{
			for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = true)
			{
				guibutton = (GuiButton)iterator.next();
			}
		}
	}

	private void respawnPlayer()
	{
		if (this.lostAllHardcoreLives())
		{
			this.mc.thePlayer.respawnPlayer();
		}
		else
		{
			VFDeath.packetDispatcher.sendToServer(new RespawnMessage());
		}
	}

	private boolean lostAllHardcoreLives()
	{
		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);
		
		return this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled() && props.isOnLastLife();
	}
}