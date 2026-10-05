package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHireableBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRMercenary;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRMercenaryTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRUnitTradeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSmith;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRAnvilMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRCoinExchangeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRTradeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTradePayloads;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * The server's side of trading: LOTREventHandler.onEntityInteract's trader
 * branch (a right click on a trader who will deal with the player opens the
 * talk-or-trade choice) and LOTRPacketTraderInteract's handler.
 */
public final class LOTRTradeNetworking {

    public static final int ACTION_TALK = 0;
    public static final int ACTION_TRADE = 1;
    public static final int ACTION_EXCHANGE = 2;
    public static final int ACTION_SMITH = 3;
    /** The unit trader's and mercenary's second button. */
    public static final int ACTION_HIRE = 1;

    private LOTRTradeNetworking() {
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(LOTRTradePayloads.TraderInfo.TYPE,
                LOTRTradePayloads.TraderInfo.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRTradePayloads.OpenInteract.TYPE,
                LOTRTradePayloads.OpenInteract.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRTradePayloads.Interact.TYPE,
                LOTRTradePayloads.Interact.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRTradePayloads.UnitInteract.TYPE,
                LOTRTradePayloads.UnitInteract.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRTradePayloads.BuyUnit.TYPE,
                LOTRTradePayloads.BuyUnit.STREAM_CODEC);

        // LOTRPacketUnitTraderInteract, and the mercenary's packet, which differed only in the screen it opened.
        ServerPlayNetworking.registerGlobalReceiver(LOTRTradePayloads.UnitInteract.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (!(player.level().getEntity(payload.entityId()) instanceof LOTRNPCEntity trader)
                    || !(trader instanceof LOTRHireableBase hirer)) {
                return;
            }
            boolean closeScreen = false;
            if (payload.action() == ACTION_TALK) {
                trader.npcTalkTick = trader.getNPCTalkInterval();
                closeScreen = trader.talkInteract(player).consumesAction();
            } else if (payload.action() == ACTION_HIRE && hirer.canTradeWith(player)) {
                LOTRUnitTradeMenu.OpeningData data = LOTRUnitTradeMenu.OpeningData.create(trader, player.level());
                player.openMenu(provider(trader, trader.getDisplayName(), data,
                        (id, inv) -> new LOTRUnitTradeMenu(id, inv, trader)));
            }
            if (closeScreen) {
                player.closeContainer();
            }
        });

