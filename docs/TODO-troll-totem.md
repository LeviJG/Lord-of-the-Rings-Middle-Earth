# TODO — Troll Totem: the summon

The three totem blocks are ported and working: they place, orient, align each
other into a column, animate the jaw, and accept the bone. The one thing they
cannot do is **produce the troll**, because the port has no entities at all yet.

Original sources under `old mod/src/main/java/lotr/`.

---

## Status

### Done

| Piece | Where |
|---|---|
| Three blocks (head / body / base) with FACING | `common/block/LOTRTrollTotemBlock.java` |
| Column alignment on placement | `LOTRTrollTotemBlock.setPlacedBy` |
| Readiness test: night + open sky + complete column | `LOTRTrollTotemBlockEntity.computeCanSummon` |
| Jaw animation, and its one-boolean sync | `LOTRTrollTotemBlockEntity.tick` |
| Bone + negative-Angmar-alignment gate | `LOTRTrollTotemBlock.useItemOn` |
| Model, renderer, texture, lang, loot | `client/model/`, `client/render/` |
| Spawn plumbing — **spawns a chicken** | `LOTRTrollTotemBlockEntity.summon` |

### Deferred

| Missing | What it blocks |
|---|---|
| `LOTREntityMountainTrollChieftain` | **the summon spawns a chicken placeholder** |
| Per-face UV rotation on item models | geometry and face regions exact; in-face rotation not reproduced |
| `LOTRWorldGenGulfTotem` / `gulf_totem.strscan` | natural generation |

---

## ⚠ PLACEHOLDER: the totem summons a chicken

`LOTRTrollTotemBlockEntity.summon()` is wired **end to end** — the three blocks
are consumed, and a mob is created, positioned at the base block with a random
yaw, finalised and added to the world. Everything is real except *what* it
spawns:

```java
private static final EntityType<? extends Mob> SUMMONED = EntityTypes.CHICKEN;
```

This is deliberate, so the whole chain (readiness → bone → alignment → consume →
spawn) can be tested today. **When entities are ported, changing that one line
to the Mountain Troll Chieftain's type is the entire fix** — the positioning,
the `finalizeSpawn` call standing in for `onSpawnWithEgg(null)`, and the
block-side gate all stay as they are.

Grep for `SUMMONED` and for "PLACEHOLDER" in that file to find it.

---

## What the totem does in 1.7.10

A three-block stone idol of a troll: a plinth with legs (**base**), a torso with
arms (**body**), and a horned skull with a hinged jaw (**head**). Trolls raise
them in the hills; one also appears in the Gulf structure.

**Summoning**, from `LOTRBlockTrollTotem.onBlockActivated` and
`LOTRTileEntityTrollTotem`:

1. The block clicked must be the **head**.
2. `canSummon()` must hold — **all** of:
   - it is night (`isDaytime()` false),
   - the head can see the sky,
   - a **body** sits directly below and a **base** below that, and all three
     share the same rotation.
3. The player's **Angmar alignment must be below zero** — an enemy of Angmar.
   Trolls do not answer their own kind's friends.
4. The held item must be a bone (ore-dict name `"bone"`).

One bone is consumed, the three blocks vanish, and a
`LOTREntityMountainTrollChieftain` spawns at the base position with a random
yaw, initialised via `onSpawnWithEgg(null)`.

The **jaw** is the visual tell: `updateEntity` ramps `jawRotation` 0→60 over
three seconds whenever `canSummon()` is true and back down when it is not, and
`getJawRotation` maps that to 0→−35°. So the idol's mouth gapes open at night
when the totem is complete and under open sky — that is your cue that it is
ready, before you spend a bone.

Note the original rotated the **head** by that angle, not the jaw part, so the
skull tips back and the mouth opens upward. `LOTRTrollTotemModel.renderHead`
reproduces that, deliberately.

---

## To finish it

Replace the `SUMMONED` constant above with the chieftain's `EntityType`. That
is the whole change; the surrounding `summon()` body already does what
`LOTRTileEntityTrollTotem.summon` did.

---

## Deviations to be aware of

**Three blocks, not one with metadata.** 1.7.10 packed part into metadata bits
0–1 and rotation into bits 2–3 of a single `trollTotem` block. The port splits
the parts into three registered blocks with a normal `FACING` property, matching
how every other metadata family here has been handled. Player-visible behaviour
is the same: three items, each dropping itself, and placing any one re-aligns
the column.

**The item models are generated block models, not the real render.** The
original drew the actual 3D model in the inventory via `renderInvTrollTotem`,
and there is no hook for that now: `SpecialModelRenderers.bootstrap()` builds a
hard-coded codec dispatch, so a custom special renderer would need a mixin.

Vanilla no longer uses one for beds either. `assets/minecraft/items/red_bed.json`
is a `minecraft:composite` of the plain block models `red_bed_head` /
`red_bed_foot`, and `template_bed_head.json` gives every face an explicit `uv`
rect. The totem follows the same pattern via
`tools/gen_totem_item_models.py`, which converts both the geometry AND the
texturing out of `LOTRModelTrollTotem`:

* **Geometry** — the boxes through the renderer's own
  `translate(0.5, 1.5, 0.5)` + `scale(1, -1, -1)`. That scale is a 180 degree
  rotation about X (determinant +1), not a mirror, so it bakes into the
  coordinates cleanly.
* **Texturing** — every face gets the rect the entity model's box unwrapping
  gives it, so the skull's front wears the carved face and the jaw's upper
  surface wears the mouth cavity.

Two things about entity UV layout are easy to get backwards, and both were,
first time round — the head came out visibly wrong while the near-featureless
body and base looked fine:

* **Entity models are built with +Y pointing DOWN.** The first cell of the
  layout's top row is therefore the box's *minY* face — `Direction.DOWN` — even
  though it is what you see looking down on the model. Confirmed against this
  sheet: the jaw's first cell is the dark mouth cavity, which is the jaw's
  *upper* surface, and the skull's second cell is its hollow underside. Getting
  this backwards put the mouth cavity on top of the head.
* **The bottom row wraps as right, front, left, back.** A player skin lays a
  head out that way and the player's right is −X, so the cells are
  minX/`WEST`, minZ/`NORTH`, maxX/`EAST`, maxZ/`SOUTH` — not east-first.

The world transform then swaps `up`↔`down` and `north`↔`south` (it is a 180°
rotation about X), and mirrored limbs swap `east`↔`west`.

Three checks say this is right: all 66 face rects land in-bounds and fully
opaque on a sheet that is only half covered; the skull's front rect contains
symmetric eye marks with a mouth band along its bottom edge; and the brow horn
sticks out of the very face that rect lands on.

Not reproduced: the per-face 180° UV *rotation* on the east/west faces, and the
u-flip on mirrored parts. Those faces are generic stone. The front face needs no
rotation — its texture-top maps to block-up — which is what matters, since it is
the only one with recognisable detail.

**If the model's boxes ever change, re-run that script** — it is the only thing
keeping the item models in step with `LOTRTrollTotemModel.createLayer()`, and
nothing will warn you if they drift.

**Bones are matched by item + tag.** The original used the ore dictionary name
`"bone"`. The port has no ore dictionary, so `isBone` tests `Items.BONE` plus
the `c:bones` convention tag, which is the modern equivalent.

---

## Natural generation

`common/world/structure2/LOTRWorldGenGulfTotem.java` plus the structure blob at
`assets/lotr/strscan/gulf_totem.strscan`. Blocked on the whole structure system,
not just this block.
