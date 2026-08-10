package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.List;
import java.util.Optional;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRRecipeTypes;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

// Mirrors vanilla CraftingMenu. It cannot subclass it: CraftingMenu's constructor hard-calls super(MenuType.CRAFTING, ...), and its recipe lookup is a protected static with RecipeType.CRAFTING baked in. The only real difference is the type passed to getRecipeFor, which is what gates the table.
public class LOTRCraftingMenu extends AbstractCraftingMenu {

    private final ContainerLevelAccess access;
    private final Player player;
    private final LOTRCraftingTable table;

    public LOTRCraftingMenu(int containerId, Inventory inventory, LOTRCraftingTable table) {
        this(containerId, inventory, ContainerLevelAccess.NULL, table);
    }

    public LOTRCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access, LOTRCraftingTable table) {
        super(LOTRMenus.forTable(table), containerId, 3, 3);
        this.access = access;
        this.player = inventory.player;
        this.table = table;
        addResultSlot(this.player, 124, 35);
        addCraftingGridSlots(30, 17);
        addStandardInventorySlots(inventory, 8, 84);
    }

    public LOTRCraftingTable table() {
        return table;
    }

    @Override
    public void slotsChanged(Container container) {
        access.execute((level, pos) -> {
            if (level instanceof ServerLevel serverLevel) {
                recalculateResult(serverLevel);
            }
        });
    }

    private void recalculateResult(ServerLevel level) {
        CraftingInput input = craftSlots.asCraftInput();
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack result = ItemStack.EMPTY;

        Optional<RecipeHolder<CraftingRecipe>> match = level.getServer().getRecipeManager()
                .getRecipeFor(LOTRRecipeTypes.forTable(table), input, level, (RecipeHolder<CraftingRecipe>) null);

        if (match.isPresent()) {
            RecipeHolder<CraftingRecipe> holder = match.get();
            if (resultSlots.setRecipeUsed(serverPlayer, holder)) {
                ItemStack assembled = holder.value().assemble(input);
                if (assembled.isItemEnabled(level.enabledFeatures())) {
                    result = assembled;
                }
            }
        }

        resultSlots.setItem(0, result);
        setRemoteSlot(0, result);
        serverPlayer.connection.send(
                new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 0, result));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, pos) -> clearContainer(player, craftSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, table.block());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex == 0) {
                stack.getItem().onCraftedBy(stack, player);
                if (!moveItemStackTo(stack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, clicked);
            } else if (slotIndex >= 10 && slotIndex < 46) {
                if (!moveItemStackTo(stack, 1, 10, false)) {
                    if (slotIndex < 37) {
                        if (!moveItemStackTo(stack, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!moveItemStackTo(stack, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!moveItemStackTo(stack, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (slotIndex == 0) {
                player.drop(stack, false);
            }
        }
        return clicked;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
        return target.container != resultSlots && super.canTakeItemForPickAll(carried, target);
    }

    @Override
    public Slot getResultSlot() {
        return slots.get(0);
    }

    @Override
    public List<Slot> getInputGridSlots() {
        return slots.subList(1, 10);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    protected Player owner() {
        return player;
    }
}