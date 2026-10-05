#!/usr/bin/env python3
"""Structure transcription check: each port structure against its original.

Pairs every port class under common/world/structure and common/world/village
with the 1.7.10 class it was transliterated from (LOTR<X>Structure ->
LOTRWorldGen<X>, LOTRVillageGen<X> -> LOTRVillageGen<X>), strips both down to
their statements, rewrites the port's legacy-block references back into the
original's spelling (LOTRLegacyBlocks.mod("x") -> LOTRMod.x,
LOTRLegacyBlocks.vanilla("x") -> Blocks.x, and the same for items), and
diffs what is left. The expected differences -- types, entity construction,
random helpers -- are dropped, so what it prints is worth a look: a changed
coordinate, metadata, block or loop bound is a transcription error.

Usage:
  tools/structure_diff.py              summary: each pair and its diff size
  tools/structure_diff.py --essential [Name]
                                       only the hunks whose numbers, blocks,
                                       items, pools, strings or creatures
                                       differ -- the ones to read
  tools/structure_diff.py Tavern       the full diff of every pair matching
  tools/structure_diff.py --unpaired   port classes with no original found
"""
import difflib
import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PORT = os.path.join(ROOT, 'src/main/java/net/blueskiez77/lord_of_the_rings__middle_earth/common/world')
OLD = os.path.join(ROOT, 'old mod/src/main/java/lotr/common/world')

# Lines that differ by construction and say nothing about the structure.
NOISE = [
    re.compile(r'^(public |private |protected )?(abstract )?class '),
    re.compile(r'^(public )?LOTR\w+\((boolean flag|.*)\) \{$'),
    re.compile(r'\b(World|WorldGenLevel|Random|RandomSource)\b.*\) \{$'),
    re.compile(r'^(public |private )?(Block|LegacyBlock|int|boolean|String\[\]|String) \w+;$'),
    re.compile(r'new LOTREntity\w+\(world\)|create\(LOTREntities\.\w+, world\)'),
]

SUBS_PORT = [
    (re.compile(r'LOTRLegacyBlocks\.mod\("(\w+)"\)'), r'LOTRMod.\1'),
    (re.compile(r'LOTRLegacyBlocks\.vanilla\("(\w+)"\)'), r'Blocks.\1'),
    (re.compile(r'LOTRLegacyItems\.modStack\("(\w+)", 1, 0\)'), r'new ItemStack(LOTRMod.\1)'),
    (re.compile(r'LOTRLegacyItems\.vanillaStack\("(\w+)", 1, 0\)'), r'new ItemStack(Items.\1)'),
    (re.compile(r'Math\.floorMod\('), 'IntMath.mod('),
    (re.compile(r'Mth\.randomBetweenInclusive\('), 'MathHelper.getRandomIntegerInRange('),
    (re.compile(r'Mth\.'), 'MathHelper.'),
    (re.compile(r'\bLegacyBlock\b'), 'Block'),
    (re.compile(r'\bRandomSource\b'), 'Random'),
    (re.compile(r'\bWorldGenLevel\b'), 'World'),
    (re.compile(r'LOTRLegacyItems\.modStack\("(\w+)", '), r'new ItemStack(LOTRMod.\1, '),
    (re.compile(r'LOTRLegacyItems\.vanillaStack\("(\w+)", '), r'new ItemStack(Items.\1, '),
    (re.compile(r'LOTRLegacyItems\.mod\("(\w+)", 0\)'), r'LOTRMod.\1'),
    (re.compile(r'LOTRLegacyItems\.vanilla\("(\w+)", 0\)'), r'Items.\1'),
    (re.compile(r'\bLOTRNPCRespawnerEntity\b'), 'LOTREntityNPCRespawner'),
    (re.compile(r'\bLOTRNPCRespawnerStructure\b'), 'LOTRWorldGenNPCRespawner'),
    (re.compile(r'\bLOTRStructureBase(2?)\b'), r'LOTRWorldGenStructureBase\1'),
    (re.compile(r'\bBlockState\b'), 'Block'),
    (re.compile(r'new BlockPos\(([^()]*)\)'), r'\1'),
    (re.compile(r'Random\.create\(\)'), 'new Random()'),
    # 1.7.10's world ran from 0 to 255; the port uses the world's own bounds.
    (re.compile(r'world\.getMinY\(\) \+ 1'), '1'),
    (re.compile(r'world\.getMinY\(\)'), '0'),
    (re.compile(r'<= world\.getMaxY\(\) \+ 1'), '<= 256'),
    (re.compile(r'<= world\.getMaxY\(\)'), '< 256'),
]

