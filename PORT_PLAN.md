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
| Projectiles | 19 | 11 ported; missing spear, fish hook/fishing, mallorn leaf bomb, marsh wraith ball, smoke ring, thrown rock, thrown termite, troll snowball |
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
    animation, sync and summon geometry match; the summon raises a CHICKEN
    stand-in until `LOTREntityMountainTrollChieftain` exists (D8/D9). Stale
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
- [ ] **B13 Entities ported so far** (projectiles, boss trophy, stone troll,
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
- [ ] **B14 Networking & client/server split.** All payloads validate input
  server-side (sign edit, beacon edit, horn mode, brewing button); no client
  classes referenced from `src/main`. *Result:*
- [ ] **B15 Creative tabs, lang, tags.** Every registered item in exactly the
  right tab in old order; no missing/duplicate lang keys; tag files match usage.
  *Result:*

## Track C — Code clean-up (behaviour-neutral only)

Run these after the relevant B unit so a refactor never hides a bug fix.

- [ ] **C1 Imports.** 179 files use fully qualified `net.blueskiez77...` names
  inline (e.g. `LOTRMod.onInitialize`). Replace with imports. Pure mechanical.
- [ ] **C2 Split the giant registries.** `LOTRBlocks` (3218 lines), `LOTRItems`
  (2837), `LOTRCreativeTabs` (1314), `LOTRToolMaterials` (1457) → split by theme
  (e.g. `LOTRBuildingBlocks`, `LOTRWoodBlocks`, `LOTRFactionItems`), keeping IDs
  and registration order identical. *Suggest-first — confirm the split scheme
  with the user before doing it.*
- [ ] **C3 Datagen coverage.** Move hand-written JSON (models, loot, tags,
  recipes) into the datagen providers where they already exist, or document why
  a file is hand-written. Delete generated duplicates.
- [ ] **C4 Dead code & orphans.** Unused classes/methods/assets from A3.
- [ ] **C5 Comment hygiene.** Keep "old class ↔ new class" mapping comments,
  remove stale ones.

## Track D — Continue porting (baseline feature parity)

Ordered by dependency. Each bullet is one or more units; split as needed.

- [ ] **D1 Remaining items/blocks** from A1 `missing` rows, batched by faction
  (≈10–30 items per unit), with all their systems.
- [ ] **D2 Missing tile entities**: bookshelf storage, flower pot, spawner
  chest, mob spawner, gulduril, corrupt mallorn, ithildin carved sign.
- [ ] **D3 Missing projectiles & dispenser behaviours**: spear, thrown rock,
  thrown termite, troll snowball, smoke ring (pipe), mallorn leaf bomb, marsh
  wraith ball, fishing.
- [ ] **D4 Decorative entities**: rugs (wargskin/bear/lion/giraffe), banners as
  entities + banner protection, barrel entity, falling treasure.
- [ ] **D5 Player data foundation**: port `LOTRPlayerData`/`LOTRLevelData` as
  Fabric attachments + SavedData (alignment already partly there — unify),
  with sync packets. Prerequisite for everything below.
- [ ] **D6 Config** (`LOTRConfig`, client options).
- [ ] **D7 Titles, capes, shields, achievements** (achievements → advancements
  or custom tracker; decide with user).
- [ ] **D8 Animals & mounts** (`entity/animal`): horses, wargs, elk, boars,
  rhinos, camels, etc. — makes mount armour functional.
- [ ] **D9 NPC core**: `LOTREntityNPC` base, AI goals (`entity/ai`), hiring,
  trading, speech banks, NPC renderers/models. Then factions one per unit
  (Hobbits → Bree → Rohan → Gondor → Elves → Dwarves → Orcs/Mordor → Harad →
  Rhûn …).
- [ ] **D10 Dimension & world gen**: Middle-earth dimension, biome registry
  from `world/biome` + variants, genlayer → map-image-driven biome source,
  features (trees for saplings!), ores, `world/feature`. Large; plan it in its
  own session first and add sub-units here.
- [ ] **D11 Structures & villages** (`structure`, `structure2`, `village`,
  mapgen dwarven mines/tpyr). Depends on D9/D10.
- [ ] **D12 NPC spawning, invasions, spawn damping** (extend `LOTRInvasions`).
- [ ] **D13 Map & waypoints / fast travel** (map GUI, `world/map`).
- [ ] **D14 Quests, fellowships, mini-quests, Grey Wanderer.**
- [ ] **D15 Portals**: elven/morgul/utumno portals, Utumno dimension.
- [ ] **D16 Remaining GUIs, commands, particles/fx, sounds, drunken speech.**

## Suggested order

A1–A3 → B1 → (B2…B15 interleaved with the matching D1 batches) → C1 →
D2–D6 → C2/C3 → D8 → D9 → D10 → D11–D16. Clean-up C1 early because it is cheap
and makes every later diff easier to read; C2 waits until the audit is done.

---

## Findings backlog

Append here: `- [unit] file:line — description (confirmed|suspected)`.

- [A1] 43 `vanilla?` rows in `docs/parity/items_blocks.csv` need the user's
  yes/no; record answers as `vanilla` rows in `tools/parity_overrides.csv`.
- [A3] Elven blade glow textures (`sting_glowing`, `glamdring_glowing`,
  `ringil_glowing`, `sword*/dagger*Elven*_glowing` …) are not in the port, which
  suggests the old "glows near orcs" behaviour isn't ported. (suspected; B7)
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
