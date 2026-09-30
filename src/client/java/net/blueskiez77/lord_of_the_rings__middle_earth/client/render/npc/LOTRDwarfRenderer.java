package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRDwarfModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRRandomSkins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRBlueDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderDwarf: thirteen sixteenths of a man's size (and upside down on
 * April Fools' Day), in one of the skins for its people and sex, with a
 * married dwarf's ring drawn over it. The commanders and the merchants
 * (LOTRRenderDwarfCommander) wear their people's cloak over body and arms;
 * the smiths (LOTRRenderDwarfSmith) a blacksmith's apron when they have no
 * chestplate on; and the wicked dwarves (LOTRRenderWickedDwarf) their own
 * skins and a darker apron.
 */
public class LOTRDwarfRenderer
        extends LOTRBipedRenderer<LOTRDwarfEntity, LOTRDwarfRenderer.State, LOTRDwarfModel<LOTRDwarfRenderer.State>> {

    private static final LOTRRandomSkins SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/dwarf_male", "dwarf/dwarf_male");
    private static final LOTRRandomSkins SKINS_FEMALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/dwarf_female", "dwarf/dwarf_female");
    private static final LOTRRandomSkins BLUE_SKINS_MALE =
            LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/blueMountains_male", "dwarf/blue_mountains_male");
    private static final LOTRRandomSkins BLUE_SKINS_FEMALE =
            LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/blueMountains_female", "dwarf/blue_mountains_female");
    private static final LOTRRandomSkins WICKED_SKINS_MALE = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/wicked_male", "dwarf/wicked_male");
    private static final Identifier RING_TEXTURE = texture("ring");
    private static final Identifier CLOAK_TEXTURE = texture("commander_cloak");
    private static final Identifier BLUE_CLOAK_TEXTURE = texture("blue_mountains_commander_cloak");
    private static final Identifier SMITH_APRON_TEXTURE = texture("blacksmith_apron");
    private static final Identifier WICKED_APRON_TEXTURE = texture("wicked_apron");

    /** Which of the original's dwarf renderers this is. */
    public enum Kind {
        DWARF, COMMANDER, SMITH, WICKED
    }

    public static class State extends LOTRNPCRenderState {
        public boolean wearingRing;
        public boolean chestEmpty;
        public boolean blueMountains;
    }

    private final Kind kind;

    public LOTRDwarfRenderer(EntityRendererProvider.Context context) {
        this(context, Kind.DWARF);
    }

    public static EntityRendererProvider<LOTRDwarfEntity> of(Kind kind) {
        return context -> new LOTRDwarfRenderer(context, kind);
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/dwarf/" + name + ".png");
    }

    private LOTRDwarfRenderer(EntityRendererProvider.Context context, Kind kind) {
        this(context, new LOTRDwarfModel<>(LOTRDwarfModel.createBodyLayer().bakeRoot()), kind);
    }

    private LOTRDwarfRenderer(EntityRendererProvider.Context context, LOTRDwarfModel<State> model, Kind kind) {
        super(context, model, model, 0.5f);
        this.kind = kind;
        addLayer(new HumanoidArmorLayer<State, LOTRDwarfModel<State>, HumanoidModel<State>>(this,
                LOTRDwarfModel.createArmorLayers().map(layer -> (HumanoidModel<State>) new LOTRDwarfModel<State>(layer.bakeRoot())),
                context.getEquipmentRenderer()));
        LOTRDwarfModel<State> outfit = new LOTRDwarfModel<>(LOTRDwarfModel.createOutfitLayer().bakeRoot());
        LOTRDwarfModel<State> ringModel = new LOTRDwarfModel<>(LOTRDwarfModel.createOutfitLayer().bakeRoot()) {
            @Override
            public void setupAnim(State state) {
                super.setupAnim(state);
                this.rightArm.visible = false;
            }
        };
        LOTRDwarfModel<State> cloakModel = new LOTRDwarfModel<>(LOTRDwarfModel.createCloakLayer().bakeRoot()) {
            @Override
            public void setupAnim(State state) {
                super.setupAnim(state);
                this.head.visible = false;
                this.hat.visible = false;
                this.rightLeg.visible = false;
                this.leftLeg.visible = false;
            }
        };
        if (kind == Kind.COMMANDER) {
            // shouldRenderPass 0: the cloak, in the helmet's pass.
            addLayer(new RenderLayer<>(this) {
                @Override
                public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                                   float yRot, float xRot) {
                    coloredCutoutModelCopyLayerRender(cloakModel, state.blueMountains ? BLUE_CLOAK_TEXTURE : CLOAK_TEXTURE,
                            poseStack, collector, light, state, -1, 1);
                }
            });
        }
        // shouldRenderPass 1: a smith's apron with no chestplate on, else the ring.
        Identifier apron = kind == Kind.SMITH ? SMITH_APRON_TEXTURE : kind == Kind.WICKED ? WICKED_APRON_TEXTURE : null;
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (apron != null && state.chestEmpty) {
                    coloredCutoutModelCopyLayerRender(outfit, apron, poseStack, collector, light, state, -1, 1);
                } else if (state.wearingRing) {
                    coloredCutoutModelCopyLayerRender(ringModel, RING_TEXTURE, poseStack, collector, light, state, -1, 1);
                }
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRDwarfEntity dwarf, State state, float partialTick) {
        super.extractRenderState(dwarf, state, partialTick);
        state.blueMountains = dwarf instanceof LOTRBlueDwarfEntity;
        LOTRRandomSkins skins;
        if (this.kind == Kind.WICKED) {
            skins = WICKED_SKINS_MALE;
        } else if (state.blueMountains) {
            skins = dwarf.familyInfo.isMale() ? BLUE_SKINS_MALE : BLUE_SKINS_FEMALE;
        } else {
            skins = dwarf.familyInfo.isMale() ? SKINS_MALE : SKINS_FEMALE;
        }
        state.skin = skins.getRandomSkin(dwarf.getUUID());
        state.chestEmpty = dwarf.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        state.wearingRing = dwarf.getClass() == dwarf.familyInfo.marriageEntityClass
                && dwarf.familyInfo.marriageRing != null
                && dwarf.getItemBySlot(EquipmentSlot.HEAD).is(dwarf.familyInfo.marriageRing);
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }

    @Override
    protected void scale(State state, PoseStack poseStack) {
        super.scale(state, poseStack);
        poseStack.scale(0.8125f, 0.8125f, 0.8125f);
        if (LOTRMod.isAprilFools()) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        }
    }
}
