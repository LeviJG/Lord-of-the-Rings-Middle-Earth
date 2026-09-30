package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import java.util.Locale;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTrollLivingModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRMountainTrollChieftainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderTroll: a troll at its own scale, in one of its kind's skins, in a
 * shirt and trousers (one of three outfits for a troll, its kind's own
 * armour for the Olog-hai and the Mirk-trolls) and with its weapon in hand --
 * a wooden club, an Olog-hai's warhammer or a Mirk-troll's battleaxe -- and
 * anything it is saying over its head. A sneezing troll shakes. A troll
 * named Shrek, or any troll on April Fools' Day, is green; one named Drek
 * blue.
 *
 * LOTRRenderMountainTroll: a mountain troll with a spiked club, but for the
 * rock it raises while throwing -- a stone block in its right hand, at its
 * own size rather than the troll's, except in mid-swing. LOTRRenderSnowTroll:
 * a snow troll with a spiked club while not throwing, its shirt and trousers
 * drawn in its own skin (bindTrollOutfitTexture bound nothing, so the skin
 * stayed bound).
 *
 * <p>NOT ported yet: the hired unit's icon and health bar (with the hire
 * screens).
 */
public class LOTRTrollRenderer extends MobRenderer<LOTRTrollEntity, LOTRTrollRenderState, LOTRTrollLivingModel> {

    /** Which of the original's troll renderers this is. */
    public enum Kind {
        TROLL("lotr:mob/troll/troll", "troll/troll", null, LOTRTrollLivingModel.Piece.WOODEN_CLUB),
        OLOG_HAI("lotr:mob/troll/ologHai", "troll/olog_hai", "olog_hai_armor", LOTRTrollLivingModel.Piece.WARHAMMER),
        MIRK_TROLL("lotr:mob/troll/mirkTroll", "troll/mirk_troll", "mirk_troll_armor", LOTRTrollLivingModel.Piece.BATTLEAXE),
        MOUNTAIN_TROLL("lotr:mob/troll/mountainTroll", "troll/mountain_troll", null,
                LOTRTrollLivingModel.Piece.WOODEN_CLUB_SPIKED),
        MOUNTAIN_TROLL_CHIEFTAIN("lotr:mob/troll/mountainTroll", "troll/mountain_troll", null,
                LOTRTrollLivingModel.Piece.WOODEN_CLUB_SPIKED),
        SNOW_TROLL("lotr:mob/troll/snowTroll", "troll/snow_troll", null, LOTRTrollLivingModel.Piece.WOODEN_CLUB_SPIKED);

        private final LOTRRandomSkins skins;
        private final @Nullable LOTRRandomSkins armorSkins;
        private final LOTRTrollLivingModel.Piece weapon;

        Kind(String oldSkins, String skins, @Nullable String armor, LOTRTrollLivingModel.Piece weapon) {
            this.skins = LOTRRandomSkins.loadSkinsList(oldSkins, skins);
            this.armorSkins = armor == null ? null
                    : LOTRRandomSkins.loadSkinsList(oldSkins + "_armor", "troll/" + armor);
            this.weapon = weapon;
        }

        private boolean throwsRocks() {
            return this == MOUNTAIN_TROLL || this == MOUNTAIN_TROLL_CHIEFTAIN;
        }
    }

    private static final ItemStack HELD_ROCK = new ItemStack(Items.STONE);

    private static final Identifier[] OUTFITS = {texture("outfit_0"), texture("outfit_1"), texture("outfit_2")};
    private static final Identifier WEAPONS = texture("weapons");
    private static final Identifier CHIEFTAIN_ARMOR = texture("mountain_troll_chieftain_armor");

    private final Kind kind;
    private final ItemModelResolver items;

    public static EntityRendererProvider<LOTRTrollEntity> of(Kind kind) {
        return context -> new LOTRTrollRenderer(context, kind);
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/troll/" + name + ".png");
    }

