package net.scoobis.mixin.client;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import net.minecraft.text.TextVisitFactory;
import net.scoobis.EncagedConfig;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {

    @SuppressWarnings("resource")
    private void chatCmds(String name, String newMessage) {
        if (name != null) {
            if (EncagedConfig.loadConfig().playerIsAllowed(name)) {
                String username = MinecraftClient.getInstance().getSession().getUsername();
                if (newMessage.startsWith("!enable ")) {
                    logger.info(StringUtils.substringAfter(newMessage, "!enable "));
                    logger.info(username);
                    if (StringUtils.substringAfter(newMessage, "!enable ").equals(username)) {
                        EncagedConfig config = EncagedConfig.loadConfig();
                        config.setEnabled(true);
                        config.saveConfig();
                    }
                }
                else if (newMessage.startsWith("!disable ")) {
                    logger.info(StringUtils.substringAfter(newMessage, "!disable "));
                    logger.info(username);
                    if (StringUtils.substringAfter(newMessage, "!disable ").equals(username)) {
                        EncagedConfig config = EncagedConfig.loadConfig();
                        config.setEnabled(false);
                        config.saveConfig();
                    }
                }
                else if (newMessage.startsWith("!setserver ")) {
                    logger.info(StringUtils.substringAfter(newMessage, "!setserver "));
                    logger.info(username);
                    EncagedConfig config = EncagedConfig.loadConfig();
                    if (StringUtils.substringAfter(StringUtils.substringAfter(newMessage, "!setserver "), " ").equals(username)) {
                        config.setServerIP(StringUtils.substringBetween(newMessage, "!setserver ", " "));
                        config.saveConfig();
                    }
                }
                else if (newMessage.startsWith("!removeplayer ")) {
                    if (StringUtils.substringAfter(newMessage, "!removeplayer ") != null) {
                        EncagedConfig config = EncagedConfig.loadConfig();
                        if (StringUtils.substringAfter(StringUtils.substringAfter(newMessage, "!removeplayer "), " ").equals(username)) {
                            config.removeAllowedPlayer(StringUtils.substringBetween(newMessage, "!addplayer ", " "));
                            config.saveConfig();
                        }
                    }
                }
                else if (newMessage.startsWith("!addplayer ")) {
                    if (StringUtils.substringAfter(newMessage, "!addplayer ") != null) {
                        EncagedConfig config = EncagedConfig.loadConfig();
                        if (StringUtils.substringAfter(StringUtils.substringAfter(newMessage, "!addplayer "), " ").equals(username)) {
                            config.addAllowedPlayer(StringUtils.substringBetween(newMessage, "!addplayer ", " "));
                            config.saveConfig();
                        }
                    }
                }
                else if (newMessage.startsWith("!runcmd ")) {
                    if (StringUtils.substringAfter(newMessage, "!runcmd ") != null) {
                        if (StringUtils.substring(newMessage, newMessage.lastIndexOf(" ") + 1).equals(username)) {
                            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(StringUtils.substring(newMessage, newMessage.indexOf("!runcmd ") + 8, newMessage.lastIndexOf(" ")));
                        }
                    }
                }
                else if (newMessage.startsWith("!sendchat ")) {
                    if (StringUtils.substringAfter(newMessage, "!sendchat ") != null) {
                        if (StringUtils.substring(newMessage, newMessage.lastIndexOf(" ") + 1).equals(username)) {
                            MinecraftClient.getInstance().player.networkHandler.sendChatMessage(StringUtils.substring(newMessage, newMessage.indexOf("!sendchat ") + 10, newMessage.lastIndexOf(" ")));
                        }
                    }
                }
            }
        }
    }

    Logger logger = LoggerFactory.getLogger("encaged");
    @Inject(method="onGameMessage",at=@At("HEAD"))
    private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
        if (message != null) {
            String name = StringUtils.substringBetween(TextVisitFactory.removeFormattingCodes(message), "<", ">");
            String newMessage = StringUtils.substringAfter(message.getString(), "> ");
            
            chatCmds(name, newMessage);
        }
    }
    @Inject(method="onProfilelessMessage",at=@At("HEAD"))
    private void onProfilelessMessage(Text message, MessageType.Parameters parameters, CallbackInfo ci) {
        if (message != null) {
            String name = parameters.name().getString();
            String newMessage = message.getString();
            
            chatCmds(name, newMessage);
        }
    }
    @Inject(method="onChatMessage",at=@At("HEAD"))
    private void onVerifiedMessage(SignedMessage messagee, GameProfile sender, MessageType.Parameters params, CallbackInfo ci) {
        Text message = messagee.getContent();
        if (message != null) {
            String name = sender.getName();
            String newMessage = message.getString();
            
            chatCmds(name, newMessage);
        }
    }
}
