package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;



public abstract class LOTREasterlingStructureTownStructure extends LOTREasterlingStructure {
    protected LOTREasterlingStructureTownStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean useTownBlocks() {
        return true;
    }
}
