package mmt007.mods.showmyenchants.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModOptionScreen extends GameOptionsScreen {
    private static final List<Consumer<Boolean>> DISABLE_BUTTONS = new ArrayList<>();

    protected ModOptionScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options,Text.translatable("option.showmyenchants.title"));
    }

    @Override
    public void removed() {
        ModConfig.FileManager.save();
    }

    @Override
    protected void addOptions() {
        if (this.body != null){
            this.body.addAll(ModConfig.getOptions());
        }
    }

    private static Text getBooleanText(Boolean value){
        return value ? Text.translatable("gui.yes").formatted(Formatting.GREEN) : Text.translatable("gui.no").formatted(Formatting.RED);
    }
}
