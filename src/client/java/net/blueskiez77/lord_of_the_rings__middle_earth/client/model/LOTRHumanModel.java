package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRNPCRenderState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelHuman: a biped with a woman's chest and a tall hood of hair (the
 * 64x64 skin's lower half, 8x16x8 over the head).
 *
 * <p>Only a child was drawn differently, and no human NPC ported so far has
 * children.
 */
public class LOTRHumanModel<S extends LOTRNPCRenderState> extends LOTRBipedModel<S> {

    private final ModelPart chest;

    public LOTRHumanModel(ModelPart root) {
        super(root);
        this.chest = this.body.getChild("chest");
    }

    /** LOTRModelHuman(f, false). */
    public static MeshDefinition createMesh(CubeDeformation f) {
        MeshDefinition mesh = HumanoidModel.createMesh(f, 0.0f);
        PartDefinition root = mesh.getRoot();
        root.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32)
                .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 16.0f, 8.0f, f.extend(0.5f)), PartPose.ZERO);
        root.getChild("body").addOrReplaceChild("chest", CubeListBuilder.create().texOffs(24, 0)
                .addBox(-3.0f, 2.0f, -4.0f, 6.0f, 3.0f, 2.0f, f), PartPose.ZERO);
        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(CubeDeformation.NONE), 64, 64);
    }

    /** The outfit passes (headwear, a trader's outfit): LOTRModelHuman(0.6, false). */
    public static LayerDefinition createOutfitLayer() {
        return LayerDefinition.create(createMesh(new CubeDeformation(0.6f)), 64, 64);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        this.chest.visible = state.renderChest;
    }
}
