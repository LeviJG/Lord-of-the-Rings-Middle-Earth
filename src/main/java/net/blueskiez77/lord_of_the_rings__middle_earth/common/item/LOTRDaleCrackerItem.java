package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRDaleCrackerMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

/**
 * LOTRItemDaleCracker. Draw one like a bow for two seconds and it goes off with
 * a bang, showering you with whatever was inside: a player's sealed gift if one
 * sealed it, otherwise one or two toys and treats from the Dalish toymakers
 * (more at Christmas).
 *
 * <p>An unsealed cracker -- the kind a Dale crafting table makes -- opens a
 * three-slot window instead, where you put in your own gift and seal it.
 *
 * <p>The original was one item with five colour subtypes; the port keeps its
 * five colours as five items, and the empty bit as {@code lotr:cracker_empty}.
 *
 * <p>NOT ported: the achievement (D7).
 */
public class LOTRDaleCrackerItem extends Item implements LOTRTooltipItem {
    /** getMaxItemUseDuration. */
    public static final int USE_DURATION = 40;
    /** CUSTOM_CAPACITY. */
    public static final int CAPACITY = 3;

    private record Loot(Supplier<ItemStack> item, int min, int max, int weight) {
    }

    /** LOTRChestContents.DALE_CRACKER, entry for entry. */
    private static final List<Loot> LOOT = List.of(
            new Loot(() -> new ItemStack(LOTRFoodItems.CRAM), 1, 4, 25),
            new Loot(() -> new ItemStack(LOTRFoodBlocks.DALISH_PASTRY), 1, 1, 25),
            new Loot(() -> new ItemStack(LOTRFoodItems.MARCHPANE), 1, 3, 25),
            new Loot(() -> new ItemStack(LOTRFoodItems.CHOCOLATE_MARCHPANE), 1, 3, 25),
            new Loot(() -> new ItemStack(Items.COOKIE), 1, 3, 25),
            new Loot(() -> new ItemStack(LOTRFoodItems.ROAST_CHESTNUT), 1, 4, 25),
            new Loot(() -> new ItemStack(LOTRFoodItems.ORANGE), 1, 3, 50),
            new Loot(() -> new ItemStack(LOTRFoodItems.LEMON), 1, 3, 50),
            new Loot(() -> new ItemStack(LOTRFoodItems.RAISINS), 1, 3, 25),
            new Loot(() -> new ItemStack(Items.COMPASS), 1, 1, 25),
            new Loot(() -> new ItemStack(Items.CLOCK), 1, 1, 25),
            new Loot(() -> new ItemStack(LOTRToolItems.SULFUR_MATCH), 1, 8, 25),
            new Loot(() -> new ItemStack(LOTRCombatItems.SLING), 1, 1, 25),
            new Loot(() -> new ItemStack(LOTRCombatItems.PEBBLE), 1, 4, 25),
            new Loot(() -> new ItemStack(Items.COAL), 1, 1, 100),
            new Loot(() -> new ItemStack(Items.STRING), 1, 3, 25),
            new Loot(() -> new ItemStack(Items.FEATHER), 1, 3, 25),
            new Loot(() -> new ItemStack(LOTRMaterialItems.SWAN_FEATHER), 1, 3, 25),
            new Loot(() -> new ItemStack(LOTRMiscItems.LEATHER_HAT), 1, 1, 50),
            new Loot(LOTRDaleCrackerItem::featheredHat, 1, 1, 50),
            new Loot(() -> partyHat(16777215), 1, 1, 25),
            new Loot(() -> partyHat(0), 1, 1, 25),
            new Loot(() -> partyHat(16711680), 1, 1, 25),
            new Loot(() -> partyHat(16227328), 1, 1, 25),
            new Loot(() -> partyHat(16776960), 1, 1, 25),
            new Loot(() -> partyHat(52224), 1, 1, 25),
            new Loot(() -> partyHat(40908), 1, 1, 25),
            new Loot(() -> partyHat(4607), 1, 1, 25),
            new Loot(() -> partyHat(13576667), 1, 1, 25),
            new Loot(() -> new ItemStack(LOTRMiscItems.GOLD_RING), 1, 1, 25),
            new Loot(() -> new ItemStack(LOTRMiscItems.SILVER_RING), 1, 1, 25),
            new Loot(() -> new ItemStack(Items.GOLD_NUGGET), 1, 5, 25),
            new Loot(() -> new ItemStack(LOTRMaterialItems.SILVER_NUGGET), 1, 5, 25),
            new Loot(() -> new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET), 1, 2, 5));

    public LOTRDaleCrackerItem(Properties properties) {
        super(properties);
    }

    public static boolean isEmpty(ItemStack stack) {
        return stack.getOrDefault(LOTRDataComponents.CRACKER_EMPTY, false);
    }

    /** receiveSealingPacket: fill it, name the sealer, and mark it sealed. */
    public static void seal(ItemStack stack, List<ItemStack> contents, Player sealer) {
        stack.remove(LOTRDataComponents.CRACKER_EMPTY);
        stack.set(LOTRDataComponents.CRACKER_SEALER, sealer.getName().getString());
        stack.set(LOTRDataComponents.CRACKER_CONTENTS, ItemContainerContents.fromItems(contents));
    }

    /** setFeatherColor(new ItemStack(leatherHat), 16777215): a hat with a white feather. */
    private static ItemStack featheredHat() {
        ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
        hat.set(LOTRDataComponents.HAT_FEATHER, LOTRLeatherHatItem.FEATHER_WHITE);
        return hat;
    }

    private static ItemStack partyHat(int color) {
        ItemStack hat = new ItemStack(LOTRMiscItems.PARTY_HAT);
        hat.set(DataComponents.DYED_COLOR, new DyedItemColor(color));
        return hat;
    }

    /** LOTRMod.isChristmas: the 24th to the 26th of December. */
    private static boolean isChristmas() {
        LocalDate today = LocalDate.now();
        int day = today.getDayOfMonth();
        return today.getMonth() == Month.DECEMBER && day >= 24 && day <= 26;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isEmpty(stack)) {
            if (!level.isClientSide()) {
                player.openMenu(new SimpleMenuProvider(
                        (containerId, inventory, p) -> new LOTRDaleCrackerMenu(containerId, inventory, hand),
                        Component.translatable("lotr.gui.daleCracker")));
            }
            return InteractionResult.SUCCESS;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    /** onEaten: bang, and out come the contents. */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (isEmpty(stack) || !(entity instanceof Player player)) {
            return stack;
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST,
                SoundSource.PLAYERS, 1.0f, 0.9f + level.getRandom().nextFloat() * 0.1f);
        if (!level.isClientSide()) {
            ItemContainerContents custom = stack.get(LOTRDataComponents.CRACKER_CONTENTS);
            List<ItemStack> loot = custom != null
                    ? custom.nonEmptyItemCopyStream().toList()
                    : randomLoot(level.getRandom());
            for (ItemStack item : loot) {
                if (!player.getInventory().add(item)) {
                    player.drop(item, false);
                }
            }
        }
        if (!player.hasInfiniteMaterials()) {
            stack.shrink(1);
        }
        return stack;
    }

    /**
     * One pick, a second one time in three, and one to four more at Christmas.
     * Each is LOTRChestContents.fillInventory's pick: a weighted entry, a count
     * in its range, a damageable item worn up to three quarters, and the mod's
     * modifiers rolled on (skilful one time in five).
     */
    private static List<ItemStack> randomLoot(RandomSource random) {
        int amount = 1;
        if (random.nextInt(3) == 0) {
            amount++;
        }
        if (isChristmas()) {
            amount += 1 + random.nextInt(4);
        }
        int totalWeight = LOOT.stream().mapToInt(Loot::weight).sum();
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            int pick = random.nextInt(totalWeight);
            Loot entry = LOOT.get(0);
            for (Loot loot : LOOT) {
                pick -= loot.weight();
                if (pick < 0) {
                    entry = loot;
                    break;
                }
            }
            ItemStack stack = entry.item().get();
            stack.setCount(Math.min(Mth.nextInt(random, entry.min(), entry.max()), stack.getMaxStackSize()));
            if (stack.isDamageableItem()) {
                stack.setDamageValue(Mth.floor(stack.getMaxDamage() * Mth.randomBetween(random, 0.0f, 0.75f)));
            }
            LOTRModifiers.applyRandom(stack, random, random.nextInt(5) == 0);
            result.add(stack);
        }
        return result;
    }

    /** getItemStackDisplayName: "Unsealed Dalish Cracker". */
    @Override
    public Component getName(ItemStack stack) {
        Component name = super.getName(stack);
        return isEmpty(stack) ? Component.translatable("item.lotr.cracker.empty", name) : name;
    }

    /** addInformation: a sealed cracker says who sealed it. */
    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        if (isEmpty(stack)) {
            return;
        }
        String sealer = stack.get(LOTRDataComponents.CRACKER_SEALER);
        Component name = sealer != null ? Component.literal(sealer)
                : Component.translatable("item.lotr.cracker.sealedByDale");
        builder.accept(Component.translatable("item.lotr.cracker.sealedBy", name));
    }
}
