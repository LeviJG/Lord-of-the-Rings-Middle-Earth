package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMiniQuestPickpocket: steal from so many of a faction's people (each but once) and bring the
 * goods back. Sneaking up empty-handed on one who is not fighting nor looking: one try in three takes
 * something; a third of those, and a quarter of the misses, are noticed, and the victim turns on the
 * thief; others of its friends near enough who see it may too. Any notice costs the thief a little
 * standing with the faction.
 */
public class LOTRMiniQuestPickpocket extends LOTRMiniQuestCollectBase {

    public @Nullable LOTRFaction pickpocketFaction;
    public final Set<UUID> pickpocketedEntityIDs = new HashSet<>();

    /** createPickpocketIcon: a leather hat with a white feather. */
    public static ItemStack createPickpocketIcon() {
        ItemStack hat = new ItemStack(LOTRMiscItems.LEATHER_HAT);
        LOTRLeatherHatItem.setHatColor(hat, 0);
        LOTRLeatherHatItem.setFeatherColor(hat, 0xFFFFFF);
        return hat;
    }

    @Override
    public int getCoinBonus() {
        return Math.round(getAlignmentBonus() * 5.0f);
    }

    @Override
    public String getObjectiveInSpeech() {
        return this.pickpocketFaction.factionEntityName().getString();
    }

    @Override
    public String getProgressedObjectiveInSpeech() {
        return this.collectTarget - this.amountGiven + " " + this.pickpocketFaction.factionEntityName().getString();
    }

    @Override
    public ItemStack getQuestIcon() {
        return createPickpocketIcon();
    }

    @Override
    public Component getQuestObjective() {
        return Component.translatable("lotr.miniquest.pickpocket", this.collectTarget, this.pickpocketFaction.factionEntityName());
    }

    @Override
    public Component getQuestProgress() {
        return Component.translatable("lotr.miniquest.pickpocket.progress", this.amountGiven, this.collectTarget);
    }

    /** isEntityWatching: within its 130-degree view, and in its line of sight. */
    public static boolean isEntityWatching(Mob watcher, LivingEntity target) {
        Vec3 look = watcher.getLookAngle();
        Vec3 disp = target.getEyePosition().subtract(watcher.getEyePosition());
        double dot = disp.normalize().dot(look.normalize());
        if (dot >= Mth.cos(2.2689280275926285f / 2.0f)) {
            return watcher.getSensing().hasLineOfSight(target);
        }
        return false;
    }

    /** Stolen goods taken from one who wanted them stolen for this quest's giver. */
    @Override
    public boolean isQuestItem(ItemStack stack) {
        return IPickpocketable.Helper.isPickpocketed(stack) && this.entityUUID.equals(IPickpocketable.Helper.getWanterID(stack));
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.pickpocketFaction != null;
    }

