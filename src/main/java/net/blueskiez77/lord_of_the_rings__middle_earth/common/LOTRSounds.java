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

    /** The Mace of Sauron coming down, and Gandalf's fireball bursting. */
    public static final SoundEvent ITEM_MACE_SAURON = register("item.mace_sauron");
    public static final SoundEvent ITEM_GANDALF_FIREBALL = register("item.gandalf_fireball");

    /** A vessel dipped in water, "lotr:item.mug_fill". */
    public static final SoundEvent ITEM_MUG_FILL = register("item.mug_fill");
    /** A draw on a smoking pipe, "lotr:item.puff". */
    public static final SoundEvent ITEM_PUFF = register("item.puff");
    /** A structure spawner building: "lotr:item.structureSpawner". */
    public static final SoundEvent ITEM_STRUCTURE_SPAWNER = register("item.structureSpawner");
    /** A blowgun dart leaving the pipe, "lotr:item.dart". */
    public static final SoundEvent ITEM_DART = register("item.dart");
    /** Pledging to a faction, "lotr:event.pledge". */
    public static final SoundEvent EVENT_PLEDGE = register("event.pledge");
    /** A trader or captain paid, "lotr:event.trade" (LOTREntityNPC.playTradeSound). */
    public static final SoundEvent EVENT_TRADE = register("event.trade");
    /** The warg's cries: "lotr:warg.attack", ".death", ".hurt" and ".say". */
    public static final SoundEvent WARG_ATTACK = register("warg.attack");
    public static final SoundEvent WARG_DEATH = register("warg.death");
    public static final SoundEvent WARG_HURT = register("warg.hurt");
    public static final SoundEvent WARG_SAY = register("warg.say");
    /** A male elf's war cry and idle speech, "lotr:elf.male.attack" and ".say". */
    public static final SoundEvent ELF_MALE_ATTACK = register("elf.male.attack");
    public static final SoundEvent ELF_MALE_SAY = register("elf.male.say");
    /** A Wood-elf scout vanishing to somewhere else, "lotr:elf.woodElf_teleport". */
    public static final SoundEvent ELF_WOOD_ELF_TELEPORT = register("elf.woodElf_teleport");
    /** A dwarf's war cry, pain, and cheer over a kill: "lotr:dwarf.attack", ".hurt" and ".kill". */
    public static final SoundEvent DWARF_ATTACK = register("dwarf.attack");
    public static final SoundEvent DWARF_HURT = register("dwarf.hurt");
    public static final SoundEvent DWARF_KILL = register("dwarf.kill");

    /** A wight's pain and death, and the barrows' voices, whispers and screams: "lotr:wight.*". */
    public static final SoundEvent WIGHT_AMBIENCE = register("wight.ambience");
    public static final SoundEvent WIGHT_DEATH = register("wight.death");
    public static final SoundEvent WIGHT_HURT = register("wight.hurt");

    /** A marsh wraith's cast, and a wraith rising: "lotr:wraith.marshWraith_shoot" and ".spawn". */
    public static final SoundEvent WRAITH_MARSH_WRAITH_SHOOT = register("wraith.marshWraith_shoot");
    public static final SoundEvent WRAITH_SPAWN = register("wraith.spawn");

    /** An Ent's heavy tread: "lotr:ent.step". */
    public static final SoundEvent ENT_STEP = register("ent.step");

    /** The Mallorn Ent's leaf bomb and its call to the trees: "lotr:ent.mallorn.leafAttack" and ".summonEnt". */
    public static final SoundEvent ENT_MALLORN_LEAF_ATTACK = register("ent.mallorn.leafAttack");
    public static final SoundEvent ENT_MALLORN_SUMMON_ENT = register("ent.mallorn.summonEnt");

    /** A half-troll's death, pain and idle grunt: "lotr:halfTroll.death", ".hurt" and ".say". */
    public static final SoundEvent HALF_TROLL_DEATH = register("halfTroll.death");
    public static final SoundEvent HALF_TROLL_HURT = register("halfTroll.hurt");
    public static final SoundEvent HALF_TROLL_SAY = register("halfTroll.say");

    /** Gollum's death, pain and gollum: "lotr:gollum.death", ".hurt" and ".say". */
    public static final SoundEvent GOLLUM_DEATH = register("gollum.death");
    public static final SoundEvent GOLLUM_HURT = register("gollum.hurt");
    public static final SoundEvent GOLLUM_SAY = register("gollum.say");
    /** An orc's death, pain and idle growl, and the cry of a bombardier dropping its bomb: "lotr:orc.death", ".hurt", ".say", ".fire". */
    public static final SoundEvent ORC_DEATH = register("orc.death");
    public static final SoundEvent ORC_HURT = register("orc.hurt");
    public static final SoundEvent ORC_SAY = register("orc.say");
    public static final SoundEvent ORC_FIRE = register("orc.fire");
    /** Breaking a pledge, "lotr:event.unpledge". */
    public static final SoundEvent EVENT_UNPLEDGE = register("event.unpledge");

    /** A plate smashing, "lotr:block.plate.break". */
    public static final SoundEvent BLOCK_PLATE_BREAK = register("block.plate.break");

    /** LOTRBlockTreasurePile.soundTypeTreasure: coins shifting underfoot. */
    public static final SoundEvent BLOCK_TREASURE_BREAK = register("block.treasure.break");
    public static final SoundEvent BLOCK_TREASURE_STEP = register("block.treasure.step");
    public static final SoundEvent BLOCK_TREASURE_PLACE = register("block.treasure.place");

    private LOTRSounds() {
    }

    /** LOTREntityDeer's calls, "lotr:deer.*". */
    public static final SoundEvent DEER_SAY = register("deer.say");
    public static final SoundEvent DEER_HURT = register("deer.hurt");
    public static final SoundEvent DEER_DEATH = register("deer.death");

    /**
     * LOTREntityAurochs' calls. It used "aurochs.hurt" for its death too, so
     * the original's unused aurochs.death is not carried over.
     */
    public static final SoundEvent AUROCHS_SAY = register("aurochs.say");
    public static final SoundEvent AUROCHS_HURT = register("aurochs.hurt");

    /** LOTREntityFlamingo's calls. */
    public static final SoundEvent FLAMINGO_SAY = register("flamingo.say");
    public static final SoundEvent FLAMINGO_HURT = register("flamingo.hurt");
    public static final SoundEvent FLAMINGO_DEATH = register("flamingo.death");

    /** LOTREntityLionBase's calls, which the lion rug also growls. */
    public static final SoundEvent LION_SAY = register("lion.say");
    public static final SoundEvent LION_HURT = register("lion.hurt");
    public static final SoundEvent LION_DEATH = register("lion.death");

    /** LOTREntityBear's calls, which the bear rug also growls. */
    public static final SoundEvent BEAR_SAY = register("bear.say");
    public static final SoundEvent BEAR_HURT = register("bear.hurt");
    public static final SoundEvent BEAR_DEATH = register("bear.death");

    /** LOTREntityCrocodile's calls and the snap of its jaws. */
    public static final SoundEvent CROCODILE_SAY = register("crocodile.say");
    public static final SoundEvent CROCODILE_DEATH = register("crocodile.death");
    public static final SoundEvent CROCODILE_SNAP = register("crocodile.snap");

    /** The elk's, zebra's and rhino's calls. */
    public static final SoundEvent ELK_SAY = register("elk.say");
    public static final SoundEvent ELK_HURT = register("elk.hurt");
    public static final SoundEvent ELK_DEATH = register("elk.death");
    public static final SoundEvent ZEBRA_SAY = register("zebra.say");
    public static final SoundEvent ZEBRA_HURT = register("zebra.hurt");
    public static final SoundEvent ZEBRA_DEATH = register("zebra.death");
    public static final SoundEvent RHINO_SAY = register("rhino.say");
    public static final SoundEvent RHINO_HURT = register("rhino.hurt");
    public static final SoundEvent RHINO_DEATH = register("rhino.death");

    /** "lotr:troll.ologHai_hammer": an Olog-hai's hammer, and a rhino's charge striking home. */
    public static final SoundEvent TROLL_OLOG_HAI_HAMMER = register("troll.olog_hai_hammer");

    /** A troll's voice (idle, hurt and dying alike), its tread, a sniff and a sneeze when tickled, its turning to stone, and a thrown rock breaking: "lotr:troll.*". */
    public static final SoundEvent TROLL_SAY = register("troll.say");
    public static final SoundEvent TROLL_STEP = register("troll.step");
    public static final SoundEvent TROLL_SNIFF = register("troll.sniff");
    public static final SoundEvent TROLL_SNEEZE = register("troll.sneeze");
    public static final SoundEvent TROLL_TRANSFORM = register("troll.transform");
    public static final SoundEvent TROLL_ROCK_SMASH = register("troll.rockSmash");

    /** A midge swarm's hum, and a swan's hiss. */
    public static final SoundEvent MIDGES_SWARM = register("midges.swarm");
    public static final SoundEvent SWAN_HISS = register("swan.hiss");

    /** The birds' calls: songbird, crow and seagull. */
    public static final SoundEvent BIRD_SAY = register("bird.say");
    public static final SoundEvent BIRD_HURT = register("bird.hurt");
    public static final SoundEvent BIRD_CROW_SAY = register("bird.crow.say");
    public static final SoundEvent BIRD_CROW_HURT = register("bird.crow.hurt");
    public static final SoundEvent BIRD_SEAGULL_SAY = register("bird.seagull.say");
    public static final SoundEvent BIRD_SEAGULL_HURT = register("bird.seagull.hurt");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    // Touching the class runs the static initialisers; nothing else to do.
    public static void init() {
    }
}