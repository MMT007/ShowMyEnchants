package mmt007.mods.showmyenchants;

import mmt007.mods.showmyenchants.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModConfig.FileManager.init(FabricLoader.getInstance().getConfigDir().resolve("showmyenchants.opt").toFile());
        ModConfig.FileManager.load();
    }
}
