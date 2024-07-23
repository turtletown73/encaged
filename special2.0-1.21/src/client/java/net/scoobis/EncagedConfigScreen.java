package net.scoobis;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class EncagedConfigScreen {
    public Screen createScreen(Screen parent) {
        EncagedConfig config = EncagedConfig.loadConfig();
        
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.literal("Encaged Config (updated to not save at all)"));
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        BooleanListEntry enabled = entryBuilder
            .startBooleanToggle(Text.literal("Enabled"), config.isEnabled())
            .setTooltip(Text.literal("Whether Encaged is enabled or not"))
            .build();

        BooleanListEntry forceserverjoin = entryBuilder
            .startBooleanToggle(Text.literal("Force Join"), config.isServerForced())
            .setTooltip(Text.literal("Whether Encaged forces you to join the server when on title screen or not"))
            .build();

        BooleanListEntry disabledisconnect = entryBuilder
            .startBooleanToggle(Text.literal("Disable Disconnect"), config.isDisconnectDisabled())
            .setTooltip(Text.literal("Whether Encaged disables the disconnect button or not"))
            .build();

        BooleanListEntry disablequit = entryBuilder
            .startBooleanToggle(Text.literal("Disable Quit"), config.isQuitDisabled())
            .setTooltip(Text.literal("Whether Encaged disables quitting the game or not"))
            .build();

        StringListEntry serverip = entryBuilder
            .startStrField(Text.literal("Server IP"), config.getServerIP())
            .setTooltip(Text.literal("Sets the server ip of the forced server"))
            .build();

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        general.addEntry(enabled);
        general.addEntry(forceserverjoin);
        general.addEntry(disabledisconnect);
        general.addEntry(disablequit);
        general.addEntry(serverip);

        builder.setSavingRunnable(() -> {
            config.setEnabled(enabled.getValue());
            config.setServerForced(forceserverjoin.getValue());
            config.setDisconnectDisabled(disabledisconnect.getValue());
            config.setQuitDisabled(disablequit.getValue());
            config.setServerIP(serverip.getValue());
        });

        return builder.build();
    }
}
