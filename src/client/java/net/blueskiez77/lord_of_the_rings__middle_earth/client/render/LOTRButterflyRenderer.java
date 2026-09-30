package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.EnumMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRButterflyModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRButterflyEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderButterfly: shadow 0.2, drawn at 0.3, random skins per kind; the
 * Lórien butterfly is drawn at full brightness.
 */
public class LOTRButterflyRenderer extends MobRenderer<LOTRButterflyEntity, LOTRButterflyRenderState, LOTRButterflyModel> {

    private static final Map<LOTRButterflyEntity.ButterflyType, LOTRRandomSkins> SKINS =
            new EnumMap<>(LOTRButterflyEntity.ButterflyType.class);

    static {
        for (LOTRButterflyEntity.ButterflyType type : LOTRButterflyEntity.ButterflyType.values()) {
            SKINS.put(type, LOTRRandomSkins.loadSkinsList("lotr:mob/butterfly/" + type.textureDir,
                    "butterfly/" + type.textureDir));
        }
    }

    public LOTRButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRButterflyModel(LOTRButterflyModel.createBodyLayer().bakeRoot()), 0.2f);
    }

    @Override
    public LOTRButterflyRenderState createRenderState() {
        return new LOTRButterflyRenderState();
    }

    @Override
    public void extractRenderState(LOTRButterflyEntity butterfly, LOTRButterflyRenderState state, float partialTick) {
        super.extractRenderState(butterfly, state, partialTick);
        state.skin = SKINS.get(butterfly.getButterflyType()).getRandomSkin(butterfly.getUUID());
        state.still = butterfly.isButterflyStill();
        state.flapping = butterfly.flapTime > 0;
        state.wingTime = state.still && state.flapping ? butterfly.flapTime - partialTick : state.ageInTicks;
    }

    @Override
    protected int getBlockLightLevel(LOTRButterflyEntity butterfly, BlockPos pos) {
        return butterfly.getButterflyType() == LOTRButterflyEntity.ButterflyType.LORIEN
                ? 15 : super.getBlockLightLevel(butterfly, pos);
    }

    @Override
    protected int getSkyLightLevel(LOTRButterflyEntity butterfly, BlockPos pos) {
        return butterfly.getButterflyType() == LOTRButterflyEntity.ButterflyType.LORIEN
                ? 15 : super.getSkyLightLevel(butterfly, pos);
    }

    @Override
    protected void scale(LOTRButterflyRenderState state, PoseStack poseStack) {
        poseStack.scale(0.3f, 0.3f, 0.3f);
    }

    @Override
    public Identifier getTextureLocation(LOTRButterflyRenderState state) {
        return state.skin;
    }
}
