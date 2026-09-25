#!/usr/bin/env python3
"""Parity ledger for items and blocks (PORT_PLAN.md unit A1).

Maps every item/block of the 1.7.10 mod (fields in old LOTRMod.java plus every
metadata subtype named in the old en_US.lang) to a registry ID in the port.

Matching, in order of confidence:
  override  - tools/parity_overrides.csv (old unloc[.meta] or old field,new_id[,status,notes])
  name      - identical English display name (normalised)
  id        - snake_case of the old unlocalised name equals a port ID
  fuzzy     - closest display name by difflib ratio >= FUZZY_MIN -> status "review"
  missing   - nothing found
Other statuses: excluded (deliberately dropped), merged (e.g. double slabs),
vanilla? (now in vanilla; confirm via an override row).

Writes docs/parity/items_blocks.csv and docs/parity/port_only.csv (port IDs no
old entry mapped to: renames the matcher missed, or port additions).

Run from the repository root:  python3 tools/parity_items_blocks.py
"""
import csv, difflib, json, os, re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OLD = os.path.join(ROOT, "old mod/src/main")
OLD_MOD = os.path.join(OLD, "java/lotr/common/LOTRMod.java")
OLD_LANG = os.path.join(OLD, "resources/assets/lotr/lang/en_US.lang")
NEW_LANG = os.path.join(ROOT, "src/main/resources/assets/lotr/lang/en_us.json")
NEW_ASSET_DIRS = [os.path.join(ROOT, p, "assets/lotr") for p in ("src/main/resources", "src/main/generated")]
OVERRIDES = os.path.join(ROOT, "tools/parity_overrides.csv")
OUT_DIR = os.path.join(ROOT, "docs/parity")
FUZZY_MIN = 0.85

# Deliberately not ported (see PORT_PLAN.md standing rules).
EXCLUDED = {"Gravel", "armorStand", "armorStandItem", "pouch"}

# Old entries that now exist in vanilla 26.2. Candidates only: status
# "vanilla?" until the user confirms each one in tools/parity_overrides.csv.
VANILLA = {"oreCopper", "ironNugget", "rabbitRaw", "rabbitCooked", "horseArmorIron",
           "horseArmorGold", "horseArmorDiamond", "redSandstone", "stairsRedSandstone",
           "stairsCobblestoneMossy", "mobSpawner", "flowerPot", "doorSpruce", "doorBirch",
           "doorJungle", "doorAcacia", "doorDarkOak", "trapdoorSpruce", "trapdoorBirch",
           "trapdoorJungle", "trapdoorAcacia", "trapdoorDarkOak", "fenceGateSpruce",
           "fenceGateBirch", "fenceGateJungle", "fenceGateAcacia", "fenceGateDarkOak",
           "spawnEgg"}
VANILLA_META = {("oreStorage", "0"), ("smoothStoneV", "0"), ("slabSingleV", "4"), ("dirtPath", "0")}


def norm(name):
    return re.sub(r"[^a-z0-9]+", " ", name.lower()).strip()


def snake(name):
    return re.sub(r"([a-z0-9])([A-Z])", r"\1_\2", name).lower()


def id_guesses(unloc):
    """snake_case of the old name plus every rotation of its words, since the
    port puts the material first: pressurePlateMordorRock -> mordor_rock_pressure_plate."""
    parts = re.sub(r"\d+$", "", snake(unloc)).split("_")
    return ["_".join(parts[i:] + parts[:i]) for i in range(len(parts))]


# The old mod names wood blocks "X Wood" / "X Wood Planks"; the port says "X Log" / "X Planks".
NAME_REWRITES = [
    (r"^(.*) wood$", r"\1 log"),
    (r"^(.*) wood (planks|slab|stairs|beam|fence|fence gate|door|trapdoor)$", r"\1 \2"),
]


def name_variants(n):
    out = [n]
    for pat, rep in NAME_REWRITES:
        if re.match(pat, n):
            out.append(re.sub(pat, rep, n))
    return out


def old_entries():
    """Rows keyed 'unloc' or 'unloc.meta' -> (kind, field, unloc, meta, name)."""
    src = open(OLD_MOD, encoding="utf-8", errors="replace").read()
    field_of = {}
    for field, kind, unloc in re.findall(
            r'^\s*(\w+)\s*=\s*new .*?set(Block|Unlocalized)Name\("lotr:(\w+)"\)', src, re.M):
        field_of[("block" if kind == "Block" else "item", unloc)] = field

    lang = {}
    for line in open(OLD_LANG, encoding="utf-8", errors="replace"):
        m = re.match(r"(tile|item)\.lotr:(\w+)(?:\.(\w+))?\.name=(.*)", line.strip())
        if m:
            lang[("block" if m.group(1) == "tile" else "item", m.group(2), m.group(3))] = m.group(4).strip()

    # Keys include the kind: an item and a block may share an unlocalised name
    # (flax the item, flax the crop block).
    has_meta = {(k, u) for (k, u, meta) in lang if meta is not None}
    rows = {}
    for (kind, unloc, meta), name in lang.items():
        if meta is None and (kind, unloc) in has_meta:
            continue  # base name of a metadata block; the subtypes carry the content
        field = field_of.get((kind, unloc), unloc)
        rows[(kind, unloc, meta)] = dict(kind=kind, field=field, unloc=unloc, meta=meta or "", name=name)
    for (kind, unloc), field in field_of.items():
        if not any(k == kind and u == unloc for (k, u, _) in rows):
            rows[(kind, unloc, None)] = dict(kind=kind, field=field, unloc=unloc, meta="", name="")
    return rows


