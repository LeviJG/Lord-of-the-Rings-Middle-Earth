package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHobbitBartender: keeps a tavern, which its speech names after it
 * ("%s's Tavern"). Hobbits who drink near a friendly bartender may get drunk.
 * Slain, it leaves a tavern's odds and ends instead of a hobbit hole's.
 *
 * <p>NOT ported yet: the tradeBartender and sellPipeweedLeaf achievements.
 */
public class LOTRHobbitBartenderEntity extends LOTRHobbitEntity implements LOTRBartender {

    public LOTRHobbitBartenderEntity(EntityType<? extends LOTRHobbitBartenderEntity> type, Level level) {
        super(type, level);
        this.npcLocationName = "entity.lotr.hobbit_bartender.locationName";
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.HOBBIT_BARTENDER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.HOBBIT_BARTENDER_SELL;
    }

    @Override
    public boolean canTradeWith(Player player) {
        return isFriendly(player);
    }

    @Override
    protected void dropHobbitItems(ServerLevel level, int looting) {
        int count = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int k = 0; k < count; ++k) {
            ItemStack drop = switch (this.random.nextInt(10)) {
                case 0, 1 -> LOTRFoods.HOBBIT.getRandomFood(this.random);
                case 2 -> new ItemStack(Items.GOLD_NUGGET, 2 + this.random.nextInt(3));
                case 3 -> new ItemStack(Items.BOWL, 1 + this.random.nextInt(4));
                // A pipe worn by up to 99 uses.
                case 4 -> damaged(new ItemStack(LOTRMiscItems.SMOKING_PIPE), this.random.nextInt(100));
                case 5 -> new ItemStack(LOTRItems.PIPEWEED, 1 + this.random.nextInt(2));
                case 6, 7, 8 -> new ItemStack(LOTRFoodItems.MUG);
                default -> {
                    // A drink from the list, in a mug, light to strong.
                    ItemStack drink = new ItemStack(LOTRFoods.HOBBIT_DRINK.getRandomFood(this.random).getItem());
                    drink.set(LOTRDataComponents.DRINK_STRENGTH, 1 + this.random.nextInt(3));
                    yield drink;
                }
            };
            spawnAtLocation(level, drop, 0.0f);
        }
    }

    private static ItemStack damaged(ItemStack stack, int damage) {
        if (stack.isDamageableItem()) {
            stack.setDamageValue(Math.min(damage, stack.getMaxDamage() - 1));
        }
        return stack;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "hobbit/bartender/friendly" : "hobbit/bartender/hostile";
    }
}
