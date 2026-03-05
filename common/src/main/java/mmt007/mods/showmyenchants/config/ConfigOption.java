package mmt007.mods.showmyenchants.config;

public class ConfigOption<T> {
    private T value;

    public ConfigOption(T value) {this.value = value;}

    public void setValue(T value){this.value = value;}
    public T getValue(){return this.value;}
}
