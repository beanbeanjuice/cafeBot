package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUsersEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class BalanceCommand extends Command implements ICommand {

    public BalanceCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));
        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        CafeUsersEndpoint endpoint = cafeBot.getCafeAPI().getCafeUsersEndpoint();

        endpoint.getAndCreateCafeUser(user.getId())
                .thenAcceptAsync((cafeUser) -> event.getHook().sendMessageEmbeds(balanceEmbed(user, cafeUser)).queue())
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Getting Balance",
                            "There was some sort of error getting your coin balance."
                    )).queue();
                    return null;
                });
    }

    public MessageEmbed balanceEmbed(final User user, final CafeUser cafeUser) {
        return new EmbedBuilder()
                .setTitle("beanCoin Balance")
                .setColor(Helper.getRandomColor())
                .addField("Orders Bought", String.valueOf(cafeUser.getOrdersBought()), true)
                .addField("Orders Received", String.valueOf(cafeUser.getOrdersReceived()), true)
                .setDescription(user.getAsMention() + " has a current balance of `$" + Helper.roundDouble(cafeUser.getBeanCoins()) + "` bC (beanCoins)!")
                .setFooter("To learn how to make money do /help serve")
                .build();
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Get your balance!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to get the balance of.", false)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

}
