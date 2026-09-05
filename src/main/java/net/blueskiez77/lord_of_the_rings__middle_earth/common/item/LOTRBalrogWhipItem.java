package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRItemBalrogWhip: a lash of fire, sixteen blocks long.
 *
 * <p>As a weapon it is a UTUMNO blade whose damage is forced to 7.0 rather than
 * worked out from the material, with a thousand uses and no enchantability at
 * all. What makes it the balrog's is what happens when it LANDS: the whip cracks
 * out along the line you are looking down, sets fire to everything caught in it,
 * and lays a trail of flame along the ground the whole way.
 *
 * <p>The crack is transcribed: a 16-block sweep off the look vector, every
 * living thing whose box that line clips takes a point of damage and five
 * seconds of burning -- except the one you actually hit, which is already
 * burning from the blow -- and then fire blocks are walked out from four blocks
 * along to the end, one per step, each dropped onto the first solid surface
 * within three blocks up or down. The trail STOPS at the first step that finds
 * nowhere to burn, so it runs along a floor and halts at a cliff.
 *
 * <p>NOT ported: the banner-protection test, which spared blocks inside a
 * faction's claim. LOTRBannerProtection is not in the port -- Khamûl's fire is
 * missing the same check -- so the whip burns anywhere.
 *
 * <p>Also not ported: checkIncompatibleModifiers, which stripped the fire and
 * chill modifiers off the whip because they would double up on its own burning.
 * Neither of those two is in the port's modifier table.
 */
public class LOTRBalrogWhipItem extends LOTRModifiableItem {

    /** lotrWeaponDamage = 7.0f, set directly rather than from the material. */
    public static final float ATTACK_DAMAGE = 7.0f;

    /** setMaxDamage(1000), where UTUMNO alone would give 400. */
    public static final int DURABILITY = 1000;

    /** How far the lash reaches. */
    private static final double RANGE = 16.0;

    /** The half-width of the swept line, as the original expanded it. */
    private static final double SIGHT_WIDTH = 1.0;

    /** Anything else the lash touches takes this and catches light. */
    private static final float SPLASH_DAMAGE = 1.0f;

    /** Five seconds of burning, on entities and along the ground alike. */
    private static final int BURN_SECONDS = 5;

    /** The trail starts four blocks out -- not under your own feet. */
    private static final int TRAIL_START = 4;

    /** And looks this far up and down for something to burn on. */
    private static final int TRAIL_REACH = 3;

    public LOTRBalrogWhipItem(Properties properties) {
        super(properties);
    }

    /**
     * hitEntity: the blow lands, and then the whip cracks.
     *
     * <p>The original gated this on {@code hitEntity.hurtTime == maxHurtTime},
     * which is 1.7.10 for "the blow actually landed rather than being eaten by
     * invulnerability frames". Item.hurtEnemy is only reached down that path in
     * 26.2, so the guard is already made for us.
     */
    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (attacker.level() instanceof ServerLevel level) {
            crack(level, attacker, target);
        }
    }

    /**
     * getMaxItemUseDuration: a second of winding up before the lash goes out.
     */
    public static final int USE_TICKS = 20;

    /**
     * onItemRightClick: start winding up. The whip is a HELD item -- right click
     * and keep holding, and it cracks when the second is up.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return USE_TICKS;
    }

    /** EnumAction.bow: the wind-up is held the way a bow is drawn. */
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    /**
     * onEaten: the second is up, so the whip cracks at nothing in particular --
     * no target, just the line you are looking down -- and costs a point.
     *
     * <p>This is the half that was missing: without it the lash only ever went
     * out as a side effect of landing a blow, and right-clicking did nothing at
     * all.
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        user.swing(user.getUsedItemHand());
        if (level instanceof ServerLevel server) {
            crack(server, user, null);
        }
        stack.hurtAndBreak(1, user, user.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? EquipmentSlot.MAINHAND
                : EquipmentSlot.OFFHAND);
        return stack;
    }

    /** launchWhip. */
    private static void crack(ServerLevel level, LivingEntity user,
            @org.jspecify.annotations.Nullable LivingEntity struck) {
        level.playSound(null, user.getX(), user.getY(), user.getZ(), LOTRSounds.ITEM_BALROG_WHIP,
                SoundSource.PLAYERS, 2.0f, 0.7f + level.getRandom().nextFloat() * 0.6f);

        Vec3 from = user.position();
        Vec3 look = user.getLookAngle();
        Vec3 to = from.add(look.scale(RANGE));

        AABB sweep = user.getBoundingBox().expandTowards(look.scale(RANGE))
                .inflate(SIGHT_WIDTH);
        List<LivingEntity> caught = level.getEntitiesOfClass(LivingEntity.class, sweep,
                other -> other != user && other.canBeCollidedWith(user)
                        && other.getBoundingBox().inflate(1.0).clip(from, to).isPresent());

        for (LivingEntity entity : caught) {
            // The one you actually struck is already alight from the blow; the
            // rest have to be hurt before they will catch.
            if (entity == struck
                    || entity.hurtServer(level, level.damageSources().mobAttack(user),
                            SPLASH_DAMAGE)) {
                entity.igniteForSeconds(BURN_SECONDS);
            }
        }

        layTrail(level, from.add(0.0, user.getEyeHeight(), 0.0), to);
    }

    /**
     * The line of fire along the ground, from four blocks out to the end.
     *
     * <p>Each step drops one fire block onto the first thing within three
     * blocks up or down that will hold it, and the first step that finds
     * nothing ENDS the trail -- which is what keeps the lash on the floor
     * instead of writing a line through the air over a drop.
     */
    private static void layTrail(ServerLevel level, Vec3 eye, Vec3 end) {
        Vec3 span = end.subtract(eye);
        for (int step = TRAIL_START; step < (int) RANGE; step++) {
            Vec3 at = eye.add(span.scale(step / RANGE));
            if (!burnAt(level, BlockPos.containing(at))) {
                return;
            }
        }
    }

    private static boolean burnAt(ServerLevel level, BlockPos pos) {
        for (int y = pos.getY() - TRAIL_REACH; y <= pos.getY() + TRAIL_REACH; y++) {
            BlockPos here = new BlockPos(pos.getX(), y, pos.getZ());
            BlockPos below = here.below();
            BlockState floor = level.getBlockState(below);
            boolean holds = floor.isFaceSturdy(level, below, net.minecraft.core.Direction.UP)
                    || floor.is(net.minecraft.tags.BlockTags.LEAVES);
            if (holds && level.getBlockState(here).canBeReplaced()) {
                level.setBlockAndUpdate(here, Blocks.FIRE.defaultBlockState());
                return true;
            }
        }
        return false;
    }

}
