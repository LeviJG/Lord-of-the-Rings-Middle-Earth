package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRModTextFiles;

import org.jspecify.annotations.Nullable;

/**
 * LOTRStructureScan: a structure recorded block by block, read from the mod's
 * {@code .strscan} files (in {@code data/lotr/strscan/}) by the same line
 * format -- {@code x.y.z."block".meta} for a block, {@code #ALIAS#.meta} for
 * a block the structure chooses, {@code ~ALIAS~} for a block and metadata it
 * chooses, {@code /SKULL/} for a skull, with {@code v} after y to fill down
 * to the ground and {@code _} to drop to it.
 *
 * <p>Writing scans out (the original's in-game scanning tool) is not ported.
 */
public final class LOTRStructureScan {

    public static final String STRSCAN_FORMAT = ".strscan";
    private static final Map<String, LOTRStructureScan> ALL_LOADED_SCANS = new HashMap<>();

    public final String scanName;
    public final List<ScanStepBase> scanSteps = new ArrayList<>();

    private LOTRStructureScan(String name) {
        this.scanName = name;
    }

    public static @Nullable LOTRStructureScan getScanByName(String name) {
        return ALL_LOADED_SCANS.get(name);
    }

    public static void loadAllScans() {
        ALL_LOADED_SCANS.clear();
        for (Map.Entry<String, List<String>> file : LOTRModTextFiles.readAll("data/lotr/strscan", STRSCAN_FORMAT).entrySet()) {
            String strName = file.getKey();
            List<String> lines = file.getValue();
            if (lines.isEmpty()) {
                LOTRMod.LOGGER.error("LOTR structure scan {} is empty!", strName);
                continue;
            }
            int curLine = 0;
            try {
                LOTRStructureScan scan = new LOTRStructureScan(strName);
                for (String line : lines) {
                    ++curLine;
                    if (line.isEmpty() || line.charAt(0) == '#' || line.charAt(0) == '~') {
                        continue;
                    }
                    scan.scanSteps.add(parseStep(line, curLine));
                }
                ALL_LOADED_SCANS.put(scan.scanName, scan);
            } catch (Exception e) {
                LOTRMod.LOGGER.error("Failed to load LOTR structure scan {}: error on line {}", strName, curLine, e);
            }
        }
    }

    private static ScanStepBase parseStep(String line, int curLine) {
        int i = 0;
        int j = line.indexOf('.');
        int x = Integer.parseInt(line.substring(i, j));
        boolean fillDown = false;
        boolean findLowest = false;
        i = j + 1;
        j = line.indexOf('.', i);
        String s = line.substring(i, j);
        if (!s.isEmpty() && s.charAt(s.length() - 1) == 'v') {
            fillDown = true;
            s = s.substring(0, s.length() - 1);
        } else if (!s.isEmpty() && s.charAt(s.length() - 1) == '_') {
            findLowest = true;
            s = s.substring(0, s.length() - 1);
        }
        int y = Integer.parseInt(s);
        i = j + 1;
        j = line.indexOf('.', i);
        int z = Integer.parseInt(line.substring(i, j));
        i = j + 1;
        char c = line.charAt(i);
        ScanStepBase step = null;
        if (c == '"') {
            j = line.indexOf('"', i + 1);
            String blockID = line.substring(i + 1, j);
            int meta = Integer.parseInt(line.substring(j + 2));
            step = new ScanStep(x, y, z, LOTRLegacyBlocks.byScanName(blockID), meta);
        } else if (c == '#') {
            j = line.indexOf('#', i + 1);
            String alias = line.substring(i + 1, j);
            int meta = Integer.parseInt(line.substring(j + 2));
            step = new ScanStepBlockAlias(x, y, z, alias, meta);
        } else if (c == '~') {
            j = line.indexOf('~', i + 1);
            step = new ScanStepBlockMetaAlias(x, y, z, line.substring(i + 1, j));
        } else if (c == '/') {
            j = line.indexOf('/', i + 1);
            if ("SKULL".equals(line.substring(i + 1, j))) {
                step = new ScanStepSkull(x, y, z);
            }
        }
        if (step == null) {
            throw new IllegalArgumentException("Invalid scan instruction on line " + curLine);
        }
        step.fillDown = fillDown;
        step.findLowest = findLowest;
        step.lineNumber = curLine;
        return step;
    }

    public abstract static class ScanStepBase {
        public final int x;
        public final int y;
        public final int z;
        public boolean fillDown;
        public boolean findLowest;
        public int lineNumber;

        protected ScanStepBase(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public abstract @Nullable String getAlias();

        public abstract LOTRLegacyBlocks.@Nullable LegacyBlock getBlock(LOTRLegacyBlocks.@Nullable LegacyBlock aliasBlock);

        public abstract int getMeta(int aliasMeta);

        public boolean hasAlias() {
            return getAlias() != null;
        }
    }

    public static final class ScanStep extends ScanStepBase {
        public final LOTRLegacyBlocks.LegacyBlock block;
        public final int meta;

        ScanStep(int x, int y, int z, LOTRLegacyBlocks.LegacyBlock block, int meta) {
            super(x, y, z);
            this.block = block;
            this.meta = meta;
        }

        @Override
        public @Nullable String getAlias() {
            return null;
        }

        @Override
        public LOTRLegacyBlocks.LegacyBlock getBlock(LOTRLegacyBlocks.@Nullable LegacyBlock aliasBlock) {
            return this.block;
        }

        @Override
        public int getMeta(int aliasMeta) {
            return this.meta;
        }
    }

    public static final class ScanStepBlockAlias extends ScanStepBase {
        public final String alias;
        public final int meta;

        ScanStepBlockAlias(int x, int y, int z, String alias, int meta) {
            super(x, y, z);
            this.alias = alias;
            this.meta = meta;
        }

        @Override
        public String getAlias() {
            return this.alias;
        }

        @Override
        public LOTRLegacyBlocks.@Nullable LegacyBlock getBlock(LOTRLegacyBlocks.@Nullable LegacyBlock aliasBlock) {
            return aliasBlock;
        }

        @Override
        public int getMeta(int aliasMeta) {
            return this.meta;
        }
    }

    public static final class ScanStepBlockMetaAlias extends ScanStepBase {
        public final String alias;

        ScanStepBlockMetaAlias(int x, int y, int z, String alias) {
            super(x, y, z);
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return this.alias;
        }

        @Override
        public LOTRLegacyBlocks.@Nullable LegacyBlock getBlock(LOTRLegacyBlocks.@Nullable LegacyBlock aliasBlock) {
            return aliasBlock;
        }

        @Override
        public int getMeta(int aliasMeta) {
            return aliasMeta;
        }
    }

    public static final class ScanStepSkull extends ScanStepBase {
        ScanStepSkull(int x, int y, int z) {
            super(x, y, z);
        }

        @Override
        public @Nullable String getAlias() {
            return null;
        }

        @Override
        public LOTRLegacyBlocks.LegacyBlock getBlock(LOTRLegacyBlocks.@Nullable LegacyBlock aliasBlock) {
            return LOTRLegacyBlocks.vanilla("skull");
        }

        @Override
        public int getMeta(int aliasMeta) {
            return 1;
        }
    }
}