def new_entries():
    ids = {}
    for base in NEW_ASSET_DIRS:
        for sub, kind in (("items", "item"), ("blockstates", "block")):
            d = os.path.join(base, sub)
            if os.path.isdir(d):
                for f in os.listdir(d):
                    if f.endswith(".json"):
                        i = f[:-5]
                        if kind == "block" or i not in ids:
                            ids[i] = kind
    lang = json.load(open(NEW_LANG, encoding="utf-8"))
    names = {}
    for k, v in lang.items():
        m = re.match(r"(item|block)\.lotr\.([a-z0-9_]+)(?:\.([a-z0-9_]+))?$", k)
        if m:
            i = m.group(2) + (f"[{m.group(3)}]" if m.group(3) else "")
            names[i] = v
            ids.setdefault(i, m.group(1))
    return ids, names


def load_overrides():
    out = {}
    if os.path.exists(OVERRIDES):
        for r in csv.reader(open(OVERRIDES, encoding="utf-8")):
            if r and not r[0].startswith("#"):
                out[r[0].strip()] = [c.strip() for c in r[1:]] + ["", "", ""]
    return out


def main():
    old = old_entries()
    ids, names = new_entries()
    overrides = load_overrides()

    by_name = defaultdict(list)
    for i, n in names.items():
        by_name[norm(n)].append(i)
    name_keys = list(by_name)

    used = set()
    rows = []
    for (_, unloc, meta), r in sorted(old.items(), key=lambda kv: (kv[0][0], kv[0][1], kv[0][2] or "")):
        key = unloc + ("." + meta if meta else "")
        new_id, how, status, notes = "", "", "", ""
        okey = next((k for k in (key, unloc, r["field"]) if k in overrides), None)
        if okey:
            new_id, status, notes = overrides[okey][:3]
            how, status = "override", status or ("ported" if new_id else "missing")
        elif r["field"] in EXCLUDED or unloc in EXCLUDED:
            status, how = "excluded", "rule"
        elif re.match(r"(wood|rotten|scorched)?[sS]lab\w*Double", unloc) or re.match(r"slab(Bone|Utumno|ClayTile|ClayTileDyed)Double", unloc):
            status, how, notes = "merged", "rule", "double slab is a slab block state in modern MC"
        else:
            n = norm(r["name"])
            cands = []
            for v in (name_variants(n) if n else []):
                cands = by_name.get(v, [])
                if cands:
                    break
            if cands:
                same_kind = [c for c in cands if ids.get(c) == r["kind"]] or cands
                new_id, how = same_kind[0], "name"
                if len(cands) > 1:
                    notes = "also: " + " ".join(c for c in cands if c != new_id)
            elif not meta and any(ids.get(g) == r["kind"] for g in id_guesses(unloc) + id_guesses(r["field"])):
                new_id, how = next(g for g in id_guesses(unloc) + id_guesses(r["field"]) if ids.get(g) == r["kind"]), "id"
            elif n:
                best = difflib.get_close_matches(n, name_keys, n=1, cutoff=FUZZY_MIN)
                if best:
                    new_id, how = by_name[best[0]][0], "fuzzy"
                    notes = f'port name "{names[new_id]}"'
            status = "ported" if new_id else "missing"
            if how == "fuzzy":
                status = "review"
            if status != "ported" and (unloc in VANILLA or (unloc, meta) in VANILLA_META):
                new_id, status, how, notes = "", "vanilla?", "rule", "exists in vanilla; confirm exclusion"
        if new_id:
            used.add(new_id)
        rows.append([r["kind"], r["field"], r["unloc"], r["meta"], r["name"], new_id, status, how, notes])

    os.makedirs(OUT_DIR, exist_ok=True)
    with open(os.path.join(OUT_DIR, "items_blocks.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["kind", "old_field", "old_unloc", "old_meta", "old_name", "new_id", "status", "match", "notes"])
        w.writerows(rows)
    with open(os.path.join(OUT_DIR, "port_only.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["new_id", "kind", "name"])
        for i in sorted(set(ids) - used):
            w.writerow([i, ids[i], names.get(i, "")])

    tally = defaultdict(int)
    for r in rows:
        tally[(r[0], r[6], r[7])] += 1
    for k in sorted(tally):
        print(*k, tally[k])
    print("port-only ids:", len(set(ids) - used))


if __name__ == "__main__":
    main()
