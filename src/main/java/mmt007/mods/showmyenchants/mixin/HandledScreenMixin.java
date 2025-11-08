package mmt007.mods.showmyenchants.mixin;

import com.google.common.collect.Sets;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends ScreenMixin{
    @Mutable @Final @Shadow protected final T handler;

    protected HandledScreenMixin(T handler) {
        this.handler = handler;
    }
}
