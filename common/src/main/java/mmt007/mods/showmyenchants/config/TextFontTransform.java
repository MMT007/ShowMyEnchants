package mmt007.mods.showmyenchants.config;

@FunctionalInterface
public interface TextFontTransform<T> {
    T transform(String font, Boolean isVanilla);
}
