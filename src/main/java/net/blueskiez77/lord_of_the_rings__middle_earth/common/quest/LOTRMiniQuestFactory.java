package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLore;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRBirdEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCrocodileEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRDeerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRDesertScorpionEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRHorseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRLionEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRLionessEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBanditEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBanditHaradEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRScrapTraderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar.LOTRAngmarHillmanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar.LOTRAngmarOrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale.LOTRDaleManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale.LOTRDaleSoldierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dolguldur.LOTRDolGuldurOrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRGaladhrimElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRHighElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRRivendellElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRWoodElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRDarkHuornEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRMoredainWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRTauredainBlowgunnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRDolAmrothSoldierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorRenegadeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorSoldierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRLossarnachAxemanEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRSwanKnightEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gundabad.LOTRGundabadOrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRHarnedorWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard.LOTRIsengardSnagaEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.isengard.LOTRUrukHaiEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRMordorOrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerIthilienEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger.LOTRRangerNorthEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohirrimWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRMirkwoodSpiderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRMountainTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRAngmarWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRGundabadWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRMordorWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRUrukWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith.LOTRBarrowWightEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMiniQuestFactory: the peoples who set mini-quests, each with the quests it may set -- gathered
 * by kind (collecting, pickpocketing, killing, bounties), a kind picked by its weight and then one of
 * its quests at random -- and the lore its rewards may include.
 *
 * <p>The original meant the kinds to be picked by weight but, its loop never stopping, always took
 * the last kind in its map; the port picks by weight. The nomads' coloured carpets were made as
 * stacks of white carpet (the colour given as the count), so white carpet is what they ask for, as in
 * the original. Not ported (user): the corsairs' chests of loot, which were pouches. NOT ported yet:
 * each group's achievement (D7).
 */
public enum LOTRMiniQuestFactory {
    HOBBIT("hobbit"),
    BREE("bree"),
    RUFFIAN_SPY("ruffianSpy"),
    RUFFIAN_BRUTE("ruffianBrute"),
    RANGER_NORTH("rangerNorth"),
    RANGER_NORTH_ARNOR_RELIC("rangerNorthArnorRelic"),
    BLUE_MOUNTAINS("blueMountains"),
    HIGH_ELF("highElf"),
    RIVENDELL("rivendell"),
    GUNDABAD("gundabad"),
    ANGMAR("angmar"),
    ANGMAR_HILLMAN("angmarHillman"),
    WOOD_ELF("woodElf"),
    DOL_GULDUR("dolGuldur"),
    DALE("dale"),
    DURIN("durin"),
    GALADHRIM("galadhrim"),
    DUNLAND("dunland"),
    ISENGARD("isengard"),
    ENT("ent"),
    ROHAN("rohan"),
    ROHAN_SHIELDMAIDEN("rohanShieldmaiden"),
    GONDOR("gondor"),
    GONDOR_KILL_RENEGADE("gondorKillRenegade"),
    MORDOR("mordor"),
    DORWINION("dorwinion"),
    DORWINION_ELF("dorwinionElf"),
    RHUN("rhun"),
    HARNENNOR("harnennor"),
    NEAR_HARAD("nearHarad"),
    UMBAR("umbar"),
    CORSAIR("corsair"),
    GONDOR_RENEGADE("gondorRenegade"),
    NOMAD("nomad"),
    GULF_HARAD("gulfHarad"),
    MOREDAIN("moredain"),
    TAUREDAIN("tauredain"),
    HALF_TROLL("halfTroll");

    private static final Map<Class<? extends LOTRMiniQuest>, Integer> QUEST_CLASS_WEIGHTS = new LinkedHashMap<>();

    private final String baseName;
    private @Nullable LOTRMiniQuestFactory baseSpeechGroup;
    private final Map<Class<? extends LOTRMiniQuest>, List<LOTRMiniQuest.QuestFactoryBase<?>>> questFactories = new LinkedHashMap<>();
    private List<LOTRLore.LoreCategory> loreCategories = new ArrayList<>();
    private @Nullable LOTRFaction alignmentRewardOverride;
    private boolean noAlignRewardForEnemy;

    LOTRMiniQuestFactory(String s) {
        this.baseName = s;
    }

    private static Supplier<ItemStack> mod(String name) {
        return mod(name, 1);
    }

