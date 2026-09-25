#!/usr/bin/env python3
"""Class-level parity ledger (PORT_PLAN.md unit A2).

One row per class under old mod/src/main/java/lotr/, with the port class it most
likely became. Run tools/parity_items_blocks.py first: block/item classes with
no same-named port class are resolved through the items they back.

Statuses:
  ported    - a port class with the same core name exists
  absorbed  - no same-named class, but every item/block the old class backs is
              ported (e.g. LOTRBlockRock -> plain blocks in LOTRBlocks)
  partial   - some of the items/blocks it backs are ported, some are missing
  excluded  - listed in EXCLUDED_CLASSES (deliberately not ported)
  base      - abstract class; its real status is its subclasses' statuses
  referenced- no class match, but port source names the old class (usually a
              "port of X" comment): probably absorbed, confirm in the B audit
  missing   - nothing found
  review    - hand-set in tools/parity_class_overrides.csv, or a weak name match

Writes docs/parity/classes.csv and prints a per-package summary.
Run from the repository root:  python3 tools/parity_classes.py
"""
import csv, os, re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OLD_SRC = os.path.join(ROOT, "old mod/src/main/java")
OLD_MOD = os.path.join(OLD_SRC, "lotr/common/LOTRMod.java")
NEW_SRCS = [os.path.join(ROOT, p) for p in ("src/main/java", "src/client/java")]
ITEMS_CSV = os.path.join(ROOT, "docs/parity/items_blocks.csv")
OVERRIDES = os.path.join(ROOT, "tools/parity_class_overrides.csv")
OUT = os.path.join(ROOT, "docs/parity/classes.csv")

# Words that differ only by convention between the two codebases
# (LOTRBlockGate <-> LOTRGateBlock, LOTRTileEntityForge <-> LOTRForgeBlockEntity, ...).
NOISE = {"lotr", "block", "item", "tile", "entity", "blockentity", "tileentity", "container",
         "menu", "gui", "screen", "render", "renderer", "model", "base", "state"}

EXCLUDED_CLASSES = {"LOTRBlockGravel", "LOTRItemPouch", "LOTRContainerPouch", "LOTRGuiPouch",
                    "LOTRInventoryPouch", "LOTRItemArmorStand", "LOTRBlockArmorStand",
                    "LOTRTileEntityArmorStand", "LOTRRenderArmorStand", "LOTRGuiArmorStand",
                    "LOTRContainerArmorStand", "LOTRModelArmorStand"}


def words(cls):
    return re.findall(r"[A-Z]+(?=[A-Z][a-z]|\d|$)|[A-Z]?[a-z]+|\d+", cls)


def core(cls):
    return tuple(sorted(w.lower() for w in words(cls) if w.lower() not in NOISE))


# Old naming prefix -> port naming suffix, used to pick the right class when
# several share a core name (LOTRGuiBarrel -> LOTRBarrelScreen, not LOTRBarrelBlock).
ROLES = [("TileEntity", "BlockEntity"), ("Container", "Menu"), ("Gui", "Screen"),
         ("Render", "Renderer"), ("Model", "Model"), ("Block", "Block"), ("Item", "Item"),
         ("Entity", "Entity")]


def pick(cls, cands):
    for old_prefix, new_suffix in ROLES:
        if cls.startswith("LOTR" + old_prefix):
            same = [c for c in cands if c.endswith(new_suffix)]
            return same or cands
    return cands


def java_classes(roots):
    out = {}
    for root in roots:
        for d, _, files in os.walk(root):
            for f in files:
                if f.endswith(".java"):
                    out[f[:-5]] = os.path.relpath(os.path.join(d, f), ROOT)
    return out


def item_status():
    """old field -> list of statuses from items_blocks.csv"""
    st = defaultdict(list)
    if os.path.exists(ITEMS_CSV):
        for r in csv.DictReader(open(ITEMS_CSV, encoding="utf-8")):
            st[r["old_field"]].append(r["status"])
    return st


def fields_by_class():
    src = open(OLD_MOD, encoding="utf-8", errors="replace").read()
    out = defaultdict(set)
    for field, cls in re.findall(r"^\s*(\w+)\s*=\s*\(?\s*new\s+(\w+)", src, re.M):
        out[cls].add(field)
    return out


def main():
    old = java_classes([os.path.join(OLD_SRC, "lotr")])
    new = java_classes(NEW_SRCS)
    new_by_core = defaultdict(list)
    for c in new:
        new_by_core[core(c)].append(c)
    istat, fbc = item_status(), fields_by_class()
    refs = defaultdict(set)
    for c, path in new.items():
        text = open(os.path.join(ROOT, path), encoding="utf-8", errors="replace").read()
        for name in set(re.findall(r"\bLOTR\w+", text)):
            if name in old and name not in new:
                refs[name].add(c)
    supers, abstract = defaultdict(set), set()
    for c, path in old.items():
        text = open(os.path.join(ROOT, path), encoding="utf-8", errors="replace").read()
        m = re.search(r"class\s+\w+(?:<[^>]*>)?\s+extends\s+(\w+)", text)
        if m:
            supers[m.group(1)].add(c)
        if re.search(r"\babstract\s+class\s+" + c + r"\b", text):
            abstract.add(c)
    overrides = {}
    if os.path.exists(OVERRIDES):
        for r in csv.reader(open(OVERRIDES, encoding="utf-8")):
            if r and not r[0].startswith("#"):
                overrides[r[0].strip()] = [c.strip() for c in r[1:]] + ["", "", ""]

    rows = []
    for cls, path in sorted(old.items(), key=lambda kv: kv[1]):
        pkg = os.path.dirname(os.path.relpath(path, "old mod/src/main/java/lotr")).replace(os.sep, ".")
        new_cls, status, notes = "", "", ""
        if cls in overrides:
            new_cls, status, notes = overrides[cls][:3]
        elif cls in EXCLUDED_CLASSES:
            status = "excluded"
        elif cls in new:
            new_cls, status = cls, "ported"
        elif new_by_core.get(core(cls)):
            new_cls, status = " ".join(pick(cls, new_by_core[core(cls)])), "ported"
        else:
            fields = fbc.get(cls, set())
            sts = [s for f in fields for s in istat.get(f, [])]
            if sts:
                done = [s for s in sts if s != "missing"]
                status = "absorbed" if len(done) == len(sts) else ("partial" if done else "missing")
                notes = f"backs {len(fields)} field(s): " + " ".join(sorted(fields)[:6]) + (" ..." if len(fields) > 6 else "")
            elif refs.get(cls):
                status, notes = "referenced", "named in: " + " ".join(sorted(refs[cls])[:4]) + (" ..." if len(refs[cls]) > 4 else "")
            elif cls in abstract:
                status, notes = "base", "extended by: " + " ".join(sorted(supers[cls])[:6]) + (" ..." if len(supers[cls]) > 6 else "")
            else:
                status = "missing"
        rows.append([pkg, cls, new_cls, status, notes])

    os.makedirs(os.path.dirname(OUT), exist_ok=True)
    with open(OUT, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["old_package", "old_class", "new_class", "status", "notes"])
        w.writerows(rows)

    summary = defaultdict(lambda: defaultdict(int))
    for pkg, _, _, st, _ in rows:
        summary[pkg][st] += 1
    for pkg in sorted(summary):
        s = summary[pkg]
        print(f"{pkg:32} total {sum(s.values()):4}  " + "  ".join(f"{k} {v}" for k, v in sorted(s.items())))


if __name__ == "__main__":
    main()
