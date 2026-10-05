package net.blueskiez77.lord_of_the_rings__middle_earth.common.config;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTRConfig: the mod's options, read from {@code config/lotr.cfg} in the
 * original's own Forge layout, categories, names, defaults and comments (see
 * {@link LOTRConfigFile}), so an existing file carries over unchanged.
 *
 * <p>Each side reads its own file. A few gameplay options matter to the
 * client as the server has them -- feast mode, fellowship creation and size,
 * the two enchanting systems, strict title requirements and the custom
 * waypoint floor -- and the server sends those on login (LOTRLevelData's
 * login packet in the original; {@link LOTRConfigSync} here). The
 * {@code is...Enabled(Level)} accessors answer from whichever applies.
 *
 * <p>The options are all here, but each takes effect only once the system
 * that reads it is ported; PORT_PLAN's tracker lists which are still waiting.
 * The {@code dimension} category (LOTRDimension.configureDimensions) comes with
 * the dimensions (D10), and the in-game config screen with the GUIs.
 */
public final class LOTRConfig {
    public static final String CATEGORY_DIMENSION = "dimension";
    public static final String CATEGORY_GAMEPLAY = "gameplay";
    public static final String CATEGORY_GUI = "gui";
    public static final String CATEGORY_ENVIRONMENT = "environment";
    public static final String CATEGORY_MISC = "misc";

    public static final int MIN_PLAYER_DATA_CLEARING_INTERVAL = 600;

    private static @Nullable LOTRConfigFile file;

    // gameplay
    public static boolean allowBannerProtection = true;
    public static boolean allowSelfProtectingBanners = true;
    public static boolean allowMiniquests = true;
    public static boolean allowBountyQuests = true;
    public static boolean enableTitles = true;
    public static boolean enableFastTravel = true;
    public static boolean enableFellowshipCreation = true;
    public static int fellowshipMaxSize = -1;
    public static boolean alignmentDrain = true;
    public static boolean enableConquest = true;
    public static boolean removeGoldenAppleRecipes = true;
    public static boolean enablePortals = true;
    public static boolean enableOrcSkirmish = true;
    public static boolean enchantingVanilla = false;
    public static boolean enchantingLOTR = true;
    public static boolean enchantingAutoRemoveVanilla = false;
    public static boolean enablePotionBrewing = true;
    public static int bannerWarningCooldown = 20;
    public static boolean dropMutton = true;
    public static boolean drunkMessages = true;
    public static boolean middleEarthRespawning = true;
    public static int MERBedRespawnThreshold = 5000;
    public static int MERWorldRespawnThreshold = 2000;
    public static int MERMinRespawn = 500;
    public static int MERMaxRespawn = 1500;
    public static boolean generateMapFeatures = true;
    public static boolean generateFixedSettlements = true;
    public static boolean changedHunger = true;
    public static boolean canAlwaysEat = true;
    public static int forceMapLocations = 0;
    public static boolean enableBandits = true;
    public static boolean enableInvasions = true;
    public static boolean enableUnitLevelling = true;
    public static boolean removeDiamondArmorRecipes = false;
    public static boolean disableEnderChestsUtumno = false;
    public static int preventTraderKidnap = 0;
    public static boolean disableLightningGrief = false;
    public static boolean disableFireSpread = false;
    public static boolean enableVillagerTrading = true;
    public static boolean strictFactionTitleRequirements = false;
    public static boolean invasionProgressReverts = false;
    public static boolean hiredUnitKillsCountForBane = true;
    public static int customWaypointMinY = -1;

