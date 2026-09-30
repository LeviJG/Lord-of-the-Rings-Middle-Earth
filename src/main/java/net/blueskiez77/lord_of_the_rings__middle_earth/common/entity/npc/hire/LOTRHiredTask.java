package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

/** LOTRHiredNPCInfo.Task: a warrior, who shows its level, or a farmer, who does not. */
public enum LOTRHiredTask {
    WARRIOR(true), FARMER(false);

    public final boolean displayXpLevel;

    LOTRHiredTask(boolean displayXpLevel) {
        this.displayXpLevel = displayXpLevel;
    }

    public static LOTRHiredTask forID(int id) {
        for (LOTRHiredTask task : values()) {
            if (task.ordinal() == id) {
                return task;
            }
        }
        return WARRIOR;
    }
}
