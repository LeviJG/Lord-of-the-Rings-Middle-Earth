package net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;

/**
 * The weapon specials -- Infernal, Chilling, Headhunting -- which the anvil
 * puts on and which do their work when a blow lands. A melee weapon carries
 * them itself; a bow, crossbow, blowgun or throwing axe hands them to what it
 * looses (setProjectileEnchantment), and the projectile carries them in as an
 * entity tag. hasMeleeOrRangedEnchant asks either.
 */
public final class LOTRModifierSpecials {

    /** The entity tag a projectile carries a special by: lotr_modifier_&lt;name&gt;. */
    private static final String TAG_PREFIX = "lotr_modifier_";

    /** LOTREnchantmentWeaponSpecial.getFireAmount: a Fire Aspect of 2. */
    private static final int FIRE_AMOUNT = 2;

    private LOTRModifierSpecials() {
    }

    public static void init() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, base, taken, blocked) -> {
            if (!blocked && entity.level() instanceof ServerLevel level) {
                onHurt(level, entity, source);
            }
        });
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer player && has(source, LOTRModifier.HEADHUNTING)) {
                dropHead(player);
            }
        });
    }

    /**
     * applyBowModifiers, the specials half: every special rides along, and an
     * Infernal launcher looses its shot already burning (setFire(100)).
     */
    public static void onLaunch(ItemStack launcher, Entity projectile) {
        for (LOTRModifier modifier : LOTRModifiers.get(launcher)) {
            if (modifier.effect() == LOTRModifier.Effect.WEAPON_SPECIAL) {
                projectile.addTag(TAG_PREFIX + modifier.getSerializedName());
            }
        }
        if (LOTRModifiers.has(launcher, LOTRModifier.FIRE)) {
            projectile.igniteForSeconds(5.0f);
        }
    }

    /**
     * hasMeleeOrRangedEnchant: a melee blow from a weapon that has it, or a
     * projectile that was loosed carrying it.
     */
    public static boolean has(DamageSource source, LOTRModifier modifier) {
        Entity direct = source.getDirectEntity();
        if (source.getEntity() instanceof LivingEntity attacker && attacker == direct) {
            ItemStack weapon = attacker.getMainHandItem();
            return LOTRModifiers.kindsOf(weapon).contains(LOTRModifier.Kind.MELEE)
                    && LOTRModifiers.has(weapon, modifier);
        }
        return direct != null && direct.entityTags().contains(TAG_PREFIX + modifier.getSerializedName());
    }

    private static void onHurt(ServerLevel level, LivingEntity entity, DamageSource source) {
        boolean infernal = has(source, LOTRModifier.FIRE);
        if (infernal && source.getEntity() instanceof LivingEntity attacker && attacker == source.getDirectEntity()) {
            // calcFireAspectForMelee, which the coremod added to Fire Aspect:
            // four seconds of burning a level.
            entity.igniteForSeconds(FIRE_AMOUNT * 4.0f);
        }
        if (infernal) {
            burst(level, entity, ParticleTypes.FLAME, 20, 0.1f, 0.15f, 0.15);
        }
        if (has(source, LOTRModifier.CHILL)) {
            // doChillAttack: Slowness II for five seconds. The FROST screen
            // overlay it also sent a player is not ported.
            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 5 * 20, 1));
            burst(level, entity, LOTRParticles.CHILL, 40, 0.1f, 0.2f, 0.0);
        }
    }

    /** LOTRPacketWeaponFX INFERNAL and CHILLING: a spray from seven-tenths up the body. */
    private static void burst(ServerLevel level, LivingEntity entity, ParticleOptions particle, int count,
            float minSpeed, float maxSpeed, double lift) {
        RandomSource random = entity.getRandom();
        double y = entity.getY() + entity.getBbHeight() * 0.7f;
        for (int i = 0; i < count; i++) {
            float angleXZ = random.nextFloat() * Mth.TWO_PI;
            float angleY = random.nextFloat() * Mth.TWO_PI;
            float speed = Mth.randomBetween(random, minSpeed, maxSpeed);
            double dx = Mth.cos(angleXZ) * Mth.cos(angleY) * speed + entity.getDeltaMovement().x;
            double dy = Mth.sin(angleY) * speed + lift + entity.getDeltaMovement().y;
            double dz = Mth.sin(angleXZ) * Mth.cos(angleY) * speed + entity.getDeltaMovement().z;
            level.sendParticles(particle, entity.getX(), y, entity.getZ(), 0, dx, dy, dz, 1.0);
        }
    }

    /** onLivingDeath: a player slain by a Headhunting weapon drops their own head. */
    private static void dropHead(ServerPlayer player) {
        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        head.set(DataComponents.PROFILE, ResolvableProfile.createResolved(player.getGameProfile()));
        player.spawnAtLocation(player.level(), head);
    }
}