    @Override
    public boolean onInteractOther(Player player, LOTRNPCEntity npc) {
        if (!player.isShiftKeyDown() || !player.getMainHandItem().isEmpty() || npc.getFaction() != this.pickpocketFaction
                || !(npc instanceof IPickpocketable pickpocketable) || !(npc.level() instanceof ServerLevel level)) {
            return false;
        }
        UUID id = npc.getUUID();
        if (!pickpocketable.canPickpocket() || this.pickpocketedEntityIDs.contains(id)) {
            return false;
        }
        if (npc.getTarget() != null) {
            player.sendSystemMessage(Component.translatable("chat.lotr.pickpocket.inCombat"));
            return true;
        }
        if (isEntityWatching(npc, player)) {
            player.sendSystemMessage(Component.translatable("chat.lotr.pickpocket.watched"));
            return true;
        }
        RandomSource rand = npc.getRandom();
        boolean success = rand.nextInt(3) == 0;
        boolean noticed = success ? rand.nextInt(3) == 0 : rand.nextInt(4) == 0;
        boolean anyoneNoticed = noticed;
        if (success) {
            ItemStack picked = pickpocketable.createPickpocketItem();
            IPickpocketable.Helper.setPickpocketData(picked, npc.getNPCName(), this.entityNameFull, this.entityUUID);
            player.getInventory().setItem(player.getInventory().getSelectedSlot(), picked);
            player.sendSystemMessage(Component.translatable("chat.lotr.pickpocket.success", picked.getCount(),
                    picked.getHoverName(), npc.getNPCName()));
            npc.playSound(LOTRSounds.EVENT_TRADE, 0.5f, 1.0f + (rand.nextFloat() - rand.nextFloat()) * 0.1f);
            npc.playSound(SoundEvents.HORSE_SADDLE.value(), 0.5f, 1.0f);
            spawnPickingFX(level, LOTRParticles.PICKPOCKET, 1.0, npc);
            this.pickpocketedEntityIDs.add(id);
            updateQuest();
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.PICKPOCKET);
        } else {
            player.sendSystemMessage(Component.translatable("chat.lotr.pickpocket.missed", npc.getNPCName()));
            npc.playSound(SoundEvents.WOOL_BREAK, 0.5f, ((rand.nextFloat() - rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            spawnPickingFX(level, LOTRParticles.PICKPOCKET_FAIL, 0.4, npc);
        }
        if (noticed) {
            player.sendSystemMessage(Component.translatable("chat.lotr.pickpocket.noticed", npc.getNPCName()));
            npc.setTarget(player, true);
            npc.setLastHurtByMob(player);
            spawnAngryFX(level, npc);
        }
        if (!noticed || rand.nextFloat() < 0.5f) {
            List<LOTRNPCEntity> nearbyFriends = level.getEntitiesOfClass(LOTRNPCEntity.class, npc.getBoundingBox().inflate(16.0),
                    other -> other.isAlive() && other.getFaction().isGoodRelation(npc.getFaction())
                            && other.hiredNPCInfo.getHiringPlayer() != player);
            for (LOTRNPCEntity other : nearbyFriends) {
                if (other == npc) {
                    continue;
                }
                boolean civilian = other.isCivilianNPC();
                double maxRange = civilian ? 8.0 : 16.0;
                double dist = other.distanceTo(npc);
                if (dist > maxRange || other.getTarget() != null || !isEntityWatching(other, player)) {
                    continue;
                }
                float distFactor = 1.0f - (float) ((dist - 4.0) / (maxRange - 4.0));
                float chance = 0.5f + distFactor * 0.5f;
                if (civilian) {
                    chance *= 0.25f;
                }
                if (rand.nextFloat() >= chance) {
                    continue;
                }
                player.sendSystemMessage(Component.translatable("chat.lotr.pickpocket.otherNoticed", other.getType().getDescription()));
                other.setTarget(player, true);
                other.setLastHurtByMob(player);
                spawnAngryFX(level, other);
                anyoneNoticed = true;
            }
        }
        if (anyoneNoticed) {
            LOTRPlayerAlignments.addAlignment(player, LOTRAlignmentValues.PICKPOCKET_PENALTY, npc.getFaction(), npc);
        }
        return true;
    }

    private static void spawnAngryFX(ServerLevel level, LivingEntity npc) {
        Vec3 motion = npc.getDeltaMovement();
        level.sendParticles(LOTRParticles.ANGRY, npc.getX(), npc.getBoundingBox().minY + npc.getBbHeight() * 2.0f, npc.getZ(),
                0, motion.x, Math.max(0.0, motion.y), motion.z, 1.0);
    }

    private static void spawnPickingFX(ServerLevel level, ParticleOptions particle, double upSpeed, LivingEntity npc) {
        RandomSource rand = npc.getRandom();
        int particles = 3 + rand.nextInt(8);
        for (int p = 0; p < particles; ++p) {
            double y = npc.getBoundingBox().minY + npc.getBbHeight() * 0.5f;
            float w = npc.getBbWidth() * 0.1f;
            float ang = rand.nextFloat() * Mth.TWO_PI;
            double hSpeed = Mth.nextDouble(rand, 0.05, 0.08);
            double vx = Mth.cos(ang) * hSpeed;
            double vz = Mth.sin(ang) * hSpeed;
            double vy = Mth.nextDouble(rand, 0.1, 0.25) * upSpeed;
            // A count of 0 sends the speed as the particle's motion.
            level.sendParticles(particle, npc.getX() + Mth.cos(ang) * w, y, npc.getZ() + Mth.sin(ang) * w, 0, vx, vy, vz, 1.0);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        this.pickpocketFaction = LOTRFaction.forName(nbt.getStringOr("PickpocketFaction", ""));
        this.pickpocketedEntityIDs.clear();
        for (Tag tag : nbt.getListOrEmpty("PickpocketedIDs")) {
            tag.asString().ifPresent(s -> this.pickpocketedEntityIDs.add(UUID.fromString(s)));
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        nbt.putString("PickpocketFaction", this.pickpocketFaction.codeName());
        ListTag ids = new ListTag();
        this.pickpocketedEntityIDs.forEach(id -> ids.add(StringTag.valueOf(id.toString())));
        nbt.put("PickpocketedIDs", ids);
    }

    public static class QFPickpocket extends QuestFactoryBase<LOTRMiniQuestPickpocket> {
        public LOTRFaction pickpocketFaction;
        public int minTarget;
        public int maxTarget;

        public QFPickpocket(String name) {
            super(name);
        }

        @Override
        protected LOTRMiniQuestPickpocket newQuest() {
            return new LOTRMiniQuestPickpocket();
        }

        @Override
        public Class<? super LOTRMiniQuestPickpocket> getQuestClass() {
            return LOTRMiniQuestPickpocket.class;
        }

        @Override
        public @Nullable LOTRMiniQuestPickpocket createQuest(LOTRNPCEntity npc, RandomSource rand) {
            LOTRMiniQuestPickpocket quest = super.createQuest(npc, rand);
            quest.pickpocketFaction = this.pickpocketFaction;
            quest.collectTarget = Mth.randomBetweenInclusive(rand, this.minTarget, this.maxTarget);
            return quest;
        }

        public QFPickpocket setPickpocketFaction(LOTRFaction faction, int min, int max) {
            this.pickpocketFaction = faction;
            this.minTarget = min;
            this.maxTarget = max;
            return this;
        }
    }
}
