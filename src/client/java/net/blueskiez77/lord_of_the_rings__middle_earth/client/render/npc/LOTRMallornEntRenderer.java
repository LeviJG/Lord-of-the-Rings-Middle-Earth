package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTREntModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTREntEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRMallornEntEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

/**
 * LOTRRenderMallornEnt: an Ent half as big again in mallorn bark, sunk in the
 * ground as it rises. With its weapon shield up it is drawn at full light,
 * wrapped in the shield's glow -- added over it, drifting across it, and
 * slowly brightening and dimming (0.15 to 0.45).
 */
public class LOTRMallornEntRenderer extends LOTREntRenderer {

    private static final Identifier SKIN = texture("mallorn");
    private static final Identifier SHIELD = texture("mallorn_ent_shield");

    public LOTRMallornEntRenderer(EntityRendererProvider.Context context) {
        super(context);
        LOTREntModel shieldModel = new LOTREntModel(LOTREntModel.createLayer(0.0f).bakeRoot(), LOTREntModel.Piece.ALL);
        // shouldRenderPass 1: the shield.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTREntRenderState state,
                               float yRot, float xRot) {
                if (state.weaponShield) {
                    float f = state.ageInTicks;
                    float scroll = f * 0.01f % 1.0f;
                    float alpha = 0.3f + Mth.sin(f * 0.05f) * 0.15f;
                    collector.order(1).submitModel(shieldModel, state, poseStack,
                            RenderTypes.energySwirl(SHIELD, scroll, scroll), 0xF000F0, OverlayTexture.NO_OVERLAY,
                            ARGB.colorFromFloat(1.0f, alpha, alpha, alpha), null, state.outlineColor, null);
                }
            }
        });
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/ent/" + name + ".png");
    }

    @Override
    public void extractRenderState(LOTREntEntity ent, LOTREntRenderState state, float partialTick) {
        super.extractRenderState(ent, state, partialTick);
        state.skin = SKIN;
        if (ent instanceof LOTRMallornEntEntity mallornEnt) {
            state.spawningOffset = mallornEnt.getSpawningOffset(partialTick);
            state.weaponShield = mallornEnt.isWeaponShieldActive();
        }
    }

    /** preRenderCallback: full light while the shield is up. */
    @Override
    protected int getBlockLightLevel(LOTREntEntity ent, BlockPos pos) {
        return ent instanceof LOTRMallornEntEntity mallornEnt && mallornEnt.isWeaponShieldActive()
                ? 15 : super.getBlockLightLevel(ent, pos);
    }

    @Override
    protected int getSkyLightLevel(LOTREntEntity ent, BlockPos pos) {
        return ent instanceof LOTRMallornEntEntity mallornEnt && mallornEnt.isWeaponShieldActive()
                ? 15 : super.getSkyLightLevel(ent, pos);
    }

    /** preRenderCallback: BOSS_SCALE, and the rise out of the ground (in its flipped y). */
    @Override
    protected void scale(LOTREntRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        float scale = LOTRMallornEntEntity.BOSS_SCALE;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0f, state.spawningOffset, 0.0f);
    }
}
