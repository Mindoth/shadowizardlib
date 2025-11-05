package net.mindoth.shadowizardlib.client.particle.ember;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class ParticleRenderTypes {

    private static final BlendFunction GLOW_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE);
    public static final RenderPipeline GLOW_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/shadowizardlib_glow")
                    .withDepthWrite(false)
                    .withBlend(GLOW_BLEND)
                    .build()
    );

    public static final SingleQuadParticle.Layer SW_RENDER_GLOW = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, GLOW_PIPELINE);

    private static final BlendFunction NO_ALPHA_BLEND = new BlendFunction(SourceFactor.SRC_COLOR, DestFactor.ONE);
    public static final RenderPipeline NO_ALPHA_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/shadowizardlib_no_alpha")
                    .withDepthWrite(false)
                    .withBlend(NO_ALPHA_BLEND)
                    .build()
    );

    public static final SingleQuadParticle.Layer SW_RENDER_NO_ALPHA = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, NO_ALPHA_PIPELINE);

    private static final BlendFunction FLAT_BLEND = new BlendFunction(SourceFactor.SRC_COLOR, DestFactor.ZERO);
    public static final RenderPipeline FLAT_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                    .withLocation("pipeline/shadowizardlib_no_alpha")
                    .withDepthWrite(false)
                    .withBlend(FLAT_BLEND)
                    .build()
    );

    public static final SingleQuadParticle.Layer SW_RENDER_FLAT = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, FLAT_PIPELINE);
}
