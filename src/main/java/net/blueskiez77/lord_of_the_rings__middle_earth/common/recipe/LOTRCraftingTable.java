package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

// Table and faction are NOT 1:1. GONDOR owns Gondorian and Dol Amroth, NEAR_HARAD owns Near Harad, Gulf and Umbar, HIGH_ELF owns High Elven and Rivendell. Each of those tables had its own recipe list, so recipes MUST be keyed on the table; the faction is only used for the alignment gate.
public enum LOTRCraftingTable implements StringRepresentable {

    ANGMAR("angmar", LOTRFaction.ANGMAR, () -> LOTRBlocks.ANGMAR_CRAFTING_TABLE),
    BLUE_DWARVEN("blue_dwarven", LOTRFaction.BLUE_MOUNTAINS, () -> LOTRBlocks.BLUE_DWARVEN_CRAFTING_TABLE),
    BREE("bree", LOTRFaction.BREE, () -> LOTRBlocks.BREE_CRAFTING_TABLE),
    DALE("dale", LOTRFaction.DALE, () -> LOTRBlocks.DALE_CRAFTING_TABLE),
    DOL_AMROTH("dol_amroth", LOTRFaction.GONDOR, () -> LOTRBlocks.DOL_AMROTH_CRAFTING_TABLE),
    DOL_GULDUR("dol_guldur", LOTRFaction.DOL_GULDUR, () -> LOTRBlocks.DOL_GULDUR_CRAFTING_TABLE),
    DORWINION("dorwinion", LOTRFaction.DORWINION, () -> LOTRBlocks.DORWINION_CRAFTING_TABLE),
    DUNLENDING("dunlending", LOTRFaction.DUNLAND, () -> LOTRBlocks.DUNLENDING_CRAFTING_TABLE),
    DWARVEN("dwarven", LOTRFaction.DURINS_FOLK, () -> LOTRBlocks.DWARVEN_CRAFTING_TABLE),
    ELVEN("elven", LOTRFaction.LOTHLORIEN, () -> LOTRBlocks.ELVEN_CRAFTING_TABLE),
    GONDORIAN("gondorian", LOTRFaction.GONDOR, () -> LOTRBlocks.GONDORIAN_CRAFTING_TABLE),
    GULF("gulf", LOTRFaction.NEAR_HARAD, () -> LOTRBlocks.GULF_CRAFTING_TABLE),
    GUNDABAD("gundabad", LOTRFaction.GUNDABAD, () -> LOTRBlocks.GUNDABAD_CRAFTING_TABLE),
    HALF_TROLL("half_troll", LOTRFaction.HALF_TROLL, () -> LOTRBlocks.HALF_TROLL_CRAFTING_TABLE),
    HIGH_ELVEN("high_elven", LOTRFaction.HIGH_ELF, () -> LOTRBlocks.HIGH_ELVEN_CRAFTING_TABLE),
    HOBBIT("hobbit", LOTRFaction.HOBBIT, () -> LOTRBlocks.HOBBIT_CRAFTING_TABLE),
    MOREDAIN("moredain", LOTRFaction.MORWAITH, () -> LOTRBlocks.MOREDAIN_CRAFTING_TABLE),
    MORGUL("morgul", LOTRFaction.MORDOR, () -> LOTRBlocks.MORGUL_CRAFTING_TABLE),
    NEAR_HARAD("near_harad", LOTRFaction.NEAR_HARAD, () -> LOTRBlocks.NEAR_HARAD_CRAFTING_TABLE),
    RANGER("ranger", LOTRFaction.RANGER_NORTH, () -> LOTRBlocks.RANGER_CRAFTING_TABLE),
    RHUN("rhun", LOTRFaction.RHUDEL, () -> LOTRBlocks.RHUN_CRAFTING_TABLE),
    RIVENDELL("rivendell", LOTRFaction.HIGH_ELF, () -> LOTRBlocks.RIVENDELL_CRAFTING_TABLE),
    ROHIRRIC("rohirric", LOTRFaction.ROHAN, () -> LOTRBlocks.ROHIRRIC_CRAFTING_TABLE),
    TAUREDAIN("tauredain", LOTRFaction.TAURETHRIM, () -> LOTRBlocks.TAUREDAIN_CRAFTING_TABLE),
    UMBAR("umbar", LOTRFaction.NEAR_HARAD, () -> LOTRBlocks.UMBAR_CRAFTING_TABLE),
    URUK("uruk", LOTRFaction.ISENGARD, () -> LOTRBlocks.URUK_CRAFTING_TABLE),
    WOOD_ELVEN("wood_elven", LOTRFaction.WOOD_ELF, () -> LOTRBlocks.WOOD_ELVEN_CRAFTING_TABLE);

    // Shared arrays from LOTRRecipes: a recipe added via addRecipeTo landed in every list named here, so recipe-to-table is many-to-many.
    public static final class Groups {
        public static final LOTRCraftingTable[] COMMON_ORC = {MORGUL, URUK, ANGMAR, DOL_GULDUR, GUNDABAD, HALF_TROLL};
        public static final LOTRCraftingTable[] COMMON_MORGUL = {MORGUL, ANGMAR, DOL_GULDUR};
        public static final LOTRCraftingTable[] COMMON_MORGUL_AND_GUNDABAD = {MORGUL, ANGMAR, DOL_GULDUR, GUNDABAD};
        public static final LOTRCraftingTable[] COMMON_ELF = {ELVEN, WOOD_ELVEN, HIGH_ELVEN, RIVENDELL};
        public static final LOTRCraftingTable[] COMMON_HIGH_ELF = {HIGH_ELVEN, RIVENDELL};
        public static final LOTRCraftingTable[] COMMON_DWARF = {DWARVEN, BLUE_DWARVEN};
        public static final LOTRCraftingTable[] COMMON_NUMENOREAN = {GONDORIAN, DOL_AMROTH, UMBAR};
        public static final LOTRCraftingTable[] COMMON_NEAR_HARAD = {NEAR_HARAD, UMBAR, GULF};
        public static final LOTRCraftingTable[] COMMON_HOBBIT = {HOBBIT, BREE};

        private Groups() {
        }
    }

    public static final Codec<LOTRCraftingTable> CODEC = StringRepresentable.fromEnum(LOTRCraftingTable::values);

    public static final StreamCodec<ByteBuf, LOTRCraftingTable> STREAM_CODEC =
            ByteBufCodecs.idMapper(i -> values()[i], LOTRCraftingTable::ordinal);

    // Built lazily: LOTRBlocks constructs these blocks, and its static init calls back into this enum. Resolving blocks eagerly here would read half-initialised LOTRBlocks fields and NPE.
    private static Map<Block, LOTRCraftingTable> byBlock;

    private final String name;
    private final LOTRFaction faction;
    private final Supplier<Block> block;

    LOTRCraftingTable(String name, LOTRFaction faction, Supplier<Block> block) {
        this.name = name;
        this.faction = faction;
        this.block = block;
    }

    public LOTRFaction faction() {
        return faction;
    }

    public Block block() {
        return block.get();
    }

    public Identifier id() {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name + "_crafting");
    }

    public String translationKey() {
        return "container.lotr.crafting." + name;
    }

    public static LOTRCraftingTable byBlock(Block block) {
        if (byBlock == null) {
            byBlock = Stream.of(values()).collect(Collectors.toMap(LOTRCraftingTable::block, t -> t));
        }
        return byBlock.get(block);
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}