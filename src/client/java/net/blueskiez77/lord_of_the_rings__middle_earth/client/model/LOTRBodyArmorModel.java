package net.blueskiez77.lord_of_the_rings__middle_earth.client.model;

import net.minecraft.client.model.geom.ModelPart;

/**
 * A custom chestplate: the three parts LOTRArmorRenderers draws inside the
 * wearer's body, right arm and left arm transforms. Every part is posed at zero
 * in the model -- the wearer's parts supply the rotation points and the pose.
 */
public interface LOTRBodyArmorModel {

    ModelPart body();

    ModelPart rightArm();

    ModelPart leftArm();
}
