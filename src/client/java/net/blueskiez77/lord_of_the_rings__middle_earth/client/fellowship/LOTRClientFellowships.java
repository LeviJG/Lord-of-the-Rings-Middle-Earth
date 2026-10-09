package net.blueskiez77.lord_of_the_rings__middle_earth.client.fellowship;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRFellowshipsScreen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowshipPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's client side of fellowships (fellowshipsClient, fellowshipInvitesClient): the
 * fellowships this player is in and those they are invited to, as the server last sent them.
 */
public final class LOTRClientFellowships {

    private static final List<LOTRFellowshipPayloads.View> FELLOWSHIPS = new ArrayList<>();
    private static final List<LOTRFellowshipPayloads.View> INVITES = new ArrayList<>();

    private LOTRClientFellowships() {
    }

    public static List<LOTRFellowshipPayloads.View> getFellowships() {
        return FELLOWSHIPS;
    }

    public static List<LOTRFellowshipPayloads.View> getInvites() {
        return INVITES;
    }

    public static LOTRFellowshipPayloads.@Nullable View getByID(@Nullable UUID id) {
        for (LOTRFellowshipPayloads.View fs : FELLOWSHIPS) {
            if (fs.fellowshipID().equals(id)) {
                return fs;
            }
        }
        return null;
    }

    public static LOTRFellowshipPayloads.@Nullable View getByName(String name) {
        for (LOTRFellowshipPayloads.View fs : FELLOWSHIPS) {
            if (fs.name().equalsIgnoreCase(name)) {
                return fs;
            }
        }
        return null;
    }

    /** anyMatchingFellowshipNames(name, true). */
    public static boolean anyMatchingFellowshipNames(String name) {
        String stripped = StringUtils.strip(name).toLowerCase(Locale.ROOT);
        for (LOTRFellowshipPayloads.View fs : FELLOWSHIPS) {
            if (stripped.equals(StringUtils.strip(fs.name()).toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    /** canCreateFellowships(true): fewer led than the most one may (one, until achievements). */
    public static boolean canCreateFellowships() {
        int leading = 0;
        for (LOTRFellowshipPayloads.View fs : FELLOWSHIPS) {
            if (fs.owned()) {
                ++leading;
            }
        }
        return leading < 1;
    }

    private static void addOrUpdate(List<LOTRFellowshipPayloads.View> list, LOTRFellowshipPayloads.View view) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).fellowshipID().equals(view.fellowshipID())) {
                list.set(i, view);
                return;
            }
        }
        list.add(view);
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.Sync.TYPE, (payload, context) ->
                addOrUpdate(payload.isInvite() ? INVITES : FELLOWSHIPS, payload.view()));
        ClientPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.Remove.TYPE, (payload, context) ->
                (payload.isInvite() ? INVITES : FELLOWSHIPS).removeIf(fs -> fs.fellowshipID().equals(payload.fellowshipID())));
        ClientPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.AcceptResult.TYPE, (payload, context) -> {
            if (Minecraft.getInstance().gui.screen() instanceof LOTRFellowshipsScreen screen) {
                screen.displayAcceptInvitationResult(payload.fellowshipID(), payload.name(), payload.result());
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.Notification.TYPE, (payload, context) ->
                Minecraft.getInstance().gui.toastManager().addToast(new LOTRFellowshipToast(payload.message())));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            FELLOWSHIPS.clear();
            INVITES.clear();
        });
    }
}
