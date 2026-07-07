package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

/**
 * Faithful port of the rank-window math from 1.7.10
 * LOTRTickHandlerClient.renderAlignmentBar (the first ~55 lines).
 *
 * Given a player's alignment with a faction, computes the window
 * [alignMin, alignMax] the bar currently spans (current rank threshold up to
 * the next rank), the ranks at each end, and 0..1 progress across that window.
 * Pure math, no rendering dependency, so it lives in the common source set.
 * The eventual textured bar can reuse the exact same values.
 */
public final class LOTRAlignmentBar {

    public final float alignMin;
    public final float alignMax;
    public final LOTRFactionRank rankMin;
    public final LOTRFactionRank rankMax;
    public final float progress; // 0..1 across [alignMin, alignMax]

    private LOTRAlignmentBar(float alignMin, float alignMax, LOTRFactionRank rankMin,
                             LOTRFactionRank rankMax, float progress) {
        this.alignMin = alignMin;
        this.alignMax = alignMax;
        this.rankMin = rankMin;
        this.rankMax = rankMax;
        this.progress = progress;
    }

    public static LOTRAlignmentBar compute(LOTRFaction faction, float alignment) {
        LOTRFactionRank rank = faction.getRank(alignment);
        float alignMin;
        float alignMax;
        LOTRFactionRank rankMin;
        LOTRFactionRank rankMax;

        if (rank.isDummyRank()) {
            float firstRankAlign;
            LOTRFactionRank firstRank = faction.getFirstRank();
            if (firstRank != null && !firstRank.isDummyRank()) {
                firstRankAlign = firstRank.alignment;
            } else {
                firstRankAlign = 10.0f;
            }
            if (Math.abs(alignment) < firstRankAlign) {
                alignMin = -firstRankAlign;
                alignMax = firstRankAlign;
                rankMin = LOTRFactionRank.RANK_ENEMY;
                rankMax = firstRank != null && !firstRank.isDummyRank() ? firstRank : LOTRFactionRank.RANK_NEUTRAL;
            } else if (alignment < 0.0f) {
                alignMax = -firstRankAlign;
                alignMin = alignMax * 10.0f;
                rankMin = rankMax = LOTRFactionRank.RANK_ENEMY;
                while (alignment <= alignMin) {
                    alignMax *= 10.0f;
                    alignMin = alignMax * 10.0f;
                }
            } else {
                alignMin = firstRankAlign;
                alignMax = alignMin * 10.0f;
                rankMin = rankMax = LOTRFactionRank.RANK_NEUTRAL;
                while (alignment >= alignMax) {
                    alignMin = alignMax;
                    alignMax = alignMin * 10.0f;
                }
            }
        } else {
            alignMin = rank.alignment;
            rankMin = rank;
            LOTRFactionRank nextRank = faction.getRankAbove(rank);
            if (nextRank != null && !nextRank.isDummyRank() && nextRank != rank) {
                alignMax = nextRank.alignment;
                rankMax = nextRank;
            } else {
                alignMax = rank.alignment * 10.0f;
                rankMax = rank;
                while (alignment >= alignMax) {
                    alignMin = alignMax;
                    alignMax = alignMin * 10.0f;
                }
            }
        }

        float progress = (alignment - alignMin) / (alignMax - alignMin);
        if (progress < 0.0f) {
            progress = 0.0f;
        } else if (progress > 1.0f) {
            progress = 1.0f;
        }
        return new LOTRAlignmentBar(alignMin, alignMax, rankMin, rankMax, progress);
    }
}
