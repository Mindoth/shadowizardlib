package net.mindoth.shadowizardlib.client.particle.ember;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleEmber extends TextureSheetParticle {
    public boolean fade;
    public float colorR;
    public float colorG;
    public float colorB;
    public float initScale;
    public float initX;
    public float initY;
    public float initZ;
    public float destX;
    public float destY;
    public float destZ;
    public int renderType;

    protected ParticleEmber(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, float r, float g, float b, float scale, int lifetime,
                            boolean fade, int renderType, SpriteSet sprite) {
        super(level, x, y, z, xd, yd, zd);
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if ( this.colorR > 1.0F ) this.colorR = this.colorR / 255.0F;
        if ( this.colorG > 1.0F ) this.colorG = this.colorG / 255.0F;
        if ( this.colorB > 1.0F ) this.colorB = this.colorB / 255.0F;
        this.setColor(colorR, colorG, colorB);
        this.lifetime = lifetime;
        this.fade = fade;
        this.quadSize = scale;
        this.initScale = scale;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.initX = (float)x;
        this.initY = (float)y;
        this.initZ = (float)z;
        this.destX = (float)xd;
        this.destY = (float)yd;
        this.destZ = (float)zd;
        //this.roll = 2.0F * (float)Math.PI;
        this.pickSprite(sprite);
        this.renderType = renderType;
    }

    @Override
    public void tick() {
        super.tick();
        if ( this.fade ) {
            float lifeCoeff = (float)this.age / (float)this.lifetime;
            this.quadSize = initScale - initScale * lifeCoeff;
            this.alpha = (-(1 / (float)lifetime) * age + 1);
        }
        else if ( this.age >= this.lifetime - 5 ) {
            float lifeCoeff = (float)(this.age - this.lifetime + 5)/ (float)this.lifetime;
            this.quadSize = initScale - initScale * lifeCoeff;
            this.alpha = (-(1 / 5.0F) * (this.age - this.lifetime + 5) + 1);
        }
        //this.oRoll = roll;
        //roll += 1.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        ParticleRenderType type = ParticleRenderType.PARTICLE_SHEET_OPAQUE;
        if ( this.renderType == 1 ) type = ParticleRenderTypes.AM_RENDER_GLOW;
        if ( this.renderType == 2 ) type = ParticleRenderTypes.AM_RENDER_NO_ALPHA;
        if ( this.renderType == 3 ) type = ParticleRenderTypes.AM_RENDER_FLAT;
        return type;
    }


    @Override
    public int getLightColor(float pTicks){
        return 255;
    }

    @Override
    public boolean isAlive() {
        return this.age < this.lifetime;
    }
}
