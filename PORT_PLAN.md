# LOTR Port — Master Work Plan

This file is the single source of truth for the multi-session effort to audit,
fix, clean up and finish the Fabric 26.2 port of the 1.7.10 LOTR mod. It is
written so any fresh chat can pick up one work unit without the history of the
others.

## Parity tooling

Re-run after any porting work to refresh the ledgers (fast, no build needed):

```
python3 tools/parity_items_blocks.py && python3 tools/parity_classes.py && python3 tools/parity_assets.py
```

`tools/` and `docs/` are committed with the project (they are no longer in
`.gitignore`), so the ledgers and scripts are available in any clone.

Record hand-confirmed mappings in `tools/parity_overrides.csv` (items/blocks)
and `tools/parity_class_overrides.csv` (classes) rather than editing the CSVs in
`docs/parity/`, which are regenerated.

## How to use this file across chats

1. Start a new chat with: **"Read PORT_PLAN.md and do work unit `<ID>`."**
2. The chat reads the "Standing rules" below, then only the files its unit names.
3. At the end of the unit the chat:
   - ticks the unit's box and fills in its **Result** line (one or two sentences),
   - appends any new findings to the **Findings backlog** at the bottom (don't fix
     them in the same unit unless the unit says so),
   - does **not** commit — the user makes all commits themselves. Leave changes
     in the working tree and say what is ready to commit.
4. If a context window runs out mid-unit, the next chat reads the unit's
   **Result** / partial notes and continues. Units are sized to fit comfortably in
   one chat; split one into `a`/`b` here if it doesn't.

## Standing rules (apply to every unit)

- `old mod/src/main/java/lotr/` is the specification. Mirror its logic; a
  divergence is a bug by default — subject to the modern-vanilla rule below. Check the registration site in
  `old mod/.../common/LOTRMod.java` before claiming a block/item diverges.
- **Modern vanilla wins over inherited 1.7.10 values.** The port targets
  Minecraft 26.2, so values must respect the mod *and* sit naturally beside
  today's vanilla. Before "restoring" an old value, ask where it came from:
  - **The mod's own decision** (it chose a number or behaviour vanilla never
    had): port the original. Examples: Utumno brick's `Float.MAX_VALUE` blast
    resistance; wooden bars being wood rather than metal; the LOTR stone slabs'
    fixed hardness.
  - **Inherited from 1.7.10 vanilla** (the mod copied a vanilla block's
    numbers, extended a vanilla class, or got the value from a 1.7.10 vanilla
    mechanism) **and vanilla has changed it since**: use the current 26.2
    vanilla value. Example: a custom dirt whose hardness matched 1.7.10 dirt
    should now match 26.2 dirt, not 1.7.10 dirt. Likewise 1.7.10 `BlockWall`
    copied one block's strength for all sixteen wall kinds; modern walls copy
    their own base, so the port follows modern walls.
  - If the old value equals both 1.7.10 and 26.2 vanilla, there is nothing to
    decide. If it is unclear which case applies, note it in the backlog and let
    the user decide rather than guessing.
  Record in the unit's Result which values were kept modern under this rule, so
  they are not later "fixed" back.
- Fix exactly what a unit describes. Suspected knock-ons go to the Findings
  backlog, not into the diff. Improvements are *suggested*, not applied.
- Porting an item/block includes its systems: creative tab, tags, recipes, loot,
  modifiers (`LOTRModifiers`), dispenser behaviour, lang, model.
- Be exhaustive within a unit: compare every class and behaviour it covers,
  not a sample. A unit may take several sessions; split it into sub-units
  (B5a, B5b...) here, finish each fully, and tick the parent last.
- Faction crafting tables are built exactly like today's vanilla crafting
  table (wood, 2.5, axe, burnable, fuel), whatever Material the original used
  (user decision). Only the Morgul table's glow is kept from the original.
- Textures: never edit pixels; do copy/rename PNGs from `old mod/.../textures/`.
- Don't alter vanilla recipes; faction recipes stay on faction tables.
- Intentionally excluded (never list as missing): `lotr:Gravel`, armour stand,
  pouches, other vanilla duplicates.
- No ritual final `./gradlew build`; compile mid-change only to settle an API
  question. The user builds and tests in game.

- **Track what cannot be ported.** Anything an audit finds that cannot be
  ported yet goes on the *Deferred-port tracker* (below Track D), under the
  unit that unblocks it, and not only in the unit's *Result* or a code
  comment.

## Snapshot of where the port stands (2026-09-23)

| Area (old package) | Old classes | Port status |
|---|---|---|
| Blocks (`common/block`) | 311 | ~1434 block lang keys vs 1330 old (flattening) — **largely done** |
| Items (`common/item`) | 132 | ~858 item lang keys vs 914 old — **largely done**, gaps to audit |
| Tile entities | 38 | 21 ported (the elven/dwarven/orc/alloy forge variants appear to be merged into one `LOTRForgeBlockEntity`; confirm in B5); missing: bookshelf, flower pot, mob spawner, spawner chest, portals, gulduril, corrupt mallorn, ithildin sign |
| Recipes / crafting tables | 18 | Ported (faction shaped/shapeless/transmute, brewing, millstone, pipe dye, poison) |
| Enchant / modifiers | 22 | `LOTRModifiers` + mixin — ported, needs audit |
| Factions / alignment | 9 | 11 classes, alignment attachment, command — ported core |
| Dispenser behaviours | 14 | Only generic projectile behaviours registered; conker, mystery web, orc bomb, rhun fire jar, spear, termite, throwing axe, spawn egg not verified |
| Projectiles | 19 | 11 ported; missing spear, fish hook/fishing, smoke ring, thrown termite (the thrown rock and troll snowball since D9n-b, the marsh wraith ball since D9p, the mallorn leaf bomb since D9q) |
| Item entities (rugs, banners, barrel, portal, falling treasure, trader respawn) | 16 | Only stone troll & boss trophy |
| NPCs + AI (`entity/npc`, `entity/ai`) | 444 + 56 | **Not started** |
| Animals (`entity/animal`) | 37 | **Not started** |
| Dimension / biomes / genlayer / features / structures / villages / mapgen | ~620 | **Not started** (`LOTRDimension` is a deliberately reduced stub) |
| Spawning / invasions | 10 | `LOTRInvasions` only |
| Player data, quests, fellowships, titles, capes, shields, achievements, waypoints/fast travel, map | ~60 + GUIs | **Not started** (only alignment + alcohol tolerance persisted) |
| Networking | 137 | 5 payloads |
| GUIs | 85 | 10 screens + faction screen |
| Client fx / particles / sounds | 35 | Minimal |
| Commands | 25 | 1 (`alignment`) |
| Config | 2 + `LOTRConfig` | None |

Total port: ~46k lines of Java across 280 files.

---

## Track A — Parity ledger (do first; everything else keys off it)

