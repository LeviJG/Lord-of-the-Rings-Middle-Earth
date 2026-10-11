package net.blueskiez77.lord_of_the_rings__middle_earth.client.particle;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMarshWraithModel;

import net.fabricmc.fabric.api.client.particle.v1.ParticleGroupRegistry;

import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityDeadMarshFace: one of the dead, a marsh wraith's form lying face-up in the water of the
 * Dead Marshes at some angle, that slowly shows and fades again over six to twelve seconds
 * (LOTRRenderDeadMarshFace).
 */
public class LOTRDeadMarshFaceParticle extends Particle {

    /** The faces' own particle group; Fabric's ParticleGroupRegistry reads a modded group's name as its identifier. */
    public static final ParticleRenderType FACES = new ParticleRenderType("lotr:dead_marsh_faces", "LDMF");
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath("lotr", "textures/entity/wraith/marsh_wraith.png");
    private static ModelPart model;

    private final float faceYaw;
    private final float facePitch;
    private float faceAlpha;
    private float prevFaceAlpha;

    public LOTRDeadMarshFaceParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 120 + this.random.nextInt(120);
        this.faceYaw = level.getRandom().nextFloat() * 360.0f;
        this.facePitch = -60.0f + level.getRandom().nextFloat() * 120.0f;
        this.hasPhysics = false;
        this.gravity = 0.0f;
    }

    public static void init() {
        ParticleGroupRegistry.register(FACES, Group::new);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        ++this.age;
        this.prevFaceAlpha = this.faceAlpha;
        this.faceAlpha = Mth.sin((float) this.age / this.lifetime * (float) Math.PI);
        if (this.age > this.lifetime) {
            remove();
        }
    }

    @Override
    public ParticleRenderType getGroup() {
        return FACES;
    }

    private static ModelPart model() {
        if (model == null) {
            model = LOTRMarshWraithModel.createBodyLayer().bakeRoot();
        }
        return model;
    }

    private record FaceState(Vec3 pos, float yaw, float pitch, int color, int light) {
    }

    private static final class Group extends ParticleGroup<LOTRDeadMarshFaceParticle> {

        Group(ParticleEngine engine) {
            super(engine);
        }

        @Override
        public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTick) {
            List<FaceState> faces = new ArrayList<>();
            for (LOTRDeadMarshFaceParticle face : this.particles) {
                Vec3 pos = new Vec3(Mth.lerp(partialTick, face.xo, face.x), Mth.lerp(partialTick, face.yo, face.y), Mth.lerp(partialTick, face.zo, face.z));
                float alpha = Mth.clamp(Mth.lerp(partialTick, face.prevFaceAlpha, face.faceAlpha), 0.0f, 1.0f);
                int light = LightCoordsUtil.getLightCoords(face.level, BlockPos.containing(pos));
                faces.add(new FaceState(pos, face.faceYaw, face.facePitch, ARGB.white(alpha), light));
            }
            return new State(faces);
        }
    }

    private record State(List<FaceState> faces) implements ParticleGroupRenderState {
        @Override
        public void submit(SubmitNodeCollector collector, CameraRenderState camera) {
            for (FaceState face : this.faces) {
                PoseStack poseStack = new PoseStack();
                poseStack.translate(face.pos().x - camera.pos.x, face.pos().y - camera.pos.y, face.pos().z - camera.pos.z);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(face.yaw()));
                poseStack.mulPose(Axis.YP.rotationDegrees(face.pitch()));
                collector.submitModelPart(model(), poseStack, RenderTypes.entityTranslucent(SKIN), face.light(), OverlayTexture.NO_OVERLAY, null,
                        face.color(), null);
            }
        }
    }
}
