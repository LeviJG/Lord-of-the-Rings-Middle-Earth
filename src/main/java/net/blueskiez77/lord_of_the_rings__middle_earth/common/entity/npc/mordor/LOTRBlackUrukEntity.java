package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBlackUruk: a great orc of Mordor, man-sized and no weak orc, in
 * Black Uruk armour with a cleaver, battleaxe, dagger (poisoned or not) or
 * warhammer, and a spear one time in six. It never skirmishes, speaks lower,
 * leaves Black Uruk steel and things from a Black Uruk fort, and -- slain by
 * a player, one time in 6000 less 500 a looting level -- a mithril shirt.
 *
 * <p>NOT ported yet: the Black Uruk shield (LOTRShields.ALIGNMENT_BLACK_URUK,
 * D7), the killBlackUruk achievement (D7).
 */
public class LOTRBlackUrukEntity extends LOTRMordorOrcEntity {

    public LOTRBlackUrukEntity(EntityType<? extends LOTRBlackUrukEntity> type, Level level) {
        super(type, level);
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRMordorOrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    @Override
    public boolean canOrcSkirmish() {
        return false;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    protected Item getOrcSteelDrop() {
        return LOTRMaterialItems.BLACK_URUK_STEEL_INGOT;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75f;
    }

    @Override
    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.BLACK_URUK_FORT, 1, 2 + looting);
        }
        if (killedByPlayer) {
            int shinyShirtChance = 6000 - looting * 500;
            if (this.random.nextInt(Math.max(shinyShirtChance, 1)) == 0) {
                spawnAtLocation(level, LOTRCombatItems.MITHRIL_CHESTPLATE);
            }
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(switch (this.random.nextInt(7)) {
            case 0, 1, 2 -> LOTRCombatItems.BLACK_URUK_CLEAVER;
            case 3 -> LOTRCombatItems.BLACK_URUK_BATTLEAXE;
            case 4 -> LOTRCombatItems.BLACK_URUK_DAGGER;
            case 5 -> LOTRCombatItems.POISONED_BLACK_URUK_DAGGER;
            default -> LOTRCombatItems.BLACK_URUK_WARHAMMER;
        }));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BLACK_URUK_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.BLACK_URUK_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.BLACK_URUK_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.BLACK_URUK_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.BLACK_URUK_HELMET));
        return data;
    }
}
