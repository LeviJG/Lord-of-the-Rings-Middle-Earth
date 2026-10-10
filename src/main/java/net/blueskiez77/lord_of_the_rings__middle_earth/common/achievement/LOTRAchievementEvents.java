package net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRCraftingMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMillstoneMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTREventHandler's onCrafting, onSmelting and onItemPickup achievements: using each faction's
 * crafting table, crafting a few particular things, smelting the steels, bronze and venison, and
 * picking up athelas, a four-leaf clover or the horn of the Kine of Araw; and the blocks' own
 * harvestBlock and removedByPlayer achievements -- mining the five rare ores and the remains, and
 * picking a banana.
 *
 * <p>Four more are earned here by new means, at the user's asking, since the port keeps vanilla's
 * rabbit, spears and trident, and the original's means are gone: attackRabbit by hurting a rabbit
 * among crops (the original's own rabbit ate them); useSpearFromFar by a kill with a spear's
 * charge from the saddle (the original's spear was thrown, a kill from 50 blocks); hitByOrcSpear
 * by an orc's spear blow on a mithril chestplate (thrown, in the original); useDunlendingTrident
 * by spearing a fish with it, in hand or thrown (the original's trident fished).
 */
public final class LOTRAchievementEvents {

    private static final Map<LOTRCraftingTable, LOTRAchievement> TABLE_ACHIEVEMENTS = new EnumMap<>(LOTRCraftingTable.class);

    private LOTRAchievementEvents() {
    }

    public static void init() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> onBlockBroken(player, state));
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, base, taken, blocked) -> onHurt(entity, source));
        ServerLivingEntityEvents.AFTER_DEATH.register(LOTRAchievementEvents::onDeath);
    }

    private static void onHurt(LivingEntity entity, DamageSource source) {
        if (entity instanceof Rabbit rabbit && source.getEntity() instanceof Player player && isAmongCrops(rabbit)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.ATTACK_RABBIT);
        }
        if (entity instanceof Player player && source.getEntity() instanceof LOTROrcEntity orc
                && source.getDirectEntity() == orc && isSpear(orc.getMainHandItem())
                && player.getItemBySlot(EquipmentSlot.CHEST).is(LOTRCombatItems.MITHRIL_CHESTPLATE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.HIT_BY_ORC_SPEAR);
        }
    }

    private static void onDeath(LivingEntity entity, DamageSource source) {
        if (!(source.getEntity() instanceof Player player) || entity == player) {
            return;
        }
        if (source.getDirectEntity() == player && player.isPassenger() && player.isUsingItem()
                && isSpear(player.getUseItem())) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.USE_SPEAR_FROM_FAR);
        }
        if (entity instanceof AbstractFish) {
            ItemStack weapon = source.getDirectEntity() instanceof ThrownTrident trident ? trident.getWeaponItem()
                    : source.getDirectEntity() == player ? player.getMainHandItem() : ItemStack.EMPTY;
            if (weapon != null && weapon.is(LOTRCombatItems.DUNLENDING_TRIDENT)) {
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.USE_DUNLENDING_TRIDENT);
            }
        }
    }

    /** A spear: anything with vanilla's spear charge, the mod's spears among them. */
    private static boolean isSpear(ItemStack stack) {
        return stack.has(DataComponents.KINETIC_WEAPON);
    }

    /** The rabbit on or beside a crop, as a rabbit raiding a garden stands. */
    private static boolean isAmongCrops(Rabbit rabbit) {
        BlockPos pos = rabbit.blockPosition();
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1))) {
            if (rabbit.level().getBlockState(p).getBlock() instanceof CropBlock) {
                return true;
            }
        }
        return false;
    }

    /**
     * harvestBlock, which ran only for a player who could harvest the block (and not in creative),
     * and the banana's removedByPlayer, which ran for any.
     */
    private static void onBlockBroken(Player player, BlockState state) {
        if (state.is(LOTRFoodBlocks.BANANA_BLOCK)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.PICK_BANANA);
        }
        if (player.isCreative() || !player.hasCorrectToolForDrops(state)) {
            return;
        }
        if (state.is(LOTRBuildingBlocks.REMAINS)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.MINE_REMAINS);
        }
        if (state.is(LOTRBuildingBlocks.MITHRIL_ORE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.MINE_MITHRIL);
        }
        if (state.is(LOTRBuildingBlocks.QUENDITE_ORE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.MINE_QUENDITE);
        }
        if (state.is(LOTRBuildingBlocks.GLOWSTONE_ORE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.MINE_GLOWSTONE);
        }
        if (state.is(LOTRBuildingBlocks.NAURITE_ORE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.MINE_NAURITE);
        }
        if (state.is(LOTRBuildingBlocks.GULDURIL_ORE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.MINE_GULDURIL);
        }
    }

    private static void initTables() {
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.ELVEN, LOTRAchievement.USE_ELVEN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.URUK, LOTRAchievement.USE_URUK_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.ROHIRRIC, LOTRAchievement.USE_ROHIRRIC_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.GONDORIAN, LOTRAchievement.USE_GONDORIAN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.WOOD_ELVEN, LOTRAchievement.USE_WOOD_ELVEN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.DWARVEN, LOTRAchievement.USE_DWARVEN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.MORGUL, LOTRAchievement.USE_MORGUL_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.DUNLENDING, LOTRAchievement.USE_DUNLENDING_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.ANGMAR, LOTRAchievement.USE_ANGMAR_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.NEAR_HARAD, LOTRAchievement.USE_NEAR_HARAD_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.HIGH_ELVEN, LOTRAchievement.USE_HIGH_ELVEN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.BLUE_DWARVEN, LOTRAchievement.USE_BLUE_DWARVEN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.RANGER, LOTRAchievement.USE_RANGER_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.DOL_GULDUR, LOTRAchievement.USE_DOL_GULDUR_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.GUNDABAD, LOTRAchievement.USE_GUNDABAD_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.HALF_TROLL, LOTRAchievement.USE_HALF_TROLL_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.DOL_AMROTH, LOTRAchievement.USE_DOL_AMROTH_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.MOREDAIN, LOTRAchievement.USE_MOREDAIN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.TAUREDAIN, LOTRAchievement.USE_TAUREDAIN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.DALE, LOTRAchievement.USE_DALE_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.DORWINION, LOTRAchievement.USE_DORWINION_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.HOBBIT, LOTRAchievement.USE_HOBBIT_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.RHUN, LOTRAchievement.USE_RHUN_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.RIVENDELL, LOTRAchievement.USE_RIVENDELL_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.UMBAR, LOTRAchievement.USE_UMBAR_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.GULF, LOTRAchievement.USE_GULF_TABLE);
        TABLE_ACHIEVEMENTS.put(LOTRCraftingTable.BREE, LOTRAchievement.USE_BREE_TABLE);
    }

    private static boolean is(ItemStack stack, Item item) {
        return stack.is(item);
    }

    private static boolean is(ItemStack stack, Block block) {
        return stack.is(block.asItem());
    }

    /** onCrafting: an item taken from a crafting grid's result. */
    public static void onCrafting(Player player, ItemStack stack) {
        if (player.level().isClientSide()) {
            return;
        }
        if (player.containerMenu instanceof LOTRCraftingMenu menu) {
            if (TABLE_ACHIEVEMENTS.isEmpty()) {
                initTables();
            }
            LOTRAchievement tableAch = TABLE_ACHIEVEMENTS.get(menu.table());
            if (tableAch != null) {
                LOTRPlayerAchievements.addAchievement(player, tableAch);
            }
        }
        if (is(stack, Items.SADDLE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_SADDLE);
        }
        if (is(stack, LOTRMaterialItems.BRONZE_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_BRONZE);
        }
        if (is(stack, LOTRFoodBlocks.APPLE_CRUMBLE)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_APPLE_CRUMBLE);
        }
        if (is(stack, LOTRFoodItems.RABBIT_STEW)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_RABBIT_STEW);
        }
        if (is(stack, LOTRFoodItems.SUSPICIOUS_MEAT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_SALTED_FLESH);
        }
        if (is(stack, LOTRBuildingBlocks.DWARVEN_MITHRIL_BRICK)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_MITHRIL_DWARVEN_BRICK);
        }
        if (is(stack, LOTRCombatBlocks.ORC_BOMB) || is(stack, LOTRCombatBlocks.DOUBLE_STRENGTH_ORC_BOMB)
                || is(stack, LOTRCombatBlocks.TRIPLE_STRENGTH_ORC_BOMB) || is(stack, LOTRCombatBlocks.ORC_FIRE_BOMB)
                || is(stack, LOTRCombatBlocks.DOUBLE_STRENGTH_ORC_FIRE_BOMB)
                || is(stack, LOTRCombatBlocks.TRIPLE_STRENGTH_ORC_FIRE_BOMB)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_ORC_BOMB);
        }
        if (is(stack, LOTRMiscItems.KEY_OF_ICE) || is(stack, LOTRMiscItems.KEY_OF_OBSIDIAN)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.CRAFT_UTUMNO_KEY);
        }
    }

    /** onSmelting: an item taken from a furnace's, forge's, oven's or millstone's output. */
    public static void onSmelting(Player player, ItemStack stack) {
        if (player.level().isClientSide()) {
            return;
        }
        if (is(stack, LOTRMaterialItems.BRONZE_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.ALLOY_BRONZE);
        }
        if (is(stack, LOTRFoodItems.COOKED_VENISON)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.COOK_DEER);
        }
        if (is(stack, LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_BLUE_DWARF_STEEL);
        }
        if (is(stack, LOTRMaterialItems.ELVEN_STEEL_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_ELF_STEEL);
        }
        if (is(stack, LOTRMaterialItems.DWARVEN_STEEL_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_DWARF_STEEL);
        }
        if (is(stack, LOTRMaterialItems.URUK_STEEL_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_URUK_STEEL);
        }
        if (is(stack, LOTRMaterialItems.ORC_STEEL_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_ORC_STEEL);
        }
        if (is(stack, LOTRMaterialItems.BLACK_URUK_STEEL_INGOT)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_BLACK_URUK_STEEL);
        }
        // LOTRSlotMillstone.onCrafting.
        if (player.containerMenu instanceof LOTRMillstoneMenu && is(stack, LOTRMaterialItems.OBSIDIAN_SHARD)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SMELT_OBSIDIAN_SHARD);
        }
    }

    /** onItemPickup: an item a player walks over. */
    public static void onItemPickup(Player player, ItemStack stack) {
        if (player.level().isClientSide()) {
            return;
        }
        if (is(stack, LOTRDecorationBlocks.ATHELAS)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.FIND_ATHELAS);
        }
        if (is(stack, LOTRDecorationBlocks.FOUR_LEAF_CLOVER)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.FIND_FOUR_LEAF_CLOVER);
        }
        if (is(stack, LOTRMaterialItems.KINE_OF_ARAW_HORN)) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.GET_KINE_ARAW_HORN);
        }
    }
}