    // gui
    public static boolean alwaysShowAlignment = false;
    public static int alignmentXOffset = 0;
    public static int alignmentYOffset = 0;
    public static boolean displayAlignmentAboveHead = true;
    public static boolean enableSepiaMap = false;
    public static boolean osrsMap = false;
    public static boolean enableOnscreenCompass = true;
    public static boolean compassExtraInfo = true;
    public static boolean hiredUnitHealthBars = true;
    public static boolean hiredUnitIcons = true;
    public static boolean elvenBladeGlow = true;
    public static boolean immersiveSpeech = true;
    public static boolean immersiveSpeechChatLog = false;
    public static boolean meleeAttackMeter = true;
    public static boolean mapLabels = true;
    public static boolean mapLabelsConquest = true;
    public static boolean enableQuestTracker = true;
    public static boolean trackingQuestRight = false;
    public static boolean customMainMenu = true;
    public static boolean fellowPlayerHealthBars = true;
    public static boolean displayCoinCounts = true;
    public static boolean balrogWings = true;
    public static boolean showPermittedBannerSilhouettes = true;

    // environment
    public static boolean enableLOTRSky = true;
    public static boolean enableMistyMountainsMist = true;
    public static boolean enableAmbience = true;
    public static boolean enableSunFlare = true;
    public static int cloudRange = 1024;
    public static boolean newWeather = true;
    public static boolean snowyStone = true;
    public static boolean aurora = true;
    public static boolean naturalBlocks = true;

    // misc
    public static boolean updateLangFiles = true;
    public static boolean checkUpdates = true;
    public static boolean strTimelapse = false;
    public static int strTimelapseInterval = 5;
    public static boolean protectHobbitKillers = false;
    public static boolean fixMobSpawning = true;
    public static int mobSpawnInterval = 0;
    public static int musicIntervalMin = 30;
    public static int musicIntervalMax = 150;
    public static boolean displayMusicTrack = false;
    public static int musicIntervalMenuMin = 10;
    public static int musicIntervalMenuMax = 20;
    public static boolean fixRenderDistance = true;
    public static boolean preventMessageExploit = true;
    public static boolean cwpLog = false;
    public static int playerDataClearingInterval = 600;

    // gameplay
    public static boolean enableAttackCooldown = false;

    /** The server's values for the options the client needs, from {@link LOTRConfigSync}. */
    public static boolean clientside_thisServer_feastMode;
    public static boolean clientside_thisServer_fellowshipCreation;
    public static int clientside_thisServer_fellowshipMaxSize;
    public static boolean clientside_thisServer_enchanting;
    public static boolean clientside_thisServer_enchantingLOTR;
    public static boolean clientside_thisServer_strictFactionTitleRequirements;
    public static int clientside_thisServer_customWaypointMinY;

    private LOTRConfig() {
    }

    /** setupAndLoad: at startup, before anything reads an option. */
    public static void setupAndLoad() {
        file = new LOTRConfigFile(FabricLoader.getInstance().getConfigDir().resolve("lotr.cfg"));
        load();
    }

