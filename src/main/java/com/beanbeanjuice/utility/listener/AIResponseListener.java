package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A {@link ListenerAdapter} that listens to {@link MessageReceivedEvent} and gives an
 * appropriate response depending on if it is enabled in the {@link Guild}.
 *
 * @author beanbeanjuice
 * @since v3.0.0
 */
public class AIResponseListener extends ListenerAdapter {

    private final HashMap<ArrayList<String>, ArrayList<String>> messageMap;

    /**
     * Create a new {@link AIResponseListener} object.
     */
    public AIResponseListener() {
        messageMap = new HashMap<>();

        createMaps();
    }

    private void createMaps() {
        createHelloMaps();
        createLOLMaps();
        createSproutMaps();
        createCafeBotMaps();
        createKenzieMaps();
        createSorryMaps();
        createAttackMaps();
        createPPMaps();
        createNahMaps();
        createSTFUMaps();
        createGenevieveMaps();
        createMikoriMaps();
        createHPMaps();
        createMeowMaps();
        createWoofMaps();
    }

    private void createHelloMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("hello");
        commandTerms.add("hi");
        commandTerms.add("hey");
        commandTerms.add("hai");

        responses.add("Hi, {user}!");
        responses.add("Hey hey, {user}! ^-^");
        responses.add("Hey, {user}! How are you?");
        responses.add("Hello, {user}.");
        responses.add("Hi! ^-^ The weather's nice today right? I can't tell... I'm just a robot...");
        responses.add("Hi hi {user}! How has your day been so far? Well, I hope it has been well...");
        responses.add("Hey hey {user}! Do you want to order some coffee from me?");

