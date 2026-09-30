package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc.LOTRHuornRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

import org.joml.Vector3f;

/**
 * LOTRModelHuorn: a tree six logs high under a small crown of leaves, the
 * same crown for every huorn (its corners thinned by a Random seeded with 100),
 * and a face -- a block a half-pixel proud, on a 64x32 sheet -- that shows on
 * the second log only while the huorn is awake.
 *
 * <p>The logs and leaves were model boxes drawn in the block's own six
 * textures, stitched together at run time for the purpose; here they are the
 * blocks themselves ({@link #WOOD_BLOCKS}, {@link #LEAF_BLOCKS}, in model units
 * at each block's centre), drawn by the renderer. This model is the face.
 */
public class LOTRHuornModel extends EntityModel<LOTRHuornRenderState> {

    public static final List<Vector3f> WOOD_BLOCKS = new ArrayList<>();
    public static final List<Vector3f> LEAF_BLOCKS = new ArrayList<>();

    static {
        Random rand = new Random(100L);
        int baseX = 2;
        int baseY = 0;
        int baseZ = 2;
        int height = 6;
        int leafStart = 3;
        int leafRangeMin = 0;
        for (int j = baseY - leafStart + height; j <= baseY + height; ++j) {
            int j1 = j - (baseY + height);
            int leafRange = leafRangeMin + 1 - j1 / 2;
            for (int i = baseX - leafRange; i <= baseX + leafRange; ++i) {
                int i1 = i - baseX;
                for (int k = baseZ - leafRange; k <= baseZ + leafRange; ++k) {
                    int k1 = k - baseZ;
                    if (Math.abs(i1) == leafRange && Math.abs(k1) == leafRange && (rand.nextInt(2) == 0 || j1 == 0)) {
                        continue;
                    }
                    // setRotationPoint(i * 16, 16 - j * 16, k * 16), then the
                    // whole moved back by (baseX, baseY, baseZ) blocks.
                    LEAF_BLOCKS.add(new Vector3f((i - baseX) * 16.0f, 16.0f - j * 16.0f + baseY * 16.0f, (k - baseZ) * 16.0f));
                }
            }
        }
        for (int j = 0; j < height; ++j) {
            WOOD_BLOCKS.add(new Vector3f(0.0f, 16.0f - j * 16.0f + baseY * 16.0f, 0.0f));
        }
    }

    private final ModelPart face;

    public LOTRHuornModel(ModelPart root) {
        super(root);
        this.face = root.getChild("face");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("face", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16, new CubeDeformation(0.5f)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LOTRHuornRenderState state) {
        super.setupAnim(state);
        this.face.visible = state.active;
    }
}
