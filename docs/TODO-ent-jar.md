# TODO — Ent Jar: the Ent-draught half

The Ent Jar block is ported and working as a **water vessel**. Everything to do
with **Ent-draughts** is deferred, because every branch of it depends on
something that does not exist in the port yet.

This file is the spec for finishing it. All original sources are under
`old mod/src/main/java/lotr/`.

---

## Status

### Done

| Piece | Where |
|---|---|
| Block, shape, support rules, drop-when-unsupported | `common/block/LOTREntJarBlock.java` |
| Block entity: `drinkMeta` + `drinkAmount`, save/load, client sync | `common/blockentity/LOTREntJarBlockEntity.java` |
| Rain filling (1-in-2500 per tick while rained on) | `LOTREntJarBlockEntity.serverTick` |
| Bucket fill / drain | `LOTREntJarBlock.useItemOn` |
| Liquid-surface renderer, biome-tinted water | `client/render/LOTREntJarRenderer.java` |
| Hollow open-topped model, item model, loot, lang | `assets/lotr/models/block/ent_jar.json` etc. |

### Deferred

| Missing thing | What it blocks |
|---|---|
| `LOTRItemEntDraught` (7 subtypes) | bowls in/out, draught tint on the liquid, the particles |
| Fangorn herbs (`fangornPlant` ×6, `fangornRiverweed`) | brewing |
| `LOTRBiomeGenFangorn` | brewing (the biome gate) and natural jar generation |
| `LOTRItemMug.Vessel` | mug / skin / goblet fill and drain |
| Huorn entity + `LOTRHiredNPCInfo` | the golden draught's summon |
| Achievements | `drinkEntDraught`, `summonHuorn` |
| `lotr:item.mug_fill` sound | currently substituted with vanilla `BOTTLE_FILL` |

**No data migration will be needed.** `drinkMeta` already persists and syncs
with the original's `-1`-means-water sentinel, so jars saved today will pick up
draught support without touching their NBT.

---

## The full cycle in 1.7.10

Source: `common/block/LOTRBlockEntJar.java`, `common/tileentity/LOTRTileEntityEntJar.java`.

1. **Fill with water** — rain, a bucket (6 units at once), or any mug vessel
   (1 unit). Capacity is 6.
2. **Brew** — right-click the water-filled jar holding a Fangorn herb, *while
   standing in the Fangorn biome*. The whole jar converts to that draught,
   keeping its level. Plays `playAuxSFX(2005, …)` — the bone-meal particle burst.
3. **Draw off** — right-click with an empty bowl to take one draught (returns a
   filled bowl); right-click holding a bowl of draught to pour one back in.
   Mixing two different draughts is refused.

A jar holding a draught (not water) emits `happyVillager` particles, 1-in-4 per
random display tick, from `y + 1.0`. Already implemented and gated on
`!holdsWater()` in `LOTREntJarBlock.animateTick` — it will start working on its
own the moment a draught can be set.

---

## Ent-draught item spec

`common/item/LOTRItemEntDraught.java`. Stack size 1, `hasSubtypes`, drink
animation over 32 ticks, creative tab `tabFood`. Drinking returns an empty bowl
unless in creative.

Durations in `DraughtInfo.addEffect(id, seconds)` are **seconds** — it multiplies
by 20 internally.

| Meta | Name | Hunger | Saturation | Effects |
|---|---|---|---|---|
| 0 | green | 0 | 0.0 | Speed 120s, Haste 120s, Strength 120s |
| 1 | brown | 20 | 3.0 | — |
| 2 | gold | 0 | 0.0 | — (see below) |
| 3 | yellow | 0 | 0.0 | Regeneration 60s |
| 4 | red | 0 | 0.0 | Fire Resistance 180s |
| 5 | silver | 0 | 0.0 | Night Vision 180s |
| 6 | blue | 0 | 0.0 | Water Breathing 150s |

