package net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCraftingTableBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCrossbowBoltItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's achievements: the ones each player has earned (saved as the original's
 * "Achievements", category and number, and kept through death), told to the player as they change;
 * earning one shows it on the player's screen and in everyone's chat, so long as they may earn it.
 * And the checks run on each player every tick: carrying a pouch, ten kinds of the mod's crafting
 * tables, 128 crossbow bolts, a hundred hired units close by, a full set of one armour, and having
 * entered ten, twenty, thirty, forty and fifty lands.
 *
 * <p>NOT ported yet: entering Middle-earth and each land in it, and climbing the Misty Mountains
 * (with the dimension and its biomes, D10); entering Utumno and its levels, and its armour (D15);
 * the siege mode that held achievements back, never set by the mod itself.
 */
public final class LOTRPlayerAchievements {

    private static final Codec<Ref> REF_CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("Category").forGetter(Ref::category),
            Codec.INT.fieldOf("ID").forGetter(Ref::id)).apply(i, Ref::new));

    /** The original's list of category and number; one that no longer exists is dropped as it loads. */
    private static final Codec<List<LOTRAchievement>> LIST_CODEC = REF_CODEC.listOf().xmap(
            refs -> refs.stream().map(Ref::achievement).filter(java.util.Objects::nonNull).toList(),
            list -> list.stream().map(ach -> new Ref(ach.category.name(), ach.ID)).toList());

    private static final StreamCodec<RegistryFriendlyByteBuf, Ref> REF_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Ref::category, ByteBufCodecs.VAR_INT, Ref::id, Ref::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, List<LOTRAchievement>> LIST_STREAM_CODEC =
            REF_STREAM_CODEC.apply(ByteBufCodecs.list()).map(refs -> refs.stream().map(Ref::achievement).filter(java.util.Objects::nonNull).toList(),
                    list -> list.stream().map(ach -> new Ref(ach.category.name(), ach.ID)).toList());

    public static final AttachmentType<List<LOTRAchievement>> ACHIEVEMENTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "achievements"),
            builder -> builder.initializer(List::of).persistent(LIST_CODEC).copyOnDeath()
                    .syncWith(LIST_STREAM_CODEC, AttachmentSyncPredicate.targetOnly()));

    private record Ref(String category, int id) {
        @Nullable LOTRAchievement achievement() {
            return LOTRAchievement.achievementForCategoryAndID(LOTRAchievement.Category.forName(this.category), this.id);
        }
    }

    /** LOTRPacketAchievement with display: an achievement just earned, to be shown. */
    public record EarnedPayload(String category, int id) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<EarnedPayload> TYPE = new CustomPacketPayload.Type<>(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "achievement_earned"));
        public static final StreamCodec<RegistryFriendlyByteBuf, EarnedPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, EarnedPayload::category, ByteBufCodecs.VAR_INT, EarnedPayload::id, EarnedPayload::new);

        public @Nullable LOTRAchievement achievement() {
            return LOTRAchievement.achievementForCategoryAndID(LOTRAchievement.Category.forName(this.category), this.id);
        }

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** Each armour item's 1.7.10 material, from lotr/legacy_armor_materials.tsv. */
    private static final Map<Identifier, String> ARMOR_MATERIALS = new HashMap<>();
    /** The full-set achievements by material. */
    private static final Map<String, Supplier<LOTRAchievement>> FULL_SET = new HashMap<>();

    private LOTRPlayerAchievements() {
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(EarnedPayload.TYPE, EarnedPayload.STREAM_CODEC);
        loadArmorMaterials();
        fullSets();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                runAchievementChecks(player);
            }
        });
    }

    // ---------------------------------------------------------------- the list

    public static List<LOTRAchievement> getAchievements(Player player) {
        return player.getAttachedOrCreate(ACHIEVEMENTS);
    }

    public static boolean hasAchievement(Player player, @Nullable LOTRAchievement achievement) {
        return achievement != null && getAchievements(player).contains(achievement);
    }

    /** getEarnedAchievements: those of the dimension that the player may still count. */
    public static List<LOTRAchievement> getEarnedAchievements(Player player, LOTRDimension dimension) {
        List<LOTRAchievement> earned = new ArrayList<>();
        for (LOTRAchievement achievement : getAchievements(player)) {
            if (achievement.getDimension() == dimension && achievement.canPlayerEarn(player)) {
                earned.add(achievement);
            }
        }
        return earned;
    }

    public static int countEarned(Player player) {
        return getEarnedAchievements(player, LOTRDimension.MIDDLE_EARTH).size();
    }

    /**
     * addAchievement: earned once and kept; shown, and told in chat, only if the player may earn it
     * now. Each one reached by entering a land also counts towards the travel achievements.
     */
    public static void addAchievement(Player player, @Nullable LOTRAchievement achievement) {
        if (achievement == null || !(player instanceof ServerPlayer serverPlayer) || hasAchievement(player, achievement)) {
            return;
        }
        List<LOTRAchievement> list = new ArrayList<>(getAchievements(player));
        list.add(achievement);
        player.setAttached(ACHIEVEMENTS, List.copyOf(list));
        if (!achievement.canPlayerEarn(player)) {
            return;
        }
        ServerPlayNetworking.send(serverPlayer, new EarnedPayload(achievement.category.name(), achievement.ID));
        achievement.broadcastEarning(player, serverPlayer.level().getServer());
        int biomes = 0;
        for (LOTRAchievement earned : getEarnedAchievements(player, LOTRDimension.MIDDLE_EARTH)) {
            if (earned.isBiomeAchievement) {
                ++biomes;
            }
        }
        if (biomes >= 10) {
            addAchievement(player, LOTRAchievement.TRAVEL10);
        }
        if (biomes >= 20) {
            addAchievement(player, LOTRAchievement.TRAVEL20);
        }
        if (biomes >= 30) {
            addAchievement(player, LOTRAchievement.TRAVEL30);
        }
        if (biomes >= 40) {
            addAchievement(player, LOTRAchievement.TRAVEL40);
        }
        if (biomes >= 50) {
            addAchievement(player, LOTRAchievement.TRAVEL50);
        }
    }

    public static void removeAchievement(Player player, LOTRAchievement achievement) {
        if (hasAchievement(player, achievement)) {
            List<LOTRAchievement> list = new ArrayList<>(getAchievements(player));
            list.remove(achievement);
            player.setAttached(ACHIEVEMENTS, List.copyOf(list));
        }
    }

    public static void clearAchievements(Player player) {
        player.setAttached(ACHIEVEMENTS, List.of());
    }

    // ---------------------------------------------------------------- the checks

    private static void loadArmorMaterials() {
        try (InputStream in = LOTRPlayerAchievements.class.getResourceAsStream("/lotr/legacy_armor_materials.tsv")) {
            if (in == null) {
                throw new IllegalStateException("lotr/legacy_armor_materials.tsv is missing");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] cols = line.split("\t");
                ARMOR_MATERIALS.put(Identifier.parse(cols[0]), cols[1]);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read lotr/legacy_armor_materials.tsv", e);
        }
    }

    /** isPlayerWearingFull: all four pieces of one of the mod's armours. */
    private static @Nullable String fullArmorMaterial(Player player) {
        String full = null;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}) {
            ItemStack stack = player.getItemBySlot(slot);
            String material = stack.isEmpty() ? null : ARMOR_MATERIALS.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if (material == null || full != null && !full.equals(material)) {
                return null;
            }
            full = material;
        }
        return full;
    }

    /** runAchievementChecks. */
    private static void runAchievementChecks(ServerPlayer player) {
        Set<Block> tables = new HashSet<>();
        int crossbowBolts = 0;
        boolean pouch = false;
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (LOTRPouchItem.isPouch(stack)) {
                pouch = true;
            }
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof LOTRCraftingTableBlock) {
                tables.add(blockItem.getBlock());
            }
            if (stack.getItem() instanceof LOTRCrossbowBoltItem) {
                crossbowBolts += stack.getCount();
            }
        }
        if (pouch) {
            addAchievement(player, LOTRAchievement.GET_POUCH);
        }
        if (tables.size() >= 10) {
            addAchievement(player, LOTRAchievement.COLLECT_CRAFTING_TABLES);
        }
        if (crossbowBolts >= 128) {
            addAchievement(player, LOTRAchievement.COLLECT_CROSSBOW_BOLTS);
        }
        if (!hasAchievement(player, LOTRAchievement.HUNDREDS) && player.tickCount % 20 == 0) {
            int hiredUnits = player.level().getEntitiesOfClass(LOTRNPCEntity.class, player.getBoundingBox().inflate(64.0),
                    npc -> npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == player).size();
            if (hiredUnits >= 100) {
                addAchievement(player, LOTRAchievement.HUNDREDS);
            }
        }
        String material = fullArmorMaterial(player);
        if (material != null) {
            Supplier<LOTRAchievement> full = FULL_SET.get(material);
            if (full != null) {
                addAchievement(player, full.get());
            }
        }
    }

    private static void fullSets() {
        FULL_SET.put("MITHRIL", () -> LOTRAchievement.WEAR_FULL_MITHRIL);
        FULL_SET.put("FUR", () -> LOTRAchievement.WEAR_FULL_FUR);
        FULL_SET.put("BLUE_DWARVEN", () -> LOTRAchievement.WEAR_FULL_BLUE_DWARVEN);
        FULL_SET.put("HIGH_ELVEN", () -> LOTRAchievement.WEAR_FULL_HIGH_ELVEN);
        FULL_SET.put("GONDOLIN", () -> LOTRAchievement.WEAR_FULL_GONDOLIN);
        FULL_SET.put("GALVORN", () -> LOTRAchievement.WEAR_FULL_GALVORN);
        FULL_SET.put("RANGER", () -> LOTRAchievement.WEAR_FULL_RANGER);
        FULL_SET.put("GUNDABAD_URUK", () -> LOTRAchievement.WEAR_FULL_GUNDABAD_URUK);
        FULL_SET.put("ARNOR", () -> LOTRAchievement.WEAR_FULL_ARNOR);
        FULL_SET.put("RIVENDELL", () -> LOTRAchievement.WEAR_FULL_RIVENDELL);
        FULL_SET.put("ANGMAR", () -> LOTRAchievement.WEAR_FULL_ANGMAR);
        FULL_SET.put("WOOD_ELVEN_SCOUT", () -> LOTRAchievement.WEAR_FULL_WOOD_ELVEN_SCOUT);
        FULL_SET.put("WOOD_ELVEN", () -> LOTRAchievement.WEAR_FULL_WOOD_ELVEN);
        FULL_SET.put("DOL_GULDUR", () -> LOTRAchievement.WEAR_FULL_DOL_GULDUR);
        FULL_SET.put("DALE", () -> LOTRAchievement.WEAR_FULL_DALE);
        FULL_SET.put("DWARVEN", () -> LOTRAchievement.WEAR_FULL_DWARVEN);
        FULL_SET.put("GALADHRIM", () -> LOTRAchievement.WEAR_FULL_ELVEN);
        FULL_SET.put("HITHLAIN", () -> LOTRAchievement.WEAR_FULL_HITHLAIN);
        FULL_SET.put("URUK", () -> LOTRAchievement.WEAR_FULL_URUK);
        FULL_SET.put("ROHAN", () -> LOTRAchievement.WEAR_FULL_ROHIRRIC);
        FULL_SET.put("ROHAN_MARSHAL", () -> LOTRAchievement.WEAR_FULL_ROHIRRIC_MARSHAL);
        FULL_SET.put("DUNLENDING", () -> LOTRAchievement.WEAR_FULL_DUNLENDING);
        FULL_SET.put("GONDOR", () -> LOTRAchievement.WEAR_FULL_GONDORIAN);
        FULL_SET.put("DOL_AMROTH", () -> LOTRAchievement.WEAR_FULL_DOL_AMROTH);
        FULL_SET.put("RANGER_ITHILIEN", () -> LOTRAchievement.WEAR_FULL_RANGER_ITHILIEN);
        FULL_SET.put("LOSSARNACH", () -> LOTRAchievement.WEAR_FULL_LOSSARNACH);
        FULL_SET.put("PELARGIR", () -> LOTRAchievement.WEAR_FULL_PELARGIR);
        FULL_SET.put("PINNATH_GELIN", () -> LOTRAchievement.WEAR_FULL_PINNATH_GELIN);
        FULL_SET.put("BLACKROOT", () -> LOTRAchievement.WEAR_FULL_BLACKROOT);
        FULL_SET.put("LAMEDON", () -> LOTRAchievement.WEAR_FULL_LAMEDON);
        FULL_SET.put("MORDOR", () -> LOTRAchievement.WEAR_FULL_ORC);
        FULL_SET.put("MORGUL", () -> LOTRAchievement.WEAR_FULL_MORGUL);
        FULL_SET.put("BLACK_URUK", () -> LOTRAchievement.WEAR_FULL_BLACK_URUK);
        FULL_SET.put("DORWINION", () -> LOTRAchievement.WEAR_FULL_DORWINION);
        FULL_SET.put("DORWINION_ELF", () -> LOTRAchievement.WEAR_FULL_DORWINION_ELF);
        FULL_SET.put("RHUN", () -> LOTRAchievement.WEAR_FULL_RHUN);
        FULL_SET.put("RHUN_GOLD", () -> LOTRAchievement.WEAR_FULL_RHUN_GOLD);
        FULL_SET.put("NEAR_HARAD", () -> LOTRAchievement.WEAR_FULL_NEAR_HARAD);
        FULL_SET.put("GULF_HARAD", () -> LOTRAchievement.WEAR_FULL_GULF_HARAD);
        FULL_SET.put("CORSAIR", () -> LOTRAchievement.WEAR_FULL_CORSAIR);
        FULL_SET.put("UMBAR", () -> LOTRAchievement.WEAR_FULL_UMBAR);
        FULL_SET.put("HARNEDOR", () -> LOTRAchievement.WEAR_FULL_HARNEDOR);
        FULL_SET.put("HARAD_NOMAD", () -> LOTRAchievement.WEAR_FULL_NOMAD);
        FULL_SET.put("BLACK_NUMENOREAN", () -> LOTRAchievement.WEAR_FULL_BLACK_NUMENOREAN);
        FULL_SET.put("MOREDAIN", () -> LOTRAchievement.WEAR_FULL_MOREDAIN);
        FULL_SET.put("TAUREDAIN", () -> LOTRAchievement.WEAR_FULL_TAUREDAIN);
        FULL_SET.put("TAUREDAIN_GOLD", () -> LOTRAchievement.WEAR_FULL_TAURETHRIM_GOLD);
        FULL_SET.put("HALF_TROLL", () -> LOTRAchievement.WEAR_FULL_HALF_TROLL);
        FULL_SET.put("UTUMNO", () -> LOTRAchievement.WEAR_FULL_UTUMNO);
    }
}
