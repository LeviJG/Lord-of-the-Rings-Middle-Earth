package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;


import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRAnimalJarBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.TagValueOutput;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LOTRItemAnimalJar: the item form of a bird cage.
 *
 * <p>Right-click a creature the cage can hold and it goes into the stack --
 * written out as its own NBT, then killed, exactly as the original did. Placing
 * the cage puts the creature into the block entity; breaking it puts the
 * creature back into the stack. It never exists as a live entity in between.
 *
 * <p>What a given cage can catch is a block tag rather than a hardcoded class.
 * The original asked {@code entity instanceof LOTREntityBird}, and LOTREntityBird
 * is not ported, so the tag is where the answer lives until it is.
 *
 * <p>Stack size 1, because a cage carrying a creature cannot sensibly stack with
 * an empty one.
 */
public class LOTRAnimalJarItem extends BlockItem {
    private static final Logger LOGGER = LoggerFactory.getLogger(LOTRAnimalJarItem.class);

    /** What this particular jar or cage will catch. */
    private final TagKey<EntityType<?>> catchable;

    public LOTRAnimalJarItem(Block block, TagKey<EntityType<?>> catchable, Properties properties) {
        super(block, properties);
        this.catchable = catchable;
    }

    /** The captured creature's NBT, or null for an empty cage. */
    public static @Nullable CompoundTag getJarEntity(ItemStack stack) {
        TypedEntityData<BlockEntityType<?>> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) {
            return null;
        }
        CompoundTag tag = data.copyTagWithoutId()
                .getCompound(LOTRAnimalJarBlockEntity.ENTITY_TAG).orElse(null);
        return tag == null || tag.isEmpty() ? null : tag;
    }

    /**
     * Writes the creature into the stack's block_entity_data, the same component
     * a spawner or a shulker box travels in, so the vanilla placement path
     * copies it onto the block entity with no extra work.
     */
    public static void setJarEntity(ItemStack stack, @Nullable CompoundTag entity) {
        try (ProblemReporter.ScopedCollector scope = new ProblemReporter.ScopedCollector(LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithoutContext(scope);
            if (entity != null) {
                output.store(LOTRAnimalJarBlockEntity.ENTITY_TAG, CompoundTag.CODEC, entity);
            }
            BlockItem.setBlockEntityData(stack, LOTRBlockEntities.ANIMAL_JAR, output);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
            LivingEntity entity, InteractionHand hand) {
        if (getJarEntity(stack) != null || !BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).is(catchable)) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        CompoundTag saved;
        try (ProblemReporter.ScopedCollector scope = new ProblemReporter.ScopedCollector(LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithoutContext(scope);
            if (!entity.saveAsPassenger(output)) {
                return InteractionResult.PASS;
            }
            saved = output.buildResult();
        }

        ItemStack filled = stack.copyWithCount(1);
        setJarEntity(filled, saved);
        player.setItemInHand(hand, filled);

        // "random.pop", which is today's item-pickup pop.
        entity.level().playSound(null, entity.blockPosition(), SoundEvents.ITEM_PICKUP,
                entity.getSoundSource(), 0.5F,
                0.5F + entity.level().getRandom().nextFloat() * 0.5F);
        entity.discard();
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTRItemAnimalJar.onItemRightClick: the creature is let out two blocks
     * ahead of the player's eyes, along where they are looking, and the jar is
     * left empty. Only the held item does this; a placed jar keeps its creature.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag data = getJarEntity(stack);
        if (data == null) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 at = eye.add(player.getLookAngle().scale(2.0));
        Entity entity = EntityType.loadEntityRecursive(data, level,
                new EntitySpawnRequest(EntitySpawnReason.BUCKET, true), e -> {
                    e.snapTo(at.x, at.y, at.z, level.getRandom().nextFloat(), 0.0F);
                    return e;
                });
        if (entity == null) {
            return InteractionResult.PASS;
        }
        level.addFreshEntity(entity);
        level.playSound(null, entity.blockPosition(), SoundEvents.ITEM_PICKUP, entity.getSoundSource(),
                0.5F, 0.5F + level.getRandom().nextFloat() * 0.5F);
        setJarEntity(stack, null);
        return InteractionResult.SUCCESS;
    }
}
