package net.blueskiez77.lord_of_the_rings__middle_earth.common.shield;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRShields: the shields a player may bear -- on their back, on their arm with a weapon in hand,
 * or as the face of a vanilla shield they carry -- and those NPCs bear. A faction's shield is worn
 * at +1000 with it; the rest are won -- by 25, 50, 100 or 200 achievements, or by the
 * gainHighAlcoholTolerance, killMountainTrollChieftain and killMallornEnt achievements.
 *
 * <p>Not ported (user): the original's exclusive shields, held by players its authors named.
 */
public enum LOTRShields {
    ALIGNMENT_BREE(LOTRFaction.BREE),
    ALIGNMENT_RANGER(LOTRFaction.RANGER_NORTH),
    ALIGNMENT_BLUE_MOUNTAINS(LOTRFaction.BLUE_MOUNTAINS),
    ALIGNMENT_HIGH_ELF(LOTRFaction.HIGH_ELF),
    ALIGNMENT_RIVENDELL(LOTRFaction.HIGH_ELF),
    ALIGNMENT_GUNDABAD(LOTRFaction.GUNDABAD),
    ALIGNMENT_ANGMAR(LOTRFaction.ANGMAR),
    ALIGNMENT_WOOD_ELF(LOTRFaction.WOOD_ELF),
    ALIGNMENT_DOL_GULDUR(LOTRFaction.DOL_GULDUR),
    ALIGNMENT_DALE(LOTRFaction.DALE),
    ALIGNMENT_ESGAROTH(LOTRFaction.DALE),
    ALIGNMENT_DWARF(LOTRFaction.DURINS_FOLK),
    ALIGNMENT_GALADHRIM(LOTRFaction.LOTHLORIEN),
    ALIGNMENT_DUNLAND(LOTRFaction.DUNLAND),
    ALIGNMENT_URUK_HAI(LOTRFaction.ISENGARD),
    ALIGNMENT_ROHAN(LOTRFaction.ROHAN),
    ALIGNMENT_GONDOR(LOTRFaction.GONDOR),
    ALIGNMENT_DOL_AMROTH(LOTRFaction.GONDOR),
    ALIGNMENT_LOSSARNACH(LOTRFaction.GONDOR),
    ALIGNMENT_LEBENNIN(LOTRFaction.GONDOR),
    ALIGNMENT_PELARGIR(LOTRFaction.GONDOR),
    ALIGNMENT_BLACKROOT_VALE(LOTRFaction.GONDOR),
    ALIGNMENT_PINNATH_GELIN(LOTRFaction.GONDOR),
    ALIGNMENT_LAMEDON(LOTRFaction.GONDOR),
    ALIGNMENT_MORDOR(LOTRFaction.MORDOR),
    ALIGNMENT_MINAS_MORGUL(LOTRFaction.MORDOR),
    ALIGNMENT_BLACK_URUK(LOTRFaction.MORDOR),
    ALIGNMENT_DORWINION(LOTRFaction.DORWINION),
    ALIGNMENT_DORWINION_ELF(LOTRFaction.DORWINION),
    ALIGNMENT_RHUN(LOTRFaction.RHUDEL),
    ALIGNMENT_HARNEDOR(LOTRFaction.NEAR_HARAD),
    ALIGNMENT_NEAR_HARAD(LOTRFaction.NEAR_HARAD),
    ALIGNMENT_UMBAR(LOTRFaction.NEAR_HARAD),
    ALIGNMENT_CORSAIR(LOTRFaction.NEAR_HARAD),
    ALIGNMENT_GULF(LOTRFaction.NEAR_HARAD),
    ALIGNMENT_MOREDAIN(LOTRFaction.MORWAITH),
    ALIGNMENT_TAUREDAIN(LOTRFaction.TAURETHRIM),
    ALIGNMENT_HALF_TROLL(LOTRFaction.HALF_TROLL),
    ACHIEVEMENT_BRONZE,
    ACHIEVEMENT_SILVER,
    ACHIEVEMENT_GOLD,
    ACHIEVEMENT_MITHRIL,
    ALCOHOLIC,
    DEFEAT_MTC,
    DEFEAT_MALLORN_ENT;

    public final ShieldType shieldType;
    public final int shieldID;
    public final Identifier shieldTexture;
    public final @Nullable LOTRFaction alignmentFaction;

    LOTRShields() {
        this(ShieldType.ACHIEVABLE, null);
    }

    LOTRShields(LOTRFaction faction) {
        this(ShieldType.ALIGNMENT, faction);
    }

    LOTRShields(ShieldType type, @Nullable LOTRFaction faction) {
        this.shieldType = type;
        this.shieldID = type.list.size();
        type.list.add(this);
        this.shieldTexture = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                "textures/entity/shield/" + name().toLowerCase(Locale.ROOT) + ".png");
        this.alignmentFaction = faction;
    }

    public static @Nullable LOTRShields shieldForName(String shieldName) {
        for (LOTRShields shield : values()) {
            if (shield.name().equals(shieldName)) {
                return shield;
            }
        }
        return null;
    }

    public boolean canDisplay(Player player) {
        return true;
    }

    public boolean canPlayerWear(Player player) {
        if (this.shieldType == ShieldType.ALIGNMENT) {
            return LOTRPlayerAlignments.getAlignment(player, this.alignmentFaction) >= 1000.0f;
        }
        return switch (this) {
            case ACHIEVEMENT_BRONZE -> LOTRPlayerAchievements.countEarned(player) >= 25;
            case ACHIEVEMENT_SILVER -> LOTRPlayerAchievements.countEarned(player) >= 50;
            case ACHIEVEMENT_GOLD -> LOTRPlayerAchievements.countEarned(player) >= 100;
            case ACHIEVEMENT_MITHRIL -> LOTRPlayerAchievements.countEarned(player) >= 200;
            case ALCOHOLIC -> LOTRPlayerAchievements.hasAchievement(player, LOTRAchievement.GAIN_HIGH_ALCOHOL_TOLERANCE);
            case DEFEAT_MTC -> LOTRPlayerAchievements.hasAchievement(player, LOTRAchievement.KILL_MOUNTAIN_TROLL_CHIEFTAIN);
            case DEFEAT_MALLORN_ENT -> LOTRPlayerAchievements.hasAchievement(player, LOTRAchievement.KILL_MALLORN_ENT);
            default -> false;
        };
    }

    public Component getShieldDesc() {
        return Component.translatable("lotr.shields." + name() + ".desc");
    }

    public Component getShieldName() {
        return Component.translatable("lotr.shields." + name() + ".name");
    }

    public Identifier getTexture() {
        return this.shieldTexture;
    }

    public enum ShieldType {
        ALIGNMENT, ACHIEVABLE;

        public final List<LOTRShields> list = new ArrayList<>();

        public Component getDisplayName() {
            return Component.translatable("lotr.shields.category." + name());
        }
    }
}
