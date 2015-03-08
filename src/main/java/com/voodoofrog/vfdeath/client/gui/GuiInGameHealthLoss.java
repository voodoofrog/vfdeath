package com.voodoofrog.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.voodoofrog.vfdeath.ModInfo;
import com.voodoofrog.vfdeath.entity.ExtendedPlayer;

@SideOnly(Side.CLIENT)
public class GuiInGameHealthLoss extends Gui
{
	private Minecraft mc;
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID, "textures/gui/skull.png");
    protected static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");

	public GuiInGameHealthLoss(Minecraft mc)
	{
		super();
		this.mc = mc;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent.Pre event)
	{
		if (event.type != ElementType.HEALTH)
		{
			return;
		}

		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);
		if (props == null || props.getHealthMod() == 0)
		{
			return;
		}

		IAttributeInstance iattributeinstance = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		float maxHealthBase = (float)iattributeinstance.getBaseValue();
		float maxHealthCurr = (float)iattributeinstance.getAttributeValue();
		float healthDiff = maxHealthBase - maxHealthCurr;
        
		ScaledResolution resolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int x = (resolution.getScaledWidth() / 2 - 91) + ((MathHelper.ceiling_float_int(maxHealthCurr / 2.0F) * 8) + 2);
        int y = resolution.getScaledHeight() - 39;
		this.mc.getTextureManager().bindTexture(texture);
        
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

        for (int numHearts = MathHelper.ceiling_float_int(healthDiff / 2.0F) - 1; numHearts >= 0; --numHearts)
        {
    		int xAdj = x + numHearts % 10 * 8;
            
            this.drawTexturedModalRect(xAdj, y + 1, 0, 0, 7, 7);
        }
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		this.mc.getTextureManager().bindTexture(icons);
	}
}