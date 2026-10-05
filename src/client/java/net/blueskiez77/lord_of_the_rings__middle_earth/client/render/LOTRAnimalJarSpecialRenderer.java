package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRAnimalJarItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderAnimalJar's item half: a full jar in hand, in a slot or on the
 * ground shows its creature inside, standing on the jar's floor, facing
 * forwards and animated by the viewer's age. The jar itself is the item
 * model's other part ({@code lotr:animal_jar} under a composite).
 */
public final class LOTRAnimalJarSpecialRenderer implements SpecialModelRenderer<CompoundTag> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "animal_jar");

    /** The creatures built for the tags seen, so a slot does not rebuild one every frame. */
    private final Map<CompoundTag, Entity> built = new HashMap<>();
    private @Nullable Level builtFor;

    public static void init() {
        SpecialModelRenderers.ID_MAPPER.put(ID, Unbaked.MAP_CODEC);
    }

    @Override
    public @Nullable CompoundTag extractArgument(ItemStack stack) {
        return LOTRAnimalJarItem.getJarEntity(stack);
    }

    @Override
    public void submit(@Nullable CompoundTag tag, PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = tag == null ? null : occupant(minecraft.level, tag);
        if (entity == null) {
            return;
        }
        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
        entity.tickCount = minecraft.player == null ? 0 : minecraft.player.tickCount;
        entity.setYRot(0.0f);
        entity.yRotO = 0.0f;
        if (entity instanceof LivingEntity living) {
            living.yBodyRot = living.yBodyRotO = living.yHeadRot = living.yHeadRotO = 0.0f;
        }
        EntityRenderState state = renderState(dispatcher, entity,
                minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false));
        if (state == null) {
            return;
        }
        state.lightCoords = lightCoords;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.0f, 0.5f);
        dispatcher.submit(state, new CameraRenderState(), 0.0, 0.0, 0.0, poseStack, collector);
        poseStack.popPose();
    }

    private @Nullable Entity occupant(@Nullable Level level, CompoundTag tag) {
        if (level == null) {
            return null;
        }
        if (level != this.builtFor || this.built.size() > 64) {
            this.built.clear();
            this.builtFor = level;
        }
        return this.built.computeIfAbsent(tag, t -> {
            Entity e = EntityType.loadEntityRecursive(t.copy(), level,
                    new EntitySpawnRequest(EntitySpawnReason.LOAD, true), x -> x);
            if (e != null) {
                // Never added to the level; renderers want an id all the same.
                e.setId(Integer.MAX_VALUE - this.built.size());
                e.snapTo(0.0, 0.0, 0.0, 0.0f, 0.0f);
            }
            return e;
        });
    }

    private static <T extends Entity> @Nullable EntityRenderState renderState(EntityRenderDispatcher dispatcher,
                                                                             T entity, float partialTick) {
        EntityRenderer<? super T, ?> renderer = dispatcher.getRenderer(entity);
        if (renderer == null) {
            return null;
        }
        try {
            return renderer.createRenderState(entity, partialTick);
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(0.0f, 0.0f, 0.0f));
        output.accept(new Vector3f(1.0f, 1.0f, 1.0f));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<CompoundTag> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<CompoundTag> bake(SpecialModelRenderer.BakingContext context) {
            return new LOTRAnimalJarSpecialRenderer();
        }
    }
}
