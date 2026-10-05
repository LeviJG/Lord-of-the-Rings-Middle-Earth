package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import org.jspecify.annotations.Nullable;

/**
 * Added to every EntityRenderState (LOTREntityRenderStateMixin): what to draw
 * above the entity's head, and the entity's id for renderers that need more
 * of it (the worn plate's food pile).
 */
public interface LOTROverheadHolder {

    LOTROverheadRendering.@Nullable Overhead lotr$getOverhead();

    void lotr$setOverhead(LOTROverheadRendering.@Nullable Overhead overhead);

    int lotr$getEntityId();

    void lotr$setEntityId(int id);
}
