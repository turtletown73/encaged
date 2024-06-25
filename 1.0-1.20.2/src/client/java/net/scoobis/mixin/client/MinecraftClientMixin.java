package net.scoobis.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.scoobis.EncagedConfig;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
    public void stop(CallbackInfo info) {
        EncagedConfig config = EncagedConfig.loadConfig();
        if (config.isEnabled() && config.isQuitDisabled()) {
            info.cancel();
        }
    }
}
