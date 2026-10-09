package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMiniQuestKillEntity: so many of one kind of creature -- or of any kind that is one of them, as
 * the uruks of Gundabad are orcs of Gundabad. Saved by the kind's entity id.
 */
public class LOTRMiniQuestKillEntity extends LOTRMiniQuestKill {

    /** The kinds quests may name, with the class whose members (and their subclasses') count. */
    private static final Map<Identifier, Class<? extends Entity>> KILL_CLASSES = new HashMap<>();

    public @Nullable EntityType<?> entityType;

    static void registerKillClass(EntityType<?> type, Class<? extends Entity> entityClass) {
        KILL_CLASSES.put(EntityType.getKey(type), entityClass);
    }

    private @Nullable Class<? extends Entity> killClass() {
        resolveKillClasses();
        return this.entityType == null ? null : KILL_CLASSES.get(EntityType.getKey(this.entityType));
    }

    @Override
    public Component getKillTargetName() {
        return this.entityType == null ? Component.empty() : this.entityType.getDescription();
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && killClass() != null;
    }

    @Override
    public void onKill(Player player, LivingEntity entity) {
        Class<? extends Entity> killClass = killClass();
        if (this.killCount < this.killTarget && killClass != null && killClass.isAssignableFrom(entity.getClass())) {
            ++this.killCount;
            updateQuest();
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readFromNBT(nbt, registries);
        Identifier id = Identifier.tryParse(nbt.getStringOr("KillClass", ""));
        this.entityType = id == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
    }

    @Override
    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeToNBT(nbt, registries);
        nbt.putString("KillClass", EntityType.getKey(this.entityType).toString());
    }

    public static class QFKillEntity extends QFKill<LOTRMiniQuestKillEntity> {
        public Supplier<? extends EntityType<?>> entityType;

        public QFKillEntity(String name) {
            super(name);
        }

        @Override
        protected LOTRMiniQuestKillEntity newQuest() {
            return new LOTRMiniQuestKillEntity();
        }

        @Override
        public @Nullable LOTRMiniQuestKillEntity createQuest(LOTRNPCEntity npc, RandomSource rand) {
            LOTRMiniQuestKillEntity quest = super.createQuest(npc, rand);
            quest.entityType = this.entityType.get();
            return quest;
        }

        public QFKillEntity setKillEntity(Supplier<? extends EntityType<?>> type, Class<? extends Entity> entityClass,
                                       int min, int max) {
            this.entityType = type;
            KILL_CLASSES_PENDING.put(type, entityClass);
            setKillTarget(min, max);
            return this;
        }
    }

    /** Kinds waiting to be told to KILL_CLASSES once the entity types exist (resolveKillClasses). */
    private static final Map<Supplier<? extends EntityType<?>>, Class<? extends Entity>> KILL_CLASSES_PENDING = new HashMap<>();

    /** Done when first needed, on either side, once the entity types exist. */
    static synchronized void resolveKillClasses() {
        if (KILL_CLASSES_PENDING.isEmpty()) {
            return;
        }
        KILL_CLASSES_PENDING.forEach((type, cls) -> registerKillClass(type.get(), cls));
        KILL_CLASSES_PENDING.clear();
    }
}
