package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/** What LOTRModelTroll and LOTRRenderTroll read off a living troll. */
public class LOTRTrollRenderState extends LivingEntityRenderState {
    public @Nullable Identifier skin;
    public @Nullable Identifier outfit;
    public boolean twoHeads;
    /** shouldRenderHeadHurt: hurt, or sneezing. */
    public boolean headHurt;
    /** sniffTime less the partial tick, while sniffing; else 0. */
    public float sniff;
    public boolean sneezing;
    /** A mountain or snow troll throwing. */
    public boolean throwing;
    /** ModelBase.onGround: the swing's progress. */
    public float attackTime;
    public float trollScale = 1.0f;
    /** The shrek and drek colouring (and April Fools'), else white. */
    public int tint = -1;
    /** A chieftain's coats of armour left (2, 1, 0). */
    public int armorLevel;
    /** LOTREntityMountainTrollChieftain.getSpawningOffset: how far below ground it still is. */
    public float spawningOffset;
    public LOTRNPCRenderState.@Nullable SpeechLines speech;
    /** A mountain troll's rock, raised to throw. */
    public final ItemStackRenderState heldRock = new ItemStackRenderState();
}
