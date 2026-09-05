package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTRItemCommandSword: not a sword at all, but a pointing stick for an army.
 *
 * <p>WHAT IT IS FOR. It is the Horn of Command's other half. The horn calls a
 * squadron to halt, to move, or to come to you; the sword tells them what to
 * KILL. Point it at something up to sixty-four blocks off and right-click:
 * every unit you have hired within twelve blocks that obeys the command sword
 * and carries the same squadron name is ordered onto the nearest enemy within
 * six blocks of the spot you indicated. Point it at open sky and the same click
 * CANCELS those orders and they stand down. So it is aim, click, charge -- and
 * click again at nothing to call them off.
 *
 * <p>It is deliberately useless as a weapon: setMaxDamage(0) makes it
 * unbreakable, getItemEnchantability returns zero, and lotrWeaponDamage is set
 * to 1.0 -- less than a fist. You carry it in place of a weapon, not as one.
 *
 * <p>WHAT IT DOES NOT DO, and cannot yet: give the order. There are no NPCs in
 * the port, so {@link #command} is where the target search and the squadron
 * test go and is empty, exactly as LOTRCommandHornItem's is. The sword aims,
 * swings and names its squadron; it commands nobody.
 */
public class LOTRCommandSwordItem extends Item {

    /** How far the original looked for something to point at. */
    public static final double COMMAND_RANGE = 64.0;

    /** getEntitiesWithinAABB(player.boundingBox.expand(12)): who can hear it. */
    public static final double UNIT_RANGE = 12.0;

    /** The spread around the point indicated that a target may be found in. */
    public static final double SPREAD_RANGE = 6.0;

    /** lotrWeaponDamage = 1.0f, so the modifier is zero: a bare fist's worth. */
    public static final double ATTACK_DAMAGE = 1.0;

    public LOTRCommandSwordItem(Properties properties) {
        super(properties);
    }

    /**
     * onItemRightClick: swing, look for a target, and order the squadron onto
     * it. The swing happens either way -- the original called swingItem before
     * it had looked at anything -- so the gesture reads the same whether or not
     * there was anything to point at.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.swing(hand);
        if (!level.isClientSide()) {
            command(level, player, player.getItemInHand(hand));
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Where the order goes when there is anybody to take it.
     *
     * <p>The original: raycast COMMAND_RANGE for an entity, else for a block,
     * else nothing; gather every living thing within SPREAD_RANGE of the hit;
     * then for each hired NPC within UNIT_RANGE whose squadron matches and
     * whose getObeyCommandSword is set, sort those targets by the NPC's own
     * TargetSorter and set it on the nearest -- or, with no target at all, call
     * commandSwordCancel. All of that waits on the NPCs.
     */
    private static void command(Level level, Player player, ItemStack stack) {
        // Intentionally empty. See the class note.
    }
}
