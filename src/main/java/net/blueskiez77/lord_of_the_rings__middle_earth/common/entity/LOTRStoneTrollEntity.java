package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTrollStatueItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityStoneTroll: a troll caught by the sun, stood still forever.
 *
 * <p>It is an {@link Entity} rather than a block because it is 1.6 by 3.2 and
 * carries an outfit and a head count -- none of which fits a block state. The
 * original was an Entity too, and this keeps that.
 *
 * <p>Two quite different lives, told apart by {@link #placedByPlayer}. One the
 * player put down is furniture: any hit from a player breaks it and gives the
 * statue item straight back. One that generated in the world is a lump of rock
 * with 40 health, chipped away a point at a time by bare hands -- which leaves
 * cobblestone -- or in bigger bites by a pickaxe, which leaves the statue whole.
 * That difference is the whole point of the thing, so it is reproduced exactly.
 *
 * <p>NOT ported: LOTRBannerProtectable, and the achievement the original gave
 * for prising a statue loose with a pickaxe. Neither system exists in the port
 * yet; the pickaxe still yields the statue, which is the part that matters.
 */
public class LOTRStoneTrollEntity extends Entity {

    /** Which of the three outfits the troll wears; 0..2, as the item damage was. */
    private static final EntityDataAccessor<Byte> DATA_OUTFIT =
            SynchedEntityData.defineId(LOTRStoneTrollEntity.class, EntityDataSerializers.BYTE);

    /** Trolls in the book came two-headed sometimes. A cosmetic flag, synced for the renderer. */
    private static final EntityDataAccessor<Boolean> DATA_TWO_HEADS =
            SynchedEntityData.defineId(LOTRStoneTrollEntity.class, EntityDataSerializers.BOOLEAN);

    /** setEntityState(this, (byte) 16): a small puff of stone, for a hit that did not finish it. */
    private static final byte EVENT_CHIPPED = 16;

    /** setEntityState(this, (byte) 17): the larger burst as it breaks apart. */
    private static final byte EVENT_SHATTERED = 17;

    public static final float MAX_TROLL_HEALTH = 40.0f;

    private float trollHealth = MAX_TROLL_HEALTH;

    /** True when a player set this one down, which makes it furniture rather than rock. */
    private boolean placedByPlayer;

    /** Ticks since it was last near a player; a worldgen statue eventually gives up. */
    private int entityAge;

    public LOTRStoneTrollEntity(EntityType<? extends LOTRStoneTrollEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OUTFIT, (byte) 0);
        builder.define(DATA_TWO_HEADS, false);
    }

    public int getTrollOutfit() {
        return this.entityData.get(DATA_OUTFIT);
    }

    public void setTrollOutfit(int outfit) {
        this.entityData.set(DATA_OUTFIT, (byte) outfit);
    }

    public boolean hasTwoHeads() {
        return this.entityData.get(DATA_TWO_HEADS);
    }

    public void setHasTwoHeads(boolean twoHeads) {
        this.entityData.set(DATA_TWO_HEADS, twoHeads);
    }

    public void setPlacedByPlayer(boolean placed) {
        this.placedByPlayer = placed;
    }

    /** canBeCollidedWith returned true: you can stand on a statue's shoulders. */
    @Override
    public boolean canBeCollidedWith(Entity entity) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    /** getPickedResult: middle-click gives back the statue you would have dropped. */
    @Override
    public ItemStack getPickResult() {
        return getStatueItem();
    }

    /** The item form, carrying this troll's outfit and head count. */
    public ItemStack getStatueItem() {
        ItemStack stack = new ItemStack(LOTRItems.TROLL_STATUE);
        LOTRTrollStatueItem.setOutfit(stack, getTrollOutfit());
        LOTRTrollStatueItem.setTwoHeads(stack, hasTwoHeads());
        return stack;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isRemoved()) {
            return false;
        }

        // A statue the player put down is furniture: one hit from a player takes
        // it back, and nothing else touches it at all.
        if (this.placedByPlayer) {
            if (!(source.getDirectEntity() instanceof Player player)) {
                return false;
            }
            playStoneSound(level, 0.8f);
            level.broadcastEntityEvent(this, EVENT_SHATTERED);
            this.discard();
            if (!player.getAbilities().instabuild) {
                dropAsStatue();
            }
            return true;
        }

        boolean drops = true;
        boolean dropStatue = false;
        float damage = amount;

        if (source.getDirectEntity() instanceof Player player) {
            if (player.getAbilities().instabuild) {
                // Creative: gone in one, and nothing left behind.
                drops = false;
                damage = this.trollHealth;
            } else {
                ItemStack held = player.getMainHandItem();
                // A pickaxe prises the statue out whole and hits far harder;
                // anything else chips it into cobblestone one point at a time.
                if (held.is(Items.WOODEN_PICKAXE) || held.is(Items.STONE_PICKAXE)
                        || held.is(Items.IRON_PICKAXE) || held.is(Items.GOLDEN_PICKAXE)
                        || held.is(Items.DIAMOND_PICKAXE) || held.is(Items.NETHERITE_PICKAXE)) {
                    dropStatue = true;
                    damage = 1.0f + (float) player.getAttributeValue(
                            net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
                } else {
                    dropStatue = false;
                    damage = 1.0f;
                }
                if (!held.isEmpty()) {
                    held.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
                }
            }
        }

        this.trollHealth -= damage;
        if (this.trollHealth <= 0.0f) {
            playStoneSound(level, 0.8f);
            level.broadcastEntityEvent(this, EVENT_SHATTERED);
            if (drops) {
                if (dropStatue) {
                    dropAsStatue();
                } else {
                    int stone = 6 + this.random.nextInt(7);
                    for (int i = 0; i < stone; i++) {
                        this.spawnAtLocation(level, new ItemStack(Blocks.COBBLESTONE));
                    }
                }
            }
            this.discard();
        } else {
            playStoneSound(level, 0.5f);
            level.broadcastEntityEvent(this, EVENT_CHIPPED);
        }
        return true;
    }

    /**
     * Blocks.stone.stepSound's break sound, at (volume + 1) / 2 and the pitch
     * scaled by the caller -- 0.8 when it breaks, 0.5 for a hit that does not.
     */
    private void playStoneSound(Level level, float pitchScale) {
        level.playSound(null, this.getX(), this.getY(), this.getZ(),
                Blocks.STONE.defaultBlockState().getSoundType().getBreakSound(),
                SoundSource.BLOCKS,
                (Blocks.STONE.defaultBlockState().getSoundType().getVolume() + 1.0f) / 2.0f,
                Blocks.STONE.defaultBlockState().getSoundType().getPitch() * pitchScale);
    }

    public void dropAsStatue() {
        if (this.level() instanceof ServerLevel level) {
            this.spawnAtLocation(level, getStatueItem());
        }
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == EVENT_CHIPPED || event == EVENT_SHATTERED) {
            // blockcrack_<stone>_0: sixteen flecks for a chip, sixty-four when
            // the whole thing comes apart.
            int count = event == EVENT_CHIPPED ? 16 : 64;
            BlockState stone = Blocks.STONE.defaultBlockState();
            for (int i = 0; i < count; i++) {
                this.level().addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, stone),
                        this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        0.0, 0.0, 0.0);
            }
        } else {
            super.handleEntityEvent(event);
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        // The original's own gravity and friction, not Entity's: 0.04 down per
        // tick, then 0.98 drag in the air and the ground block's slipperiness
        // times 0.98 on the floor. Transcribed rather than replaced with
        // applyGravity() so a statue slides and settles the way it always did.
        this.setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.03999999910593033, 0.0));
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());

        float friction = 0.98f;
        if (this.onGround()) {
            BlockPos below = BlockPos.containing(this.getX(),
                    Mth.floor(this.getBoundingBox().minY) - 1, this.getZ());
            BlockState state = this.level().getBlockState(below);
            if (!state.isAir()) {
                friction = state.getBlock().getFriction() * 0.98f;
            } else {
                friction = 0.58800006f;
            }
        }

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * friction, motion.y * 0.98,
                motion.z * friction);
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.5, 1.0));
        }

        // A statue that generated in the world is scenery, and scenery that
        // nobody is looking at gets cleaned up: instantly past 128 blocks, and
        // on a slow dice roll past 32 once it has stood for half a minute. One
        // the player placed is never removed.
        if (!this.level().isClientSide() && !this.placedByPlayer) {
            this.entityAge++;
            Player player = this.level().getNearestPlayer(this, -1.0);
            if (player != null) {
                double distanceSq = player.distanceToSqr(this);
                if (distanceSq > 16384.0) {
                    this.discard();
                } else if (this.entityAge > 600 && this.random.nextInt(800) == 0
                        && distanceSq > 1024.0) {
                    this.discard();
                } else if (distanceSq < 1024.0) {
                    this.entityAge = 0;
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.trollHealth = input.getFloatOr("TrollHealth", MAX_TROLL_HEALTH);
        setTrollOutfit(input.getByteOr("TrollOutfit", (byte) 0));
        this.placedByPlayer = input.getBooleanOr("PlacedByPlayer", false);
        setHasTwoHeads(input.getBooleanOr("TwoHeads", false));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putFloat("TrollHealth", this.trollHealth);
        output.putByte("TrollOutfit", (byte) getTrollOutfit());
        output.putBoolean("PlacedByPlayer", this.placedByPlayer);
        output.putBoolean("TwoHeads", hasTwoHeads());
    }
}
