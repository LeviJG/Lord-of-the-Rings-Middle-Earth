package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRZebraEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * LOTREventHandler.onEntityInteract's animal half: a cow (or an aurochs) or a zebra milked into an
 * empty mug, goblet or the like fills it with milk; and a wolf takes the mod's bones as it takes a
 * bone. (The mod's dyes are vanilla dyes already, so they colour a collar as they are.)
 */
public final class LOTRInteractEvents {

    private static final TagKey<Item> BONES = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "bones"));

    private LOTRInteractEvents() {
    }

    public static void init() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> onEntityInteract(player, level, hand, entity));
    }

    private static InteractionResult onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if ((entity instanceof AbstractCow || entity instanceof LOTRZebraEntity) && LOTRVessel.isEmptyDrink(stack)) {
            if (!level.isClientSide()) {
                ItemStack milk = LOTRVessel.of(stack).fill(new ItemStack(LOTRFoodItems.MILK));
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                if (stack.isEmpty() || player.isCreative()) {
                    player.setItemInHand(hand, milk);
                } else if (!player.getInventory().add(milk)) {
                    player.drop(milk, false);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (entity instanceof Wolf wolf && stack.is(BONES) && !stack.is(Items.BONE)) {
            // As the original did: the wolf is shown a bone in its place.
            player.setItemInHand(hand, stack.transmuteCopy(Items.BONE));
            InteractionResult result = wolf.mobInteract(player, hand);
            ItemStack after = player.getItemInHand(hand);
            stack.setCount(after.is(Items.BONE) ? after.getCount() : 0);
            player.setItemInHand(hand, stack.isEmpty() ? ItemStack.EMPTY : stack);
            if (result.consumesAction()) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }
}
