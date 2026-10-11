package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditFleeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBanditStealGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBandit;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRInventoryNPC;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.MiniQuestSelector;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;


/**
 * LOTREntityRuffianSpy: a ruffian who picks pockets. He robs the player he
 * last turned on, or one reeling with nausea, of one theft, then runs; he
 * fights only when set upon. Slain, he gives up what he took.
 *
 * A player may pay him with coins, gold, silver, a gem or a ring to hear where
 * the target of any bounty they hunt was last seen.
 */
public class LOTRRuffianSpyEntity extends LOTRBreeRuffianEntity implements LOTRBandit {

    private final LOTRInventoryNPC ruffianInventory = LOTRBandit.createInv(this, getMaxThefts());
    private @Nullable Player playerToRob;

    public LOTRRuffianSpyEntity(EntityType<? extends LOTRRuffianSpyEntity> type, Level level) {
        super(type, level);
        // His bounty help is paid for: coins, gold, silver, a gem or a ring in hand, one of which he takes.
        this.questInfo.setBountyHelpPredicate(player -> {
            ItemStack itemstack = player.getMainHandItem();
            if (LOTRCoins.getStackValue(itemstack, true) > 0) {
                return true;
            }
            return !itemstack.isEmpty() && (itemstack.is(Items.GOLD_INGOT) || itemstack.is(LOTRMaterialItems.SILVER_INGOT)
                    || GEMS.contains(itemstack.getItem()) || RINGS.contains(itemstack.getItem()));
        });
        this.questInfo.setBountyHelpConsumer(player -> {
            if (!player.getAbilities().instabuild && !player.getMainHandItem().isEmpty()) {
                player.getMainHandItem().shrink(1);
            }
            playTradeSound();
            return true;
        });
        this.questInfo.setActiveBountySelector(new MiniQuestSelector.BountyActiveAnyFaction());
    }

    /** LOTRItemGem and LOTRItemGemWithAnvilNameColor's items. */
    private static final java.util.Set<Item> GEMS = java.util.Set.of(LOTRMaterialItems.TOPAZ, LOTRMaterialItems.AMETHYST,
            LOTRMaterialItems.SAPPHIRE, LOTRMaterialItems.RUBY, LOTRMaterialItems.AMBER, LOTRMaterialItems.DIAMOND,
            LOTRMaterialItems.PEARL, LOTRMaterialItems.CORAL, LOTRMaterialItems.OPAL, LOTRMaterialItems.EMERALD);
    /** LOTRItemRing's items. */
    private static final java.util.Set<Item> RINGS = java.util.Set.of(LOTRMiscItems.GOLD_RING, LOTRMiscItems.SILVER_RING,
            LOTRMiscItems.MITHRIL_RING);

    @Override
    protected int addBreeAttackAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRBanditStealGoal(this, 1.6));
        prio++;
        this.goalSelector.addGoal(prio, new LOTRAttackOnCollideGoal(this, 1.4, false));
        prio++;
        this.goalSelector.addGoal(prio, new LOTRBanditFleeGoal(this, 1.4));
        return prio;
    }

    @Override
    public boolean canTargetPlayerForTheft(Player player) {
        return player == this.playerToRob || canRuffianTarget(player);
    }

    @Override
    public LOTRNPCEntity getBanditAsNPC() {
        return this;
    }

    @Override
    public LOTRInventoryNPC getBanditInventory() {
        return this.ruffianInventory;
    }

    @Override
    public int getMaxThefts() {
        return 1;
    }

    @Override
    public Component getTheftChatMsg(Player player) {
        return Component.translatable("chat.lotr.ruffianSteal");
    }

    @Override
    public String getTheftSpeechBank(Player player) {
        return "bree/ruffian/hostile";
    }

    /** setAttackTarget: a player (not in creative) he turns on is the one he will rob. */
    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        if (target instanceof Player player && !player.isCreative()) {
            this.playerToRob = player;
        }
        super.setTarget(target, speak);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide()) {
            this.ruffianInventory.dropAllItems();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.ruffianInventory.save(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.ruffianInventory.load(input);
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.RUFFIAN_SPY.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_RUFFIAN_SPY;
    }

    @Override
    public @Nullable LOTRMiniQuestFactory getBountyHelpSpeechDir() {
        return LOTRMiniQuestFactory.RUFFIAN_SPY;
    }
}