        // LOTRPacketBuyUnit: a mercenary, once hired, closes the screen.
        ServerPlayNetworking.registerGlobalReceiver(LOTRTradePayloads.BuyUnit.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (!(player.containerMenu instanceof LOTRUnitTradeMenu menu) || menu.unitTrader() == null) {
                return;
            }
            LOTRHireableBase unitTrader = menu.unitTrader();
            int tradeIndex = payload.tradeIndex();
            LOTRUnitTradeEntry trade = null;
            if (unitTrader instanceof LOTRUnitTradeable unitTradeable) {
                List<LOTRUnitTradeEntry> tradeList = unitTradeable.getUnits().tradeEntries;
                if (tradeIndex >= 0 && tradeIndex < tradeList.size()) {
                    trade = tradeList.get(tradeIndex);
                }
            } else if (unitTrader instanceof LOTRMercenary merc) {
                trade = LOTRMercenaryTradeEntry.createFor(merc);
            }
            String squadron = payload.squadron().isEmpty() ? null : LOTRCommandHornItem.checkAcceptableLength(payload.squadron());
            if (trade != null) {
                trade.hireUnit(player, unitTrader, squadron);
                if (unitTrader instanceof LOTRMercenary) {
                    player.closeContainer();
                }
            } else {
                LOTRMod.LOGGER.error("LOTR: Error player {} trying to hire unit from {} - trade is null or bad index!",
                        player.getName().getString(), menu.theLivingTrader.getNPCName());
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRTradePayloads.Interact.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (!(player.level().getEntity(payload.entityId()) instanceof LOTRNPCEntity trader)
                    || !(trader instanceof LOTRTradeable tradeable)) {
                return;
            }
            int action = payload.action();
            boolean closeScreen = false;
            if (action == ACTION_TALK) {
                trader.npcTalkTick = trader.getNPCTalkInterval();
                closeScreen = trader.talkInteract(player).consumesAction();
            } else if (action == ACTION_TRADE && tradeable.canTradeWith(player)) {
                player.openMenu(provider(trader, trader.getDisplayName(), (id, inv) -> new LOTRTradeMenu(id, inv, trader)));
                trader.traderNPCInfo.sendClientPacket(player);
            } else if (action == ACTION_EXCHANGE && tradeable.canTradeWith(player)) {
                player.openMenu(provider(trader, trader.getDisplayName(), (id, inv) -> new LOTRCoinExchangeMenu(id, inv, trader)));
            } else if (action == ACTION_SMITH && tradeable.canTradeWith(player) && trader instanceof LOTRSmith) {
                player.openMenu(provider(trader, Component.translatable("container.lotr.smith"),
                        (id, inv) -> new LOTRAnvilMenu(id, inv, trader)));
            }
            if (closeScreen) {
                player.closeContainer();
            }
        });
    }

    /**
     * The trader branches of onEntityInteract, which ran before the NPC's own
     * interact: a trader who will deal with the player offers to talk or
     * trade (and hire, if it sells units too); a hirer, to talk or hire; and
     * a mercenary no one has hired, the same; a hired unit, its player's
     * choice of talk, command or dismiss.
     */
    public static boolean tryOpenInteract(LOTRNPCEntity npc, Player player) {
        int kind;
        if (npc instanceof LOTRTradeable tradeable && tradeable.canTradeWith(player)) {
            kind = npc instanceof LOTRUnitTradeable ? LOTRTradePayloads.OpenInteract.TRADE_UNIT_TRADE
                    : LOTRTradePayloads.OpenInteract.TRADE;
        } else if (npc instanceof LOTRUnitTradeable hirer && hirer.canTradeWith(player)) {
            kind = LOTRTradePayloads.OpenInteract.UNIT_TRADE;
        } else if (npc instanceof LOTRMercenary merc && merc.canTradeWith(player)
                && npc.hiredNPCInfo.getHiringPlayerUUID() == null) {
            kind = LOTRTradePayloads.OpenInteract.MERCENARY;
        } else if (npc.hiredNPCInfo.getHiringPlayer() == player) {
            // openGui 21: the unit's own player.
            kind = LOTRTradePayloads.OpenInteract.HIRED;
        } else {
            return false;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new LOTRTradePayloads.OpenInteract(npc.getId(), kind));
        }
        return true;
    }

    /** checkGUIOpenAndNavigation's test: is a player at this NPC's counter? */
    public static boolean isGuiOpen(LOTRNPCEntity npc, Player player) {
        return LOTRTradeMenu.isTradingWith(player, npc) || LOTRCoinExchangeMenu.isExchangingWith(player, npc)
                || LOTRAnvilMenu.isSmithingWith(player, npc) || LOTRUnitTradeMenu.isHiringFrom(player, npc);
    }

    private interface MenuFactory {
        AbstractContainerMenu create(int containerId, Inventory inventory);
    }

    private static ExtendedMenuProvider<Integer> provider(LOTRNPCEntity trader, Component title, MenuFactory factory) {
        return provider(trader, title, trader.getId(), factory);
    }

    private static <D> ExtendedMenuProvider<D> provider(LOTRNPCEntity trader, Component title, D data, MenuFactory factory) {
        return new ExtendedMenuProvider<>() {
            @Override
            public D getScreenOpeningData(ServerPlayer player) {
                return data;
            }

            @Override
            public Component getDisplayName() {
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                return factory.create(containerId, inventory);
            }
        };
    }
}
