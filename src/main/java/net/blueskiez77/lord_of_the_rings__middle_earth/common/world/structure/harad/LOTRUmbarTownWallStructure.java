package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;



public abstract class LOTRUmbarTownWallStructure extends LOTRSouthronTownWallStructure {
    protected LOTRUmbarTownWallStructure(boolean flag) {
        super(flag);
    }

    public static class Extra extends LOTRSouthronTownWallStructure.Extra {
        public Extra(boolean flag) {
            super(flag);
        }

        @Override
        public boolean isUmbar() {
            return true;
        }
    }

    public static class Long extends LOTRSouthronTownWallStructure.Long {
        public Long(boolean flag) {
            super(flag);
        }

        @Override
        public boolean isUmbar() {
            return true;
        }
    }

    public static class Short extends LOTRSouthronTownWallStructure.Short {
        public Short(boolean flag) {
            super(flag);
        }

        @Override
        public boolean isUmbar() {
            return true;
        }
    }

    public static class SideMid extends LOTRSouthronTownWallStructure.SideMid {
        public SideMid(boolean flag) {
            super(flag);
        }

        @Override
        public boolean isUmbar() {
            return true;
        }
    }

}
