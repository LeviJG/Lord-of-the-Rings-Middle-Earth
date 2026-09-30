package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRMordorSpiderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
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
 * LOTREntityMordorOrcSpiderKeeper (the Mordor Spider Keeper): a man-sized
 * orc, no weak one, bare-headed in Mordor armour with a skull staff, who
 * spawns riding a great Mordor spider (the largest, if there is room for it).
 * He seeks no one out -- he only answers attacks. He hires out Mordor
 * spiders, orcs and archers (either on spiders) and Nan Ungol banner bearers
 * to those at +250 or better.
 *
 * <p>NOT ported yet: his warhorn (LOTRInvasions.MORDOR_NAN_UNGOL, D12), and
 * the tradeOrcSpiderKeeper achievement (D7).
 */
public class LOTRMordorOrcSpiderKeeperEntity extends LOTRMordorOrcEntity implements LOTRUnitTradeable {

    public LOTRMordorOrcSpiderKeeperEntity(EntityType<? extends LOTRMordorOrcSpiderKeeperEntity> type, Level level) {
        super(type, level);
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRMordorOrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.MORDOR_ORC_SPIDER_KEEPER;
    }

    /** canTradeWith: +250 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 250.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 5.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "mordor/chieftain/friendly" : "mordor/chieftain/neutral";
        }
        return "mordor/orc/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ORC_SKULL_STAFF));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.MORDOR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.MORDOR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.MORDOR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        ServerLevel server = level.getLevel();
        LOTRMordorSpiderEntity spider = LOTREntities.MORDOR_SPIDER.create(server, EntitySpawnReason.JOCKEY);
        if (spider != null) {
            spider.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
            spider.setSpiderScale(3);
            if (server.noCollision(spider)) {
                spider.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, null);
                server.addFreshEntity(spider);
                startRiding(spider, true, false);
            }
        }
        return data;
    }
}