    private static Supplier<ItemStack> mod(String name, int count) {
        Identifier id = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name);
        return () -> new ItemStack(BuiltInRegistries.ITEM.getValue(id), count);
    }

    private static Supplier<ItemStack> van(Item item) {
        return van(item, 1);
    }

    private static Supplier<ItemStack> van(Item item, int count) {
        return () -> new ItemStack(item, count);
    }

    public static void createMiniQuests() {
        registerQuestClass(LOTRMiniQuestCollect.class, 10);
        registerQuestClass(LOTRMiniQuestPickpocket.class, 6);
        registerQuestClass(LOTRMiniQuestKill.class, 8);
        registerQuestClass(LOTRMiniQuestBounty.class, 4);
        HOBBIT.setLore(LOTRLore.LoreCategory.SHIRE);
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("pipeweed").setCollectItem(mod("pipeweed"), 20, 40).setRewardFactor(0.25f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("ale"), 1, 6).setRewardFactor(3.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cider"), 1, 6).setRewardFactor(3.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("perry"), 1, 6).setRewardFactor(3.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("mead"), 1, 6).setRewardFactor(4.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cherry_liqueur"), 1, 6).setRewardFactor(3.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(van(Items.APPLE), 4, 10).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("green_apple"), 4, 10).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("pear"), 4, 10).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("plum"), 4, 10).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("cherries"), 4, 10).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(mod("apple_crumble"), 1, 3).setRewardFactor(5.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(mod("cherry_pie"), 1, 3).setRewardFactor(5.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(mod("berry_pie"), 1, 3).setRewardFactor(5.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(van(Items.BREAD), 4, 12).setRewardFactor(1.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(van(Items.COOKED_CHICKEN), 4, 8).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(van(Items.COOKED_PORKCHOP), 4, 8).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(mod("cooked_mutton"), 4, 8).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("uncleBirthday").setCollectItem(mod("cooked_venison"), 4, 8).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("farmingTool").setCollectItem(van(Items.IRON_HOE), 1, 3).setRewardFactor(4.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("farmingTool").setCollectItem(mod("bronze_hoe"), 1, 3).setRewardFactor(4.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("farmingTool").setCollectItem(van(Items.BUCKET), 1, 4).setRewardFactor(3.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(van(Items.OAK_LOG), 10, 30).setRewardFactor(0.5f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("shire_pine_log"), 10, 30).setRewardFactor(0.5f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("chestnut_log"), 10, 30).setRewardFactor(0.5f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("willow_log"), 10, 30).setRewardFactor(0.5f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("kitchenware").setCollectItem(mod("fine_plate"), 5, 12).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("kitchenware").setCollectItem(mod("clay_plate"), 5, 12).setRewardFactor(1.5f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("kitchenware").setCollectItem(mod("mug"), 5, 15).setRewardFactor(1.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("books").setCollectItem(van(Items.BOOK), 4, 10).setRewardFactor(2.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("pastries").setCollectItem(mod("apple_crumble"), 3, 5).setRewardFactor(4.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("pastries").setCollectItem(mod("cherry_pie"), 3, 5).setRewardFactor(4.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("pastries").setCollectItem(mod("berry_pie"), 3, 5).setRewardFactor(4.0f));
        HOBBIT.addQuest(new LOTRMiniQuestCollect.QFCollect("pastries").setCollectItem(van(Items.CAKE), 3, 5).setRewardFactor(4.0f));
        BREE.setLore(LOTRLore.LoreCategory.BREE);
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBucket").setCollectItem(van(Items.BUCKET), 1, 4).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("ale"), 1, 6).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cider"), 1, 6).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("perry"), 1, 6).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("mead"), 1, 6).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cherry_liqueur"), 1, 6).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BREAD), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 5, 20).setRewardFactor(0.75f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 5, 20).setRewardFactor(0.75f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_CHICKEN), 5, 20).setRewardFactor(0.75f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 5, 20).setRewardFactor(0.75f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 5, 20).setRewardFactor(0.75f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_RABBIT), 3, 15).setRewardFactor(1.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("rabbit_stew"), 3, 8).setRewardFactor(2.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BAKED_POTATO), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.APPLE), 3, 12).setRewardFactor(1.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("green_apple"), 3, 12).setRewardFactor(1.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("pear"), 3, 12).setRewardFactor(1.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("apple_crumble"), 2, 5).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cherry_pie"), 2, 5).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("ale"), 5, 15).setRewardFactor(1.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cider"), 5, 15).setRewardFactor(1.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRangerItem").setCollectItem(mod("ranger_hood"), 1, 2).setRewardFactor(8.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRangerItem").setCollectItem(mod("ranger_tunic"), 1, 2).setRewardFactor(8.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRangerItem").setCollectItem(mod("ranger_leggings"), 1, 2).setRewardFactor(8.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRangerItem").setCollectItem(mod("ranger_boots"), 1, 2).setRewardFactor(8.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(van(Items.IRON_HOE), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(mod("bronze_hoe"), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(van(Items.IRON_AXE), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(mod("bronze_axe"), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(van(Items.IRON_SHOVEL), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(mod("bronze_shovel"), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(mod("chisel"), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(van(Items.SHEARS), 1, 3).setRewardFactor(4.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTool").setCollectItem(van(Items.BUCKET), 1, 4).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(van(Items.OAK_LOG), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("beech_log"), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("maple_log"), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("chestnut_log"), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("firewood").setCollectItem(mod("willow_log"), 10, 30).setRewardFactor(0.5f));
        BREE.addQuest(new LOTRMiniQuestCollect.QFCollect("pipeweed").setCollectItem(mod("pipeweed"), 20, 40).setRewardFactor(0.25f));
        BREE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killEnemy").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        BREE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killEnemy").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        BREE.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
        BREE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killEnemy").setKillEntity(() -> LOTREntities.BANDIT, LOTRBanditEntity.class, 1, 3).setRewardFactor(8.0f));
        BREE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 40));
        BREE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWight").setKillEntity(() -> LOTREntities.BARROW_WIGHT, LOTRBarrowWightEntity.class, 5, 10).setRewardFactor(3.0f));
        BREE.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        RUFFIAN_SPY.setAlignmentRewardOverride(LOTRFaction.ISENGARD).setNoAlignRewardForEnemy();
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestPickpocket.QFPickpocket("pickpocket").setPickpocketFaction(LOTRFaction.BREE, 2, 6));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestPickpocket.QFPickpocket("pickpocketForBoss").setPickpocketFaction(LOTRFaction.BREE, 2, 8).setRewardFactor(1.5f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPipeweed").setCollectItem(mod("pipeweed"), 10, 20).setRewardFactor(0.5f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("ale"), 3, 6).setRewardFactor(3.0f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cider"), 3, 6).setRewardFactor(3.0f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("perry"), 3, 6).setRewardFactor(3.0f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("mead"), 3, 6).setRewardFactor(4.0f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMathom").setCollectItem(mod("hobbit_banner"), 10, 15).setRewardFactor(1.5f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMathom").setCollectItem(mod("smoking_pipe"), 1, 2).setRewardFactor(15.0f));
        RUFFIAN_SPY.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMathom").setCollectItem(mod("pipeweed"), 10, 20).setRewardFactor(0.5f));
        RUFFIAN_BRUTE.setAlignmentRewardOverride(LOTRFaction.ISENGARD).setNoAlignRewardForEnemy();
        RUFFIAN_BRUTE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("ale"), 3, 6));
        RUFFIAN_BRUTE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cider"), 3, 6));
        RUFFIAN_BRUTE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("perry"), 3, 6));
        RUFFIAN_BRUTE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("mead"), 3, 6));
        RUFFIAN_BRUTE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPipeweed").setCollectItem(mod("pipeweed"), 20, 40));
        RUFFIAN_BRUTE.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killBreelanders").setKillFaction(LOTRFaction.BREE, 10, 30));
        RUFFIAN_BRUTE.forEachFactory(qf -> {
            qf.setRewardFactor(0.0f);
            qf.setHiring(0.0f);
        });
        RANGER_NORTH.setLore(LOTRLore.LoreCategory.ERIADOR);
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(van(Items.OAK_LOG), 30, 60).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(van(Items.SPRUCE_LOG), 30, 60).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(mod("beech_log"), 30, 60).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(mod("chestnut_log"), 30, 60).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(mod("willow_log"), 30, 60).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBricks").setCollectItem(van(Items.STONE_BRICKS), 40, 100).setRewardFactor(0.2f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBricks").setCollectItem(mod("arnor_brick"), 30, 80).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BREAD), 10, 30).setRewardFactor(0.5f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 5, 20).setRewardFactor(0.75f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 5, 20).setRewardFactor(0.75f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 5, 20).setRewardFactor(0.75f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 5, 20).setRewardFactor(0.75f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_RABBIT), 3, 15).setRewardFactor(1.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("ale"), 5, 15).setRewardFactor(1.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cider"), 5, 15).setRewardFactor(1.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectAthelas").setCollectItem(mod("athelas"), 2, 6).setRewardFactor(3.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGondorItem").setCollectItem(mod("gondor_sword"), 1, 1).setRewardFactor(10.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGondorItem").setCollectItem(mod("gondor_helmet"), 1, 1).setRewardFactor(10.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGondorItem").setCollectItem(mod("gondor_winged_helmet"), 1, 1).setRewardFactor(15.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("craftRangerItem").setCollectItem(mod("ranger_hood"), 2, 5).setRewardFactor(3.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("craftRangerItem").setCollectItem(mod("ranger_tunic"), 2, 5).setRewardFactor(4.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(van(Items.IRON_SWORD), 2, 4).setRewardFactor(3.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("iron_dagger"), 2, 6).setRewardFactor(2.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("ranger_bow"), 3, 7).setRewardFactor(2.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(van(Items.ARROW), 20, 40).setRewardFactor(0.25f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(van(Items.WOOL.pick(DyeColor.WHITE)), 6, 15).setRewardFactor(1.0f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(van(Items.LEATHER), 10, 20).setRewardFactor(0.5f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEnemyBones").setCollectItem(mod("orc_bone"), 10, 40).setRewardFactor(0.5f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEnemyBones").setCollectItem(mod("warg_bone"), 10, 30).setRewardFactor(0.75f));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 40));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killAngmar").setKillFaction(LOTRFaction.ANGMAR, 10, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killTroll").setKillEntity(() -> LOTREntities.TROLL, LOTRTrollEntity.class, 10, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killMountainTroll").setKillEntity(() -> LOTREntities.MOUNTAIN_TROLL, LOTRMountainTrollEntity.class, 20, 40));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 40));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.ANGMAR_WARG, LOTRAngmarWargEntity.class, 10, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killDarkHuorn").setKillEntity(() -> LOTREntities.DARK_HUORN, LOTRDarkHuornEntity.class, 20, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("avengeBrother").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("avengeBrother").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killHillmen").setKillEntity(() -> LOTREntities.ANGMAR_HILLMAN, LOTRAngmarHillmanEntity.class, 10, 30));
        RANGER_NORTH.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        RANGER_NORTH_ARNOR_RELIC.setBaseSpeechGroup(RANGER_NORTH);
        RANGER_NORTH_ARNOR_RELIC.setLore(LOTRLore.LoreCategory.ERIADOR);
        RANGER_NORTH_ARNOR_RELIC.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("arnorRelicKill").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
        RANGER_NORTH_ARNOR_RELIC.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("arnorRelicKill").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        RANGER_NORTH_ARNOR_RELIC.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("arnorRelicKill").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        RANGER_NORTH_ARNOR_RELIC.forEachFactory(qf -> {
            qf.setRewardFactor(0.0f);
            qf.setRewardItems(List.of(mod("arnor_helmet"), mod("arnor_chestplate"), mod("arnor_leggings"), mod("arnor_boots"), mod("arnor_sword"), mod("arnor_dagger"), mod("arnor_spear")));
        });
        BLUE_MOUNTAINS.setLore(LOTRLore.LoreCategory.BLUE_MOUNTAINS);
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("mineMithril").setCollectItem(mod("mithril"), 1, 2).setRewardFactor(50.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 15).setRewardFactor(4.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 15).setRewardFactor(4.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GLOWSTONE_DUST), 5, 15).setRewardFactor(2.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("diamond"), 1, 3).setRewardFactor(15.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("sapphire"), 1, 3).setRewardFactor(12.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("opal"), 1, 3).setRewardFactor(10.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("amethyst"), 1, 3).setRewardFactor(10.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeDwarfWeapon").setCollectItem(mod("blue_dwarven_warhammer"), 1, 3).setRewardFactor(5.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeDwarfWeapon").setCollectItem(mod("blue_dwarven_battleaxe"), 1, 3).setRewardFactor(5.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeDwarfWeapon").setCollectItem(mod("blue_dwarven_throwing_axe"), 1, 4).setRewardFactor(4.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("dwarven_ale"), 2, 5).setRewardFactor(3.0f));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 20, 40));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        HIGH_ELF.setLore(LOTRLore.LoreCategory.LINDON);
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(van(Items.BIRCH_SAPLING), 5, 20).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(mod("beech_sapling"), 5, 20).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBirchWood").setCollectItem(van(Items.BIRCH_LOG), 10, 50).setRewardFactor(0.5f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGoldenLeaves").setCollectItem(mod("mallorn_leaves"), 10, 20).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMallornSapling").setCollectItem(mod("mallorn_sapling"), 3, 10).setRewardFactor(2.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMallornNut").setCollectItem(mod("mallorn_nut"), 1, 3).setRewardFactor(5.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectCrystal").setCollectItem(mod("edhelvir"), 4, 16).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("lindon_sword"), 1, 4).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("lindon_battlestaff"), 1, 4).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("lindon_longspear"), 1, 4).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("lindon_spear"), 1, 4).setRewardFactor(2.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("lindon_helmet"), 1, 4).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("lindon_chestplate"), 1, 4).setRewardFactor(4.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 10).setRewardFactor(4.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 10).setRewardFactor(4.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("sapphire"), 1, 3).setRewardFactor(12.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("opal"), 1, 3).setRewardFactor(10.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("pearl"), 1, 3).setRewardFactor(15.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDwarven").setCollectItem(mod("glowing_dwarven_brick"), 2, 6).setRewardFactor(4.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("gondor_sword"), 1, 1).setRewardFactor(10.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("gondor_winged_helmet"), 1, 1).setRewardFactor(15.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("ranger_hood"), 1, 1).setRewardFactor(10.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("gondor_brick"), 10, 20).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("carved_gondor_brick"), 3, 5).setRewardFactor(4.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("arnor_brick"), 10, 20).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("carved_arnor_brick"), 3, 5).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.OAK_SAPLING), 2, 5).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.SPRUCE_SAPLING), 2, 5).setRewardFactor(3.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.BIRCH_SAPLING), 2, 5).setRewardFactor(2.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.POPPY), 2, 8).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.DANDELION), 2, 8).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectString").setCollectItem(van(Items.STRING), 5, 20).setRewardFactor(1.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_helmet"), 1, 1).setRewardFactor(10.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("angmar_helmet"), 1, 1).setRewardFactor(10.0f));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 40));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.ANGMAR_ORC, LOTRAngmarOrcEntity.class, 10, 40));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 40));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killAngmar").setKillFaction(LOTRFaction.ANGMAR, 10, 30));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.ANGMAR_WARG, LOTRAngmarWargEntity.class, 10, 30));
        HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killTroll").setKillEntity(() -> LOTREntities.TROLL, LOTRTrollEntity.class, 10, 30));
        HIGH_ELF.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        RIVENDELL.setLore(LOTRLore.LoreCategory.RIVENDELL, LOTRLore.LoreCategory.EREGION);
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(van(Items.BIRCH_SAPLING), 5, 20).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(mod("beech_sapling"), 5, 20).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBirchWood").setCollectItem(van(Items.BIRCH_LOG), 10, 50).setRewardFactor(0.5f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGoldenLeaves").setCollectItem(mod("mallorn_leaves"), 10, 20).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMallornSapling").setCollectItem(mod("mallorn_sapling"), 3, 10).setRewardFactor(2.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMallornNut").setCollectItem(mod("mallorn_nut"), 1, 3).setRewardFactor(5.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectCrystal").setCollectItem(mod("edhelvir"), 4, 16).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("rivendell_sword"), 1, 4).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("rivendell_battlestaff"), 1, 4).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("rivendell_longspear"), 1, 4).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("rivendell_spear"), 1, 4).setRewardFactor(2.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("rivendell_helmet"), 1, 4).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("rivendell_chestplate"), 1, 4).setRewardFactor(4.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 10).setRewardFactor(4.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 10).setRewardFactor(4.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("diamond"), 1, 3).setRewardFactor(15.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("emerald"), 1, 3).setRewardFactor(12.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("pearl"), 1, 3).setRewardFactor(15.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDwarven").setCollectItem(mod("glowing_dwarven_brick"), 2, 6).setRewardFactor(4.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("gondor_sword"), 1, 1).setRewardFactor(10.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("gondor_winged_helmet"), 1, 1).setRewardFactor(15.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("ranger_hood"), 1, 1).setRewardFactor(10.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("gondor_brick"), 10, 20).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("carved_gondor_brick"), 3, 5).setRewardFactor(4.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("arnor_brick"), 10, 20).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectNumenorItem").setCollectItem(mod("carved_arnor_brick"), 3, 5).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.OAK_SAPLING), 2, 5).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.SPRUCE_SAPLING), 2, 5).setRewardFactor(3.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.BIRCH_SAPLING), 2, 5).setRewardFactor(2.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.POPPY), 2, 8).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.DANDELION), 2, 8).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectString").setCollectItem(van(Items.STRING), 5, 20).setRewardFactor(1.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_helmet"), 1, 1).setRewardFactor(10.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("angmar_helmet"), 1, 1).setRewardFactor(10.0f));
        RIVENDELL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 40));
        RIVENDELL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.ANGMAR_ORC, LOTRAngmarOrcEntity.class, 10, 40));
        RIVENDELL.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 40));
        RIVENDELL.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killAngmar").setKillFaction(LOTRFaction.ANGMAR, 10, 30));
        RIVENDELL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        RIVENDELL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.ANGMAR_WARG, LOTRAngmarWargEntity.class, 10, 30));
        RIVENDELL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killTroll").setKillEntity(() -> LOTREntities.TROLL, LOTRTrollEntity.class, 10, 30));
        RIVENDELL.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        GUNDABAD.setLore(LOTRLore.LoreCategory.GUNDABAD);
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(van(Items.IRON_SWORD), 1, 5).setRewardFactor(3.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("iron_dagger"), 1, 6).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 2, 8).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 2, 8).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 2, 8).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 2, 8).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BONE), 2, 8).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(van(Items.IRON_HELMET), 2, 5).setRewardFactor(3.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(van(Items.IRON_CHESTPLATE), 2, 5).setRewardFactor(4.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("bronze_helmet"), 2, 5).setRewardFactor(3.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("bronze_chestplate"), 2, 5).setRewardFactor(4.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(2.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 8).setRewardFactor(4.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 8).setRewardFactor(4.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("diamond"), 1, 3).setRewardFactor(15.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("ruby"), 1, 3).setRewardFactor(12.0f));
        GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.HOBBIT, 10, 30));
        GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.RANGER_NORTH, 10, 40));
        GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.HIGH_ELF, 10, 40));
        GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.LOTHLORIEN, 10, 40));
        GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRanger").setKillEntity(() -> LOTREntities.RANGER_NORTH, LOTRRangerNorthEntity.class, 10, 30));
        GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.HIGH_ELF, LOTRHighElfEntity.class, 10, 30));
        GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.RIVENDELL_ELF, LOTRRivendellElfEntity.class, 10, 30));
        GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.GALADHRIM_ELF, LOTRGaladhrimElfEntity.class, 10, 30));
        GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killDwarf").setKillEntity(() -> LOTREntities.DWARF, LOTRDwarfEntity.class, 10, 30));
        GUNDABAD.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        ANGMAR.setLore(LOTRLore.LoreCategory.ANGMAR);
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_sword"), 1, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_battleaxe"), 1, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_axe"), 1, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_dagger"), 1, 6).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_poleaxe"), 1, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 2, 8).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 2, 8).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 2, 8).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 2, 8).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BONE), 2, 8).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_helmet"), 2, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_chestplate"), 2, 5).setRewardFactor(4.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_leggings"), 2, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_boots"), 2, 5).setRewardFactor(3.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(2.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 8).setRewardFactor(4.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 8).setRewardFactor(4.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("gulduril"), 3, 6).setRewardFactor(4.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("emerald"), 1, 3).setRewardFactor(12.0f));
        ANGMAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("ruby"), 1, 3).setRewardFactor(12.0f));
        ANGMAR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.RANGER_NORTH, 10, 40));
        ANGMAR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.HIGH_ELF, 10, 40));
        ANGMAR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRanger").setKillEntity(() -> LOTREntities.RANGER_NORTH, LOTRRangerNorthEntity.class, 10, 30));
        ANGMAR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.HIGH_ELF, LOTRHighElfEntity.class, 10, 30));
        ANGMAR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.RIVENDELL_ELF, LOTRRivendellElfEntity.class, 10, 30));
        ANGMAR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        ANGMAR_HILLMAN.setLore(LOTRLore.LoreCategory.ANGMAR);
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMeat").setCollectItem(van(Items.COOKED_PORKCHOP), 4, 8).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMeat").setCollectItem(van(Items.COOKED_BEEF), 4, 8).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMeat").setCollectItem(mod("cooked_mutton"), 4, 8).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMeat").setCollectItem(mod("cooked_venison"), 4, 8).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMeat").setCollectItem(van(Items.COOKED_CHICKEN), 4, 8).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_helmet"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_chestplate"), 2, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_leggings"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("angmar_boots"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("bronze_helmet"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("bronze_chestplate"), 2, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("bronze_leggings"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("bronze_boots"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(van(Items.IRON_HELMET), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(van(Items.IRON_CHESTPLATE), 2, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(van(Items.IRON_LEGGINGS), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(van(Items.IRON_BOOTS), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMetal").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMetal").setCollectItem(mod("bronze_ingot"), 3, 10).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMetal").setCollectItem(van(Items.COPPER_INGOT), 3, 10).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMetal").setCollectItem(mod("orc_steel_ingot"), 3, 10).setRewardFactor(2.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcBrew").setCollectItem(mod("orc_draught"), 2, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_sword"), 1, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_battleaxe"), 1, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("angmar_poleaxe"), 1, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("bronze_sword"), 1, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("bronze_battleaxe"), 1, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(van(Items.IRON_SWORD), 1, 5).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("iron_battleaxe"), 1, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("iron_pike"), 1, 5).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectThrowingAxes").setCollectItem(mod("iron_throwing_axe"), 3, 6).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectThrowingAxes").setCollectItem(mod("bronze_throwing_axe"), 3, 6).setRewardFactor(3.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTreasure").setCollectItem(van(Items.GOLD_INGOT), 3, 6).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTreasure").setCollectItem(mod("silver_ingot"), 3, 6).setRewardFactor(4.0f));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killRanger").setKillFaction(LOTRFaction.RANGER_NORTH, 10, 30));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killRangerMany").setKillFaction(LOTRFaction.RANGER_NORTH, 40, 60));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killHighElf").setKillFaction(LOTRFaction.HIGH_ELF, 10, 30));
        ANGMAR_HILLMAN.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        WOOD_ELF.setLore(LOTRLore.LoreCategory.WOODLAND_REALM);
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGoldenLeaves").setCollectItem(mod("mallorn_leaves"), 10, 20).setRewardFactor(1.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMallornSapling").setCollectItem(mod("mallorn_sapling"), 3, 10).setRewardFactor(2.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMallornNut").setCollectItem(mod("mallorn_nut"), 1, 3).setRewardFactor(5.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectLorienFlowers").setCollectItem(mod("elanor"), 2, 7).setRewardFactor(2.5f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectLorienFlowers").setCollectItem(mod("niphredil"), 2, 7).setRewardFactor(2.5f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("wood_elven_sword"), 1, 4).setRewardFactor(3.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("wood_elven_battlestaff"), 1, 4).setRewardFactor(3.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("wood_elven_longspear"), 1, 4).setRewardFactor(3.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("wood_elven_spear"), 1, 4).setRewardFactor(2.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("wood_elven_helmet"), 1, 4).setRewardFactor(3.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("wood_elven_chestplate"), 1, 4).setRewardFactor(4.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(mod("green_oak_sapling"), 4, 8).setRewardFactor(2.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(mod("mirk_oak_red_sapling"), 2, 5).setRewardFactor(4.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectString").setCollectItem(van(Items.STRING), 5, 20).setRewardFactor(1.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_helmet"), 1, 1).setRewardFactor(10.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("dol_guldur_helmet"), 1, 1).setRewardFactor(10.0f));
        WOOD_ELF.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killDolGuldur").setKillFaction(LOTRFaction.DOL_GULDUR, 10, 40));
        WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.DOL_GULDUR_ORC, LOTRDolGuldurOrcEntity.class, 10, 40));
        WOOD_ELF.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
        WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killSpider").setKillEntity(() -> LOTREntities.MIRKWOOD_SPIDER, LOTRMirkwoodSpiderEntity.class, 10, 40));
        WOOD_ELF.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        DOL_GULDUR.setLore(LOTRLore.LoreCategory.DOL_GULDUR);
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("dol_guldur_sword"), 1, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("dol_guldur_battleaxe"), 1, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("dol_guldur_axe"), 1, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("dol_guldur_dagger"), 1, 6).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("dol_guldur_spike"), 1, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 2, 8).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 2, 8).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 2, 8).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 2, 8).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BONE), 2, 8).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("dol_guldur_helmet"), 2, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("dol_guldur_chestplate"), 2, 5).setRewardFactor(4.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("dol_guldur_leggings"), 2, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("dol_guldur_boots"), 2, 5).setRewardFactor(3.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(2.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 8).setRewardFactor(4.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 8).setRewardFactor(4.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("gulduril"), 3, 6).setRewardFactor(4.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("emerald"), 1, 3).setRewardFactor(12.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("ruby"), 1, 3).setRewardFactor(12.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("amethyst"), 1, 3).setRewardFactor(10.0f));
        DOL_GULDUR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.LOTHLORIEN, 10, 40));
        DOL_GULDUR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.WOOD_ELF, 10, 40));
        DOL_GULDUR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.DALE, 10, 40));
        DOL_GULDUR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.GALADHRIM_ELF, LOTRGaladhrimElfEntity.class, 10, 30));
        DOL_GULDUR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killElf").setKillEntity(() -> LOTREntities.WOOD_ELF, LOTRWoodElfEntity.class, 10, 30));
        DOL_GULDUR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        DALE.setLore(LOTRLore.LoreCategory.DALE);
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("pastries").setCollectItem(mod("dalish_pastry"), 3, 8).setRewardFactor(4.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("red_wine"), 2, 5).setRewardFactor(5.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("white_wine"), 2, 5).setRewardFactor(5.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArrows").setCollectItem(van(Items.ARROW), 10, 30).setRewardFactor(0.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("foreignItem").setCollectItem(mod("olive_bread"), 2, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("foreignItem").setCollectItem(mod("banana"), 3, 6).setRewardFactor(4.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("foreignItem").setCollectItem(mod("mango"), 3, 6).setRewardFactor(4.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("foreignItem").setCollectItem(mod("gondor_dagger"), 1, 1).setRewardFactor(15.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("foreignItem").setCollectItem(mod("rohirric_dagger"), 1, 1).setRewardFactor(15.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("foreignItem").setCollectItem(mod("blue_dwarven_dagger"), 1, 1).setRewardFactor(15.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(van(Items.GOLD_INGOT), 3, 10).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(mod("silver_ingot"), 3, 10).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(van(Items.IRON_INGOT), 5, 12).setRewardFactor(2.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(mod("bronze_ingot"), 5, 12).setRewardFactor(2.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(van(Items.WHEAT), 20, 40).setRewardFactor(0.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(van(Items.BREAD), 3, 10).setRewardFactor(2.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(van(Items.OAK_LOG), 20, 60).setRewardFactor(0.25f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("dwarfTrade").setCollectItem(van(Items.SPRUCE_LOG), 20, 60).setRewardFactor(0.25f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("smithyItem").setCollectItem(van(Items.COAL), 10, 30).setRewardFactor(0.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("smithyItem").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(1.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("smithyItem").setCollectItem(mod("bronze_ingot"), 3, 10).setRewardFactor(1.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("smithyItem").setCollectItem(van(Items.BUCKET), 3, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("smithyItem").setCollectItem(van(Items.LAVA_BUCKET), 2, 4).setRewardFactor(5.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_sword"), 1, 4).setRewardFactor(5.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_dagger"), 2, 6).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_spear"), 1, 4).setRewardFactor(4.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_pitchfork"), 1, 4).setRewardFactor(4.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_helmet"), 2, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_chestplate"), 2, 5).setRewardFactor(4.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_leggings"), 2, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_boots"), 2, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dale_bow"), 3, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(van(Items.ARROW), 10, 40).setRewardFactor(0.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(van(Items.LEATHER), 10, 30).setRewardFactor(0.5f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.APPLE), 3, 8).setRewardFactor(2.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("green_apple"), 3, 8).setRewardFactor(2.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("apple_crumble"), 2, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 2, 6).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_CHICKEN), 2, 6).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cherry_pie"), 2, 5).setRewardFactor(3.0f));
        DALE.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killDolGuldur").setKillFaction(LOTRFaction.DOL_GULDUR, 10, 40));
        DALE.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killMordor").setKillFaction(LOTRFaction.MORDOR, 10, 40));
        DALE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        DALE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.DOL_GULDUR_ORC, LOTRDolGuldurOrcEntity.class, 10, 30));
        DALE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        DALE.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        DURIN.setLore(LOTRLore.LoreCategory.DURIN);
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("mineMithril").setCollectItem(mod("mithril"), 1, 2).setRewardFactor(50.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 15).setRewardFactor(4.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 15).setRewardFactor(4.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GLOWSTONE_DUST), 5, 15).setRewardFactor(2.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("diamond"), 1, 3).setRewardFactor(15.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("emerald"), 1, 3).setRewardFactor(12.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("ruby"), 1, 3).setRewardFactor(12.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("opal"), 1, 3).setRewardFactor(10.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeDwarfWeapon").setCollectItem(mod("dwarven_warhammer"), 1, 3).setRewardFactor(5.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeDwarfWeapon").setCollectItem(mod("dwarven_battleaxe"), 1, 3).setRewardFactor(5.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeDwarfWeapon").setCollectItem(mod("dwarven_throwing_axe"), 1, 4).setRewardFactor(4.0f));
        DURIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("dwarven_ale"), 2, 5).setRewardFactor(3.0f));
        DURIN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 20, 40));
        DURIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        DURIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        DURIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killSpider").setKillEntity(() -> LOTREntities.MIRKWOOD_SPIDER, LOTRMirkwoodSpiderEntity.class, 20, 30));
        DURIN.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        GALADHRIM.setLore(LOTRLore.LoreCategory.LOTHLORIEN);
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(mod("mallorn_sapling"), 5, 20).setRewardFactor(0.5f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(mod("elanor"), 5, 30).setRewardFactor(0.25f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(mod("niphredil"), 5, 30).setRewardFactor(0.25f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collect").setCollectItem(mod("mallorn_nut"), 5, 10).setRewardFactor(2.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFangorn").setCollectItem(mod("fangorn_plant_green"), 4, 10).setRewardFactor(2.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFangorn").setCollectItem(mod("fangorn_plant_gold"), 4, 10).setRewardFactor(2.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFangorn").setCollectItem(mod("fangorn_plant_silver"), 4, 10).setRewardFactor(2.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectCrystal").setCollectItem(mod("edhelvir"), 4, 16).setRewardFactor(1.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("galadhrim_sword"), 1, 4).setRewardFactor(3.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("galadhrim_battlestaff"), 1, 4).setRewardFactor(3.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("galadhrim_longspear"), 1, 4).setRewardFactor(3.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("galadhrim_spear"), 1, 4).setRewardFactor(2.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("galadhrim_helmet"), 1, 4).setRewardFactor(3.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("galadhrim_chestplate"), 1, 4).setRewardFactor(4.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 10).setRewardFactor(4.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 10).setRewardFactor(4.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("emerald"), 1, 3).setRewardFactor(12.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("opal"), 1, 3).setRewardFactor(10.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("amber"), 1, 3).setRewardFactor(10.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDwarven").setCollectItem(mod("glowing_dwarven_brick"), 2, 6).setRewardFactor(4.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.SPRUCE_SAPLING), 2, 5).setRewardFactor(3.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.BIRCH_SAPLING), 2, 5).setRewardFactor(3.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.POPPY), 2, 8).setRewardFactor(1.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPlants").setCollectItem(van(Items.DANDELION), 2, 8).setRewardFactor(1.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectString").setCollectItem(van(Items.STRING), 5, 20).setRewardFactor(1.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_helmet"), 1, 1).setRewardFactor(10.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("dol_guldur_helmet"), 1, 1).setRewardFactor(10.0f));
        GALADHRIM.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killDolGuldur").setKillFaction(LOTRFaction.DOL_GULDUR, 10, 30));
        GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.DOL_GULDUR_ORC, LOTRDolGuldurOrcEntity.class, 10, 30));
        GALADHRIM.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
        GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        GALADHRIM.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        DUNLAND.setLore(LOTRLore.LoreCategory.DUNLAND);
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectResources").setCollectItem(van(Items.OAK_LOG), 30, 80).setRewardFactor(0.25f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectResources").setCollectItem(van(Items.SPRUCE_LOG), 30, 80).setRewardFactor(0.25f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectResources").setCollectItem(van(Items.COAL), 10, 30).setRewardFactor(0.5f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectResources").setCollectItem(van(Items.COBBLESTONE), 30, 80).setRewardFactor(0.25f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectResources").setCollectItem(van(Items.LEATHER), 10, 30).setRewardFactor(0.5f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectResources").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(1.5f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("ale"), 3, 10).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("mead"), 3, 10).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cider"), 3, 10).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("rum"), 3, 10).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRohanItem").setCollectItem(mod("rohirric_coif"), 1, 3).setRewardFactor(10.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRohanItem").setCollectItem(mod("rohirric_hauberk"), 1, 3).setRewardFactor(10.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRohanItem").setCollectItem(mod("rohirric_sword"), 1, 3).setRewardFactor(10.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 3, 8).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 3, 8).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 3, 8).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 3, 8).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_CHICKEN), 3, 8).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_RABBIT), 3, 12).setRewardFactor(2.0f));
        DUNLAND.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BREAD), 5, 15).setRewardFactor(1.0f));
        DUNLAND.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killRohirrim").setKillFaction(LOTRFaction.ROHAN, 10, 40));
        DUNLAND.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("avengeKin").setKillFaction(LOTRFaction.ROHAN, 30, 60));
        DUNLAND.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killHorse").setKillEntity(() -> LOTREntities.HORSE, LOTRHorseEntity.class, 10, 20));
        DUNLAND.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        ISENGARD.setLore(LOTRLore.LoreCategory.ISENGARD);
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("uruk_cleaver"), 1, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("uruk_pike"), 1, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("uruk_battleaxe"), 1, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("uruk_axe"), 1, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("uruk_dagger"), 1, 6).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 2, 8).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 2, 8).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 2, 8).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 2, 8).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BONE), 2, 8).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("uruk_helmet"), 2, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("uruk_chestplate"), 2, 5).setRewardFactor(4.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("uruk_leggings"), 2, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("uruk_boots"), 2, 5).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(van(Items.GOLD_INGOT), 3, 8).setRewardFactor(4.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("silver_ingot"), 3, 8).setRewardFactor(4.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("ruby"), 1, 3).setRewardFactor(12.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeSteel").setCollectItem(mod("uruk_steel_ingot"), 3, 10).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeSteel").setCollectItem(mod("orc_steel_ingot"), 3, 10).setRewardFactor(3.0f));
        ISENGARD.addQuest(new LOTRMiniQuestCollect.QFCollect("forgeSteel").setCollectItem(van(Items.IRON_INGOT), 3, 10).setRewardFactor(2.0f));
        ISENGARD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.ROHAN, 10, 40));
        ISENGARD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.GONDOR, 10, 40));
        ISENGARD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killMen").setKillEntity(() -> LOTREntities.ROHIRRIM_WARRIOR, LOTRRohirrimWarriorEntity.class, 10, 30));
        ISENGARD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killMen").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        ISENGARD.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        ROHAN.setLore(LOTRLore.LoreCategory.ROHAN);
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 3, 8).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 3, 8).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 3, 8).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 3, 8).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_CHICKEN), 3, 8).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_RABBIT), 3, 12).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BREAD), 5, 15).setRewardFactor(1.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMead").setCollectItem(mod("mead"), 3, 20).setRewardFactor(1.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.OAK_LOG), 20, 60).setRewardFactor(0.25f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.SPRUCE_LOG), 20, 60).setRewardFactor(0.25f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.OAK_PLANKS), 80, 160).setRewardFactor(0.125f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.SPRUCE_PLANKS), 80, 160).setRewardFactor(0.125f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.COBBLESTONE), 30, 80).setRewardFactor(0.25f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringWeapon").setCollectItem(mod("rohirric_sword"), 1, 4).setRewardFactor(3.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringWeapon").setCollectItem(van(Items.IRON_INGOT), 3, 8).setRewardFactor(2.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("stealUruk").setCollectItem(mod("uruk_crafting_table"), 1, 2).setRewardFactor(10.0f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBones").setCollectItem(mod("orc_bone"), 15, 30).setRewardFactor(0.5f));
        ROHAN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBones").setCollectItem(mod("warg_bone"), 15, 30).setRewardFactor(0.75f));
        ROHAN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.ISENGARD_SNAGA, LOTRIsengardSnagaEntity.class, 10, 30));
        ROHAN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.URUK_HAI, LOTRUrukHaiEntity.class, 10, 30));
        ROHAN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.MORDOR_ORC, LOTRMordorOrcEntity.class, 10, 30));
        ROHAN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("avengeRiders").setKillEntity(() -> LOTREntities.URUK_WARG, LOTRUrukWargEntity.class, 10, 20));
        ROHAN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killDunland").setKillFaction(LOTRFaction.DUNLAND, 10, 40));
        ROHAN.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        ROHAN_SHIELDMAIDEN.setLore(LOTRLore.LoreCategory.ROHAN);
        ROHAN_SHIELDMAIDEN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemies").setKillFaction(LOTRFaction.DUNLAND, 5, 20));
        ROHAN_SHIELDMAIDEN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemies").setKillFaction(LOTRFaction.ISENGARD, 5, 20));
        ROHAN_SHIELDMAIDEN.forEachFactory(qf -> {
            qf.setRewardFactor(0.0f);
            qf.setHiring(150.0f);
        });
        GONDOR.setLore(LOTRLore.LoreCategory.GONDOR);
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.OAK_LOG), 20, 60).setRewardFactor(0.25f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(van(Items.OAK_PLANKS), 80, 160).setRewardFactor(0.125f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(mod("gondor_rock"), 30, 80).setRewardFactor(0.25f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(mod("gondor_brick"), 30, 60).setRewardFactor(0.5f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("defences").setCollectItem(mod("gondor_cobblebrick"), 30, 60).setRewardFactor(0.5f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("lebethron").setCollectItem(mod("lebethron_log"), 10, 30).setRewardFactor(1.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectIron").setCollectItem(van(Items.IRON_ORE), 10, 20).setRewardFactor(1.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectIron").setCollectItem(van(Items.IRON_INGOT), 6, 15).setRewardFactor(1.5f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("blackMarble").setCollectItem(mod("mordor_rock"), 25, 35).setRewardFactor(1.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("gondor_sword"), 1, 4).setRewardFactor(5.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("gondor_spear"), 1, 4).setRewardFactor(5.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("gondor_pike"), 1, 4).setRewardFactor(5.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("gondor_helmet"), 1, 4).setRewardFactor(5.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("gondor_winged_helmet"), 1, 4).setRewardFactor(5.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("forge").setCollectItem(mod("gondor_chestplate"), 1, 4).setRewardFactor(5.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRohanItem").setCollectItem(mod("rohirric_sword"), 1, 1).setRewardFactor(15.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRohanItem").setCollectItem(mod("rohirric_coif"), 1, 1).setRewardFactor(15.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRohanItem").setCollectItem(mod("rohirric_marshal_helmet"), 1, 1).setRewardFactor(20.0f));
        GONDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPipeweed").setCollectItem(mod("pipeweed_plant"), 2, 4).setRewardFactor(6.0f));
        GONDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killMordor").setKillFaction(LOTRFaction.MORDOR, 10, 40));
        GONDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.MORDOR, 30, 40));
        GONDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.DUNLAND, 30, 40));
        GONDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.ISENGARD, 30, 40));
        GONDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killMordorMany").setKillFaction(LOTRFaction.MORDOR, 60, 90));
        GONDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killHarad").setKillFaction(LOTRFaction.NEAR_HARAD, 10, 40));
        GONDOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killHarnennor").setKillEntity(() -> LOTREntities.HARNEDOR_WARRIOR, LOTRHarnedorWarriorEntity.class, 20, 30));
        GONDOR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        GONDOR_KILL_RENEGADE.setBaseSpeechGroup(GONDOR);
        GONDOR_KILL_RENEGADE.setLore(LOTRLore.LoreCategory.GONDOR);
        GONDOR_KILL_RENEGADE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRenegades").setKillEntity(() -> LOTREntities.GONDOR_RENEGADE, LOTRGondorRenegadeEntity.class, 2, 6).setRewardFactor(8.0f));
        MORDOR.setLore(LOTRLore.LoreCategory.MORDOR);
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("mordor_scimitar"), 1, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("mordor_warscythe"), 1, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("mordor_battleaxe"), 1, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("mordor_axe"), 1, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapon").setCollectItem(mod("mordor_dagger"), 1, 6).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_PORKCHOP), 2, 8).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.COOKED_BEEF), 2, 8).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_mutton"), 2, 8).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_venison"), 2, 8).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BONE), 2, 8).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("mordor_helmet"), 2, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("mordor_chestplate"), 2, 5).setRewardFactor(4.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("mordor_leggings"), 2, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectArmour").setCollectItem(mod("mordor_boots"), 2, 5).setRewardFactor(3.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("orc_steel_ingot"), 3, 10).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("durnor"), 4, 12).setRewardFactor(2.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("gulduril"), 3, 6).setRewardFactor(4.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("diamond"), 1, 3).setRewardFactor(15.0f));
        MORDOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMineral").setCollectItem(mod("ruby"), 1, 3).setRewardFactor(12.0f));
        MORDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.ROHAN, 10, 40));
        MORDOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killEnemy").setKillFaction(LOTRFaction.GONDOR, 10, 40));
        MORDOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killMen").setKillEntity(() -> LOTREntities.ROHIRRIM_WARRIOR, LOTRRohirrimWarriorEntity.class, 10, 30));
        MORDOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killMen").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        MORDOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRanger").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 30));
        MORDOR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        DORWINION.setLore(LOTRLore.LoreCategory.DORWINION);
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBarrel").setCollectItem(mod("barrel"), 3, 6).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("red_grapes"), 4, 12).setRewardFactor(2.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("green_grapes"), 4, 12).setRewardFactor(2.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("raisins"), 4, 12).setRewardFactor(2.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("red_wine"), 2, 6).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("white_wine"), 2, 6).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("red_grape_juice"), 2, 6).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("green_grape_juice"), 2, 6).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("olives"), 10, 20).setRewardFactor(1.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(mod("cooked_venison"), 5, 12).setRewardFactor(1.5f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(van(Items.COOKED_RABBIT), 5, 12).setRewardFactor(1.5f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("feast").setCollectItem(van(Items.COOKED_BEEF), 5, 12).setRewardFactor(1.5f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWater").setCollectItem(van(Items.BUCKET), 3, 5).setRewardFactor(3.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWater").setCollectItem(van(Items.WATER_BUCKET), 3, 5).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectHoe").setCollectItem(van(Items.IRON_HOE), 1, 3).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectHoe").setCollectItem(mod("bronze_hoe"), 1, 3).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectHoe").setCollectItem(van(Items.STONE_HOE), 2, 6).setRewardFactor(2.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectHoe").setCollectItem(van(Items.WOODEN_HOE), 3, 8).setRewardFactor(1.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBonemeal").setCollectItem(van(Items.BONE_MEAL), 12, 40).setRewardFactor(0.25f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(van(Items.IRON_SWORD), 2, 4).setRewardFactor(3.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("iron_dagger"), 2, 6).setRewardFactor(2.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("iron_pike"), 2, 4).setRewardFactor(3.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dorwinion_helmet"), 2, 5).setRewardFactor(3.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dorwinion_chestplate"), 2, 5).setRewardFactor(4.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dorwinion_leggings"), 2, 5).setRewardFactor(3.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("dorwinion_boots"), 2, 5).setRewardFactor(3.0f));
        DORWINION.addQuest(new LOTRMiniQuestCollect.QFCollect("kineHorn").setCollectItem(mod("kine_of_araw_horn"), 1, 3).setRewardFactor(20.0f));
        DORWINION.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRabbits").setKillEntity(() -> EntityTypes.RABBIT, Rabbit.class, 10, 20));
        DORWINION.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killBirds").setKillEntity(() -> LOTREntities.BIRD, LOTRBirdEntity.class, 10, 20));
        DORWINION.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killBandit").setKillEntity(() -> LOTREntities.BANDIT, LOTRBanditEntity.class, 1, 3).setRewardFactor(8.0f));
        DORWINION.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOddmentCollector").setKillEntity(() -> LOTREntities.SCRAP_TRADER, LOTRScrapTraderEntity.class, 1, 2).setRewardFactor(15.0f));
        DORWINION.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        DORWINION_ELF.setLore(LOTRLore.LoreCategory.DORWINION);
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(van(Items.OAK_LOG), 20, 60).setRewardFactor(0.25f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(van(Items.BIRCH_LOG), 20, 60).setRewardFactor(0.25f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(mod("cypress_log"), 20, 60).setRewardFactor(0.25f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(mod("olive_log"), 20, 60).setRewardFactor(0.25f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("red_grapes"), 4, 12).setRewardFactor(2.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("green_grapes"), 4, 12).setRewardFactor(2.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("barrel"), 3, 6).setRewardFactor(4.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("red_wine"), 2, 6).setRewardFactor(4.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWine").setCollectItem(mod("white_wine"), 2, 6).setRewardFactor(4.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBarrel").setCollectItem(mod("barrel"), 3, 6).setRewardFactor(4.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGreenOak").setCollectItem(mod("green_oak_log"), 20, 40).setRewardFactor(0.5f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGreenOak").setCollectItem(mod("green_oak_leaves"), 40, 80).setRewardFactor(0.25f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGreenOak").setCollectItem(mod("green_oak_sapling"), 5, 10).setRewardFactor(1.5f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("collectGreenOak").setCollectItem(mod("green_oak_planks"), 40, 80).setRewardFactor(0.25f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("brewing").setCollectItem(mod("red_grapes"), 4, 12).setRewardFactor(2.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("brewing").setCollectItem(mod("green_grapes"), 4, 12).setRewardFactor(2.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("brewing").setCollectItem(mod("barrel"), 3, 6).setRewardFactor(4.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("brewing").setCollectItem(mod("mug"), 5, 12).setRewardFactor(1.5f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("brewing").setCollectItem(van(Items.BUCKET), 3, 5).setRewardFactor(3.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestCollect.QFCollect("brewing").setCollectItem(van(Items.WATER_BUCKET), 3, 5).setRewardFactor(4.0f));
        DORWINION_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.GUNDABAD_ORC, LOTRGundabadOrcEntity.class, 10, 30));
        DORWINION_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killOrc").setKillEntity(() -> LOTREntities.MORDOR_ORC, LOTRMordorOrcEntity.class, 10, 30));
        DORWINION_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.GUNDABAD_WARG, LOTRGundabadWargEntity.class, 10, 30));
        DORWINION_ELF.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killWarg").setKillEntity(() -> LOTREntities.MORDOR_WARG, LOTRMordorWargEntity.class, 10, 30));
        DORWINION_ELF.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        RHUN.setLore(LOTRLore.LoreCategory.RHUN);
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("pomegranate"), 4, 12).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("date"), 4, 12).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("rhun_flower_chrys_blue"), 2, 5).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("rhun_flower_chrys_orange"), 2, 5).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("rhun_flower_chrys_pink"), 2, 5).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("rhun_flower_chrys_yellow"), 2, 5).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("rhun_flower_chrys_white"), 2, 5).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringRhunThing").setCollectItem(mod("redwood_sapling"), 3, 5).setRewardFactor(3.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_scimitar"), 3, 5).setRewardFactor(3.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_spear"), 3, 5).setRewardFactor(3.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("orc_steel_ingot"), 5, 10).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_helmet"), 2, 4).setRewardFactor(4.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_chestplate"), 2, 4).setRewardFactor(4.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_leggings"), 2, 4).setRewardFactor(4.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectOrcItem").setCollectItem(mod("mordor_boots"), 2, 4).setRewardFactor(4.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("goHunting").setCollectItem(van(Items.RABBIT), 5, 10).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("goHunting").setCollectItem(mod("raw_venison"), 5, 10).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("goHunting").setCollectItem(van(Items.BEEF), 5, 10).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("goHunting").setCollectItem(van(Items.LEATHER), 8, 16).setRewardFactor(1.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("goHunting").setCollectItem(van(Items.FEATHER), 8, 16).setRewardFactor(1.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("goHunting").setCollectItem(mod("kine_of_araw_horn"), 1, 2).setRewardFactor(10.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFire").setCollectItem(mod("durnor"), 4, 8).setRewardFactor(3.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFire").setCollectItem(mod("sulfur"), 4, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFire").setCollectItem(mod("niter"), 4, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(van(Items.BREAD), 5, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(mod("olive_bread"), 5, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(van(Items.COOKED_BEEF), 2, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(van(Items.COOKED_COD), 2, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(mod("cooked_mutton"), 2, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(mod("cooked_venison"), 2, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRations").setCollectItem(mod("raisins"), 6, 12).setRewardFactor(1.5f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(van(Items.OAK_LOG), 30, 60).setRewardFactor(0.25f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(mod("redwood_log"), 30, 60).setRewardFactor(0.25f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(mod("cypress_log"), 30, 60).setRewardFactor(0.25f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(mod("rhun_brick"), 40, 100).setRewardFactor(0.2f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMaterials").setCollectItem(van(Items.COBBLESTONE), 40, 100).setRewardFactor(0.25f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectKineHorn").setCollectItem(mod("kine_of_araw_horn"), 1, 1).setRewardFactor(30.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDorwinionWine").setCollectItem(mod("red_wine"), 3, 8).setRewardFactor(3.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDorwinionWine").setCollectItem(mod("white_wine"), 3, 8).setRewardFactor(3.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringPoison").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        RHUN.addQuest(new LOTRMiniQuestCollect.QFCollect("bringPoison").setCollectItem(mod("sulfur"), 4, 8).setRewardFactor(2.0f));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killThings").setKillEntity(() -> LOTREntities.GONDOR_MAN, LOTRGondorManEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killThings").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killThings").setKillEntity(() -> LOTREntities.DALE_MAN, LOTRDaleManEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killThings").setKillEntity(() -> EntityTypes.RABBIT, Rabbit.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killThings").setKillEntity(() -> LOTREntities.DEER, LOTRDeerEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorAllies").setKillEntity(() -> LOTREntities.ROHIRRIM_WARRIOR, LOTRRohirrimWarriorEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorAllies").setKillEntity(() -> LOTREntities.DALE_SOLDIER, LOTRDaleSoldierEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorAllies").setKillEntity(() -> LOTREntities.DOL_AMROTH_SOLDIER, LOTRDolAmrothSoldierEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorAllies").setKillEntity(() -> LOTREntities.LOSSARNACH_AXEMAN, LOTRLossarnachAxemanEntity.class, 10, 30));
        RHUN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killDale").setKillFaction(LOTRFaction.DALE, 20, 40));
        RHUN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killNorthmen").setKillFaction(LOTRFaction.DALE, 10, 40));
        RHUN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killDwarves").setKillFaction(LOTRFaction.DURINS_FOLK, 10, 40));
        RHUN.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        HARNENNOR.setLore(LOTRLore.LoreCategory.HARNENNOR);
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("bringWater").setCollectItem(van(Items.WATER_BUCKET), 3, 5).setRewardFactor(5.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBlackRock").setCollectItem(mod("mordor_rock"), 30, 50).setRewardFactor(0.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDates").setCollectItem(mod("date"), 8, 15).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lemon"), 4, 12).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("orange"), 4, 12).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lime"), 4, 12).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("plum"), 4, 12).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("orangeJuice").setCollectItem(mod("orange_juice"), 2, 6).setRewardFactor(4.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("lemonLiqueur").setCollectItem(mod("lemon_liqueur"), 2, 6).setRewardFactor(4.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPoison").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRangedWeapon").setCollectItem(mod("harad_bow"), 1, 3).setRewardFactor(5.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectRangedWeapon").setCollectItem(van(Items.ARROW), 20, 40).setRewardFactor(0.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTradeGoods").setCollectItem(van(Items.LAPIS_LAZULI), 3, 8).setRewardFactor(3.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTradeGoods").setCollectItem(mod("lion_fur"), 3, 6).setRewardFactor(3.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTradeGoods").setCollectItem(mod("flame_of_harad"), 5, 15).setRewardFactor(1.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectTradeGoods").setCollectItem(mod("olives"), 10, 20).setRewardFactor(1.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMarketFood").setCollectItem(van(Items.BREAD), 5, 8).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMarketFood").setCollectItem(van(Items.COOKED_RABBIT), 5, 12).setRewardFactor(1.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMarketFood").setCollectItem(mod("cooked_venison"), 5, 12).setRewardFactor(1.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMarketFood").setCollectItem(mod("orange"), 4, 8).setRewardFactor(2.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectMarketFood").setCollectItem(mod("lemon"), 4, 8).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(van(Items.OAK_LOG), 20, 60).setRewardFactor(0.25f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(mod("cedar_log"), 20, 60).setRewardFactor(0.25f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(van(Items.OAK_PLANKS), 80, 160).setRewardFactor(0.125f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(mod("cedar_planks"), 80, 160).setRewardFactor(0.125f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(van(Items.SANDSTONE), 30, 80).setRewardFactor(0.25f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(van(Items.CHISELED_SANDSTONE), 15, 40).setRewardFactor(0.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("buildMaterials").setCollectItem(mod("thatch_reed"), 20, 40).setRewardFactor(0.5f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("specialFood").setCollectItem(mod("orange"), 4, 8).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("specialFood").setCollectItem(mod("lemon"), 4, 8).setRewardFactor(2.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("specialFood").setCollectItem(mod("mango"), 2, 4).setRewardFactor(4.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("specialFood").setCollectItem(mod("banana"), 2, 4).setRewardFactor(4.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestCollect.QFCollect("specialFood").setCollectItem(mod("cooked_lion"), 3, 6).setRewardFactor(3.0f));
        HARNENNOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("conquerGondor").setKillFaction(LOTRFaction.GONDOR, 20, 50));
        HARNENNOR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGondor").setKillFaction(LOTRFaction.GONDOR, 20, 50));
        HARNENNOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("avengeRangers").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 30));
        HARNENNOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorSoldier").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        HARNENNOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("reclaimHarondor").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        HARNENNOR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRohirrim").setKillEntity(() -> LOTREntities.ROHIRRIM_WARRIOR, LOTRRohirrimWarriorEntity.class, 10, 30));
        HARNENNOR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        NEAR_HARAD.setLore(LOTRLore.LoreCategory.SOUTHRON);
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringWater").setCollectItem(van(Items.WATER_BUCKET), 3, 5).setRewardFactor(5.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBlackRock").setCollectItem(mod("mordor_rock"), 30, 50).setRewardFactor(0.5f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDates").setCollectItem(mod("date"), 8, 15).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lemon"), 4, 12).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("orange"), 4, 12).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lime"), 4, 12).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("plum"), 4, 12).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("orangeJuice").setCollectItem(mod("orange_juice"), 2, 6).setRewardFactor(4.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("lemonLiqueur").setCollectItem(mod("lemon_liqueur"), 2, 6).setRewardFactor(4.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPoison").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("gulfSword").setCollectItem(mod("gulfen_khopesh"), 1, 1).setRewardFactor(5.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("ringTax").setCollectItem(mod("gold_ring"), 2, 5).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("ringTax").setCollectItem(mod("silver_ring"), 2, 5).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("saveFromVenom").setCollectItem(mod("pearl"), 1, 1).setRewardFactor(20.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringReedsRoof").setCollectItem(mod("dried_reeds"), 10, 20).setRewardFactor(0.5f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringReedsRoof").setCollectItem(mod("thatch_reed"), 10, 20).setRewardFactor(0.5f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringReedsRoof").setCollectItem(mod("thatch_reed_slab"), 20, 40).setRewardFactor(0.25f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("cedarWood").setCollectItem(mod("cedar_log"), 30, 60).setRewardFactor(0.25f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("cedarWood").setCollectItem(mod("cedar_planks"), 60, 120).setRewardFactor(0.125f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(van(Items.SANDSTONE), 30, 80).setRewardFactor(0.25f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(mod("near_harad_brick"), 30, 60).setRewardFactor(0.5f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(mod("near_harad_red_brick"), 30, 60).setRewardFactor(0.75f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringMeat").setCollectItem(van(Items.COOKED_RABBIT), 5, 10).setRewardFactor(1.5f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringMeat").setCollectItem(mod("cooked_venison"), 5, 10).setRewardFactor(1.5f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringMeat").setCollectItem(van(Items.COOKED_BEEF), 4, 8).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringMeat").setCollectItem(van(Items.COOKED_PORKCHOP), 4, 8).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringMeat").setCollectItem(mod("cooked_mutton"), 4, 8).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("bringMeat").setCollectItem(mod("kebab"), 4, 8).setRewardFactor(2.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("lionSteak").setCollectItem(mod("cooked_lion"), 2, 4).setRewardFactor(4.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarArmor").setCollectItem(mod("umbaric_helmet"), 1, 1).setRewardFactor(15.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarArmor").setCollectItem(mod("umbaric_chestplate"), 1, 1).setRewardFactor(15.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarArmor").setCollectItem(mod("umbaric_leggings"), 1, 1).setRewardFactor(15.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarArmor").setCollectItem(mod("umbaric_boots"), 1, 1).setRewardFactor(15.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("defendCorsair").setCollectItem(mod("umbaric_scimitar"), 1, 3).setRewardFactor(5.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("defendCorsair").setCollectItem(mod("umbaric_spear"), 1, 3).setRewardFactor(5.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("defendCorsair").setCollectItem(mod("umbaric_mace"), 1, 3).setRewardFactor(5.0f));
        NEAR_HARAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGondor").setKillFaction(LOTRFaction.GONDOR, 20, 50));
        NEAR_HARAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killForUmbar").setKillFaction(LOTRFaction.GONDOR, 10, 40));
        NEAR_HARAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killRohirrim").setKillFaction(LOTRFaction.ROHAN, 10, 30));
        NEAR_HARAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRangers").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 30));
        NEAR_HARAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("greenDemons").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 20));
        NEAR_HARAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorSoldiers").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        NEAR_HARAD.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        UMBAR.setLore(LOTRLore.LoreCategory.UMBAR);
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBlackRock").setCollectItem(mod("mordor_rock"), 30, 50).setRewardFactor(0.5f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDates").setCollectItem(mod("date"), 8, 15).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lemon"), 4, 12).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("orange"), 4, 12).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lime"), 4, 12).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("plum"), 4, 12).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("orangeJuice").setCollectItem(mod("orange_juice"), 2, 6).setRewardFactor(4.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("lemonLiqueur").setCollectItem(mod("lemon_liqueur"), 2, 6).setRewardFactor(4.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPoison").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("gulfSword").setCollectItem(mod("gulfen_khopesh"), 1, 1).setRewardFactor(5.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("ringTax").setCollectItem(mod("gold_ring"), 2, 5).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("ringTax").setCollectItem(mod("silver_ring"), 2, 5).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("saveFromVenom").setCollectItem(mod("pearl"), 1, 1).setRewardFactor(20.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("bringReedsRoof").setCollectItem(mod("dried_reeds"), 10, 20).setRewardFactor(0.5f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("bringReedsRoof").setCollectItem(mod("thatch_reed"), 10, 20).setRewardFactor(0.5f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("bringReedsRoof").setCollectItem(mod("thatch_reed_slab"), 20, 40).setRewardFactor(0.25f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("cedarWood").setCollectItem(mod("cedar_log"), 30, 60).setRewardFactor(0.25f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("cedarWood").setCollectItem(mod("cedar_planks"), 60, 120).setRewardFactor(0.125f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(van(Items.SANDSTONE), 30, 80).setRewardFactor(0.25f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(van(Items.STONE), 30, 80).setRewardFactor(0.25f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(mod("near_harad_brick"), 30, 60).setRewardFactor(0.5f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("repairWall").setCollectItem(mod("umbar_brick"), 30, 60).setRewardFactor(0.5f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("lionSteak").setCollectItem(mod("cooked_lion"), 2, 4).setRewardFactor(4.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("defendCorsair").setCollectItem(mod("umbaric_scimitar"), 1, 3).setRewardFactor(5.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("defendCorsair").setCollectItem(mod("umbaric_spear"), 1, 3).setRewardFactor(5.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("defendCorsair").setCollectItem(mod("umbaric_mace"), 1, 3).setRewardFactor(5.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(mod("gondor_sword"), 1, 2).setRewardFactor(8.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(mod("gondor_helmet"), 1, 2).setRewardFactor(8.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(mod("gondor_winged_helmet"), 1, 1).setRewardFactor(20.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(mod("gondor_chestplate"), 1, 2).setRewardFactor(8.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(mod("arnor_sword"), 1, 1).setRewardFactor(40.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(mod("arnor_helmet"), 1, 1).setRewardFactor(40.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(van(Items.IRON_INGOT), 4, 8).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(van(Items.GOLD_INGOT), 3, 6).setRewardFactor(4.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarCraft").setCollectItem(van(Items.LAVA_BUCKET), 2, 4).setRewardFactor(5.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("travelSupplies").setCollectItem(mod("kebab"), 4, 8).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("travelSupplies").setCollectItem(van(Items.COOKED_BEEF), 4, 8).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("travelSupplies").setCollectItem(mod("cooked_mutton"), 4, 8).setRewardFactor(2.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("travelSupplies").setCollectItem(mod("arak"), 3, 5).setRewardFactor(4.0f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("travelSupplies").setCollectItem(mod("waterskin"), 8, 20).setRewardFactor(0.75f));
        UMBAR.addQuest(new LOTRMiniQuestCollect.QFCollect("findOldDagger").setCollectItem(mod("old_haradric_sacrificial_dagger"), 1, 2).setRewardFactor(20.0f));
        UMBAR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGondor").setKillFaction(LOTRFaction.GONDOR, 10, 30));
        UMBAR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("revengeGondor").setKillFaction(LOTRFaction.GONDOR, 20, 40));
        UMBAR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killForAnadune").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 20, 50));
        UMBAR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRangers").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 40));
        UMBAR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killSwanKnights").setKillEntity(() -> LOTREntities.SWAN_KNIGHT, LOTRSwanKnightEntity.class, 10, 30));
        UMBAR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        CORSAIR.setLore(LOTRLore.LoreCategory.UMBAR);
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("whipMaterial").setCollectItem(van(Items.STRING), 5, 12).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("whipMaterial").setCollectItem(mod("rope"), 5, 12).setRewardFactor(1.1f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("whipMaterial").setCollectItem(van(Items.LEATHER), 10, 20).setRewardFactor(0.75f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("scurvy").setCollectItem(mod("orange"), 6, 12).setRewardFactor(1.75f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("scurvy").setCollectItem(mod("lemon"), 6, 12).setRewardFactor(1.75f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("scurvy").setCollectItem(mod("lemon"), 6, 12).setRewardFactor(1.75f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("arak"), 4, 10).setRewardFactor(2.5f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("rum"), 4, 10).setRewardFactor(2.5f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("cactus_liqueur"), 4, 10).setRewardFactor(2.5f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("carrot_wine"), 4, 10).setRewardFactor(2.5f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("banana_beer"), 4, 10).setRewardFactor(2.5f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDrink").setCollectItem(mod("corn_liquor"), 4, 10).setRewardFactor(2.5f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectChests").setCollectItem(van(Items.CHEST), 8, 16).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("collectChests").setCollectItem(mod("reed_basket"), 5, 10).setRewardFactor(2.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("poisonCaptain").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixSails").setCollectItem(van(Items.STRING), 5, 12).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixSails").setCollectItem(mod("rope"), 5, 12).setRewardFactor(1.1f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixSails").setCollectItem(van(Items.WOOL.pick(DyeColor.BLACK)), 6, 15).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixSails").setCollectItem(van(Items.WOOL.pick(DyeColor.RED)), 6, 15).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixSails").setCollectItem(van(Items.WOOL.pick(DyeColor.BROWN)), 6, 15).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixSails").setCollectItem(van(Items.WOOL.pick(DyeColor.WHITE)), 6, 15).setRewardFactor(1.0f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixShip").setCollectItem(mod("cedar_planks"), 60, 120).setRewardFactor(0.2f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixShip").setCollectItem(mod("palm_planks"), 60, 120).setRewardFactor(0.2f));
        CORSAIR.addQuest(new LOTRMiniQuestCollect.QFCollect("fixShip").setCollectItem(mod("olive_planks"), 60, 120).setRewardFactor(0.2f));
        CORSAIR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondor").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        CORSAIR.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRangers").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 20).setRewardFactor(1.5f));
        CORSAIR.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killTaurethrim").setKillFaction(LOTRFaction.TAURETHRIM, 10, 30));
        CORSAIR.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        GONDOR_RENEGADE.setLore(LOTRLore.LoreCategory.UMBAR);
        GONDOR_RENEGADE.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorSoldiers").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 3, 8));
        GONDOR_RENEGADE.forEachFactory(qf -> {
            qf.setRewardFactor(0.0f);
            qf.setHiring(50.0f);
        });
        NOMAD.setLore(LOTRLore.LoreCategory.NOMAD);
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDates").setCollectItem(mod("date"), 8, 15).setRewardFactor(2.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lemon"), 4, 12).setRewardFactor(2.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("orange"), 4, 12).setRewardFactor(2.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("lime"), 4, 12).setRewardFactor(2.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFruit").setCollectItem(mod("plum"), 4, 12).setRewardFactor(2.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("orangeJuice").setCollectItem(mod("orange_juice"), 2, 6).setRewardFactor(4.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("lemonLiqueur").setCollectItem(mod("lemon_liqueur"), 2, 6).setRewardFactor(4.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPoison").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 14), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 4), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 13), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 11), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 10), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 5), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 4), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE), 3), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("camelCarpets").setCollectItem(van(Items.CARPET.pick(DyeColor.WHITE)), 8, 15).setRewardFactor(0.75f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("waterskins").setCollectItem(mod("waterskin"), 8, 16).setRewardFactor(1.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("gulfEquipment").setCollectItem(mod("gulfen_khopesh"), 1, 2).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("gulfEquipment").setCollectItem(mod("gulfen_helmet"), 1, 2).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("gulfEquipment").setCollectItem(mod("gulfen_chestplate"), 1, 2).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarEquipment").setCollectItem(mod("umbaric_scimitar"), 1, 2).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarEquipment").setCollectItem(mod("umbaric_helmet"), 1, 2).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestCollect.QFCollect("umbarEquipment").setCollectItem(mod("umbaric_chestplate"), 1, 2).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killScorpions").setKillEntity(() -> LOTREntities.DESERT_SCORPION, LOTRDesertScorpionEntity.class, 10, 30));
        NOMAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killManyScorpions").setKillEntity(() -> LOTREntities.DESERT_SCORPION, LOTRDesertScorpionEntity.class, 40, 80));
        NOMAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killBandits").setKillEntity(() -> LOTREntities.BANDIT_HARAD, LOTRBanditHaradEntity.class, 1, 3).setRewardFactor(8.0f));
        NOMAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRangers").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 20));
        NOMAD.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        GULF_HARAD.setLore(LOTRLore.LoreCategory.GULF);
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDates").setCollectItem(mod("date"), 8, 15).setRewardFactor(2.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("orangeJuice").setCollectItem(mod("orange_juice"), 2, 6).setRewardFactor(4.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("lemonLiqueur").setCollectItem(mod("lemon_liqueur"), 2, 6).setRewardFactor(4.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectPoison").setCollectItem(mod("bottle_of_poison"), 2, 4).setRewardFactor(5.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectIronWeapon").setCollectItem(mod("umbaric_scimitar"), 2, 3).setRewardFactor(5.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectIronWeapon").setCollectItem(mod("umbaric_spear"), 2, 3).setRewardFactor(4.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectIronWeapon").setCollectItem(mod("umbaric_poleaxe"), 2, 3).setRewardFactor(6.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectIronWeapon").setCollectItem(mod("umbaric_mace"), 2, 3).setRewardFactor(6.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("findOldDagger").setCollectItem(mod("old_haradric_sacrificial_dagger"), 1, 2).setRewardFactor(20.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("morwaithStuff").setCollectItem(mod("morwaith_helmet"), 1, 1).setRewardFactor(8.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("morwaithStuff").setCollectItem(mod("morwaith_chestplate"), 1, 1).setRewardFactor(8.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("morwaithStuff").setCollectItem(mod("morwaith_leggings"), 1, 1).setRewardFactor(8.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("morwaithStuff").setCollectItem(mod("morwaith_boots"), 1, 1).setRewardFactor(8.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("easterlingStuff").setCollectItem(mod("golden_rhunic_helmet"), 1, 1).setRewardFactor(20.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("easterlingStuff").setCollectItem(mod("golden_rhunic_chestplate"), 1, 1).setRewardFactor(20.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("easterlingStuff").setCollectItem(mod("golden_rhunic_leggings"), 1, 1).setRewardFactor(20.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("easterlingStuff").setCollectItem(mod("golden_rhunic_boots"), 1, 1).setRewardFactor(20.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBones").setCollectItem(van(Items.BONE), 10, 20).setRewardFactor(1.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(van(Items.SANDSTONE), 30, 80).setRewardFactor(0.25f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("near_harad_brick"), 30, 60).setRewardFactor(0.5f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("near_harad_red_brick"), 30, 60).setRewardFactor(0.5f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("thatch_reed"), 10, 20).setRewardFactor(0.5f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("palm_log"), 30, 60).setRewardFactor(0.25f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("palm_planks"), 60, 120).setRewardFactor(0.125f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("dragon_log"), 30, 60).setRewardFactor(0.25f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("dragon_planks"), 60, 120).setRewardFactor(0.125f));
        GULF_HARAD.addQuest(new LOTRMiniQuestCollect.QFCollect("templeBuild").setCollectItem(mod("bone_block"), 5, 10).setRewardFactor(2.0f));
        GULF_HARAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killTaurethrim").setKillFaction(LOTRFaction.TAURETHRIM, 10, 30));
        GULF_HARAD.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGondor").setKillFaction(LOTRFaction.GONDOR, 20, 40));
        GULF_HARAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killScorpions").setKillEntity(() -> LOTREntities.DESERT_SCORPION, LOTRDesertScorpionEntity.class, 10, 30));
        GULF_HARAD.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRangers").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 20, 40));
        GULF_HARAD.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        MOREDAIN.setLore(LOTRLore.LoreCategory.FAR_HARAD);
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectLionFur").setCollectItem(mod("lion_fur"), 3, 6).setRewardFactor(3.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_lion"), 4, 6).setRewardFactor(3.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_zebra"), 4, 6).setRewardFactor(2.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("cooked_rhino"), 4, 6).setRewardFactor(3.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(van(Items.BREAD), 5, 8).setRewardFactor(2.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectFood").setCollectItem(mod("roast_yam"), 5, 8).setRewardFactor(2.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectHide").setCollectItem(mod("gemsbok_hide"), 4, 12).setRewardFactor(2.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBananas").setCollectItem(mod("banana"), 4, 6).setRewardFactor(4.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("morwaith_battleaxe"), 1, 4).setRewardFactor(5.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("morwaith_dagger"), 1, 4).setRewardFactor(5.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("morwaith_spear"), 1, 4).setRewardFactor(5.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("morwaith_sword"), 1, 4).setRewardFactor(5.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("morwaith_club"), 1, 4).setRewardFactor(5.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("huntRhino").setCollectItem(mod("rhino_horn"), 1, 3).setRewardFactor(8.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDates").setCollectItem(mod("date"), 3, 5).setRewardFactor(4.0f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warResources").setCollectItem(van(Items.OAK_LOG), 30, 60).setRewardFactor(0.3f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warResources").setCollectItem(van(Items.ACACIA_LOG), 30, 60).setRewardFactor(0.3f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warResources").setCollectItem(mod("gemsbok_hide"), 6, 15).setRewardFactor(1.5f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warResources").setCollectItem(van(Items.STICK), 80, 200).setRewardFactor(0.05f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("wallMaterials").setCollectItem(mod("morwaith_brick"), 40, 60).setRewardFactor(0.2f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("wallMaterials").setCollectItem(van(Items.TERRACOTTA), 20, 30).setRewardFactor(0.5f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("roofMaterials").setCollectItem(mod("thatch_thatch"), 10, 20).setRewardFactor(0.5f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("jungleWood").setCollectItem(van(Items.JUNGLE_LOG), 40, 80).setRewardFactor(0.25f));
        MOREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("jungleWood").setCollectItem(mod("mahogany_log"), 40, 80).setRewardFactor(0.25f));
        MOREDAIN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGondor").setKillFaction(LOTRFaction.GONDOR, 20, 50));
        MOREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorSoldier").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 10, 30));
        MOREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRanger").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 30));
        MOREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killCrocodile").setKillEntity(() -> LOTREntities.CROCODILE, LOTRCrocodileEntity.class, 10, 20));
        MOREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killLion").setKillEntity(() -> LOTREntities.LION, LOTRLionEntity.class, 10, 20));
        MOREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killLion").setKillEntity(() -> LOTREntities.LIONESS, LOTRLionessEntity.class, 10, 20));
        MOREDAIN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killTauredain").setKillFaction(LOTRFaction.TAURETHRIM, 20, 50));
        MOREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killTauredainBlowgunner").setKillEntity(() -> LOTREntities.TAUREDAIN_BLOWGUNNER, LOTRTauredainBlowgunnerEntity.class, 10, 30));
        MOREDAIN.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        TAUREDAIN.setLore(LOTRLore.LoreCategory.FAR_HARAD_JUNGLE);
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_sword"), 1, 4).setRewardFactor(5.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_dagger"), 1, 4).setRewardFactor(4.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("poisoned_taurethrim_dagger"), 1, 3).setRewardFactor(6.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_axe"), 1, 4).setRewardFactor(5.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_spear"), 1, 4).setRewardFactor(5.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_bludgeon"), 1, 4).setRewardFactor(5.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_battleaxe"), 1, 4).setRewardFactor(5.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWeapons").setCollectItem(mod("taurethrim_pike"), 1, 4).setRewardFactor(5.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectObsidian").setCollectItem(mod("obsidian_shard"), 10, 30).setRewardFactor(0.75f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectCocoa").setCollectItem(van(Items.COCOA_BEANS), 8, 20).setRewardFactor(1.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(van(Items.BREAD), 5, 8).setRewardFactor(2.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("banana_bread"), 5, 8).setRewardFactor(2.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("corn_bread"), 5, 8).setRewardFactor(2.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("banana"), 4, 6).setRewardFactor(4.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("mango"), 4, 6).setRewardFactor(4.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(van(Items.MELON_SLICE), 10, 20).setRewardFactor(0.75f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("melon_soup"), 3, 8).setRewardFactor(2.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("corn"), 6, 12).setRewardFactor(1.5f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("warriorFood").setCollectItem(mod("cooked_corn"), 5, 10).setRewardFactor(2.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDarts").setCollectItem(mod("taurethrim_dart"), 20, 40).setRewardFactor(0.5f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectDarts").setCollectItem(mod("poisoned_taurethrim_dart"), 10, 20).setRewardFactor(1.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("collectBanners").setCollectItem(mod("tauredain_banner"), 5, 15).setRewardFactor(1.5f));
        TAUREDAIN.addQuest(new LOTRMiniQuestCollect.QFCollect("amulet").setCollectItem(mod("taurethrim_amulet"), 1, 4).setRewardFactor(20.0f));
        TAUREDAIN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killMoredain").setKillFaction(LOTRFaction.MORWAITH, 20, 50));
        TAUREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killMoredainWarrior").setKillEntity(() -> LOTREntities.MOREDAIN_WARRIOR, LOTRMoredainWarriorEntity.class, 10, 30));
        TAUREDAIN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killHalfTrolls").setKillFaction(LOTRFaction.HALF_TROLL, 10, 40));
        TAUREDAIN.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killNearHaradrim").setKillFaction(LOTRFaction.NEAR_HARAD, 20, 50));
        TAUREDAIN.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killNearHaradWarrior").setKillEntity(() -> LOTREntities.NEAR_HARADRIM_WARRIOR, LOTRNearHaradrimWarriorEntity.class, 10, 30));
        TAUREDAIN.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
        HALF_TROLL.setLore(LOTRLore.LoreCategory.HALF_TROLL);
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_scimitar"), 2, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_mace"), 2, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_pike"), 2, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_dagger"), 2, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_battleaxe"), 2, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_helmet"), 1, 4).setRewardFactor(4.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_chestplate"), 1, 4).setRewardFactor(5.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_leggings"), 1, 4).setRewardFactor(4.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectEquipment").setCollectItem(mod("half_troll_boots"), 1, 4).setRewardFactor(4.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("flesh").setCollectItem(mod("raw_lion"), 2, 6).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("flesh").setCollectItem(mod("raw_zebra"), 2, 6).setRewardFactor(2.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("flesh").setCollectItem(mod("raw_rhino"), 2, 6).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("flesh").setCollectItem(van(Items.ROTTEN_FLESH), 3, 8).setRewardFactor(2.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(van(Items.OAK_LOG), 30, 60).setRewardFactor(0.3f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(van(Items.ACACIA_LOG), 30, 60).setRewardFactor(0.3f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("collectWood").setCollectItem(mod("baobab_log"), 20, 40).setRewardFactor(0.5f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("manTrophy").setCollectItem(mod("gondor_sword"), 1, 1).setRewardFactor(20.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("manTrophy").setCollectItem(mod("gondor_bow"), 1, 1).setRewardFactor(20.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("manTrophy").setCollectItem(mod("beacon_of_gondor"), 1, 1).setRewardFactor(20.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("resources").setCollectItem(van(Items.OAK_LOG), 30, 80).setRewardFactor(0.25f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("resources").setCollectItem(van(Items.COAL), 10, 30).setRewardFactor(0.5f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("resources").setCollectItem(van(Items.COBBLESTONE), 30, 80).setRewardFactor(0.25f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("resources").setCollectItem(mod("gemsbok_hide"), 10, 30).setRewardFactor(0.5f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("huntItems").setCollectItem(mod("raw_lion"), 4, 8).setRewardFactor(2.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("huntItems").setCollectItem(mod("rhino_horn"), 3, 6).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("huntItems").setCollectItem(mod("gemsbok_hide"), 4, 10).setRewardFactor(2.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("huntItems").setCollectItem(mod("gemsbok_horn"), 3, 6).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("mordorItems").setCollectItem(mod("orc_steel_ingot"), 4, 8).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("mordorItems").setCollectItem(mod("mordor_scimitar"), 3, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("mordorItems").setCollectItem(mod("mordor_battleaxe"), 3, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("mordorItems").setCollectItem(mod("mordor_warhammer"), 3, 5).setRewardFactor(3.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("tribeItem").setCollectItem(mod("taurethrim_sword"), 1, 1).setRewardFactor(20.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestCollect.QFCollect("tribeItem").setCollectItem(mod("taurethrim_dagger"), 1, 1).setRewardFactor(20.0f));
        HALF_TROLL.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killGondor").setKillFaction(LOTRFaction.GONDOR, 20, 50));
        HALF_TROLL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killGondorSoldier").setKillEntity(() -> LOTREntities.GONDOR_SOLDIER, LOTRGondorSoldierEntity.class, 20, 40));
        HALF_TROLL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRanger").setKillEntity(() -> LOTREntities.RANGER_ITHILIEN, LOTRRangerIthilienEntity.class, 10, 30));
        HALF_TROLL.addQuest(new LOTRMiniQuestKillEntity.QFKillEntity("killRohirrim").setKillEntity(() -> LOTREntities.ROHIRRIM_WARRIOR, LOTRRohirrimWarriorEntity.class, 10, 30));
        HALF_TROLL.addQuest(new LOTRMiniQuestKillFaction.QFKillFaction("killTauredain").setKillFaction(LOTRFaction.TAURETHRIM, 20, 50));
        HALF_TROLL.addQuest(new LOTRMiniQuestBounty.QFBounty("bounty"));
    }

    public static @Nullable LOTRMiniQuestFactory forName(String name) {
        for (LOTRMiniQuestFactory group : values()) {
            if (group.baseName.equals(name)) {
                return group;
            }
        }
        return null;
    }

    private static int getQuestClassWeight(Class<? extends LOTRMiniQuest> questClass) {
        Integer i = QUEST_CLASS_WEIGHTS.get(questClass);
        if (i == null) {
            throw new IllegalStateException("Encountered a registered quest class " + questClass + " which is not assigned a weight");
        }
        return i;
    }

    private static void registerQuestClass(Class<? extends LOTRMiniQuest> questClass, int weight) {
        QUEST_CLASS_WEIGHTS.put(questClass, weight);
    }

    /** addQuest: filed under its kind -- its own class, or the registered kind it is one of. */
    private void addQuest(LOTRMiniQuest.QuestFactoryBase<?> factory) {
        Class<?> questClass = factory.getQuestClass();
        Class<? extends LOTRMiniQuest> registryClass = null;
        for (Class<? extends LOTRMiniQuest> c : QUEST_CLASS_WEIGHTS.keySet()) {
            if (questClass.equals(c)) {
                registryClass = c;
                break;
            }
        }
        if (registryClass == null) {
            for (Class<? extends LOTRMiniQuest> c : QUEST_CLASS_WEIGHTS.keySet()) {
                if (c.isAssignableFrom(questClass)) {
                    registryClass = c;
                    break;
                }
            }
        }
        if (registryClass == null) {
            throw new IllegalArgumentException("Could not find registered quest class for " + questClass);
        }
        factory.questFactoryGroup = this;
        this.questFactories.computeIfAbsent(registryClass, k -> new ArrayList<>()).add(factory);
    }

    private void forEachFactory(Consumer<LOTRMiniQuest.QuestFactoryBase<?>> action) {
        this.questFactories.values().forEach(list -> list.forEach(action));
    }

    public LOTRFaction checkAlignmentRewardFaction(LOTRFaction fac) {
        return this.alignmentRewardOverride != null ? this.alignmentRewardOverride : fac;
    }

    /** createQuest: a kind by weight, then one of its quests; none if that one cannot be set now. */
    public @Nullable LOTRMiniQuest createQuest(LOTRNPCEntity npc) {
        RandomSource rand = npc.getRandom();
        int totalWeight = 0;
        for (Class<? extends LOTRMiniQuest> c : this.questFactories.keySet()) {
            totalWeight += getQuestClassWeight(c);
        }
        if (totalWeight <= 0) {
            LOTRMod.LOGGER.warn("LOTR: No quests registered for {}!", this.baseName);
            return null;
        }
        int i = rand.nextInt(totalWeight);
        List<LOTRMiniQuest.QuestFactoryBase<?>> chosenFactoryList = null;
        for (Map.Entry<Class<? extends LOTRMiniQuest>, List<LOTRMiniQuest.QuestFactoryBase<?>>> entry : this.questFactories.entrySet()) {
            chosenFactoryList = entry.getValue();
            i -= getQuestClassWeight(entry.getKey());
            if (i < 0) {
                break;
            }
        }
        LOTRMiniQuest.QuestFactoryBase<?> factory = chosenFactoryList.get(rand.nextInt(chosenFactoryList.size()));
        LOTRMiniQuest quest = factory.createQuest(npc, rand);
        if (quest != null) {
            quest.questGroup = this;
        }
        return quest;
    }

    public String getBaseName() {
        return this.baseName;
    }

    public LOTRMiniQuestFactory getBaseSpeechGroup() {
        return this.baseSpeechGroup != null ? this.baseSpeechGroup : this;
    }

    private void setBaseSpeechGroup(LOTRMiniQuestFactory qf) {
        this.baseSpeechGroup = qf;
    }

    public List<LOTRLore.LoreCategory> getLoreCategories() {
        return this.loreCategories;
    }

    public boolean isNoAlignRewardForEnemy() {
        return this.noAlignRewardForEnemy;
    }

    private LOTRMiniQuestFactory setAlignmentRewardOverride(LOTRFaction fac) {
        this.alignmentRewardOverride = fac;
        return this;
    }

    private void setLore(LOTRLore.LoreCategory... categories) {
        this.loreCategories = List.of(categories);
    }

    private LOTRMiniQuestFactory setNoAlignRewardForEnemy() {
        this.noAlignRewardForEnemy = true;
        return this;
    }
}
