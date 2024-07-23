package net.scoobis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;

public class EncagedClient implements ClientModInitializer {
	Logger logger = LoggerFactory.getLogger("Encaged");
	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register(new ScreenEvents.AfterInit() {
			@Override
			public void afterInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
				EncagedConfig config = EncagedConfig.loadConfig();
				if (config.isServerForced() && config.isEnabled()) {
					if (screen instanceof TitleScreen) {
						ServerInfo info = new ServerInfo("Server", config.getServerIP(), ServerType.OTHER);
						ConnectScreen.connect(new TitleScreen(), MinecraftClient.getInstance(), ServerAddress.parse(config.getServerIP()), info, false, null);
					}
				}
			}
		});
	}
}