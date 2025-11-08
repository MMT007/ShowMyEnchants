package mmt007.mods.showmyenchants;

import mmt007.mods.showmyenchants.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class ShowMyEnchantsClient implements ClientModInitializer {
    public static final String MOD_ID = "showmyenchants";

    @Override
    public void onInitializeClient() {
        ModConfig.FileManager.load();
    }
}
