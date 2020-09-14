package de.ipepper.commands;

import com.fasterxml.jackson.databind.JsonNode;
import de.ipepper.Config;
import de.ipepper.commands.types.CommandContext;
import de.ipepper.commands.types.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        String allowedRole = Config.get("meme_role");
        boolean isOwner = ctx.getMember().getUser().getId().equals(Config.get("owner_id"));
        Role memeAllowed = findRole(member, allowedRole);

        if(memeAllowed != null || isOwner) {
            WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> {
                if (!json.get("success").asBoolean()) {
                    channel.sendMessage("Something went wrong, try again later").queue();
                    System.out.println(json);
                    return;
                }

                final JsonNode data = json.get("data");
                final String title = data.get("title").asText();
                final String url = data.get("url").asText();
                final String image = data.get("image").asText();
                final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);

                channel.sendMessage(embed.build()).queue();
            });
        } else {
            channel.sendMessage("You are not permitted to use this command!").complete().delete().queueAfter(3, TimeUnit.SECONDS);
            return;
        }
    }

    private Role findRole(Member member, String name) {
        List<Role> roles = member.getRoles();
        return roles.stream()
                .filter(role -> role.getName().equals(name)) // filter by role name
                .findFirst() // take first result
                .orElse(null); // else return null
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp() {
        return "Shows a random meme from Reddit\n" +
                "Usage: `" + Config.get("prefix") + "help " + getName() + "`";
    }
}
