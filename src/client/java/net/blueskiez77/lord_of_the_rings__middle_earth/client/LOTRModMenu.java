package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.gui.LOTRConfigScreen;

/** Mod Menu's config button opens the mod's config screen, as Forge's mod list did (LOTRGuiFactory). */
public final class LOTRModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return LOTRConfigScreen::new;
    }
}
