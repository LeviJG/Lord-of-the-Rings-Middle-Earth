package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRAnimalJarBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LOTRRenderAnimalJar: draws whatever a bird cage or butterfly jar holds --
 * the jar's own copy of the creature (see LOTRAnimalJarBlockEntity), at the
 * jar's height less half its own, bobbing gently, facing the way it turned.
 * A butterfly instead turns to face whoever looks at it.
 *
 * <p>Nothing is drawn when the jar is empty, or when the occupant is a kind of
 * entity this client cannot draw.
 */
public class LOTRAnimalJarRenderer
        implements BlockEntityRenderer<LOTRAnimalJarBlockEntity, LOTRAnimalJarRenderState> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOTRAnimalJarRenderer.class);

    private final EntityRenderDispatcher entityRenderer;

    /** Complain about an undrawable occupant once, not once a frame. */
    private boolean warned;

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
        Entity entity = jar.getOrCreateJarEntity();
        if (entity == null) {
            return;
        }
        if (entity instanceof LivingEntity living) {
            if (jar.isEntityWatching()) {
                double dx = living.getX() - cameraPos.x;
                double dz = living.getZ() - cameraPos.z;
                float lookYaw = (float) Math.toDegrees(Math.atan2(dz, dx));
                living.setYRot(lookYaw + 90.0f);
                living.yRotO = living.getYRot();
            }
            living.yBodyRot = living.getYRot();
            living.yBodyRotO = living.yRotO;
            living.yHeadRot = living.getYRot();
            living.yHeadRotO = living.yRotO;
        }
        state.height = jar.getEntityHeight() - entity.getBbHeight() / 2.0f + jar.getEntityBobbing(partialTick);
        state.occupant = extractOccupant(entity, partialTick);
    }

    /**
     * The occupant's render state.
     *
     * <p>Guarded, deliberately. A jar can be holding any entity type at all --
     * whatever the tag allowed when it was caught, including one from a mod
     * that has since changed -- and its renderer may want things a free-standing
     * entity does not have. A cosmetic bird is not worth taking the client down
     * for, so a renderer that objects means an empty jar for this frame.
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

    @Override
    public void submit(LOTRAnimalJarRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.occupant == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5F, state.height, 0.5F);
        entityRenderer.submit(state.occupant, camera, 0.0, 0.0, 0.0, poseStack, collector);
        poseStack.popPose();
    }
}
