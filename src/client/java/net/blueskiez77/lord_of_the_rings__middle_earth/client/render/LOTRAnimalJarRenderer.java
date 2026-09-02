package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRAnimalJarBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Draws whatever a bird cage is holding.
 *
 * <p>LOTRTileEntityAnimalJar kept a live client-side copy of the caged creature
 * purely so the renderer had something to draw, and rebuilt it whenever the NBT
 * changed. This does the same: the entity is built from the block entity's tag,
 * cached against that tag, and never added to the level -- it exists only to be
 * a source of a render state.
 *
 * <p>Nothing is drawn when the cage is empty, or when the occupant is a kind of
 * entity this client cannot build.
 */
public class LOTRAnimalJarRenderer
        implements BlockEntityRenderer<LOTRAnimalJarBlockEntity, LOTRAnimalJarRenderState> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOTRAnimalJarRenderer.class);

    /** Where in the cage the occupant sits, as a fraction of the block. */
    private static final float PERCH_HEIGHT = 0.25F;

    /** How big the occupant is drawn: a bird has to fit inside one block. */
    private static final float SCALE = 0.5F;

    private final EntityRenderDispatcher entityRenderer;

    /** The last tag we built an entity for, and what we built. */
    private @Nullable CompoundTag cachedTag;
    private @Nullable Entity cachedEntity;

    /** Complain about an undrawable occupant once, not once a frame. */
    private boolean warned;

    private static int idFor(BlockPos pos) {
        int id = pos.hashCode();
        return id == 0 ? 1 : id;
    }

    public LOTRAnimalJarRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.entityRenderer();
    }

    @Override
    public LOTRAnimalJarRenderState createRenderState() {
        return new LOTRAnimalJarRenderState();
    }

    @Override
    public void extractRenderState(LOTRAnimalJarBlockEntity jar, LOTRAnimalJarRenderState state,
            float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTick, cameraPos, crumbling);
        state.occupant = null;

        Entity entity = occupantOf(jar);
        if (entity == null) {
            return;
        }
        state.height = PERCH_HEIGHT;
        // The original turned the caged creature on the spot; a whole turn every
        // ten seconds is slow enough not to be distracting.
        long time = jar.getLevel() == null ? 0L : jar.getLevel().getGameTime();
        state.spin = ((time + jar.getBlockPos().hashCode()) % 200L + partialTick) / 200.0F * 360.0F;
        state.occupant = extractOccupant(entity, partialTick);
    }

    /**
     * The occupant's render state.
     *
     * <p>Guarded, deliberately. A cage can be holding any entity type at all --
     * whatever the tag allowed when it was caught, including one from a mod
     * that has since changed -- and its renderer may want things a free-standing
     * entity does not have. A cosmetic bird is not worth taking the client down
     * for, so a renderer that objects means an empty cage for this frame.
     */
    private <T extends Entity> @Nullable EntityRenderState extractOccupant(T entity, float partialTick) {
        EntityRenderer<? super T, ?> renderer = entityRenderer.getRenderer(entity);
        if (renderer == null) {
            return null;
        }
        try {
            return renderer.createRenderState(entity, partialTick);
        } catch (RuntimeException e) {
            if (warned) {
                return null;
            }
            warned = true;
            LOGGER.error("Could not draw the occupant of a LOTR animal jar; leaving it empty", e);
            return null;
        }
    }

    /**
     * The caged creature, rebuilt only when the cage's contents actually change.
     * It is never added to the level: it is a model to copy, not a mob.
     */
    private @Nullable Entity occupantOf(LOTRAnimalJarBlockEntity jar) {
        CompoundTag tag = jar.getEntityData();
        if (tag == null || jar.getLevel() == null) {
            cachedTag = null;
            cachedEntity = null;
            return null;
        }
        if (cachedEntity != null && tag.equals(cachedTag)) {
            return cachedEntity;
        }
        cachedTag = tag;
        cachedEntity = EntityType.loadEntityRecursive(tag.copy(), jar.getLevel(),
                new EntitySpawnRequest(EntitySpawnReason.LOAD, true), e -> {
                    // The occupant is never added to the level, so nothing ever
                    // hands it a network id -- and Entity.getId throws rather
                    // than returning 0. Several renderers reach for it (the
                    // item model resolver seeds itself from it), so give it a
                    // stable non-zero one of our own, derived from the cage's
                    // position so the seed does not change between frames.
                    e.setId(idFor(jar.getBlockPos()));
                    return e;
                });
        return cachedEntity;
    }

    @Override
    public void submit(LOTRAnimalJarRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.occupant == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5F, state.height, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.spin));
        poseStack.scale(SCALE, SCALE, SCALE);
        entityRenderer.submit(state.occupant, camera, 0.0, 0.0, 0.0, poseStack, collector);
        poseStack.popPose();
    }
}
