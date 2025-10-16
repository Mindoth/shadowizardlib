package net.mindoth.shadowizardlib.client.particle.ember;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class ParticleRenderTypes {

    static final ParticleRenderType AM_RENDER_GLOW = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            //RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
        }


        @Override
        public String toString() {
            return "shadowizardlib:sw_rend_glow";
        }
    };

    static final ParticleRenderType AM_RENDER_NO_ALPHA = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();

            //Solution from here https://community.khronos.org/t/best-blending-mode-for-particles/14987
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

            //Old method
            /*RenderSystem.depthMask(false);
            textureManager.bindForSetup(TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);*/

            //This REALLY needs some work, Thanks Mojang...
            /*RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_SRC_COLOR, GlStateManager.DestFactor.DST_COLOR,
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);*/

            //Older method
            //RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            //RenderSystem.alphaFunc(516, 0.003921569F);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
        }

        @Override
        public String toString() {
            return "shadowizardlib:sw_rend_no_alpha";
        }
    };

    static final ParticleRenderType AM_RENDER_FLAT = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ZERO);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
        }

        @Override
        public String toString() {
            return "shadowizardlib:sw_rend_flat";
        }
    };
}
