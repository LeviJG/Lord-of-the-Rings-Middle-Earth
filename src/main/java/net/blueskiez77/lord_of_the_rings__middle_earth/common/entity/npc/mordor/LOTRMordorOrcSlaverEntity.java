package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMordorOrcSlaver (the Mordor Slaver): a man-sized orc, no weak one,
 * bare-headed in Mordor armour, who sells Nurn slaves -- hired to farm -- to
 * those at +200 or better. He seeks no one out -- he only answers attacks, and
 * carries no warhorn. Until the branding iron exists he idles with his weapon,
 * where the original gave him the iron.
 *
 * <p>NOT ported yet: the branding iron in his idle hand, with the branding
 * iron item, and the hireNurnSlave achievement (D7).
 */
public class LOTRMordorOrcSlaverEntity extends LOTRMordorOrcEntity implements LOTRUnitTradeable {

    public LOTRMordorOrcSlaverEntity(EntityType<? extends LOTRMordorOrcSlaverEntity> type, Level level) {
        super(type, level);
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRMordorOrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.MORDOR_ORC_SLAVER;
    }

    /** canTradeWith: +200 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 200.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "mordor/slaver/friendly" : "mordor/slaver/neutral";
        }
        return "mordor/orc/hostile";
    }

    @Override
    public boolean shouldTraderRespawn() {
        return true;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.MORDOR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.MORDOR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.MORDOR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }
}
