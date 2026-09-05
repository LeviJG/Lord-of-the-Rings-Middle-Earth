package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import net.minecraft.entity.boss.IBossDisplayData;

public interface LOTRBoss extends IBossDisplayData {
	float getBaseChanceModifier();

	LOTRAchievement getBossKillAchievement();

	void onJumpAttackFall();
}
