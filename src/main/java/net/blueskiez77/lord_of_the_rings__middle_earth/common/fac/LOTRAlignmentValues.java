package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.minecraft.util.Mth;

/**
 * Faithful port of the CORE of 1.7.10 lotr.common.fac.LOTRAlignmentValues.
 *
 * Ported now (needed by the alignment-change logic):
 *  - MAX_ALIGNMENT cap
 *  - AlignmentBonus: the "source" object describing an alignment change
 *    (how much, whether it's a kill, civilian kill, or hired-unit kill)
 *  - scalePenalty: penalties grow the higher your standing (so falling from
 *    grace hurts more the higher you'd climbed)
 *  - a few named bonuses used by non-entity gameplay (marriage, cutting a
 *    Fangorn tree, killing a Rohan horse, pickpocketing)
 *
 * DEFERRED (large per-NPC value table + chat plumbing), re-add when entities
 * and messaging are ported:
 *  - Bonuses: the ~250-constant table of alignment gained per NPC type killed
 *  - formatAlignForDisplay / parseDisplayedAlign (DecimalFormat helpers)
 *  - notifyAlignmentNotHighEnough / notifyMiniQuestsNeeded (need ICommandSender)
 */
public final class LOTRAlignmentValues {

    public static final float MAX_ALIGNMENT = 10000.0f;

    public static final AlignmentBonus MARRIAGE_BONUS = new AlignmentBonus(5.0f, "lotr.alignment.marriage");
    public static final AlignmentBonus FANGORN_TREE_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.cutFangornTree");
    public static final AlignmentBonus ROHAN_HORSE_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.killRohanHorse");
    public static final AlignmentBonus VINEYARD_STEAL_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.vineyardSteal");
    public static final AlignmentBonus PICKPOCKET_PENALTY = new AlignmentBonus(-1.0f, "lotr.alignment.pickpocket");

    private LOTRAlignmentValues() {
    }

    public static AlignmentBonus createPledgePenalty(float alignPenalty) {
        AlignmentBonus penalty = new AlignmentBonus(-Math.abs(alignPenalty), "lotr.alignment.pledgeBroken");
        return penalty;
    }

    /**
     * Describes a single alignment change and its nature. The nature flags
     * drive the relation-graph propagation in LOTRPlayerAlignments.addAlignment.
     */
    public static class AlignmentBonus {
        public float bonus;
        public String name;
        public boolean needsTranslation = true;
        public boolean isKill;
        public boolean killByHiredUnit;
        public boolean isCivilianKill;

        public AlignmentBonus(float f, String s) {
            bonus = f;
            name = s;
        }

        public AlignmentBonus setKill() {
            isKill = true;
            return this;
        }

        public AlignmentBonus setCivilianKill() {
            isKill = true;
            isCivilianKill = true;
            return this;
        }

        public AlignmentBonus setKillByHiredUnit() {
            killByHiredUnit = true;
            return this;
        }

        /**
         * Penalties scale up with how much alignment you have with the faction
         * you're offending: at high standing, a betrayal costs proportionally
         * more (up to 20x). Only applies to negative deltas at positive standing.
         */
        public static float scalePenalty(float penalty, float alignment) {
            if (alignment > 0.0f && penalty < 0.0f) {
                float factor = alignment / 50.0f;
                factor = Mth.clamp(factor, 1.0f, 20.0f);
                penalty *= factor;
            }
            return penalty;
        }
    }
}