# The original's side, spelt as the port spells the same thing.
SUBS_OLD = [
    (re.compile(r'LOTRMod\.isOpaque\('), 'isOpaqueAt('),
    (re.compile(r'LOTRItemBanner\.BannerType\.(\w+)'), r'"\1"'),
    (re.compile(r'(LOTRChestContents\.fillChest\([^;]*?LOTRChestContents\.\w+)\);'), r'\1, -1);'),
    (re.compile(r'getEquipmentInSlot\(0\)'), 'getMainHandItem()'),
    (re.compile(r'(getEquipmentInSlot|setCurrentItemOrArmor)\(4'), r'\1(EquipmentSlot.HEAD'),
    (re.compile(r'(getEquipmentInSlot|setCurrentItemOrArmor)\(3'), r'\1(EquipmentSlot.CHEST'),
    (re.compile(r'(getEquipmentInSlot|setCurrentItemOrArmor)\(2'), r'\1(EquipmentSlot.LEGS'),
    (re.compile(r'(getEquipmentInSlot|setCurrentItemOrArmor)\(1'), r'\1(EquipmentSlot.FEET'),
]

# 1.7.10 spelling of the same test, applied to both sides.
SUBS_BOTH = [
    (re.compile(r'!(Blocks|LOTRMod)\.(\w+)\.matches\(([^()]*(?:\([^()]*\))?[^()]*)\)'), r'\3 != \1.\2'),
    (re.compile(r'(Blocks|LOTRMod)\.(\w+)\.matches\(([^()]*(?:\([^()]*\))?[^()]*)\)'), r'\3 == \1.\2'),
    (re.compile(r'\b(getBlockState|getBlock)\('), 'getBlock('),
]


def statements(path, port):
    out = []
    for line in open(path, encoding='utf-8', errors='replace'):
        line = line.strip()
        if not line or line.startswith(('import ', 'package ', '//', '*', '/*', '@')):
            continue
        for pattern, repl in SUBS_PORT if port else SUBS_OLD:
            line = pattern.sub(repl, line)
        for pattern, repl in SUBS_BOTH:
            line = pattern.sub(repl, line)
        if re.fullmatch(r'[{}();]*', line) or any(n.search(line) for n in NOISE):
            continue
        out.append(line)
    return out


def index(base, suffix=''):
    found = {}
    for dirpath, _, files in os.walk(base):
        for f in files:
            if f.endswith('.java'):
                found[f[:-5]] = os.path.join(dirpath, f)
    return found


def pairs():
    old = index(OLD)
    result, unpaired = [], []
    for name, path in sorted(index(PORT).items()):
        m = re.fullmatch(r'LOTR(\w+?)Structure', name)
        candidates = []
        if m:
            candidates = ['LOTRWorldGen' + m.group(1), 'LOTRWorldGen' + m.group(1) + 'Structure']
        elif name.startswith('LOTRVillageGen'):
            candidates = [name]
        orig = next((old[c] for c in candidates if c in old), None)
        if orig:
            result.append((name, path, orig))
        elif m or name.startswith('LOTRVillageGen'):
            unpaired.append(name)
    return result, unpaired


# What a structure is made of: numbers, the blocks and items it names, its
# chest pools, string literals, and the kinds of creature it spawns. A hunk
# whose two sides name the same of these is only an API rename.
ESSENCE = re.compile(r'-?\b\d+(?:\.\d+)?f?\b|(?:LOTRMod|Blocks|Items)\.\w+|LOTRChestContents\.\w+|"[^"]*"')
ENTITY_OLD = re.compile(r'LOTREntity(\w+?)(?:\.class)?\b')
ENTITY_NEW = re.compile(r'LOTREntities\.(\w+)|LOTR(\w+?)Entity\b')


def essence(lines):
    found = []
    for line in lines:
        found += ESSENCE.findall(line)
        found += [m.lower() for m in ENTITY_OLD.findall(line)]
        found += [(a or b).replace('_', '').lower() for a, b in ENTITY_NEW.findall(line)]
    return [t.replace('f', '') if re.fullmatch(r'-?[\d.]+f', t) else t for t in found]


def essential(diff):
    out, minus, plus, header = [], [], [], None
    def flush():
        if (minus or plus) and essence(minus) != essence(plus):
            out.append(header)
            out.extend('-' + m for m in minus)
            out.extend('+' + p for p in plus)
    for d in diff:
        if d.startswith('@@'):
            flush()
            minus, plus, header = [], [], d
        elif d.startswith('-') and not d.startswith('---'):
            minus.append(d[1:])
        elif d.startswith('+') and not d.startswith('+++'):
            plus.append(d[1:])
    flush()
    return out


def main():
    args = sys.argv[1:]
    only_essential = '--essential' in args
    args = [a for a in args if a != '--essential']
    found, unpaired = pairs()
    if args == ['--unpaired']:
        print('\n'.join(unpaired))
        return
    for name, port, orig in found:
        if args and not any(a in name for a in args):
            continue
        diff = list(difflib.unified_diff(statements(orig, False), statements(port, True),
                                         os.path.relpath(orig, ROOT), os.path.relpath(port, ROOT), n=0, lineterm=''))
        if only_essential:
            diff = essential(diff)
        changed = sum(1 for d in diff if d[:1] in '+-' and d[:3] not in ('+++', '---'))
        if only_essential and args:
            if diff:
                print(f'== {name}')
                print('\n'.join(diff))
            continue
        if args:
            print('\n'.join(diff) if diff else f'{name}: no differences')
        else:
            print(f'{changed:5d}  {name}')


if __name__ == '__main__':
    main()
