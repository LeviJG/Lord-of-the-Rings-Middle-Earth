package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRElfModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRDorwinionElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRGaladhrimWardenEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRHighElfBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRWoodElfEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EquipmentSlot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderElf: an elf in one of its people's skins for its sex. A jazz elf
 * has its own skins, one in two with a jazz outfit if bare-headed; in a solo
 * it spins on the spot, its whole body cycling through the colours, and its
 * held item is put away. On the first of April every elf is a quarter of its
 * size.
 *
 * <p>LOTRRenderElvenTrader and LOTRRenderElvenSmith: a trader's or smith's
 * cloak ({@code elf/<outfit>.png}) always worn over it.
 *
 * <p>LOTRRenderGaladhrimWarden: while a warden is unseen it is drawn at a
 * twentieth of its opacity. The original drew its armour and held items that
 * faintly too; here they are left out while it is unseen.
 *
 * <p>NOT ported yet: the smith's cape (with NPC capes), and the saxophone
 * drawn in a jazz elf's hands (D16).
 */
public class LOTRElfRenderer
        extends LOTRBipedRenderer<LOTRElfEntity, LOTRElfRenderer.State, LOTRElfModel<LOTRElfRenderer.State>> {

    private static final LOTRRandomSkins GALADHRIM_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/galadhrim_male", "elf/galadhrim_male");
    private static final LOTRRandomSkins GALADHRIM_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/galadhrim_female", "elf/galadhrim_female");
    private static final LOTRRandomSkins HIGH_ELF_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/highElf_male", "elf/high_elf_male");
    private static final LOTRRandomSkins HIGH_ELF_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/highElf_female", "elf/high_elf_female");
    private static final LOTRRandomSkins WOOD_ELF_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/woodElf_male", "elf/wood_elf_male");
    private static final LOTRRandomSkins WOOD_ELF_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/woodElf_female", "elf/wood_elf_female");
    private static final LOTRRandomSkins DORWINION_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/dorwinion_male", "elf/dorwinion_male");
    private static final LOTRRandomSkins DORWINION_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/dorwinion_female", "elf/dorwinion_female");
    private static final LOTRRandomSkins JAZZ_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/jazz_male", "elf/jazz_male");
    private static final LOTRRandomSkins JAZZ_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/jazz_female", "elf/jazz_female");
    private static final LOTRRandomSkins JAZZ_OUTFITS = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/jazz_outfit", "elf/jazz_outfit");

    /** Opacity of an unseen warden, 0.05, over vanilla's own 0.15 for a see-through body. */
    private static final int UNSEEN_TINT = ARGB.color(85, 255, 255, 255);

    public static class State extends LOTRElfModel.ElfState {
        public @Nullable Identifier jazzOutfit;
        public int soloTint = -1;
        public float soloSpin;
        public boolean unseen;
    }

    private final @Nullable Identifier cloak;

    public LOTRElfRenderer(EntityRendererProvider.Context context) {
        this(context, null);
    }

    /** LOTRRenderElvenTrader / LOTRRenderElvenSmith: the cloak ({@code elf/<name>.png}), always worn. */
    public static EntityRendererProvider<LOTRElfEntity> cloaked(String cloak) {
        return context -> new LOTRElfRenderer(context,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/elf/" + cloak + ".png"));
    }

    private LOTRElfRenderer(EntityRendererProvider.Context context, @Nullable Identifier cloak) {
        this(context, new LOTRElfModel<>(LOTRElfModel.createBodyLayer().bakeRoot()), cloak);
    }

    private LOTRElfRenderer(EntityRendererProvider.Context context, LOTRElfModel<State> model, @Nullable Identifier cloak) {
        super(context, model, model, 0.5f);
        this.cloak = cloak;
        addLayer(new HumanoidArmorLayer<State, LOTRElfModel<State>, HumanoidModel<State>>(this,
                LOTRElfModel.createArmorLayers().map(layer -> (HumanoidModel<State>) new LOTRElfModel<State>(layer.bakeRoot())),
                context.getEquipmentRenderer()));
        LOTRElfModel<State> jazzOutfitModel = new LOTRElfModel<>(LOTRElfModel.createOutfitLayer().bakeRoot());
        LOTRElfModel<State> cloakModel = new LOTRElfModel<>(LOTRElfModel.createTraderOutfitLayer().bakeRoot());
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                // Pass 0: the cloak, or failing that a jazz outfit.
                if (LOTRElfRenderer.this.cloak != null) {
                    coloredCutoutModelCopyLayerRender(cloakModel, LOTRElfRenderer.this.cloak, poseStack, collector,
                            light, state, state.soloTint, 1);
                } else if (state.jazzOutfit != null) {
                    coloredCutoutModelCopyLayerRender(jazzOutfitModel, state.jazzOutfit, poseStack, collector,
                            light, state, state.soloTint, 1);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRElfEntity elf, State state, float partialTick) {
        super.extractRenderState(elf, state, partialTick);
        boolean male = elf.familyInfo.isMale();
        boolean jazz = elf.isJazz();
        LOTRRandomSkins skins;
        if (jazz) {
            skins = male ? JAZZ_MALE : JAZZ_FEMALE;
        } else if (elf instanceof LOTRDorwinionElfEntity) {
            skins = male ? DORWINION_MALE : DORWINION_FEMALE;
        } else if (elf instanceof LOTRHighElfBaseEntity) {
            skins = male ? HIGH_ELF_MALE : HIGH_ELF_FEMALE;
        } else if (elf instanceof LOTRWoodElfEntity) {
            skins = male ? WOOD_ELF_MALE : WOOD_ELF_FEMALE;
        } else {
            skins = male ? GALADHRIM_MALE : GALADHRIM_FEMALE;
        }
        state.skin = skins.getRandomSkin(elf.getUUID());
        state.jazzOutfit = jazz && elf.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && LOTRRandomSkins.nextInt(elf.getUUID(), 2) == 0 ? JAZZ_OUTFITS.getRandomSkin(elf.getUUID()) : null;
        state.jazzSolo = jazz && elf.isSolo();
        if (state.jazzSolo) {
            // getHeldItem: nothing in hand during a solo; the colour cycles once a second.
            state.rightHandItemState.clear();
            state.leftHandItemState.clear();
            float hue = (elf.tickCount + partialTick) / 20.0f;
            int rgb = Color.HSBtoRGB(hue % 360.0f, 0.5f, 1.0f);
            state.soloTint = ARGB.opaque(rgb);
            state.soloSpin = elf.getSoloSpin(partialTick);
        } else {
            state.soloTint = -1;
            state.soloSpin = 0.0f;
        }
        state.bowAmount = elf.getBowingAmount(partialTick);
        state.unseen = elf instanceof LOTRGaladhrimWardenEntity warden && warden.isElfSneaking();
        if (state.unseen) {
            state.isInvisibleToPlayer = false;
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected void scale(State state, PoseStack poseStack) {
        super.scale(state, poseStack);
        if (LOTRMod.isAprilFools()) {
            poseStack.scale(0.25f, 0.25f, 0.25f);
        }
        if (state.jazzSolo) {
            poseStack.mulPose(Axis.YP.rotationDegrees(state.soloSpin));
        }
    }

    @Override
    protected int getModelTint(State state) {
        if (state.unseen) {
            return UNSEEN_TINT;
        }
        return state.jazzSolo ? state.soloTint : super.getModelTint(state);
    }

    @Override
    protected boolean isBodyVisible(State state) {
        return !state.unseen && super.isBodyVisible(state);
    }

    @Override
    protected boolean shouldRenderLayers(State state) {
        return !state.unseen && super.shouldRenderLayers(state);
    }
}
