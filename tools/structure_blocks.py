#!/usr/bin/env python3
"""Structure block check: every (block, metadata) the structures name, against
what LOTRLegacyBlocks can make of it.

Reads every port structure and village class (common/world/structure,
common/world/village) and every .strscan scan, collects each legacy block they
name -- LOTRLegacyBlocks.mod("field") / .vanilla("name") with the metadata
given beside it, palette fields set as a "xBlock = ...; xMeta = n;" pair, and
the scans' "name".meta steps -- and reports what would go wrong:

  UNKNOWN MOD     the field has no row in lotr/legacy_blocks.tsv: it places air.
  UNKNOWN VANILLA 1.7.10 has no block of that name (by 1.12's BlockStateData).
  NO ROW          the field's rows do not cover this metadata (after the
                  kind mask): it falls back to the first row, a wrong variant.
  ANY-META        a "*" field given several metadata values: whatever the
                  metadata carried (orientation, a kind) is only kept if the
                  port block is one of the shapes LOTRLegacyBlocks orients;
                  listed for review, not as an error.

Usage: tools/structure_blocks.py [--any-meta]
"""
import glob
import os
import re
import sys
import zipfile
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WORLD = os.path.join(ROOT, 'src/main/java/net/blueskiez77/lord_of_the_rings__middle_earth/common/world')
SCANS = os.path.join(ROOT, 'src/main/resources/data/lotr/strscan')
TSV = os.path.join(ROOT, 'src/main/resources/lotr/legacy_blocks.tsv')

LITERAL = re.compile(r'LOTRLegacyBlocks\.(mod|vanilla)\("(\w+)"\)(?:\s*,\s*(\d+)\s*[),])?')
PAIR = re.compile(r'(\w+)Block\s*=\s*LOTRLegacyBlocks\.(mod|vanilla)\("(\w+)"\);\s*\n\s*\1Meta\s*=\s*(\d+);')
SCAN_STEP = re.compile(r'"([\w:.]+)"\.(\d+)$')


def load_tsv():
    rows, by_unloc = defaultdict(list), {}
    for line in open(TSV, encoding='utf-8'):
        if not line.strip() or line.startswith('#'):
            continue
        cols = line.rstrip('\n').split('\t')
        rows[cols[0]].append(cols[1])
        if len(cols) > 4 and cols[4]:
            by_unloc.setdefault(cols[4], cols[0])
    return rows, by_unloc


def old_vanilla_names():
    jars = glob.glob(os.path.join(ROOT, '.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-common-*/*/*-sources.jar'))
    if not jars:
        return None
    with zipfile.ZipFile(jars[0]) as z:
        src = z.read('net/minecraft/util/datafix/fixes/BlockStateData.java').decode()
    names = set()
    for part in src.split('register(')[1:]:
        found = re.findall(r'create\("([^"]+)"', part)
        names.update(n[len('minecraft:'):] for n in found[1:] if n.startswith('minecraft:'))
    # The skull flattens to a placeholder (its kind was its block entity's);
    # LOTRLegacyBlocks.vanilla handles it itself.
    names.add('skull')
    return names


def mask_of(metas):
    if '*' in metas:
        return None
    top = max(int(m) for m in metas)
    return (1 << max(top, 1).bit_length()) - 1


def main():
    rows, by_unloc = load_tsv()
    vanilla = old_vanilla_names()
    uses = defaultdict(lambda: defaultdict(set))  # (kind, name) -> meta -> {where}

    for path in glob.glob(os.path.join(WORLD, '**/*.java'), recursive=True):
        text = open(path, encoding='utf-8').read()
        where = os.path.basename(path)[:-5]
        for m in LITERAL.finditer(text):
            uses[(m.group(1), m.group(2))][m.group(3)].add(where)
        for m in PAIR.finditer(text):
            uses[(m.group(2), m.group(3))][m.group(4)].add(where)
    for path in glob.glob(os.path.join(SCANS, '*.strscan')):
        where = os.path.basename(path)
        for line in open(path, encoding='utf-8'):
            m = SCAN_STEP.search(line.strip())
            if not m:
                continue
            name, meta = m.group(1), m.group(2)
            if name.startswith('lotr:tile.'):
                unloc = name[len('lotr:tile.'):]
                uses[('mod', by_unloc.get(unloc, unloc))][meta].add(where)
            else:
                uses[('vanilla', name.replace('minecraft:', ''))][meta].add(where)

    problems = 0
    for (kind, name), metas in sorted(uses.items()):
        places = sorted({w for ws in metas.values() for w in ws})
        if kind == 'mod':
            if name not in rows:
                if name != 'armorStand':
                    print(f'UNKNOWN MOD     {name}: {", ".join(places)}')
                    problems += 1
                continue
            mask = mask_of(rows[name])
            if mask is None:
                given = sorted(int(m) for m in metas if m is not None)
                if '--any-meta' in sys.argv and len(set(given)) > 1:
                    print(f'ANY-META        {name} {given}: {", ".join(places)}')
                continue
            covered = {int(r) for r in rows[name]}
            for meta, ws in sorted(metas.items(), key=lambda kv: int(kv[0] or 0)):
                if meta is not None and (int(meta) & mask) not in covered:
                    print(f'NO ROW          {name} meta {meta} (rows {sorted(covered)}): {", ".join(sorted(ws))}')
                    problems += 1
        elif vanilla is not None and name not in vanilla:
            print(f'UNKNOWN VANILLA {name}: {", ".join(places)}')
            problems += 1
    print(f'{len(uses)} blocks named, {problems} problems')


if __name__ == '__main__':
    main()
