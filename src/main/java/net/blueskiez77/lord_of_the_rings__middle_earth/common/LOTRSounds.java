package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

// 1.7.10 played sounds by name -- world.playSoundEffect(.., "lotr:block.gate.open", ..)
// resolved straight out of sounds.json with nothing registered in code. Sound
// events are a registry now, so the four names LOTRBlockGate.activateGate used
// are declared here. The ids are unchanged, and so are the ogg files behind
// them.
public final class LOTRSounds {
    public static final SoundEvent GATE_OPEN = register("block.gate.open");
    public static final SoundEvent GATE_CLOSE = register("block.gate.close");
    public static final SoundEvent GATE_STONE_OPEN = register("block.gate.stone_open");
    public static final SoundEvent GATE_STONE_CLOSE = register("block.gate.stone_close");

    /**
     * The Horn of Command's blow, "lotr:item.horn".
     *
     * <p>It MUST be here and not only in sounds.json. sounds.json is a client
     * resource that maps a name to its ogg; the sound_event REGISTRY is what a
     * datapack can point at, and data/lotr/instrument/command_horn.json points
     * at this one. Adding the sound to sounds.json alone left the instrument
     * unable to resolve it, which failed the whole registry load and stopped any
     * world from opening.
     */
    public static final SoundEvent ITEM_HORN = register("item.horn");

    /** The crack of the balrog whip, "lotr:item.balrog_whip". */
    public static final SoundEvent ITEM_BALROG_WHIP = register("item.balrog_whip");

    private LOTRSounds() {
    }

    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    // Touching the class runs the static initialisers; nothing else to do.
    public static void init() {
    }
}