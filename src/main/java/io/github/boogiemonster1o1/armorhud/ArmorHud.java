package io.github.boogiemonster1o1.armorhud;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;

public class ArmorHud implements ModInitializer {
    @Override
    public void onInitialize() {
        LogManager.getLogger(ArmorHud.class).info("[ArmorHud] Initializing");
    }
}
