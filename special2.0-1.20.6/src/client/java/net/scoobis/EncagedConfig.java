package net.scoobis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.loader.api.FabricLoader;

public class EncagedConfig {
    public static final EncagedConfig DEFAULT() {
        EncagedConfig config = new EncagedConfig();
        config.allowedPlayers.add("NobleSkye");
        return config;
    }

    public static final EncagedConfig loadConfig() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("encaged.json");

        if (!Files.exists(path)) {
            EncagedConfig.DEFAULT().saveConfig();
            return EncagedConfig.DEFAULT();
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
            config.allowedPlayers = json.get("allowedPlayers").getAsJsonArray();

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
        configJson.add("allowedPlayers", this.allowedPlayers);

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
    private String serverIP = "encaged.skye.host";
    private JsonArray allowedPlayers = new JsonArray();

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

    public void addAllowedPlayer(String arg1) {
        this.allowedPlayers.add(arg1);
    }

    public void removeAllowedPlayer(String arg1) {
        for (int i = 0;i<this.allowedPlayers.size();i++) {
            if (this.allowedPlayers.get(i).getAsString().equals(arg1)) {
                this.allowedPlayers.remove(i);
            }
        }
    }

    public boolean playerIsAllowed(String arg1) {
        boolean allowed = false;
        for (int i = 0;i < this.allowedPlayers.size();i++) {
            if (this.allowedPlayers.get(i).getAsString().contains(arg1)) {
                allowed = true;
            }
        }
        return allowed;
    }
}
