package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.util.List;
import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRFamilyInfo: an NPC's age, sex, name and drunkenness, and its marriage
 * and children. The age, sex, name and whether it is drunk reach the client
 * through the entity's synced data, which is what LOTRPacketFamilyInfo sent.
 *
 * <p>A negative age is a child growing up; a positive one after marrying or
 * breeding is the wait before breeding again.
 */
public class LOTRFamilyInfo {

    private final LOTRNPCEntity entity;
    public @Nullable Class<?> marriageEntityClass;
    public @Nullable Item marriageRing;
    /** Earned by the player who gave the ring, at the wedding. */
    public @Nullable LOTRAchievement marriageAchievement;
    public float marriageAlignmentRequired;
    public int potentialMaxChildren;
    public int timeToMature;
    public int breedingDelay;
    public @Nullable UUID spouseUniqueID;
    public int children;
    public int maxChildren;
    public @Nullable UUID maleParentID;
    public @Nullable UUID femaleParentID;
    public @Nullable UUID ringGivingPlayer;
    private int drunkTime;
    private int timeUntilDrunkSpeech;

    LOTRFamilyInfo(LOTRNPCEntity entity) {
        this.entity = entity;
    }

    public int getAge() {
        return this.entity.getEntityData().get(LOTRNPCEntity.DATA_AGE);
    }

    public void setAge(int age) {
        this.entity.getEntityData().set(LOTRNPCEntity.DATA_AGE, age);
    }

    public boolean isMale() {
        return this.entity.getEntityData().get(LOTRNPCEntity.DATA_MALE);
    }

    public void setMale(boolean male) {
        this.entity.getEntityData().set(LOTRNPCEntity.DATA_MALE, male);
    }

    public String getName() {
        return this.entity.getEntityData().get(LOTRNPCEntity.DATA_NAME);
    }

    public void setName(String name) {
        this.entity.getEntityData().set(LOTRNPCEntity.DATA_NAME, name == null ? "" : name);
    }

    public boolean isDrunk() {
        return this.entity.level().isClientSide() ? this.entity.getEntityData().get(LOTRNPCEntity.DATA_DRUNK)
                : this.drunkTime > 0;
    }

    public void setDrunkTime(int time) {
        this.drunkTime = time;
        this.entity.getEntityData().set(LOTRNPCEntity.DATA_DRUNK, time > 0);
    }

    public void setChild() {
        setAge(-this.timeToMature);
    }

    public void setMaxBreedingDelay() {
        setAge((int) (this.breedingDelay * (0.5f + this.entity.getRandom().nextFloat() * 0.5f)));
    }

    public int getRandomMaxChildren() {
        return 1 + this.entity.getRandom().nextInt(this.potentialMaxChildren);
    }

    /** canMarryNPC: same kind, unmarried, grown, the other sex, not a sibling, holding the ring. */
    public boolean canMarryNPC(LOTRNPCEntity npc) {
        LOTRFamilyInfo other = npc.familyInfo;
        if (npc.getClass() != this.entity.getClass() || other.spouseUniqueID != null || other.getAge() != 0
                || !npc.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            return false;
        }
        if (npc == this.entity || other.isMale() == isMale()
                || this.maleParentID != null && this.maleParentID.equals(other.maleParentID)
                || this.femaleParentID != null && this.femaleParentID.equals(other.femaleParentID)) {
            return false;
        }
        ItemStack held = npc.getMainHandItem();
        return !held.isEmpty() && held.is(this.marriageRing);
    }

    private @Nullable LOTRNPCEntity findNearby(@Nullable UUID id) {
        if (id == null) {
            return null;
        }
        List<? extends LOTRNPCEntity> list = this.entity.level().getEntitiesOfClass(this.entity.getClass(),
                this.entity.getBoundingBox().inflate(16.0, 8.0, 16.0));
        for (LOTRNPCEntity npc : list) {
            if (npc != this.entity && npc.getUUID().equals(id)) {
                return npc;
            }
        }
        return null;
    }

    public @Nullable LOTRNPCEntity getParentToFollow() {
        return findNearby(isMale() ? this.maleParentID : this.femaleParentID);
    }

    public @Nullable LOTRNPCEntity getSpouse() {
        LOTRNPCEntity npc = findNearby(this.spouseUniqueID);
        if (npc != null && this.entity.getUUID().equals(npc.familyInfo.spouseUniqueID)) {
            return npc;
        }
        return null;
    }

