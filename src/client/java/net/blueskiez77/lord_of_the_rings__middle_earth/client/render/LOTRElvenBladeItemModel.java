package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderElvenBlade: an elven blade held in the hand glows when an orc is
 * within {@code distance} blocks of its holder (24, or 40 for Sting) -- drawn
 * with its glowing texture, without shading (the original turned lighting
 * off), and -- with "Animated Elven blade glow" on -- the enchantment glint
 * laid on four times over. Anywhere else -- in a GUI, on the ground, in a
 * frame -- it is drawn as usual.
 *
 * <p>Used in an items/*.json as {@code "type": "lotr:elven_blade"} with
 * {@code "model"}, {@code "glowing"} and {@code "distance"}.
 */
public record LOTRElvenBladeItemModel(ItemModel model, ItemModel glowing, double distance) implements ItemModel {

    private static final int GLINT_PASSES = 4;

    public static void init() {
        ItemModels.ID_MAPPER.put(net.minecraft.resources.Identifier.fromNamespaceAndPath(
                net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod.NAMESPACE, "elven_blade"), Unbaked.MAP_CODEC);
    }

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver,
                       ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        if (!glows(displayContext, level, owner)) {
            this.model.update(output, item, resolver, displayContext, level, owner, seed);
            return;
        }
        output.appendModelIdentityElement(this);
        boolean glint = LOTRConfig.elvenBladeGlow;
        int first = output.activeLayerCount;
        for (int pass = 0; pass < (glint ? GLINT_PASSES : 1); ++pass) {
            this.glowing.update(output, item, resolver, displayContext, level, owner, seed);
        }
        for (int i = first; i < output.activeLayerCount; ++i) {
            ItemStackRenderState.LayerRenderState layer = output.layers[i];
            if (glint) {
                layer.setFoilType(ItemStackRenderState.FoilType.STANDARD);
            }
            List<BakedQuad> quads = layer.prepareQuadList();
            quads.replaceAll(LOTRElvenBladeItemModel::unshaded);
        }
        if (glint) {
            output.setAnimated();
        }
    }

    private boolean glows(ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner) {
        if (level == null || !(displayContext.firstPerson() || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            return false;
        }
        LivingEntity holder = owner == null ? null : owner.asLivingEntity();
        return holder != null && !level.getEntitiesOfClass(LOTROrcEntity.class,
                holder.getBoundingBox().inflate(this.distance)).isEmpty();
    }

    private static BakedQuad unshaded(BakedQuad quad) {
        BakedQuad.MaterialInfo info = quad.materialInfo();
        if (!info.shade()) {
            return quad;
        }
        return new BakedQuad(quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(), quad.direction(),
                new BakedQuad.MaterialInfo(info.sprite(), info.layer(), info.itemRenderType(), info.tintIndex(),
                        false, info.lightEmission()));
    }

    public record Unbaked(ItemModel.Unbaked model, ItemModel.Unbaked glowing, double distance) implements ItemModel.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemModels.CODEC.fieldOf("model").forGetter(Unbaked::model),
                ItemModels.CODEC.fieldOf("glowing").forGetter(Unbaked::glowing),
                Codec.DOUBLE.optionalFieldOf("distance", 24.0).forGetter(Unbaked::distance)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            return new LOTRElvenBladeItemModel(this.model.bake(context, transformation),
                    this.glowing.bake(context, transformation), this.distance);
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            this.model.resolveDependencies(resolver);
            this.glowing.resolveDependencies(resolver);
        }
    }
}
