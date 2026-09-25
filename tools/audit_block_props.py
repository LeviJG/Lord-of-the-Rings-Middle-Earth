#!/usr/bin/env python3
"""Hardness / blast resistance / sound audit (PORT_PLAN.md unit B2).

Old side: for each block field in old LOTRMod.java, replays the setHardness /
setResistance / setStepSound calls of its class chain's constructors and then of
its LOTRMod.java initialiser, with 1.7.10's arithmetic:
    setResistance(r): blockResistance = 3r
    setHardness(h):   hardness = h; blockResistance = max(blockResistance, 5h)
    explosion resistance = blockResistance / 5   (what modern strength() takes)
LOTRBlockStairs and LOTRBlockWallBase copy their base block (as vanilla
BlockStairs and BlockWall did).

Port side: parses LOTRBlocks.java register helper calls. Slabs, stairs, walls,
fences and fence gates copy their base block (Properties.ofFullCopy).

Blocks are matched through docs/parity/items_blocks.csv (run
tools/parity_items_blocks.py first). Classes that override getBlockHardness /
getExplosionResistance per metadata are reported as "per-meta: check by hand".

Differences are not automatically bugs. PORT_PLAN.md's "modern vanilla" rule
applies: when an old value only came from 1.7.10 vanilla (a copied vanilla
number or a 1.7.10 vanilla mechanism) and vanilla has changed since, the port
keeps the modern value. The one mechanism known here is 1.7.10 BlockWall, which
copied the strength of a single block for all sixteen wall kinds; modern walls
copy their own base, so old walls built that way are skipped.

Output: docs/parity/block_props.csv (mismatches only) and a summary.
Run from the repository root:  python3 tools/audit_block_props.py
"""
import csv, os, re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OLD_DIR = os.path.join(ROOT, "old mod/src/main/java/lotr/common/block")
OLD_MOD = os.path.join(ROOT, "old mod/src/main/java/lotr/common/LOTRMod.java")
NEW_BLOCKS = os.path.join(ROOT, "src/main/java/net/blueskiez77/lord_of_the_rings__middle_earth/common/block/LOTRBlocks.java")
ITEMS_CSV = os.path.join(ROOT, "docs/parity/items_blocks.csv")
OUT = os.path.join(ROOT, "docs/parity/block_props.csv")

SOUNDS = {"soundTypeStone": "STONE", "soundTypePiston": "STONE", "soundTypeWood": "WOOD",
          "soundTypeGrass": "GRASS", "soundTypeGravel": "GRAVEL", "soundTypeMetal": "METAL",
          "soundTypeGlass": "GLASS", "soundTypeCloth": "WOOL", "soundTypeSand": "SAND",
          "soundTypeSnow": "SNOW", "soundTypeLadder": "LADDER", "soundTypeAnvil": "ANVIL"}
CALL = re.compile(r"(?:this\.|\.|\b)(setHardness|setResistance|setStepSound)\(\s*([^()]*?(?:\([^()]*\))?)\s*\)")
FLOAT = re.compile(r"^-?[\d.]+[fFdD]?$")

# 1.7.10 vanilla parents whose constructors set these values themselves.
VANILLA_CTORS = {
    "BlockLeaves": "setHardness(0.2F); setStepSound(Block.soundTypeGrass);",
    "BlockLog": "setHardness(2.0F); setStepSound(Block.soundTypeWood);",
}
# Parents that copy hardness, resistance and sound from the block passed as the
# first constructor argument (vanilla BlockStairs and BlockWall both do).
COPIES_BASE = {"LOTRBlockStairs", "LOTRBlockWallBase"}


class Props:
    def __init__(self):
        self.h, self.res3, self.sound = 0.0, 0.0, "STONE"

    def apply(self, name, arg):
        arg = arg.strip()
        if name == "setStepSound":
            m = re.search(r"(soundType\w+)", arg)
            if m:
                self.sound = SOUNDS.get(m.group(1), m.group(1))
            return True
        if arg == "Float.MAX_VALUE":
            v = float("inf")
        elif FLOAT.match(arg):
            v = float(arg.rstrip("fFdD"))
        else:
            return False
        if name == "setHardness":
            self.h = v
            self.res3 = max(self.res3, v * 5)
        else:
            self.res3 = v * 3
        return True

    def copy(self):
        p = Props()
        p.h, p.res3, p.sound = self.h, self.res3, self.sound
        return p

    @property
    def r(self):
        return self.res3 / 5 if self.res3 == float("inf") else round(self.res3 / 5, 3)