- [x] **A1 Item/block ledger.** Write a script (keep it in `tools/`) that maps
  every `public static Block|Item` in old `LOTRMod.java` (plus metadata subtypes
  from each block's `getSubBlocks`/names array and the old `en_US.lang`) to the
  port's registry ID, producing `docs/parity/items_blocks.csv` with columns
  `old_field, old_meta, old_lang_name, new_id, status(ported|missing|excluded|merged), notes`.
  Match by English display name first (lang files on both sides) — name-based
  field matching is unreliable because the port uses faction-first names
  (`gondor_chestplate` vs `bodyGondor`). *Result:* `tools/parity_items_blocks.py`
  (+ hand rows in `tools/parity_overrides.csv`) → `docs/parity/items_blocks.csv`,
  `docs/parity/port_only.csv`. 2,324 old entries: **2,204 ported**, 39 merged
  (double slabs, bare berry bushes, powered rail), 6 excluded, **43 `vanilla?`**
  (vanilla doors/gates/trapdoors, copper, rabbit, iron horse armour, red
  sandstone set, stone-brick walls… — awaiting the user's confirmation that they
  count as vanilla duplicates), **32 missing**: stalactites/stalagmites ×6,
  fallen leaves ×4, spawner chests ×3, elven/morgul/utumno/utumno-return portals,
  coral reef, goran, marsh lights, rhûn fire, bookshelf storage, utumno entrance
  brick, carved black Gondor brick; items: 4 rugs, branding iron, dyed feather,
  NPC respawner, structure spawner. 96 port-only IDs (lang sub-keys, wall
  banners, extra blocks) — skim for renames the matcher missed.
- [x] **A2 Systems ledger.** Same idea for non-item systems: one row per old
  class under `old mod/src/main/java/lotr/` → port class or `missing`/`excluded`.
  Output `docs/parity/classes.csv`. *Result:* `tools/parity_classes.py`
  (overrides: `tools/parity_class_overrides.csv`). Statuses:
  ported (name match), absorbed (all backed items ported), referenced (old
  class named in a port comment — confirm in the B unit), base (abstract),
  missing. Of 2,502 old classes (211 ported, 280 absorbed, 106 referenced, 99 abstract, 1,793 missing), blocks are ~95% ported/absorbed; items ~85%;
  enchant 14/22 referenced (the remaining 8 — MeleeReach, ProtectionFall/Fire/
  Mithril/Ranged, RangedKnockback, WeaponSpecial, Combining — are probably
  folded into `LOTRModifiers`; verify in B11); recipes 7/18 (dye/feather/
  turban/party-hat/armour-dye/banner/treasure-pile/vessel recipe classes not
  found — check `LOTRTranscribedRecipes` in B6); dispensers 6/14; tile
  entities 23/38 (alloy/elven/orc forge variants unmatched — confirm merge in
  B5); commands 1/25; network 6/137; everything under world/, npc, ai, animal,
  quest, fellowship, fx, sound is missing. The name heuristic has false positives
  (e.g. `LOTRModelBanner` → `LOTRBannerModel` is right, but check "ported" rows
  with several candidates).
- [x] **A3 Asset ledger.** List old textures/sounds/lang keys with no counterpart
  in `src/main/resources`, and port assets that nothing references (orphans).
  *Result:* `tools/parity_assets.py` (hash-matched, since copies are
  byte-identical) → `docs/parity/assets_old.csv`, `assets_orphans.csv`,
  `lang_missing.csv`. Item textures: 1,129 ported / 93 missing; block textures:
  1,574 / 102. The missing ones are mostly expected (pouches, armour stand,
  vanilla duplicates, `_fast` leaves which modern MC doesn't need, bed item icons,
  rugs), but see backlog for the ones that point at missing features. All mob,
  cape, shield, sky, weather, map, gui/npc/quest textures and all mob/ambient
  sounds are unported (they belong to Track D). 11 port files are unreferenced
  (CTM sprites excluded since they are named at runtime). 3,723 old lang lines
  have no text match; they cluster in achievements, waypoints, entity names,
  structures, GUI, titles, biomes, shields, commands — again Track D.

## Track B — Audit & bug-fix of ported areas

Each unit: read the old classes, read the port classes, list divergences in
the unit's Result, fix the confirmed ones, backlog the rest. Test notes should
say what the user should check in game.

- [x] **B1 Registration & bootstrap.** `LOTRMod`, `LOTRModClient`, init order,
  `MOD_ID` (contains spaces — only used for the logger? confirm), fabric.mod.json
  metadata (empty authors/description), access widener, mixin config. *Result:*
  No behaviour bugs found; nothing changed. Registration happens in static
  initialisers, so `init()` order only decides class-load order and is safe
  (`LOTRMillstoneRecipes.createRecipes()` loads `LOTRItems` before
  `LOTRItems.init()` is reached — harmless, though untidy). `MOD_ID` is only the
  logger name. Access widener entries (PrimedTnt owner, ThrownTrident loyalty/
  foil) are all used and documented. Compared against old `LOTRMod.load()`/
  `preload`/`onServerStarting`: fire info and harvest levels moved to B2
  (backlog), commands/speech/lore/achievements/titles/dimension belong to
  Track D, `onMissingMappings` is 1.7.10 world migration and does not apply.
  Suggestions for the user are in the backlog under [B1].
- [x] **B2 Blocks — building sets** (stone/brick/slab/stair/wall/pillar, CTM
  rendering in `client/render/ctm`, woods/leaves/saplings/beams/fences). Check
  hardness, resistance, tool tags, flammability, loot, map colour, sounds against
  old `LOTRMod.java` values. *Result:* New `tools/audit_block_props.py`
  replays 1.7.10's setHardness/setResistance/setStepSound arithmetic per block
  and compares it with `LOTRBlocks` → `docs/parity/block_props.csv`
  (891 match; doors, trapdoors, glass, panes, buttons, pressure plates and
  ladders the script can't parse were checked by hand and match). Fixed:
  (1) the 108 stone slabs of `slabSingle`…`slabSingle14` now use hardness 2 /
  resistance 6 / stone sound via `registerStoneSlab` (they copied their base
  block's 1.5); (2) wood bars are 2 / 3 / wood, axe, no tool needed, and reed
  bars 0.5 / grass / no tool (both were metal bars, 5 / 6, pickaxe);
  (3) Utumno bricks and pillars (and so their slabs/stairs/walls) have
  `Float.MAX_VALUE` blast resistance as in the original — the port's comment
  claiming the original never called setResistance was wrong; (4) fire spread: added wood bars, reed bars, double flowers,
  Mordor moss, daub, thatch/reed slabs and stairs, and removed it from reeds,
  corn, grapevine and riverweed, which the original never set on fire;
  (5) dirt/sand/gravel slabs cut from vanilla blocks are now shovel-mineable
  instead of pickaxe. All other harvest levels match `setHarvestLevel`. Map
  colours follow material as in 1.7.10. Tag changes are datagen output: re-run
  datagen. Kept modern under the modern-vanilla rule: walls copy their own
  base block (1.7.10 `BlockWall` copied one block for all sixteen kinds, which
  would have made the white sandstone wall 1.5 / 6); the audit script skips
  those walls. The stone slabs' 2 / 6 and thatch's 60/20 fire values also
  equal modern vanilla (stone slabs, hay). In game: break a Gondor brick slab (slightly slower), blow up Utumno
  brick with TNT (should survive), light thatch stairs, break wood bars by hand.
- [x] **B3 Blocks — plants & farming** (crops, corn, grapevine, berry bush,
  hanging fruit, reeds, riverweed, morgul shroom, farmland, sapling growth into
  LOTR trees — trees need Track D features, note stubs). *Result:* Fixed, all against the original classes:
  crops — the four vanilla-shaped loot tables (leek, lettuce, turnip, yam)
  used 1.7.10's 8/15 bonus chance, now 26.2's 4/7 (modern-vanilla rule);
  lettuce renders as a cross (`getRenderType` 1) instead of `#`; ripe pipeweed
  crop and wild pipeweed smoke again (`LOTRPlantBlock.pipeweedSmoke`).
  Corn — may also be planted on dirt/grass/sand beside water (Forge's Beach
  plant rule), and a stalk that grows a segment still gets its ear roll that
  tick. Grapevine — seeds are a new `LOTRGrapeSeedsItem` that only plants onto
  a bare post with farmland at most three blocks below (they placed a vine
  anywhere); ripening uses `getGrowthFactor` (farmland 3x3, 1 in 80/growth+1,
  was a flat 1/12); harvest drops are `getVineDrops` exactly; a player
  breaking a vine leaves the post (`removedByPlayer`); a post may stand on a
  vine; the post is hardness 2 / wood / solid / axe (was an instabreak
  walk-through plant); hoes till dirt, grass, path and mud under a grapevine
  (`LOTRBlockGrapevine.hoeing`), and mud now needs air or a vine above like
  vanilla soil (it tilled under anything). Berry bush — fast growth only on
  farmland in light 9+, otherwise light/2000 on any soil (grass used the fast
  branch). Hanging fruit blast resistance 0.6 (1.7.10's `setResistance(1)`).
  Mud farmland turns to mud, not dirt, when a block is set on it (scheduled
  tick path). Waste block (from B2) — new `LOTRWasteBlock`: soul-sand collision
  and 0.4 slow (modern soul sand, which 1.7.10's behaviour matched),
  infiniburn on top, and eight random per-face texture models from the
  original's `_var0..7` sprites (copied, not edited). Matches already:
  morgul shroom spreading, riverweed, crop seeds, berry-bush harvest/loot,
  hanging fruit support. Kept modern: grape ripening uses today's crop light
  test (`getRawBrightness(pos, 0)`), not 1.7.10's block-above test. Re-run
  datagen (models, tags). In game: plant grape seeds on a post over farmland,
  hoe under a post, bone-meal lettuce, stand on waste block, light fire on it.
- [x] **B4 Blocks — functional** (gates, dwarven/ithildin doors, dart trap,
  orc bomb, quagmire, rope, chains, mechanised rail, beacon, table of command,
  utumno return portal, khamul's fire, treasure piles, troll totem). *Result:* Fixed:
  (1) Blast resistance converted from 1.7.10's `setResistance` (x3/5) where
  the port had copied the raw number: gates (wood 3, stone/metal/dwarven door
  6), barrel 3, beacon 3, chandeliers 1.2, kebab stand 0.6, weapon rack 0.6,
  forges/unsmeltery/dart trap 4, troll totem 12 (it also had NO hardness at
  all and broke instantly — now 5, pickaxe). Quagmire hardness 1 (comment
  claimed the original never set one). Ungoliant web hardness 2, orc chain
  metal sound, mud dirt path 0.5/gravel — the mod's own values. Termite mound
  stone sound. (2) [Superseded in B5 by the user: all faction tables now
  match the vanilla crafting table.] (3) Block-entity drops: in
  26.2 `affectNeighborsAfterRemoval` runs AFTER the block entity is removed
  (checked in LevelChunk bytecode), so the kebab stand and weapon rack lost
  their contents when broken; their drops now happen in
  `preRemoveSideEffects`. The forge/oven/unsmeltery/dart trap drop code there
  was dead (Container block entities drop by themselves) and was removed with
  its wrong comments. (4) Rope retracts with anything but rope in hand, into the
  player's inventory. (5) Khamûl's fire: original tick rate (2 + 0..9, the
  comment claimed 30) and it now burns LOTR stone and gates as the original's
  Material.rock/clay test did. (6) Treasure piles fall instead of breaking when
  undermined, and can be placed over air. (7) Table of command is pickaxe-
  mineable (it required a tool but had none, so never dropped). (8) Particles:
  new `LOTRParticles` (morgul portal/water, white and quendite smoke) with
  client classes in `client/particle`, and `LOTRBlockParticles` + a
  `Block.animateTick` mixin hook for plain blocks: Mordor rock smoke, hearth
  smoke over fire, Morgul table flames, Dol Guldur table and corrupt mallorn
  sparks, Morgul flower, quendite grass. (9) Corn restored to 3 high (user),
  bone meal only grows the second segment as in the original. Checked and
  matching: gate flood fill/redstone/culling, dwarven and ithildin doors (break
  handling lives in the block entity), orc bomb, orc chain (climbable),
  mechanised rail, beacon, table of command zoom, Utumno return blocks.
  `tools/audit_block_props.py` now also reads literal `.strength()`/`.sound()`
  in registrations and helper bodies; its remaining 21 rows are known and
  expected: crop/vine/riverweed sounds kept modern (rule), wood crafting tables
  and grapevines (parser can't follow a ternary / else-branch). Re-run datagen.
  In game: break a kebab stand with meat and a weapon rack with a sword, light Khamûl's fire on Gondor brick, drop a
  treasure pile off a ledge, retract a hithlain rope holding a sword.
- [x] **B5 Block entities & menus** (barrel/brewing, forge, unsmeltery, hobbit
  oven, millstone, dale cracker, chest, mug, plate, kebab stand, weapon rack,
  animal/ent jar). Check tick logic, fuel/recipe tables, NBT/component
  persistence across save/reload, hopper interaction, comparator output,
  slot shift-click rules. *Result:* Split:
  - [x] **B5a Forges and unsmeltery.** *Result:* Tick loop, alloy pairs, special
    smelts and material filter already matched. Fixed: both are now
    `BaseContainerBlockEntity` (an anvil-named forge/unsmeltery keeps its name;
    it was never set or saved) and `WorldlyContainer` with
    `getAccessibleSlotsFromSide`'s rules (fuel from the sides; inputs from the
    top, emptiest lane first; outputs and fuel below, fuel only giving up an
    empty bucket) — hoppers could previously fill the alloy slots and pull
    inputs out. Hoppers may only insert what `canMachineInsertInput` allowed
    (things with a smelting result — not alloy-only ingots) and fuel. GUI slots
    are plain `Slot`s as in the original (anything by hand); shift-click routes
    as `transferStackInSlot` did. Forges pay smelting XP again (vanilla's
    result slot only pays `AbstractFurnaceBlockEntity`): furnace-recipe XP, or
    `addSmeltingXPForItem`'s 0.7/0.8/1.0 for the alloys and mithril, stored per
    forge and paid when outputs are taken. Forges puff `useLargeSmoke`'s six
    corner clouds. Unsmeltery counts materials recursively through sub-recipes
    (iron blocks = 9 ingots each), refuses burnable block items, output slot is
    take-only with no XP (`LOTRSlotUnsmeltResult`). Screens and the rocking
    renderer match. Kept: the crackle sound is today's vanilla furnace effect,
    which the original's copied 1.7.10 furnace effect becomes under the
    modern-vanilla rule; forge lava sparks are a port addition, unsmeltery keeps the largest recipe count rather than the
    first found (modern recipe order is not the original's).
  - [x] **B5b Barrel, brewing, mugs/vessels.** *Result:* No bugs found; nothing
    changed. Checked against `LOTRTileEntityBarrel`, `LOTRBlockBarrel`,
    `LOTRItemBarrel`, `LOTRContainerBarrel`, `LOTRSlotBarrel(Result)`,
    `LOTRGuiBarrel`, `LOTRPacketBrewingButton`, `LOTRBrewingRecipes` (all 29
    recipes, and the `bone`/`apple` ore groups exactly), `LOTRTileEntityMug`,
    `LOTRBlockMug` and its six vessel subclasses (sizes and sounds),
    `LOTRRenderMug` (every meniscus/liquid number). Modes, 12000-tick strength
    steps, stop-early strength, start consuming one of each (buckets back),
    tap filling and emptying, poisoning, creative-break flag, item keeping its
    brew, hopper sides (the original's quirk of accepting ingredients while
    brewing is kept), GUI fill/bubbles/button, mug take/fill/drink/poison, rain
    filling, drops. Port additions noted: the button packet also checks
    `stillValid`. Needs other systems: floating barrel entity placed on water
    (D4), `brewDrinkInBarrel` achievement (D7).
  - [x] **B5c Hobbit oven, millstone, kebab stand.** *Result:* Hobbit oven —
    cooking logic matched; fixed the same gaps as the forge: now
    `BaseContainerBlockEntity` (name kept) + `WorldlyContainer` (hopper
    sides; hoppers insert only food-cookable inputs and fuel, as
    `isItemValidForSlot`), plain GUI slots, original shift-click routing,
    cooking XP paid from its `SlotFurnace` outputs, pickaxe + tool needed
    (Material.rock). Millstone — logic, 25 cracked-brick pairs and hopper
    sides matched; input slot made plain with original shift-click, pickaxe +
    tool needed (Material.rock). Barrel (B5b) given the axe tag
    (Material.wood). Kebab stand — cooking/fuel-from-below/spin matched;
    fixed: it drops as one item holding its meat (`KEBAB_DATA`, tooltip
    "N meat"/"Cooked", placing restores it with the fire out,
    pick-block keeps it, creative break drops nothing) instead of spilling the
    meat; it no longer needs a block beneath it (the original never checked);
    holding meat at a full spit does nothing instead of taking a piece.
    Screens matched. Needs other systems: `cookKebab` and
    `smeltObsidianShard` achievements (D7).
  - [x] **B5d Dale cracker, chest, plate, weapon rack, animal jar, ent jar.**
    *Result:* Chests (vanilla `ChestBlock` base, so lid/hopper/comparator
    are modern vanilla) — added tool tags from each chest's Material: stone and
    Ancient Haradric need a pickaxe, lebethron casket and mallorn box are axe,
    reed basket none. Plate — matched. Weapon rack — matched; clicking a
    block's underside places a floor rack again (the original's
    `onBlockPlaced`); its tighter selection boxes are a documented port choice.
    Ent jar — matched; shovel tag (Material.clay); the "bucket needs a full
    jar" rule is a documented fix of the original's water duplication.
    Animal jar — the held jar/cage releases its creature two blocks ahead on
    right-click in the air (`onItemRightClick`; the port had no release on the
    item), and a placed jar no longer lets it out on click (the original had no
    such interaction); bird cages hang anywhere (`canBlockStay` true), the
    butterfly jar still needs a floor; catch/release sound is the original's
    "random.pop" (was the chicken egg sound). Dale cracker — matched (closing
    returns unsealed contents to the inventory, the modern convention, where
    the original dropped them). Needs other systems (D8): jar occupant
    bobbing/turning/sounds and the Lórien butterfly's light 7; catching needs
    the LOTR birds/butterflies. `catchButterfly` achievement (D7).
  - [x] **B5e Dart trap, beacon, table of command, troll totem, banner, carved
    sign, dwarven door block entities.** *Result:* Dart trap — debug code and
    a doubled launch sound (1002 played twice) removed. Beacon, table of
    command, banner — matched (the banner BE stores nothing; owner and
    protection data arrive with banner territory, D). Troll totem — jaw
    animation, sync and summon geometry match; the summon raises the
    Hill-troll Chieftain (D9q). Stale
    `docs/TODO-*.md` references (troll totem, table of command) repointed to
    Track D. Carved sign — lines, 15-char cap, one-time edit by the carver,
    NBT keys match. Open (not changed): 26.2's renderer `getViewDistance()`
    takes no block entity, so the ithildin sign draws to 40 blocks like the
    plain one (original 32). Dwarven door — matched, including dissolve on
    break (`preRemoveSideEffects`). Two knowing divergences: the original's
    `getGlowBrightness` read the base BE at (doorPosX, baseY, baseZ) — a typo
    for doorBaseX — the port reads the base; and `replacementGlowTicks` (keeps
    the glow when 1.7.10 swapped the tile entity on open/close) is not needed:
    `LevelChunk.setBlockState` removes the block entity only when the block
    itself changes (checked in the 26.2 bytecode), and the gate toggles
    `OPEN` on the same block, so the door keeps its block entity and glow.
- [x] **B6 Crafting tables & recipes.** Every faction table opens, filters to
  the right recipes; `LOTRTranscribedRecipes` (1731 lines) vs old
  `lotr/common/recipe/LOTRRecipes.java`; recipe book categories. *Result:* Split:
  - [x] **B6a Tables, menu, recipe types.** All 27 tables' factions match the
    `LOTRBlock*Table` subclasses (Dol Amroth→Gondor, Gulf/Umbar→Near Harad,
    Rivendell→High Elf, Moredain→Morwaith, Rhûn→Rhudel). Per-table recipe
    types reproduce the per-table lists (`addRecipeTo` groups = `Groups`);
    no vanilla recipes at faction tables, no faction recipes at the vanilla
    table — as the original. Fixed: the alignment failure used an invented
    `container.lotr.crafting.alignmentTooLow` message (key removed) — now
    `notifyAlignmentNotHighEnough` (`chat.lotr.insufficientAlignment`), plus
    the original's eight smoke puffs on the table top (sent from the server,
    which holds the alignment). Fixed: faction recipes reported vanilla
    `CRAFTING_*` book categories, so once unlocked they showed in the
    inventory and vanilla table recipe books where they cannot be crafted;
    they now use a registered `lotr:faction_crafting` category no tab shows
    (the original had no recipe book). They still unlock, which
    `limitedCrafting` needs. Menu, shift-click, 8-block reach mirror vanilla
    `CraftingMenu`; the faction screen has no recipe book, as in 1.7.10.
    Pouch recipe at every table not ported (pouches excluded). Open: unlock
    toasts still fire for faction recipes (`showNotification`); duplicate
    helpers `LOTRAlignmentMessages` vs `LOTRAlignmentValues` (Track C).
  - [x] **B6b Standard recipes** and **B6c Faction recipe lists** — done
    together by the new `tools/parity_recipes.py` → `docs/parity/recipes.csv`
    (+ `recipes_port_only.csv`). It parses every recipe statement in
    `LOTRRecipes.java` (loops expanded, `continue` skips honoured, `addRecipeTo`
    groups fanned out, banner types and noted metas resolved) and matches on
    (table, result), then on count, ingredients (LOTRMod ids + ore-dictionary
    names → port tags) and pattern shape. Result: 2,084 present and matching;
    0 missing; 6 blocked on unported results (Carved Black Gondor Brick ×2,
    Branding Iron ×3, LOTR diamond horse armour); 60 excluded by the vanilla
    recipe rule / vanilla duplicates. The 34 "manual" rows are B6d's special
    classes and helpers, plus charcoal (vanilla `logs_that_burn` holds all 39
    LOTR logs, 0.15 XP as the original), fallen leaves (not ported) and
    poisoned arrows/bolts (checked: all nine orc + Near Harad tables, same
    shape). Smelting XP checked against `createSmeltingRecipes`. Fixed: three
    ore-dictionary recipes for vanilla results were never transcribed —
    arrows from `#lotr:arrow_tips` (flint or any horn) + `#lotr:sticks` +
    `#lotr:feathers`, and mossy cobblestone / mossy stone bricks from willow
    or Mirkwood vines (LOTR vines only, as the saddle recipes do). Stale
    "NOT here" header in `LOTRTranscribedRecipes` rewritten. Re-run datagen.
    **Open, for the user:**
    (1) DONE (user: follow the old mod). Ported Carved Black Gondor Brick
    (`brick4/6` → `carved_black_gondor_brick`, 1.5 / 6, texture copied from
    `brick4_blackGondorCarved.png`); the Gondorian and Dol Amroth tables now
    make it from Númenórean brick, and `carved_black_umbar_brick` is Umbar's
    only, as in the original. Ledger override updated.
    (2) DONE (user: a separate item). `lotr:diamond_horse_armor`
    (`horseArmorDiamond`): 14 protection (the original's chest + legs of
    diamond, 8 + 6, above vanilla's 11), repaired with diamonds, Combat tab
    before the Gondor barding as registered; the recipe now makes it. ART: the
    original borrowed 1.7.10 vanilla's diamond barding art through
    `setTemplateItem`, and no copy of that art is in the repo or on this
    machine, so `models/item/diamond_horse_armor.json` and
    `equipment/diamond_horse.json` point at vanilla's current files until the
    old PNGs are supplied.
    (3) The original's copper-core compass and clock (iron/gold around
    copper) are excluded — copper is vanilla now, so they are vanilla-only
    recreations; say if they should count as LOTR additions.
    (4) [Retracted] Cracked-brick smelting is NOT a port addition: the
    original's `LOTRMillstoneRecipes.addCrackedBricks` registers every
    millstone cracking as a furnace recipe too (0.1 XP). Kept.
    (5) Clean-up (Track C): 37 recipes exist twice with identical content,
    once from datagen and once hand-written (mostly smelting — the cracked
    bricks, ores, dried reeds — plus `mud_slab` and the three shish kebabs);
    the hand-written `scorched_stone_from_blue_rock` (all six rocks) also
    overlaps the six generated per-rock ones. The recipe book lists each twice.
  - [x] **B6d Special recipe classes.** Pipe dye (`LOTRSmokingPipeDyeRecipe`)
    — fixed: it copied the whole pipe; the original builds a fresh pipe that
    keeps only its wear. Poison drinks — matched. Poison weapons — present as
    faction transmutes (B6b). Treasure piles — every combination the two item
    forms (1-layer carpet, 8-layer block) can express is present (8 carpets →
    block, block alone → 4 ingots, 2×2 ingots → 8 carpets); the intermediate
    heights are the documented item-metadata divergence. Vessels — all 13
    drinks × 12 vessels, bases keep their vessel. Ent jar — B5. Armour dyes —
    only ever covered cloth armour, i.e. vanilla leather; modern vanilla's.
    Fixed, dyes: none of the eight LOTR dyeables could be dyed (26.2 dyeing
    needs a `crafting_dye` recipe per item; `#minecraft:dyeable` alone does
    nothing). Added: leather hat and party hat at any table
    (`misc/*_dyed.json`); Harad turban/robe/leggings/shoes at the Near Harad,
    Umbar and Gulf tables and the kaftan pieces at Rhûn (new
    `LOTRFactionDyeRecipe`, `lotr:faction_crafting_dye`, wrapping vanilla
    `DyeRecipe` as the original's `LOTRRecipeHaradRobesDye(material)` was
    faction-locked). Dye ingredient `#c:dyes` so LOTR dyes work, as
    `LOTRItemDye.isItemDye` allowed. Removed the default white `DYED_COLOR`
    on all eight items: vanilla's blend includes an existing colour, so the
    first dye would have come out pastel (the original skipped undyed items);
    the equipment layers already render undyed as white via
    `color_when_undyed`. Added the eight to `#minecraft:cauldron_can_remove_dye`
    (the original's cauldron washing in `LOTREventHandler`).
    Blocked: feather dye and hat-feather recipes (`featherDyed` not ported;
    a feathered hat also could not be re-dyed); turban gold ornament (no
    ornament state on the port's turban); banner protection copy/clear
    (`LOTRRecipesBanners`, needs banner protection — D). Pouch recipes
    excluded. Open for B7/B10: the original's cauldron also washed pipe smoke
    colour and turned poisoned weapons back into plain ones; dyed-robe item
    icons are not tinted (B9).
- [x] **B7 Weapons & tools** (`LOTRToolMaterials` 1457 lines, axes, spears,
  daggers, poisoned variants, hammers/knockback, reach, throwing axes, tridents,
  balrog whip, sauron mace, gandalf staff). *Result:* Split:
  - [x] **B7a Tool materials** vs `LOTRMaterial`. All 55 materials with a tool
    half match on uses, speed, damage, enchantability and harvest level
    (renamed: ROHAN→ROHIRRIC, UMBAR→UMBARIC, HIGH_ELVEN→LINDON,
    NEAR_HARAD→COAST_SOUTHRON, MOREDAIN*→MORWAITH*, MALLORN_MACE→
    CHARRED_MALLORN, TAUREDAIN→TAURETHRIM, DORWINION_ELF→DORWINION_ELVEN);
    the nine unmatched (Dorwinion, Dunlending, Galvorn, Harnedor, Lamedon,
    Pinnath Gelin, Ranger Ithilien, Rhûn gold, Rohan marshal) are armour-only.
    Fixed: `setCraftingItems(tool, armour)` gave tools the first item only,
    but six port tags were shared by tool and armour material and held both —
    mithril tools took mithril mail, Ranger/Corsair/half-troll/Morwaith/
    Taurethrim tools their armour item. Split into `repairs_*_tools` (tool
    item) and new `repairs_*_armor` (armour item); Ranger's tool material now
    uses a new `repairs_ranger_tools` (iron). `setManFlesh` is only read by
    `LOTREntityMan`'s drops — D.
  - [x] **B7b Weapon registration.** All 200 original melee weapons
    (`LOTRItemSword` and subclasses) checked by script against class,
    material and damage: all match (damage args give the original's
    `lotrWeaponDamage` as the 26.2 total, the file's stated convention).
    Tools (axe/pickaxe/shovel/hoe) follow the same convention consistently.
    Not ported: poisoned Dorwinion elven and Gundabad Uruk daggers.
  - [x] **B7c Class speed/reach/knockback** vs `LOTRWeaponStats` (factors over
    the sword's vanilla 1.6/s and 3 blocks, as the port's polearms already
    did). Fixed: battleaxe 1.0 → 1.2 (all 20). User decision: dagger keeps 2.0/s
    and 2.5 blocks, hammer 0.85/s (port's own figures, not the factors). Rewrote the comments that claimed 1.7.10 had no
    melee speed or knockback (it had `LOTRWeaponStats`). Polearm, long
    polearm, pike, lance and knockback all matched.
    **User decision: keep both as they are.** The spear uses 26.2's vanilla spear component
    (charge/lunge, stab period 0.95 s → 1.05/s) rather than the original's
    1.333/s and 4.5-block reach; the trident is vanilla's throwable trident
    (Loyalty/Riptide/Channeling, 1.1/s, 3-block reach) where the original's
    was a polearm (1.067/s, 4.5 reach) that could not be thrown and fished
    instead (`LOTRFishing`).
  - [x] **B7d Special weapons.** Balrog whip — the lash (16-block sweep, 1
    damage + 5 s fire to all it clips, fire trail from 4 blocks out that stops
    at the first gap), 7 damage, 1000 uses, Flame of Udûn repair, 4.5 reach:
    matched. Fixed: it was enchantable at Utumno's 12 (`sword()`); the
    original's `getItemEnchantability` was 0, now no enchantable component.
    Sauron's mace (8 damage, 1500 uses, 40-tick wind-up, 12×8 slam, 2
    durability) and both Gandalf staves (grey 4 / 1000, white 8 / 1500 and
    the fireball at 1.5 / 1.0) — matched. Command sword — 1 damage,
    unenchantable, undamageable; the command itself needs hired NPCs (D).
    Orc skull staff — repaired with heads only, as `getIsRepairable`.
    Chisels — matched (carvable = pickaxe/axe-mineable solid blocks for the
    rock/wood/iron materials). Mattocks — pickaxe + axe tag; plants and vines
    are instabreak or already axe-mineable. Poisoned daggers — poison
    `(1 + 2·difficulty) + rand` seconds, matched. Blocked on D: elven-blade
    glow near orcs (`setIsElvenBlade`), NPC-faction exemption on Sauron's
    mace. Blocked on B11: fire modifier / projectile modifiers on thrown
    axes, the whip's `checkIncompatibleModifiers` (fire, chill).
    Deliberate port choices, documented in code, left as they are: throwing
    axe velocity 2.0 (original 3.0, damage scaled to match), trident-throw
    sound (original `random.bow`), faster tumble. 1.7.10 sword blocking is
    gone from modern vanilla.
- [x] **B8 Ranged** (bows, crossbows + bolts, blowgun + darts, sling, pebbles,
  fire pots, poisoned arrows, dispenser behaviours for all of these). *Result:*
  Bows — all 17 match on draw time, speed factor and material (launch
  `charge*2*1.5` = vanilla's `power*3`). User decision: bows (and
  the blowgun) draw like vanilla — the original's 65% MIN_BOW_DRAW_AMOUNT is
  not reproduced. Fixed: the Ranger bow's repair
  tag pointed at the (now leather-only) Ranger armour tag after B7a's split,
  now iron + string as `RANGER`'s tool half. Crossbows — bolts only, material
  speed factor, bolt base 4 (twice an arrow), 1200-tick ground life: matched;
  vanilla draw and sounds are the user's recorded choice. Poisoned arrows,
  bolts, darts — standard poison, matched. Blowgun — 5-tick draw, dart speed
  ×1 damage, reeds, unenchantable: matched. Sling (250, leather, slung pebble
  2 damage) and pebbles (1.0 speed, 0.04 gravity, water skipping): matched;
  the snowball throw sound in place of `random.bow` is a documented choice
  (the sling copies the pebble's). Fire pot (3 direct / 1 splash in 3, 2–4
  +2–4 s fire, detonates a Khamûl's fire jar): matched. Fixed: its smash
  sound was the decorated pot's; now the plate break sound
  (`soundTypePlate`), as the original. `hitBirdFirePot` achievement — D.
  Dispensers: bolts, pebbles, darts, poisoned arrows/bolts, fire pots and
  plates were there. Added: conker, mystery web and exploding termite
  (`LOTRThrownMiscItem` is now a ProjectileItem, 1.1 / 6.0 as
  BehaviorProjectileDispense), the five throwing axes (loosed pick-up-able,
  1.1 / 6.0), the six orc bombs (a lit bomb in front, normal fuse — the
  original's `fuse += damage*10` wrote vanilla TNT's unused field) and
  Khamûl's fire jar (placed in front without detonating, via a restored
  `explodeOnPlace` switch; otherwise dropped) — new
  `LOTRDispenserBehaviours`. Not applicable: spear (vanilla spear, the user's
  choice, has no thrown entity), spawn eggs (no entities yet).
  Also fixed (B7 scope, found here): the Morgul blade's wither — onLivingHurt
  gave anything hit by a MORGUL-material weapon 8 s of wither; new
  `LOTRMorgulBladeItem`. For B9: a full Morgul armour set wore down the
  attacker's weapon to 90% of its remaining durability per hit (same
  handler) — not ported.
- [x] **B9 Armour** (materials, equipment models in `assets/lotr/equipment`,
  `LOTRArmorRenderers`, horse/warg/elk/boar/rhino armour — mounts don't exist
  yet, record which are inert). *Result:* Materials — every armour material
  checked by script against `LOTRMaterial` (durability `round(uses*0.06)`,
  `round(base*protection*25)` per piece, enchantability): all match, variants
  included (winged/champion/berserker/warlord helmets, cloak, jackets,
  chieftain = MOREDAIN_LION_ARMOR); Ancient Harad, Barrow, Bladorthin and
  Moredain bronze had no armour. Pieces — all 235 original `LOTRItemArmor`
  pieces ported with the right material and slot. Horse armour — all 13 give
  chest + legs as `damageReduceAmount` did; warg (3), elk, boar (2) and rhino
  armour are inert items until those mounts exist (D). Equipment assets — all
  88 exist with every texture they name. Fixed: Wood-elven Scout armour also
  took elven steel (tool half merged in); now leather only. Fixed (user): bone
  armour is repaired as in the original — not at a vanilla anvil (BONE had no
  crafting item), only at the LOTR anvil with bones. Fixed, full-set effects
  (none were ported; new `LOTRArmourSets` + a `LivingEntity.tick` inject):
  full galvorn turns arrows, bolts and darts (a player's armour takes
  max(1, dmg/4) wear instead); full Morgul rots a melee attacker's weapon to
  90% of its remaining durability; full Wood-elven Scout +30% movement speed
  (operation 2), re-checked every 10 ticks. Special models: the original's 36
  `LOTRArmorModels` entries are now all covered. Added: Black Uruk helmet
  (`LOTRBlackUrukHelmetModel`), the Dol Amroth swan chestplate with its two
  twelve-feather wings animated off age and walk (`LOTRSwanChestplateModel`;
  the original drove them off the mount's stride when riding — no mounts
  yet), and the leather hat, party hat and Harad turban tinted by their dye
  (`LOTRDyedHeadModel`; the turban's gold ornament waits on an ornament state,
  the hat's feather on `featherDyed`). The Harad robe, leggings and shoes need
  no model of their own — `LOTRModelHaradRobes` was the standard armour biped
  (plus an NPC-only chest), which the dyeable equipment layer already draws.
  Plates worn on the head were already handled by `LOTRPlateHeadRenderer`
  (the plate geometry on the crown, following head yaw only); the held-food
  pile on it is not reproduced.
  Fixed: dyed-item icons were untinted — `minecraft:dye` tints on the eight
  dyeables (leather hat undyed 0x684A36, the rest white); the leather hat's
  equipment `color_when_undyed` was white, now the original's brown. These
  are visual and want an in-game check.
- [x] **B10 Food, drink & vessels** (`LOTRVessel`, mugs/goblets/horns, drink
  strength/alcohol tolerance/drunk camera, poison drinks, placeable foods,
  plates). *Result:* Food — all 58 foods (`LOTRItemFood`, berries, stews, seed
  foods, hanging fruit, kebab, man flesh) match on nutrition and saturation;
  maggoty bread and yam's 40% hunger, wildberries' 3–6 s poison, stews one to
  a stack returning the bowl, the kebab's one-in-a-hundred line, and the
  wolf-food tag (every `canWolfEat` food) all match. Drinks — all 46 full
  `LOTRItemMug`s checked by script (food/brewable/alcoholicity, drink stats,
  effects and durations, damage, cures): all match; `onEaten` (strength and
  food-strength tables, alcohol chance with tolerance ×0.99^n, nausea length
  and tolerance gain, appetite rule), the Morgul draught, Tauredain cure,
  Athelas brew and termite tequila specials, and the drunk camera match.
  Poisoned drinks (killing poison 15 s, 1 damage every `5 >> level` ticks,
  poisoner-only visibility, water bottles count) and the bottle of poison
  match. Vessels — the twelve, their empty items, placeability and blocks,
  and dipping an empty vessel in a water source: match (extraPrice → trade,
  D). Placeable foods — sizes, 2/0.1 (marchpane 3/0.3), six bites, drop only
  uneaten, needs a solid top: match; the vanilla cake replacement is dropped
  (vanilla rule). Thrown plates (1.5, 0.02, 1 damage, shatters glass 5×5×5):
  match (banner protection — D). Achievements (drinkSkull, getDrunk, …) — D.
  Fixed, smoking pipe: it started a draw with no pipeweed (the original only
  began one with pipeweed or in creative); there was no puff sound (added
  `lotr:item.puff`, `puff.ogg` copied); and the smoke was a particle
  stand-in — ported `LOTREntitySmokeRing` as `LOTRSmokeRingEntity` (0.1 speed,
  weightless, 300-tick life, out on impact or in water) with
  `LOTRSmokeRingRenderer`: a camera-facing puff from the mod's
  `particles.png` (copied) tinted by the dye and growing as it fades, or for
  magic smoke the untextured little ship (`LOTRSmokeShipModel`).
  Fixed, cauldron washing (the rest of LOTREventHandler's, new
  `LOTRCauldronWashing`, `Dispatcher.put` access-widened): a dyed pipe's
  smoke washes back to plain, and poisoned daggers and the moon chisel
  (LOTRRecipePoisonWeapon.poisonedToInput) wash back to the plain item, each
  using a level of water and keeping the stack's wear and enchantments.
- [x] **B11 Modifier system** (`LOTRModifiers`, `LOTRModifiableItem`,
  enchantment/projectile mixins, smith's scroll, anvil behaviour) vs
  `lotr/common/enchant`. *Result:* Split:
  - [x] **B11a Modifier table.** Every rollable `LOTREnchantment` (strong,
    weak, durable, melee speed/slow, reach/unreach, knockback, tool speed/slow,
    silk, looting, protect/weak, protect fire/fall/ranged, ranged
    strong/weak/knockback) matches on value, weight, skilful flag and item
    kinds (`LOTREnchantmentType` per class). Spider- and wightbane present.
    Missing, all weight 0 (never rolled): elf/orc/dwarf/warg/troll/wraith
    banes (need those entities — D); `protectMithril`, `fire`, `chill`,
    `headhunting`, which were only ever put on at the LOTR anvil (below).
  - [x] **B11b Effects and rolling.** `applyRandom` matches
    `applyRandomEnchantments` (0/1/2 draws at 35/55/10%, skilful 20/65/15%,
    `getSkilfulWeight`, tools' ÷3 weighting, incompatibility pruning,
    wightbane on the barrow blades — the only BARROW swords — and spiderbane
    on Sting). Every calc (damage, speed, reach, knockback, tool speed,
    looting, silk, bane, protection, fire/fall/ranged protection, max fire
    protection, durability negation, ranged damage/knockback) matches its
    `calc*` and is wired (attributes, or the enchantment-helper, projectile,
    player, block and living-entity mixins). Rolling on player inventories and
    mob equipment as `onEntityUpdate` did. Ancient items' wraithbane — D.
  - [x] **B11c LOTR anvil** (user: a replica anvil block with the custom
    screen, vanilla's anvil untouched). New `lotr:anvil` (`LOTRAnvilBlock`:
    vanilla's anvil block — falls, faces, vanilla model and sounds — never
    wears; recipe: a vanilla anvil converts to it and back, a port choice),
    `LOTRAnvilMenu` + `LOTRAnvilScreen` (the original's `gui/anvil.png`,
    copied) and `LOTRAnvilRenamePayload`; reforge and engrave go through
    `clickMenuButton`. Transcribed from `LOTRContainerAnvil`'s block path:
    material costs (not XP) with the item's `LOTRRepairCost`; repair by
    quarters; combining two of a kind (+12%); smith's scrolls and the four
    special items (Flame of Udûn → Infernal, which also burns the poison off a
    dagger; Chill of Daedelos → Chilling; Headhunter's Trophy →
    Headhunting; Book of True-silver → True) with the three-modifier limit
    that banes and specials bypass; value-modifier costs; vanilla
    enchantments combined on the enchantingVanilla path with 26.2's anvil
    costs and compatibility (the original shipped with vanilla enchanting off
    and stripped them); reforge (full repair, skilled re-roll keeping banes,
    2 s apart, the slot flash); engrave ownership (`LOTRItemOwnership`, three
    previous owners, the tooltip lines); renaming, coloured by a gem (durnor,
    topaz, amethyst, sapphire, ruby, amber, diamond, opal, emerald) or
    washed with flint; string items' costs ×3; the extra repair materials
    (string, iron, paper, planks for Morwaith wood, mallorn planks, a
    mallorn log for the charred mace, bones for bone armour — as the original:
    bone armour mends only here, never at a vanilla anvil).
    Waiting on D: the smith NPCs' trader anvil (coins, scroll combining via
    `LOTREnchantmentCombining`, the scrap trader's mischief), achievements.
  - [x] **B11d Special modifiers, earned banes, fixes.** Added to the table:
    True (`protectMithril`, mithril armour only, +4 protection vs melee from a
    weapon of base reach ≥1.3), Infernal, Chilling, Headhunting (not on the
    Balrog whip for the first two; Headhunting goes with another special but
    not with a bane), each modifier's `getValueModifier`, reforge
    persistence and anvil-limit bypass. Effects (`LOTRModifierSpecials`):
    melee Infernal sets targets burning 8 s (Fire Aspect II) with the
    INFERNAL flame spray; launchers and throwing axes hand their specials to
    the projectile (entity tags) and Infernal ones loose burning shots;
    Chilling gives Slowness II 5 s with a new `lotr:chill` particle
    (`LOTREntityChillFX`) — the FROST screen overlay is not ported;
    Headhunting makes a slain player drop their head. Earned banes
    (`onKillEntity`): 100–250 kills of spiders/undead with a weapon that can
    take it earns Spiderbane/Wightbane, announced to the server. Fixed:
    re-applying modifiers built on the previous application (a reforge or
    earned bane would have doubled the damage bonus and read "Keen Keen …");
    they now rebuild from the item's defaults. The smith's scroll tooltip
    now reads "Keen: +0.5 melee damage" as the original's.
- [x] **B12 Factions & alignment** (`common/fac`, attachment persistence and
  sync on login/respawn/dimension change, alignment bar, faction screen,
  `LOTRAlignmentCommand`, control zones). *Result:*
  - [x] **B12a Faction definitions.** `LOTRFaction` enum line, all 91 control
    zones (waypoint coordinates checked), ranks, relations defaults,
    `LOTRControlZone`, `LOTRMapRegion`: match apart from API renames.
    `isAprilFools` date check implemented. Fixed in `LOTRAlignmentValues`:
    the pledge penalty used a made-up key (`pledgeBroken`) and forced the
    sign. It now takes the value as passed, with key `breakPledge`. Added
    `createMiniquestBonus`, `notifyMiniQuestsNeeded`, `formatConqForDisplay`,
    `parseDisplayedAlign`, and the 12 missing `lotr.alignment.*` and
    `chat.lotr.requireMiniQuest` lang keys. Deferred to Track D: dimension
    check (`isFactionDimension` returns true), NPC-influence control-zone
    check, rank achievements and titles (flags only), and the `Bonuses` NPC
    kill values. Relation overrides are not yet saved or synced (B12b).
  - [x] **B12b Alignment data, persistence, sync.** Checked against
    `LOTRPlayerData`'s alignment and pledge code. Fixes:
    - `setAlignment` now uses the original's `isPlayableAlignmentFaction`
      check, and auto-breaks a pledge the new alignment no longer allows.
    - The alignment attachment syncs to all tracking players, as
      `sendAlignmentToAllPlayersInWorld` did (it was target-only).
    - Pledging was client-only: the faction screen set the pledge locally
      and the server never saw it. Added `LOTRPledgeSetPayload`
      (`LOTRPacketPledgeSet`: pledge only if `canPledgeTo` and
      `canMakeNewPledge`; null unpledges).
    - Ported the pledge lifecycle into a new persistent `LOTRPledgeCooldowns`
      attachment, saved under the original's keys:
      - `revokePledgeFaction`: 30–180 min break cooldown, alignment drops two
        ranks or to half the pledge level, unpledge sound, chat message.
      - `setPledgeBreakCooldown`, with `LOTRBrokenPledgePayload` sent on the
        original's schedule and a client store.
      - `onPledgeKill`: a day's kill cooldown.
      - `handlePledgeCooldowns` and `runAlignmentDraining` (every 1000 ticks,
        −5 per mortal-enemy pair). Drain is always on; the original's config
        defaulted on.
    - Pledge sound on pledging; `event/pledge` and `event/unpledge` oggs copied
      over, plus 5 lang keys.
    - The pledge button now also respects the break cooldown and refreshes
      every tick.
    Deferred:
    - B12c: the drain HUD notice (`LOTRPacketAlignDrain`), the alignment
      bonus popup (`sendAlignmentBonusPacket`, and with it the position
      arguments to `addAlignment`), relation-override save/load and sync
      (nothing can set an override until `/factionrelations` exists), and
      the fem-rank option.
    - Track D: conquest bonuses and `forcedBonusFactions`, alignment
      achievements, `onPledgeKill` callers (NPC kills).
  - [x] **B12c** Alignment command, `/facRelations`, alignment bar,
    bonus popup, drain notice, faction screen unpledge.
    - [x] **Commands.** `/alignment` rewritten to the original:
      `<set|add> <faction|all> <amount> [player]`, op level 2, set bounded
      ±10000, add refused if any faction would pass the bounds, lang messages
      broadcast to ops. Removed the port-only `get` and `list` subcommands.
      Added `/facRelations set|reset` and `/alignmentZones enable|disable`,
      plus 11 `commands.lotr.*` lang keys (`%d` became `%s`, since modern
      translation has no `%d`).
    - [x] **World state.** New `LOTRLevelData` SavedData (`lotr:level_data`)
      saves `AlignmentZones` and the relation `Overrides` (the original's
      `faction_relations.dat` keys). Clients get both on join and on every
      change (`LOTRAlignmentZonesPayload`, `LOTRFactionRelationsPayload`).
      `controlZonesEnabled` now reads the flag instead of returning true.
      `LOTRClientPledgeState` renamed to `LOTRClientFactionState` and now
      holds all three client-side copies.
    - [x] **Unpledge.** The faction screen's pledge button reads "Break
      Pledge" on the pledged faction. A first press shows the original's
      `unpledgeDesc1/2` warning; a second sends `LOTRPledgeSetPayload(null)`.
      Lang corrected to the original: "Pledge Service", "Pledged!".
    - **Deferred (user: the HUD comes in a later release, after entities and
      structures):** the HUD alignment bar and `LOTRAlignmentTicker`, the
      drain notice, the floating gain/loss popup (`LOTREntityAlignmentBonus`),
      and the rest of `LOTRGuiFactions`.
  - [x] **B12d Faction data, bounties.** Nothing portable yet, so deferred
    whole to Track D. Every writer of `LOTRFactionData` (NPC kills, enemy
    kills, trades, hires, mini-quests, conquest, conquest horn) and of
    `LOTRFactionBounties` (`recordNewKill` on an NPC death) needs NPCs, and
    the readers are the bounty mini-quests and the faction GUI. Port both
    with the NPC death handler. Save keys to keep: per faction `NPCKill`,
    `EnemyKill`, `Trades`, `Hired`, `MiniQuests`, `Conquest`, `ConquestHorn`;
    bounties per faction, per player UUID, with kill records.
- [x] **B13 Entities ported so far** (projectiles, boss trophy, stone troll,
  exploding termite, mystery web, plate entity) + their renderers. *Result:*
  - [x] **B13a Stone troll, boss trophy, plate.** Stone troll: the pickaxe
    test listed the six vanilla pickaxes, but the original took any
    `ItemPickaxe`, so LOTR pickaxes chipped cobblestone instead of prising
    the statue out; it now uses `#minecraft:pickaxes`. Both it and the
    trophy lacked `func_145771_j` (pushed out of blocks), now
    `moveTowardsClosestSpace`. The trophy's default facing was north; the
    original defaults to 0 (south). Everything else matched: health, damage
    rules, drops, particles, sounds, physics, despawn, save keys, and the
    plate's glass-shattering, spin and water skipping. Still unported:
    banner protection and the statue achievement (Track D).
  - [x] **B13b Orc bomb, termite, mystery web, conker, fireball.**
    - **Mystery web** did something the original never did: it placed a
      cobweb. The original's `onImpact` returns on hitting the thrower. One
      time in four it tries a small Mirkwood spider (Track D). Otherwise it
      pops out one pick of `MIRKWOOD_LOOT`, or one time in 500 a stack of
      64 melon slices, with a 10-tick pickup delay, plus the pop sound.
      Ported, with a new `LOTRChestContents` holding the pool (all 40
      entries, `mithrilBook` = `BOOK_OF_TRUE_SILVER`) and `fillInventory`'s
      single pick:
      - Pouch roll: its smith's-scroll branch is kept, via the new
        `LOTRModifiers.randomTemplate` (`getRandomCommonTemplate`).
      - Damage up to three quarters, stack cap, and random modifiers,
        skilful 1 in 5.
      - Lore books are not ported.
    - **Gandalf's fireball:** the explosion effect was 40 soul-fire flames.
      The original's is a single firework-overlay flash tinted (0.33, 1, 1),
      now vanilla's `FLASH` with that colour. Damage (10 direct, 10 − 0.5 ×
      distance within 6), High-Elf vulnerability, 200-tick life and sound
      all matched.
    - **Orc bomb** matched: fuse 40 + 20 × strength, blast (strength + 1) × 4,
      fire, its own physics, fuse-from-explosion. Every non-NPC source set
      `droppedByPlayer`, so all port bombs break terrain, as they should.
    - **Exploding termite** (strength-2 explosion) and **conker** (1 damage,
      drops the chestnut, which is the port's `conker`) matched. The smoke
      ring was ported and checked in B9.
  - [x] **B13c Bolt, dart, poisoned arrow, pebble, fire pot, throwing axe,
    trident.** Entity classes diffed against the originals and
    `LOTREntityProjectileBase`. One fix: the **dart** lacked
    `getKnockbackFactor` 0.5; the motion change its hit causes is now
    halved. The blowgun has no vanilla counterpart, so this is the mod's own
    mechanic. Everything else matched, or follows a recorded decision:
    - Bolt: 2 × 2 damage factor, poison; draw and sounds vanilla (user).
    - Poisoned arrow: standard poison.
    - Pebble: 1 thrown / 2 slung, 0.04 gravity, water skip, drops itself.
    - Fire pot: 3 direct / 1 splash in 3, 2–4 + 2–4 s fire, fire-jar
      detonation, plate sound, shards and flames.
    - Throwing axe: spin period 5, vanilla gravity, 2.0 speed (user); 6000 /
      1200 ground ticks, pickup wear, knockback modifiers.
    - Trident: vanilla's.
    Track D: the fire pot's bird achievement.
  - [x] **Tracker sweep (with B13).** Every deferral from B3 onwards, and every
    "NOT ported" code comment, is now on the Deferred-port tracker. The sweep
    found:
    - **Anvil name colour:** Edhelvir (dark aqua) and Gulduril (dark green)
      were missing from `LOTRAnvilMenu.nameColour`; both extend
      `LOTRItemWithAnvilNameColor`. Added (a B11 gap).
    - **Ancient items** (`LOTRItemAncientItem`) were plain items waiting on
      chest pools. Ported as `LOTRAncientItem` with the six `ANCIENT_*` pools
      in `LOTRChestContents` (orc/Mordor 100, the four elven 25 each, Gondor,
      Arnor and Dwarven 50 each). Wraithbane roll and achievement are
      tracked.
    - **Stale comments** removed or rewritten, their gaps now closed:
      `LOTRSmithsScrollItem`, `LOTRThrowingAxeItem`, `LOTRModifiers`,
      `LOTRBalrogWhipItem`, the Edhelvir and Gulduril item classes, and
      `LOTRItems` (special items, gem colours, whip repair, smoking pipe,
      ancient items).
  - [x] **B13d Renderers.** Each checked against its `LOTRRender*`.
    - **Gandalf's fireball** was a 2× thrown item sprite. The original is a
      camera-facing, full-bright 1×1 sprite cycling through cells 24–27 of
      `particles.png`, a frame every 5 ticks. Now `LOTRGandalfFireballRenderer`.
    - **Crossbow bolt:** the poisoned bolt read
      `crossbow_bolt_poisoned.png`, which is not in the old mod; it is a
      generated crop of the original sheet's lower row. Now both bolts read
      the original `crossbow_bolt.png`, the poisoned one through a copy of
      `ArrowModel` with texture offsets 10 px down (`yOffset * 10`). The
      generated PNG was deleted (user).
    - **Gandalf's fireball item removed** (user): `gandalf_fireball` had no
      original counterpart and existed only to give the projectile a sprite.
      The entity is now a plain `ThrowableProjectile` (caster, eye height
      −0.1). The item, its model, item definition, lang key and its generated,
      animated texture are gone.
    - **Stone troll and boss trophy** had shadows (0.8 / 0.5). The originals
      left `Render.shadowSize` at 0. Removed.
    - **Matched:**
      - Orc bomb: vanilla `TntRenderer`, the same swell and flash formula,
        drawn from the bomb's block state.
      - Plate, poisoned arrow (vanilla arrow, own texture), smoke ring.
      - The `RenderSnowball` ones: pebble, web, termite, conker, fire pot.
      - Trophy geometry.
    - **Reported, not changed** (hand-tuned earlier; the user decides):
      - Throwing axe: the original stands a stuck axe upright (−90° roll,
        +0.75) and draws a flying one 0.5 higher; the port lays a stuck axe
        along its flight line.
      - Dart: now 0.6 blocks as the original (user), `SCALE` 1.2 on the GROUND
        model.
- [x] **B14 Networking & client/server split.** All payloads validate input
  server-side (sign edit, beacon edit, horn mode, brewing button); no client
  classes referenced from `src/main`. *Result:* No `net.minecraft.client` or
  Fabric client API in `src/main`; clientbound receivers live in the client
  source set (`LOTRClientFactionState`, the sign editor). Each serverbound
  handler compared with its original packet:
  - **Beacon edit** checked distance only. The original accepts an edit only
    from a player on the beacon's `editingPlayers` list, added when they
    open the dialog (`openGui 50` → `addEditingPlayer`) and released by the
    edit. Now: `LOTRBeaconBlock.useWithoutItem` registers the player
    server-side (`useItemOn` falls through with `TRY_WITH_EMPTY_HAND`; the
    client's `UseBlockCallback` still sends the use packet), and the handler
    requires and releases it. The list is not saved and alive-checked, as
    the original.
  - **Anvil rename:** the original handler ignored names over 30 characters
    (the box takes 40) and cleared on blank. Now the same.
  - **Horn:** the port set the mode on a horn in either hand, whatever its
    mode. Both original packets (`LOTRPacketHornSelect`,
    `LOTRPacketItemSquadron`) act on the main-hand item only, and the mode is
    only chosen for a plain horn (damage 0 = `Mode.SELECT`). Now so, and the
    selection screen opens for the main hand only.
  - **Matched:** sign edit (distance, carver, 15 allowed chars or "!?"),
    brewing button (open barrel menu), pledge set (B12b).
- [x] **B15 Creative tabs, lang, tags.** Every registered item in exactly the
  right tab in old order; no missing/duplicate lang keys; tag files match usage.
  *Result:*
  - **Removed the "LOTR (All, temporary)" tab** (user: debugging scaffolding):
    its key, registration, lang key and the header comment that called it
    scaffolding. Nothing depended on it.
  - **Membership, checked at runtime:** a throwaway datagen provider bound
    item components and built every LOTR tab (now removed, datagen output
    unchanged).
    - Every item is in exactly one tab, except 10 in none, all correct:
      crops, mechanised rail and Utumno portal pieces (`setCreativeTab(null)`
      in the original), and the bone block (vanilla duplicate).
    - **Right tab:** compared against each original item's tab (explicit
      `setCreativeTab` on its `LOTRMod` line, else its class or superclass).
      2,120 match. The rest are variant stacks, blocks whose separate item
      carries the tab (beds, cakes), gate factories, and placed-only blocks,
      each confirmed by hand.
    - Mud farmland is in Blocks although 1.7.10 farmland had no tab. That was
      inherited from vanilla, and modern vanilla lists farmland, so it stays.
  - **Lang:** no duplicate keys. Every item has a name (the troll statue by
    outfit, `.0`–`.2`, as the original). The 82 keys without an item are all
    item-less blocks (wall torches and banners, placed mugs, signs, fire) or
    shared tooltip keys.
  - **Tags:** every `lotr:` id in 183 tag files exists, and every code
    `TagKey` has a file except `lotr:is_fangorn` (biome, empty until D10). The
    11 cube/gate/door blocks with no blockstate file are modelled by the
    connected-border plugin, so that is correct.
  - **Parity ledger fix:** `redClayBall` was name-matched to the `red_clay`
    block; `tools/parity_overrides.csv` now maps it to `red_clay_ball`.
  - Found out of scope, logged in the findings backlog: 703 mechanical
    recipes still generate under the mod-id namespace, 49 possibly
    duplicating `lotr` ones [B15→B6].

## Track C — Code clean-up (behaviour-neutral only)

Run these after the relevant B unit so a refactor never hides a bug fix.

- [x] **C1 Imports.** 179 files use fully qualified `net.blueskiez77...` names
  inline (e.g. `LOTRMod.onInitialize`). Replace with imports. Pure mechanical.
  *Result:* By script: every inline `net.blueskiez77...Class` outside string
  literals and `//` comments became an import, same-package references need
  none, and no simple name clashed. 17 files had any left (80 references).
  Also moved `LOTRMillstoneRecipes.createRecipes()` after `LOTRItems.init()`,
  the B1 note (it worked either way, as the item fields are static). Compiles.
- [x] **C2 Split the giant registries.** `LOTRBlocks` (3218 lines), `LOTRItems`
  (2837), `LOTRCreativeTabs` (1314), `LOTRToolMaterials` (1457) → split by theme
  (e.g. `LOTRBuildingBlocks`, `LOTRWoodBlocks`, `LOTRFactionItems`), keeping IDs
  and registration order identical. *Suggest-first — confirm the split scheme
  with the user before doing it.* *Result (user: by creative tab):*
  - **Items:** 812 fields assigned by the tab each item appears in.
    `LOTRCombatItems` (496), `LOTRFoodItems` (125), `LOTRMaterialItems` (68),
    `LOTRToolItems` (63), `LOTRMiscItems` (47), `LOTRStoryItems` (7).
    `LOTRItems` keeps the six early items registered first on purpose
    (mithril, pipeweed, kebab, troll statue, the two trophies), `register`,
    and the shared builders and constants (now package-private).
  - **Blocks:** 1,429 fields. `LOTRBuildingBlocks` (830),
    `LOTRDecorationBlocks` (343), `LOTRUtilityBlocks` (212), `LOTRFoodBlocks`
    (26), `LOTRMiscBlocks` (12), `LOTRCombatBlocks` (7). Item-less blocks go
    with the block before them, e.g. wall banners with banners. `LOTRBlocks`
    keeps the family lists, maps, sound types and builders. The four mid-file
    `static` blocks and `ALL_CARVED_SIGNS` moved with the blocks they read;
    the file-final pass (leaves↔saplings, `NOT_SMALL_FLOWERS`) now runs in
    `LOTRBlocks.init()` after the six classes load.
  - Each part is loaded by its parent's `init()`, in first-appearance order;
    comments travelled with their fields. All references across the code
    were rewritten (83 files).
  - **Checks:** compiles; no init cycle (every cross-class read in a field
    initialiser points at `LOTRBuildingBlocks`, which loads first, and
    `LOTRBlocks` reads no moved block at class-init); datagen output
    identical except the entry order inside `mineable/pickaxe.json`; every
    creative tab identical in contents and order (runtime audit, before and
    after).
  - Not split: `LOTRToolMaterials` (materials don't sort by tab) and
    `LOTRCreativeTabs` (it is the tab definitions).
- [x] **C3 Datagen coverage.** Move hand-written JSON (models, loot, tags,
  recipes) into the datagen providers where they already exist, or document why
  a file is hand-written. Delete generated duplicates. *Result:*
  - **Duplicates:** no path exists in both `src/main/resources` and
    `src/main/generated`, and every model is referenced.
  - **Moved into datagen, each verified equal to the file it replaced, then
    deleted (and removed from git):**
    - 660 flat items: 1,320 files, each item's definition plus its flat
      model. New `LOTRFlatItemModels` lists them as vanilla's
      ItemModelGenerators does (492 flat, 168 handheld).
    - All 18 hand-written loot tables, into `LOTRBlockLootProvider`:
      plates and fruit blocks (single item), seven cakes and pies (self
      only at `bites=0`), flax and pipe-weed (vanilla crop shape), lettuce,
      leek, turnip and yam (the potato shape without the poisonous potato).
      The only differences are `random_sequence`, which no generated table
      carries, and the order of AND-ed conditions.
  - **Documented as hand-written:** `docs/hand_written_resources.md`, with the
    reason for each remaining group (custom blockstates and models, pull and
    in-hand item models, equipment layer data, hand-picked tags, special
    recipe types, dynamic registries).
  - **Recipes moved (user):** 790 of the 952 ordinary hand-written recipes
    are now generated by new `LOTRMovedRecipes`, called from
    `LOTRTranscribedRecipes`. Each keeps its id and its recipe-book category:
    the helpers take a one-shot `in(RecipeCategory)`, since their derived
    category would have put all 478 equipment recipes under misc. All 790
    are identical to the JSON they replaced; that JSON is deleted and removed
    from git. They gain recipe-book unlock advancements like every other
    generated recipe. Parity unchanged: 2,089 present, 0 missing. Still
    hand-written: 126 drinks (result components), 36 with ingredient
    alternatives, and 55 special types.
  - **Fixed (user report):** the lettuce had no item definition at all, in
    either tree or in git history, so its inventory icon never rendered. It
    is now a generated flat item. It was the only registered item without
    one (the `*_crop` block items are tabless).
- [x] **C4 Dead code & orphans.** Unused classes/methods/assets from A3.
  *Result:* `parity_assets.py` rerun: 11 orphans.
  - **Deleted (and removed from git):**
    - `block/marzipan_chocolate.png`: a stray copy of the original's unused
      blocks texture; the chocolate marzipan is an item with its own.
    - The five vanilla door item textures (acacia, birch, dark oak, jungle,
      spruce), which have no LOTR item.
    - `textures/block/textures.txt`: a saved colour `ls` listing committed
      by accident.
  - **Kept, false positives:** `entity/troll/outfit_0..2`, built as
    `"outfit_" + i`.
  - **Kept, a render gap (reported):** `reeds_lower` and `dried_reeds_lower`.
    The original drew the lowest stalk segment, the one on solid ground,
    with them; the redesigned reeds (B3) only have `top`/`mid` states.
  - **Code:** every Java class is referenced, and no private or package
    method is uncalled. The one class nothing uses, `LOTRKeyBindings`, is
    entirely commented out (since the 26.2 upgrade commit). It is the only
    way to open the factions screen, so it is kept and tracked (D16).
- [x] **C5 Comment hygiene.** Keep "old class ↔ new class" mapping comments,
  remove stale ones.

## Track D — Continue porting (baseline feature parity)

Ordered by dependency. Each bullet is one or more units; split as needed. *Result:*
  - **Stale "not ported" claims fixed, now true:**
    - Bow and crossbow: the ranged modifier family is ported (B11).
    - Fire pot: plays `BLOCK_PLATE_BREAK`; the sound claim is gone.
    - Coast Southron repair tag: the port has a bronze ingot.
    - Combat tab: the tools are left out because they sit in the tools
      tab, not because they are "not ported".
  - **Wrong names fixed:** `LOTREntityThrownTermite` (was
    `…ExplodingTermite`), `LOTRItemBanner` / `LOTREntityBanner` (was a
    nonexistent `LOTRBlockBanner`), and the three minecart mixins (was
    `LOTRMinecartMixin`, plus a doubled "powered").
  - The broken fragment heading `LOTRItems` is replaced by a class comment
    describing it after C2.
  - **Checked and left as correct:** the remaining "not ported" / "waits on
    NPCs" notes (all on the tracker), the NBT key names the original used
    (`LOTRRepairCost`, `LOTREnchantProgress`, `LOTRBarrelData`,
    `LOTRKebabData`, `LOTRCrossbowAmmo`…), and every "Track D" pointer. No
    TODO/FIXME remains.
- [x] **D1 Remaining items/blocks** from A1 `missing` rows, batched by faction
  (≈10–30 items per unit), with all their systems.
  - Remaining `missing` rows (31) after Tracks A–C: most need other systems
    and are listed under their D unit on the tracker:
    - portals and the Utumno entrance (D15);
    - spawner chests and bookshelf storage (D2);
    - rugs (D4);
    - branding iron, NPC respawner, spawn eggs (D9);
    - structure spawner (D11);
    - coral reef (D10).
  - [x] **D1a Stalactites.** `LOTRBlockStalactite` → `LOTRStalactiteBlock`,
    six blocks: `stalactite`, `stalagmite`, `ice_*` and `obsidian_*`.
    - Taken from the model block: hardness, resistance, sound, tool and
      texture; friction left at 0.6, as the original did not copy it.
    - Needs a sturdy face above or below, both to be placed and to stay;
      broken with its drop when that goes.
    - Landing on a stalagmite deals `2 × fall + 1` *instead of* fall damage.
    - A stalactite under pickaxe-mineable rock drips (Material.rock).
    - Shape: `renderStalactite`'s 16 tapering layers, as two hand-written
      template models with datagen children, also the item.
    - Drops: stone and obsidian drop themselves, packed ice only to silk
      touch. All pickaxe; obsidian needs diamond.
    - Decorations tab; lang; parity overrides. Cave generation → D10.
  - [x] **D1b Fallen leaves.** `LOTRBlockFallenLeaves` → `LOTRFallenLeavesBlock`
    and `LOTRFallenLeavesItem`, one block per leaf: `fallen_<leaf>` for
    vanilla's six and all 38 LOTR leaves, 44 blocks.
    - Material.vine behaviour: hardness 0.2, grass sound, no collision, 2 px
      tall, replaceable, lit by lava, broken by pistons.
    - Sits on a sturdy top or a water source, never inside a liquid; placed
      on water like a lily pad; gone, without drops, when unsupported.
    - Named "Fallen %s" after its leaf; shears-only drop.
    - Recipe: 3 leaves in a row make 6, LOTR leaves only (the original loop
      skipped vanilla's). Decorations tab.
    - Look: `LOTRFallenLeavesModel` transcribes `renderFallenLeaves` (fancy),
      with the original position seed: 6–10 pieces, 2–6 px, random crop,
      angle and place, 0.7 shade, no AO. Supplied by
      `LOTRFallenLeavesPlugin` in place of a blockstate. Vanilla's six take
      their leaf's tint (foliage, or the birch and evergreen constants), and
      their items are tinted the way vanilla tints the leaf item.
    - The fast-graphics flat slab is not ported, as 26.2 has no such toggle
      for block models. Generation under trees → D10.
  - [x] **D1c Dyed feather and the hat's feather.** Everything that used
    `LOTRItemFeatherDyed`:
    - **The item:** `feather_dyed`, stack 1, no tab (the original set none).
      Vanilla's feather sprite tinted by `DYED_COLOR`, white undyed.
    - **Feather dye** (`LOTRRecipeFeatherDye`): vanilla `crafting_dye`, whose
      blend is the same and which transmutes onto the result. Target
      `#lotr:dyeable_feathers` (new tag: the "feather" ore name plus a dyed
      feather).
    - **Hat feather** (`LOTRRecipeLeatherHatFeather`): new
      `lotr:leather_hat_feather`. One featherless hat and one feather, plain
      or dyed, nothing else; the colour goes in new `HAT_FEATHER`.
    - **Hat dye** (`LOTRRecipeLeatherHatDye`): now `lotr:leather_hat_dye`, a
      `DyeRecipe` that refuses a feathered hat, as the original did.
    - **Hat item:** `LOTRLeatherHatItem`, with the "Dyed" / "Feathered"
      tooltip. Its inventory icon adds the original's `leatherHat_feather`
      overlay (copied) when feathered, tinted by new item tint source
      `lotr:hat_feather`.
    - **Worn:** the feather is drawn on the hat with `LOTRModelLeatherHat`'s
      transforms, as the dyed-feather item in its colour.
    - **Cauldron** (LOTREventHandler): a hat washes its dye and its feather's
      (back to white) if either is dyed; a dyed feather via
      `#cauldron_can_remove_dye`.
    - **Dale cracker:** its second hat entry is the white-feathered hat again.
    - The feathered hats of Shirriff chiefs and Bree captains come with those
      NPCs (D9).
  - [x] **D1d Marsh lights, Goran.**
    - **Marsh lights** (`LOTRBlockMarshLights` → `LOTRMarshLightsBlock`,
      `marsh_lights`): invisible, no collision, no outline, no drop, and no
      item (the original's had no icon). Needs water beneath, gone without
      it. Emits the corpse-lights two ticks in three, half a block down: a
      marsh flame one time in three, a marsh light otherwise.
    - **Two new particles,** `marsh_flame` and `marsh_light`
      (`LOTRMarshParticle`). Both are `EntityFlameFX` underneath: vanilla
      FlameParticle's shrink, self-light and movement, 40–59 ticks. The
      light uses sheet cell 49, vanilla's `lava` sprite, tinted pale grey
      and moving as spawned. Both are vanilla sprites, so no new art.
    - **Goran** (`LOTRBlockGoran` → `LOTRGoranBlock`, `goran` "Goran" and
      `goran_rock` "Cargoran", textures copied): the developers' joke block,
      no tab, instabreak, stone sound, a pickaxe to keep it. Used by a
      server operator, it fills every loaded air block within 32 with water.
  - **D1 status:** 18 `missing` rows remain, every one on the deferred-port
    tracker under the system it needs.
- [x] **D2 Missing tile entities**: bookshelf storage, flower pot, spawner
  chest, mob spawner, gulduril, corrupt mallorn, ithildin carved sign.
  - Excluded (user): bookshelf storage (`LOTRBlockBookshelfStorage`,
    `LOTRTileEntityBookshelf`, container and GUI) -- unless rebuilt on modern
    bookshelves -- and the flower pot (`LOTRBlockFlowerPot`,
    `LOTRTileEntityFlowerPot`).
  - Deferred: corrupt mallorn (spawns Ents in Fangorn: D9/D10), mob
    spawner and spawner chests (NPCs: D9/D12), the portals (D15). The armour
    stand is a vanilla duplicate.
  - **Gulduril glow: not ported (user).** `LOTRTileEntityGulduril` /
    `LOTRRenderGuldurilGlow` drew a drifting glow around gulduril blocks and
    bricks; the user replaced it with an animated texture instead. (It was
    briefly ported in D2a and then removed.)
- [x] **D3 Missing projectiles & dispenser behaviours**: spear, thrown rock,
  thrown termite, troll snowball, smoke ring (pipe), mallorn leaf bomb, marsh
  wraith ball, fishing. *Result:* nothing portable on its own.
  - **Already covered:** the spear is vanilla's (user decision); "thrown
    termite" is `LOTREntityThrownTermite`, ported as the exploding termite
    (B13); the smoke ring (B9); dispensers (B8).
  - **Tracked with their throwers (D9):** ~~mallorn leaf bomb (mallorn Ent)~~ — D9q.
    (The thrown rock and troll snowball came with the mountain and snow
    trolls, D9n-b; the marsh-wraith ball with the marsh wraith, D9p.)
  - **Tracked for the dimension (D10):** `LOTREntityFishHook`, which the
    event handler swapped in for vanilla's hook inside Middle-earth.
- [ ] **D4 Decorative entities**: rugs (wargskin/bear/lion/giraffe), banners as
  entities + banner protection, barrel entity, falling treasure.
  - **Rugs → D8.** `LOTRModelBearRug`/`LionRug`/`WargskinRug`/`GiraffeRug`
    lay the animals' own models flat, and rugs only drop from those animals,
    so they come with the animal models there. The Decorations-tab note
    already says so.
  - [x] **D4a Floating barrel** (`LOTREntityBarrel` → `LOTRBarrelBoatEntity`).
    - The original was 1.7.10's EntityBoat with a barrel drawn in, so it is
      a 26.2 `Boat` now, keeping what is the mod's own:
      - one rider;
      - five-eighths of a boat's top speed (speedMultiplier 0.04–0.25
        against the boat's 0.07–0.40). 26.2's paddling is private, so while
        ridden the horizontal speed is scaled by 0.943 a tick, which in
        water caps it at that ratio;
      - it bumps only other barrels;
      - broken, it drops the barrel it was placed from, contents included.
    - The item's `use` sets it afloat on still water, square to the player
      (onItemRightClick).
    - `LOTRBarrelBoatRenderer`: the barrel item at 1.5×, half a block up,
      rocking when struck, no shadow.
    - Pick-block gives a plain barrel (26.2's boat `getPickResult` is final).
    - The two stale "not ported" comments are fixed.
  - **Banners as entities and banner protection → D14 (user):** wait for
    fellowships, whose members a banner could whitelist.
  - [x] **D4b Falling treasure** (`LOTREntityFallingTreasure` →
    `LOTRFallingTreasureEntity`, a FallingBlockEntity subclass, `blockState`
    access-widened).
    - The pile already fell through vanilla's falling block, which on
      landing in a part-full pile dropped a single item, losing the other
      layers.
    - Now, as the original: it pours into the pile it lands in up to a full
      block, sets any left over on top, and drops the rest a coin item per
      layer. Moving pistons are waited out; it is dropped after 100 ticks
      out of the world, or 600 in any case. Vanilla's falling-block renderer
      draws it.
- [x] **D5 Player data foundation**: port `LOTRPlayerData`/`LOTRLevelData` as
  Fabric attachments + SavedData (alignment already partly there — unify),
  with sync packets. Prerequisite for everything below.
  *Result (user): per-system attachments, not one unified object.* Each
  system gets its own Fabric attachment (persistent, synced where the client
  needs it, copyOnDeath) when it is ported, keeping the original's NBT keys;
  nothing is ported ahead of its users. Already in place:
  - `alignment_data`: `AlignmentMap`, `PledgeFac`, `HideAlignment`;
  - `pledge_cooldowns`: `PledgeKillCD`, `PledgeBreakCD`,
    `PledgeBreakCDStart`, `BrokenPledgeFac`;
  - `LOTRAlcoholTolerance`: `Alcohol`;
  - `LOTRLevelData`: `AlignmentZones` and the faction-relation overrides.

  Where each remaining `LOTRPlayerData` field goes:
  - **D7 titles, shields, achievements:** `playerTitle`, `femRankOverride`,
    `shield`, `achievements`, `takenAlignmentRewards`.
  - **D9 NPCs / hired units:** `factionDataMap` (`LOTRFactionData`),
    `hiredDeathMessages`, `uuidToMount`/`uuidToMountTime`, `bountiesPlaced`.
  - **D10 dimension:** `deathPoint`/`deathDim`, `lastBiome`, `teleportedME`,
    `structuresBanned`, `hasPre35Alignments`, `chosenUnwantedAlignments`.
  - **D12 invasions/sieges:** `siegeActiveTime`, `conquestKills`.
  - **D13 map, waypoints, fast travel:** `viewingFaction`,
    `prevRegionFactions`, `hideOnMap`, `adminHideMap`, `showWaypoints`,
    `showCustomWaypoints`, `showHiddenSharedWaypoints`, `lastWaypoint`,
    `ftSinceTick`, `targetFTWaypoint`, `ticksUntilFT`, `unlockedFTRegions`,
    custom and shared waypoints with their use counts, `cwpShared*`,
    `nextCwpID`.
  - **D14 quests, fellowships:** `miniQuests`, `miniQuestsCompleted`,
    `completedMiniquestCount`, `completedBountyQuests`,
    `trackingMiniQuestID`, `questData`, `fellowshipIDs`, invites,
    `chatBoundFellowshipID`, `friendlyFire`.
  - **D16 GUIs:** `sentMessageTypes`.
  - **Bookkeeping, not ported:** `playerUUID`, `needsSave`, `pdTick`,
    `lastOnlineTime` (bookkeeping the attachment API does itself).
  - **`LOTRLevelData`:** portal positions (D15); `gollumSpawned` (D9);
    waypoint cooldowns (D13); `conquestRate`, `difficulty` lock and the
    `clientside_thisServer_*` mirrors (D6 config).
- [ ] **D6 Config** (`LOTRConfig`, client options).
- [ ] **D7 Titles, capes, shields, achievements** (achievements → advancements
  or custom tracker; decide with user).
- [x] **D8 Animals & mounts** (`entity/animal`): horses, wargs, elk, boars,
  rhinos, camels, etc. — makes mount armour functional. Split into batches
  (wargs are NPCs and belong to D9):
  - [x] **D8a Shared base + deer.** `LOTRAnimalMF` (mates only with the
    other sex), client `LOTRRandomSkins` (skin picked from the UUID, still
    hashed by the original path so each animal keeps its skin),
    `LOTREntityLootProvider` (dropFewItems becomes loot tables), and
    `lotr:deer` with its model, renderer, sounds and skins. Spawn eggs
    (`LOTRSpawnEggItem`, `LOTRSpawnItems`, the spawning tab) came with it.
  - [x] **D8b Grazers:** dik-dik, aurochs, kine of Araw, gemsbok, white
    oryx, flamingo, each with sounds, skins, loot table and spawn egg. New
    shared AI: `LOTRAttackOnCollideGoal` (the creature half of
    LOTREntityAIAttackOnCollide; the NPC half waits for D9) and
    `LOTRAvoidWithChanceGoal`. Babies drawn as the original drew them (the
    aurochs calf keeps a full-size head). The aurochs and kine are
    AbstractCows, so modern vanilla's cow tasks and milking stand in for
    1.7.10's EntityCow. The rabbit is dropped (user: vanilla duplicate).
  - [x] **D8c Predators:** lion and lioness (`LOTRLionBaseEntity`,
    `LOTRLionChaseGoal`, the "ticket lion" easter egg with
    `LOTRLionOldModel`), bear (three colours; Wojtek), and the lion and bear
    rugs (`LOTRRugEntity`, `LOTRRugItem`, `LOTRRugRenderer`; five rug items
    in the Decorations tab). The rug chance is in code (`LOTRRugDrops`)
    because 1 in (30 - 5 x looting) is not a linear loot-table chance. The
    original's age-driven task swapping (with its never-updated prevIsChild)
    is expressed as goals gated on age and anger.
  - [x] **D8d Horse base and mounts:** `LOTRHorseEntity` (extends vanilla
    `Horse`: coats, saddle, barding, taming, inventory) with the original's
    own breeding (own kind, own breeding item, the parent-range stat formula
    and per-mount clamps), spawn-stat adjustments, hostile mounts (elk, boar,
    rhino), the water lift, and the fork's sprint charge (kept by the user;
    silent, as its sounds never existed). Mounts: horse, Shire pony, elk,
    camel (chest, carpets), zebra (milkable), giraffe (and its rug), wild
    boar, rhino (the original's charge). **User: every LOTR equine uses
    1.7.10's horse model and coats** (`LOTRLegacyHorseModel`, 1.7.10's
    horse sheets copied from the 1.7.10 jar), because the LOTR barding and
    the zebra are painted for it. Elk, boar and rhino barding are now body
    armour for their mount (`registerMountArmor`), drawn by
    `LOTRMountRenderer` from `textures/entity/mount_armor/`. Mount speeds are
    read from position change: a player's mount moves on the client now.
  - [x] **D8e Hostile creatures:** jungle and desert scorpions (three sizes,
    poison sting, milked for a bottle of poison), crocodile (hunts in the
    dark, swims at its prey, snaps), exploding termite. Scorpions and termite
    are in `minecraft:arthropod` (`LOTREntityTypeTagProvider`) for 1.7.10's
    ARTHROPOD attribute.
  - [x] **D8f Ambient:** butterfly (four kinds; the Lórien one glows and
    trails a mallorn torch's light), midges (swarms that breed and follow
    players), swan (violent or timid by UUID, hisses, pecks), and the
    thieving birds -- songbird, crow, magpie, Far Harad bird, with gorcrow,
    crebain and seagull (`LOTRValuableItems`, `LOTRScarecrows`). The bird
    cage and butterfly jar tags now hold the LOTR creatures instead of the
    vanilla stand-ins. The fish is dropped (user: vanilla duplicate).
  - Natural spawning (`LOTRAnimalSpawnConditions`, `LOTRAmbientSpawnChecks`)
    waits for the biomes (D10) and spawning (D12). Until then the animals
    are summoned with `/summon`.
- [x] **D9 NPC core**: `LOTREntityNPC` base, AI goals (`entity/ai`), hiring,
  trading, speech banks, NPC renderers/models. Then factions one per unit
  (Hobbits → Bree → Rohan → Gondor → Elves → Dwarves → Orcs/Mordor → Harad →
  Rhûn …).
  Split (444 NPC classes, ~5,000 lines of AI). GUI work -- the trade, hire
  and hired-unit screens -- stays with D16 (user: GUI later), so hiring and
  trading land as data and logic first:
  - [x] **D9a NPC base:** `LOTREntityNPC` (attributes incl. the NPC attack
    and accuracy attributes, attack modes, `LOTRInventoryNPCItems`, drops
    and coin drops, drunkenness, eating, kill alignment and bonuses, target
    selection by faction, speech on right-click with the speech banks and
    `LOTRNames`, gender/family basics), the shared AI goals it needs, and
    the humanoid renderer (`LOTRRenderBiped`, `LOTRModelBiped`, held items,
    armour, combinatorial skins). Proven on the first faction: Hobbits.
    Done: `LOTRNPCEntity`, `LOTRManEntity` (man-flesh drop), `LOTRHobbitEntity`
    (egg, lang, skins); `LOTRFamilyInfo`, `LOTRInventoryNPCItems`,
    `LOTRNPCAttributes`, `LOTRSpeech`/`LOTRNames`/`LOTRDrunkenSpeech` with the
    text banks, `LOTRFoods` (HOBBIT, HOBBIT_DRINK), the HOBBIT_HOLE_STUDY and
    LARDER pools; goals for burning panic, hurt-by and faction targeting,
    eat/drink/smoke, marry/mate/follow parent and spouse, avoid evil player,
    hobbit child following a good player; kill alignment and ally
    retaliation (`LOTRNPCKillEvents`); client speech over the head or in chat
    (`LOTRSpeechClient`); `LOTRBipedModel`, `LOTRHobbitModel`,
    `LOTRBipedRenderer`, `LOTRHobbitRenderer`. The Shire's other hobbits:
    bartender (with its trader outfit), bounder (dagger and sling,
    `LOTRRangedAttackGoal`), shirriff, orcharder, farmer, farmhand. What
    waits is on the tracker.
  - [x] **D9b Hiring and trading (logic):** `LOTRHiredNPCInfo`,
    `LOTRTraderNPCInfo`, unit/trade tables; screens with D16.
    Done: trades (`trade/`: `LOTRTradeable`, `LOTRTradeEntry`,
    `LOTRTradeEntries` with the Shire pools, `LOTRTraderNPCInfo`,
    `LOTRTradeSellResult`), the trader's respawner
    (`LOTRTraderRespawnEntity` and renderer), the coin in a trader's hand,
    `LOTRCoins`; hiring (`hire/`: `LOTRHiredNPCInfo`, `LOTRUnitTradeable`,
    `LOTRUnitTradeEntries` for the shirriff and farmer, `LOTRUnitTradeEntry`,
    pledge types, `LOTRInventoryNPC`); the hired goals (following, remaining
    still, both hiring-player target goals, `LOTRFarmGoal`); the hired-info
    packet; the command horn's and sword's orders; `LOTRAttackRules` in full
    with friendly fire (`LOTRPlayerNPCOptions`); NPC drops' previous owner;
    the arm poses of `setupHeldItem`; the trade sound.
  - [x] **D9c NPC mounts and wargs:** `LOTRNPCMount`, `LOTRNPCRideable`,
    the mount AI tracked in D8, the three wargs (and their armour and rug).
    Done: `LOTRNPCMount` (horses and rideable NPCs), NPCs spawning on and
    riding mounts (the Bounder's pony), mounted speeds and reach, hired
    units with mounts; `LOTRNPCRideableEntity` (taming by riding,
    `LOTRUntamedPanicGoal`, player steering); the wargs of Mordor, Isengard,
    Gundabad and Angmar and the three bombardiers
    (`LOTRWargBombardierAttackGoal`), their model, renderer, glowing eyes,
    saddle and barding (the warg armour now works), sounds and eggs; the
    seven wargskin rugs; the NPC half of `LOTRAttackOnCollideGoal`; the
    NPC attack sound; scorpions, crocodiles and termites hunting NPCs.
  - [x] **D9d onward, one faction per unit:** ~~Bree~~, ~~Rohan~~, ~~Gondor and its fiefs~~, ~~Elves~~, ~~Dwarves~~, ~~Orcs/Mordor~~, ~~Isengard/Dunland~~, ~~Near Harad~~ (done)
    → ~~Rhûn~~ → ~~Far Harad~~; then ~~trolls~~, spiders (the Utumno ice spider; the Mirkwood and Mordor ones are done), ~~ents/huorns~~ (the Mallorn Ent with the bosses), ~~wraiths~~, ~~bosses~~
    and characters (~~Gandalf, Saruman, Gollum~~; the Balrog and the tormented elf go with Utumno).
    - [x] **D9d-a Bree-land:** all twenty Bree NPCs (`npc/bree/`): the
      Bree-man base (`LOTRBreeManEntity`, with the original's goal layout
      and its attack/hiring/avoid hooks, Peter Jackson and
      `LOTRBreeEatGoal`), guard, banner bearer, captain ("Sherriff"),
      blacksmith, innkeeper, farmer, farmhand, the market traders (baker,
      butcher, brewer, mason, lumberman, florist), the Bree-hobbit and its
      innkeeper and four market traders.
      - Systems: `LOTRFoods.BREE`/`BREE_DRINK`; the Bree name banks in
        `LOTRNames`; the `BREE_HOUSE` drop pool; all eighteen Bree trade
        pools, with the brewer's barrels (`LOTRTradeEntryBarrel` as a
        `barrel(...)` entry: a full, finished barrel of the drink at
        strength 2 or 3); the captain's and farmer's unit lists; spawn eggs
        (colours as registered, IDs 385–406), lang, skins and outfits
        copied from `mob/bree` and `mob/hobbit`.
      - **Banner bearers (shared, first used here):** `LOTRBannerBearer`,
        the nearby-banner count every ten ticks (`updateNearbyBanners`,
        which feeds the damage, protection and healing that already read
        it), a hired bearer keeping with its fellow units
        (`LOTRFollowHiringPlayerGoal`), the banner raised in the left hand
        (`LOTRBipedRenderer`), and the unit's "Banner" info line.
      - Client: `LOTRHumanModel` (LOTRModelHuman) and
        `LOTRBreeManRenderer` (skins by sex, a woman's headwear one in
        four, trader outfits); Bree-hobbits use the hobbit renderer.
      - Also: `LOTRSmith` (LOTRTradeable.Smith) marks the blacksmith for the
        trade screen's anvil button (D16).
      - Divergences kept from the original, deliberately: the innkeepers'
        last drop case gives a Bree *food* at a drink's strength (a no-op on
        food), so it is a plain food; the florist's "dandelion" is the
        yellow flower at damage 8, i.e. a dandelion.
    - [x] **D9d-b Ruffians:** `LOTRBreeRuffianEntity` (Ruffian faction,
      Isengard influence; anger spreading to ruffians within up to 24
      blocks, with speech; targeting nauseous players; avoiding Bree
      guards; silver coins and a 1-in-5 skull cup; Dunlending or Bree
      names; hats), `LOTRRuffianBruteEntity` (30 health),
      `LOTRRuffianSpyEntity` (one theft, then flight; drops its loot).
      - The theft AI, shared with the not-yet-ported `LOTREntityBandit`:
        `LOTRBandit` (IBandit), `LOTRBanditStealGoal`,
        `LOTRBanditFleeGoal`. "Armour" is anything giving armour points;
        "sword or tool" is a weapon or tool component minus hoes and shears
        (not tools in 1.7.10). `LOTRValuableItems` now exposes its coin,
        ring, gem and tool-material checks.
      - Bree-men and hobbits keep 8 blocks from brutes; renderer
        `LOTRBreeManRenderer.ruffian` (ruffian skins, a hood one in three);
        eggs (IDs 392–393), lang (with `chat.lotr.ruffianSteal`), textures.
    - [x] **D9e Rohan:** eighteen NPCs (`npc/rohan/`): the Rohan man
      ("Name of the Rohirrim", a `getNPCFormattedName` hook on
      `LOTRNPCEntity`), Rohirrim warrior (one in three mounted on a horse in
      Rohirric barding; sword or battleaxe, a lance one in four when
      mounted, a spear with the other weapon as backup one in four),
      bowman, marshal, shieldmaiden, banner bearer, meadhost, blacksmith,
      farmer, farmhand, stablemaster, and the market traders (baker, brewer,
      builder, butcher, fishmonger, lumberman, orcharder).
      - Systems: `LOTRFoods.ROHAN`/`ROHAN_DRINK`, `getRohirricName`,
        `ROHAN_HOUSE`, all twenty-two Rohan trade pools (brewer's barrels
        included), the marshal's and farmer's unit lists (mounted units in
        Rohirric barding), eggs at their original IDs, lang, skins and
        outfits (`mob/rohan`), `LOTRRohirrimRenderer`.
      - Killing a horse near Rohirrim sets them on the player, with the
        `avengeHorse` speech and a −1 Rohan penalty (LOTREventHandler, now in
        `LOTRNPCKillEvents`).
      - Shared, new: `npcArrowAttack` on `LOTRNPCEntity` (which is now the
        `RangedAttackMob` the original's base class was) -- the bow's launch
        speed and modifiers, the NPC's ranged accuracy, poisoned arrows at
        the NPC's chance; the spread per point of accuracy is today's
        vanilla's. Banner bearers never spawn mounted
        (`canBannerBearerSpawnRiding`, which nothing sets).
      - The Rohan barrow wraith is a skeletal wraith and comes with the
        wraiths.
    - [x] **D9f-a Gondor:** twenty NPCs (`npc/gondor/`): the Gondor man
      ("Name of Gondor"), levyman (militia in dyed leather and a gambeson,
      seeking enemies even out of sight), soldier (one in six mounted in
      Gondorian barding; warhammer, sword or pike, lance mounted one in
      three, spear with backup one in five), archer, captain, banner
      bearer, Tower Guard, the renegade (Near Harad's, armed as Umbar),
      bartender, blacksmith, farmer, farmhand and the market traders
      (baker, brewer, butcher, fishmonger, florist, greengrocer, lumberman,
      mason).
      - Systems: `LOTRFoods.GONDOR`/`GONDOR_DRINK`, `getGondorName`,
        `GONDOR_HOUSE`, all twenty-four Gondor trade pools (brewer's barrels
        included), the captain's unit list (the Tower Guard pledge-only,
        mounted soldiers barded) and the farmer's, eggs at their original
        IDs, lang, skins and outfits (`mob/gondor`, the renegade's hood from
        `mob/nearHarad`), `LOTRGondorManRenderer` (random common outfits
        and headwear, trader outfits, the renegade's hood).
    - [x] **D9f-b Gondor's fiefs:** twenty-six NPCs (`npc/gondor/`):
      Dol Amroth's man-at-arms, archer, swan knight, captain and banner
      bearer (all in the swan knights' skins, `LOTRGondorManRenderer.swanKnight`,
      mounts in Dol Amroth barding); Lossarnach's axeman (battleaxe up close,
      thrown Lossarnach axes at 16 blocks), captain and banner bearer;
      Pelargir's marine, commander and banner bearer; Pinnath Gelin's
      soldier, captain and banner bearer; the Blackroot Vale's soldier,
      bowman (24 blocks, accuracy 0.5), bowlord and banner bearer;
      Lebennin's levyman, levymaster and banner bearer; Lamedon's warrior
      (Lamedon barding), archer, captain, banner bearer and hillman.
      - All seven captains' unit lists (swan knights pledge-only, Dol
        Amroth's mounted men-at-arms barded half the time), eggs at their
        original IDs, lang, swan knight skins. The fief soldiers keep the
        Gondor soldier's mounted/spear rolls underneath their own gear, as
        the original's `super.onSpawnWithEgg` left them.
      - The Ithilien rangers go with the Rangers of the North (shared
        `LOTRRenderRanger`); the ruins wraith with the wraiths.
    - [x] **D9g-a Elves: the base and Lothlórien.** `LOTRElfEntity`
      (LOTREntityElf: 30 health, poison-proof, blade and bow switching,
      elf bones, arrows, lembas to a player, male war cry and idle speech --
      `lotr:elf.male.attack`/`.say`, sounds copied -- Sindarin or Quenya
      names; the jazz elf: its "BoopBoopBaDoop" flag, solo, spin, colours,
      skins, outfit and "Jazz-elf" name). Lothlórien: elf, warrior, lord,
      banner bearer, warden (all but invisible and silent while stalking a
      new target), smith and trader.
      - Shared, new: `LOTRTravellingTrader` / `LOTRTravellingTraderInfo`
        (a day's visit with an escort, announced, warned and ended; saved
        as "DespawnTime" and "Escorts"), wired into `LOTRNPCEntity`;
        `getEntityClassName` on `LOTRNPCEntity`; `LOTRElfModel` (ears,
        chest, tall hair, jazz-solo arms) and `LOTRElfRenderer` (skins by
        people and sex, jazz, cloaked traders and smiths, the unseen warden,
        April Fools).
      - Systems: `LOTRFoods.ELF`/`ELF_DRINK`, the Sindarin/Quenya name
        generators, `ELF_HOUSE`, the Galadhrim trader's and smith's pools,
        `ELF_LORD` (wardens, warriors and banner bearers for anyone pledged
        to an elven people), eggs at their original IDs, lang (with the
        travelling trader's arrival and departure), skins and cloaks.
      - Kept from vanilla 26.2's renderer: an unseen warden's armour and held
        items are hidden rather than drawn at 5% opacity.
      - Elves bowing (user: keep it): to friendly players of the
        bowing-elves group within eight blocks, the pose in `LOTRBipedModel`.
        The original read the group from its authors' player-details web
        service; here it is the player tag `lotr.bowing_elves`
        (`/tag <player> add lotr.bowing_elves`).
    - [x] **D9g-b High Elves:** `LOTRHighElfBaseEntity` (the High Elf
      faction, miruvor to a player) and, for Lindon and Rivendell alike, the
      elf, warrior (bow at 24 blocks), lord ("Rivendell Captain"), banner
      bearer and smith, with Rivendell's travelling trader (the Rivendell
      Wanderer). `HIGH_ELVEN_HALL` and `RIVENDELL_HALL` drop pools, both
      smiths' pools and the trader's (its "planks" at damage 9 are beech),
      `HIGH_ELF_LORD` and `RIVENDELL_LORD`, eggs at their original IDs,
      lang, High Elf skins and cloaks.
    - [x] **D9g-c Wood-elves:** elf (bow only, Sindarin names, red wine to a
      player), scout (vanishes to somewhere 6-16 blocks off when pressed,
      with `lotr:elf.woodElf_teleport`, copied), warrior (on elk in
      Wood-elven barding), captain, banner bearer and smith.
      `LOTRWoodElfTargetGoal` (LOTREntityAINearestAttackableTargetWoodElf:
      no trust below the first rank, and at 0 or above a one-in-(20 x
      alignment) chance of being set upon), with an `addTargetTasks` variant
      taking a people's own player goal. `LOTRFoods.WOOD_ELF_DRINK`,
      `WOOD_ELF_HOUSE`, the smith's pools, `WOOD_ELF_CAPTAIN`, eggs at their
      original IDs, lang, skins and cloak. The scout armour's speed boost was
      already ported with the armour (`LOTRArmourSets`).
    - [x] **D9g-d Dorwinion, men and elves** (one faction): the Dorwinman
      (runs from fights; "Name of Dorwinion"), Vintner Guard (grape alert,
      "GrapeAlert"), its captain, banner bearer and crossbower, vinehand
      (red or white grapes), vinekeeper, travelling merchant; the Dorwinion
      elf (blade only), Warrior, Archer and Captain of Bladorthin, banner
      bearer, Vintner-elf (wine of every strength, a wine in hand) and elf
      merchant.
      - Shared, new: `npcCrossbowAttack` on `LOTRNPCEntity` (the vanilla
        crossbow shot sound, as the port's crossbows use);
        `LOTRNPCEntity.shouldTraderRespawn()`, which the trader respawner
        now reads as the original did, so a travelling trader or vinekeeper
        brought back once is not brought back again (the Galadhrim and
        Rivendell traders now say no too); `LOTRFoods.getRandomBrewableDrink`;
        fixed-strength drink trade entries.
      - Systems: `LOTRFoods.DORWINION`/`DORWINION_DRINK`, `getDorwinionName`,
        `DORWINION_HOUSE` (its "reeds" are vanilla's sugar cane), the
        vintner's, vinekeeper's and merchants' pools (barrels included),
        `DORWINION_CAPTAIN`, `DORWINION_ELF_CAPTAIN`, `DORWINION_VINEKEEPER`,
        eggs at their original IDs, lang, `LOTRDorwinionManRenderer` (skins,
        one in two in an outfit), Dorwinion elf skins and the vintner's cloak.
    - [x] **D9h Dwarves** (`npc/dwarf/`, 17 NPCs): `LOTRDwarfEntity`
      (Durin's Folk, "Name son/daughter of Father"; marries at +200 with the
      dwarven ring, up to three children; one in three spawned by hand or
      hired a woman, one in twenty spawned by hand a child; dwarf attack,
      hurt and kill sounds, a woman's pitch x1.4; bones, larder and hall
      drops, and a player's rare drop of iron, steel, gold or silver nuggets
      or the Book of True-silver; legacy "DwarfName"), warrior (spear with
      backup one in six, bare-headed one in ten), axe-thrower (dwarven
      throwing axes at 12 blocks), commander, banner bearer (silver-trimmed),
      miner (mine-corridor haul, mithril nugget), smith, Iron Hills
      merchant; the same eight for the Blue Mountains (`LOTRBlueDwarfEntity`,
      marrying only their own; the smith is `LOTRBlueMountainsSmithEntity`);
      and the wicked dwarf (Mordor's; trades at +100 with Mordor, Angmar or
      Rhûn).
      - Systems: `LOTRFoods.DWARF`/`BLUE_DWARF`/`DWARF_DRINK`, `getDwarfName`
        and the child's name, `DWARF_HOUSE_LARDER`, `BLUE_DWARF_HOUSE_LARDER`,
        `DWARVEN_TOWER`, `BLUE_MOUNTAINS_STRONGHOLD`, `DWARVEN_MINE_CORRIDOR`,
        all fourteen trade pools (barrels of dwarven ale included),
        `DWARF_COMMANDER`/`BLUE_DWARF_COMMANDER` (dwarf-pledged, boars in
        dwarven barding), the dwarf sounds, Dwarfbane (`BANE_DWARF` on the
        new `lotr:dwarves` tag), eggs at their original IDs, lang,
        `LOTRDwarfModel` (widened body and legs, beard hood, child sizing)
        and `LOTRDwarfRenderer` (13/16 size, skins per people and sex, ring,
        the commanders' and merchants' cloaks, the smiths' apron, the wicked
        dwarves' skins and apron).
      - Follow-ups: Elfbane and Wargbane (`BANE_ELF`, `BANE_WARG` on the new
        `lotr:elves` and `lotr:wargs` tags), now their NPCs exist. Every
        later elf, dwarf or warg type must join its tag. The Hobbit
        orcharder's `shouldTraderRespawn()` now says no, as the original's.
    - [x] **D9i-a Orcs: the base and Mordor** (`npc/orc/`, `npc/mordor/`, 12
      NPCs). `LOTROrcEntity`: weak orcs slowed under the day sky and their
      armour at 3/4; skirmishing (`LOTROrcSkirmishGoal`, with its jeers);
      Mordor's wary player targeting (`LOTROrcTargetGoal`); fleeing lit orc
      bombs (`LOTRAvoidOrcBombGoal`) and, outside Mordor, players at -500
      (`LOTROrcAvoidGoodPlayerGoal`); flesh, bone, maggoty bread and the
      rare orc draught or steel; orc sounds; "OrcName". The Mordor orc,
      archer, bombardier (`LOTROrcPlaceBombGoal`, the bomb shown in its left
      hand through `LOTRNPCEntity.getHeldItemLeft`), trader, commander, the
      Mordor, Minas Morgul and Nan Ungol banner bearers, and the Black Uruk,
      archer, captain and banner bearer.
      - Systems: `LOTRFoods.ORC`/`ORC_DRINK`, `getOrcName`, `ORC_TENT`,
        `BLACK_URUK_FORT`, the trader's pools, `MORDOR_ORC_MERCENARY_CAPTAIN`
        and `BLACK_URUK_CAPTAIN`, Orcbane (`lotr:orcs`), hobbits fleeing orcs,
        eggs at their original IDs, lang, `LOTROrcModel` and `LOTROrcRenderer`
        (orc and Black Uruk skins, 0.85 for weak orcs, glowing eyes).
      - The orc bomb's `droppedByHiredUnit`/`droppedTargetingPlayer` rule
        (B13b) is now ported: an NPC's bomb breaks terrain only if hired or
        aimed at a player, and then only with mobGriefing.
      - Moved on: the slaver (with the Nurn slaves) and the spider keeper
        (with the Mordor spiders).
    - [x] **D9i-b Orcs: Gundabad and Angmar** (`npc/gundabad/`, `npc/angmar/`,
      13 NPCs). Gundabad's orc (armed and armoured from 45 scavenged weapons,
      seven spears and eight sets of gear; one time in 4000 "Such Wealth"),
      archer (a crossbow one time in four, else an orc bow or bow), scrounger
      (trades at +50), banner bearer, Uruk, Uruk archer and chieftain (an
      Uruk); Angmar's orc, archer, bombardier, trader, chieftain and banner
      bearer.
      - Systems: `GUNDABAD_TENT`, `ANGMAR_TENT`, both traders' pools,
        `GUNDABAD_ORC_MERCENARY_CAPTAIN`, `ANGMAR_ORC_MERCENARY_CAPTAIN` (its
        trolls wait on the trolls), the `lotr:orcs` tag, eggs at their
        original IDs, lang, `LOTROrcRenderer`.
      - Wired up now that orcs exist: hobbits flee wargs; wargs spawn with an
        orc rider one time in three (Mordor and Angmar ones then barded half
        the time; Uruk wargs got theirs in D9j-a); orcs hunt rabbits (vanilla's,
        as the wargs do); and the elven blades glow near orcs
        (`LOTRElvenBladeItemModel`, `lotr:elven_blade`: the glowing
        texture, unshaded, four glints, within 24 blocks -- Sting 40 -- and
        only in the hand). The elven daggers' and Sting's models moved from
        datagen to hand-written resources for it.
    - [x] **D9i-c Orcs: Dol Guldur, and the spiders** (`npc/dolguldur/`,
      `npc/spider/`). Dol Guldur's orc (riding a Mirkwood spider when sent
      out mounted), archer, trader, chieftain and banner bearer.
      `LOTRSpiderEntity` (LOTREntitySpiderBase): size 0-3 setting health,
      speed, bite and scale; slowness or poison venom by difficulty, immune
      to its own; leaping, wall-climbing (a ridden one five seconds at a
      time), no fall damage, not held by cobwebs but held by quagmire;
      rideable and tameable at +50, mends on bones, poison drawn into a glass
      bottle; string and eyes. The Mirkwood spider (size 0-2, half venomous,
      mystery webs) and the Mordor spider (size 1-3, poisonous, one in three
      with an orc rider). The Mordor spider keeper, riding a size-3 spider.
      - Systems: `DOL_GULDUR_TENT`, the trader's pools, `DOL_GULDUR_CAPTAIN`
        (its Mirk-trolls wait on the trolls) and `MORDOR_ORC_SPIDER_KEEPER`,
        the orcs and arthropod tags, eggs at their original IDs, lang,
        `LOTRSpiderModel` and `LOTRSpiderRenderer` (skins by venom, two sets
        of glowing eyes -- `LOTRGlowingEyes` is now keyed per eye set).
      - Wired up: the mystery web's small spider; the quagmire holding
        spiders; hobbits fleeing spiders.
      Isengard's orcs go with Isengard/Dunland; Utumno's with Utumno.
    - [x] **D9i-d Angmar's hillmen** (`npc/angmar/`): the Rhudaur hillman
      (man or woman, Rhudaur names, scavenged wood/stone/iron/bronze arms, a
      spear one time in eight, house goods), warrior (Angmar arms, Angmar,
      bone or fur armour), axe-thrower (iron or bronze throwing axes at 12
      blocks), chieftain and banner bearer (Rhudaur's banner).
      - Systems: `LOTRFoods.RHUDAUR`/`RHUDAUR_DRINK`, `getRhudaurName`,
        `ANGMAR_HILLMAN_HOUSE`, `ANGMAR_HILLMAN_CHIEFTAIN` (Angmar wargs,
        the armed hillmen's barded three times in ten), eggs at their
        original IDs, lang, `LOTRAngmarHillmanRenderer` (skins by sex, the
        plain hillman's outfits).
    - [x] **D9j-a Isengard** (`npc/isengard/`): the snaga (scavenged gear,
      orc steel, Uruk tents) and archer (a plain bow); the Uruk-hai (26
      health, speed 0.22, no skirmishing, deeper voice, Uruk steel),
      crossbower, sapper (does not flee bombs; places his at 1.5, then
      charges at 2.0), berserker (x1.15 in box and drawing, 30 health, +2
      damage, furs and berserker gear), trader, chieftain and banner bearer.
      - Systems: `URUK_TENT`, the trader's pools, `URUK_HAI_MERCENARY_CAPTAIN`
        (berserkers pledge-only), the orcs tag, eggs at their original IDs,
        lang, Uruk skins and the berserker's scale in `LOTROrcRenderer`,
        `LOTROrcEntity.avoidsOrcBombs`/`getPlaceBombSpeed` hooks.
      - Wired up: Uruk wargs spawning with snaga riders.
    - [x] **D9j-b Dunland** (`npc/dunland/`): the Dunlending (man or woman,
      club/trident/stone arms, a fur hat one time in four, drunkard speech,
      "DunlendingName"; one in 10000 riding an Uruk warg bombardier with an
      orc bomb for a hat), warrior, bowman, axe-thrower, berserker (30
      health, +2 damage, fur and bone, own skins), warlord, bartender (a
      `LOTRBartender`, "%s's Tavern", his own drops) and banner bearer.
      - Systems: `LOTRFoods.DUNLENDING`/`DUNLENDING_DRINK`, `DUNLENDING_HOUSE`,
        the bartender's pools (vanilla rabbit stew), `DUNLENDING_WARLORD`,
        eggs at their original IDs, lang, `LOTRDunlendingRenderer` (skins by
        sex and berserker, outfits and the bartender's apron for the plain
        Dunlending's renderer only, the bomb rider's floating bomb and half-
        block lift).
    - [x] **D9k-a Near Harad: the base and the Southron coast** (`npc/harad/`,
      19 NPCs). `LOTRNearHaradrimBaseEntity` (men or women; seek no one out
      -- the warriors do; "HaradrimName"; Harad turbans dyed through
      DYED_COLOR). The coast Southron (coast names, Southron food and drink,
      horses in coast Southron barding), warrior (bronze or Umbaric arms,
      turbans of five colours), archer, warlord (his own skin), banner
      bearer, champion (always mounted), blacksmith, the Merchant of Harad
      (travelling, escorted by Southrons), bartender, farmer (a plain turban:
      the original's colour went to a tag the turban never read) and nine
      bazaar traders (random-hued turbans).
      - Systems: `LOTRFoods.SOUTHRON`/`SOUTHRON_DRINK`, the coast, Harnennor,
        Umbar and nomad name functions, `NEAR_HARAD_HOUSE`, the blacksmith's,
        merchant's, bartender's and every `HARAD_*` market pool (which the
        other Harad peoples share), `NEAR_HARADRIM_WARLORD`, eggs at their
        original IDs, lang, `LOTRNearHaradrimRenderer` (skins by sex,
        warriors', the warlord's; trader outfits).
    - [x] **D9k-b Umbar and the Corsairs** (`npc/harad/`, 21 NPCs): the
      Umbarian ("Name of Umbar"), soldier (one in six mounted), archer,
      captain, banner bearer, bartender, blacksmith, farmer and nine market
      traders; the Corsair (blade and Harad bow by turns, looting coins,
      Corsair chests), his captain and slaver; and the Harad slave (Gondor,
      Near Harad, Morwaith or Taurethrim by birth, of that faction, farming
      for hire, only set upon by Near Harad's enemies).
      - Systems: `LOTRFoods.CORSAIR`/`CORSAIR_DRINK`/`HARAD_SLAVE`,
        `getMoredainName`/`getTauredainName`, the `CORSAIR` chest (coins in
        their three denominations), `UMBAR_BLACKSMITH`/`UMBAR_MASON` pools,
        `UMBAR_CAPTAIN`, `CORSAIR_CAPTAIN` (with the "Corsair" unit note --
        `LOTRUnitTradeEntry.setExtraInfo`) and `CORSAIR_SLAVER` (slaves as
        farmers), eggs at their original IDs, lang, the Corsairs' Harnedor
        skins and Umbar soldiers' warrior skins in `LOTRNearHaradrimRenderer`,
        `LOTRHaradSlaveRenderer`.
    - [x] **D9k-c Harnedor** (`npc/harad/`, 17 NPCs): the Harnedhrim ("Name
      of Harnennor"; unlike the rest of Near Harad, every one seeks out its
      enemies), warrior (Haradric bronze, turbans or Harnennor helmets, one in
      eight mounted), archer, warlord, banner bearer, bartender, bronzesmith,
      farmer (who also hires out farmhands), farmhand, and the market -- baker,
      brewer, butcher, fishmonger, huntsman (in leather, spear and fur),
      lumberman, stonemason and miner, the last three with bronze tools.
      - Systems: `LOTRFoods.HARNEDOR`/`HARNEDOR_DRINK`, `HARNENNOR_HOUSE`,
        the bartender's, bronzesmith's and `HARAD_HUNTER_*` pools,
        `HARNEDOR_WARLORD` and `HARNEDOR_FARMER`, eggs at their original IDs,
        lang, the Harnedor skins, warriors' skins and outfits (half the time,
        with no chestplate) in `LOTRNearHaradrimRenderer`.
    - [x] **D9k-d The nomads** (`npc/harad/`, 10 NPCs): the Harad nomad
      (a turban one time in four), the Nomad Guard (Haradric bronze, nomad
      armour, turban or nomad cap, one in eight riding out), archer,
      chieftain, banner bearer, armourer, merchant (travelling, in full dyed
      robes, with a nomad escort), and the bazaar stonemason, brewer and
      miner. Nomads ride camels, chested and carpeted.
      - Systems: `LOTRFoods.NOMAD`/`NOMAD_DRINK`, `NOMAD_TENT`, the
        armourer's and merchant's pools, `NOMAD_WARLORD` (camel-mounted
        units), `LOTRCamelEntity.setNomadChestAndCarpet`, eggs at their
        original IDs, lang, the nomad skins and hats (half the time, with an
        empty head slot) in `LOTRNearHaradrimRenderer`.
    - [x] **D9k-e The Gulf of Harad** (`npc/harad/`, 17 NPCs): the Gulfing
      (who, like the Harnedhrim, seeks out its enemies), warrior (Gulfen
      armour, a khopesh two times in three, one in ten mounted), archer,
      warlord, banner bearer, blacksmith (drops iron), bartender (skull cup),
      farmer (no farmhands), and the bazaar -- stonemason, butcher, brewer,
      fishmonger, baker, miner, goldsmith, lumberman and huntsman (in gemsbok
      hide, with a lion fur).
      - Systems: `LOTRFoods.GULF_HARAD`/`GULF_HARAD_DRINK`, `GULF_HOUSE`,
        `getGulfHaradName`, the blacksmith's, bartender's, huntsman's,
        baker's and lumberman's pools, `GULF_WARLORD`, eggs at their original
        IDs, lang, the Harnedor skins and outfits for the Gulf in
        `LOTRNearHaradrimRenderer`.
      - Fixed along the way: the Harnedor, Southron and Umbar traders the
        original makes male-only (butchers, masons, lumbermen, huntsman,
        miner) now are; the Harnedor and Umbar blacksmiths no longer wear
        the blacksmith's outfit, which the original gave only to the Near
        Harad blacksmith.
      The bandits (`LOTREntityBandit`, `LOTREntityBanditHarad`) go with the
      spawning (D12), the pyramid wraiths with the wraiths.
  - [x] **D9l Rhûn** (`npc/rhun/`, 20 NPCs): the Easterling (Rhûnic, iron or
    bronze dagger; drunkards' speech), the Clansman (levyman: any weapon to
    hand, leather or bronze piece by piece, a kaftan half the time), warrior
    (full Rhûnic armour, one in six mounted), archer, fire-thrower (fire pots
    at range, a dagger up close or against a burning target), Golden
    Easterling (25 health, gold armour, Rhûnic barding), warlord, banner
    bearer, blacksmith (drops iron or gilded iron), bartender, farmer (hires
    farmhands), farmhand, and the market -- lumberman, stonemason, butcher,
    brewer, fishmonger, baker, huntsman and goldsmith, most in kaftans of
    their trade's colour.
    - Systems: `LOTRFoods.RHUN`/`RHUN_DRINK`, `EASTERLING_HOUSE`,
      `getRhunicName`, the eleven `RHUN_*` trade pairs (the farmer's without
      the branding iron), `EASTERLING_WARLORD` (pledge-only Golden
      Easterlings, their horses barded) and `EASTERLING_FARMER`, eggs at
      their original IDs, lang, `LOTREasterlingRenderer` (skins and the
      blacksmith's outfit).
  - [x] **D9m Far Harad**, one people per unit:
    - [x] **D9m-a The Moredain** (`npc/farharad/`, 7 NPCs): the Morwaith
      ("Name of the Morwaith"; every one seeks out its enemies, on a zebra
      when mounted), warrior (Moredain weapons four times in five, a
      Moredain spear as well one in three, the helmet one in three, one in
      ten mounted), banner bearer, chieftain (lion-skin armour, battleaxe),
      huntsman, hutmaker, and the mercenary (fights and is hired for Near
      Harad, 20 coins, in Near Harad armour and a turban).
      - Systems: `LOTRFoods.MOREDAIN`/`MOREDAIN_DRINK`, `MOREDAIN_HUT`, the
        huntsman's and hutmaker's pools, `MOREDAIN_CHIEFTAIN` (zebra-mounted
        warriors), `LOTRMercenary` (its hire screens stay with D16, and
        `isTrader`/`isCivilianNPC` now count it), eggs at their original
        IDs, lang, `LOTRMoredainRenderer` (skins, outfits one in three with
        no chestplate).
      - Fixed along the way: the Harnedor and Gulf traders, bartenders,
        farmers and smiths went looking for enemies, as their people do;
        the original's `addTargetTasks(false)` is back. `isCivilianNPC`
        now also excludes unit traders, as the original did.
    - [x] **D9m-b The Tauredain** (`npc/farharad/`, 9 NPCs): the Taurethrim
      ("Name of the Taurethrim"; kind words only to the friendly and
      aligned), warrior (Taurethrim armour, the helmet four in five, a
      spear as well one in five), blowgunner (darts at 16 blocks, sees 24,
      drops darts), banner bearer, chieftain (+200), shaman (+100, poisoned
      dagger and poison), smith (drops obsidian shards), farmer (hires
      farmhands) and farmhand (potatoes unhired). The pyramid wraith goes
      with the wraiths.
      - Systems: `LOTRFoods.TAUREDAIN`/`TAUREDAIN_DRINK`, `TAUREDAIN_HOUSE`,
        the shaman's, smith's and farmer's pools, `TAUREDAIN_CHIEFTAIN` and
        `TAUREDAIN_FARMER`, eggs at their original IDs, lang,
        `LOTRTauredainRenderer` (skins, outfits one in three with no
        chestplate, the shaman's outfit always).
    - [x] **D9m-c The half-trolls** (`npc/halftroll/`, 5 NPCs): the
      half-troll (35 health, hits for 6, sees 24, hunts rabbits, spawns in
      darkness, a mohawk and horns at random, an orc's name; hired, it wears
      only half-troll armour), warrior (half-troll armour, one in twelve on
      a rhino, half of those barded), banner bearer, warlord (40 health,
      +200) and scavenger (+50, still seeks out enemies, never respawns).
      - Systems: `LOTRFoods.HALF_TROLL`/`HALF_TROLL_DRINK`, the scavenger's
        pools, `HALF_TROLL_WARLORD` (barded rhinos half the time), the
        `halfTroll.*` sounds, `canReEquipHired` on the NPC (for the hired
        inventory screen, D16), eggs at their original IDs, lang,
        `LOTRHalfTrollModel` and `LOTRHalfTrollRenderer` (skins, mohawk and
        horns, the half-troll armour textures on a half-troll, the
        scavenger's outfit in place of a helmet).
  - [x] **D9n The trolls**, in parts:
    - [x] **D9n-a Trolls, Olog-hai and Mirk-trolls** (`npc/troll/`): the
      troll (60 health, armour 8, hits for 5 and flings, half knockback;
      burns under the open sky by day and runs for shelter
      (`LOTRTrollFleeSunGoal`), turning to stone after 300 ticks; lets be
      those at +100 and attacks those at 0 or better only now and then
      (`LOTRTrollTargetGoal`); one in ten two-headed; tickled with feathers
      by friends at +100 it sniffs and sneezes slime), the Olog-hai (x1.25,
      80, armour 15, hits for 7 with a hammer-blow that strikes all about,
      sun-proof) and the Mirk-troll (x1.2, 70, armour 12, hits for 6 and
      poisons, sun-proof).
      - Systems: `troll.*` sounds, troll names, `lotr.hiredNPC.trollStone`,
        `LOTRNPCEntity.getWalkTargetValue` (getBlockPathWeight: creatures of
        the dark prefer the dark -- the orcs, wargs, spiders and half-trolls
        too), `canReEquipHired` refused, the Olog-hai in the Mordor and
        Black Uruk captains' lists, the troll in Angmar's and the
        Mirk-troll in Dol Guldur's, eggs at their original IDs, lang,
        `LOTRTrollLivingModel` (hurt head, two heads, the four weapons, the
        walk's sway, sniffing, the throwing pose) and `LOTRTrollRenderer`
        (skins, outfits or armour, weapons, the Shrek and Drek colours, the
        sneeze's shaking, speech), with speech drawing shared through
        `LOTRNPCSpeechRendering`.
    - [x] **D9n-b Hill-trolls and snow-trolls** (`npc/troll/`): the
      Hill-troll (`LOTREntityMountainTroll`: x1.6, 70, hits for 7; within
      twelve blocks it fights, further off it hurls rocks for its thrown
      rock damage, 5; crumbles in the sun; may leave a troll totem part)
      and the snow-troll (x0.8, 40, frost-proof; slowing blows, snowballs
      that hit for 3; bursts into snow in the sun; fur and snowballs).
      The mountain-troll chieftain goes with the bosses, the Utumno trolls
      with Utumno.
      - Systems: `LOTRThrownRockEntity` (four blocks across, smashing into
        stone and cobblestone, a totem's rock bringing a troll) and its
        renderer, `LOTRTrollSnowballEntity`, the `large_stone` particle
        (LOTREntityLargeBlockFX), `LOTRNPCAttributes.THROWN_ROCK_DAMAGE`,
        `troll.rockSmash`, the Hill-troll in Angmar's captain's list (120,
        +350, pledge only), eggs, lang, and the renderer's spiked clubs, the
        Hill-troll's raised rock, and the snow-troll's skin-drawn outfit.
  - [x] **D9o Ents and huorns** (`npc/ent/`): the tree base (`LOTRTreeEntity`:
    oak, beech or birch; poison-proof; a third of any damage but fire or an
    axe by hand; half knockback; never turns on another tree; logs and
    sticks), the Ent (100, hits for 7 and throws back; heals corrupt
    mallorns within 24 blocks when not fighting (`LOTREntHealSaplingGoal`);
    blinks; two to five extra crowns for half of them; may leave an
    Ent-draught), the Huorn and the Dark Huorn (60, hits for 4; a tree at
    rest, unshoved, awake when struck or busy; stirs to hunt rarely
    (`LOTRHuornTargetGoal`, throttling both its player and its NPC
    targeting, as the original's one class did); never drowns). The Mallorn
    Ent goes with the bosses.
    - Systems: `LOTRCorruptMallornBlock` (the `ent_kills` count, 0-2) and
      `LOTRCorruptMallornBlockEntity`, `addTargetTasks` taking a faction
      goal too, `ent.step`, Ent names, eggs, lang, `LOTREntModel` (the
      extra crowns, eyelids, pained brows, the healing lean) and
      `LOTREntRenderer` (bark by tree, glowing eyes, speech),
      `LOTRHuornModel` and `LOTRHuornRenderer` (the tree drawn in its own
      log and leaf blocks, the oak's leaves in the biome's foliage colour,
      the face while awake, set square on its block while asleep).
  - [x] **D9p Wraiths and wights** (`npc/wraith/`): the skeletal wraiths
    (`LOTRSkeletalWraithEntity`: 24, fireproof, undead, hostile to all,
    keep from the sun and burn in it save for a helmet, smoke, bones and
    no rares) -- of the Gondor ruins, the Rohan barrows, the Harad pyramids
    (30; the scorpions let them be) and the Taurethrim pyramids, each in
    their people's arms and armour; the Barrow-wight (50, sees 40, a blow
    that slows, weakens and withers, Morgul sparks, grave-goods, rares,
    its quarry synced); and the marsh wraith (fades in and out, hovers
    above the mire, casts `LOTRMarshWraithBallEntity` at its mark from
    twelve blocks, touched only by Wraithbane, leaves when its mark has
    been five seconds out of the water).
    - Systems: Trollbane (trolls and half-trolls, `lotr:trolls`) and
      Wraithbane (`lotr:marsh_wraiths`, earned by no kill) in
      `LOTRModifier`, the ancient items' one-in-four Wraithbane, the
      `minecraft:undead` tag (for Smite and Wightbane), `BARROW_DOWNS` and
      `MARSH_REMAINS`, the `wight.*` and `wraith.*` sounds, eggs, lang,
      `LOTRSkeletalWraithModel`/`Renderer` (the zombie arms on the
      skeleton sheet, at full size), `LOTRBarrowWightModel`/`Renderer`
      (legless, bobbing, swelling and fading as it dies),
      `LOTRMarshWraithModel`/`Renderer` (the fades) and
      `LOTRMarshWraithBallRenderer`.
- [x] **D9q Bosses** (`LOTRBoss`, `LOTRBossInfo`, `LOTRBossJumpAttackGoal`):
    - [x] Hill-troll Chieftain (`LOTRMountainTrollChieftainEntity`): x2, 50
      health, armour 12, hits and throws for 8; rises from the ground over
      five seconds, untouchable, and leaps clear; three coats of armour
      (health x2 and a full heal per coat, x1.5 speed with none, then
      smoking); non-player damage capped at 1; leaps, troll-bringing rocks
      and draining nearby trolls scale with its hurt and lost armour; ten
      seconds of thrashing as it turns to stone. Drops the troll leavings,
      bones, 50-100+ silver coins, `TROLL_HOARD`, its trophy and sometimes
      Gondolin gear; +50, 100 XP, no totem part. No egg, as in the original.
      - Systems: the boss bar (ServerBossEvent), jump attacks,
        `mtc_spawn`/`mtc_armor`/`mtc_heal` particles, the renderer's helmet
        and chestplate coats (`mountain_troll_chieftain_armor.png`) and
        spawn sink, the troll totem now summoning it, lang.
    - [x] Mallorn Ent (`LOTRMallornEntEntity`): an Ent x1.5, 300 health,
      hits for 8 at 12 blocks, leaf bombs (6) further off; rises over 150
      ticks, untouchable, and leaps clear; non-player damage capped at 1 and
      non-tree-felling damage halved; the first time it is brought to
      nothing its weapon shield rises (full heal; then only fire hurts it,
      and only while it burns); leaps, leaf healings (five at once, from
      leaves within 16) and summoned Ents/Huorns scale with its hurt; its
      crown's branches fall away with its health; puts out fires within 12
      as it dies. Drops mallorn logs and sticks, an Ent's drops, its trophy
      and sometimes the charred mallorn mace; +50, 100 XP. No egg.
      - Systems: the third Ent slain beside a corrupt mallorn now calls it
        up (`LOTRCorruptMallornBlock.summonEntBoss`, with its "summon" and
        "shield" speech), `LOTRMallornLeafBombEntity` (no model, a ring of
        leaf fragments), the heal and summon packets
        (`LOTRMallornEntHealPayload`, `LOTRMallornEntSummonPayload`) and
        their client ends, the `mallorn_ent_spawn`, `mallorn_ent_jump_smash`
        and `mallorn_ent_heal` particles and the summon arc
        (`LOTRMallornEntSummonParticle`), the `ent.mallorn.leafAttack` and
        `.summonEnt` sounds, `LOTRMallornEntRenderer` (mallorn skin, x1.5,
        spawn sink, full-bright scrolling shield overlay), lang.
      - Leaf healings save `healTime` as "Time" and read it as "healTime",
        so a reloaded healing comes back spent, as in the original.
- [x] **D9r Characters** (`npc/character/`):
    - [x] **D9r-a Gandalf and Saruman.** The Grey Wanderer (`LOTRGandalfEntity`):
      30 health, sees 40, unhurt by all but creative players and never a
      free target; grey staff, Glamdring drawn to fight with the staff in
      his other hand; hunts Saruman; magic-smoke pipe
      (`LOTRGandalfSmokeGoal`); arrives and departs with a speech to every
      player within 64, a pop and a puff; departs when the tracker lets his
      time lapse and he is not fighting. Saruman (`LOTRSarumanEntity`): the
      original's joke -- eats logs, grunts like an orc ten times a second,
      flings everything within 24 away (players by a sizeless explosion
      packet, bang included), conjures vanilla rabbits and stacks them on his
      head, a hundred random characters for a name, rendered twitching in
      cycling colours; may drop bones.
      - Systems: `LOTRGreyWandererTracker` (active wanderers and their
        3600-tick time, counted down each server tick, saved in
        `LOTRLevelData` as "GreyWanderers"/"GWSpawnTick"),
        `LOTRGandalfRenderer` (skin, wizard hat `LOTRWizardHatModel` and
        cloak unless helmeted/chestplated, Father Christmas at Christmas,
        name 0.75 higher unless speaking), `LOTRSarumanRenderer`,
        `LOTRFoods.SARUMAN`, eggs, lang ("Grey Wanderer").
    - [x] **D9r-b Gollum** (`LOTRGollumEntity`, a `LOTRCharacter` and so
      never despawned): 30 health, 0.6 by 1.2; runs from orcs, elves and harm
      with his arms over his head (`LOTRGollumAvoidEntityGoal`,
      `LOTRGollumPanicGoal`); tamed by twenty raw fish (then 1.5-1.75x as
      many for the next tamer), announced to all; his owner cannot hurt him,
      feeds him fish or meat, and tells him to stay (`LOTRGollumRemainStillGoal`,
      rocking in place) or follow (`LOTRGollumFollowOwnerGoal`, teleporting
      when 16 blocks behind with no path); he fishes for them
      (`LOTRGollumFishingGoal`: 4-12 cod, rests 3000/600 ticks) and drops the
      catch at their feet; gollums at unowned players within 80; his owned
      death is told to all; drops his 27-slot pack.
      - Systems: `LOTRCharacter` (persistence in `LOTRNPCEntity`),
        `LOTRLevelData.gollumSpawned` ("GollumSpawned"), `gollum.*` sounds,
        `LOTRGollumModel`, `LOTRGollumRenderer` (0.85, fish in his mouth,
        name 0.5 higher unless speaking), `chat.lotr.tameGollum`, egg, lang.
- [ ] **D10 Dimension & world gen**: Middle-earth dimension, biome registry
  from `world/biome` + variants, genlayer → map-image-driven biome source,
  features (trees for saplings!), ores, `world/feature`. Large; plan it in its
  own session first and add sub-units here. **Comes after D11 and the GUIs
  (user: structures and UI before world gen).** Decided (user):
  - the dimension is 0-255 with sea level 62, as the original, so
    `LOTRChunkProvider`'s terrain ports exactly;
  - until the ring portal (D15), an op-only command travels to and from
    Middle-earth;
  - the "Middle-earth Classic" world type is ported later, as a late D10
    sub-unit, once the map-driven world works.
- [ ] **D11 Structures & villages** (`structure`, `structure2`, `village`,
  mapgen dwarven mines/tpyr). Ported before D10 (user): each structure is
  built and tested through the structure spawner item, as the original's
  could be; placing them in generated terrain comes with D10.
  Scope: 327 `structure2` classes (~53k lines), 30 older `structure` classes
  (~8k), 14 village generators (~4k), 75 `.strscan` scans, and the
  `LOTRStructures` registry (341 entries, each a structure-spawner subtype).
  - [x] **D11a Foundation.** Done except the spawner chests (below).
    - `LOTRLegacyBlocks`: the original's (block, metadata) pairs as port
      `BlockState`s -- built from `docs/parity/items_blocks.csv` for the
      mod's blocks and 1.7.10's ids for vanilla's, with per-shape metadata
      decoders (stairs, slabs, logs and pillars, doors, trapdoors, fence
      gates, torches, ladders, signs, beds, chests and furnaces, crops,
      colours). Every structure goes through it, so it is proven first.
    - `LOTRStructureBase2` (`LOTRWorldGenStructureBase2`): rotation, the
      bounding box, surface checks, block aliases, restriction flags, and
      the `place*` helpers (chests and their pools, barrels, mugs, plates,
      weapon racks, kebab stands, flower pots, signs, skulls, spawners,
      rugs, animal jars, the Ithildin door, leashes, armour -- on vanilla's
      armour stand, the mod's being a vanilla duplicate).
    - The `.strscan` loader (`LOTRStructureScan`) and `generateStrScan`.
    - The NPC respawner entity (`LOTREntityNPCRespawner`), which most
      inhabited structures place.
    - `LOTRStructures` and the structure spawner item: one item carrying
      the structure id, drawn in its two tint colours (the village kind
      with its own icon), a one-second cooldown, the `item.structureSpawner`
      sound, the "structures banned" level flag and per-player ban, and the
      spawn tab listing.
    - Proven on the hobbit structures (ids 1-8).
    - As built: `LOTRLegacyBlocks` (`lotr/legacy_blocks.tsv`, generated
      from the parity table with each old field's unlocalized name for the
      scans; 1.7.10's blocks through BlockStateData and the data fixers;
      orientation borrowed from the vanilla block of the same shape, with
      the mod's gate, mugs and weapon racks read directly) and
      `LOTRLegacyItems` (`lotr/legacy_items.tsv`; 1.7.10's items through
      ItemStackTheFlatteningFix and the fixers). `LOTRStructureBase2` turns
      only the kinds of block the original turned (`rotateMeta`), sets
      blocks without neighbour updates as the original did, then gives
      every block it set its shape from its neighbours so fences, walls,
      panes and stairs join. `LOTRStructureBase` is the older, unrotated
      base. `LOTRStructureScan` reads the 75 scans from
      `data/lotr/strscan/`. `LOTRNPCRespawnerEntity` and the
      `npc_respawner` item (creative); `LOTRStructures`;
      `LOTRStructureSpawnerItem` (one item, `lotr:structure_id`, tinted and
      switched to the village icon in its items json by
      `lotr:structure_spawner` and `lotr:village_structure`); the ban flag
      (`LOTRLevelData` "StructuresBanned"), the per-player ban attachment,
      /banStructures and /allowStructures; `LOTRChestContents.Pool` now
      carries the original's item counts and `fillChest`; `LOTRFoods`'s
      plate and placeable-drink pickers; the structure names in lang
      (`lotr.structure.<Name>`); the spawn tab lists the spawners and takes
      the Ent's egg as its icon again.
    - The structures themselves are transliterated from the original by a
      script (block and item references, entities, math) and finished by
      hand.
    - [ ] The spawner chests (`spawnerChest`, `spawnerChestStone`,
      `spawnerChestAncientHarad`): chests that let out a creature when
      opened, which ruins and barrows place. With the first structure that
      needs them.
  - [ ] **D11b Older structures** (`structure/`, `LOTRWorldGenStructureBase`)
    that the registry still lists, alongside the faction that owns them.
  - [ ] **D11c onward, one group per unit, in the NPC order:** Shire
    (done in D11a: hole, tavern, picnic bench, windmill, farm, hay bales,
    house, burrow) and Bree; Rangers, Barrow-downs and the ruins; the elves (High Elves,
    Rivendell, Galadhrim, Wood-elves, Dorwinion); dwarves and Dale; Rohan;
    Gondor and its fiefs; Mordor, Dol Guldur, Uruks, Angmar, Gundabad and
    the half-trolls; Dunland; Near Harad (Southron, Umbar, Corsairs,
    Harnedor, nomads, Gulf); Rhûn; Far Harad (Moredain, Tauredain).
  - [ ] **D11 villages** (`village/`, `LOTRVillageGen` and the twelve
    peoples'), each spawnable from its structure-spawner subtype.
  - Left for D10: placing any of it in generated terrain (biome decorators'
    structure chances, fixed structures, the village position caches), the
    dwarven mines and Tauredain pyramids (`mapgen`), and conquest.
  - Banners among the structures wait for the banner entities (D14, user);
    until then their `placeBanner` calls place nothing and are tracked.
- [ ] **D12 NPC spawning, invasions, spawn damping** (extend `LOTRInvasions`).
- [ ] **D13 Map & waypoints / fast travel** (map GUI, `world/map`).
- [ ] **D14 Quests, fellowships, mini-quests, Grey Wanderer.**
- [ ] **D15 Portals**: elven/morgul/utumno portals, Utumno dimension.
- [ ] **D16 Remaining GUIs, commands, particles/fx, sounds, drunken speech.**

## Deferred-port tracker

Everything an audit found that **could not be ported yet**, grouped by the
Track D unit that unblocks it. When a unit below is started, work through its
list here as part of it. Format: what (original class/method) → where it goes
in the port [found in]. Tick an entry and note where it landed once it is
ported. Anything newly found unportable during an audit is added here, not
only to the unit's *Result*.

### D1 — remaining items
- [x] Poisoned Dorwinion elven dagger and poisoned Gundabad Uruk dagger (not
  registered) → `LOTRItems` [B7b] -- both registered in `LOTRCombatItems`
- [x] `featherDyed` and the leather hat's feather: dye recipe, hat-feather
  recipes, the feathered-hat overlay (`leatherHat_feather`), and the Dale
  cracker's leather hat with a white feather → `LOTRDyedHeadModel`,
  `LOTRDaleCrackerItem` [B6d, B9] -- done in D1c
- [ ] Harad turban gold ornament (needs an ornament state; the Merchant of
  Harad wears one half the time and the bazaar traders one time in three) and kaftan/robe
  overlays (`bodyKaftan_overlay`, `legsKaftan_overlay`,
  `helmetHaradRobes_ornament`) [A3, B9]
- [ ] Coin value system (the three coin items as one currency) [LOTRItems]
- [ ] Lore books (`LOTRLore`): also the 1-in-20 lore roll in chest pools
  (`MIRKWOOD_LOOT` carries Woodland Realm and Dol Guldur lore) →
  `LOTRChestContents.pick` [B13b]
- [ ] Fallen leaves (and their charcoal/recipes) [B6b]

### D4 — decorative entities and banners
- [ ] Banner protection (`LOTRBannerProtection`), then its checks in:
  Khamûl's fire `isBannered` (`LOTRKhamulsFireBlock`), the balrog whip's lash
  (`LOTRBalrogWhipItem`), the plate's glass smashing (`LOTRPlateEntity`),
  `LOTRBannerProtectable` on the stone troll, boss trophy and rugs
  (`LOTRRugEntity`), the banner block
  entity's owner/protection data, and the banner copy/clear recipes
  (`LOTRRecipesBanners`) [B4, B5e, B6d, B7d, B13a]
- [ ] Floating barrel entity: `LOTRItemBarrel.onItemRightClick` puts a
  rideable barrel on water → `LOTRBarrelItem` [B5b]

### D5/D6 — player data, config
- [ ] `LOTRConfig` "Immersive Speech" (default on) and "Immersive Speech
  Chat Logs" (default off), fixed at their defaults in `LOTRSpeechClient`
  [D9a]
- [ ] `LOTRConfig` "Enable hired unit levelling" (on) and "Prevent trader
  transport range" (0, off -- the initial home is already recorded), fixed
  at their defaults [D9b]
- [ ] Fellowships' PVP and hired-unit friendly-fire protections and siege
  mode in `LOTRAttackRules` (D14) [D9b]
- [ ] `LOTRConfig`: `alignmentDrain` (drain always on until then),
  `alwaysShowAlignment`, `alignmentXOffset`/`YOffset`, `enchantingLOTR`
  [B12b]
- [ ] Feminine rank option: `setFemRankOverride`, `useFeminineRanks` (also
  via titles) → `LOTRPlayerAlignments` [B12a/b]
- [ ] Viewing faction per dimension region (`getViewingFaction`,
  `prevRegionFactions`) [B12c]

### D7 — achievements and titles
- [ ] `rideWarg` (a tamed, saddled warg ridden) [D9c]
- [ ] Hobbit achievements: `killHobbit`, `marryHobbit` (the marriage goal's
  `marriageAchievement`), `speakToDrunkard` (`LOTREntityHobbit.speakTo`),
  `tradeHobbitShirriff`, `buyPotatoHobbitFarmer`, `hireHobbitFarmer`,
  `buyOrcharderFood`, `tradeBartender`, `sellPipeweedLeaf` [D9a]
- [ ] Achievements to wire up where their triggers already exist:
  `harvestGrapes`, `pickBanana` [B3]; Dwarven door, grape, banana and beacon
  achievements [B4]; `brewDrinkInBarrel` [B5b]; `cookKebab`,
  `smeltObsidianShard` [B5c]; `catchButterfly` [B5d]; `unsmelt` [B5a];
  drink achievements (`LOTRDrinkItem`) [B10]; Dale cracker [B5d];
  `getTrollStatue` (`LOTRStoneTrollEntity`) [B13a]; `hitBirdFirePot`
  (`LOTRFirePotEntity`) [B13c]; `craftAncientItem` (`LOTRAncientItem`)
  [B13]; `pledgeService` and `checkAlignmentAchievements` on alignment
  change and pledging (`LOTRPlayerAlignments`) [B12b]
- [ ] Dwarf achievements: `killDwarf`, `killBlueDwarf`, `killWickedDwarf`,
  `marryDwarf`, `marryBlueDwarf`, `talkDwarfWoman`, `tradeDwarfMiner`,
  `tradeBlueDwarfMiner`, `tradeDwarfSmith`, `tradeBlueDwarfSmith`,
  `tradeDwarfCommander`, `tradeBlueDwarfCommander`, `tradeIronHillsMerchant`,
  `tradeBlueDwarfMerchant`, `tradeWickedDwarf`; the Durin's Folk and Blue
  Mountains shields (`ALIGNMENT_DWARF`, `ALIGNMENT_BLUE_MOUNTAINS`) on
  warriors [D9h]
- [ ] Dorwinion achievements: `killDorwinion`, `killDorwinionElf`,
  `stealDorwinionGrapes`, `tradeDorwinionCaptain`, `tradeDorwinionElfCaptain`,
  `hireDorwinionVinekeeper`, `tradeDorwinionMerchant`, `buyWineVintner`; the
  Dorwinion and Dorwinion elf shields [D9g]
- [ ] Wood-elf achievements: `killWoodElf`, `tradeWoodElfCaptain`,
  `tradeWoodElfSmith`; the Wood-elf shield (`ALIGNMENT_WOOD_ELF`) [D9g]
- [ ] High Elf achievements: `killHighElf`, `killRivendellElf`,
  `tradeHighElfLord`, `tradeRivendellLord`, `tradeHighElfSmith`,
  `tradeRivendellSmith`, `tradeRivendellTrader`; the Lindon and Rivendell
  shields (`ALIGNMENT_HIGH_ELF`, `ALIGNMENT_RIVENDELL`) on warriors [D9g]
- [ ] Lothlórien achievements: `killElf`, `tradeElfLord`,
  `tradeGaladhrimSmith`, `tradeElvenTrader`, `takeMallornWood`; the
  Galadhrim shield (`LOTRShields.ALIGNMENT_GALADHRIM`) on warriors [D9g]
- [ ] Fief achievements: `killSwanKnight`, `tradeDolAmrothCaptain`,
  `tradeLossarnachCaptain`, `tradePelargirCaptain`, `tradePinnathGelinCaptain`,
  `tradeBlackrootCaptain`, `tradeLebenninCaptain`, `tradeLamedonCaptain`; the
  fief shields (`ALIGNMENT_DOL_AMROTH`, `_LOSSARNACH`, `_PELARGIR`,
  `_PINNATH_GELIN`, `_BLACKROOT_VALE`, `_LEBENNIN`, `_LAMEDON`) [D9f]
- [ ] Gondor achievements: `killGondorian`, `tradeGondorBlacksmith`,
  `tradeGondorBartender`, `tradeGondorMarketTrader`, `buyPipeweedGondorFarmer`,
  `hireGondorFarmer`, `tradeGondorianCaptain`; the Gondor shield
  (`LOTRShields.ALIGNMENT_GONDOR`) on soldiers [D9f]
- [ ] Rohan achievements: `killRohirrim`, `tradeRohanBlacksmith`,
  `buyRohanMead`, `tradeRohanMarketTrader`, `tradeRohanFarmer`,
  `hireRohanFarmer`, `tradeRohanStablemaster`, `tradeRohirrimMarshal`; and
  the Rohan shield (`LOTRShields.ALIGNMENT_ROHAN`) on warriors [D9e]
- [ ] Bree achievements: `killRuffianSpy`, `killRuffianBrute`,
  `killBreelander`, `killBreeHobbit`,
  `tradeBreeCaptain`, `tradeBreeBlacksmith`, `tradeBreeInnkeeper`,
  `tradeBreeMarketTrader`, `buyAppleBreeFarmer`, `hireBreeFarmer`; and the
  Bree-land shield (`LOTRShields.ALIGNMENT_BREE`) on guards [D9d]
- [ ] Rank achievements and titles: `LOTRFactionRank.hasRankAchievement` /
  `hasRankTitle` are flags only (`LOTRAchievementRank`, `LOTRTitle`) [B12a]

### D8 — animals and mounts
- [ ] Swan chestplate wings driven by the mount's stride when riding
  (`LOTRSwanChestplateModel`) [B9]
- [ ] Rohirric marshal helmet plume sway (needs limb-swing state through
  `ArmorRenderer`) → `LOTRRohirricMarshalHelmetModel` [B9]
- [ ] Animal jar: occupant bobbing/turning/sounds, the Lórien butterfly's
  light 7, and AnimalJarUpdater (a caged bird never perches) -- unblocked now
  the creatures exist; catching them works through the tags since D8f [B5d]
- [ ] The fire pot's hitBirdFirePot achievement (a bird hit in flight) → D7;
  the bird cage tag is done [B13c, D8f]
- [ ] Creatures' kinds by biome (butterfly: Mirkwood, Lórien, jungle; bird:
  Far Harad) and the Midgewater swarms → D10; midges' shootDownMidges
  achievement → D7 [D8f]
- [x] ~~LOTR rabbit~~ -- user: dropped as a vanilla duplicate (and with it
  `LOTREntityAIRabbitEatCrops` and the attackRabbit achievement) [D8b]
- [x] ~~LOTR fish~~ -- user: dropped as a vanilla duplicate [D8f]
- [ ] Dik-dik's `LOTRAmbientSpawnChecks` and the bear's
  `LOTRAnimalSpawnConditions` (trees per chunk in the biome variant) → with
  natural spawning (D10/D12) [D8b, D8c]
- [x] ~~NPC spear throwing in `LOTRAttackOnCollideGoal`~~ -- not ported:
  spears keep 26.2's mechanics (user), and 26.2's spears are not thrown
  [D8b, D9c]
- [ ] The scorpions' spawner flag and
  depth/pyramid spawn rules, the desert scorpion's ImmuneToHeat, and the
  crocodile's water search and Far Harad swamp light rule → D10-D12 [D8e]
- [ ] Mount achievements rideShirePony, rideCamel, rideGiraffeShire → D7;
  the Rohan and Dor-en-Ernil horse boosts, the camel's ImmuneToHeat, the
  Shire check → D10 [D8d]
- [ ] Vanilla leather horse armour has no 1.7.10-layout sheet, so it is not
  drawn on LOTR horses [D8d]. (LOTR barding no longer goes on vanilla horses
  at all -- user: LOTR horses only.)
- [x] Spawn eggs (`LOTRItemSpawnEgg`): user said to do them as the original
  did -- 1.7.10's shared egg and spots textures (copied from the 1.7.10
  client jar), tinted with each creature's two registerCreature colours.
  One `<creature>_spawn_egg` item per creature in `LOTRSpawnItems` and the
  new "LOTR Spawning" tab; add each egg with its creature [D8a]

### D9 — NPCs
- [x] ~~NPC projectiles: `LOTREntityMallornLeafBomb` (mallorn Ent) [D3]~~ — D9q
- [ ] Corrupt mallorn's tile entity's ticking: in Fangorn, one tick in forty,
  spawns an Ent within 20 if none is within 24 (`LOTRTileEntityCorruptMallorn`),
  with the biomes (D10). The block entity itself exists since D9o, for the
  Ents to find [D2]
- [x] ~~The third Ent slain by a player beside a corrupt mallorn summoning the
  Mallorn Ent (`LOTRBlockCorruptMallorn.summonEntBoss`); until then the count
  stops at two, with the bosses [D9o]~~ — D9q
- [ ] `LOTRFactionData` per-player per-faction stats (NPC kills, enemy kills,
  trades, hires, mini-quests, conquest, conquest horn; save keys `NPCKill`,
  `EnemyKill`, `Trades`, `Hired`, `MiniQuests`, `Conquest`, `ConquestHorn`)
  and `LOTRFactionBounties` (per faction, per player, kill records;
  `recordNewKill` on NPC death) [B12d]
- [ ] Alignment from kills: `LOTRAlignmentValues.Bonuses` (every NPC's kill
  value), `addAlignment`'s `forcedBonusFactions`, and callers of
  `LOTRPlayerAlignments.onPledgeKill` [B12a/b]
- [ ] NPC-influence check in control zones (`LOTRFaction` control-zone
  multiplier) [B12a]
- [x] ~~Mountain troll chieftain: the troll totem summons a CHICKEN stand-in
  (`LOTRTrollTotemBlock`) [B5e]~~ — summons the chieftain (D9q)
- [ ] Orders and summons: command sword, command horn (`command`), war horn
  (`summon`), NPC respawner item [B7d, LOTRItems]
- [ ] Gandalf: his natural arrival 4 to 16 blocks from a player every two
  minutes while no Grey Wanderer is abroad
  (`LOTRGreyWandererTracker.performSpawning`, from LOTREventSpawner in the
  Middle-earth dimension) [D12], his welcome mini-quest and the renewal of
  his time when he offers it [D14], his cape (`LOTRCapes.GANDALF`,
  `GANDALF_SANTA`) with the NPC capes, and his hunting of Balrogs with
  Utumno [D15] [D9r-a]
- [ ] Gollum: natural spawning within 128 of the High Pass waypoint
  (`LOTRGollumSpawner`, with the Middle-earth dimension and waypoints)
  [D12, D13]; his pack's screen for his owner (sneak-use, GUI 10) with the
  GUIs [D16]; his health bar for his owner, with the hired units' bars;
  `tameGollum` achievement [D7] [D9r-b]
- [ ] Structures (D11) until the biomes (D10): a structure's biome top and
  filler blocks are grass and dirt; its flowers and grass are vanilla's
  dandelion, poppy and grass (the original's outside its biomes); a biome's
  own trees (`placeBiomeTree`, e.g. the hobbit hole's Shire tree) grow
  nothing; and natural generation's biome checks (`isBiome`) match nothing
  [D11a]
- [ ] Structures' banners (`placeBanner`, `placeWallBanner`) place nothing
  until the banner entities (D14) [D11a]
- [ ] Structures' flower pots hold only plants vanilla can pot; the mod's own
  plants need their potted forms (the original's pot was its own block) [D11a]
- [ ] The NPC respawner's screen (set its kinds, ranges, interval, mounts,
  enemy blocking; destroy it), with the GUIs; until then a creative player
  breaks it by striking it [D11a]
- [ ] NPC faction checks: Sauron's mace exemption, Gandalf's fireball
  (`!HIGH_ELF.isGoodRelation(npcFaction)`), rope/Lothlórien
  [B7, B10, B13b]
- [ ] Termite mound spawning termites [B4] (the quagmire and spiders: done, D9i-c)
- [ ] Hobbits flee trolls (12 blocks) -- orcs, wargs, spiders and ruffian
  brutes are done -- and huorns (`LOTREntityAIAvoidHuorn`): each `AvoidEntityGoal`
  arrives with its creature (`LOTRHobbitEntity`) [D9a]
- [ ] Drinking near a friendly bartender makes an NPC drunk: implement
  `LOTRBartender` on the remaining innkeepers and bartenders (the Hobbit bartender, both Bree innkeepers, the Rohan meadhost, the Gondor and Dunlending bartenders have it) [D9a]
- [ ] The shirriff's warhorn (`LOTRInvasions.HOBBIT`), with invasions (D12)
  [D9a]
- [ ] The Bree captain's warhorn (`LOTRInvasions.BREE`), with invasions
  (D12) [D9d]
- [ ] The Rohirrim marshal's warhorn (`LOTRInvasions.ROHAN`: warriors 10,
  bowmen 5) and cape (`LOTRCapes.ROHAN`), with D12 and NPC capes [D9e]
- [ ] The Gondorian captain's warhorn (`LOTRInvasions.GONDOR`) and cape
  (`LOTRCapes.GONDOR`), the Tower Guard's cape (`LOTRCapes.TOWER_GUARD`),
  with D12 and NPC capes [D9f]
- [ ] Vintner Guards defending Dorwinion's vineyards
  (`LOTREntityDorwinionGuard.defendGrapevines`: warnings, attacks, guards
  called in and the `VINEYARD_STEAL_PENALTY` for picking grapes there), with
  the vineyard biome variant (D10) [D9g]
- [ ] Dorwinion's warhorns (`LOTRInvasions.DORWINION`, `DORWINION_ELF`) and
  the capes of the captains and vintner, with D12 and NPC capes [D9g]
- [ ] The travelling traders' spawner (`LOTRTravellingTraderSpawner`, which
  calls `startTraderVisiting`), with D12; until then a travelling trader
  never leaves [D9g]
- [ ] Galadhrim warriors called out against a player felling mallorn in
  Lothlórien (`LOTRBlockWood.removedByPlayer`, "defendTrees"), with the
  biomes (D10) [D9g]
- [ ] The elf lords' warhorns (`LOTRInvasions.GALADHRIM`, `HIGH_ELF_LINDON`,
  `HIGH_ELF_RIVENDELL`, `WOOD_ELF`) and the capes of the lords, traders and smiths
  (`LOTRCapes.GALADHRIM`, `HIGH_ELF`, `RIVENDELL`, `WOOD_ELF`, `GALADHRIM_TRADER`,
  `RIVENDELL_TRADER`, the smiths' `*Smith_cape`), with D12 and NPC capes [D9g]
- [ ] The fief captains' warhorns (`GONDOR_DOL_AMROTH`, `_LOSSARNACH`,
  `_PELARGIR`, `_PINNATH_GELIN`, `_BLACKROOT`, `_LEBENNIN`, `_LAMEDON`) and
  capes (`LOTRCapes.GONDOR`, `LOSSARNACH`, `PELARGIR`, `BLACKROOT`,
  `PINNATH_GELIN`, `LAMEDON`), with D12 and NPC capes [D9f]
- [ ] The branding iron in the Gondor and Harad farmers' buy pools (16 coins), with the
  branding iron item [D9f]
- [ ] The Mordor orc slaver (`LOTREntityMordorOrcSlaver`, hires Nurn slaves
  as farmers at +200, with the branding iron in hand), with the Nurn slaves
  [D9i]
- [ ] The Mordor orc commander's and Black Uruk captain's warhorns
  (`LOTRInvasions.MORDOR`, `MORDOR_BLACK_URUK`), with D12 [D9i]
- [ ] Orc spawning in darkness, the dwarven biomes' top-block rule, and the
  biomes where hostiles walk by day (no daylight slowness there), with the
  biomes (D10) [D9i]
- [ ] Orc achievements: `killMordorOrc`, `killBlackUruk`, `tradeOrcTrader`,
  `tradeOrcCaptain`, `tradeBlackUrukCaptain`, `killBombardier`,
  `hitByOrcSpear`; the Black Uruk shield (`ALIGNMENT_BLACK_URUK`) [D9i]
- [ ] The hired bombardier's bomb slot in the hired-warrior inventory, with
  that screen (D16) [D9i]
- [ ] The Gundabad and Angmar chieftains' warhorns (`LOTRInvasions.GUNDABAD`,
  `ANGMAR`), with D12 [D9i]
- [ ] Gundabad and Angmar achievements: `killGundabadOrc`, `killGundabadUruk`,
  `killAngmarOrc`, `tradeGundabadTrader`, `tradeGundabadCaptain`,
  `tradeAngmarTrader`, `tradeAngmarCaptain`; the Gundabad shield
  (`ALIGNMENT_GUNDABAD`) [D9i]
- [ ] The Dol Guldur chieftain's and spider keeper's warhorns
  (`LOTRInvasions.DOL_GULDUR`, `MORDOR_NAN_UNGOL`), with D12 [D9i]
- [ ] Dol Guldur and spider achievements: `killDolGuldurOrc`,
  `tradeDolGuldurTrader`, `tradeDolGuldurCaptain`, `tradeOrcSpiderKeeper`,
  `killMirkwoodSpider`, `killMordorSpider` [D9i]
- [ ] The spider climbing meter on the HUD (`shouldRenderClimbingMeter`), with
  the HUD [D9i]
- [ ] The Rhudaur hillman chieftain's warhorn (`LOTRInvasions.ANGMAR_HILLMEN`),
  with D12; `killAngmarHillman`, `tradeAngmarHillmanChieftain`; the Angmar
  shield (`ALIGNMENT_ANGMAR`) on warriors; mini-quests; spawning above y 62
  on the top block (D10) [D9i]
- [ ] Isengard: the Uruk chieftain's warhorn (`LOTRInvasions.URUK_HAI`, D12);
  `killIsengardSnaga`, `killUrukHai`, `tradeUrukTrader`, `tradeUrukCaptain`,
  and `raidUrukCamp` (killing an Uruk near an Uruk NPC respawner, with the
  respawner item); the Uruk-hai shield (`ALIGNMENT_URUK_HAI`) [D9j]
- [ ] Dunland: the warlord's warhorn (`LOTRInvasions.DUNLAND`, D12);
  `killDunlending`, `tradeDunlendingWarlord`, `tradeDunlendingBartender`; the
  Dunland shield (`ALIGNMENT_DUNLAND`) and the berserker's cape
  (`LOTRCapes.DUNLENDING_BERSERKER`) [D9j]
- [ ] Near Harad coast: the warlord's warhorn (`LOTRInvasions.NEAR_HARAD_COAST`,
  D12) and cape (`LOTRCapes.NEAR_HARAD`), the champion's cape
  (`SOUTHRON_CHAMPION`); `killNearHaradrim`, `tradeNearHaradWarlord`,
  `tradeNearHaradBlacksmith`, `tradeNearHaradMerchant`, `tradeHaradBartender`,
  `tradeHaradFarmer`, `tradeBazaarTrader`; the Near Harad shield
  (`ALIGNMENT_NEAR_HARAD`); mini-quests; spawning (D10) [D9k]
- [ ] Umbar and the Corsairs: the captains' warhorns (`NEAR_HARAD_UMBAR`,
  `NEAR_HARAD_CORSAIR`, D12) and the Umbar captain's cape; the Umbar and
  Corsair shields; `tradeUmbarCaptain`, `tradeUmbarBlacksmith`,
  `tradeCorsairCaptain`, `hireHaradSlave`; the branding iron in the Corsair
  slaver's hand and the Corsair chest (weight 25), with the branding iron
  [D9k]
- [ ] Harnedor: the warlord's warhorn (`NEAR_HARAD_HARNEDOR`, D12) and cape;
  the Harnedor shield; `tradeHarnedorWarlord`, `tradeHarnedorBlacksmith`,
  `hireHarnedorFarmer` [D9k]
- [ ] The Gulf: the warlord's warhorn (`NEAR_HARAD_GULF`, D12) and cape
  (`LOTRCapes.GULF_HARAD`); the Gulf shield (`ALIGNMENT_GULF`);
  `tradeGulfWarlord`, `tradeGulfBlacksmith`, `tradeHaradBartender`,
  `tradeHaradFarmer`, `tradeBazaarTrader`; mini-quests; the Gulf house's lore
  books and pouches [D9k]
- [ ] Rhûn: the warlord's warhorn (`LOTRInvasions.RHUN`, D12); the Rhûn shield
  (`ALIGNMENT_RHUN`); `killEasterling`, `tradeRhunCaptain`,
  `tradeRhunBlacksmith`, `tradeRhunBartender`, `tradeRhunMarketTrader`,
  `hireRhunFarmer`; the pull of the Rhûn lands and the y > 62 top-block spawn
  rule (D10); mini-quests; the Easterling house's lore books and pouches; the
  branding iron in the farmer's pool, with the branding iron [D9l]
- [ ] The Moredain: the chieftain's warhorn (`LOTRInvasions.MOREDAIN`, D12);
  the Moredain shield (`ALIGNMENT_MOREDAIN`) on warriors and mercenaries;
  `killMoredain`, `tradeMoredainChieftain`, `tradeMoredainVillager`,
  `hireMoredainMercenary`; the mercenary's screens and `LOTRPacketBuyUnit`'s
  mercenary branch (D16); the pull of Far Harad and the y > 62 grass/sand
  spawn rule (D10); mini-quests; the hut's lore books and pouches; bosses
  among the non-civilians of `isCivilianNPC`, with the bosses [D9m]
- [ ] The Tauredain: the chieftain's warhorn (`LOTRInvasions.TAUREDAIN`, D12)
  and cape (`LOTRCapes.TAURETHRIM`); the Taurethrim shield
  (`ALIGNMENT_TAUREDAIN`); `killTauredain`, `tradeTauredainChieftain`,
  `tradeTauredainShaman`, `tradeTauredainSmith`, `tradeTauredainFarmer`,
  `hireTauredainFarmer`; the pull of Far Harad and the y > 62 top-block
  spawn rule (D10); mini-quests; the house's lore books and pouches [D9m]
- [ ] The half-trolls: the warlord's warhorn (`LOTRInvasions.HALF_TROLL`, D12);
  the half-troll shield (`ALIGNMENT_HALF_TROLL`); `killHalfTroll`,
  `tradeHalfTrollWarlord`, `tradeHalfTrollScavenger`; mini-quests; the hired
  inventory's use of `canReEquipHired` (D16) [D9m]
- [ ] The trolls: `killTroll`, `killTrollFleeingSun`, `makeTrollSneeze`,
  `killOlogHai`, `killMirkTroll`, `killMountainTroll`, `killSnowTroll` (D7); the biomes where hostiles walk by day,
  which the sun does not trouble and where creatures of the dark are at home
  (`canSpawnHostilesInDay`, D10); conquest spawning's exemption from the
  dark (D12); the hired unit's icon and health bar over trolls (with the hire
  screens) [D9n]
- [ ] Ents and huorns: `killEnt`, `talkEnt`, `killHuorn`, `killDarkHuorn` (D7);
  the pull of Fangorn (the Old Forest for Dark Huorns) and the y > 62
  grass-or-dirt spawn rule (D10); the hired unit's icon and health bar over
  huorns (with the hire screens) [D9o]
- [ ] Wraiths and wights: the marsh wraiths rising in the Dead Marshes for
  whoever wades there (`LOTREventHandler.spawnMarshWraithIfConditionsMet`,
  with `wraith.spawn`) and the wight's y > 62 top-block spawn rule, with
  the biomes (D10); the darkening of the screen of a wight's quarry and the
  barrows' ambience (`LOTRTickHandlerClient`, `wight.ambience`, with the
  HUD); `killBarrowWight`, `killMarshWraith` (D7) [D9p]
- [ ] The nomads: the chieftain's warhorn (`NEAR_HARAD_NOMAD`, D12) and cape;
  `tradeNomadWarlord`, `tradeNomadArmourer`, `tradeNomadMerchant`,
  `tradeBazaarTrader`; ImmuneToHeat (D10); mini-quests; the nomad tent's lore
  books and pouches [D9k]
- [ ] The dwarf commanders' warhorns (`LOTRInvasions.DWARF`,
  `BLUE_MOUNTAINS`), with D12 [D9h]
- [ ] Dwarves' natural spawning (underground on lit rock below y 60, or one
  time in 200 anywhere; miners never above ground; wicked dwarves above
  y 62 on the biome's top block) and the dwarven mountains' pull on their
  wandering (`getBlockPathWeight`), with the biomes (D10) [D9h]
- [ ] The LOTR anvil's free mithril repair at a dwarf trader
  (`LOTRContainerAnvil`: `theTrader instanceof LOTREntityDwarf`), with the
  smith's anvil screen (D16) [D9h]
- [ ] The dwarven lore books in `DWARVEN_TOWER`, `BLUE_MOUNTAINS_STRONGHOLD`
  and `DWARVEN_MINE_CORRIDOR`, with lore books [D9h]
- [ ] Ruffians keeping clear of the Rangers of the North (12 blocks), with
  the Rangers [D9d]
- [ ] Ruffian spy's bounty help (paid off with coins, gold, silver, a gem or
  a ring) and the ruffian mini-quests, with D14; the thief cancelling the
  victim's fast travel, with D13 [D9d]
- [ ] `LOTREntityBandit` (and `LOTREntityBanditHarad`,
  `LOTREntityAINearestAttackableTargetBandit`), reusing `LOTRBandit` [D9d]
- [ ] The branding iron in the Bree farmer's buy pool (16 coins), with the
  branding iron item [D9d]
- [ ] Pickpocketing Bree-men and Bree-hobbits (`IPickpocketable`,
  `BREE_PICKPOCKET`), with the pickpocket mini-quest (D14) [D9d]
- [ ] The branding iron in the Hobbit farmer's buy pool (16 coins), with the
  branding iron item [D9b]
- [ ] Mercenaries (`LOTRMercenary`, `LOTRMercenaryTradeEntry`) and travelling
  traders (the visit itself is ported, D9g; each travelling trader comes with
  its faction); hired units hunting bandits (`LOTREntityBandit`) [D9b]
- [ ] Faction trade and hire counters (`LOTRFactionData.addTrade`/`addHire`)
  [D9b]
- [ ] Refusing pickpocketed coins and trade items (`IPickpocketable`), with
  the pickpocket mini-quest (D14) [D9b]
- [ ] Bounders hunt Bree ruffians (`LOTREntityAIHobbitTargetRuffian`) --
  only inside the Shire biome, so it waits for the biomes (D10); the
  ruffians themselves are ported [D9a]
- [ ] `LOTRRandomSkinsCombinatorial` (layered NPC skins), NPC capes and
  shields (`renderNPCCape`, `renderNPCShield`), `LOTRArmorModels` special
  armour models on NPCs, and the elf's bowing pose in `LOTRBipedModel`, each
  with the first NPC that uses it [D9a]
- [ ] Other mods' NPCs given a faction by config (`LOTREntityRegistry`),
  which `getNPCFaction` and the attack rules also read [D9a]
- [ ] Utumno's creatures, with Utumno (D15): the wargs (plain, ice, obsidian,
  fire; their rugs are already in) [D9c], orc and orc archer [D9j], troll
  and snow troll [D9n], ice spider [D9i], the Balrog and the tormented elf
  [D9r]. (`LOTREntitySauron` is never registered in the original, so it is
  not ported.)
- [ ] Utumno return portal sacrifice kill hook
  (`LOTRUtumnoReturnPortalBaseBlock`), with D15 [B4]

### D10 — dimension and world gen
- [ ] Hobbits' wandering drawn to the Shire biomes (`getBlockPathWeight`
  +20) and their natural spawn check (above y 62 on the biome's top block,
  `getCanSpawnHere`), with the biomes and D12 spawning [D9a]
- [ ] `LOTREntityFishHook`: swapped in for vanilla's hook in Middle-earth
  (LOTREventHandler), for the Middle-earth fishing loot [D3]
- [ ] Marsh lights over the Dead Marshes' water (the block exists, D1d) [D1]
- [ ] Fallen leaves scattered under trees (the blocks exist, D1b) [D1]
- [ ] Stalactites and stalagmites in caves (stone; ice and obsidian in their
  biomes) -- the blocks exist (D1) [D1]
- [ ] Bree-men's and Bree-hobbits' wandering drawn to Bree-land
  (`getBlockPathWeight` +20) and the Bree-men's natural spawn check (above
  y 62 on the biome's top block), with the biomes and D12 [D9d]
- [ ] Dorwinion's natural spawn check and pull towards Dorwinion, with the
  biomes and D12 [D9g]
- [ ] Elves' natural spawn check (`canElfSpawnHere`; High Elves above y 62
  on grass) and each people's pull towards Lothlórien, Lindon or Rivendell, with the biomes and D12 [D9g]
- [ ] Gondorians' wandering drawn to Gondor (`getBlockPathWeight` +20) and
  their natural spawn check, with the biomes and D12 [D9f]
- [ ] Rohirrim wandering drawn to Rohan (`getBlockPathWeight` +20) and their
  natural spawn check, with the biomes and D12 [D9e]
- [ ] The `RUFFIANS` spawn list (spy 10, brute 5, groups of 1–4), with the
  biomes and D12 spawning [D9d]
- [ ] `LOTRFaction.isFactionDimension` returns true until the dimension
  exists [B12a]
- [ ] Sapling tree growers are placeholders; grapes' ×1.6 Dorwinion bonus;
  ent jar herb brewing needs the Fangorn biome; wild yam [B3, B5d]

### D11 — structures
- [ ] The remaining `LOTRChestContents` pools (structure chests) →
  `LOTRChestContents` [B13b]

### D13/D14 — map, quests, conquest
- [ ] Bree mini-quests (`LOTRMiniQuestFactory.BREE`, its bounty help speech)
  [D9d]
- [ ] Dorwinion mini-quests (`DORWINION`, `DORWINION_ELF`) [D9g]
- [ ] Galadhrim, Lindon, Rivendell and Wood-elf mini-quests (`GALADHRIM`,
  `HIGH_ELF`, `RIVENDELL`, `WOOD_ELF`) [D9g]
- [ ] The jazz elf's music notes (the "music" particle) and its jazz
  (`LOTRAmbience`, `music.jazzelf`), and the saxophone drawn in its hands
  (`LOTRItemRing.saxIcon`) [D9g]
- [ ] Gondor mini-quests (`GONDOR`, the soldier's 1-in-8
  `GONDOR_KILL_RENEGADE`, the renegade's `GONDOR_RENEGADE` at +50, 1 in 4000)
  [D9f]
- [ ] Rohan mini-quests (`ROHAN`, and `ROHAN_SHIELDMAIDEN` offered at +150,
  1 in 4000) [D9e]
- [ ] NPC mini-quests: `createMiniQuest` (the Hobbit factory), the bounty
  help speech bank, and the quest book and offer icons over NPCs
  (`LOTRNPCRendering.renderQuestBook`/`renderQuestOffer`) [D9a]
- [ ] Table of command map, squadron and conquest screens [B4, B5e]
- [ ] Mini-quests and bounty quests: `createMiniquestBonus` and
  `notifyMiniQuestsNeeded` callers; conquest (`LOTRConquestGrid`,
  `onConquestKill`) [B12]

### D16 — GUIs, HUD, fx (user: HUD in a later release, after entities and structures)
- [ ] Hired units' icon and health bars over their heads
  (`LOTRNPCRendering.renderHiredIcon`/`renderNPCHealthBar`) [D9a]
- [ ] The trade, unit-trade and hired-unit screens with their containers and
  packets (`LOTRGuiTrade`, `LOTRContainerTrade`, `LOTRGuiUnitTrade`,
  `LOTRGuiHiredWarrior`/`Farmer`/`Dismiss`, `LOTRPacketTraderInfo`,
  `LOTRPacketHiredGui`/`UnitCommand`/`UnitDismiss`/`UnitInteract`,
  `LOTRPacketNPCSquadron`), the replaced-item store they use
  (`LOTRInventoryHiredReplacedItems`), and an NPC keeping still while one is
  open (`checkGUIOpenAndNavigation`); naming a squadron on an item
  (`LOTRGuiSquadronItem`); the mount inventory screen through which a tame
  warg is saddled and barded (`openGUI`, gui 29 -- until then no player can
  saddle one); the command sword's location marker
  (`LOTRPacketLocationFX`); the one-time Friendly Fire notice and the Options
  screen toggles (Friendly Fire, hired death messages) [D9b]
- [ ] HUD alignment bar and `LOTRAlignmentTicker`; alignment-drain notice
  (`LOTRPacketAlignDrain`); floating gain/loss popup
  (`LOTREntityAlignmentBonus`, `sendAlignmentBonusPacket`, and with it the
  position arguments to `addAlignment`) [B12c]
- [ ] Full `LOTRGuiFactions` (the port's screen is a list with pledge and
  unpledge only: pledge requirements and descriptions, break-cooldown text,
  enemies preventing a pledge, rank limits, map) [B12c]
- [ ] Alignment shown above other players' heads (data already synced to
  trackers) [B12b]
- [ ] Chilling's FROST screen overlay (`LOTRModifierSpecials`) [B11]
- [ ] Worn plate's food pile and `LOTRPlateFallingInfo`
  (`LOTRPlateHeadRenderer`) [B9]
- [ ] Leaf particles (user: "leave the leaves for now") [B4]; also the
  Galadhrim trader's burst of gold leaves on death and departure, and the
  Wood-elf scout's green leaves as it vanishes [D9g], the gold leaves an
  Ent lets fall as it heals a corrupt mallorn [D9o], and the Mallorn Ent's
  gold leaves (falling from its crown, swirling as it rises, bursting as it
  dies, and ringing its leaf bomb) [D9q]
- [ ] Faction recipe unlock toasts still fire (`showNotification`) [B6a]
- [ ] The menu key: the original's **L** (`LOTRKeyHandler.keyBindingMenu`,
  key 38) opened the LOTR menu, and from it the factions screen.
  `LOTRKeyBindings` is commented out, so `LOTRScreenFactions` (pledge and
  unpledge included) cannot be opened in game [C4]
- [ ] Reeds' lowest segment on solid ground should use `reeds_lower` /
  `dried_reeds_lower`, as `LOTRRenderBlocks.renderReeds` did; the blockstate
  has only `top`/`mid` [C4]

## Suggested order

A1–A3 → B1 → (B2…B15 interleaved with the matching D1 batches) → C1 →
D2–D6 → C2/C3 → D8 → D9 → D11 → GUIs (D16's screens, HUD) → D10 → D12–D16
(user: structures and UI before world gen). Clean-up C1 early because it is cheap
and makes every later diff easier to read; C2 waits until the audit is done.

**User (at D5): mobs first.** After D5, D8 (animals and mounts) and D9 (NPCs)
come before D6, D7 and the rest. GUI work still waits for its later release.

---

## Findings backlog

Append here: `- [unit] file:line — description (confirmed|suspected)`.

- [A1] 43 `vanilla?` rows in `docs/parity/items_blocks.csv` need the user's
  yes/no; record answers as `vanilla` rows in `tools/parity_overrides.csv`.
- [A3] Elven blade glow textures (`sting_glowing`, `glamdring_glowing`,
  `ringil_glowing`, `sword*/dagger*Elven*_glowing` …) are not in the port, which
  suggests the old "glows near orcs" behaviour isn't ported. (resolved in D9i-b: `LOTRElvenBladeItemModel`)
- [A3] Dye/ornament overlay layers not ported: `bodyKaftan_overlay`,
  `legsKaftan_overlay` (armour), `helmetHaradRobes_ornament`,
  `leatherHat_feather`, `armor/kaftan_*_overlay`. Suggests dyeable kaftan/robe
  ornaments and hat feathers render without their overlay. (suspected; B9)
- [A3] Old vessel icons `drink_mug`, `drink_goblet*`, `drink_horn*`,
  `drink_skin`, `drink_skull`, `drink_bottle`, `drink_glass`, `drink_clay` are not
  in the port — check how the port draws drink vessels. (suspected; B10)
- [A3] `brick5_gondorRustic_00/01/10/11`, `wasteBlock_var1–7`,
  `haradFlower_redALT`, `utumnoBrick_fireTile_side`, `utumnoBrick_iceGlowing_top`,
  crafting-table `side1/side2` faces, `birdCage_wood_base`, `boneBlock`,
  `driedReeds` are missing — random/alternate texture variants may be lost.
  (suspected; B2/B4)
- [A3] Unreferenced port files: `block/dried_reeds_lower`, `block/reeds_lower`,
  `block/marzipan_chocolate`, `entity/troll/outfit_0..2` (stone troll outfits
  not rendered?), `item/{acacia,birch,dark_oak,jungle,spruce}_door` (vanilla
  duplicates — delete if the doors are confirmed vanilla). (suspected; C4)
- [A2] `tools/parity_class_overrides.csv` is empty; B units should add
  rows there as they confirm absorbed/merged classes so the ledger converges.

- [survey] `LOTRMod.java` — `MOD_ID` is `"lord_of_the_rings - middle_earth"`
  (spaces) while fabric.mod.json id is `lord_of_the_rings_-_middle_earth` and the
  namespace is `lotr`; verify nothing uses `MOD_ID` as an identifier. (suspected)
- [survey] `fabric.mod.json` — empty `description`, `authors`, `contact`. (confirmed, cosmetic)
- [survey] No tests or CI. Consider fabric-gametest for block-entity save/load
  and recipe checks. (suggestion)
- [B1] DONE `fabric.mod.json` — `description`, `authors`, `contact`, `icon`
  are empty. The original's `mcmod.info` says "The Lord of the Rings Mod:
  Bringing Middle-earth to Minecraft", credits Hummel009 (URL
  github.com/Hummel009), logo `assets/lotr/logo.png`. The user should decide
  what to put, since the port has its own author too. Icon later changed by
  the user to the ring image, `assets/lotr/icon.png` (the wide `logo.png`
  stays for anything that wants the banner).
- [B1] DONE `fabric.mod.json` — add `"java": ">=25"` to `depends`, so a
  wrong JVM gives a clear loader error (the build targets Java 25 and the mixin
  config says JAVA_25).
- [B1] DONE (unified on `lotr`; generated files moved, `RECIPE_NAMESPACE = LOTRMod.NAMESPACE`) recipes lived in two namespaces: datagen writes
  `data/lord_of_the_rings_-_middle_earth/recipe` + `advancement`
  (`LOTRTranscribedRecipes.RECIPE_NAMESPACE`), hand-written ones are in
  `data/lotr/recipe`. Unifying on `lotr` would change recipe IDs (recipe-book
  unlocks in existing test worlds reset). User's call.
- [B1] CLEAN-UP (C4) `src/client/resources/lord_of_the_rings_-_middle_earth.client.mixins.json`
  is empty, its package `mixin.client` does not exist, and `fabric.mod.json`
  never lists it. Dead file; delete or register when a client mixin is needed.
- [B1] CLEAN-UP (C1) `LOTRMod` and `LOTRModClient` use inline fully qualified
  names (`...item.LOTRAlcoholTolerance.init()`, `...client.render.LOTRMugRenderer::new`,
  ...). Also move `LOTRMillstoneRecipes.createRecipes()` below `LOTRItems.init()`
  to mirror the original, where recipes were built after registration.
- [B1→B2] DONE Fire info vs old `LOTRMod.load()` (confirmed by reading
  `LOTRBlockBehaviours.init`): missing entries for wood bars (5/20, e.g.
  `galadhrim_wood_bars`, `high_elf_wood_bars`), reed bars (60/20), the grass
  plants / Mordor grass / Mordor moss (60/100), grass-material slabs and stairs
  such as thatch slab/stairs (60/20 — the port only tests `isWood`), and daub
  (40/40). Double flowers need checking against `ALL_FLOWERS`.
- [B1→B2] DONE Harvest levels from `LOTRMod.load()` (`setHarvestLevel`) need checking
  against `mineable/*` and `needs_*_tool` tags: e.g. silver/mithril/quendite/
  gulduril/gem ores and several storage blocks are level 2 (iron), copper/tin/
  morgul iron level 1 (stone); dirt-like blocks are shovel.
- [B2→B4] DONE `termite_mound` / `infested_termite_mound` use SAND sound; the
  original `LOTRBlockTermite` never calls setStepSound, so it had stone.
  (confirmed)
- [B2→B3] DONE `LOTRBlockWaste` picks one of `wasteBlock_var0..7` at random per
  position (`randomIcons`); the port has none of those textures. (confirmed)
- [B2] `brick5_gondorRustic_00/01/10/11.png` are unused by the original code too,
  so A3's note about them can be ignored.
- [B2] ACCEPTED by the user, keep as is: composting (`LOTRBlockBehaviours.composting`) has no
  original counterpart — 1.7.10 had no composter — so its values are the port's
  own. Note that reeds, corn, grapevine and riverweed sit in `ALL_FLOWERS` and
  so compost at 0.65 like flowers.
- [B3] DONE (user: restore 3) `LOTRCornBlock.MAX_HEIGHT` is 2 with the comment "Deliberately
  2, not LOTRBlockCorn.MAX_GROW_HEIGHT's 3". The original grows 3-high corn
  (two ear-bearing stalks). No reason is recorded; keep 2 or restore 3?
- [B3] NOTE reeds were redesigned on request (rooted underwater, grow up out of
  it; the original sat on top of the water). Bone meal on reeds and the berry-
  pick sound on grape harvest are port additions. Left as they are.
- [B3] Grapevine: when a vine loses its support the original dropped its
  grapes/seeds before reverting to a post; the port reverts silently
  (`updateShape` cannot drop). The vine's selection box also widened with age
  (0.1875 → 0.375 half-width). Breaking a vine uses the vanilla crop loot shape,
  not `getVineDrops`' age-scaled seed chance. (confirmed, minor)
- [B3→D10] Saplings use placeholder `TreeGrower`s and cannot grow until the
  LOTR tree features exist. Grapes' ×1.6 Dorwinion growth bonus needs the
  biome. Yam crop metadata 8 (wild yam on grass) belongs to world gen.
- [B3→D7] Achievements `harvestGrapes` and `pickBanana` fire from these blocks.
- [B4] DEFERRED by the user ("leave the leaves for now") — leaf particles. `LOTRBlockLeaves` (gold mallorn, mirk-oak,
  red) and `LOTRBlockLeaves7` (green) drop falling-leaf particles drawn from
  cells of the sheet `old mod/.../assets/lotr/misc/particles.png`. Modern
  particles need one PNG per sprite, i.e. cropping cells of that sheet into
  new files (pixels unchanged, but new images). Allowed?
- [B4] DELIBERATE (port choices, left in place; user may review):
  Khamûl's fire-jar blast breaks no blocks and chains jars within 3 blocks
  explicitly (original's blast broke blocks and chained through that); the
  fire has an age ceiling and generations (original only had the 1-in-12
  burn-out); beacon adds campfire-style lava sparks and a steady smoke column
  (original had neither); gates and totems refuse pistons
  (original pushable); open full-block Dwarven doors let light through
  (original's lightOpacity 255 held even when open); Dwarven doors adopt the
  facing of a door they are placed against (needed to build ithildin doors by
  hand).
- [B4→D] Needs other systems: termite mound spawning termites (mob), quagmire
  letting spiders through, Dwarven door / grape / banana / beacon achievements
  (D7), banner protection smothering Khamûl's fire, the table of command's
  squadron and conquest screens, kebab stand keeping meat in its item (original
  stored it in NBT; now it drops).
- [B4→B5] Block-entity behaviour itself (dart trap firing, forge smelting,
  kebab cooking, animal jar, weapon rack contents) is B5's audit.
- [B5a→D7] Unsmeltery `unsmelt` achievement on taking the output.
- [B15→B6] DONE (user: fix it) Recipe namespace: B1 is marked DONE ("unified on `lotr`"), but
  `LOTRRecipeProvider`'s mechanical recipes (planks, beams, smooth stone,
  cut blocks, and smelting) are saved without an explicit id, so Fabric files
  703 of them under `lord_of_the_rings_-_middle_earth` (in the 0.2.2 jar
  too). 49 of those share result and type with a recipe under `lotr` (e.g.
  `mud_slab`, `naurite_from_smelting`, `sulfur_from_smelting`) and may be
  duplicates. Not changed: recipe IDs are the user's call (B1). (confirmed)
  Fixed: `LOTRRecipeProvider.getRecipeIdentifier` returns the `lotr`
  namespace, so all 1,532 generated recipes are `lotr:` (703 moved, no path
  collisions, nothing else in the generated output changed, and nothing
  referred to the old ids). The duplicates were between datagen and older
  hand-written JSON under `src/main/resources/data/lotr/recipe`: 38
  hand-written recipes that datagen already produced in equivalent form were
  deleted (and removed from git). They were the cracked bricks/pillars,
  scorched stone, ore smeltings, salt, dried reeds, mud slab and three
  faction shish kebabs. The millstone's two recipes (iron and bronze) are
  both original. `parity_recipes.py`: 2,089 present, 0 missing.
- [D9d] `LOTRNPCEntity` has no `getBaseExperienceReward`, so an NPC without
  its own override gives vanilla's 0 XP; the original's
  `LOTREntityNPC.getExperiencePoints` gave 4–6 to every NPC that did not
  override it. The Bree-men override it themselves; the other NPCs (wargs
  among them) are not checked. (confirmed)
- [D9d] Traders' rabbit stew: user decision, they sell vanilla `Items.RABBIT_STEW` (Hobbit bartender and Bree innkeepers), not `lotr:rabbit_stew`. (resolved)
- [D9g] `LOTREntityHobbitOrcharder.shouldTraderRespawn()` returned false in
  the original, so an orcharder brought back by a respawner is not brought
  back again. The port's respawner now reads `shouldTraderRespawn()`, but
  `LOTRHobbitOrcharderEntity` did not override it, so it kept respawning.
  (resolved: it now returns false)
