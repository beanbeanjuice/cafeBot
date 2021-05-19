package com.beanbeanjuice.utility.interaction;

import com.beanbeanjuice.main.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A custom class used for Interaction Commands
 *
 * @author beanbeanjuice
 */
public class InteractionHandler {

    /**
     * @return A random hug image.
     */
    public String getHugImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/16.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/17.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/18.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/19.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/20.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/21.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hugs/22.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random punch image.
     */
    public String getPunchImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/8.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random kiss image.
     */
    public String getKissImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/14.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random bite image.
     */
    public String getBiteImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/16.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bites/17.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random blush image.
     */
    public String getBlushImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/16.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/17.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/18.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/19.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/20.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/21.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random cuddle image.
     */
    public String getCuddleImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cuddles/15.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random nom image.
     */
    public String getNomImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/16.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random poke image.
     */
    public String getPokeImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/noms/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random slap image.
     */
    public String getSlapImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/14.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random stab image.
     */
    public String getStabImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stabs/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stabs/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stabs/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stabs/4.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random hmph image.
     */
    public String getHmphImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/8.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random pout image.
     */
    public String getPoutImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random throw image.
     */
    public String getThrowImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/throws/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/throws/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/throws/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/throws/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/throws/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/throws/6.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random smile image.
     */
    public String getSmileImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/15.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random stare image.
     */
    public String getStareImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random tickle image.
     */
    public String getTickleImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/10.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random rage image.
     */
    public String getRageImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random yell image.
     */
    public String getYellImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/11.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random head pat image.
     */
    public String getHeadPatImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/16.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/17.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/headpats/18.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random cry image.
     */
    public String getCryImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/cries/14.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    public String getDanceImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dances/12.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * Gets the receivers {@link String} for the Interaction Commands.
     * @param receivers The {@link ArrayList<User>} to be used as the receivers.
     * @return The new receiver {@link String}.
     */
    @NotNull
    public String getReceiverString(ArrayList<User> receivers) {
        StringBuilder receiverBuilder = new StringBuilder();

        for (int i = 0; i < receivers.size(); i++) {
            receiverBuilder.append(receivers.get(i).getName());

            if (i != receivers.size() - 1) {
                receiverBuilder.append(", ");
            }

            if (i == receivers.size() - 2) {
                receiverBuilder.append(" and ");
            }
        }
        return receiverBuilder.toString();
    }

    /**
     * @param link The image URL for the {@link MessageEmbed}.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed actionEmbed(@NotNull String link) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    /**
     * @param link The image URL for the {@link MessageEmbed}.
     * @param description The {@link String} description for the message embed.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed actionWithDescriptionEmbed(@NotNull String link, @NotNull String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("\"").append(description).append("\"");
        embedBuilder.setDescription(descriptionBuilder.toString());
        return embedBuilder.build();
    }

}
