package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBoarModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRCamelModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRElkModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGiraffeModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRLegacyHorseModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRRhinoModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCamelEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRElkEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRGiraffeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRHorseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRRhinoEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRShirePonyEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRWildBoarEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRZebraEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.equine.Markings;
import net.minecraft.world.entity.animal.equine.Variant;

/**
 * One factory per LOTR mount: LOTRRenderHorse, LOTRRenderShirePony,
 * LOTRRenderZebra, LOTRRenderElk, LOTRRenderCamel, LOTRRenderGiraffe,
 * LOTRRenderWildBoar and LOTRRenderRhino.
 *
 * <p>Left out (user): LOTRRenderHorse drawing every horse as a donkey on the
 * first of April.
 */
public final class LOTRMountRenderers {

    private LOTRMountRenderers() {
    }

    private static Identifier tex(String path) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/" + path + ".png");
    }

    /** RenderHorse's layered coat: 1.7.10's sheet for the coat, and its markings over it. */
    private static Identifier coat(Variant variant) {
        String name = switch (variant) {
            case WHITE -> "white";
            case CREAMY -> "creamy";
            case CHESTNUT -> "chestnut";
            case BROWN -> "brown";
            case BLACK -> "black";
            case GRAY -> "gray";
            case DARK_BROWN -> "darkbrown";
        };
        return tex("legacy_horse/horse_" + name);
    }

    private static Identifier markings(Markings markings) {
        return switch (markings) {
            case NONE -> null;
            case WHITE -> tex("legacy_horse/horse_markings_white");
            case WHITE_FIELD -> tex("legacy_horse/horse_markings_whitefield");
            case WHITE_DOTS -> tex("legacy_horse/horse_markings_whitedots");
            case BLACK_DOTS -> tex("legacy_horse/horse_markings_blackdots");
        };
    }

    private static LOTRLegacyHorseModel legacyHorse() {
        return new LOTRLegacyHorseModel(LOTRLegacyHorseModel.createBodyLayer().bakeRoot());
    }

    /** LOTRRenderHorse (shadow 0.75), and LOTRRenderShirePony at PONY_SCALE. */
    public static LOTRMountRenderer<LOTRHorseEntity, LOTRLegacyHorseModel> horse(EntityRendererProvider.Context context) {
        return horseRenderer(context, 1.0f);
    }

    public static LOTRMountRenderer<LOTRHorseEntity, LOTRLegacyHorseModel> shirePony(EntityRendererProvider.Context context) {
        return horseRenderer(context, LOTRShirePonyEntity.PONY_SCALE);
    }

    private static LOTRMountRenderer<LOTRHorseEntity, LOTRLegacyHorseModel> horseRenderer(
            EntityRendererProvider.Context context, float scale) {
        LOTRLegacyHorseModel model = legacyHorse();
        return LOTRMountRenderer.<LOTRHorseEntity, LOTRLegacyHorseModel>create(context, model, model, 0.75f, scale,
                (horse, state) -> {
                    state.markings = markings(horse.getMarkings());
                    return coat(horse.getVariant());
                }).withMarkings();
    }

    /** LOTRRenderZebra: the horse model in the zebra's own sheet. */
    public static LOTRMountRenderer<LOTRZebraEntity, LOTRLegacyHorseModel> zebra(EntityRendererProvider.Context context) {
        LOTRLegacyHorseModel model = legacyHorse();
        Identifier texture = tex("zebra");
        return LOTRMountRenderer.create(context, model, model, 0.75f, 1.0f, (zebra, state) -> texture);
    }

    /** LOTRRenderElk: shadow 0.5, random skins, the saddle pass, and the Christmas nose. */
    public static LOTRMountRenderer<LOTRElkEntity, LOTRElkModel> elk(EntityRendererProvider.Context context) {
        LOTRRandomSkins skins = LOTRRandomSkins.loadSkinsList("lotr:mob/elk/elk", "elk/elk");
        LOTRElkModel noseOnly = new LOTRElkModel(LOTRElkModel.createBodyLayer(0.0f).bakeRoot(), true);
        LOTRElkModel noseOnlyBaby = new LOTRElkModel(LOTRElkModel.createBabyLayer(0.0f).bakeRoot(), true);
        LOTRMountRenderer<LOTRElkEntity, LOTRElkModel> renderer = LOTRMountRenderer.create(context,
                new LOTRElkModel(LOTRElkModel.createBodyLayer(0.0f).bakeRoot()),
                new LOTRElkModel(LOTRElkModel.createBabyLayer(0.0f).bakeRoot()),
                0.5f, 1.0f, (elk, state) -> skins.getRandomSkin(elk.getUUID()));
        renderer.withSaddle(new LOTRElkModel(LOTRElkModel.createBodyLayer(0.5f).bakeRoot()),
                new LOTRElkModel(LOTRElkModel.createBabyLayer(0.5f).bakeRoot()), tex("elk/saddle"));
        return renderer.withLayer(new RenderLayer<>(renderer) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRMountRenderState state, float yRot, float xRot) {
                if (state.christmas) {
                    coloredCutoutModelCopyLayerRender(state.isBaby ? noseOnlyBaby : noseOnly, state.skin,
                            poseStack, collector, light, state, 0xFFFF0000, 1);
                }
            }
        });
    }

    /** LOTRRenderCamel: shadow 0.5, drawn at 1.25, the saddle and the carpet. */
    public static LOTRMountRenderer<LOTRCamelEntity, LOTRCamelModel> camel(EntityRendererProvider.Context context) {
        Identifier texture = tex("camel/camel");
        return LOTRMountRenderer.<LOTRCamelEntity, LOTRCamelModel>create(context,
                        new LOTRCamelModel(LOTRCamelModel.createBodyLayer(0.0f).bakeRoot()),
                        new LOTRCamelModel(LOTRCamelModel.createBabyLayer(0.0f).bakeRoot()),
                        0.5f, 1.25f, (camel, state) -> texture)
                .withSaddle(new LOTRCamelModel(LOTRCamelModel.createBodyLayer(0.5f).bakeRoot()),
                        new LOTRCamelModel(LOTRCamelModel.createBabyLayer(0.5f).bakeRoot()), tex("camel/saddle"))
                .withCarpet(new LOTRCamelModel(LOTRCamelModel.createBodyLayer(0.55f).bakeRoot()),
                        new LOTRCamelModel(LOTRCamelModel.createBabyLayer(0.55f).bakeRoot()));
    }

    /** LOTRRenderGiraffe: shadow 0.5 and the saddle; its texture never showed barding. */
    public static LOTRMountRenderer<LOTRGiraffeEntity, LOTRGiraffeModel> giraffe(EntityRendererProvider.Context context) {
        Identifier texture = tex("giraffe/giraffe");
        return LOTRMountRenderer.<LOTRGiraffeEntity, LOTRGiraffeModel>create(context,
                        new LOTRGiraffeModel(LOTRGiraffeModel.createBodyLayer(0.0f).bakeRoot()),
                        new LOTRGiraffeModel(LOTRGiraffeModel.createBabyLayer(0.0f).bakeRoot()),
                        0.5f, 1.0f, (giraffe, state) -> {
                            state.armor = null;
                            return texture;
                        })
                .withSaddle(new LOTRGiraffeModel(LOTRGiraffeModel.createBodyLayer(0.5f).bakeRoot()),
                        new LOTRGiraffeModel(LOTRGiraffeModel.createBabyLayer(0.5f).bakeRoot()), tex("giraffe/saddle"));
    }

    /** LOTRRenderWildBoar: shadow 0.7 and the saddle. */
    public static LOTRMountRenderer<LOTRWildBoarEntity, LOTRBoarModel> wildBoar(EntityRendererProvider.Context context) {
        Identifier texture = tex("boar/boar");
        return LOTRMountRenderer.<LOTRWildBoarEntity, LOTRBoarModel>create(context,
                        new LOTRBoarModel(LOTRBoarModel.createBodyLayer(0.0f).bakeRoot()),
                        new LOTRBoarModel(LOTRBoarModel.createBabyLayer(0.0f).bakeRoot()),
                        0.7f, 1.0f, (boar, state) -> texture)
                .withSaddle(new LOTRBoarModel(LOTRBoarModel.createBodyLayer(0.5f).bakeRoot()),
                        new LOTRBoarModel(LOTRBoarModel.createBabyLayer(0.5f).bakeRoot()), tex("boar/saddle"));
    }

    /** LOTRRenderRhino: shadow 0.5 and the saddle. */
    public static LOTRMountRenderer<LOTRRhinoEntity, LOTRRhinoModel> rhino(EntityRendererProvider.Context context) {
        Identifier texture = tex("rhino/rhino");
        return LOTRMountRenderer.<LOTRRhinoEntity, LOTRRhinoModel>create(context,
                        new LOTRRhinoModel(LOTRRhinoModel.createBodyLayer(0.0f).bakeRoot()),
                        new LOTRRhinoModel(LOTRRhinoModel.createBabyLayer(0.0f).bakeRoot()),
                        0.5f, 1.0f, (rhino, state) -> texture)
                .withSaddle(new LOTRRhinoModel(LOTRRhinoModel.createBodyLayer(0.5f).bakeRoot()),
                        new LOTRRhinoModel(LOTRRhinoModel.createBabyLayer(0.5f).bakeRoot()), tex("rhino/saddle"));
    }
}
