package net.scoobis.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Keyboard;
import net.scoobis.EncagedConfig;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "pollDebugCrash", at = @At("HEAD"), cancellable = true)
    public void pollDebugCrash(CallbackInfo ci) {
        if (EncagedConfig.loadConfig().isEnabled()) {
            ci.cancel();
        }
    }
}
