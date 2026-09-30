package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRLionModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRLionOldModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRLionBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRLionessEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderLion: shadow 0.5, the lion's or the lioness's texture -- or, for
 * one named "ticket lion", the mod's first lion model and texture.
 */
public class LOTRLionRenderer extends MobRenderer<LOTRLionBaseEntity, LOTRLionRenderState, EntityModel<LivingEntityRenderState>> {

    public static final Identifier TEXTURE_LION = texture("lion");
    public static final Identifier TEXTURE_LIONESS = texture("lioness");
    private static final Identifier TEXTURE_TICKET = texture("ticketlion");

    private final EntityModel<LivingEntityRenderState> adult;
    private final EntityModel<LivingEntityRenderState> baby;
    private final EntityModel<LivingEntityRenderState> ticketAdult;
    private final EntityModel<LivingEntityRenderState> ticketBaby;

    public LOTRLionRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRLionModel(LOTRLionModel.createBodyLayer().bakeRoot()), 0.5f);
        this.adult = this.model;
        this.baby = new LOTRLionModel(LOTRLionModel.createBabyLayer().bakeRoot());
        this.ticketAdult = new LOTRLionOldModel(LOTRLionOldModel.createBodyLayer().bakeRoot());
        this.ticketBaby = new LOTRLionOldModel(LOTRLionOldModel.createBabyLayer().bakeRoot());
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/lion/" + name + ".png");
    }

    /** isTicket. */
    public static boolean isTicket(LOTRLionBaseEntity lion) {
        return lion.hasCustomName() && "ticket lion".equalsIgnoreCase(lion.getCustomName().getString());
    }

    @Override
    public LOTRLionRenderState createRenderState() {
        return new LOTRLionRenderState();
    }

    @Override
    public void extractRenderState(LOTRLionBaseEntity lion, LOTRLionRenderState state, float partialTick) {
        super.extractRenderState(lion, state, partialTick);
        state.ticket = isTicket(lion);
        state.skin = state.ticket ? TEXTURE_TICKET : lion instanceof LOTRLionessEntity ? TEXTURE_LIONESS : TEXTURE_LION;
    }

    @Override
    public void submit(LOTRLionRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        this.model = state.ticket ? (state.isBaby ? this.ticketBaby : this.ticketAdult) : (state.isBaby ? this.baby : this.adult);
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    public Identifier getTextureLocation(LOTRLionRenderState state) {
        return state.skin;
    }
}
