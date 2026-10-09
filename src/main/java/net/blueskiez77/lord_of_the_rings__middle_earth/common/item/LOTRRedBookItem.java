package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestEvent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuests;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * LOTRItemRedBook: opens the red book of the player's mini-quests -- reading it moves the Grey
 * Wanderer's welcome along -- and tells, in its tooltip, how many quests are in hand.
 */
public class LOTRRedBookItem extends Item implements LOTRTooltipItem {

    /** Opens the red book; set by the client. */
    public static Runnable openScreen = () -> {
    };
    /** The player's quests in hand, as the client knows them; set by the client. */
    public static IntSupplier activeQuests = () -> 0;

    public LOTRRedBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            openScreen.run();
        } else {
            LOTRMiniQuests.forPlayer(player.getUUID()).distributeMQEvent(LOTRMiniQuestEvent.OPEN_RED_BOOK);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.lotr.redBook.activeQuests", activeQuests.getAsInt()));
    }
}
