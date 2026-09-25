#!/usr/bin/env python3
"""Asset parity ledger (PORT_PLAN.md unit A3).

Textures and sounds are copied into the port byte-for-byte under new names, so
they are matched by content hash rather than by file name.

Outputs (docs/parity/):
  assets_old.csv    - every old texture/sound: its port copy, or "missing"
  assets_orphans.csv- port textures/sounds that no JSON, Java or sounds.json refers to
  lang_missing.csv  - old en_US.lang values (outside item/tile names, which
                      items_blocks.csv covers) whose text appears nowhere in the port lang

Run from the repository root:  python3 tools/parity_assets.py
"""
import csv, hashlib, json, os, re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OLD_ASSETS = os.path.join(ROOT, "old mod/src/main/resources/assets/lotr")
NEW_ASSET_ROOTS = [os.path.join(ROOT, p, "assets") for p in ("src/main/resources", "src/main/generated")]
NEW_CODE = [os.path.join(ROOT, p) for p in ("src/main/java", "src/client/java")]
OLD_LANG = os.path.join(OLD_ASSETS, "lang/en_US.lang")
NEW_LANG = os.path.join(ROOT, "src/main/resources/assets/lotr/lang/en_us.json")
OUT = os.path.join(ROOT, "docs/parity")
EXTS = (".png", ".ogg")


def walk(root, exts):
    for d, _, files in os.walk(root):
        for f in files:
            if f.endswith(exts):
                yield os.path.join(d, f)


def md5(path):
    return hashlib.md5(open(path, "rb").read()).hexdigest()


def resource_id(path):
    """assets/<ns>/textures/item/foo.png -> ns:item/foo ; sounds -> ns:item/foo too."""
    m = re.search(r"assets/([^/]+)/(?:textures|sounds)/(.+)\.(png|ogg)$", path.replace(os.sep, "/"))
    return f"{m.group(1)}:{m.group(2)}" if m else ""


def main():
    new_by_hash = defaultdict(list)
    new_files = []
    for r in NEW_ASSET_ROOTS:
        for p in walk(r, EXTS):
            new_files.append(p)
            new_by_hash[md5(p)].append(os.path.relpath(p, ROOT))

    rows = []
    for p in sorted(walk(OLD_ASSETS, EXTS)):
        rel = os.path.relpath(p, OLD_ASSETS)
        hits = new_by_hash.get(md5(p), [])
        rows.append(["/".join(os.path.dirname(rel).split(os.sep)[:2]),
                     rel, " ".join(hits), "ported" if hits else "missing"])

    os.makedirs(OUT, exist_ok=True)
    with open(os.path.join(OUT, "assets_old.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["area", "old_path", "port_paths", "status"])
        w.writerows(rows)

    # Orphans: a port texture/sound is referenced if its resource id (with or
    # without namespace, e.g. "lotr:item/x" or "item/x") or its bare path
    # appears in any JSON under assets/data or in Java source.
    corpus = []
    for r in NEW_ASSET_ROOTS + [os.path.join(ROOT, p, "data") for p in ("src/main/resources", "src/main/generated")]:
        for p in walk(r, (".json", ".mcmeta")):
            corpus.append(open(p, encoding="utf-8", errors="replace").read())
    for r in NEW_CODE:
        for p in walk(r, (".java",)):
            corpus.append(open(p, encoding="utf-8", errors="replace").read())
    text = "\n".join(corpus)
    orphans = []
    for p in sorted(new_files):
        rid = resource_id(p)
        if not rid:
            continue
        ns, path = rid.split(":", 1)
        if path.startswith("block/ctm/"):
            continue  # connected-texture sprites; client/render/ctm builds these names at runtime
        stem = path.split("/")[-1]
        keys = [rid, '"' + path + '"', "/" + path + '"', ns + ":" + path.split("/", 1)[-1]]
        if not any(k in text for k in keys) and not re.search(r'["/]' + re.escape(stem) + r'["_.]', text):
            orphans.append([os.path.relpath(p, ROOT)])
    with open(os.path.join(OUT, "assets_orphans.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["port_path (no reference found; animated/CTM/programmatic use possible)"])
        w.writerows(orphans)

    # Lang: compare display text, since keys changed wholesale.
    new_vals = {re.sub(r"\s+", " ", v).strip().lower() for v in json.load(open(NEW_LANG, encoding="utf-8")).values()}
    lang_rows = []
    for line in open(OLD_LANG, encoding="utf-8", errors="replace"):
        line = line.rstrip("\n")
        if "=" not in line or line.startswith(("tile.lotr:", "item.lotr:", "#")):
            continue
        k, v = line.split("=", 1)
        if re.sub(r"\s+", " ", v).strip().lower() not in new_vals:
            lang_rows.append([k.split(".")[0] + "." + (k.split(".")[1] if "." in k else ""), k, v])
    with open(os.path.join(OUT, "lang_missing.csv"), "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["group", "old_key", "old_text"])
        w.writerows(lang_rows)

    tally = defaultdict(lambda: defaultdict(int))
    for area, _, _, st in rows:
        tally[area][st] += 1
    for a in sorted(tally):
        print(f"{a:40} " + "  ".join(f"{k} {v}" for k, v in sorted(tally[a].items())))
    print("orphans:", len(orphans))
    groups = defaultdict(int)
    for g, _, _ in lang_rows:
        groups[g] += 1
    print("lang lines with no matching port text:", len(lang_rows))
    for g, n in sorted(groups.items(), key=lambda x: -x[1])[:25]:
        print(f"  {g:40} {n}")


if __name__ == "__main__":
    main()
