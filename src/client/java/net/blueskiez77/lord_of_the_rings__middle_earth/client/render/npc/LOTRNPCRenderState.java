package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import java.util.List;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import org.jspecify.annotations.Nullable;

/** What LOTRModelBiped and LOTRRenderBiped read off an NPC. */
public class LOTRNPCRenderState extends HumanoidRenderState {
    public boolean isNPC = true;
    public boolean drunkard;
    public boolean holdingItem;
    /** ModelBiped.heldItemRight / heldItemLeft: 0 empty, 1 held, 3 raised (aiming, eating, a banner). */
    public int heldItemRight;
    public int heldItemLeft;
    /** ModelBiped.aimedBow: a bow or sling raised in a fight. */
    public boolean aimedBow;
    public boolean renderChest;
    public boolean renderHair = true;
    /** LOTREntityElf.getBowingAmount: 0 upright to 1 fully bowed. */
    public float bowAmount;
    public @Nullable Identifier skin;
    /** The cape on its back (LOTREntityNPC.npcCape, or one its renderer gives it). */
    public @Nullable Identifier cape;
    /** The NPC's speech, when it is saying something (LOTRSpeechClient). */
    public @Nullable SpeechLines speech;

    public record SpeechLines(FormattedCharSequence name, List<FormattedCharSequence> lines, float age) {
    }
}
