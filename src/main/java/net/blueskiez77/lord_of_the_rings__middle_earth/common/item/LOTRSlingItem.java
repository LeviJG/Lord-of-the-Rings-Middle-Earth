package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPebbleEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTRItemSling: a strap of leather that throws a stone harder than an arm can.
 *
 * <p>250 uses, one per shot, mended with leather. It has no draw and no charge
 * -- right click and the stone is away -- and it costs a {@link LOTRPebbleItem}
 * out of the inventory, which is the whole of its ammunition. A slung pebble
 * hits for two where a thrown one manages one.
 *
 * <p>NOT a ProjectileWeaponItem. Vanilla's ammunition machinery is built around
 * a draw that can be held and released; the sling has none, and its stone is a
 * ThrowableItemProjectile rather than an arrow. Spending the pebble by hand is
 * the smaller and more honest fit.
 */
public class LOTRSlingItem extends Item implements LOTRModifiable {

    /** setMaxDamage(250). */
    public static final int DURABILITY = 250;

    public LOTRSlingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level,
            net.minecraft.world.entity.Entity holder, EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }

    /**
     * onItemRightClick: a stone if there is one to spend, and nothing at all if
     * there is not -- the original checked the inventory first and the sling
     * took no wear on a dry click.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean free = player.hasInfiniteMaterials();
        int pebbleSlot = free ? -1 : findPebble(player);
        if (!free && pebbleSlot < 0) {
            return InteractionResult.FAIL;
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5f,
                0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));

        if (level instanceof ServerLevel server) {
            // See LOTRPebbleItem: spawnProjectileUsingShoot wants a direction
            // vector, not a pitch and a yaw, and this is the overload that
            // takes the shooter's rotation properly.
            ItemStack ammo = new ItemStack(LOTRItems.PEBBLE);
            Projectile.spawnProjectileFromRotation(
                    (lvl, shooter, unused) -> new LOTRPebbleEntity(
                            LOTREntities.PEBBLE, shooter, lvl,
                            new ItemStack(LOTRItems.PEBBLE)).setSlung(),
                    server, ammo, player, 0.0f, LOTRPebbleEntity.THROW_VELOCITY, 1.0f);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!free) {
            player.getInventory().removeItem(pebbleSlot, 1);
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND);
        }
        return InteractionResult.SUCCESS;
    }

    /** inventory.hasItem(LOTRMod.pebble), and the slot to spend it from. */
    private static int findPebble(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(LOTRItems.PEBBLE)) {
                return slot;
            }
        }
        return -1;
    }
}
