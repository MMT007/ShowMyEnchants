package mmt007.mods.showmyenchants.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.Identifier;
import net.minecraft.util.TranslatableOption;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static mmt007.mods.showmyenchants.ShowMyEnchantsClient.MOD_ID;

public class ModConfig{
    public static final SimpleOption<Boolean> ENABLED;
    public static final SimpleOption<Boolean> HINT_TOOLTIP;
    public static final SimpleOption<TextFont> FONT;

    public static SimpleOption<?>[] getOptions() {
        return new SimpleOption[]{ENABLED,HINT_TOOLTIP,FONT};
    }

    private static String getKey(String key){
        return "option."+MOD_ID+"."+key;
    }

    static {
       var TEXT_FONT_CALLBACK = new SimpleOption.PotentialValuesBasedCallbacks<>(
            ImmutableList.of(TextFont.NORMAL, TextFont.STAND_GALACTIC_ALPHA, TextFont.RUNATICA),
            TextFont.CODEC
        );

        ENABLED = new SimpleOption<>(getKey("enabled"), SimpleOption.emptyTooltip(), SimpleOption.BOOLEAN_TEXT_GETTER, SimpleOption.BOOLEAN, true, aBoolean -> {});
        HINT_TOOLTIP = new SimpleOption<>(getKey("hint_tooltip"), SimpleOption.emptyTooltip(), SimpleOption.BOOLEAN_TEXT_GETTER, SimpleOption.BOOLEAN, false, aBoolean ->{});
        FONT = new SimpleOption<>(getKey("font"), SimpleOption.emptyTooltip(), SimpleOption.enumValueText(), TEXT_FONT_CALLBACK, TextFont.NORMAL, aFont ->{});
    }

    public enum TextFont implements TranslatableOption {
        NORMAL(Identifier.ofVanilla("default")),
        STAND_GALACTIC_ALPHA(Identifier.ofVanilla("alt")),
        RUNATICA(Identifier.of(MOD_ID,"runatica"));

        public static final Codec<TextFont> CODEC = RecordCodecBuilder.create(textFontInstance -> textFontInstance.group(
           Codec.STRING.fieldOf("name").forGetter(TextFont::getNameOf)
        ).apply(textFontInstance, TextFont::getTextFont));

        private final Identifier font;
        TextFont(Identifier font){this.font = font;}
        public Identifier getFont() {return font;}

        public static String getNameOf(TextFont textFont){return textFont.name().toLowerCase();}
        public static TextFont getTextFont(String name){
            for (TextFont textFont : TextFont.values()){
                if (name.equalsIgnoreCase(textFont.name()))
                    return textFont;
            }

            return TextFont.NORMAL;
        }

        public int getId() {return 0;}
        public String getTranslationKey() {return "option.showmyenchants.font."+getNameOf(this);}
    }

    public static class FileManager {
        private static final File config_file = FabricLoader.getInstance().getConfigDir().resolve("showmyenchants.opt").toFile();

        public static void load(){
            try {
                InputStream reader = new FileInputStream(config_file);
                byte[] contents = reader.readAllBytes();

                if(contents.length == 3){
                    ModConfig.ENABLED.setValue(contents[0] != 0);
                    ModConfig.HINT_TOOLTIP.setValue(contents[1] != 0);
                    ModConfig.FONT.setValue(TextFont.values()[contents[2] & 0b11]);
                }

                reader.close();
            } catch (Exception ignored) {}
        }

        public static void save(){
            try {
                OutputStream writer = new FileOutputStream(config_file);
                byte[] contents = new byte[]{
                    (byte) (ModConfig.ENABLED.getValue() ? 1 : 0),
                    (byte) (ModConfig.HINT_TOOLTIP.getValue() ? 1 : 0),
                    (byte) (Arrays.stream(TextFont.values()).toList().indexOf(ModConfig.FONT.getValue()))
                };

                writer.write(contents);
                writer.close();
            } catch (Exception ignored) {}
        }
    }
}
