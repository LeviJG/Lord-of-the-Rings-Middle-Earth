#!/usr/bin/env python3
"""Recipe parity: every recipe statement in the 1.7.10 LOTRRecipes.java against
the port's recipe JSONs (generated + hand-written), keyed on (table, result).

Writes docs/parity/recipes.csv. Status per old recipe:
  present   a port recipe with the same result exists at that table
  missing   not present, and the result and every LOTRMod ingredient are ported
  blocked   not present, and something it needs is not ported (see notes)
  excluded  result is an excluded / vanilla-duplicate entry
  manual    built in a loop or through a helper -- check by hand

"table" is the port's LOTRCraftingTable name, or "vanilla" for GameRegistry.
Run from the repo root.
"""
import csv
import glob
import json
import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OLD = os.path.join(ROOT, "old mod/src/main/java/lotr/common/recipe/LOTRRecipes.java")
LEDGER = os.path.join(ROOT, "docs/parity/items_blocks.csv")
OUT = os.path.join(ROOT, "docs/parity/recipes.csv")
RECIPE_DIRS = [os.path.join(ROOT, "src/main/generated/data/lotr/recipe"),
               os.path.join(ROOT, "src/main/resources/data/lotr/recipe")]

LIST_TO_TABLE = {
    "morgul": "morgul", "elven": "elven", "dwarven": "dwarven", "uruk": "uruk",
    "woodElven": "wood_elven", "gondorian": "gondorian", "rohirric": "rohirric",
    "dunlending": "dunlending", "angmar": "angmar", "nearHarad": "near_harad",
    "highElven": "high_elven", "blueMountains": "blue_dwarven", "ranger": "ranger",
    "dolGuldur": "dol_guldur", "gundabad": "gundabad", "halfTroll": "half_troll",
    "dolAmroth": "dol_amroth", "moredain": "moredain", "tauredain": "tauredain",
    "dale": "dale", "dorwinion": "dorwinion", "hobbit": "hobbit", "rhun": "rhun",
    "rivendell": "rivendell", "umbar": "umbar", "gulf": "gulf", "bree": "bree",
}
GROUPS = {
    "commonOrc": ["morgul", "uruk", "angmar", "dol_guldur", "gundabad", "half_troll"],
    "commonMorgul": ["morgul", "angmar", "dol_guldur"],
    "commonMorgulAndGundabad": ["morgul", "angmar", "dol_guldur", "gundabad"],
    "commonElf": ["elven", "wood_elven", "high_elven", "rivendell"],
    "commonHighElf": ["high_elven", "rivendell"],
    "commonDwarf": ["dwarven", "blue_dwarven"],
    "commonNumenorean": ["gondorian", "dol_amroth", "umbar"],
    "commonNearHarad": ["near_harad", "umbar", "gulf"],
    "commonHobbit": ["hobbit", "bree"],
}

# 1.7.10 vanilla results with metadata or renamed ids, as 26.2 ids.
DYES = ["ink_sac", "red_dye", "green_dye", "cocoa_beans", "lapis_lazuli", "purple_dye",
        "cyan_dye", "light_gray_dye", "gray_dye", "pink_dye", "lime_dye", "yellow_dye",
        "light_blue_dye", "magenta_dye", "orange_dye", "bone_meal"]
VANILLA = {("stonebrick", "3"): "chiseled_stone_bricks", ("hardened_clay", ""): "terracotta",
           ("packed_ice", ""): "packed_ice", ("saddle", ""): "saddle"}


def vanilla_id(field, meta):
    if field == "dye" and meta.isdigit():
        return "minecraft:" + DYES[int(meta)]
    name = VANILLA.get((field, meta)) or re.sub(r"(?<!^)(?=[A-Z])", "_", field).lower()
    return "minecraft:" + name


