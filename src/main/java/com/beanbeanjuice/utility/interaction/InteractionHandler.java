package com.beanbeanjuice.utility.interaction;

import com.beanbeanjuice.main.CafeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/6.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/punches/8.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/5.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/14.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/5.webp");
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/16.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/17.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/18.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/19.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/20.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/blushes/21.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random slap image.
     */
    public String getSlapImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/slaps/1.webp");
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/hmphs/8.webp");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/11.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pouts/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/12.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/smiles/15.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/12.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/stares/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random tickle image.
     */
    public String getTickleImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/2.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/3.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/tickles/10.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/10.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/9.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/10.webp");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/yells/11.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random head pat image.
     */
    public String getHeadPatImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media0.giphy.com/media/ARSp9T7wwxNcs/giphy.gif");
        arrayList.add("https://media.tenor.com/images/ad8357e58d35c1d63b570ab7e587f212/tenor.gif");
        arrayList.add("https://i.pinimg.com/originals/1a/94/61/1a9461f15bb5ae752680d99195f9b944.gif");
        arrayList.add("https://media1.tenor.com/images/d7c326bd43776f1e0df6f63956230eb4/tenor.gif?itemid=17187002");
        arrayList.add("https://i.pinimg.com/originals/ba/0a/18/ba0a18b4028f9c210f830f7a82a574cb.gif");
        arrayList.add("https://i.postimg.cc/fTZRHj4V/Adorable-raccoon-girl-headpat.gif");
        arrayList.add("https://64.media.tumblr.com/acab0232bfb5cfd5d2d45e55e9dae898/tumblr_pbxhq7GyIf1th206io1_500.gifv");
        arrayList.add("https://media.tenor.com/images/50b500c0fc0ad01a974af8b58b5e0c9b/tenor.gif");
        arrayList.add("https://thumbs.gfycat.com/AgileHeavyGecko-max-1mb.gif");
        arrayList.add("https://i.imgur.com/aPSW2uf.gif?noredirect");
        arrayList.add("https://media.tenor.com/images/da8431374a530ae516c0cc8f966d1c2b/tenor.gif");
        arrayList.add("https://i.imgur.com/42VnOL9.gif");
        arrayList.add("https://64.media.tumblr.com/80f4e1aeee44dee530b1e6b416a8459d/83ad7e3b43d48041-53/s500x750/ddbb45d884338428dd0f1e042099b353fd3f49b3.gifv");
        arrayList.add("https://pa1.narvii.com/6680/1edd1db645f55aa7f2923838b5afabfc863fc109_hq.gif");
        arrayList.add("https://thumbs.gfycat.com/FlimsyDeafeningGrassspider-max-1mb.gif");
        arrayList.add("https://media.tenor.com/images/385a8d13c1ee5213e560e07d12320d02/tenor.gif");
        arrayList.add("https://64.media.tumblr.com/c90065c0ad52fe54d6de824507ebb68b/tumblr_pj79r3W3wU1th206io1_1280.gifv");
        arrayList.add("https://i.pinimg.com/originals/ec/b8/7f/ecb87fb2827a022884d5165046f6608a.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random cry image.
     */
    public String getCryImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media3.giphy.com/media/ukfn7kMzzLqLeyi5Tt/giphy.gif");
        arrayList.add("https://i.pinimg.com/originals/b4/b1/64/b4b1640525ecadfa1030e6096f3ec842.gif");
        arrayList.add("https://media1.tenor.com/images/09b085a6b0b33a9a9c8529a3d2ee1914/tenor.gif?itemid=5648908");
        arrayList.add("https://media.tenor.com/images/b261321eff758fb664ea6ff110fec20a/tenor.gif");
        arrayList.add("https://steamuserimages-a.akamaihd.net/ugc/860612455483948951/A1597F74D3C26AB279E6C02C2D05B18831D30C32/");
        arrayList.add("https://media.tenor.com/images/19089cd2b4970740debff2cdfc43329a/tenor.gif");
        arrayList.add("https://media4.giphy.com/media/yarJ7WfdKiAkE/giphy.gif");
        arrayList.add("https://media1.tenor.com/images/ce52606293142a2bd11cda1d3f0dc12c/tenor.gif?itemid=5184314");
        arrayList.add("https://media3.giphy.com/media/8YutMatqkTfSE/200.gif");
        arrayList.add("https://66.media.tumblr.com/tumblr_m4soo4uyBO1qj4s4bo1_400.gif");
        arrayList.add("https://s11.favim.com/orig/7/719/7192/71923/gif-anime-girl-sad-Favim.com-7192355.gif");
        arrayList.add("https://media.tenor.com/images/cac86b83b562bc6add7e5e34311a8588/tenor.gif");
        arrayList.add("https://thumbs.gfycat.com/CompletePotableDove-max-1mb.gif");
        arrayList.add("https://cdn140.picsart.com/331786189096201.gif?to=min&r=640");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
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
     * @param args The {@link ArrayList<String>} for the message embed.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed actionWithDescriptionEmbed(@NotNull String link, @NotNull ArrayList<String> args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("\"");

        for (int i = 1; i < args.size(); i++) {
            descriptionBuilder.append(args.get(i));
            if (i != args.size() - 1) {
                descriptionBuilder.append(" ");
            }
        }

        descriptionBuilder.append("\"");
        embedBuilder.setDescription(descriptionBuilder.toString());
        return embedBuilder.build();
    }

}
