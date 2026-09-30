package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.mojang.logging.LogUtils;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import org.slf4j.Logger;

/**
 * LOTRRandomSkins: a numbered set of skins, {@code 0.png}, {@code 1.png} and
 * up, one of which each entity wears for good, chosen from its UUID.
 *
 * <p>The skins now live under {@code textures/entity/<dir>/}, but each set is
 * still keyed and hashed by its original path ({@code "lotr:mob/deer"}): the
 * pick mixes in {@code skinPath.hashCode()}, and keeping that string keeps
 * every animal in the skin it had.
 *
 * <p>Lists are read lazily and dropped on every resource reload, as the
 * original's reload listener rebuilt them.
 *
 * <p>NOT ported yet: LOTRRandomSkinsCombinatorial, which layers several sets
 * into one. Only NPCs use it (D9).
 */
public final class LOTRRandomSkins {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random RAND = new Random();
    private static final Map<String, LOTRRandomSkins> ALL = new HashMap<>();

    private final String skinPath;
    private final String dir;
    private List<Identifier> skins;

    private LOTRRandomSkins(String skinPath, String dir) {
        this.skinPath = skinPath;
        this.dir = dir;
    }

    /**
     * loadSkinsList. {@code skinPath} is the original's path, kept for the
     * hash; {@code dir} is the folder under {@code textures/entity/}.
     */
    public static LOTRRandomSkins loadSkinsList(String skinPath, String dir) {
        return ALL.computeIfAbsent(skinPath, p -> new LOTRRandomSkins(p, dir));
    }

    /** Called once from client init. */
    public static void init() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "random_skins"),
                (ResourceManagerReloadListener) manager -> ALL.values().forEach(s -> s.skins = null));
    }

    /** nextInt: a number fixed for the entity, for renderers that pick more than a skin. */
    public static int nextInt(UUID uuid, int n) {
        long l = uuid.getLeastSignificantBits();
        l = l * 29506206L * (l ^ 0x6429C58L) + 25859L;
        l = l * l * 426430295004L + 25925025L * l;
        RAND.setSeed(l);
        return RAND.nextInt(n);
    }

    /** getRandomSkin(LOTRRandomSkinEntity). */
    public Identifier getRandomSkin(UUID uuid) {
        List<Identifier> list = skins();
        if (list.isEmpty()) {
            return MissingTextureAtlasSprite.getLocation();
        }
        long l = uuid.getLeastSignificantBits();
        long hash = this.skinPath.hashCode();
        l = l * 39603773L ^ l * 6583690632L ^ hash;
        l = l * hash * 2906920L + l * 65936063L;
        RAND.setSeed(l);
        return list.get(RAND.nextInt(list.size()));
    }

    private List<Identifier> skins() {
        if (this.skins == null) {
            this.skins = load(Minecraft.getInstance().getResourceManager());
        }
        return this.skins;
    }

    /** loadAllRandomSkins: count up from 0, giving up after ten missing numbers. */
    private List<Identifier> load(ResourceManager manager) {
        List<Identifier> list = new ArrayList<>();
        int skips = 0;
        boolean foundAfterSkip = false;
        for (int skinCount = 0; ; ++skinCount) {
            Identifier skin = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
                    "textures/entity/" + this.dir + "/" + skinCount + ".png");
            if (manager.getResource(skin).isEmpty()) {
                if (++skips >= 10) {
                    break;
                }
                continue;
            }
            list.add(skin);
            if (skips > 0) {
                foundAfterSkip = true;
            }
        }
        if (list.isEmpty()) {
            LOGGER.warn("LOTR: No random skins for {}", this.skinPath);
        }
        if (foundAfterSkip) {
            LOGGER.warn("LOTR: Random skins {} skipped a number. This is not good - please number your skins from 0 and upwards, with no gaps!", this.skinPath);
        }
        return list;
    }
}
