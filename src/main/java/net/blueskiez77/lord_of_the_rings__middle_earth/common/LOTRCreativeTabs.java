package net.blueskiez77.lord_of_the_rings__middle_earth.common;


import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Creative inventory tabs.
 *
 * Modded content does not appear in the creative menu automatically -- it has
 * to be placed in a tab, which is why everything was only reachable via /give
 * until now. Registered content is still searchable regardless, but the tab is
 * what makes it browsable.
 *
 * Contents are driven off the family lists in LOTRBlocks, so a block added
 * there shows up here with no further work. Families are emitted in a
 * deliberate order (building materials, then wood, then foliage, then
 * fixtures) because a tab has no sorting of its own -- items appear exactly in
 * the order they are accepted.
 */
public final class LOTRCreativeTabs {

    public static final ResourceKey<CreativeModeTab> BLOCKS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "blocks"));

    public static final CreativeModeTab BLOCKS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(LOTRBlocks.MITHRIL_BLOCK))
            .title(Component.translatable("creativeTab.lotr.blocks"))
            .displayItems((params, output) -> {
                // Every registered block that has an item, in registration
                // order. Driven off LOTRBlocks.ALL_BLOCKS so a newly added
                // family shows up here automatically -- the previous version
                // named each family by hand and silently missed the ~500
                // blocks added since.
                LOTRBlocks.ALL_BLOCKS.forEach(output::accept);

                output.accept(LOTRItems.MITHRIL);
                output.accept(LOTRItems.PIPEWEED);
            })
            .build();

    private LOTRCreativeTabs() {
    }

    /** Called from mod init. Must run AFTER blocks and items are registered. */
    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS_KEY, BLOCKS);
    }
}