BANNER_SRC = os.path.join(ROOT, "old mod/src/main/java/lotr/common/item/LOTRItemBanner.java")
# Ledger rows that list their metadata variants only in the notes.
META_IDS = {("orcBomb", "1"): "double_strength_orc_bomb", ("orcBomb", "2"): "triple_strength_orc_bomb",
            ("orcBomb", "8"): "orc_fire_bomb", ("orcBomb", "9"): "double_strength_orc_fire_bomb",
            ("orcBomb", "10"): "triple_strength_orc_fire_bomb",
            ("silverCoin", ""): "silver_coin", ("commandHorn", ""): "command_horn",
            ("utumnoKey", "5"): "obsidian_key_handle", ("utumnoKey", "6"): "obsidian_key_shaft",
            ("utumnoKey", "7"): "obsidian_key_pin", ("redClayBall", ""): "red_clay_ball",
            ("daleCracker", "1"): "blue_dalish_cracker", ("daleCracker", "2"): "green_dalish_cracker",
            ("daleCracker", "3"): "silver_dalish_cracker", ("daleCracker", "4"): "gold_dalish_cracker"}


# Ore dictionary name -> what the port recipe must use for it (any one of).
DYE_COLOURS = ["black", "red", "green", "brown", "blue", "purple", "cyan", "lightGray", "gray", "pink",
               "lime", "yellow", "lightBlue", "magenta", "orange", "white"]
ORES = {
    "plankWood": ["#minecraft:planks"], "stickWood": ["#lotr:sticks"], "logWood": ["#minecraft:logs"],
    "slabWood": ["#minecraft:wooden_slabs"], "stairWood": ["#minecraft:wooden_stairs"],
    "treeLeaves": ["#minecraft:leaves"], "treeSapling": ["#minecraft:saplings"],
    "vine": ["#lotr:vines"], "feather": ["#lotr:feathers"], "bone": ["#lotr:bones"],
    "apple": ["#lotr:apples"], "clayBall": ["#lotr:clay_balls"], "horn": ["#lotr:horns"],
    "arrowTip": ["#lotr:arrow_tips"], "berry": ["#lotr:berries"], "poison": ["lotr:bottle_of_poison"],
    "ingotIron": ["minecraft:iron_ingot"], "ingotGold": ["minecraft:gold_ingot"],
    "ingotCopper": ["minecraft:copper_ingot"], "ingotTin": ["lotr:tin_ingot"],
    "ingotBronze": ["lotr:bronze_ingot"], "ingotSilver": ["lotr:silver_ingot"],
    "nuggetGold": ["minecraft:gold_nugget"], "nuggetIron": ["minecraft:iron_nugget"],
    "nuggetSilver": ["lotr:silver_nugget"], "gemDiamond": ["minecraft:diamond"],
    # createRhunRecipes registers "rhunStone" as stone and sandstone; sandstone is the tell.
    "rhunStone": ["minecraft:sandstone"],
    "gemEmerald": ["minecraft:emerald"], "gemLapis": ["minecraft:lapis_lazuli"], "dustRedstone": ["minecraft:redstone"],
    "sulfur": ["lotr:sulfur"], "saltpeter": ["lotr:niter"], "salt": ["lotr:salt"],
    "sand": ["#minecraft:sand"], "sandstone": ["#minecraft:sandstone", "#c:sandstone/blocks"],
    "cropWheat": ["minecraft:wheat"], "stone": ["minecraft:stone"], "cobblestone": ["minecraft:cobblestone"],
}
for c in DYE_COLOURS:
    snake = re.sub(r"(?<!^)(?=[A-Z])", "_", c).lower()
    ORES["dye" + c[0].upper() + c[1:]] = ["#c:dyes/" + snake]
ORE_NAME = re.compile(r'"([a-z][A-Za-z]+)"')


def banner_names():
    """BannerType constant -> bannerName, which is what the ledger keys on."""
    src = open(BANNER_SRC).read()
    return dict(re.findall(r"(\w+)\(\d+, \"(\w+)\", LOTRFaction", src))


def load_ledger():
    by_field = {k: "lotr:" + v for k, v in META_IDS.items()}
    status = {k: "ported" for k in META_IDS}
    with open(LEDGER, newline="") as f:
        for row in csv.DictReader(f):
            key = (row["old_field"], row["old_meta"] or "")
            if row["new_id"] and key not in by_field:
                by_field[key] = "lotr:" + row["new_id"]
            status[key] = row["status"]
    return by_field, status