def old_classes():
    info = {}
    for f in os.listdir(OLD_DIR):
        if f.endswith(".java"):
            text = open(os.path.join(OLD_DIR, f), encoding="utf-8", errors="replace").read()
            cls = f[:-5]
            m = re.search(r"class\s+" + cls + r"\b[^{]*?extends\s+(\w+)", text)
            ctor = "\n".join(re.findall(r"(?:public|protected|private)?\s*" + cls + r"\s*\([^)]*\)\s*\{(.*?)\n\t\}", text, re.S))
            # Conditional calls cannot be replayed blindly; the one that matters
            # (LOTRBlockSlabBase's Material.wood branch) is handled in chain().
            ctor = re.sub(r"if\s*\([^)]*\)\s*\{[^{}]*\}", "", ctor)
            per_meta = bool(re.search(r"(getBlockHardness|getExplosionResistance)\s*\(", text))
            info[cls] = dict(parent=m.group(1) if m else None, ctor=ctor, per_meta=per_meta)
    return info


def old_props():
    classes = old_classes()

    def chain(cls, seen=()):
        if cls in VANILLA_CTORS:
            return [VANILLA_CTORS[cls]], False
        if cls not in classes or cls in seen:
            return [], False
        up, pm = chain(classes[cls]["parent"], seen + (cls,))
        body = classes[cls]["ctor"]
        up = up + [body]
        if re.search(r"super\([^)]*Material\.wood", body) and "LOTRBlockSlabBase" in slab_ancestors(cls):
            up.append("setHardness(2.0f); setResistance(5.0f); setStepSound(Block.soundTypeWood);")
        return up, pm or classes[cls]["per_meta"]

    def slab_ancestors(cls):
        out = set()
        while cls in classes and cls not in out:
            out.add(cls)
            cls = classes[cls]["parent"]
        return out

    src = open(OLD_MOD, encoding="utf-8", errors="replace").read()
    result, per_meta, unknown = {}, set(), set()
    for m in re.finditer(r"^\s*(\w+)\s*=\s*\(?\s*new\s+(\w+)\s*\(([^;]*?)\)\s*((?:\.\w+\([^;]*?\))*)\s*;", src, re.M):
        field, cls, args, tail = m.groups()
        root, walk = cls, set()
        while root in classes and root not in COPIES_BASE and root not in walk:
            walk.add(root)
            root = classes[root]["parent"]
        if root in COPIES_BASE:
            base = args.split(",")[0].strip()
            if not base:  # e.g. LOTRBlockWall(): super(LOTRMod.brick, 16) -- see docstring
                result[field] = None
                continue
            if base:
                result[field] = ("stairs", base)
                continue
        bodies, pm = chain(cls)
        if not bodies and not tail:
            continue
        p = Props()
        ok = True
        for body in bodies + [tail]:
            for name, arg in CALL.findall(body):
                ok &= p.apply(name, arg)
        if not ok:
            unknown.add(field)
        if pm:
            per_meta.add(field)
        result[field] = p
    # resolve stairs to their base block's props
    for f, v in list(result.items()):
        if isinstance(v, tuple):
            base = result.get(v[1])
            result[f] = base.copy() if isinstance(base, Props) else None
    return result, per_meta, unknown


def literal_props(text):
    """(hardness, resistance, sound) from .strength(h[, r]) / .instabreak() and
    .sound(SoundType.X) literals in a piece of source, or None."""
    m = re.search(r"\.strength\(\s*([\d.]+)f?\s*(?:,\s*([\d.]+|UTUMNO_RESISTANCE)f?\s*)?\)", text)
    if m:
        h = float(m.group(1))
        r = float("inf") if m.group(2) == "UTUMNO_RESISTANCE" else float(m.group(2) or m.group(1))
    elif ".instabreak()" in text:
        h = r = 0.0
    else:
        return None
    snd = re.search(r"\.sound\(SoundType\.(\w+)\)", text)
    return (h, r, snd.group(1) if snd else "?")


