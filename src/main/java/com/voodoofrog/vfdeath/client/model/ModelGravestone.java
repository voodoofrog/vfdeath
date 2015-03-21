package com.voodoofrog.vfdeath.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Gravestone - VoodooFrog
 * Created using Tabula 4.1.1
 */
public class ModelGravestone extends ModelBase {
    public ModelRenderer base;
    public ModelRenderer main;
    public ModelRenderer topLower;
    public ModelRenderer top;

    public ModelGravestone() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.topLower = new ModelRenderer(this, 0, -1);
        this.topLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.topLower.addBox(-6.0F, -7.0F, -7.0F, 12, 1, 2, 0.0F);
        this.top = new ModelRenderer(this, 1, -2);
        this.top.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top.addBox(-5.0F, -8.0F, -7.0F, 10, 1, 2, 0.0F);
        this.main = new ModelRenderer(this, -1, 0);
        this.main.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.main.addBox(-7.0F, -6.0F, -7.0F, 14, 13, 2, 0.0F);
        this.base = new ModelRenderer(this, 0, 11);
        this.base.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base.addBox(-8.0F, 7.0F, -8.0F, 16, 1, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.base.render(f5);
        this.main.render(f5);
        this.topLower.render(f5);
        this.top.render(f5);
    }

    public void renderAll()
    {
        this.base.render(0.0625F);
        this.main.render(0.0625F);
        this.topLower.render(0.0625F);
        this.top.render(0.0625F);
    }
    
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
