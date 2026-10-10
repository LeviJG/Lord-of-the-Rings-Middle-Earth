package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBreeGuard: Bree-land's watch, always men. They seek out their
 * people's enemies and flee no one, armed with an iron sword or pike, in
 * leather dyed in Bree-land's browns or in mail, under an iron helmet.
 */
public class LOTRBreeGuardEntity extends LOTRBreeManEntity {

    /** leatherDyes: the colours a guard's leather leggings and jerkin come in. */
    private static final int[] LEATHER_DYES = {11373426, 7823440, 5983041, 9535090};

    public LOTRBreeGuardEntity(EntityType<? extends LOTRBreeGuardEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_BREE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected int addBreeAttackAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRAttackOnCollideGoal(this, 1.45, false));
        return prio;
    }

    @Override
    protected void addBreeAvoidAI(int prio) {
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "bree/guard/hired" : "bree/guard/friendly";
        }
        return "bree/guard/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {Items.IRON_SWORD, Items.IRON_SWORD, LOTRCombatItems.IRON_PIKE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, this.random.nextInt(3) == 0 ? new ItemStack(Items.CHAINMAIL_BOOTS)
                : dyed(Items.LEATHER_BOOTS, 3354152));
        setItemSlot(EquipmentSlot.LEGS, this.random.nextInt(3) == 0 ? new ItemStack(Items.CHAINMAIL_LEGGINGS)
                : dyed(Items.LEATHER_LEGGINGS, LEATHER_DYES[this.random.nextInt(LEATHER_DYES.length)]));
        setItemSlot(EquipmentSlot.CHEST, this.random.nextInt(3) == 0 ? new ItemStack(Items.CHAINMAIL_CHESTPLATE)
                : dyed(Items.LEATHER_CHESTPLATE, LEATHER_DYES[this.random.nextInt(LEATHER_DYES.length)]));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        return data;
    }

    /** LOTRColorUtil.dyeLeather. */
    private static ItemStack dyed(Item leather, int colour) {
        ItemStack stack = new ItemStack(leather);
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(colour));
        return stack;
    }
}
