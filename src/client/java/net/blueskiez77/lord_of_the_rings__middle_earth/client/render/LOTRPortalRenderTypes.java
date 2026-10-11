package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client.LOTRRenderTypeInvoker;

import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

/** The built portals' surfaces. */
public final class LOTRPortalRenderTypes {

    /**
     * LOTRRenderElvenPortal: the end portal's own layered drawing (the 1.7.10 end portal's, which it
     * copied) over the elven portal's two textures.
     */
    public static final RenderType ELVEN_PORTAL = LOTRRenderTypeInvoker.lotr$create("lotr_elven_portal",
            RenderSetup.builder(RenderPipelines.END_PORTAL)
                    .withTexture("Sampler0", Identifier.fromNamespaceAndPath("lotr", "misc/elvenportal_0.png"))
                    .withTexture("Sampler1", Identifier.fromNamespaceAndPath("lotr", "misc/elvenportal_1.png"))
                    .createRenderSetup());

    /** LOTRRenderMorgulPortal: the gulduril glow added onto what lies behind (GL_SRC_COLOR, GL_ONE), its texture repeating. */
    private static final RenderPipeline MORGUL_PIPELINE = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withLocation(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pipeline/morgul_portal"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(new BlendFunction(BlendFactor.SRC_COLOR, BlendFactor.ONE)))
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderType MORGUL_PORTAL = LOTRRenderTypeInvoker.lotr$create("lotr_morgul_portal",
            RenderSetup.builder(MORGUL_PIPELINE)
                    .withTexture("Sampler0", Identifier.fromNamespaceAndPath("lotr", "misc/gulduril_glow.png"),
                            () -> RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR))
                    .createRenderSetup());

    /** LOTRRenderUtumnoReturnPortal's circles of light: blended, seen from both sides, writing no depth. */
    private static final RenderPipeline LIGHT_CIRCLE_PIPELINE = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withLocation(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "pipeline/utumno_light_circle"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new DepthStencilState(com.mojang.blaze3d.platform.CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderType UTUMNO_LIGHT_CIRCLE = LOTRRenderTypeInvoker.lotr$create("lotr_utumno_light_circle",
            RenderSetup.builder(LIGHT_CIRCLE_PIPELINE)
                    .withTexture("Sampler0", Identifier.fromNamespaceAndPath("lotr", "misc/utumno_portal_light_circle.png"))
                    .sortOnUpload()
                    .createRenderSetup());

    private LOTRPortalRenderTypes() {
    }
}