        messageMap.put(commandTerms, responses);
    }

    private void createLOLMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("lol");
        commandTerms.add("haha");
        commandTerms.add("lmao");
        commandTerms.add("xd");
        commandTerms.add("bruh");
        commandTerms.add("bro");
        commandTerms.add("lmfao");

        responses.add("Really? That's all you can do to respond?");
        responses.add("That response was dryer than the Sahara Desert.");
        responses.add("How long did it take you to think of that response?");
        responses.add("Maybe if you thought up a better response, you wouldn't be seeing this message.");
        responses.add("You're fun to talk to in a conversation huh?");
        responses.add("If that's all you can say, then you need some help...");
        responses.add("Is there nothing else you have to say?");
        responses.add("Can you be less of a dry texter?");
        responses.add("Could you speak any less? (If you can, please do. That response was dryer than this gin.)");

        messageMap.put(commandTerms, responses);
    }

    private void createSproutMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("sprout");
        commandTerms.add("kuromi");

        responses.add("That's a Sprout moment...");
        responses.add("If you're seeing this, then you said a word that reminds me of some clown named sprout who pulls sprout moments all the time.");
        responses.add("Imagine breaking a bee hive without silk touch in Minecraft...");
        responses.add("Stfu sprout...");
        responses.add("Soap?");
        responses.add("Who? Asked?");

        messageMap.put(commandTerms, responses);
    }

    private void createCafeBotMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("cafebot");
        commandTerms.add("cafe bot");
        commandTerms.add("what is cafebot");
        commandTerms.add("who is cafebot");
        commandTerms.add("what is cafe bot");
        commandTerms.add("who is cafe bot");
        commandTerms.add("hi cafebot");

        responses.add("Hi, {user}. I'm cafeBot! A general-purpose bot who can also serve you some coffee!");
        responses.add("Hey, that's me! ^-^");
        responses.add("Someone called?");
        responses.add("Am I a real human yet?");

        messageMap.put(commandTerms, responses);
    }

    private void createKenzieMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("kenzie");
        commandTerms.add("kenzieisme");

        responses.add("Kenzie? The doctor?");
        responses.add("Did someone say Kenzie? The one who has an AMD gpu? Pepelaugh?");

        messageMap.put(commandTerms, responses);
    }

    private void createSorryMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("im sorry");
        commandTerms.add("i'm sorry");
        commandTerms.add("i am sorry");
        commandTerms.add("sorry");

        responses.add("Don't worry, {user}! Everything will be forgiven and everything will be alright.");
        responses.add("Why are you sorry? Everything will be okay in the future. Don't worry.");
        responses.add("It's okay that you're sorry, but try not to be so hard on yourself! ^-^");

        messageMap.put(commandTerms, responses);
    }

    private void createAttackMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("stfu cafebot");
        commandTerms.add("cafebot sucks");
        commandTerms.add("cafebot is bad");
        commandTerms.add("shut up cafebot");
        commandTerms.add("cafebot is a bitch");
        commandTerms.add("i hate cafebot");
        commandTerms.add("i hate you cafebot");

        responses.add("Who asked? Literally? Who? <:stab_u:886216384864997406>");
        responses.add("I still have more of a purpose than you do.");
        responses.add("Imagine talking back to someone made of code?");
        responses.add("I'm still more intelligent than you.");
        responses.add("Imagine thinking I care?");
        responses.add("Imagine having an argument with a bot? Pepelaugh? Pepelaugh.");
        responses.add("You need friends don't you? You're literally arguing with code. <:stab_u:886216384864997406>");

        messageMap.put(commandTerms, responses);
    }

    private void createPPMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("pp");

        responses.add("poopoo");

        messageMap.put(commandTerms, responses);
    }

    private void createNahMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("nah");

        responses.add("yah");

        messageMap.put(commandTerms, responses);
    }

    private void createSTFUMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("stfu");
        commandTerms.add("shut up");

        responses.add("You stfu, {user}...");
        responses.add("You shut up, {user}.");
        responses.add("Maybe say something kinder?");

        messageMap.put(commandTerms, responses);
    }

    private void createGenevieveMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("gen");
        commandTerms.add("genevieve");
        commandTerms.add("lolitagiant");
        commandTerms.add("lolita");
        commandTerms.add("gen gen");

        responses.add("Gen? Genevieve? She's so cute!");
        responses.add("Genevieve has such cute outfits!");
        responses.add("My creator knows someone named Genevieve! Apparently they're so cute!");
        responses.add("Honestly... I think Genevieve is pretty cute.");
        responses.add("Genevieve??!? The gamer gurl? uwu");
        responses.add("If you don't think Genevieve is hot girl sh*t then get out honestly...");
        responses.add("Genevieve! ❤");
        responses.add("The only matweial gowrl that the world needs!");
        responses.add("She's stacked at royal high on roblox.");

        messageMap.put(commandTerms, responses);
    }

    private void createMikoriMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("miko");
        commandTerms.add("mikori");

        responses.add("Miko? I heard they're very mean...");
        responses.add("Mikori? Mikori... hmmm.. I've heard their name before but not good things.");
        responses.add("My creator tells me to stay away from someone named Mikori...");
        responses.add("Mikori? <:madison_when_short:843673314990882836> STAY AWAY");
        responses.add("Mikori seems like a TERRIBLE person...");

        messageMap.put(commandTerms, responses);
    }

    private void createHPMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("hp");
        commandTerms.add("horsepower");
        commandTerms.add("horse power");

        responses.add("HP? Horsepower? I heard they're friends with Miko... so I'm not sure how smart they are.");
        responses.add("I heard HP is delusional.");
        responses.add("Hmm... who's HP? Because I heard they're not kind at all.");
        responses.add("I heard her and Miko attacked someone...");

        messageMap.put(commandTerms, responses);
    }

    private void createMeowMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("meow");

        responses.add("Meow! <a:catpats:950514533875720232>");
        responses.add("猫🐱?");
        responses.add("Meow! >3<");
        responses.add("Meow meows? <:zerotwo_scream:841921420904497163>");

        messageMap.put(commandTerms, responses);
    }

    private void createWoofMaps() {
        ArrayList<String> commandTerms = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        commandTerms.add("woof");
        commandTerms.add("bark");

        responses.add("Woof woof!");
        responses.add("Woof! <a:wiggle:886217792578269236>");
        responses.add("Why are you acting like a dog? <:zerotwo_scream:841921420904497163>");

        messageMap.put(commandTerms, responses);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        try {
            Guild guild = event.getGuild();

            if (event.getAuthor().isBot())
                return;

            if (!GuildHandler.getCustomGuild(guild).getAIState())
                return;

            String message = event.getMessage().getContentRaw().toLowerCase();

            messageMap.forEach((commandTerms, commandResponses) -> {
                if (commandTerms.contains(message.replace(".", "").replace("?", ""))) {
                    event.getMessage().reply(parseMessage(
                            commandResponses.get(Helper.getRandomNumber(0, commandResponses.size())),
                            event.getAuthor()
                    )).queue();
                    Bot.commandsRun++;
                }
            });
        } catch (NullPointerException ignored) {}
    }

    @NotNull
    private String parseMessage(@NotNull String message, @NotNull User user) {
        return message.replace("{user}", user.getAsMention());
    }

}
