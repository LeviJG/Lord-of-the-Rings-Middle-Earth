package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRangerIthilien: a Ranger of Ithilien, of Gondor -- a Gondorian
 * dagger (three times in four) or sword and a Gondorian bow, in the Ithilien
 * rangers' hood, tunic, leggings and boots, eating and drinking as Gondor
 * does. Slain, one time in six he leaves something from a Gondor house.
 *
 * <p>NOT ported yet: his cape (with the NPC capes), the pull of Ithilien on his
 * wandering (+20 there, with the biomes, D10), and the killRangerIthilien
 * achievement (D7).
 */
public class LOTRRangerIthilienEntity extends LOTRRangerEntity {

    public LOTRRangerIthilienEntity(EntityType<? extends LOTRRangerIthilienEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.RANGER_ITHILIEN;
    }

    @Override
    public LOTRFoods getDunedainFoods() {
        return LOTRFoods.GONDOR;
    }

    @Override
    public LOTRFoods getDunedainDrinks() {
        return LOTRFoods.GONDOR_DRINK;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.GONDOR;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    protected void dropDunedainItems(ServerLevel level, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.GONDOR_HOUSE, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "gondor/ranger/hired" : "gondor/ranger/friendly";
        }
        return "gondor/ranger/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(4) < 3
                ? LOTRCombatItems.GONDOR_DAGGER : LOTRCombatItems.GONDOR_SWORD));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.GONDOR_BOW));
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.ITHILIEN_RANGER_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.ITHILIEN_RANGER_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.ITHILIEN_RANGER_TUNIC));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.ITHILIEN_RANGER_HOOD));
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.GONDOR.createQuest(this);
    }
}
