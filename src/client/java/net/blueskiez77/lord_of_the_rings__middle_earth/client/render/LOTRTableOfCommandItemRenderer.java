package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderCommandTable.renderInvTable: the Table of Command's item, its block with the map on top,
 * turned a quarter, centred on where the player stands, at the nearest zoom.
 */
public final class LOTRTableOfCommandItemRenderer implements SpecialModelRenderer<Void> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "table_of_command");

    public static void init() {
        SpecialModelRenderers.ID_MAPPER.put(ID, Unbaked.MAP_CODEC);
    }

    @Override
    public @Nullable Void extractArgument(ItemStack stack) {
        return null;
    }

    @Override
    public void submit(@Nullable Void argument, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
                       boolean hasFoil, int outlineColor) {
        Player viewer = Minecraft.getInstance().player;
        double x = viewer == null ? 0.0 : viewer.getX();
        double z = viewer == null ? 0.0 : viewer.getZ();
        boolean meneltarma = viewer != null && LOTRPlayerAchievements.hasAchievement(viewer, LOTRAchievement.ENTER_MENELTARMA);
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.0f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        poseStack.translate(-0.5f, 0.0f, -0.5f);
        LOTRTableOfCommandRenderer.submitMap(poseStack, collector, x, z, 0, meneltarma, lightCoords);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-0.4f, 0.0f, -0.4f));
        output.accept(new Vector3f(1.4f, 1.15f, 1.4f));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Void> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
            return new LOTRTableOfCommandItemRenderer();
        }
    }
}