    public static void load() {
        LOTRConfigFile file = LOTRConfig.file;
        if (file == null) {
            return;
        }
        file.load();
enableAttackCooldown = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Attack Cooldown?", false, null);
        allowBannerProtection = file.getBoolean(CATEGORY_GAMEPLAY, "Allow Banner Protection", true, null);
        allowSelfProtectingBanners = file.getBoolean(CATEGORY_GAMEPLAY, "Allow Self-Protecting Banners", true, null);
        allowMiniquests = file.getBoolean(CATEGORY_GAMEPLAY, "NPCs give mini-quests", true, null);
        allowBountyQuests = file.getBoolean(CATEGORY_GAMEPLAY, "NPCs give bounty mini-quests", true, "Allow NPCs to generate mini-quests to kill enemy players");
        enableTitles = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Titles", true, null);
        enableFastTravel = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Fast Travel", true, null);
        enableFellowshipCreation = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Fellowship creation", true, "If disabled, admins can still create Fellowships using the command");
        fellowshipMaxSize = file.getInt(CATEGORY_GAMEPLAY, "Fellowship maximum size", -1, "Maximum player count for Fellowships. Negative = no limit");
        alignmentDrain = file.getBoolean(CATEGORY_GAMEPLAY, "Enable alignment drain", true, "Factions dislike if a player has + alignment with enemy factions");
        enableConquest = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Conquest", true, null);
        removeGoldenAppleRecipes = file.getBoolean(CATEGORY_GAMEPLAY, "Remove Golden Apple recipes", true, null);
        enablePortals = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Middle-earth Portals", true, "Enable or disable the buildable Middle-earth portals (excluding the Ring Portal). If disabled, portals can still be made, but will not function");
        enableOrcSkirmish = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Orc Skirmishes", true, null);
        enchantingVanilla = file.getBoolean(CATEGORY_GAMEPLAY, "Enchanting: Vanilla System", false, "Enable the vanilla enchanting system: if disabled, prevents players from enchanting items, but does not affect existing enchanted items");
        enchantingLOTR = file.getBoolean(CATEGORY_GAMEPLAY, "Enchanting: LOTR System", true, "Enable the LOTR enchanting system: if disabled, prevents newly crafted items, loot chest items, etc. from having modifiers applied, but does not affect existing modified items");
        enchantingAutoRemoveVanilla = file.getBoolean(CATEGORY_GAMEPLAY, "Enchanting: Auto-remove vanilla enchants", false, "Intended for servers. If enabled, enchantments will be automatically removed from items");
        enablePotionBrewing = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Potion Brewing", true, "Mainly intended for servers. Disable the vanilla potion brewing system, as it is not 'lore-friendly'");
        bannerWarningCooldown = file.getInt(CATEGORY_GAMEPLAY, "Protection Warning Cooldown", 20, "Cooldown time (in ticks) between appearances of the warning message for banner-public land");
        dropMutton = file.getBoolean(CATEGORY_GAMEPLAY, "Mutton Drops", true, "Enable or disable sheep dropping the mod's mutton items");
        drunkMessages = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Drunken Messages", true, null);
        middleEarthRespawning = file.getBoolean(CATEGORY_GAMEPLAY, "Middle-earth Respawning: Enable", true, "If enabled, when a player dies in Middle-earth far from their spawn point, they will respawn somewhere near their death point instead");
        MERBedRespawnThreshold = file.getInt(CATEGORY_GAMEPLAY, "Middle-earth Respawning: Bed Threshold", 5000, "Threshold distance from spawn for applying Middle-earth Respawning when the player's spawn point is a bed");
        MERWorldRespawnThreshold = file.getInt(CATEGORY_GAMEPLAY, "Middle-earth Respawning: World Threshold", 2000, "Threshold distance from spawn for applying Middle-earth respawning when the player's spawn point is the world spawn (no bed)");
        MERMinRespawn = file.getInt(CATEGORY_GAMEPLAY, "Middle-earth Respawning: Min Respawn Range", 500, "Minimum possible range to place the player from their death point");
        MERMaxRespawn = file.getInt(CATEGORY_GAMEPLAY, "Middle-earth Respawning: Max Respawn Range", 1500, "Maximum possible range to place the player from their death point");
        generateMapFeatures = file.getBoolean(CATEGORY_GAMEPLAY, "Generate map features", true, "Roads; fixed hills and mountains; fixed structures, such as the Utumno entrance");
        generateFixedSettlements = file.getBoolean(CATEGORY_GAMEPLAY, "Generate fixed settlements", true, "Villages in fixed locations, such as Bree");
        changedHunger = file.getBoolean(CATEGORY_GAMEPLAY, "Hunger changes", true, "Food meter decreases more slowly");
        canAlwaysEat = file.getBoolean(CATEGORY_GAMEPLAY, "Feast Mode", true, "Food can always be eaten regardless of hunger");
        forceMapLocations = file.getInt(CATEGORY_GAMEPLAY, "Force Hide/Show Map Locations", 0, "Force hide or show players' map locations. 0 = per-player (default), 1 = force hide, 2 = force show");
        enableBandits = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Bandits", true, null);
        enableInvasions = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Invasions", true, null);
        enableUnitLevelling = file.getBoolean(CATEGORY_GAMEPLAY, "Enable hired unit levelling", true, null);
        removeDiamondArmorRecipes = file.getBoolean(CATEGORY_GAMEPLAY, "Remove diamond armour recipes", false, null);
        disableEnderChestsUtumno = file.getBoolean(CATEGORY_GAMEPLAY, "Disable ender chests in Utumno", false, null);
        preventTraderKidnap = file.getInt(CATEGORY_GAMEPLAY, "Prevent trader transport range", 0, "Prevent transport of structure-bound traders beyond this distance outside their initial home range (0 = disabled)");
        disableLightningGrief = file.getBoolean(CATEGORY_GAMEPLAY, "Disable lightning grief", false, "Prevent lightning from placing fire blocks");
        disableFireSpread = file.getBoolean(CATEGORY_GAMEPLAY, "Disable fire spread", false, "Activate instead of gamerule doFireTick for finer control over fire behaviour. Fire will still die out and burn blocks, but will not spread");
        enableVillagerTrading = file.getBoolean(CATEGORY_GAMEPLAY, "Enable Villager trading", true, "Intended for servers. Enable or disable vanilla villager trading");
        strictFactionTitleRequirements = file.getBoolean(CATEGORY_GAMEPLAY, "Strict faction title requirements", false, "Require a pledge to bear faction titles of alignment level equal to the faction's pledge level - not just those titles higher than pledge level");
        invasionProgressReverts = file.getBoolean(CATEGORY_GAMEPLAY, "Invasion progress reverts", false, "Invasion progress slowly reverts if left alone for too long");
        hiredUnitKillsCountForBane = file.getBoolean(CATEGORY_GAMEPLAY, "Hired units killed count towards x-bane modifiers", true, "");
        customWaypointMinY = file.getInt(CATEGORY_GAMEPLAY, "Custom waypoint minimum y-level", -1, "Minimum y-coordinate at which a player can create a custom waypoint. Negative value = no limit");
        alwaysShowAlignment = file.getBoolean(CATEGORY_GUI, "Always show alignment", false, "If set to false, the alignment bar will only be shown in Middle-earth. If set to true, it will be shown in all dimensions");
        alignmentXOffset = file.getInt(CATEGORY_GUI, "Alignment x-offset", 0, "Configure the x-position of the alignment bar on-screen. Negative values move it left, positive values right");
        alignmentYOffset = file.getInt(CATEGORY_GUI, "Alignment y-offset", 0, "Configure the y-position of the alignment bar on-screen. Negative values move it up, positive values down");
        displayAlignmentAboveHead = file.getBoolean(CATEGORY_GUI, "Display alignment above head", true, "Enable or disable the rendering of other players' alignment values above their heads");
        enableSepiaMap = file.getBoolean(CATEGORY_GUI, "Sepia Map", false, "Display the Middle-earth map in sepia colours");
        osrsMap = file.getBoolean(CATEGORY_GUI, "OSRS Map", false, "It's throwback time. (Requires game restart)");
        enableOnscreenCompass = file.getBoolean(CATEGORY_GUI, "On-screen Compass", true, null);
        compassExtraInfo = file.getBoolean(CATEGORY_GUI, "On-screen Compass Extra Info", true, "Display co-ordinates and biome below compass");
        hiredUnitHealthBars = file.getBoolean(CATEGORY_GUI, "Hired NPC Health Bars", true, null);
        hiredUnitIcons = file.getBoolean(CATEGORY_GUI, "Hired NPC Icons", true, null);
        elvenBladeGlow = file.getBoolean(CATEGORY_GUI, "Animated Elven blade glow", true, null);
        immersiveSpeech = file.getBoolean(CATEGORY_GUI, "Immersive Speech", true, "If set to true, NPC speech will appear on-screen with the NPC. If set to false, it will be sent to the chat box");
        immersiveSpeechChatLog = file.getBoolean(CATEGORY_GUI, "Immersive Speech Chat Logs", false, "Toggle whether speech still shows in the chat box when Immersive Speech is enabled");
        meleeAttackMeter = file.getBoolean(CATEGORY_GUI, "Melee attack meter", true, null);
        mapLabels = file.getBoolean(CATEGORY_GUI, "Map Labels", true, null);
        mapLabelsConquest = file.getBoolean(CATEGORY_GUI, "Map Labels - Conquest", true, null);
        enableQuestTracker = file.getBoolean(CATEGORY_GUI, "Enable quest tracker", true, null);
        trackingQuestRight = file.getBoolean(CATEGORY_GUI, "Flip quest tracker", false, "Display the quest tracker on the right-hand side of the screen instead of the left");
        customMainMenu = file.getBoolean(CATEGORY_GUI, "Custom main menu", true, "Use the mod's custom main menu screen");
        fellowPlayerHealthBars = file.getBoolean(CATEGORY_GUI, "Fellow Player Health Bars", true, null);
        displayCoinCounts = file.getBoolean(CATEGORY_GUI, "Inventory coin counts", true, null);
        balrogWings = file.getBoolean(CATEGORY_GUI, "Balrog Wings", true, "Choose your side in the legendary debate...");
        showPermittedBannerSilhouettes = file.getBoolean(CATEGORY_GUI, "Show permitted banner silhouettes", true, "In the debug screen, render any protection banners for which you have permission as a solid green shape, visible through blocks");
        enableLOTRSky = file.getBoolean(CATEGORY_ENVIRONMENT, "Middle-earth sky", true, "Toggle the new Middle-earth sky");
        enableMistyMountainsMist = file.getBoolean(CATEGORY_ENVIRONMENT, "Misty Misty Mountains", true, "Toggle mist overlay in the Misty Mountains");
        enableAmbience = file.getBoolean(CATEGORY_ENVIRONMENT, "Ambience", true, null);
        enableSunFlare = file.getBoolean(CATEGORY_ENVIRONMENT, "Sun flare", true, null);
        cloudRange = file.getInt(CATEGORY_ENVIRONMENT, "Cloud range", 1024, "Middle-earth cloud rendering range. To use vanilla clouds, set this to a non-positive value");
        newWeather = file.getBoolean(CATEGORY_ENVIRONMENT, "New weather", true, "New rain textures and sounds; mist during rain; wind sounds; new weather types");
        snowyStone = file.getBoolean(CATEGORY_ENVIRONMENT, "Snowy stone", true, "Snowy texture on the sides of snow-capped stone blocks");
        aurora = file.getBoolean(CATEGORY_ENVIRONMENT, "Aurora", true, "The Aurora, or Northern Lights! May be a slightly performance-intensive feature.");
        naturalBlocks = file.getBoolean(CATEGORY_ENVIRONMENT, "Natural blocks", true, "Randomly rotate textures on some blocks - grass, dirt, sand, etc. - for a more natural appearance");
        updateLangFiles = file.getBoolean(CATEGORY_MISC, "Run language update helper", true, "Run the mod's language file update helper on launch - see .minecraft/mods/LOTR_UpdatedLangFiles/readme.txt");
        checkUpdates = file.getBoolean(CATEGORY_MISC, "Check for updates", true, "Disable this if you will be playing offline");
        strTimelapse = file.getBoolean(CATEGORY_MISC, "Structure Timelapse", false, "Structure spawners generate as a timelapse instead of instantly. WARNING: May be buggy. See also the command /strTimelapse");
        strTimelapseInterval = file.getInt(CATEGORY_MISC, "Structure Timelapse Interval", 5, "Structure timelapse interval (in ms) between each block placement");
        protectHobbitKillers = file.getBoolean(CATEGORY_MISC, "Protect Hobbit Killers", false, "For servers: Disable broadcasting of the 'Hobbit Slayer' achievement, to protect new evil players from being persecuted");
        fixMobSpawning = file.getBoolean(CATEGORY_MISC, "Fix mob spawning lag", true, "Fix a major source of server lag caused by the vanilla mob spawning system");
        mobSpawnInterval = file.getInt(CATEGORY_MISC, "Mob spawn interval", 0, "Tick interval between mob spawn cycles (which are then run multiple times to compensate). Higher values may reduce server lag");
        musicIntervalMin = file.getInt(CATEGORY_MISC, "Music Interval: Min.", 30, "Minimum time (seconds) between LOTR music tracks");
        musicIntervalMax = file.getInt(CATEGORY_MISC, "Music Interval: Max.", 150, "Maximum time (seconds) between LOTR music tracks");
        displayMusicTrack = file.getBoolean(CATEGORY_MISC, "Display music track", false, "Display the name of a LOTR music track when it begins playing");
        musicIntervalMenuMin = file.getInt(CATEGORY_MISC, "Menu Music Interval: Min.", 10, "Minimum time (seconds) between LOTR menu music tracks");
        musicIntervalMenuMax = file.getInt(CATEGORY_MISC, "Menu Music Interval: Max.", 20, "Maximum time (seconds) between LOTR menu music tracks");
        fixRenderDistance = file.getBoolean(CATEGORY_MISC, "Fix render distance", true, "Fix a vanilla crash caused by having render distance > 16 in the options.txt. NOTE: This will not run if Optifine is installed");
        preventMessageExploit = file.getBoolean(CATEGORY_MISC, "Fix /msg exploit", true, "Disable usage of @a, @r, etc. in the /msg command, to prevent exploiting it as a player locator");
        cwpLog = file.getBoolean(CATEGORY_MISC, "Custom Waypoint logging", false, null);
        playerDataClearingInterval = file.getInt(CATEGORY_MISC, "Playerdata clearing interval", 600, "Tick interval between clearing offline LOTR-playerdata from the cache. Offline players' data is typically loaded to serve features like fellowships and their shared custom waypoints. Higher values may reduce server lag, as data will have to be reloaded from disk less often, but will result in higher RAM usage to some extent");
        if (strTimelapseInterval < 0) {
            setStructureTimelapseInterval(0);
        }
        if (file.hasChanged()) {
            file.save();
        }
    }

