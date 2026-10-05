package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;



public abstract class LOTRUmbarFortWallStructure extends LOTRSouthronFortWallStructure {
    protected LOTRUmbarFortWallStructure(boolean flag) {
        super(flag);
    }

    public static class Long extends LOTRSouthronFortWallStructure.Long {
        public Long(boolean flag) {
            super(flag);
        }

        @Override
        public boolean isUmbar() {
            return true;
        }
    }

    public static class Short extends LOTRSouthronFortWallStructure.Short {
        public Short(boolean flag) {
            super(flag);
        }

        @Override
        public boolean isUmbar() {
            return true;
        }
    }

}
