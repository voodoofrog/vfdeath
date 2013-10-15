package uk.co.forgottendream.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uk.co.forgottendream.vfdeath.ModInfo;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import uk.co.forgottendream.vfdeath.inventory.ContainerResurrectionAltar;
import uk.co.forgottendream.vfdeath.item.ItemResurrectionAnkh;
import uk.co.forgottendream.vfdeath.network.PacketHandler;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiResurrectionAltar extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), ConfigHandler.GUI_CONTAINER_PATH + "gui_altar.png");
	private ContainerResurrectionAltar altarContainer;
	private GuiTextField playerNameField;
	private GuiButtonResurrect resButton;
	//private InventoryPlayer playerInventory;
	
	public GuiResurrectionAltar(InventoryPlayer playerInventory, TileEntityResurrectionAltar altar) {
		super(new ContainerResurrectionAltar(playerInventory, altar));
		//this.playerInventory = playerInventory;
		altarContainer = (ContainerResurrectionAltar)this.inventorySlots;
		
		xSize = 176;
		ySize = 166;
	}
	
	@Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.playerNameField = new GuiTextField(fontRenderer, guiLeft + 44, guiTop + 62, 103, 12);
        this.playerNameField.setTextColor(-1);
        this.playerNameField.setDisabledTextColour(-1);
        this.playerNameField.setEnableBackgroundDrawing(false);
        this.playerNameField.setMaxStringLength(40);
        
        buttonList.clear();
        buttonList.add(resButton = new GuiButtonResurrect(0, guiLeft + 25, guiTop + 58));
    }
	
	@Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
	
    protected void keyTyped(char par1, int par2) {
        if (this.playerNameField.textboxKeyTyped(par1, par2))
        {
            this.updateTextField();
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    private void updateTextField() {
        String s = this.playerNameField.getText();
        //this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload("MC|ItemName", s.getBytes()));
    }
    
	@Override
	protected void actionPerformed(GuiButton button) {
		PacketHandler.sendButtonPacket((byte) button.id, playerNameField.getText());
	}
	
	@Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.playerNameField.mouseClicked(par1, par2, par3);
    }
	
    private int getChargedAnkhCount() {
		int count = 0;
		
		for(int x = 0; x < 10; x++) {
			ItemStack item = altarContainer.getSlot(x + 36).getStack();

			if(item != null) {
				if(item.getItem() instanceof ItemResurrectionAnkh) {
					ItemResurrectionAnkh ankh = (ItemResurrectionAnkh) item.getItem();

					if (ankh.hasEffect(item)) {
						count++;
					}
				}
			}
		}
		return count;
    }
    
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.playerNameField.drawTextBox();
    }
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		drawTexturedModalRect(guiLeft + 41, guiTop + 58, 0, this.ySize + (getChargedAnkhCount() > 0 ? 0 : 16), 110, 16);
	}
	
	@Override
    public void updateScreen()
    {
        super.updateScreen();
        
        if (getChargedAnkhCount() > 0)
        {
            this.playerNameField.setEnabled(true);
            if(!this.playerNameField.getText().isEmpty()) {
            	resButton.enabled = true;
            } else {
            	resButton.enabled = false;
            }
        } else {
            this.playerNameField.setEnabled(false);
            resButton.enabled = false;
        }
    }

}
