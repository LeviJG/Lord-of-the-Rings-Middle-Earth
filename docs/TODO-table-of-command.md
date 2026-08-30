# TODO — Table of Command: the map and the two screens

The block is ported and placeable. Everything it is actually *for* — the map of
Middle-earth on its surface, and the two screens it opens — is deferred, because
all of it depends on systems the port does not have yet.

Original sources under `old mod/src/main/java/lotr/`.

---

## Status

### Done

| Piece | Where |
|---|---|
| Block, shape (body + overhanging plank top), sound, hardness | `common/block/LOTRTableOfCommandBlock.java` |
| Block entity storing the map zoom, saved and synced | `common/blockentity/LOTRTableOfCommandBlockEntity.java` |
| Sneak-right-click cycles the zoom, with the original's clack | `LOTRTableOfCommandBlock.useWithoutItem` |
| Model, textures, lang, loot, creative tab | `assets/lotr/models/block/table_of_command.json` |

### Deferred

| Missing | What it blocks |
|---|---|
| `LOTRConquestGrid` | the conquest map screen (GUI id 60), and the map drawn on the table |
| `LOTRSquadrons` + `SquadronItem` | the squadron orders screen (GUI id 33) |
| `LOTRRenderCommandTable` | the map surface itself, a block entity renderer |

---

## What the table does in 1.7.10

An iron block with a plank surface overhanging it on every side, onto which a
map of Middle-earth is projected. `LOTRBlockCommandTable.onBlockActivated` has
three branches, checked in this order:

1. **Sneaking** — `toggleZoomExp()`. **Ported.** Steps the zoom down one notch
   and wraps from the bottom back to the top, so repeated sneak-clicks cycle
   through the five levels. Stored as a power of two, clamped to [-2, 2].
2. **Holding a squadron banner** (`LOTRSquadrons.SquadronItem`) — opens GUI 33,
   the squadron orders screen, for directing hired troops.
3. **Otherwise, if `LOTRConquestGrid.conquestEnabled(world)`** — opens GUI 60,
   the conquest map.

Branches 2 and 3 both also play the block's own break sound at half pitch. That
sound is kept on the zoom toggle, which is the one branch that survived.

`LOTRTileEntityCommandTable` holds nothing but `zoomExp`, and
`LOTRRenderCommandTable` is the block entity renderer that draws the map across
the tabletop. Note its `getRenderBoundingBox` is inflated to the 3x3 area around
the block — the map overhangs, so the default single-block box would cull it
early.

---

## To finish it

* **The map surface.** Port `LOTRRenderCommandTable` as a `BlockEntityRenderer`.
  There are two working examples in the port to copy from —
  `LOTRIthildinDoorRenderer` (a textured quad) and `LOTRTrollTotemRenderer` (an
  entity model). Remember the inflated render bounding box.
* **The two screens.** Both go through `LOTRCommonProxy.sendClientsideGUI`. The
  port has no equivalent GUI id system; `LOTRMenus` handles container screens
  and `LOTRBeaconScreen` shows the pattern for a screen with no container.
* **The interaction branches.** They slot into
  `LOTRTableOfCommandBlock.useWithoutItem` above the sneak check, in the
  original's order — squadron banner first, then conquest.

---

## Deviations

**No `useItemOn` override.** The original's squadron-banner branch reads the
held item, so it will need `useItemOn` rather than `useWithoutItem` when it
lands. Only the sneak branch exists today and that one ignores the held item.

**The zoom is stored even though nothing reads it.** Deliberate: it costs one
byte and means a table set up now is already at the zoom its owner chose when
there is finally a map to draw. No migration needed.