def new_props():
    src = open(NEW_BLOCKS, encoding="utf-8").read()
    helper_props = {}
    for m in re.finditer(r"private static Block (register\w*)\(([^)]*)\)\s*\{(.*?)\n    \}", src, re.S):
        # Only helpers whose body fixes the numbers (no hardness parameter).
        if "hardness" not in m.group(2):
            found = literal_props(m.group(3))
            if found:
                helper_props.setdefault(m.group(1), found)
    const = {}   # JAVA_NAME -> id
    props = {}   # id -> (h, r, sound) or ("copy", JAVA_NAME)
    props_base = {}
    for m in re.finditer(r"public static final Block (\w+)\s*=\s*(?:track\([^,]+,\s*)?(register\w*)\(\s*\"([a-z0-9_]+)\"\s*,?([^;]*);", src):
        java, helper, i, rest = m.groups()
        const[java] = i
        props_base[i] = rest.split(",")[0].strip()
        args = [a.strip() for a in re.split(r",(?![^(]*\))", rest.rstrip(")").strip())] if rest.strip(") ") else []
        args = ["inf" if a == "UTUMNO_RESISTANCE" else a for a in args]
        nums = [float("inf") if a == "inf" else float(a.rstrip("fF")) for a in args if FLOAT.match(a) or a == "inf"]
        snd = next((a.split(".")[-1] for a in args if a.startswith("SoundType.")), None)
        if helper in ("registerCube", "registerCubeColumn") and len(nums) >= 2:
            props[i] = (nums[0], nums[1], snd or "STONE")
        elif helper == "registerColumn":
            props[i] = (nums[0], nums[1], "STONE") if len(nums) >= 2 else (1.5, 6.0, "STONE")
        elif helper in ("registerSoil", "registerSoilColumn") and len(nums) >= 2:
            props[i] = (nums[0], nums[1], snd or "?")
        elif helper == "registerBottomTop" and len(nums) >= 2:
            props[i] = (nums[0], nums[1], "STONE")
        elif helper == "registerSoftBlock" and nums:
            props[i] = (nums[0], nums[0], snd or "?")
        elif helper == "registerBars":
            props[i] = (5.0, 6.0, "METAL")
        elif helper in ("registerLog", "registerBeam"):
            props[i] = (2.0, 2.0, "WOOD")
        elif helper == "registerPlanks":
            props[i] = (2.0, 3.0, "WOOD")
        elif helper == "registerLeaves":
            props[i] = (0.2, 0.2, "GRASS")
        elif helper == "registerPillar":
            props[i] = (1.5, nums[0] if nums else 6.0, "STONE")
        elif helper == "registerStoneSlab":
            props[i] = (2.0, 6.0, "STONE")
        elif helper == "registerWoodBars":
            props[i] = (2.0, 3.0, "WOOD")
        elif helper == "registerReedBars":
            props[i] = (0.5, 0.5, "GRASS")
        elif helper == "registerWall" and len(nums) >= 2:
            props[i] = (nums[0], nums[1], "copy-sound")
        elif helper in ("registerSlab", "registerStairs", "registerWall", "registerFence", "registerFenceGate") and args:
            props[i] = ("copy", args[0])
        else:
            # Anything else: literal .strength()/.sound() written in the
            # registration statement itself, or else in the helper's body.
            found = literal_props(m.group(0)) or helper_props.get(helper)
            if found:
                props[i] = found
    out = {}
    for i, v in props.items():
        seen = 0
        while v and v[0] == "copy" and seen < 5:
            base = v[1]
            v = props.get(const.get(base, ""), None) if not base.startswith("Blocks.") else None
            seen += 1
        if v and v[2] == "copy-sound":
            b = props.get(const.get(props_base.get(i, ""), ""))
            v = (v[0], v[1], b[2] if b and b[0] != "copy" else "?")
        if v and v[0] != "copy":
            out[i] = v
    return out


def main():
    old, per_meta, unknown = old_props()
    new = new_props()
    rows, stats = [], defaultdict(int)
    for r in csv.DictReader(open(ITEMS_CSV, encoding="utf-8")):
        if r["kind"] != "block" or r["status"] != "ported":
            continue
        f, i = r["old_field"], r["new_id"]
        o, n = old.get(f), new.get(i)
        if not isinstance(o, Props) or n is None:
            stats["not comparable"] += 1
            continue
        diffs = []
        if abs(o.h - n[0]) > 0.01:
            diffs.append(f"hardness {o.h:g} -> {n[0]:g}")
        if not (o.r == n[1] or abs(o.r - n[1]) <= 0.01):
            diffs.append(f"resistance {o.r:g} -> {n[1]:g}")
        if n[2] != "?" and o.sound != n[2]:
            diffs.append(f"sound {o.sound} -> {n[2]}")
        note = "per-meta: check by hand" if f in per_meta else ("old value not a literal" if f in unknown else "")
        if diffs:
            stats["mismatch"] += 1
            rows.append([f, r["old_meta"], i, "; ".join(diffs), note])
        else:
            stats["match"] += 1
    with open(OUT, "w", newline="", encoding="utf-8") as fh:
        w = csv.writer(fh)
        w.writerow(["old_field", "old_meta", "new_id", "difference (old -> port)", "note"])
        w.writerows(sorted(rows, key=lambda x: x[3]))
    print(dict(stats))


if __name__ == "__main__":
    main()
