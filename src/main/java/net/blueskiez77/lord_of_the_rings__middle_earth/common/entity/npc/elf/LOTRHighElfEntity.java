package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHighElf: an elf of Lindon, with its people's dagger and bow; its mounts
 * wear Lindon's barding.
 *
 * <p>NOT ported yet: the pull of Lindon on its wandering (with the biomes), and
 * the killHighElf achievement (D7).
 */
public class LOTRHighElfEntity extends LOTRHighElfBaseEntity {

    public LOTRHighElfEntity(EntityType<? extends LOTRHighElfEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.LINDON_HORSE_ARMOR));
        }
        return horse;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "highElf/elf/hired" : "highElf/elf/friendly";
        }
        return "highElf/elf/hostile";
    }

    /** dropElfItems: and one time in six, whoever the killer, something from Lindon's halls. */
    @Override
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropElfItems(level, killedByPlayer, looting);
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.HIGH_ELVEN_HALL, 1, 1 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.LINDON_DAGGER));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.LINDON_BOW));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.HIGH_ELF.createQuest(this);
    }
}
