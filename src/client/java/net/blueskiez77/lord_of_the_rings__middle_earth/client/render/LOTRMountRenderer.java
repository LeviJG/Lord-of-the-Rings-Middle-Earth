package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.function.BiFunction;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCamelEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRHorseEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;

import org.jspecify.annotations.Nullable;

/**
 * The LOTR mounts' renderer: LOTRRenderHorse and its kin. Each mount draws
 * its skin, then -- where the original baked them into one LayeredTexture or
 * drew a second pass -- the extra sheets over the same model: the horse's
 * markings, the barding, the saddle (an inflated copy of the model in the
 * saddle sheet) and the camel's carpet (an inflated copy, the base dyed and
 * the overlay not; the original generated the dyed sheet at run time, which
 * a tint does the same way).
 */
public class LOTRMountRenderer<T extends LOTRHorseEntity, M extends EntityModel<LOTRMountRenderState>>
        extends AbstractHorseRenderer<T, LOTRMountRenderState, M> {

    private final BiFunction<T, LOTRMountRenderState, Identifier> skin;
    private final float scale;

    @SuppressWarnings("unchecked")
    private LOTRMountRenderer(EntityRendererProvider.Context context, M adult, M baby, float shadow, float scale,
                              BiFunction<T, LOTRMountRenderState, Identifier> skin) {
        super(context, adult, baby);
        this.shadowRadius = shadow;
        this.scale = scale;
        this.skin = skin;
        // The barding, over the skin (LOTRRenderHorse.getLayeredMountTexture).
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRMountRenderState state, float yRot, float xRot) {
                if (state.armor != null) {
                    coloredCutoutModelCopyLayerRender(getParentModel(), state.armor, poseStack, collector, light, state, -1, 2);
                }
            }
        });
    }

    public static <T extends LOTRHorseEntity, M extends EntityModel<LOTRMountRenderState>> LOTRMountRenderer<T, M> create(
            EntityRendererProvider.Context context, M adult, M baby, float shadow, float scale,
            BiFunction<T, LOTRMountRenderState, Identifier> skin) {
        return new LOTRMountRenderer<>(context, adult, baby, shadow, scale, skin);
    }

    /** A copy of the model, inflated, drawn in the saddle sheet on a saddled mount (shouldRenderPass 0). */
    public LOTRMountRenderer<T, M> withSaddle(M saddleAdult, M saddleBaby, Identifier texture) {
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRMountRenderState state, float yRot, float xRot) {
                if (!state.saddle.isEmpty() && !state.isInvisible) {
                    M model = state.isBaby ? saddleBaby : saddleAdult;
                    collector.order(3).submitModel(model, state, poseStack, RenderTypes.entityCutout(texture), light,
                            LivingEntityRenderer.getOverlayCoords(state, 0.0f), -1, null, state.outlineColor, null);
                }
            }
        });
        return this;
    }

    /** The horse's markings, in the coat's place in the layered texture. */
    public LOTRMountRenderer<T, M> withMarkings() {
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRMountRenderState state, float yRot, float xRot) {
                if (state.markings != null) {
                    coloredCutoutModelCopyLayerRender(getParentModel(), state.markings, poseStack, collector, light, state, -1, 1);
                }
            }
        });
        return this;
    }

    /** LOTRRenderCamel's pass 1: the carpet, dyed. */
    public LOTRMountRenderer<T, M> withCarpet(M carpetAdult, M carpetBaby) {
        Identifier base = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/camel/carpet_base.png");
        Identifier overlay = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/camel/carpet_overlay.png");
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRMountRenderState state, float yRot, float xRot) {
                if (state.carpetColor != 0 && !state.isInvisible) {
                    M model = state.isBaby ? carpetBaby : carpetAdult;
                    int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
                    collector.order(4).submitModel(model, state, poseStack, RenderTypes.entityCutout(base), light,
                            overlayCoords, state.carpetColor, null, state.outlineColor, null);
                    collector.order(5).submitModel(model, state, poseStack, RenderTypes.entityCutout(overlay), light,
                            overlayCoords, -1, null, state.outlineColor, null);
                }
            }
        });
        return this;
    }

    public LOTRMountRenderer<T, M> withLayer(RenderLayer<LOTRMountRenderState, M> layer) {
        addLayer(layer);
        return this;
    }

    @Override
    public LOTRMountRenderState createRenderState() {
        return new LOTRMountRenderState();
    }

    @Override
    public void extractRenderState(T mount, LOTRMountRenderState state, float partialTick) {
        super.extractRenderState(mount, state, partialTick);
        state.armor = LOTRMountArmorTextures.textureFor(mount.getBodyArmorItem());
        state.riddenByPlayer = mount.getFirstPassenger() instanceof Player;
        state.christmas = LOTRMod.isChristmas();
        state.carpetColor = 0;
        state.chested = false;
        if (mount instanceof LOTRCamelEntity camel) {
            DyeColor color = camel.getCamelCarpetColor();
            state.carpetColor = color == null ? 0 : color.getTextureDiffuseColor();
            state.chested = camel.hasChest();
            state.armor = null;
        }
        state.markings = null;
        state.skin = this.skin.apply(mount, state);
    }

    @Override
    protected void scale(LOTRMountRenderState state, PoseStack poseStack) {
        if (this.scale != 1.0f) {
            poseStack.scale(this.scale, this.scale, this.scale);
        }
    }

    @Override
    public Identifier getTextureLocation(LOTRMountRenderState state) {
        return state.skin;
    }

}
