package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSpiderModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRSpiderEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

import java.util.function.ToIntFunction;

/**
 * LOTRRenderSpiderBase and its kinds: a spider (the model inflated by 0.5)
 * at its own size, rolling right over when it dies, in its kind's skin -- a
 * Mirkwood spider's by its venom -- with two sets of eyes that glow: four 2x2
 * patches of the skin at (39, 10), (42, 11), (44, 11), (47, 10) and four 1x1
 * at (41, 8), (42, 9), (45, 9), (46, 8), drawn again at full brightness over
 * the head.
 *
 * <p>The size comes through the entity's own scale (getNPCScale), as the
 * original's preRenderCallback applied it.
 */
public class LOTRSpiderRenderer extends MobRenderer<LOTRSpiderEntity, LOTRSpiderRenderer.State, LOTRSpiderModel> {

    private static final Identifier[] MIRKWOOD = {texture("mirkwood"), texture("mirkwood_slowness"), texture("mirkwood_poison")};
    private static final Identifier MORDOR = texture("mordor");
    private static final int[][] EYES_LARGE = {{39, 10}, {42, 11}, {44, 11}, {47, 10}};
    private static final int[][] EYES_SMALL = {{41, 8}, {42, 9}, {45, 9}, {46, 8}};

    public static class State extends LivingEntityRenderState {
        public Identifier skin = MORDOR;
    }

    private final ToIntFunction<LOTRSpiderEntity> skinIndex;
    private final Identifier[] skins;

    private LOTRSpiderRenderer(EntityRendererProvider.Context context, Identifier[] skins,
                               ToIntFunction<LOTRSpiderEntity> skinIndex) {
        super(context, new LOTRSpiderModel(LOTRSpiderModel.createBodyLayer(0.5f).bakeRoot()), 1.0f);
        this.skins = skins;
        this.skinIndex = skinIndex;
        LOTRSpiderModel eyesModel = new LOTRSpiderModel(LOTRSpiderModel.createBodyLayer(0.55f).bakeRoot());
        eyesModel.showOnlyHead();
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (!state.isInvisible) {
                    for (Identifier eyes : new Identifier[]{
                            LOTRGlowingEyes.getEyesTexture(state.skin, EYES_LARGE, 2, 2),
                            LOTRGlowingEyes.getEyesTexture(state.skin, EYES_SMALL, 1, 1)}) {
                        collector.order(3).submitModel(eyesModel, state, poseStack, RenderTypes.eyes(eyes),
                                0xF000F0, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
                    }
                }
            }
        });
    }

    /** LOTRRenderMirkwoodSpider: the skin of its venom. */
    public static LOTRSpiderRenderer mirkwood(EntityRendererProvider.Context context) {
        return new LOTRSpiderRenderer(context, MIRKWOOD, LOTRSpiderEntity::getSpiderType);
    }

    /** LOTRRenderMordorSpider. */
    public static LOTRSpiderRenderer mordor(EntityRendererProvider.Context context) {
        return new LOTRSpiderRenderer(context, new Identifier[]{MORDOR}, spider -> 0);
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/spider/" + name + ".png");
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRSpiderEntity spider, State state, float partialTick) {
        super.extractRenderState(spider, state, partialTick);
        int index = this.skinIndex.applyAsInt(spider);
        state.skin = this.skins[Math.clamp(index, 0, this.skins.length - 1)];
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0f;
    }
}
