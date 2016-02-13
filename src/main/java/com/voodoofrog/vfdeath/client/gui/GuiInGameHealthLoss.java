package com.voodoofrog.vfdeath.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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

import com.voodoofrog.vfdeath.entity.ExtendedPlayer;
import com.voodoofrog.vfdeath.misc.ModInfo;

import javafx.scene.shape.VertexFormat;

@SideOnly(Side.CLIENT)
public class GuiInGameHealthLoss extends Gui
{
	private Minecraft mc;
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID, "textures/gui/skull.png");
	private static final ResourceLocation ghostBlur = new ResourceLocation(ModInfo.ID, "textures/misc/ghost_blur.png");
	protected static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");

	public GuiInGameHealthLoss(Minecraft mc)
	{
		super();
		this.mc = mc;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void preRenderExperienceBar(RenderGameOverlayEvent.Pre event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);

		if (event.type != ElementType.HEALTH || (props == null || props.getHealthMod() == 0))
		{
			return;
		}

		IAttributeInstance iattributeinstance = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		float maxHealthBase = (float)iattributeinstance.getBaseValue();
		float maxHealthCurr = (float)iattributeinstance.getAttributeValue();
		float healthDiff = maxHealthBase - maxHealthCurr;

		if (props.getIsDead())
			healthDiff = 20f;

		ScaledResolution resolution = new ScaledResolution(this.mc);
		int x = (resolution.getScaledWidth() / 2 - 91);
		int y = resolution.getScaledHeight() - 39;
		this.mc.getTextureManager().bindTexture(texture);

		if (!props.getIsDead())
			x += ((MathHelper.ceiling_float_int(maxHealthCurr / 2.0F) * 8) + 2);

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

		if (props.getIsDead())
			event.setCanceled(true);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void preRenderHelmet(RenderGameOverlayEvent.Pre event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);

		if (event.type != ElementType.HELMET || (props == null || !props.getIsDead()))
		{
			return;
		}

		ScaledResolution resolution = new ScaledResolution(this.mc);

		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableAlpha();
		this.mc.getTextureManager().bindTexture(ghostBlur);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, (double)resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
		worldrenderer.pos((double)resolution.getScaledWidth(), (double)resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos((double)resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void preRenderHunger(RenderGameOverlayEvent.Pre event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);

		if (event.type != ElementType.FOOD || (props == null || !props.getIsDead()))
		{
			return;
		}

		event.setCanceled(true);
	}
}