def resolve(field, meta, by_field, status):
    """(port id or None, ledger status)."""
    keys = ((field, meta), (field, "")) if meta else ((field, ""), (field, "0"))
    for key in keys:
        if key in by_field:
            return by_field[key], status.get(key, "")
    for key in keys:
        if key in status:
            return None, status[key]
    return None, "unknown"


def split_methods(src):
    methods = {}
    for m in re.finditer(r"public static void (\w+)\(\)\s*\{", src):
        depth, i = 1, m.end()
        while depth:
            depth += {"{": 1, "}": -1}.get(src[i], 0)
            i += 1
        methods[m.group(1)] = src[m.end():i - 1]
    return methods


LOOP = re.compile(r"for \((?:int )?(\w+) = (\d+); \1 (<=|<) (\d+);")


def statements(body):
    """Top-level statements, each with the stack of loops around it: a list of
    (var, range) for numeric for-loops, or (None, None) for any other loop."""
    out, depth, start, loops, expr, heads = [], 0, 0, [], 0, []
    i = 0
    while i < len(body):
        c = body[i]
        balanced = body[start:i].count("(") == body[start:i].count(")")
        if c == "{" and not balanced:
            expr += 1  # array initialiser inside an expression
        elif c == "}" and expr:
            expr -= 1
        elif c == "{":
            head = body[start:i].strip()
            heads.append(head)
            if head.startswith(("for", "while")):
                m = LOOP.match(head)
                rng = None
                if m:
                    lo, hi = int(m.group(2)), int(m.group(4))
                    rng = range(lo, hi + 1 if m.group(3) == "<=" else hi)
                loops.append((depth, m.group(1) if m else None, rng))
            depth += 1
            start = i + 1
        elif c == "}":
            if heads:
                heads.pop()
            depth -= 1
            if loops and loops[-1][0] == depth:
                loops.pop()
            start = i + 1
        elif c == ";" and balanced:
            skip = re.match(r"if \((\w+) == (\d+)\)", heads[-1]) if heads else None
            if body[start:i].strip() == "continue" and skip and loops and loops[-1][1] == skip.group(1):
                d, v, r = loops[-1]
                loops[-1] = (d, v, [n for n in r if n != int(skip.group(2))])
                start = i + 1
                i += 1
                continue
            out.append((body[start:i].strip(), [(v, r) for _, v, r in loops]))
            start = i + 1
        i += 1
    return out


def expand(stmt, loops):
    """The statement once per loop iteration, loop variables substituted and
    simple arithmetic folded; None if a loop cannot be expanded."""
    if any(v is None for v, _ in loops):
        return None
    combos = [{}]
    for v, r in loops:
        combos = [dict(c, **{v: n}) for c in combos for n in r]
    out = []
    for env in combos:
        text = stmt
        for v, n in env.items():
            text = re.sub(r"\b" + v + r"\b", str(n), text)
        text = re.sub(r"(?<=, )(\d+ [-+*] \d+)(?=\))", lambda m: str(eval(m.group(1))), text)
        out.append(text)
    return out


RESULT = re.compile(r"new ItemStack\((?:LOTRMod|LOTRBanner|Items|Blocks)\.(\w+)(?:\s*,\s*(\d+))?(?:\s*,\s*([^)]+))?\)")
INGREDIENT = re.compile(r"LOTRMod\.(\w+)(?:\s*,\s*\d+\s*,\s*(\d+))?")
BANNER = re.compile(r"new ItemStack\(LOTRMod\.banner, \d+, LOTRItemBanner\.BannerType\.(\w+)\.bannerID\)")


def targets(stmt):
    if stmt.startswith(("GameRegistry.add", "woodenSlabRecipes.add")):
        return ["vanilla"]
    m = re.match(r"(\w+)Recipes\.add\(", stmt)
    if m:
        name = m.group(1)
        if name in LIST_TO_TABLE:
            return [LIST_TO_TABLE[name]]
        return None
    m = re.match(r"addRecipeTo\((\w+)Recipes,", stmt) or re.match(r"addDyeableWoolRobeRecipes\((\w+)Recipes,", stmt)
    if m:
        name = m.group(1)
        return GROUPS.get(name) or ([LIST_TO_TABLE[name]] if name in LIST_TO_TABLE else None)
    if stmt.startswith(("GameRegistry.add", "woodenSlabRecipes.add")):
        return ["vanilla"]
    return None


