package uk.co.forgottendream.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uk.co.forgottendream.vfdeath.ModInfo;
import uk.co.forgottendream.vfdeath.config.ConfigHandler;
import uk.co.forgottendream.vfdeath.inventory.ContainerResurrectionAltar;
import uk.co.forgottendream.vfdeath.tileentity.TileEntityResurrectionAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiResurrectionAltar extends GuiContainer{

	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), ConfigHandler.GUI_CONTAINER_PATH + "gui_altar.png");
	private ContainerResurrectionAltar altarContainer;
	//private GuiTextField playerNameField;
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
        //this.playerNameField = new GuiTextField(this.fontRenderer, i + 62, j + 24, 103, 12);
        //this.playerNameField.setTextColor(-1);
        //this.playerNameField.setDisabledTextColour(-1);
        //this.playerNameField.setEnableBackgroundDrawing(false);
        //this.playerNameField.setMaxStringLength(40);
        //this.inventorySlots.removeCraftingFromCrafters(this);
        //this.inventorySlots.addCraftingToCrafters(this);
    }
	
	@Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        //this.inventorySlots.removeCraftingFromCrafters(this);
    }
	
	/*@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.fontRenderer.drawString(I18n.getString("container.repair"), 60, 6, 4210752);
        
        int k = 8453920;
        boolean flag = true;
        String s = I18n.getStringParams("container.repair.cost");

        if (!this.altarContainer.getSlot(2).getHasStack()) {
        	flag = false;
        }
        else if (!this.altarContainer.getSlot(2).canTakeStack(this.playerInventory.player)) {
        	k = 16736352;
        }

        if (flag) {
        	int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
        	int i1 = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
        	byte b0 = 67;

        	if (this.fontRenderer.getUnicodeFlag()) {
        		drawRect(i1 - 3, b0 - 2, this.xSize - 7, b0 + 10, -16777216);
        		drawRect(i1 - 2, b0 - 1, this.xSize - 8, b0 + 9, -12895429);
        	} else {
        		this.fontRenderer.drawString(s, i1, b0 + 1, l);
        		this.fontRenderer.drawString(s, i1 + 1, b0, l);
        		this.fontRenderer.drawString(s, i1 + 1, b0 + 1, l);
        	}

        	this.fontRenderer.drawString(s, i1, b0, k);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }*/

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		boolean hasAnkh = false;
		
		//TODO: Fix this
		for(int x = 0; x < 10; x++) {
			if(altarContainer.getSlot(x + 36).getHasStack()) {
				if(altarContainer.getSlot(x + 36).getStack().itemID == ConfigHandler.RES_ANKH_ID) {
					hasAnkh = true;
				} else {
					hasAnkh = false;
				}
			} else {
				hasAnkh = false;
			}
		}
		
		drawTexturedModalRect(guiLeft + 51, guiTop + 58, 0, this.ySize + (hasAnkh ? 0 : 16), 110, 16);
	}

}
