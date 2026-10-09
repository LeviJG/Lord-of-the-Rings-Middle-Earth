package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

/**
 * A vanilla shield borne by someone who has chosen a LOTR shield: drawn as that shield, face out,
 * where the vanilla shield's plate would be and as tall as it, so it is held, raised and blocks as
 * the vanilla shield does (the item model's {@code lotr:shield_design}, chosen while the stack
 * carries the design; see LOTRItemModelResolverMixin).
 */
public final class LOTRShieldDesignRenderer implements SpecialModelRenderer<LOTRShields> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "shield_design");
    /** The 32-pixel square the art sits in, drawn so the art is the vanilla plate's 22 pixels tall. */
    private static final float SIZE = 2.0f;

    public static void init() {
        SpecialModelRenderers.ID_MAPPER.put(ID, Unbaked.MAP_CODEC);
    }

    @Override
    public @Nullable LOTRShields extractArgument(ItemStack stack) {
        return stack.get(LOTRDataComponents.SHIELD_DESIGN);
    }

    @Override
    public void submit(@Nullable LOTRShields shield, PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        if (shield == null) {
            return;
        }
        poseStack.pushPose();
        // Out of the model's space (y down, the plate facing -z), which the item model turned into,
        // and onto the plate's front.
        poseStack.scale(1.0f, -1.0f, -1.0f);
        poseStack.translate(-SIZE / 2.0f, -SIZE / 2.0f, 1.0f / 16.0f);
        poseStack.scale(SIZE, SIZE, SIZE);
        LOTRShieldRenderer.submitSlab(shield, poseStack, collector, lightCoords, 255);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-SIZE / 2.0f, -SIZE / 2.0f, -0.25f));
        output.accept(new Vector3f(SIZE / 2.0f, SIZE / 2.0f, 0.25f));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<LOTRShields> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<LOTRShields> bake(SpecialModelRenderer.BakingContext context) {
            return new LOTRShieldDesignRenderer();
        }
    }
}