    public static boolean areStrictFactionTitleRequirementsEnabled(Level level) {
        return level.isClientSide() ? clientside_thisServer_strictFactionTitleRequirements : strictFactionTitleRequirements;
    }

    public static int getCustomWaypointMinY(Level level) {
        return level.isClientSide() ? clientside_thisServer_customWaypointMinY : customWaypointMinY;
    }

    public static int getFellowshipMaxSize(Level level) {
        return level.isClientSide() ? clientside_thisServer_fellowshipMaxSize : fellowshipMaxSize;
    }

    public static boolean isEnchantingEnabled(Level level) {
        return level.isClientSide() ? clientside_thisServer_enchanting : enchantingVanilla;
    }

    public static boolean isFellowshipCreationEnabled(Level level) {
        return level.isClientSide() ? clientside_thisServer_fellowshipCreation : enableFellowshipCreation;
    }

    public static boolean isLOTREnchantingEnabled(Level level) {
        return level.isClientSide() ? clientside_thisServer_enchantingLOTR : enchantingLOTR;
    }

    /** Feast mode as the side in question has it (LOTRReplacedMethods.Player.canEat). */
    public static boolean isFeastMode(Level level) {
        return level.isClientSide() ? clientside_thisServer_feastMode : canAlwaysEat;
    }

    public static void setStructureTimelapse(boolean flag) {
        strTimelapse = flag;
        save(CATEGORY_MISC, "Structure Timelapse", flag);
    }

    public static void setStructureTimelapseInterval(int i) {
        strTimelapseInterval = Math.max(i, 0);
        if (file != null) {
            file.set(CATEGORY_MISC, "Structure Timelapse Interval", strTimelapseInterval);
            file.save();
        }
    }

    public static void toggleMapLabels() {
        mapLabels = !mapLabels;
        save(CATEGORY_GUI, "Map Labels", mapLabels);
    }

    public static void toggleMapLabelsConquest() {
        mapLabelsConquest = !mapLabelsConquest;
        save(CATEGORY_GUI, "Map Labels - Conquest", mapLabelsConquest);
    }

    public static void toggleSepia() {
        enableSepiaMap = !enableSepiaMap;
        save(CATEGORY_GUI, "Sepia Map", enableSepiaMap);
    }

    private static void save(String category, String name, boolean value) {
        if (file != null) {
            file.set(category, name, value);
            file.save();
        }
    }
}
