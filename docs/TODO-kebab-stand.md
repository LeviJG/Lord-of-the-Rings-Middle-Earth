# TODO — Kebab Stand

Both stands are ported and working, along with the Kebab item they produce.
A few small things are deferred.

Original sources under `old mod/src/main/java/lotr/`.

---

## What it is

A spit-roast you set **on top of a heat source**. It has no fuel slot of its
own — it reaches down into whatever container sits directly beneath it (a
furnace, a forge, a chest of coal), takes one piece of fuel out and burns it.
That is the whole design: a rack you put over something already hot.

- Right-click with **raw meat** to skewer a piece on, up to **eight**.
- It burns fuel from below, **200 ticks per piece**, turning each into a
  **Kebab** (heals 8, saturation 0.8).
- Right-click **empty-handed** to take a piece back — cooked ones come off
  first, so you get your kebabs before your raw meat.
- The spit **turns** while cooking and throws smoke and flame. When the fire
  goes out it speeds up and parks at the next whole turn, so it always stops
  square.
- No collision — you walk through it — and it breaks instantly.

Two variants for the two halves of the map: a wooden stand and a pale
**Sandstone Kebab Stand** for Harad. They differ only in the texture the
renderer binds.

---

## Deferred

**Contents are not carried in the dropped item.** The original stored the whole
spit in the item's NBT via `LOTRItemKebabStand.setKebabData` / `loadKebabData`,
so picking a stand up and putting it down again kept its meat, and
`getPickBlock` handed you a loaded stand in creative. The port drops the meat
alongside the stand instead. Restoring it wants a data component holding the
eight stacks plus the cooked mask, applied in `getCloneItemStack` and read back
in `setPlacedBy`.

**No achievement.** `LOTRAchievement.cookKebab` fired when you took a cooked
piece off. Dropped with the rest of the achievement system.

**The kebab's chat line.** `LOTRItemKebab.onEaten` had a one-in-a-hundred chance
of "That was a good kebab. You feel a lot better." A consumption hook needs an
item subclass; the item is a plain food item for now.

**`tile.lotr.kebabStand.meats` / `.cooked`** — the original had lang keys for a
"%d meat / Cooked" readout, presumably a tooltip on the dropped item. Not
ported; they belong with the NBT work above.

---

## Deviations

**"Raw meat" is a tag now.** The original took any `ItemFood` whose
`isWolfsFavoriteMeat()` was true and that had a furnace recipe. The modern
spelling is the `minecraft:wolf_food` item tag, and the smelting check is
unchanged — so anything a wolf would eat and a furnace would cook goes on the
spit, including modded meat, with no list to maintain.

**The cooked flags are a bit mask.** The original wrote a per-slot
`SlotCooked` boolean alongside each item. Eight bits is one integer either way,
and a mask cannot fall out of step with the slot list — its
`readKebabStandFromNBT` had a real bug here, indexing `cooked[i]` by *tag list
position* while indexing `inventory[slot]` by the stored slot number, so the
flags shifted whenever a slot was empty.

**The client is sent the whole block entity.** The original's description packet
carried a three-field summary (`Cooked`, `Cooking`, `Meats`) and the tile entity
kept `cookedClient` / `cookingClient` / `meatAmountClient` mirrors to read it
back. This syncs the full contents, so both sides read the same arrays and those
mirrors are gone.

**The renderer's transform order is its own.** Unlike the totem and the
unsmeltery — `translate, scale(1,-1,-1), rotate` — the kebab stand is
`translate, rotate, scale(-1,-1,1)`, and its facing-to-angle mapping (north 0,
west 90, south 180, east 270) is neither `toYRot()` nor its half turn. Both are
reproduced literally in `LOTRKebabStandRenderer`; do not "tidy" them to match
the other two.
