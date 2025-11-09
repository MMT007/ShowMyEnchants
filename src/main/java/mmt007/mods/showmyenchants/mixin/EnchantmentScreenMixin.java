package mmt007.mods.showmyenchants.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mmt007.mods.showmyenchants.config.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin extends HandledScreenMixin<EnchantmentScreenHandler> {
    @Unique private static final float scale_factor = 0.87f;
    @Unique private static final float scale_factor_inv = 1 / scale_factor;

    @Unique private static final ThreadLocal<Integer> CURRENT_INDEX = new ThreadLocal<>();

    public EnchantmentScreenMixin(EnchantmentScreenHandler handler) {
        super(handler);
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantingPhrases;generatePhrase(Lnet/minecraft/client/font/TextRenderer;I)Lnet/minecraft/text/StringVisitable;"))
    private void captureIndex(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci, @Local(ordinal = 5) int l){
        CURRENT_INDEX.set(l);
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantingPhrases;generatePhrase(Lnet/minecraft/client/font/TextRenderer;I)Lnet/minecraft/text/StringVisitable;", shift = At.Shift.AFTER))
    private void captureIndex(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci){
        CURRENT_INDEX.remove();
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawWrappedText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/StringVisitable;IIIIZ)V"))
    private static void pushMatrixScaling(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci){
        if(ModConfig.ENABLED.getValue()) {
            context.getMatrices().push();
            context.getMatrices().scale(scale_factor,scale_factor,scale_factor);
        }
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawWrappedText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/StringVisitable;IIIIZ)V", shift = At.Shift.AFTER))
    private static void popMatrixScaling(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci){
        if(ModConfig.ENABLED.getValue()) {
            context.getMatrices().pop();
        }
    }

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawWrappedText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/StringVisitable;IIIIZ)V"))
    private static void drawCorrection(DrawContext instance, TextRenderer textRenderer, StringVisitable text, int x, int y, int width, int color, boolean shadow){
        if(ModConfig.ENABLED.getValue()) {
            int lines = textRenderer.wrapLines(text, width).size();
            int text_y = (int) ((y + (lines > 1 ? 0 : lines * 9 / 2f)) * scale_factor_inv) + 1;
            instance.drawWrappedText(textRenderer, text, (int) (x * scale_factor_inv), text_y, (int) (width * scale_factor_inv), color,shadow);
        }else instance.drawWrappedText(textRenderer, text, x, y, width, color,shadow);
    }

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantingPhrases;generatePhrase(Lnet/minecraft/client/font/TextRenderer;I)Lnet/minecraft/text/StringVisitable;"))
    public StringVisitable generateEnchantmentPhrase(EnchantingPhrases instance, TextRenderer textRenderer, int width){
        if(!ModConfig.ENABLED.getValue()) return instance.generatePhrase(textRenderer, width);

        assert this.client.world != null;

        Optional<RegistryEntry.Reference<Enchantment>> optional = this.client.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT)
            .getEntry((this.handler).enchantmentId[CURRENT_INDEX.get()]);
        int l = (this.handler).enchantmentLevel[CURRENT_INDEX.get()];

        return optional.map(enchantmentReference -> (
                (MutableText) Enchantment.getName(enchantmentReference, l))
                    .setStyle(Style.EMPTY.withFont(ModConfig.FONT.getValue().getFont()))
               ).orElse(Text.literal(". . . ?"));
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z",ordinal = 0))
    private <E> boolean removeEnchantmentTooltip(List<E> instance, E e){
        if(!(ModConfig.ENABLED.getValue() && !ModConfig.HINT_TOOLTIP.getValue())) return instance.add(e);
        return false;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z",ordinal = 1))
    private <E> boolean removeEnchantmentTooltipSpacer(List<E> instance, E e){
        if(!(ModConfig.ENABLED.getValue() && !ModConfig.HINT_TOOLTIP.getValue())) return instance.add(e);
        return false;
    }


}

