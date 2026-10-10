package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure;

/** 1.7.10's StructureBoundingBox: an inclusive box of blocks, its bounds open to change. */
public class LOTRStructureBoundingBox {
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public LOTRStructureBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    /** A column box, y 1 to 512. */
    public LOTRStructureBoundingBox(int minX, int minZ, int maxX, int maxZ) {
        this(minX, 1, minZ, maxX, 512, maxZ);
    }

    public LOTRStructureBoundingBox(int[] bounds) {
        this(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
    }

    /** getNewBoundingBox: an empty box, ready to be grown. */
    public static LOTRStructureBoundingBox getNewBoundingBox() {
        return new LOTRStructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public boolean intersectsWith(LOTRStructureBoundingBox bb) {
        return this.maxX >= bb.minX && this.minX <= bb.maxX && this.maxZ >= bb.minZ && this.minZ <= bb.maxZ
                && this.maxY >= bb.minY && this.minY <= bb.maxY;
    }

    public boolean intersectsWith(int minX, int minZ, int maxX, int maxZ) {
        return this.maxX >= minX && this.minX <= maxX && this.maxZ >= minZ && this.minZ <= maxZ;
    }

    public void expandTo(LOTRStructureBoundingBox bb) {
        this.minX = Math.min(this.minX, bb.minX);
        this.minY = Math.min(this.minY, bb.minY);
        this.minZ = Math.min(this.minZ, bb.minZ);
        this.maxX = Math.max(this.maxX, bb.maxX);
        this.maxY = Math.max(this.maxY, bb.maxY);
        this.maxZ = Math.max(this.maxZ, bb.maxZ);
    }

    public boolean isVecInside(int x, int y, int z) {
        return x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ && y >= this.minY && y <= this.maxY;
    }

    public int getXSize() {
        return this.maxX - this.minX + 1;
    }

    public int getYSize() {
        return this.maxY - this.minY + 1;
    }

    public int getZSize() {
        return this.maxZ - this.minZ + 1;
    }

    public int getCenterX() {
        return this.minX + (this.maxX - this.minX + 1) / 2;
    }

    public int getCenterY() {
        return this.minY + (this.maxY - this.minY + 1) / 2;
    }

    public int getCenterZ() {
        return this.minZ + (this.maxZ - this.minZ + 1) / 2;
    }

    public int[] toArray() {
        return new int[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ};
    }
}
