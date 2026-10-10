package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifierCombining;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRScrapTraderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRDwarfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChiselItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItemOwnership;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItemTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmithsScrollItem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerAnvil, the block anvil's half: repair with material, combine two
 * of a kind, put a modifier on from a smith's scroll or one of the four
 * special items, reforge, engrave ownership, and rename -- with a colour from a
 * gem, or flint to wash one off. Costs are counted in repair material, not
 * experience.
 *
 * <p>A smith NPC's anvil (the trader half) has no material slot: the work is
 * paid for in coins, priced by what the smith would pay for the item's repair
 * material, and two smith's scrolls of a kind combine into the next grade up.
 *
 * <p>The oddment collector (the scrap trader) works for half a smith's
 * price, but cannot leave a job alone: whatever he reforges, engraves or
 * hands back may come out with its name garbled (four times in five) or a
 * new random modifier (one time in five), and he has a word to say about it
 * as the screen closes.
 *
 * <p>Engraving earns engraveOwnership, reforging at a block anvil reforge,
 * and having two smith's scrolls combined combineSmithScrolls.
 *
 * <p>With the vanilla enchanting system on, vanilla enchantments are combined
 * too, in 26.2's terms: each enchantment's own anvil cost and compatibility
 * rules. With it off, as the original shipped, an enchanted book makes
 * nothing.
 */
public class LOTRAnvilMenu extends AbstractContainerMenu {

    public static final int INPUT = 0;
    public static final int COMBINER = 1;
    public static final int MATERIAL = 2;
    /** The result slot on the block anvil; the smith's, with no material slot, is 2 -- see {@link #resultIndex()}. */
    public static final int RESULT = 3;

    /** maxReforgeTime: how long the slot flashes after a reforge, in ticks. */
    public static final int REFORGE_FLASH = 40;

    /** The most modifiers the anvil will stack on one item. */
    private static final int MAX_MODIFIERS = 3;

    public static final int BUTTON_REFORGE = 0;
    public static final int BUTTON_ENGRAVE = 1;

