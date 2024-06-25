package net.scoobis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.loader.api.FabricLoader;

public class EncagedConfig {
    public static final EncagedConfig DEFAULT = new EncagedConfig();

    public static final EncagedConfig loadConfig() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("encaged.json");

        if (!Files.exists(path)) {
            return new EncagedConfig();
        }
        BufferedReader br;
        try {
            br = Files.newBufferedReader(path);
            String jsonString = br.readLine();
            JsonObject json = (JsonObject) JsonParser.parseString(jsonString);
            EncagedConfig config = new EncagedConfig();

            config.enabled = json.get("enabled").getAsBoolean();
            config.serverForced = json.get("serverForced").getAsBoolean();
            config.disableDisconnect = json.get("disableDisconnect").getAsBoolean();
            config.disableQuit = json.get("disableQuit").getAsBoolean();
            config.serverIP = json.get("serverIP").getAsString();

            return config;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new EncagedConfig();
    }

    public void saveConfig() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("encaged.json");

        JsonObject configJson = new JsonObject();

        configJson.addProperty("enabled", this.enabled);
        configJson.addProperty("serverForced", this.serverForced);
        configJson.addProperty("disableDisconnect", this.disableDisconnect);
        configJson.addProperty("disableQuit", this.disableQuit);
        configJson.addProperty("serverIP", this.serverIP);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write(configJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean enabled = true;
    private boolean serverForced = true;
    private boolean disableDisconnect = true;
    private boolean disableQuit = true;
    private String serverIP = "legitimoose.com";

    public EncagedConfig() {}

    public EncagedConfig(
        boolean enabled
    )
    {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean arg1) {
        this.enabled = arg1;
    }

    public boolean isServerForced() {
        return serverForced;
    }

    public void setServerForced(boolean arg1) {
        this.serverForced = arg1;
    }

    public boolean isDisconnectDisabled() {
        return disableDisconnect;
    }

    public void setDisconnectDisabled(boolean arg1) {
        this.disableDisconnect = arg1;
    }

    public boolean isQuitDisabled() {
        return disableQuit;
    }

    public void setQuitDisabled(boolean arg1) {
        this.disableQuit = arg1;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String arg1) {
        this.serverIP = arg1;
    }
}
