package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRItemHobbitPipe: draw on it for two seconds and, if you have pipeweed,
 * you burn a leaf for a little food and a puff of smoke.
 *
 * <p>The smoke colour is the pipe's own, set by crafting it with a dye -- 0 to
 * 15 are the dye colours and 16 is the mithril-touched magic smoke, exactly as
 * LOTRRecipeHobbitPipe set them.
 */
public class LOTRSmokingPipeItem extends Item {
    /** getMaxItemUseDuration */
    public static final int USE_DURATION = 40;

    public static final int MAGIC_COLOR = 16;
    public static final int COLORS = 17;

    public LOTRSmokingPipeItem(Properties properties) {
        super(properties);
    }

    public static int getSmokeColor(ItemStack stack) {
        Integer colour = stack.get(LOTRDataComponents.SMOKE_COLOR);
        return colour == null ? 0 : colour;
    }

    public static ItemStack of(int smokeColour) {
        ItemStack stack = new ItemStack(LOTRItems.SMOKING_PIPE);
        stack.set(LOTRDataComponents.SMOKE_COLOR, smokeColour);
        return stack;
    }

    /** addInformation: the one line naming the smoke. */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.lotr.smoking_pipe.subtitle." + getSmokeColor(stack))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    /** onEaten: a leaf of pipeweed for two half-hearts and a puff. */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!(user instanceof Player player)) {
            return stack;
        }
        boolean free = player.hasInfiniteMaterials();
        if (!free && !takePipeweed(player)) {
            return stack;
        }
        stack.hurtAndBreak(1, player, player.getUsedItemHand());
        if (player.canEat(false)) {
            player.getFoodData().eat(2, 0.3f);
        }
        if (level instanceof ServerLevel server) {
            smoke(server, player, getSmokeColor(stack));
        }
        return stack;
    }

    private static boolean takePipeweed(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(LOTRItems.PIPEWEED)) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    private static void smoke(ServerLevel server, Player player, int smokeColour) {
        ParticleOptions particle = smokeColour == MAGIC_COLOR
                ? ParticleTypes.ENCHANT
                : new DustParticleOptions(DyeColor.byId(smokeColour).getTextureDiffuseColor(), 1.0f);
        server.sendParticles(particle, player.getX(), player.getEyeY() + 0.2, player.getZ(),
                8, 0.15, 0.1, 0.15, 0.02);
    }
}
