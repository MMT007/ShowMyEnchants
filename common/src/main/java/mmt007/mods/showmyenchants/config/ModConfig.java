package mmt007.mods.showmyenchants.config;

import java.io.*;
import java.util.Arrays;

public class ModConfig{
    public static ConfigOption<Boolean> ENABLED = new ConfigOption<>(true);
    public static ConfigOption<Boolean> HINT_TOOLTIP = new ConfigOption<>(false);
    public static ConfigOption<TextFont> FONT = new ConfigOption<>(TextFont.NORMAL);

    public static class FileManager {
        private static File config_file;

        public static void init(File config_file){FileManager.config_file = config_file;}

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
