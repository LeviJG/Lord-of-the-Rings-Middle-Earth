package net.blueskiez77.lord_of_the_rings__middle_earth.common.quest;

import java.util.UUID;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

/** MiniQuestSelector: which of a player's quests to take. */
public interface MiniQuestSelector {

    boolean include(LOTRMiniQuest quest);

    class OptionalActive implements MiniQuestSelector {
        public boolean activeOnly;

        @Override
        public boolean include(LOTRMiniQuest quest) {
            return !this.activeOnly || quest.isActive();
        }

        public OptionalActive setActiveOnly() {
            this.activeOnly = true;
            return this;
        }
    }

    /** Active bounties not yet claimed, for any faction. */
    class BountyActiveAnyFaction extends OptionalActive {
        public BountyActiveAnyFaction() {
            setActiveOnly();
        }

        @Override
        public boolean include(LOTRMiniQuest quest) {
            return super.include(quest) && quest instanceof LOTRMiniQuestBounty bounty && !bounty.killed;
        }
    }

    class BountyActiveFaction extends BountyActiveAnyFaction {
        private final Supplier<LOTRFaction> factionGet;

        public BountyActiveFaction(Supplier<LOTRFaction> sup) {
            this.factionGet = sup;
        }

        @Override
        public boolean include(LOTRMiniQuest quest) {
            return super.include(quest) && quest.entityFaction == this.factionGet.get();
        }
    }

    class EntityId extends OptionalActive {
        private final UUID entityID;

        public EntityId(UUID id) {
            this.entityID = id;
        }

        @Override
        public boolean include(LOTRMiniQuest quest) {
            return super.include(quest) && quest.entityUUID.equals(this.entityID);
        }
    }

    class Faction extends OptionalActive {
        private final Supplier<LOTRFaction> factionGet;

        public Faction(Supplier<LOTRFaction> sup) {
            this.factionGet = sup;
        }

        @Override
        public boolean include(LOTRMiniQuest quest) {
            return super.include(quest) && quest.entityFaction == this.factionGet.get();
        }
    }
}
