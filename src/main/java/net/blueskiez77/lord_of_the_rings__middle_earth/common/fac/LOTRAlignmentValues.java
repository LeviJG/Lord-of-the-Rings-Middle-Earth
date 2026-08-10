package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import net.minecraft.util.Mth;

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