    private LOTRTrollRenderer(EntityRendererProvider.Context context, Kind kind) {
        super(context, new LOTRTrollLivingModel(LOTRTrollLivingModel.createLayer(0.0f).bakeRoot(),
                LOTRTrollLivingModel.Piece.ALL), 0.5f);
        this.kind = kind;
        this.items = context.getItemModelResolver();
        LOTRTrollLivingModel shirt = new LOTRTrollLivingModel(LOTRTrollLivingModel.createLayer(1.0f).bakeRoot(),
                LOTRTrollLivingModel.Piece.SHIRT);
        LOTRTrollLivingModel trousers = new LOTRTrollLivingModel(LOTRTrollLivingModel.createLayer(0.75f).bakeRoot(),
                LOTRTrollLivingModel.Piece.TROUSERS);
        LOTRTrollLivingModel weapon = new LOTRTrollLivingModel(LOTRTrollLivingModel.createLayer(0.0f).bakeRoot(),
                kind.weapon);
        // shouldRenderPass 0 and 1: the shirt, then the trousers, in the outfit.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTRTrollRenderState state,
                               float yRot, float xRot) {
                if (state.outfit != null) {
                    coloredCutoutModelCopyLayerRender(shirt, state.outfit, poseStack, collector, light, state, state.tint, 1);
                    coloredCutoutModelCopyLayerRender(trousers, state.outfit, poseStack, collector, light, state, state.tint, 2);
                }
            }
        });
        if (kind == Kind.MOUNTAIN_TROLL_CHIEFTAIN) {
            // LOTRRenderMountainTrollChieftain passes 2 and 3: the helmet with
            // two coats of armour left, the chestplate with one.
            LOTRTrollLivingModel helmet = new LOTRTrollLivingModel(LOTRTrollLivingModel.createLayer(1.5f).bakeRoot(),
                    LOTRTrollLivingModel.Piece.HELMET);
            LOTRTrollLivingModel chestplate = new LOTRTrollLivingModel(LOTRTrollLivingModel.createLayer(1.5f).bakeRoot(),
                    LOTRTrollLivingModel.Piece.CHESTPLATE);
            addLayer(new RenderLayer<>(this) {
                @Override
                public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                                   LOTRTrollRenderState state, float yRot, float xRot) {
                    if (state.armorLevel >= 2) {
                        coloredCutoutModelCopyLayerRender(helmet, CHIEFTAIN_ARMOR, poseStack, collector, light, state, -1, 3);
                    }
                    if (state.armorLevel >= 1) {
                        coloredCutoutModelCopyLayerRender(chestplate, CHIEFTAIN_ARMOR, poseStack, collector, light, state, -1, 4);
                    }
                }
            });
        }
        // renderEquippedItems: the weapon, in white, in the weapon sheet --
        // or, for a mountain troll throwing, its rock.
        LOTRTrollLivingModel main = getModel();
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, LOTRTrollRenderState state,
                               float yRot, float xRot) {
                if (!state.throwing) {
                    coloredCutoutModelCopyLayerRender(weapon, WEAPONS, poseStack, collector, light, state, -1, 3);
                } else if (kind.throwsRocks() && state.attackTime <= 0.0f) {
                    poseStack.pushPose();
                    main.setupAnim(state);
                    main.translateToRightArm(poseStack);
                    poseStack.translate(0.375f, 1.5f, 0.0f);
                    poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
                    float unscale = 1.0f / state.trollScale;
                    poseStack.scale(unscale, unscale, unscale);
                    state.heldRock.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
                    poseStack.popPose();
                }
            }
        });
    }

    @Override
    public LOTRTrollRenderState createRenderState() {
        return new LOTRTrollRenderState();
    }

    @Override
    public void extractRenderState(LOTRTrollEntity troll, LOTRTrollRenderState state, float partialTick) {
        super.extractRenderState(troll, state, partialTick);
        state.skin = this.kind.skins.getRandomSkin(troll.getUUID());
        if (this.kind.armorSkins != null) {
            state.outfit = this.kind.armorSkins.getRandomSkin(troll.getUUID());
        } else if (this.kind == Kind.SNOW_TROLL) {
            state.outfit = state.skin;
        } else {
            int outfit = troll.getTrollOutfit();
            state.outfit = OUTFITS[outfit < 0 || outfit >= OUTFITS.length ? 0 : outfit];
        }
        state.twoHeads = troll.hasTwoHeads();
        state.headHurt = troll.shouldRenderHeadHurt();
        state.sniff = troll.sniffTime > 0 ? troll.sniffTime - partialTick : 0.0f;
        state.sneezing = troll.getSneezingTime() > 0;
        state.throwing = troll.isThrowing();
        state.attackTime = troll.getAttackAnim(partialTick);
        state.trollScale = troll.getTrollScale();
        String name = troll.familyInfo.getName() == null ? "" : troll.familyInfo.getName().toLowerCase(Locale.ROOT);
        if (LOTRMod.isAprilFools() || "shrek".equals(name)) {
            state.tint = 0xFF00FF00;
        } else if ("drek".equals(name)) {
            state.tint = 0xFF3366FF;
        } else {
            state.tint = -1;
        }
        if (troll instanceof LOTRMountainTrollChieftainEntity chieftain) {
            state.armorLevel = chieftain.getTrollArmorLevel();
            state.spawningOffset = chieftain.getSpawningOffset(partialTick);
        }
        state.speech = LOTRNPCSpeechRendering.extract(troll, getFont());
        if (this.kind.throwsRocks() && state.throwing) {
            this.items.updateForNonLiving(state.heldRock, HELD_ROCK, ItemDisplayContext.NONE, troll);
        }
    }

    @Override
    public Identifier getTextureLocation(LOTRTrollRenderState state) {
        return state.skin;
    }

    @Override
    protected int getModelTint(LOTRTrollRenderState state) {
        return state.tint;
    }

    /** preRenderCallback: the troll's own scale. */
    @Override
    protected void scale(LOTRTrollRenderState state, PoseStack poseStack) {
        poseStack.scale(state.trollScale, state.trollScale, state.trollScale);
        // LOTRRenderMountainTrollChieftain: still sunk in the ground as it
        // rises (the original's translate, after its scale, in its flipped y).
        poseStack.translate(0.0f, state.spawningOffset, 0.0f);
    }

    /** rotateCorpse: a sneezing troll shakes from side to side. */
    @Override
    protected void setupRotations(LOTRTrollRenderState state, PoseStack poseStack, float bodyRot, float scale) {
        if (state.sneezing) {
            bodyRot += (float) (Math.cos(Mth.floor(state.ageInTicks) * 3.25) * Math.PI);
        }
        super.setupRotations(state, poseStack, bodyRot, scale);
    }

    @Override
    public void submit(LOTRTrollRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (state.speech != null && state.distanceToCameraSq <= LOTRNPCSpeechRendering.NAME_TAG_RANGE * LOTRNPCSpeechRendering.NAME_TAG_RANGE) {
            LOTRNPCSpeechRendering.submit(getFont(), state.boundingBoxHeight, state.speech, poseStack, collector, camera);
        }
    }
}
