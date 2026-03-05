package mmt007.mods.showmyenchants.config;

public enum TextFont {
    NORMAL("default", true),
    STAND_GALACTIC_ALPHA("alt", true),
    RUNATICA("runatica", false);

    private final String font;
    private final boolean isVanilla;
    TextFont(String font, boolean isVanilla){this.font = font; this.isVanilla = isVanilla;}

    public <T> T transform(TextFontTransform<T> func) {return func.transform(font, isVanilla);}
    public String getTranslationKey() {return "option.showmyenchants.font."+font;}
    public static String getNameOf(TextFont textFont){return textFont.font;}
    public static TextFont getTextFont(String font){
        for (TextFont textFont : TextFont.values()){
            if (font.equalsIgnoreCase(textFont.font))
                return textFont;
        }

        return TextFont.NORMAL;
    }

}