**The alignment sting:** drinking any draught with **Fangorn alignment < 0**
grants Poison for 100 ticks *instead of* the food and effects. `LOTRFaction.FANGORN`
and the alignment system are already ported, so this part can be written today.

**The golden draught** is not drunk for effect — it is *used on a sapling*
(`onItemUse`) to summon a Huorn:

- requires Fangorn alignment ≥ 500, else `notifyAlignmentNotHighEnough`
- the sapling must match an entry in `LOTREntityTree.SAPLING_BLOCKS` /
  `SAPLING_META` (meta masked `& 7`)
- spawns `LOTREntityHuorn` with that tree type, persistent, spawn restrictions
  lifted, hired to the player as `Task.WARRIOR` with
  `alignmentRequiredToCommand = 500`
- 24 `happyVillager` particles, and consumes the draught into a bowl

Textures are already in the old mod at
`old mod/src/main/resources/assets/lotr/textures/items/entDraught_<name>.png`
(green, brown, gold, yellow, red, silver, blue) — copy them in as
`ent_draught_<name>.png` per the port's snake_case convention.

Lang names, from `en_US.lang` lines 1627–1633: "Green Ent-draught", "Brown
Ent-draught", "Golden Ent-draught", "Yellow Ent-draught", "Red Ent-draught",
"Silver Ent-draught", "Blue Ent-draught".

---

## Brewing recipes

`common/recipe/LOTREntJarRecipes.java` — a flat herb → draught map:

| Ingredient | Result |
|---|---|
| `fangornPlant` meta 0–5 | `entDraught` meta 0–5 (same index) |
| `fangornRiverweed` | `entDraught` meta 6 (blue) |

---

## Where each piece plugs into the port

- **`LOTREntJarBlockEntity.fillFromBowl(int draughtMeta)`** and
  **`brew(int draughtMeta)`** are already written with the original's exact
  bodies and are currently unused. They need callers, not changes.
- **`LOTREntJarBlock.useItemOn`** holds the two bucket branches. The bowl,
  mug-vessel and brewing branches go alongside them; the original's ordering is
  worth preserving — bowl-in first, then bowl-out, then brew, then water.
- **Mug vessels** are the same two bucket branches with capacity 1 instead of
  `MAX_CAPACITY`, so they generalise rather than duplicate.
- **`LOTREntJarRenderer.extractRenderState`** picks the sprite and tint. The
  draught branch is marked with a comment: the original sampled a single pixel
  (u/v 7→8) from the middle of the draught's item icon and used it as a flat
  colour, rather than using the icon as a texture.

---

## Natural generation

`common/world/feature/LOTRWorldGenEntJars.java`. Not portable until biomes are.
16 placement attempts per call: offset ±6 in x/z and ±2 in y, requires grass
directly below, a non-normal-cube at the spot, `getPrecipitationHeight == y`
(i.e. open to the sky), and the biome to be Fangorn. Each placed jar is filled
with a random 0…6 units of water.

---

## Settled deviation — water duplication

`LOTRBlockEntJar` gated the bucket-drain on `drinkAmount > 0` and then called
`consume()` six times, clamped at zero — so a jar holding a *single* unit still
handed back a full water bucket, duplicating water out of nothing.

**Decided: keep the fix.** The port requires the jar to be full before it will
fill a bucket. Do not restore the original test. When the mug vessels are added
they take 1 unit each and so are unaffected.

---

## Related deferred work found while porting the dwarven doors

Not Ent Jar work, but noted here so it is not lost:

- **`LOTRAchievement.useDwarvenDoor`** — dropped from `LOTRDwarvenDoorBlock`;
  the port has no achievement system.
- **Ithildin door recipe** — `XX / XY / XX` with stone and an `ithildin` item
  at the centre-right. The plain dwarven door's six-stone recipe is emitted;
  this one waits on the ithildin item.
- **Dwarven door facing inheritance** is a deliberate deviation from 1.7.10 —
  see the javadoc on `LOTRDwarvenDoorBlock.getStateForPlacement` for why.
