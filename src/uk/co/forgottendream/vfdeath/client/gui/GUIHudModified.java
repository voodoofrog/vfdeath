package uk.co.forgottendream.vfdeath.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.event.ForgeSubscribe;

@SideOnly(Side.CLIENT)
public class GUIHudModified extends Gui
{
    protected final Random rand = new Random();
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected int updateCounter = 0;

    @ForgeSubscribe
    public void heartGUI(Pre event)
    {
        if (event.type == ElementType.HEALTH)
        {
            event.setCanceled(true);
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int playerHealth = scaledresolution.getScaledWidth();
            int playerPrevHealth = scaledresolution.getScaledHeight();

            if (this.mc.playerController.shouldDrawHUD())
            {
                this.renderHUD(playerHealth, playerPrevHealth);
            }
        }
    }

    public void renderHUD(int par1, int par2)
    {
        boolean highlight = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

        if (this.mc.thePlayer.hurtResistantTime < 10)
        {
            highlight = false;
        }

        int playerHealth = MathHelper.ceiling_float_int(this.mc.thePlayer.getHealth());
        int playerPrevHealth = MathHelper.ceiling_float_int(this.mc.thePlayer.prevHealth);
        this.rand.setSeed((long)(this.updateCounter * 312871));
        AttributeInstance attributeinstance = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        int k1 = par1 / 2 - 91;
        int l1 = par1 / 2 + 91;
        int i2 = par2 - 39;
        float maxHealth = (float)attributeinstance.getAttributeValue();
        float f1Absorption = this.mc.thePlayer.getAbsorptionAmount();
        int j2 = MathHelper.ceiling_float_int((maxHealth + f1Absorption) / 2.0F / 10.0F);
        int k2 = Math.max(10 - (j2 - 2), 3);
        float f2Absorption = f1Absorption;
        int j3 = -1;

        if (this.mc.thePlayer.isPotionActive(Potion.regeneration))
        {
            j3 = this.updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
        }

        this.mc.mcProfiler.startSection("health");

        for (int iter = MathHelper.ceiling_float_int((maxHealth + f1Absorption) / 2.0F) - 1; iter >= 0; --iter)
        {
            int iconX = 16;

            if (this.mc.thePlayer.isPotionActive(Potion.poison))
            {
                iconX += 36;
            }
            else if (this.mc.thePlayer.isPotionActive(Potion.wither))
            {
                iconX += 72;
            }

            byte b0 = 0;

            if (highlight)
            {
                b0 = 1;
            }

            int i4 = MathHelper.ceiling_float_int((float)(iter + 1) / 10.0F) - 1;
            int x = k1 + iter % 10 * 8;
            int y = i2 - i4 * k2;

            if (playerHealth <= 4)
            {
                y += this.rand.nextInt(2);
            }

            if (iter == j3)
            {
                y -= 2;
            }

            byte iconY = 0;

            if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
            {
                iconY = 5;
            }

            this.drawTexturedModalRect(x, y, 16 + b0 * 9, 9 * iconY, 9, 9);

            if (highlight)
            {
                if (iter * 2 + 1 < playerPrevHealth)
                {
                    this.drawTexturedModalRect(x, y, iconX + 54, 9 * iconY, 9, 9);
                }

                if (iter * 2 + 1 == playerPrevHealth)
                {
                    this.drawTexturedModalRect(x, y, iconX + 63, 9 * iconY, 9, 9);
                }
            }

            if (f2Absorption > 0.0F)
            {
                if (f2Absorption == f1Absorption && f1Absorption % 2.0F == 1.0F)
                {
                    this.drawTexturedModalRect(x, y, iconX + 153, 9 * iconY, 9, 9);
                }
                else
                {
                    this.drawTexturedModalRect(x, y, iconX + 144, 9 * iconY, 9, 9);
                }

                f2Absorption -= 2.0F;
            }
            else
            {
                if (iter * 2 + 1 < playerHealth)
                {
                    this.drawTexturedModalRect(x, y, iconX + 36, 9 * iconY, 9, 9);
                }

                if (iter * 2 + 1 == playerHealth)
                {
                    this.drawTexturedModalRect(x, y, iconX + 45, 9 * iconY, 9, 9);
                }
            }
        }

        this.mc.mcProfiler.endSection();
    }
}
