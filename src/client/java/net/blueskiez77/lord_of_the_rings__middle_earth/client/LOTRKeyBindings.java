package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.platform.InputConstants;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRMenuScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMenuPayloads;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

/**
 * LOTRKeyHandler: L (key 38 in 1.7.10) opens the LOTR menu, or the menu screen last opened from it;
 * and, with no screen open, the arrow keys cycle the faction whose alignment is shown -- left and
 * right through the region's factions, up and down through the regions (each remembering the faction
 * last viewed in it) -- at most one change each two ticks.
 *
 * <p>NOT ported yet: the original's other keys (the map key and the dismount key), with their
 * systems.
 */
public final class LOTRKeyBindings {

    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "general"));

    public static KeyMapping menu;
    public static KeyMapping alignmentCycleLeft;
    public static KeyMapping alignmentCycleRight;
    public static KeyMapping alignmentGroupPrev;
    public static KeyMapping alignmentGroupNext;

    private static int alignmentChangeTick;

    private LOTRKeyBindings() {
    }

    private static KeyMapping register(String name, int key) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(name, InputConstants.Type.KEYSYM, key, CATEGORY));
    }

    public static void register() {
        menu = register("key.lotr.menu", GLFW.GLFW_KEY_L);
        alignmentCycleLeft = register("key.lotr.alignmentCycleLeft", GLFW.GLFW_KEY_LEFT);
        alignmentCycleRight = register("key.lotr.alignmentCycleRight", GLFW.GLFW_KEY_RIGHT);
        alignmentGroupPrev = register("key.lotr.alignmentGroupPrev", GLFW.GLFW_KEY_UP);
        alignmentGroupNext = register("key.lotr.alignmentGroupNext", GLFW.GLFW_KEY_DOWN);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menu.consumeClick()) {
                if (client.gui.screen() == null && client.player != null) {
                    client.setScreenAndShow(LOTRMenuScreen.openMenu());
                }
            }
            if (alignmentChangeTick > 0) {
                --alignmentChangeTick;
            }
            cycleAlignment(client);
        });
    }

    private static int clicks(KeyMapping key) {
        int n = 0;
        while (key.consumeClick()) {
            ++n;
        }
        return n;
    }

    private static void cycleAlignment(Minecraft client) {
        int left = clicks(alignmentCycleLeft);
        int right = clicks(alignmentCycleRight);
        int prev = clicks(alignmentGroupPrev);
        int next = clicks(alignmentGroupNext);
        LocalPlayer player = client.player;
        if (player == null || client.gui.screen() != null || alignmentChangeTick > 0) {
            return;
        }
        Map<LOTRDimension.DimensionRegion, LOTRFaction> lastViewedRegions = new EnumMap<>(LOTRDimension.DimensionRegion.class);
        LOTRFaction currentFaction = LOTRViewingFaction.getViewingFaction(player);
        LOTRDimension.DimensionRegion currentRegion = currentFaction.factionRegion;
        List<LOTRDimension.DimensionRegion> regionList = LOTRDimension.MIDDLE_EARTH.dimensionRegions;
        List<LOTRFaction> factionList = currentRegion.factionList;
        boolean used = false;
        if (left > 0) {
            currentFaction = factionList.get(Math.floorMod(factionList.indexOf(currentFaction) - 1, factionList.size()));
            used = true;
        }
        if (right > 0) {
            currentFaction = factionList.get(Math.floorMod(factionList.indexOf(currentFaction) + 1, factionList.size()));
            used = true;
        }
        for (int step : new int[]{prev > 0 ? -1 : 0, next > 0 ? 1 : 0}) {
            if (step != 0) {
                LOTRViewingFaction.setRegionLastViewedFaction(player, currentRegion, currentFaction);
                lastViewedRegions.put(currentRegion, currentFaction);
                currentRegion = regionList.get(Math.floorMod(regionList.indexOf(currentRegion) + step, regionList.size()));
                currentFaction = LOTRViewingFaction.getRegionLastViewedFaction(player, currentRegion);
                used = true;
            }
        }
        if (used) {
            LOTRViewingFaction.setViewingFaction(player, currentFaction);
            ClientPlayNetworking.send(new LOTRMenuPayloads.ClientInfo(currentFaction, lastViewedRegions));
            alignmentChangeTick = 2;
        }
    }
}
