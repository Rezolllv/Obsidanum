package net.rezolv.obsidanum.block.entity.renderer;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;


public class ACRenderTypes extends RenderType {


    protected static final TransparencyStateShard EYES_ALPHA_TRANSPARENCY = new TransparencyStateShard("eyes_alpha_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });


    public ACRenderTypes(String s, VertexFormat format, VertexFormat.Mode mode, int i, boolean b1, boolean b2, Runnable runnable1, Runnable runnable2) {
        super(s, format, mode, i, b1, b2, runnable1, runnable2);
    }

    public static RenderType getParticleTrail(ResourceLocation resourceLocation) {
        return create("particle_trail", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new TextureStateShard(resourceLocation, true, true)).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(OVERLAY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(true));
    }

    public static RenderType getVoidBeingCloud(ResourceLocation resourceLocation) {
        return create("void_being", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new TextureStateShard(resourceLocation, false, true)).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(OVERLAY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(true));
    }

    public static RenderType getEyesAlphaEnabled(ResourceLocation locationIn) {
        CompositeState rendertype$compositestate = CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new TextureStateShard(locationIn, false, false)).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setDepthTestState(EQUAL_DEPTH_TEST).createCompositeState(true);
        return create("eye_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    }

    public static RenderType getAmbersolShine() {
        return create("ambersol_shine", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
                .setTransparencyState(EYES_ALPHA_TRANSPARENCY)
                .setCullState(CULL)
                .setLightmapState(NO_LIGHTMAP)
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .createCompositeState(true));
    }

    public static RenderType getNucleeperLights() {
        return create("nucleeper_lights", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
                .setTransparencyState(EYES_ALPHA_TRANSPARENCY)
                .setCullState(CULL)
                .setLightmapState(NO_LIGHTMAP)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(true));
    }



    public static RenderType getCrucibleItemBeam() {
        return create("crucible_item_beam", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_LIGHTNING_SHADER)
                .setCullState(CULL)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(NO_LIGHTMAP)
                .createCompositeState(true));
    }

    public static RenderType getSubmarineLights() {
        return create("submarine_lights", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setLightmapState(NO_LIGHTMAP)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false));
    }





    public static RenderType getSubmarineMask() {
        return create("submarine_mask", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RENDERTYPE_WATER_MASK_SHADER).setTextureState(NO_TEXTURE).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).setWriteMaskState(DEPTH_WRITE).setCullState(NO_CULL).createCompositeState(false));
    }

    public static RenderType getGhostly(ResourceLocation texture) {
        CompositeState renderState = CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                .setCullState(NO_CULL)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .createCompositeState(true);
        return create("ghostly", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }


    public static RenderType getTeslaBulb(ResourceLocation resourceLocation) {
        return create("tesla_bulb", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new TextureStateShard(resourceLocation, false, true)).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(true));
    }



}
