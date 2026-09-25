package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;

/**
 * LOTRItemOwnership: a name engraved on an item at the anvil. Engraving a new
 * one moves the old owner onto a list of up to three previous owners, most
 * recent first. Kept in the stack's custom data under the original's keys.
 */
public final class LOTRItemOwnership {

    private static final String CURRENT = "LOTRCurrentOwner";
    private static final String PREVIOUS = "LOTRPrevOwnerList";
    private static final int MAX_PREVIOUS = 3;

    private LOTRItemOwnership() {
    }

    public static @Nullable String getCurrentOwner(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getString(CURRENT).orElse(null);
    }

    public static List<String> getPreviousOwners(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        List<String> owners = new ArrayList<>();
        for (Tag entry : tag.getListOrEmpty(PREVIOUS)) {
            entry.asString().ifPresent(owners::add);
        }
        return owners;
    }

    /** setCurrentOwner: the present owner, if any, goes to the head of the previous list. */
    public static void setCurrentOwner(ItemStack stack, String name) {
        String previousCurrent = getCurrentOwner(stack);
        List<String> previous = getPreviousOwners(stack);
        if (previousCurrent != null) {
            previous.add(0, previousCurrent);
            while (previous.size() > MAX_PREVIOUS) {
                previous.remove(previous.size() - 1);
            }
        }
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        stack.set(DataComponents.CUSTOM_DATA, data.update(tag -> {
            tag.putString(CURRENT, name);
            if (!previous.isEmpty()) {
                ListTag list = new ListTag();
                for (String owner : previous) {
                    list.add(StringTag.valueOf(owner));
                }
                tag.put(PREVIOUS, list);
            }
        }));
    }
}
