package net.scoobis.mixin.client;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.scoobis.EncagedConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin {
    @Inject(method = "disconnect", at = @At("HEAD"), cancellable = true)
	public void disconnect(CallbackInfo info) {
        EncagedConfig config = EncagedConfig.loadConfig();
        if (config.isEnabled() && config.isDisconnectDisabled()) {
            info.cancel();
        }
    }
}