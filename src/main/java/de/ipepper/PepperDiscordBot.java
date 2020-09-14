package de.ipepper;

import de.ipepper.listeners.Listener;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;

import javax.security.auth.login.LoginException;

public class PepperDiscordBot {

    private static PepperDiscordBot INSTANCE;
    private JDA jdaInstance;
    private CommandManager commandManager;

    public static void main(String[] args) {
        try {
            new PepperDiscordBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public PepperDiscordBot() throws LoginException {

        INSTANCE = this;

        WebUtils.setUserAgent("PeppersDiscordBot JDA");
        EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder()
            .setColor(0x3883d9)
            .setFooter("PeppersBot")
        );

        JDABuilder builder = JDABuilder.createDefault(Config.get("token"));

        builder.setActivity(Activity.playing("RunEscape"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setCompression(Compression.NONE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setEnableShutdownHook(true);

        builder.addEventListeners(new Listener());

        this.commandManager = new CommandManager();

        jdaInstance = builder.build();
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public JDA getJdaInstance() {
        return this.jdaInstance;
    }

    public static PepperDiscordBot getInstance() {
        return INSTANCE;
    }


}
