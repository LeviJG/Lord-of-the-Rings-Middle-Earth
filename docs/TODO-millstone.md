# TODO — Millstone

The block, its block entity, its screen and its recipe list are ported and
working. What is left is mostly recipes that depend on things not yet ported.

Original sources under `old mod/src/main/java/lotr/`:
`common/block/LOTRBlockMillstone.java`, `common/tileentity/LOTRTileEntityMillstone.java`,
`common/recipe/LOTRMillstoneRecipes.java`, `common/inventory/LOTRContainerMillstone.java`,
`client/gui/LOTRGuiMillstone.java`.

---

## What it is

A stone grinder with **no fuel slot**. It runs for exactly as long as the block
is receiving a redstone signal, and while it runs it grinds one item out of its
input slot every **200 ticks**.

- Two slots, stacked: input at the top, output at the bottom.
- Each recipe carries a **chance**. The input is consumed whether the roll
  succeeds or not, so cobblestone loses a quarter of itself on the way to
  gravel and gravel loses three quarters on the way to flint.
- Cut the power mid-grind and the progress is **discarded**, not banked.
- Running swaps in an animated texture and throws smoke off the top.
- Hoppers feed the input from any side and pull the output from below.
- Comparators read the contents.

Its real job is turning stone back down the chain — stone to cobble to gravel
to flint — and cracking bricks, which is otherwise a one-way trip.

---

## Deferred

**No crafting recipe.** The block cannot be made yet; it is creative-only. Same
for the Stone Chest and the other chests.

**Recipes whose outputs are not ported.** These are in the original list and are
simply missing here, because the items do not exist yet:

| input | output | chance |
|---|---|---|
| `obsidianGravel` | Obsidian Shard | 1.0 |
| `oreSalt` | Salt | 1.0 |
| `rock` (meta 0) | Mordor Gravel | 0.75 |
| `brick_block` | cracked red brick | 1.0 |

Add them to `LOTRMillstoneRecipes.createRecipes()` when the items land.

**Cracked bricks are not craftable in a furnace.** The original's
`addCrackedBricks` did two things: it added the millstone recipe *and* called
`GameRegistry.addSmelting(itemstack, result, 0.1f)`, so a furnace could crack
bricks too. Only the millstone half is ported. The furnace half belongs in
`LOTRRecipeProvider` as a smelting recipe per brick family.

**The GUI has no progress-bar sync test.** `LOTRContainerMillstone` sent
`currentMillTime` and `isMilling` as separate progress bars; both are carried
here as `ContainerData` slots. Worth a look in game that the chute actually
fills over the ten seconds rather than snapping.

---

## Deviations

**Recipes are keyed by item, not item plus damage.** The 1.7.10 list was full of
`new ItemStack(LOTRMod.brick, 1, 11)` entries because one block id held sixteen
bricks in its metadata. Those are separate blocks now, so the linear scan over
an `ItemStack` list becomes a plain map lookup.

**The cracked-brick entries are derived rather than listed.** Roughly thirty
`addCrackedBricks` calls all encode the same rule, so
`LOTRMillstoneRecipes.addCrackedPairs()` walks the block registry and pairs
`lotr:x` with `lotr:cracked_x` wherever both exist. New brick families are
covered automatically and no entry can name an unported block.

**Red sandstone is vanilla's.** `LOTRMod.redSandstone` only existed because
1.7.10 had none; modern Minecraft does, and it grinds to vanilla red sand.

**`Result` holds an item and a count, not an `ItemStack`.** The recipe list is
built during mod init, before item components are bound, and constructing a
stack that early throws `Components not bound yet`. The stack is built on
demand instead.

**ACTIVE reuses `BlockStateProperties.LIT`.** The original kept "running" in
metadata bit 8. Any boolean property would do; LIT avoids registering a new one
and reads correctly in the blockstate JSON.
