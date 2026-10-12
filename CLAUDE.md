# LOTR Middle-earth — Fabric 26.2 port

A port of the 1.7.10 Forge "Lord of the Rings — Middle-earth" mod to Fabric for Minecraft 26.2
(Mojang mappings). The original source sits beside the port in `old mod/` for class-by-class
comparison. Mod id `lotr`; Java package `net.blueskiez77.lord_of_the_rings__middle_earth`
(`src/main` common, `src/client` client-only, `src/main/generated` datagen output).

**`PORT_PLAN.md` is the single source of truth**: its standing rules, the work units (Track B audits,
Track D porting units with their *Result* lines), the Deferred-port tracker and the Findings backlog.
Read its "Standing rules" before working. This file adds the user's rules that live outside it.

## Working rules (from the user)

- **Mirror the original.** `old mod/src/main/java/lotr/` is the specification; a divergence is a bug
  by default. Check the registration site in `old mod/.../common/LOTRMod.java` before claiming one,
  since names map loosely. When the user describes how something behaved in the original, find where
  the original did it (subclasses, unit variants, `LOTREntities`) and copy that; do not implement a
  guess.
- **Modern vanilla over inherited 1.7.10 values** (see PORT_PLAN): values the mod chose are ported;
  values it only inherited from 1.7.10 vanilla follow 26.2 vanilla.
- **Deliberate port values win.** A value a comment marks as the port's deliberate choice is the
  user's decision: report the original's figure, don't change it (fix a false rationale only).
- **Vanilla weapon handling stays.** Bows, crossbows, spears and tridents keep 26.2 vanilla's
  draw/charge/throw mechanics; only the mod's stats carry over.
- **Names match the original.** Display names equal the original's `en_US.lang`; entity registry
  ids are the name the original registered, classes `LOTREntityX` → `LOTRXEntity`, entity textures
  keep their file names (snake_case). Items and blocks keep the port's modern ids.
- **Fix exactly what was reported.** Suspected knock-ons are reported, not silently fixed;
  improvements are suggested, not applied. **But** original behaviour that was only waiting for a
  dependency the current work just ported (a flee goal, a bane, a tracker item whose blocker is gone)
  is wired up in the same change, mentioned in the report and struck from the tracker.
- **Surface divergences and bug fixes** to the user in the final report.
- **Be exhaustive** within a unit; split big units into sub-units, finish each, tick the parent last.
  Everything is open now (since v0.4.0): pick the order freely, world gen and advancements included.
- **Track what can't be ported yet** on the Deferred-port tracker in PORT_PLAN (original class or
  method, what it does, the port file it belongs in, the unit that found it). Prose mentions are not
  enough.
- **Port items with their systems**: creative tab, tags, recipes, loot, modifiers/enchantments,
  dispenser behaviour, lang, model.
- **Per-system player data.** The original's `LOTRPlayerData` is split into one Fabric attachment per
  system (persistent, `copyOnDeath`, synced where the client needs it), added with the system that
  uses it, keeping the original's NBT keys. World-wide state that offline players need (fellowships,
  custom waypoints) is `SavedData`.
- **Recipes:** never alter vanilla recipes (the config-driven removals `removeGoldenAppleRecipes` /
  `removeDiamondArmorRecipes` excepted); faction recipes stay on their faction tables.
- **Textures:** never author or alter pixels (no generated/rotated/rescaled PNGs). Copying and
  renaming the original's PNGs from `old mod/.../textures/` into the port is expected.
- **Excluded on purpose** (never list as missing): vanilla duplicates (`lotr:Gravel`, armour stand,
  copper, red sandstone, vanilla-wood doors and the like, the LOTR rabbit and fish), bookshelf storage,
  the LOTR flower pot, the melee attack meter, the gulduril glow (the user's animated texture replaces
  it), exclusive titles and shields. Traders whose original pool had `rabbitStew` sell vanilla
  `Items.RABBIT_STEW`.
- **Comments:** while auditing, remove stale or noisy comments (notes to the user, debugging
  narrative, claims a fix disproved); keep ones naming the original class/method mirrored or
  explaining a modern-API constraint.

## Git and builds

- **Never `git commit` or push.** The user makes every commit; leave changes in the working tree and
  say what is ready.
- Every deleted file is also removed from the index (`git rm -f`, or
  `git rm --cached --ignore-unmatch -q -- <path>`); renames use `git mv`.
- **Never launch Minecraft** (`runClient`, datagen runs, ...). The user tests the built jar in the
  Prism instance "26.2 test env" (with Sodium and Mod Menu). For in-game bug reports read
  `~/.local/share/PrismLauncher/instances/26.2 test env/minecraft/logs/latest.log` and its
  `crash-reports/` first; the project's `run/logs` is stale.
- **No ritual final build.** Compile mid-change only to settle an API question:
  `./gradlew compileClientJava -q 2>&1 | grep -E "error:" -A3` (compiles common too). Don't report
  "builds clean" as part of the deliverable.
- Datagen can't be run, so generated files under `src/main/generated` are written by hand alongside
  the provider changes.

## Looking things up in 26.2

Before declaring a rendering or API problem unfixable, read vanilla's actual code:
`javap -p -c -cp <jar>` on the deobfuscated jars under
`~/.gradle/caches/fabric-loom/minecraftMaven/net/minecraft/minecraft-{common,clientonly}-deobf/<version>/`,
and `unzip -p` on the client jar for vanilla assets. 26.2 notes collected so far:
- GUI drawing goes through `GuiGraphicsExtractor` (`blit`, `fill`, `text`, `centeredText`, `item`,
  `pose()` as a `Matrix3x2fStack`, `enableScissor`, `nextStratum`, `setTooltipForNextFrame`); screens
  override `extractRenderState` / `extractBackground`; the current screen is `minecraft.gui.screen()`
  and screens open with `setScreenAndShow`.
- World presets: `data/<ns>/worldgen/world_preset` plus the `minecraft:worldgen/world_preset/normal`
  tag. Day length and moon come from `world_clock` and `timeline` data (`level.getDefaultClockTime()`).
- Lang `%d` works; render layers come from texture transparency.

## Where things stand (2026-10-10)

Done most recently: D12 (invasions, warhorns), D10j, D10i (partly), D10k-a (Middle-earth world type,
respawning there), and all of **D13** (waypoints, fast travel, custom waypoints, the shared
`client/map/LOTRMapRenderer`, the loading and journey screens, and the full map screen
`LOTRMapScreen` with player locations, control zones, `/opHideMap` and the factions-page map), and
the bounty help (`LOTRLastKnownLocation`); D10g, Middle-earth's look (`client/world`: colours, fog, sky,
clouds, aurora, weather, ambience); D10k-b, the "M.E. Classic" world type (all of D10 is done); all of
**D15**: the Ring portal and the elven and Morgul portals (D15a), the Utumno dimension with its entrance,
portals, rules and client side (D15b), and its creatures, the Balrog among them (D15c); Track W, the
world-gen review (2026-10-11; its standing decisions are in PORT_PLAN, "World-gen decisions"); D17a
and D17b, conquest (`world/map/LOTRConquestGrid`: the zones, kills, spawning, conquest-opened
waypoints, `/conquest`, and the Table of Command's conquest map); D17c, the map on the table's top.

Open, in the order chosen with the user (D10g, D10k-b, D15, Track W and D17 done):
- The tracker's one open entry: vanilla leather horse armour on LOTR horses (needs the user's call).
- B17: the user's in-game pass over the structures.

The PORT_PLAN tracker lists every remaining item with its unit.