    private final ContainerLevelAccess access;
    private final Player player;
    /** isTrader: the smith whose anvil this is. */
    private final @Nullable LOTRNPCEntity theNPC;
    private final boolean isTrader;
    private final int resultIndex;
    private final SimpleContainer input = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            slotsChanged(this);
        }
    };
    private final ResultContainer output = new ResultContainer();
    /** 0 material cost, 1 reforge cost, 2 engrave cost, 3 reforges so far (the flash). */
    private final ContainerData data;

    private @Nullable String repairedItemName;
    private long lastReforgeTime = -1L;
    /** Whether the oddment collector has had his fun with something here. */
    private boolean doneMischief;
    /** Whether the output is two smith's scrolls combined. */
    private boolean isSmithScrollCombine;

    public LOTRAnvilMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LOTRAnvilMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        this(LOTRMenus.ANVIL, containerId, inventory, access, null);
    }

    /** The client's smith anvil: the smith as the opening data names it. */
    public LOTRAnvilMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory,
                inventory.player.level().getEntity(entityId) instanceof LOTRNPCEntity npc ? npc : null);
    }

    public LOTRAnvilMenu(int containerId, Inventory inventory, @Nullable LOTRNPCEntity npc) {
        this(LOTRMenus.SMITH, containerId, inventory, ContainerLevelAccess.NULL, npc);
    }

    private LOTRAnvilMenu(MenuType<?> type, int containerId, Inventory inventory, ContainerLevelAccess access,
            @Nullable LOTRNPCEntity npc) {
        super(type, containerId);
        this.access = access;
        this.player = inventory.player;
        this.theNPC = npc;
        this.isTrader = type == LOTRMenus.SMITH;
        this.data = new SimpleContainerData(4);
        addSlot(new Slot(input, INPUT, 27, 58));
        addSlot(new Slot(input, COMBINER, 76, 47));
        if (!this.isTrader) {
            addSlot(new Slot(input, MATERIAL, 76, 70));
        }
        this.resultIndex = this.slots.size();
        addSlot(new ResultSlot(output, 0, 134, 58));
        addStandardInventorySlots(inventory, 8, 116);
        addDataSlots(data);
    }

    public boolean isTrader() {
        return this.isTrader;
    }

    public int resultIndex() {
        return this.resultIndex;
    }

    public static boolean isSmithingWith(Player player, LOTRNPCEntity npc) {
        return player.containerMenu instanceof LOTRAnvilMenu menu && menu.theNPC == npc;
    }

    public int materialCost() {
        return data.get(0);
    }

    public int reforgeCost() {
        return data.get(1);
    }

    public int engraveOwnerCost() {
        return data.get(2);
    }

    /** Counts up by one per reforge; the screen flashes the input slot when it changes. */
    public int reforges() {
        return data.get(3);
    }

    public ItemStack inputItem() {
        return input.getItem(INPUT);
    }

    public ItemStack resultItem() {
        return output.getItem(0);
    }

    // ---------------------------------------------------------------- repair

    /** hasMaterialOrCoinAmount: the coins for a smith; the right material, enough of it, for the block. */
    public boolean hasMaterialOrCoinAmount(int cost) {
        if (this.isTrader) {
            return LOTRCoins.getInventoryValue(this.player) >= cost;
        }
        ItemStack material = input.getItem(MATERIAL);
        return !material.isEmpty() && isRepairMaterial(input.getItem(INPUT), material)
                && material.getCount() >= cost;
    }

    private void takeMaterialOrCoinAmount(int cost) {
        if (this.isTrader) {
            if (!this.player.level().isClientSide() && this.theNPC != null) {
                LOTRCoins.takeCoins(cost, this.player);
                broadcastChanges();
                this.theNPC.playTradeSound();
            }
            return;
        }
        ItemStack material = input.getItem(MATERIAL);
        if (!material.isEmpty()) {
            material.shrink(cost);
            input.setItem(MATERIAL, material.isEmpty() ? ItemStack.EMPTY : material);
        }
    }

    /**
     * isRepairMaterial: the item's own repair material, plus the anvil's extras
     * -- string for a bow or rod, iron for shears and chisels, paper for an
     * enchanted book, planks for Morwaith wood, mallorn planks for mallorn
     * tools, a mallorn log for the charred mace and bones for bone armour, which
     * have no repair item
     * of their own anywhere else.
     */
    public static boolean isRepairMaterial(ItemStack stack, ItemStack material) {
        if (stack.isEmpty() || material.isEmpty()) {
            return false;
        }
        if (stack.isValidRepairItem(material)) {
            return true;
        }
        Item item = stack.getItem();
        if (item == Items.BOW && (material.is(Items.STRING) || material.is(Items.IRON_INGOT))) {
            return true;
        }
        if (item instanceof FishingRodItem && material.is(Items.STRING)) {
            return true;
        }
        if ((item instanceof ShearsItem || item instanceof LOTRChiselItem) && material.is(Items.IRON_INGOT)) {
            return true;
        }
        if (item == Items.ENCHANTED_BOOK && material.is(Items.PAPER)) {
            return true;
        }
        TagKey<Item> repairs = repairTag(stack);
        if (LOTRItemTags.REPAIRS_MORWAITH_WOOD_TOOLS.equals(repairs)) {
            return material.is(net.minecraft.tags.ItemTags.PLANKS);
        }
        if (LOTRItemTags.REPAIRS_MALLORN_TOOLS.equals(repairs)) {
            return material.is(LOTRBuildingBlocks.MALLORN_PLANKS.asItem());
        }
        if (LOTRItemTags.REPAIRS_CHARRED_MALLORN_MACE.equals(repairs)) {
            return material.is(LOTRBuildingBlocks.MALLORN_LOG.asItem());
        }
        // Bone armour: nothing mends it anywhere else (BONE had no crafting
        // item), but this anvil takes any bone.
        if (LOTRItemTags.REPAIRS_BONE_ARMOR.equals(repairs)) {
            return material.is(TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                    net.minecraft.resources.Identifier.fromNamespaceAndPath("lotr", "bones")));
        }
        return false;
    }

    private static @Nullable TagKey<Item> repairTag(ItemStack stack) {
        var repairable = stack.get(DataComponents.REPAIRABLE);
        if (repairable == null) {
            return null;
        }
        return repairable.items().unwrapKey().orElse(null);
    }

    /**
     * getTraderMaterialPrice: what one of the item's repair material costs at
     * this smith -- from its current sell trades, else from all it could sell;
     * a dwarf will price mithril at 200 though it never buys it.
     */
    private float getTraderMaterialPrice(ItemStack inputItem) {
        float materialPrice = 0.0f;
        if (this.theNPC == null || this.theNPC.traderNPCInfo == null) {
            return materialPrice;
        }
        for (LOTRTradeEntry trade : this.theNPC.traderNPCInfo.getSellTrades()) {
            ItemStack tradeItem = trade.createTradeItem();
            if (isRepairMaterial(inputItem, tradeItem)) {
                materialPrice = (float) trade.getCost() / tradeItem.getCount();
                break;
            }
        }
        if (materialPrice <= 0.0f) {
            for (LOTRTradeEntry trade : ((LOTRTradeable) this.theNPC).getSellPool().createAllTrades()) {
                ItemStack tradeItem = trade.createTradeItem();
                if (isRepairMaterial(inputItem, tradeItem)) {
                    materialPrice = (float) trade.getCost() / tradeItem.getCount();
                    break;
                }
            }
        }
        if (materialPrice <= 0.0f && (isRepairMaterial(inputItem, new ItemStack(LOTRItems.MITHRIL))
                || isRepairMaterial(inputItem, new ItemStack(LOTRMaterialItems.MITHRIL_MAIL)))
                && this.theNPC instanceof LOTRDwarfEntity) {
            materialPrice = 200.0f;
        }
        return materialPrice;
    }

    /** costsToRename: gear -- weapons, tools, protective armour, launchers, throwing axes. */
    private static boolean costsToRename(ItemStack stack) {
        var kinds = LOTRModifiers.kindsOf(stack);
        return kinds.contains(LOTRModifier.Kind.MELEE) || kinds.contains(LOTRModifier.Kind.TOOL)
                || kinds.contains(LOTRModifier.Kind.ARMOR) || kinds.contains(LOTRModifier.Kind.RANGED_LAUNCHER)
                || kinds.contains(LOTRModifier.Kind.THROWING_AXE);
    }

    /** AnvilNameColorProvider: the gems that colour a name. */
    private static @Nullable ChatFormatting nameColour(ItemStack stack) {
        if (stack.is(LOTRMaterialItems.DURNOR)) {
            return ChatFormatting.DARK_RED;
        }
        if (stack.is(LOTRMaterialItems.TOPAZ)) {
            return ChatFormatting.GOLD;
        }
        if (stack.is(LOTRMaterialItems.AMETHYST)) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (stack.is(LOTRMaterialItems.SAPPHIRE)) {
            return ChatFormatting.BLUE;
        }
        if (stack.is(LOTRMaterialItems.RUBY)) {
            return ChatFormatting.RED;
        }
        if (stack.is(LOTRMaterialItems.AMBER)) {
            return ChatFormatting.YELLOW;
        }
        if (stack.is(LOTRMaterialItems.DIAMOND)) {
            return ChatFormatting.GRAY;
        }
        if (stack.is(LOTRMaterialItems.OPAL)) {
            return ChatFormatting.AQUA;
        }
        if (stack.is(LOTRMaterialItems.EMERALD)) {
            return ChatFormatting.GREEN;
        }
        // LOTRItemWithAnvilNameColor: the two crystals as well as the gems.
        if (stack.is(LOTRMaterialItems.EDHELVIR)) {
            return ChatFormatting.DARK_AQUA;
        }
        if (stack.is(LOTRMaterialItems.GULDURIL)) {
            return ChatFormatting.DARK_GREEN;
        }
        return null;
    }

    /** getAppliedFormattingCodes: the colour a name is written in, if any. */
    public static @Nullable ChatFormatting nameColourOf(ItemStack stack) {
        Component name = stack.get(DataComponents.CUSTOM_NAME);
        if (name == null || name.getStyle().getColor() == null) {
            return null;
        }
        TextColor colour = name.getStyle().getColor();
        for (ChatFormatting format : ChatFormatting.values()) {
            if (colour.equals(TextColor.fromLegacyFormat(format))) {
                return format;
            }
        }
        return null;
    }

    /** updateItemName: what the player has typed in the rename box. */
    public void setItemName(String name) {
        String filtered = StringUtil.filterText(name);
        this.repairedItemName = filtered;
        updateRepairOutput();
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == input) {
            updateRepairOutput();
        }
    }

    // --------------------------------------------------------------- output

    /** updateRepairOutput. */
    private void updateRepairOutput() {
        this.isSmithScrollCombine = false;
        ItemStack inputItem = input.getItem(INPUT);
        int materialCost = 0;
        int reforgeCost = 0;
        int engraveCost = 0;
        if (inputItem.isEmpty()) {
            output.setItem(0, ItemStack.EMPTY);
            setCosts(0, 0, 0);
            return;
        }

        ItemStack inputCopy = inputItem.copy();
        ItemStack combiner = input.getItem(COMBINER);
        ItemStack material = this.isTrader ? ItemStack.EMPTY : input.getItem(MATERIAL);
        int baseAnvilCost = LOTRModifiers.getAnvilCost(inputItem)
                + (combiner.isEmpty() ? 0 : LOTRModifiers.getAnvilCost(combiner));
        int repairCost = 0;
        int combineCost = 0;
        int renameCost = 0;

        // The name, and a colour from a gem or washed off with flint.
        Component defaultName = inputCopy.getItem().getName(inputCopy.getItem().getDefaultInstance());
        String previousName = inputCopy.getHoverName().getString();
        ChatFormatting colour = nameColourOf(inputCopy);
        boolean alteringColour = false;
        if (costsToRename(inputItem) && !combiner.isEmpty()) {
            ChatFormatting gem = nameColour(combiner);
            if (gem != null && gem != colour) {
                colour = gem;
                alteringColour = true;
            } else if (combiner.is(Items.FLINT) && colour != null) {
                colour = null;
                alteringColour = true;
            }
            if (alteringColour) {
                ++renameCost;
            }
        }
        String typed = repairedItemName;
        boolean nameChange = false;
        String wanted = typed == null ? previousName : typed;
        if (wanted.isBlank() || wanted.equals(defaultName.getString())) {
            if (colour != null) {
                inputCopy.set(DataComponents.CUSTOM_NAME, defaultName.copy().withStyle(colour));
            } else {
                inputCopy.remove(DataComponents.CUSTOM_NAME);
            }
            nameChange = inputItem.has(DataComponents.CUSTOM_NAME) && !previousName.equals(defaultName.getString());
        } else {
            Component named = Component.literal(wanted);
            inputCopy.set(DataComponents.CUSTOM_NAME, colour == null ? named : named.copy().withStyle(colour));
            nameChange = !wanted.equals(previousName);
        }
        if (nameChange && costsToRename(inputItem)) {
            ++renameCost;
        }

        LOTRModifierCombining.CombineRecipe scrollCombine;
        if (this.isTrader && (scrollCombine = LOTRModifierCombining.getCombinationResult(inputItem, combiner)) != null) {
            output.setItem(0, scrollCombine.createOutputItem());
            setCosts(scrollCombine.cost(), 0, 0);
            this.isSmithScrollCombine = true;
            return;
        }

        boolean combining = false;
        if (!combiner.isEmpty()) {
            boolean withBook = combiner.is(Items.ENCHANTED_BOOK)
                    && !combiner.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
            if (withBook && !LOTRConfig.enchantingVanilla) {
                output.setItem(0, ItemStack.EMPTY);
                setCosts(0, 0, 0);
                return;
            }
            LOTRModifier combinerModifier = specialModifier(combiner);
            if (!withBook && combinerModifier == null) {
                if (inputCopy.isDamageableItem() && inputCopy.getItem() == combiner.getItem()) {
                    int inputUseLeft = inputItem.getMaxDamage() - inputItem.getDamageValue();
                    int combinerUseLeft = combiner.getMaxDamage() - combiner.getDamageValue();
                    int restoredUses = combinerUseLeft + inputCopy.getMaxDamage() * 12 / 100;
                    int newDamage = Math.max(inputCopy.getMaxDamage() - (inputUseLeft + restoredUses), 0);
                    if (newDamage < inputCopy.getDamageValue()) {
                        inputCopy.setDamageValue(newDamage);
                        int restored1 = inputCopy.getMaxDamage() - inputUseLeft;
                        int restored2 = inputCopy.getMaxDamage() - combinerUseLeft;
                        combineCost += Math.max(0, Math.min(restored1, restored2) / 100);
                    }
                    combining = true;
                } else if (!alteringColour) {
                    output.setItem(0, ItemStack.EMPTY);
                    setCosts(0, 0, 0);
                    return;
                }
            }

            if (LOTRConfig.enchantingVanilla) {
                combineCost += combineEnchantments(inputItem, inputCopy, combiner);
            }

            List<LOTRModifier> outputMods = new ArrayList<>(LOTRModifiers.get(inputCopy));
            List<LOTRModifier> combinerMods = new ArrayList<>(LOTRModifiers.get(combiner));
            if (combinerModifier != null) {
                combinerMods.add(combinerModifier);
                // The Flame of Udûn burns the poison off a blade.
                if (combinerModifier == LOTRModifier.FIRE) {
                    Item plain = unpoisoned(inputCopy.getItem());
                    if (plain != null) {
                        inputCopy = inputCopy.transmuteCopy(plain);
                    }
                }
            }
            for (LOTRModifier mod : combinerMods) {
                boolean canApply = LOTRModifiers.canApply(mod, inputItem, false);
                if (canApply) {
                    for (LOTRModifier existing : outputMods) {
                        if (!existing.isCompatibleWith(mod) || !mod.isCompatibleWith(existing)) {
                            canApply = false;
                        }
                    }
                }
                int counted = 0;
                for (LOTRModifier existing : outputMods) {
                    if (!existing.bypassAnvilLimit()) {
                        ++counted;
                    }
                }
                if (!mod.bypassAnvilLimit() && counted >= MAX_MODIFIERS) {
                    canApply = false;
                }
                if (!canApply) {
                    continue;
                }
                outputMods.add(mod);
                if (mod.isBeneficial()) {
                    combineCost += Math.max(1, (int) mod.valueModifier());
                }
            }
            LOTRModifiers.set(inputCopy, outputMods);
        }
        if (combineCost > 0) {
            combining = true;
        }

        for (var entry : EnchantmentHelper.getEnchantmentsForCrafting(inputItem).entrySet()) {
            baseAnvilCost += 1 + entry.getIntValue() * entry.getKey().value().getAnvilCost();
        }
        for (LOTRModifier mod : LOTRModifiers.get(inputItem)) {
            if (mod.isBeneficial()) {
                baseAnvilCost += Math.max(1, (int) mod.valueModifier());
            }
        }

        if (inputCopy.isDamageableItem()) {
            boolean canRepair;
            int available;
            if (this.isTrader) {
                canRepair = getTraderMaterialPrice(inputItem) > 0.0f;
                available = Integer.MAX_VALUE;
            } else {
                canRepair = !material.isEmpty() && isRepairMaterial(inputItem, material);
                available = material.isEmpty() ? 0 : material.getCount() - combineCost - renameCost;
            }
            int oneItemRepair = Math.min(inputCopy.getDamageValue(), inputCopy.getMaxDamage() / 4);
            if (canRepair && available > 0 && oneItemRepair > 0) {
                available -= baseAnvilCost;
                if (available > 0) {
                    int used = 0;
                    while (oneItemRepair > 0 && used < available) {
                        inputCopy.setDamageValue(inputCopy.getDamageValue() - oneItemRepair);
                        oneItemRepair = Math.min(inputCopy.getDamageValue(), inputCopy.getMaxDamage() / 4);
                        ++used;
                    }
                    repairCost += used;
                } else if (!nameChange && !combining) {
                    repairCost = 1;
                    inputCopy.setDamageValue(inputCopy.getDamageValue() - oneItemRepair);
                }
            }
        }
        boolean repairing = repairCost > 0;
        materialCost = combining || repairing ? baseAnvilCost + combineCost + repairCost : 0;
        materialCost += renameCost;

        int nextAnvilCost = LOTRModifiers.getAnvilCost(inputItem);
        if (!combiner.isEmpty()) {
            nextAnvilCost = Math.max(nextAnvilCost, LOTRModifiers.getAnvilCost(combiner));
        }
        if (combining) {
            nextAnvilCost += 2;
        } else if (repairing) {
            ++nextAnvilCost;
        }
        if (nextAnvilCost > 0) {
            LOTRModifiers.setAnvilCost(inputCopy, nextAnvilCost);
        }

        if (LOTRModifiers.isReforgeable(inputItem)) {
            reforgeCost = inputItem.has(DataComponents.EQUIPPABLE)
                    && LOTRModifiers.kindsOf(inputItem).contains(LOTRModifier.Kind.ARMOR) ? 3 : 2;
            if (inputItem.isDamageableItem()) {
                ItemStack reforgeCopy = inputItem.copy();
                int oneItemRepair = Math.min(reforgeCopy.getDamageValue(), reforgeCopy.getMaxDamage() / 4);
                while (oneItemRepair > 0) {
                    reforgeCopy.setDamageValue(reforgeCopy.getDamageValue() - oneItemRepair);
                    oneItemRepair = Math.min(reforgeCopy.getDamageValue(), reforgeCopy.getMaxDamage() / 4);
                    ++reforgeCost;
                }
            }
            engraveCost = 2;
        }

        // String is cheap: a bow's costs are tripled.
        if (isRepairMaterial(inputItem, new ItemStack(Items.STRING))) {
            materialCost *= 3;
            reforgeCost *= 3;
            engraveCost *= 3;
        }

        // A smith charges coins: the material it would have taken, at its price.
        if (this.isTrader) {
            boolean isCommonRenameOnly = nameChange && materialCost == 0;
            float materialPrice = getTraderMaterialPrice(inputItem);
            if (materialPrice > 0.0f) {
                materialCost = Math.max(Math.round(materialCost * materialPrice), 1);
                reforgeCost = Math.max(Math.round(reforgeCost * materialPrice), 1);
                engraveCost = Math.max(Math.round(engraveCost * materialPrice), 1);
                if (this.theNPC instanceof LOTRScrapTraderEntity) {
                    materialCost = Math.max(Mth.ceil(materialCost * 0.5f), 1);
                    reforgeCost = Math.max(Mth.ceil(reforgeCost * 0.5f), 1);
                    engraveCost = Math.max(Mth.ceil(engraveCost * 0.5f), 1);
                }
            } else if (!isCommonRenameOnly) {
                output.setItem(0, ItemStack.EMPTY);
                setCosts(0, 0, 0);
                return;
            }
        }

        if (combining || repairing || nameChange || alteringColour) {
            output.setItem(0, inputCopy);
        } else {
            output.setItem(0, ItemStack.EMPTY);
            materialCost = 0;
        }
        setCosts(materialCost, reforgeCost, engraveCost);
        broadcastChanges();
    }

    private void setCosts(int material, int reforge, int engrave) {
        data.set(0, material);
        data.set(1, reforge);
        data.set(2, engrave);
    }

    /**
     * LOTRItemEnchantment and LOTRItemModifierTemplate: the four special items
     * and a smith's scroll each carry one modifier to put on.
     */
    private static @Nullable LOTRModifier specialModifier(ItemStack stack) {
        if (stack.is(LOTRMaterialItems.FLAME_OF_UDUN)) {
            return LOTRModifier.FIRE;
        }
        if (stack.is(LOTRMaterialItems.CHILL_OF_DAEDELOS)) {
            return LOTRModifier.CHILL;
        }
        if (stack.is(LOTRMaterialItems.HEADHUNTERS_TROPHY)) {
            return LOTRModifier.HEADHUNTING;
        }
        if (stack.is(LOTRMaterialItems.BOOK_OF_TRUE_SILVER)) {
            return LOTRModifier.PROTECT_MITHRIL;
        }
        if (stack.getItem() instanceof LOTRSmithsScrollItem) {
            return LOTRSmithsScrollItem.getModifier(stack);
        }
        return null;
    }

    /** LOTRRecipePoisonWeapon.poisonedToInput: a poisoned dagger's plain one. */
    private static @Nullable Item unpoisoned(Item item) {
        var id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
        if (id.getNamespace().equals("lotr") && id.getPath().startsWith("poisoned_") && id.getPath().endsWith("_dagger")) {
            return net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(
                    id.withPath(id.getPath().substring("poisoned_".length())));
        }
        return null;
    }

    /**
     * The enchantingVanilla half of combining: the combiner's enchantments go
     * onto the copy by the anvil's rules -- matching levels step up, the higher
     * otherwise, capped at the maximum; incompatible ones cost and are refused.
     * Returns what it adds to the combine cost.
     */
    private int combineEnchantments(ItemStack inputItem, ItemStack inputCopy, ItemStack combiner) {
        int cost = 0;
        ItemEnchantments.Mutable out = new ItemEnchantments.Mutable(
                EnchantmentHelper.getEnchantmentsForCrafting(inputCopy));
        ItemEnchantments incoming = EnchantmentHelper.getEnchantmentsForCrafting(combiner);
        for (var entry : incoming.entrySet()) {
            Holder<Enchantment> ench = entry.getKey();
            int inputLevel = out.getLevel(ench);
            int combinerLevel = entry.getIntValue();
            int level = inputLevel == combinerLevel ? combinerLevel + 1 : Math.max(combinerLevel, inputLevel);
            int added = level - inputLevel;
            boolean canApply = ench.value().canEnchant(inputItem) || player.hasInfiniteMaterials()
                    || inputItem.is(Items.ENCHANTED_BOOK);
            for (Holder<Enchantment> existing : out.keySet()) {
                if (!existing.equals(ench) && !Enchantment.areCompatible(existing, ench)) {
                    canApply = false;
                    cost += added;
                }
            }
            if (!canApply) {
                continue;
            }
            out.set(ench, Math.min(level, ench.value().getMaxLevel()));
            cost += ench.value().getAnvilCost() * added;
        }
        EnchantmentHelper.setEnchantments(inputCopy, out.toImmutable());
        return cost;
    }

    // --------------------------------------------------------------- buttons

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == BUTTON_REFORGE) {
            return reforge();
        }
        if (id == BUTTON_ENGRAVE) {
            return engraveOwnership();
        }
        return false;
    }

    /** reforgeItem: fully mended, and its modifiers rolled again as a skilled smith would. */
    private boolean reforge() {
        long now = System.currentTimeMillis();
        ItemStack inputItem = input.getItem(INPUT);
        int cost = reforgeCost();
        if (lastReforgeTime >= 0L && now - lastReforgeTime < 2000L || inputItem.isEmpty() || cost <= 0
                || !hasMaterialOrCoinAmount(cost)) {
            return false;
        }
        if (inputItem.isDamageableItem()) {
            inputItem.setDamageValue(0);
        }
        LOTRModifiers.applyRandom(inputItem, player.getRandom(), true, true);
        LOTRModifiers.setAnvilCost(inputItem, 0);
        if (isScrapTrader() && applyMischief(inputItem)) {
            this.doneMischief = true;
        }
        input.setItem(INPUT, inputItem);
        takeMaterialOrCoinAmount(cost);
        playAnvilSound();
        lastReforgeTime = now;
        data.set(3, reforges() + 1);
        if (!this.isTrader) {
            LOTRPlayerAchievements.addAchievement(this.player, LOTRAchievement.REFORGE);
        }
        return true;
    }

    /** engraveOwnership: put the player's name on it, if it is not theirs already. */
    private boolean engraveOwnership() {
        ItemStack inputItem = input.getItem(INPUT);
        int cost = engraveOwnerCost();
        if (inputItem.isEmpty() || cost <= 0 || !hasMaterialOrCoinAmount(cost) || !canEngraveNewOwner(inputItem, player)) {
            return false;
        }
        LOTRItemOwnership.setCurrentOwner(inputItem, player.getName().getString());
        if (isScrapTrader() && applyMischief(inputItem)) {
            this.doneMischief = true;
        }
        input.setItem(INPUT, inputItem);
        takeMaterialOrCoinAmount(cost);
        playAnvilSound();
        LOTRPlayerAchievements.addAchievement(this.player, LOTRAchievement.ENGRAVE_OWNERSHIP);
        return true;
    }

    private boolean isScrapTrader() {
        return this.isTrader && this.theNPC instanceof LOTRScrapTraderEntity
                && !this.player.level().isClientSide();
    }

    /**
     * applyMischief: four times in five the name is garbled
     * (OddmentCollectorNameMischief; garbled back into the item's own name, the
     * custom name goes), and one time in five a random modifier is added.
     */
    private boolean applyMischief(ItemStack stack) {
        boolean changed = false;
        RandomSource rand = this.player.level().getRandom();
        if (rand.nextFloat() < 0.8f) {
            Component custom = stack.get(DataComponents.CUSTOM_NAME);
            String name = LOTRNameMischief.garbleName(stack.getHoverName().getString(), rand);
            if (name.equals(stack.getItem().getName(stack).getString())
                    && (custom == null || custom.getStyle().isEmpty())) {
                stack.remove(DataComponents.CUSTOM_NAME);
            } else {
                stack.set(DataComponents.CUSTOM_NAME,
                        Component.literal(name).withStyle(custom == null ? Style.EMPTY : custom.getStyle()));
            }
            changed = true;
        }
        if (rand.nextFloat() < 0.2f) {
            LOTRModifiers.applyRandom(stack, rand, false, true);
            changed = true;
        }
        return changed;
    }

    /**
     * slotClick: taking the result from the oddment collector, the player gets
     * it with his mischief done; the clean result stays if it is not taken.
     */
    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        ItemStack result = output.getItem(0).copy();
        boolean changed = false;
        if (slotIndex == this.resultIndex && !result.isEmpty() && isScrapTrader()) {
            ItemStack mischief = result.copy();
            changed = applyMischief(mischief);
            if (changed) {
                output.setItem(0, mischief);
            }
        }
        super.clicked(slotIndex, buttonNum, containerInput, player);
        if (changed) {
            this.doneMischief = true;
            if (!output.getItem(0).isEmpty()) {
                output.setItem(0, result);
            }
        }
    }

    public static boolean canEngraveNewOwner(ItemStack stack, Player player) {
        String owner = LOTRItemOwnership.getCurrentOwner(stack);
        return owner == null || !owner.equals(player.getName().getString());
    }

    /** 1021 in 1.7.10, "random.anvil_use": the anvil-used level event, 1030 now -- at the smith for a smith. */
    private void playAnvilSound() {
        if (this.isTrader) {
            if (this.theNPC != null && !this.player.level().isClientSide()) {
                this.player.level().levelEvent(1030, this.theNPC.blockPosition(), 0);
            }
            return;
        }
        access.execute((level, pos) -> level.levelEvent(1030, pos, 0));
    }

    // ---------------------------------------------------------------- slots

    /** LOTRSlotAnvilOutput. */
    private class ResultSlot extends Slot {
        ResultSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            if (!hasItem()) {
                return false;
            }
            return materialCost() <= 0 || hasMaterialOrCoinAmount(materialCost());
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            int materials = materialCost();
            input.setItem(INPUT, ItemStack.EMPTY);
            ItemStack combiner = input.getItem(COMBINER);
            if (!combiner.isEmpty()) {
                combiner.shrink(1);
                input.setItem(COMBINER, combiner.isEmpty() ? ItemStack.EMPTY : combiner);
            }
            if (materials > 0) {
                takeMaterialOrCoinAmount(materials);
            }
            data.set(0, 0);
            playAnvilSound();
            if (!player.level().isClientSide() && isSmithScrollCombine) {
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.COMBINE_SMITH_SCROLLS);
            }
            isSmithScrollCombine = false;
            super.onTake(player, stack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.isTrader) {
            LOTRNPCEntity npc = this.theNPC;
            return npc != null && player.distanceTo(npc) <= 12.0 && npc.isAlive() && npc.getTarget() == null
                    && ((LOTRTradeable) npc).canTradeWith(player);
        }
        return stillValid(access, player, LOTRUtilityBlocks.ANVIL);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (this.isTrader) {
            if (!player.level().isClientSide()) {
                clearContainer(player, input);
                if (this.doneMischief && this.theNPC instanceof LOTRScrapTraderEntity scrapTrader) {
                    scrapTrader.sendSpeechBank(player, scrapTrader.getSmithSpeechBank());
                }
            }
            return;
        }
        access.execute((level, pos) -> clearContainer(player, input));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack moved = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            moved = stack.copy();
            int invStart = this.resultIndex + 1;
            int invEnd = invStart + 36;
            if (index == this.resultIndex) {
                if (!moveItemStackTo(stack, invStart, invEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, moved);
            } else if (index >= invStart) {
                if (!moveItemStackTo(stack, INPUT, this.resultIndex, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, invStart, invEnd, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == moved.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return moved;
    }
}
