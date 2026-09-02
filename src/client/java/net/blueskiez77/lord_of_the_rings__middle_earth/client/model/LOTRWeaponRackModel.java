package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelWeaponRack, transcribed box for box.
 *
 * <p>Kept as an entity-style model rather than converted to a block model JSON:
 * the texture is a 64x32 entity sheet, and CubeListBuilder unwraps the box UVs
 * from the same {@code texOffs} numbers the original used. Hand-computing those
 * faces into JSON would be a transcription error waiting to happen -- the same
 * reasoning as the troll totem.
 *
 * <p>The original renders one of two arrangements from one mesh, switched by
 * {@code onWall}: a floor rack stands on legs with the holder up at head
 * height, while a wall rack lies flat against the wall with the legs hidden and
 * the holder pulled down. Rather than mutate a shared part every frame, the two
 * are two calls here -- {@link #renderFloor} and {@link #renderWall}.
 */
public class LOTRWeaponRackModel {

    // ModelBase's defaults, which LOTRModelWeaponRack never overrode. The sheet
    // is assets/lotr/item/weaponRack.png from the released mod -- NOT
    // assets/lotr/items/weaponRack.png, which is the 16x16 inventory icon.
    // Feeding the icon to this model unwrapped every box against the wrong
    // sheet size, which is what made the rack render as garbage.
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    private final ModelPart base;
    private final ModelPart stand;
    private final ModelPart holder;
    private final ModelPart holderUpperParts;

    public LOTRWeaponRackModel(ModelPart root) {
        this.base = root.getChild("base");
        this.stand = base.getChild("stand");
        this.holder = base.getChild("holder");
        this.holderUpperParts = holder.getChild("holder_upper_parts");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition base = root.addOrReplaceChild("base",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.0F, -2.0F, -3.0F, 14, 2, 6),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        base.addOrReplaceChild("stand",
                CubeListBuilder.create()
                        .texOffs(34, 0).addBox(-4.0F, -4.0F, -0.5F, 8, 1, 1)
                        .texOffs(52, 0).addBox(-6.0F, -6.0F, -1.0F, 2, 6, 2)
                        .texOffs(52, 0).mirror().addBox(4.0F, -6.0F, -1.0F, 2, 6, 2),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition holder = base.addOrReplaceChild("holder",
                CubeListBuilder.create()
                        .texOffs(0, 8).addBox(-7.0F, -1.0F, -2.0F, 14, 1, 4)
                        .texOffs(6, 13).addBox(-6.0F, -2.0F, -1.5F, 2, 1, 3)
                        .texOffs(6, 13).mirror().addBox(4.0F, -2.0F, -1.5F, 2, 1, 3)
                        // holder.mirror = false in the original: CubeListBuilder's
                        // mirror() latches until it is turned off, so the reset
                        // has to be explicit or this box comes out mirrored.
                        .mirror(false)
                        .texOffs(0, 13).addBox(-6.0F, -3.0F, 0.5F, 2, 1, 1)
                        .texOffs(0, 13).mirror().addBox(4.0F, -3.0F, 0.5F, 2, 1, 1),
                PartPose.offset(0.0F, -8.0F, 0.0F));

        holder.addOrReplaceChild("holder_upper_parts",
                CubeListBuilder.create()
                        .mirror(false)
                        .texOffs(0, 13).addBox(-6.0F, -3.0F, -1.5F, 2, 1, 1)
                        .texOffs(0, 13).mirror().addBox(4.0F, -3.0F, -1.5F, 2, 1, 1),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /** The standing form: legs, and the holder up at the top of them. */
    public void renderFloor(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        base.xRot = 0.0F;
        stand.visible = true;
        holder.setPos(0.0F, -8.0F, 0.0F);
        holderUpperParts.visible = true;
        base.render(poseStack, consumer, light, overlay);
    }

    /** The wall form: flat against the wall, no legs, holder pulled down. */
    public void renderWall(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        base.xRot = -((float) Math.PI / 2.0F);
        stand.visible = false;
        holder.setPos(0.0F, -2.0F, 0.0F);
        holderUpperParts.visible = false;
        base.render(poseStack, consumer, light, overlay);
    }
}
