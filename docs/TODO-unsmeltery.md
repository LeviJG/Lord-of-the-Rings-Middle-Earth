# TODO — Unsmeltery: the recursive recipe scan

The unsmeltery is ported and working end to end: block, cauldron model with its
rocking animation, three-slot machine, GUI, and the unsmelting maths. One part
of the original algorithm is deliberately simplified.

Original sources under `old mod/src/main/java/lotr/`.

---

## What it does

A furnace run backwards. Feed it a tool, weapon or piece of armour and it hands
back some of the metal it was made from, burning fuel to do it. Three slots —
input, fuel, output — and a 400-tick cycle, twice a furnace's.

The yield is deliberately lossy, from `getRandomUnsmeltingResult`:

```
count = recipeCost × 0.8 × (remainingDurability / maxDurability) × random(0.7, 1.0)
```

So a pristine iron sword (one ingot of iron in its recipe) usually returns
nothing at all, a full iron chestplate (eight) returns a useful handful, and a
nearly-broken tool returns almost nothing. **The input is consumed either way** —
that is the point of the loss.

---

## Simplified: `determineResourcesUsed`

`recipeCost` above is "how many of the repair material does this item's crafting
recipe use". The port scans crafting recipes for one whose result is the item,
then counts ingredients matching the material.

**The original also RECURSED.** `countMatchingIngredients` called
`determineResourcesUsed` on each ingredient that did not directly match, so an
item crafted from iron *blocks* counted nine ingots per block, and a recipe
whose ingredient was itself crafted from the material contributed that whole
sub-cost. It carried a `recursiveCheckedRecipes` list to avoid cycling.

The port counts **direct matches only**. For vanilla gear — every tool and
armour piece is ingots and sticks — the two agree exactly. They diverge for
anything crafted from an intermediate, which in practice means modded or
future LOTR recipes that go through block or nugget forms.

To restore it: recurse in `LOTRUnsmelteryBlockEntity.resourcesUsed` where the
ingredient fails `ingredient.test(material)`, threading a visited-recipe set
through to stop cycles. The result cache keyed on (item, material) already
exists and should wrap the recursive call too.

---

## Other deviations

**Materials come from a data component, not a type switch.** The original
branched over `ItemTool`, `ItemSword`, `ItemArmor`, `LOTRItemCrossbow`,
`LOTRItemThrowingAxe`, `LOTRItemMountArmor` and then a hand-written tail of odds
and ends — buckets, the three rings, the three goblets. All of that is now one
lookup of `DataComponents.REPAIRABLE`, which every repairable item carries and
which covers anything the port adds later with no list to maintain.

The consequence: the hand-listed items are only unsmeltable if they declare a
repair material. A vanilla bucket does not, so it no longer melts down — the
original special-cased it to one iron ingot. If those matter, they want a small
override map consulted before the component.

**`Recipe.getResultItem` is gone.** 26.2 removed it because a recipe's output
can depend on its input. `resultOf` calls `assemble(CraftingInput.EMPTY)`
instead, which shaped and shapeless recipes answer without looking at the input,
and skips anything that throws rather than failing the scan.

**The active/idle sync is one blockstate, not a packet.** The original pushed
its own "Active" boolean to drive the cauldron's sway. `LIT` already lives on
the blockstate and reaches clients for free, so `clientTick` reads that.

**The two stand panels are missing from the ITEM model only.** They are tilted
45° about a child pivot, which a JSON block model cannot express. They are three
pixels at icon size. The world model draws them correctly.

**No achievement.** `lotr.achievement.unsmelt` ("Melt down an item in the
unsmeltery") is dropped along with the rest of the achievement system.

---

## Regenerating the item model

`python3 tools/gen_unsmeltery_item_model.py` — same arrangement as the troll
totem's. **Re-run it if `LOTRUnsmelteryModel.createLayer()` changes**; nothing
will warn you if they drift.
