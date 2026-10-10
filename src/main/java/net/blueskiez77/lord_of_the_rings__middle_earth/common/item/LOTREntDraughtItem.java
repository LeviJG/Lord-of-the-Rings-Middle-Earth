package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRHuornEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRTreeEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredTask;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

/**
 * LOTRItemEntDraught: the Ents' drinks, seven of them, served in a bowl.
 *
 * <p>Green quickens, strengthens and hastens; brown is a hearty meal; gold does
 * nothing at all to drink; yellow heals; red wards off fire; silver sees in the
 * dark; blue lets you breathe water. Anyone Fangorn counts an enemy is poisoned
 * for five seconds instead; anyone else earns drinkEntDraught.
 *
 * <p>Gold, poured on an oak, beech or birch sapling by a player Fangorn holds
 * at +500, wakes a Huorn of that tree in its place -- hired to that player, a
 * warrior they need +500 to command -- and earns summonHuorn.
 */
public class LOTREntDraughtItem extends Item implements LOTRTooltipItem {

    private record Draught(int heal, float saturation, List<MobEffectInstance> effects) {
    }

    private static final List<Draught> DRAUGHTS = List.of(
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.SPEED, 120 * 20),
                    new MobEffectInstance(MobEffects.HASTE, 120 * 20), new MobEffectInstance(MobEffects.STRENGTH, 120 * 20))),
            new Draught(20, 3.0f, List.of()),
            new Draught(0, 0.0f, List.of()),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.REGENERATION, 60 * 20))),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 180 * 20))),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.NIGHT_VISION, 180 * 20))),
            new Draught(0, 0.0f, List.of(new MobEffectInstance(MobEffects.WATER_BREATHING, 150 * 20))));

    public static final int COUNT = 7;
    /** The gold draught, which wakes a Huorn. */
    public static final int GOLD = 2;
    private static final float HUORN_ALIGNMENT = 500.0f;

    public LOTREntDraughtItem(Properties properties) {
        super(properties);
    }

    public static ItemStack stack(Item item, int index) {
        ItemStack stack = new ItemStack(item);
        stack.set(LOTRDataComponents.ENT_DRAUGHT, index);
        return stack;
    }

    public static int index(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(LOTRDataComponents.ENT_DRAUGHT, 0), 0, COUNT - 1);
    }

    /** getUnlocalizedName(stack): item.lotr:entDraught.N. */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId() + "." + index(stack));
    }

    /**
     * canPlayerDrink: a draught with an effect can always be drunk; one that is
     * only food (or nothing at all) only by a player who can eat.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (DRAUGHTS.get(index(stack)).effects().isEmpty() && !player.canEat(true)) {
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Draught draught = DRAUGHTS.get(index(stack));
        if (entity instanceof Player player) {
            if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.FANGORN) < 0.0f) {
                if (!level.isClientSide()) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
                }
            } else {
                if (player.canEat(false)) {
                    player.getFoodData().eat(draught.heal(), draught.saturation());
                }
                if (!level.isClientSide()) {
                    for (MobEffectInstance effect : draught.effects()) {
                        player.addEffect(new MobEffectInstance(effect));
                    }
                }
                if (!level.isClientSide()) {
                    LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.DRINK_ENT_DRAUGHT);
                }
            }
        }
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return result;
        }
        return result.isEmpty() ? new ItemStack(Items.BOWL) : result;
    }

    /** onItemUse: the gold draught wakes a Huorn from a sapling. */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player == null || index(stack) != GOLD) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        if (LOTRPlayerAlignments.getAlignment(player, LOTRFaction.FANGORN) < HUORN_ALIGNMENT) {
            if (!level.isClientSide()) {
                LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, HUORN_ALIGNMENT, LOTRFaction.FANGORN);
            }
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        for (int huornType = 0; huornType < LOTRTreeEntity.TYPES.length; ++huornType) {
            if (!state.is(LOTRTreeEntity.saplingBlock(huornType))) {
                continue;
            }
            if (level instanceof ServerLevel server) {
                LOTRHuornEntity huorn = LOTREntities.HUORN.create(server, EntitySpawnReason.MOB_SUMMONED);
                if (huorn == null) {
                    return InteractionResult.PASS;
                }
                huorn.setTreeType(huornType);
                huorn.isNPCPersistent = true;
                huorn.liftSpawnRestrictions = true;
                huorn.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
                if (!huorn.checkSpawnRules(server, EntitySpawnReason.MOB_SUMMONED) || !huorn.checkSpawnObstruction(server)) {
                    continue;
                }
                huorn.initCreatureForHire(server);
                server.addFreshEntity(huorn);
                huorn.hiredNPCInfo.isActive = true;
                huorn.hiredNPCInfo.alignmentRequiredToCommand = HUORN_ALIGNMENT;
                huorn.hiredNPCInfo.setHiringPlayer(player);
                huorn.hiredNPCInfo.setTask(LOTRHiredTask.WARRIOR);
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.SUMMON_HUORN);
                RandomSource random = server.getRandom();
                for (int l = 0; l < 24; ++l) {
                    server.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5 - random.nextDouble() * 2.0 + random.nextDouble() * 2.0,
                            pos.getY() + random.nextDouble() * 4.0,
                            pos.getZ() + 0.5 - random.nextDouble() * 2.0 + random.nextDouble() * 2.0,
                            1, 0.0, 0.0, 0.0, 0.0);
                }
            }
            if (!player.hasInfiniteMaterials()) {
                player.setItemInHand(context.getHand(), new ItemStack(Items.BOWL));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        PotionContents.addPotionTooltip(DRAUGHTS.get(index(stack)).effects(), builder, 1.0f, context.tickRate());
    }
}