CRAFTING_TYPES = ("minecraft:crafting_shaped", "minecraft:crafting_shapeless", "minecraft:crafting_transmute",
                  "minecraft:smelting", "lotr:faction_crafting", "lotr:faction_crafting_shapeless",
                  "lotr:faction_crafting_transmute")


def strings(node):
    if isinstance(node, str):
        yield node
    elif isinstance(node, list):
        for n in node:
            yield from strings(n)
    elif isinstance(node, dict):
        for n in node.values():
            yield from strings(n)


def canon(rows):
    """A shaped pattern with its key letters renamed in order of appearance,
    so "XYX" and "ABA" compare equal. Empty for shapeless recipes."""
    if not rows:
        return ""
    names = {}
    out = []
    for row in rows:
        out.append("".join(" " if c == " " else names.setdefault(c, "abcdefghi"[len(names)]) for c in row))
    return "/".join(out)


def port_recipes():
    """(table, result id) -> [(count, ingredient ids, path)] for crafting and
    smelting recipes."""
    have = {}
    for d in RECIPE_DIRS:
        for path in glob.glob(os.path.join(d, "**/*.json"), recursive=True):
            try:
                data = json.load(open(path))
            except Exception:
                continue
            if data.get("type") not in CRAFTING_TYPES:
                continue
            res = data.get("result")
            rid = res.get("id") if isinstance(res, dict) else res
            if not rid:
                continue
            count = res.get("count", 1) if isinstance(res, dict) else 1
            ings = set()
            for k in ("key", "ingredients", "ingredient", "input", "material"):
                ings.update(strings(data.get(k)))
            table = data.get("table", "vanilla")
            ings.add("pattern=" + canon(data.get("pattern")))
            have.setdefault((table, rid), []).append((count, ings, os.path.relpath(path, ROOT)))
    return have