    public @Nullable Player getRingGivingPlayer() {
        return this.ringGivingPlayer == null ? null : this.entity.level().getPlayerByUUID(this.ringGivingPlayer);
    }

    /**
     * interact: handing the marriage ring to a grown, unmarried NPC of this kind
     * with nothing in its hands or on its head, at high enough alignment.
     */
    public boolean interact(Player player, ItemStack stack) {
        if (this.entity.isHired()) {
            return false;
        }
        if (!stack.isEmpty() && this.marriageRing != null && stack.is(this.marriageRing)
                && LOTRPlayerAlignments.getAlignment(player, this.entity.getFaction()) >= this.marriageAlignmentRequired
                && this.entity.getClass() == this.marriageEntityClass && getAge() == 0
                && this.entity.getMainHandItem().isEmpty() && this.entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && this.spouseUniqueID == null) {
            stack.consume(1, player);
            if (!this.entity.level().isClientSide()) {
                this.entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(this.marriageRing));
                this.ringGivingPlayer = player.getUUID();
            }
            this.entity.isNPCPersistent = true;
            return true;
        }
        return false;
    }

    /** onUpdate: server side only. */
    void tick() {
        int age = getAge();
        if (age < 0) {
            setAge(age + 1);
        } else if (age > 0) {
            setAge(age - 1);
        }
        if (this.drunkTime > 0) {
            setDrunkTime(this.drunkTime - 1);
        }
        if (this.drunkTime > 0) {
            this.entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 20));
            if (this.timeUntilDrunkSpeech > 0) {
                --this.timeUntilDrunkSpeech;
            }
            if (this.entity.isAlive() && this.entity.getTarget() == null && this.timeUntilDrunkSpeech == 0) {
                for (Player player : this.entity.level().getEntitiesOfClass(Player.class,
                        this.entity.getBoundingBox().inflate(12.0))) {
                    String bank;
                    if (!player.isAlive() || player.isCreative() || (bank = this.entity.getSpeechBank(player)) == null
                            || this.entity.getRandom().nextInt(3) != 0) {
                        continue;
                    }
                    this.entity.sendSpeechBank(player, bank);
                }
                this.timeUntilDrunkSpeech = 20 * Mth.nextInt(this.entity.getRandom(), 5, 20);
            }
        }
    }

    private static void putUUID(ValueOutput output, String key, @Nullable UUID id) {
        if (id != null) {
            output.putLong(key + "UUIDMost", id.getMostSignificantBits());
            output.putLong(key + "UUIDLeast", id.getLeastSignificantBits());
        }
    }

    private static @Nullable UUID getUUID(ValueInput input, String key) {
        long most = input.getLongOr(key + "UUIDMost", 0L);
        long least = input.getLongOr(key + "UUIDLeast", 0L);
        return most == 0L && least == 0L ? null : new UUID(most, least);
    }

    void save(ValueOutput output) {
        output.putInt("NPCAge", getAge());
        output.putBoolean("NPCMale", isMale());
        if (!getName().isEmpty()) {
            output.putString("NPCName", getName());
        }
        output.putInt("NPCDrunkTime", this.drunkTime);
        putUUID(output, "Spouse", this.spouseUniqueID);
        output.putInt("Children", this.children);
        output.putInt("MaxChildren", this.maxChildren);
        putUUID(output, "MaleParent", this.maleParentID);
        putUUID(output, "FemaleParent", this.femaleParentID);
        putUUID(output, "RingGivingPlayer", this.ringGivingPlayer);
    }

    /**
     * readFromNBT. The ring-giver is written but, as in the original, never read
     * back: its read waited for a "RingGivingPlayer" key that was never written.
     */
    void load(ValueInput input) {
        setAge(input.getIntOr("NPCAge", 0));
        setMale(input.getBooleanOr("NPCMale", isMale()));
        input.getString("NPCName").ifPresent(this::setName);
        setDrunkTime(input.getIntOr("NPCDrunkTime", 0));
        this.spouseUniqueID = getUUID(input, "Spouse");
        this.children = input.getIntOr("Children", 0);
        this.maxChildren = input.getIntOr("MaxChildren", 0);
        this.maleParentID = getUUID(input, "MaleParent");
        this.femaleParentID = getUUID(input, "FemaleParent");
    }
}
