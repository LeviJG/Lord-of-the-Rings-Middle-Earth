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
 * LOTRModelBanner, transcribed box for box.
 *
 * <p>Not vanilla's BannerModel: that one is a hanging cloth with a crossbar,
 * sized and unwrapped for the banner atlas and its pattern layers. A LOTR
 * banner is a different object -- a base, a full-height post with a short bar
 * near the top, and a flat two-sided cloth -- and its textures are cut for
 * these boxes, so vanilla's would unwrap them wrongly.
 *
 * <p>Two sheets, as the original bound: the stand and post come from
 * banner/stand.png, the cloth from the faction's own image. Both are declared
 * 64x64 here even though the faction images are 128x128, because that is what
 * LOTRModelBanner set -- the cloth's UVs work out to the top-left 32x64 pixels
 * of the larger sheet, which is exactly where the artwork sits.
 */
public class LOTRBannerModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 64;

    private final ModelPart stand;
    private final ModelPart post;
    private final ModelPart lowerPost;
    private final ModelPart bannerFront;
    private final ModelPart bannerBack;

    public LOTRBannerModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.post = root.getChild("post");
        this.lowerPost = root.getChild("lower_post");
        this.bannerFront = root.getChild("banner_front");
        this.bannerBack = root.getChild("banner_back");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("stand",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.0f, -2.0f, -6.0f, 12, 2, 12),
                PartPose.offset(0.0f, 24.0f, 0.0f));

        // The post, and the short crossbar the cloth hangs from.
        root.addOrReplaceChild("post",
                CubeListBuilder.create()
                        .texOffs(0, 14).addBox(-0.5f, -48.0f, -0.5f, 1, 47, 1)
                        .texOffs(4, 14).addBox(-8.0f, -43.0f, -1.5f, 16, 1, 3),
                PartPose.offset(0.0f, 24.0f, 0.0f));

        root.addOrReplaceChild("lower_post",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-0.5f, -1.0f, -0.5f, 1, 24, 1),
                PartPose.offset(0.0f, 24.0f, 0.0f));

        // A zero-thickness quad, drawn once forward and once turned right round,
        // so the design reads correctly from both sides.
        root.addOrReplaceChild("banner_front",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0f, 0.0f, -1.0f, 16, 32, 0),
                PartPose.offset(0.0f, -18.0f, 0.0f));

        root.addOrReplaceChild("banner_back",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0f, 0.0f, -1.0f, 16, 32, 0),
                PartPose.offsetAndRotation(0.0f, -18.0f, 0.0f, 0.0f, (float) Math.PI, 0.0f));

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /** renderStand plus renderPost: everything drawn in the stand texture. */
    public void renderPost(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
            boolean onWall) {
        // A wall banner has no base and no length of post below the cloth --
        // it is nailed up, not stood in the ground.
        if (!onWall) {
            this.stand.render(poseStack, consumer, light, overlay);
            this.lowerPost.render(poseStack, consumer, light, overlay);
        }
        this.post.render(poseStack, consumer, light, overlay);
    }

    /** renderBanner: the cloth, in the faction's texture. */
    public void renderCloth(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.bannerFront.render(poseStack, consumer, light, overlay);
        this.bannerBack.render(poseStack, consumer, light, overlay);
    }
}
