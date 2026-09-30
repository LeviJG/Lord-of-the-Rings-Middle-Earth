package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/** What LOTRModelEnt and LOTRRenderEnt read off an Ent. */
public class LOTREntRenderState extends LivingEntityRenderState {
    public @Nullable Identifier skin;
    public boolean healing;
    public boolean eyesClosed;
    public boolean hurt;
    public int extraBranches;
    /** ModelBase.onGround: the swing's progress. */
    public float attackTime;
    public LOTRNPCRenderState.@Nullable SpeechLines speech;
    /** The Mallorn Ent's: how far below ground it still is as it rises. */
    public float spawningOffset;
    /** The Mallorn Ent's weapon shield, up and not failing in fire. */
    public boolean weaponShield;
}
