package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * LOTRModelMorgulHelmet, transcribed box for box.
 *
 * <p>Three things the standard armour biped has none of. The SHELL is twelve
 * deep rather than eight, so it comes down over the neck. The CREST is a flat
 * plate standing ten pixels above the crown. And there are EIGHT SPIKES, in
 * pairs off the front, back and both sides, each turned twenty degrees out of
 * true so the pair splays -- which is why each is a part of its own rather than
 * another cube on the head.
 *
 * <p>NOT transcribed: bipedHeadwear. LOTRModelMorgulHelmet is the one helmet in
 * the set that does not clear it, so 1.7.10 also drew ModelBiped's hat layer --
 * an 8x8x8 box at (32,0) inflated by f + 0.5. Every pixel of that region of
 * morgul_helmet.png is transparent, so it drew nothing; leaving it out costs
 * the model twelve invisible quads and changes no rendered pixel.
 */
public class LOTRMorgulHelmetModel {

    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    /** The f LOTRArmorModels passed: new LOTRModelMorgulHelmet(1.0f). */
    private static final float INFLATE = 1.0f;

    /** Every spike's rotation: twenty degrees, in one axis or another. */
    private static final float SPLAY = 0.3490658503988659f;

    private final ModelPart head;

    public LOTRMorgulHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation f = new CubeDeformation(INFLATE);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        // Twelve deep, not eight: it covers the neck.
                        .texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8, 12, 8, f)
                        // The plate over the crown.
                        .texOffs(0, 20).addBox(-3.5f, -18.0f, -3.5f, 7, 10, 1, f),
                PartPose.ZERO);

        // Front pair, splayed left and right off the brow.
        spike(head, "spike_front_left", -1.0f, -5.5f, -10.0f, 1, 1, 4, -SPLAY, SPLAY, 0.0f);
        spike(head, "spike_front_right", 0.0f, -5.5f, -10.0f, 1, 1, 4, -SPLAY, -SPLAY, 0.0f);
        // Right side, out over the ear.
        spike(head, "spike_right_upper", 6.0f, -5.5f, -1.0f, 4, 1, 1, 0.0f, SPLAY, -SPLAY);
        spike(head, "spike_right_lower", 6.0f, -5.5f, 0.0f, 4, 1, 1, 0.0f, -SPLAY, -SPLAY);
        // Back pair.
        spike(head, "spike_back_left", 0.0f, -5.5f, 6.0f, 1, 1, 4, SPLAY, SPLAY, 0.0f);
        spike(head, "spike_back_right", -1.0f, -5.5f, 6.0f, 1, 1, 4, SPLAY, -SPLAY, 0.0f);
        // And the left side.
        spike(head, "spike_left_upper", -10.0f, -5.5f, 0.0f, 4, 1, 1, 0.0f, SPLAY, SPLAY);
        spike(head, "spike_left_lower", -10.0f, -5.5f, -1.0f, 4, 1, 1, 0.0f, -SPLAY, SPLAY);

        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    /**
     * One spike. They share the texture region at (16,20) and take no inflation
     * -- the original passes none and folds nothing into their coordinates.
     */
    private static void spike(PartDefinition head, String name,
            float x, float y, float z, int width, int height, int depth,
            float xRot, float yRot, float zRot) {
        head.addOrReplaceChild(name,
                CubeListBuilder.create().texOffs(16, 20).addBox(x, y, z, width, height, depth),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, xRot, yRot, zRot));
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.head.render(poseStack, consumer, light, overlay);
    }
}