def main():
    by_field, status = load_ledger()
    have = port_recipes()
    banners = banner_names()
    used = set()
    src = open(OLD).read()
    rows = []
    for method, body in split_methods(src).items():
        # createAllRecipes only calls the others; unsmelting lists are B5's.
        if not method.startswith(("create", "modify")) or "Unsmelting" in method or method == "createAllRecipes":
            continue
        expanded = []
        for stmt, loops in statements(body):
            if loops:
                parts = expand(stmt, loops)
                if parts is None:
                    expanded.append((stmt, True))
                else:
                    expanded.extend((p, False) for p in parts)
            else:
                expanded.append((stmt, False))
        for stmt, in_loop in expanded:
            stmt = BANNER.sub(lambda b: "new ItemStack(LOTRMod.banner, 1, " + banners[b.group(1)] + ")", stmt)
            tabs = targets(stmt)
            if tabs is None and method == "modifyStandardRecipes":
                continue  # removeRecipes*: the vanilla recipe rule
            if tabs is None:
                if "Recipe" in stmt and "(" in stmt and not stmt.startswith(("int ", "Object", "ItemStack", "List")):
                    rows.append([method, "?", "", "", "manual", "helper: " + stmt[:90]])
                continue
            m = RESULT.search(stmt)
            pw = re.search(r"new LOTRRecipePoisonWeapon\(LOTRMod\.\w+, LOTRMod\.(\w+)", stmt)
            if pw:
                # (plain item, transformed item, catalyst): the result is the second.
                m = re.search(r"LOTRMod\.(" + pw.group(1) + r")()()", stmt[pw.start():])
            elif stmt.startswith("GameRegistry.addSmelting") and m:
                m = RESULT.search(stmt, m.end()) or m
            if not m:
                rows.append([method, "|".join(tabs), "", "", "manual", "no result: " + stmt[:90]])
                continue
            field, meta = m.group(1), (m.group(3) or "").strip()
            if meta and not meta.isdigit() and not (field == "banner" and meta.isidentifier()):
                meta_note = meta
                meta = ""
            else:
                meta_note = ""
            src_ns = "LOTRMod" if pw else stmt[m.start():m.end()].split("(")[1].split(".")[0]
            if src_ns == "LOTRBanner":
                rid, st = "lotr:" + field, "ported"
            elif src_ns == "LOTRMod":
                rid, st = resolve(field, meta, by_field, status)
            else:
                rid, st = vanilla_id(field, meta), "vanilla"
            # Ingredients after the result.
            blockers, lotr_ings, ing_ids = [], False, set()
            for im in INGREDIENT.finditer(stmt[m.end():]):
                iid, ist = resolve(im.group(1), im.group(2) or "", by_field, status)
                if ist != "vanilla":
                    lotr_ings = True
                if iid:
                    ing_ids.add(iid)
                if iid is None and ist != "vanilla":
                    blockers.append(im.group(1) + ("/" + im.group(2) if im.group(2) else ""))
            old = field + ("/" + meta if meta else "") + (" [" + meta_note + "]" if meta_note else "")
            for t in tabs:
                if method == "modifyStandardRecipes":
                    state, note = "excluded", "vanilla recipe rule"
                elif in_loop or meta_note:
                    state, note = "manual", "loop/computed meta"
                elif rid.startswith("minecraft:") if rid else False:
                    if (t, rid) in have:
                        used.add((t, rid))
                    state, note = ("present", "") if (t, rid) in have else (
                        ("excluded", "vanilla recipe rule") if not lotr_ings else ("missing", ""))
                elif st in ("excluded", "vanilla") or (rid is None and st.startswith("exclu")):
                    state, note = "excluded", st
                elif rid is None:
                    state, note = "blocked", "result not ported (" + st + ")"
                elif (t, rid) in have:
                    state, note = "present", ""
                    count = int(m.group(2)) if m.group(2) else 1
                    if stmt.startswith("GameRegistry.addSmelting"):
                        count = 1
                    cands = have[(t, rid)]
                    ing_ids.discard(rid)  # a transmute names its own result
                    ores = [o for o in ORE_NAME.findall(stmt[m.end():]) if o in ORES]
                    unknown = [o for o in ORE_NAME.findall(stmt[m.end():]) if o not in ORES]
                    ore_ok = lambda i: all(any(x in i for x in ORES[o]) for o in ores)
                    rows_m = re.match(r'(?:\s*,\s*"([A-Z# ]{1,3})")+', stmt[m.end():].split("new ItemStack(")[0].lstrip(")"))
                    old_rows = re.findall(r'"([A-Z# ]{1,3})"', rows_m.group(0)) if rows_m else []
                    # A letter with no key entry is an empty slot to ShapedOreRecipe
                    # (chestMallorn's "XYX" never defines Y).
                    keyed = set(re.findall(r"'(.)'", stmt))
                    old_rows = ["".join(c if c in keyed else " " for c in r) for r in old_rows]
                    shape = "pattern=" + canon(old_rows)
                    shape_ok = lambda i: not old_rows or shape in i
                    if unknown:
                        state, note = "differs", "unmapped ore " + ",".join(sorted(set(unknown)))
                    elif not any(c == count and ing_ids <= i and ore_ok(i) and shape_ok(i) for c, i, _ in cands):
                        detail = "; ".join("x%d %s" % (c, ",".join(sorted(i))) for c, i, _ in cands)
                        state, note = "differs", "old x%d %s %s %s | port %s" % (
                            count, ",".join(sorted(ing_ids)), ",".join(ores), shape, detail)
                    used.add((t, rid))
                elif blockers:
                    state, note = "blocked", "needs " + ",".join(sorted(set(blockers)))
                else:
                    state, note = "missing", ""
                rows.append([method, t, old, rid or "", state, note])
    with open(OUT, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["method", "table", "old_result", "port_result", "status", "notes"])
        w.writerows(rows)
    with open(OUT.replace(".csv", "_port_only.csv"), "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["table", "port_result", "files"])
        for (t, rid), cands in sorted(have.items()):
            if (t, rid) not in used:
                w.writerow([t, rid, " ".join(p for _, _, p in cands)])
    counts = {}
    for r in rows:
        counts[r[4]] = counts.get(r[4], 0) + 1
    print(len(rows), "rows:", counts, file=sys.stderr)


if __name__ == "__main__":
    main()
