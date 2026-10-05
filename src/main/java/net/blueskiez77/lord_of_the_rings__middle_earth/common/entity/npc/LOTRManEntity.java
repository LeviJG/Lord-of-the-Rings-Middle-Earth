package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRThrowingAxeItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolMaterials;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * LOTREntityMan: the peoples of flesh an orc would eat. One time in five, one
 * slain by a friend of the orcs or trolls -- an NPC of theirs, or a player
 * with positive alignment to any of them -- with a sword, tool or throwing
 * axe of a man-flesh material leaves one or two pieces of man-flesh.
 */
public abstract class LOTRManEntity extends LOTRNPCEntity {

    /**
     * LOTRMaterial.canHarvestManFlesh: the orc and troll materials. 26.2 items
     * don't keep their material, so a weapon is known by its repair items,
     * which for these materials are theirs alone.
     */
    private static final List<ToolMaterial> MAN_FLESH_MATERIALS = List.of(LOTRToolMaterials.MORDOR,
            LOTRToolMaterials.URUK, LOTRToolMaterials.GUNDABAD_URUK, LOTRToolMaterials.MORGUL, LOTRToolMaterials.ANGMAR,
            LOTRToolMaterials.DOL_GULDUR, LOTRToolMaterials.UTUMNO, LOTRToolMaterials.BLACK_URUK,
            LOTRToolMaterials.HALF_TROLL);

    protected LOTRManEntity(EntityType<? extends LOTRManEntity> type, Level level) {
        super(type, level);
    }

    private static boolean canHarvestManFlesh(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof HoeItem
                || !stack.has(DataComponents.WEAPON) && !stack.has(DataComponents.TOOL)
                && !(stack.getItem() instanceof LOTRThrowingAxeItem)) {
            return false;
        }
        Repairable repairable = stack.get(DataComponents.REPAIRABLE);
        if (repairable == null) {
            return false;
        }
        Set<TagKey<Item>> tags = new HashSet<>();
        MAN_FLESH_MATERIALS.forEach(m -> tags.add(m.repairItems()));
        return repairable.items().unwrapKey().map(tags::contains).orElse(false);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!(level() instanceof ServerLevel level) || !level.getGameRules().get(GameRules.MOB_DROPS)
                || this.random.nextInt(5) != 0 || !(source.getDirectEntity() instanceof LivingEntity killer)) {
            return;
        }
        List<LOTRFaction> manFleshFactions = LOTRFaction.getAllOfType(LOTRFaction.FactionType.TYPE_ORC,
                LOTRFaction.FactionType.TYPE_TROLL);
        boolean aligned = false;
        if (killer instanceof Player player) {
            for (LOTRFaction faction : manFleshFactions) {
                if (LOTRPlayerAlignments.getAlignment(player, faction) > 0.0f) {
                    aligned = true;
                }
            }
        } else {
            aligned = manFleshFactions.contains(LOTRNearestAttackableTargetGoal.factionOf(killer));
        }
        if (aligned && canHarvestManFlesh(killer.getMainHandItem())) {
            spawnAtLocation(level, new ItemStack(LOTRFoodItems.MAN_FLESH, 1 + this.random.nextInt(2)), 0.0f);
        }
    }
}
