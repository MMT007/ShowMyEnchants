package mmt007.mods.showmyenchants.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static mmt007.mods.showmyenchants.Mod.MOD_ID;

public class ModOptionScreen extends GameOptionsScreen {
    protected ModOptionScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("option.showmyenchants.title"));
    }

    @Override
    public void removed() {
        ModConfig.FileManager.save();
    }

    @Override
    protected void addOptions() {
        if (this.body != null) {
            var TEXT_FONT_CALLBACK = new SimpleOption.PotentialValuesBasedCallbacks<>(
                ImmutableList.of(TextFont.NORMAL, TextFont.STAND_GALACTIC_ALPHA, TextFont.RUNATICA),
                fontCodec()
            );

            var enabled_option = new SimpleOption<>(
                getKey("enabled"), SimpleOption.emptyTooltip(),
                ModOptionScreen::getBooleanText, SimpleOption.BOOLEAN,
                ModConfig.ENABLED.getValue(), v -> ModConfig.ENABLED.setValue(v)
            );

            var hint_tooltip_option = new SimpleOption<>(
                getKey("hint_tooltip"), SimpleOption.emptyTooltip(),
                ModOptionScreen::getBooleanText, SimpleOption.BOOLEAN,
                ModConfig.HINT_TOOLTIP.getValue(), v -> ModConfig.HINT_TOOLTIP.setValue(v)
            );

            var text_font_option = new SimpleOption<>(
                getKey("font"), SimpleOption.emptyTooltip(),
                ModOptionScreen::getFontText, TEXT_FONT_CALLBACK,
                ModConfig.FONT.getValue(), v -> ModConfig.FONT.setValue(v)
            );

            this.body.addAll(enabled_option, hint_tooltip_option, text_font_option);
        }
    }

    private static Text getBooleanText(Text optionText, Boolean value) {
        return value ? Text.translatable("gui.yes").formatted(Formatting.GREEN) : Text.translatable("gui.no").formatted(Formatting.RED);
    }

    private static Text getFontText(Text optionText, TextFont value) {
        return Text.translatable(value.getTranslationKey());
    }

    private static Codec<TextFont> fontCodec() {
        return RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("textFont").forGetter(TextFont::getNameOf)
        ).apply(inst, TextFont::getTextFont));
    }

    private static String getKey(String key) {
        return String.format("option.%s.%s", MOD_ID, key);
    }
}
