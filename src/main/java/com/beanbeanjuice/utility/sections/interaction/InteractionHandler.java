package com.beanbeanjuice.utility.sections.interaction;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/kisses/15.gif");
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/pokes/13.gif");
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
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/rages/14.gif");
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

    /**
     * @return A random dance image.
     */
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
     * @return A random dab image.
     */
    public String getDabImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dabs/11.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random bonk image.
     */
    public String getBonkImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/bonks/13.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random sleep image.
     */
    public String getSleepImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/16.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/17.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/sleeps/18.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return A random die image.
     */
    public String getDieImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/dies/7.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return a random welcome image.
     */
    public String getWelcomeImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/welcomes/9.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * @return a random lick image.
     */
    @NotNull
    public String getLickImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/cafeBot/interaction/licks/14.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()));
    }

    /**
     * Checks if the {@link User} has any {@link InteractionType}.
     * @param userID The ID of the {@link User} specified.
     * @return Whether or not if the user exists.
     * @throws SQLException An {@link SQLException} is thrown if the database cannot be contact.
     */
    @NotNull
    private Boolean checkIfSenderExists(@NotNull String userID) throws SQLException {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.interaction_senders WHERE user_id = (?);";

        PreparedStatement statement = connection.prepareStatement(arguments);
        statement.setLong(1, Long.parseLong(userID));
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            arguments = "INSERT INTO cafeBot.interaction_senders (user_id) VALUES (?);";
            statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            statement.execute();
            return true;
        }
        return true;
    }

    /**
     * Checks if the {@link User} has any {@link InteractionType}.
     * @param userID The ID of the {@link User} specified.
     * @return Whether or not if the user exists.
     * @throws SQLException An {@link SQLException} is thrown if the database cannot be contact.
     */
    @NotNull
    public Boolean checkIfReceiverExists(@NotNull String userID) throws SQLException {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.interaction_receivers WHERE user_id = (?);";

        PreparedStatement statement = connection.prepareStatement(arguments);
        statement.setLong(1, Long.parseLong(userID));
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            arguments = "INSERT INTO cafeBot.interaction_receivers (user_id) VALUES (?);";
            statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            statement.execute();
            return true;
        }
        return true;
    }

    /**
     * Gets the amount of {@link InteractionType} that a specified {@link User} has.
     * @param userID The ID of the {@link User} specified.
     * @param type The {@link InteractionType} to look for.
     * @return The amount of times that {@link User} has sent the {@link InteractionType}.
     */
    @NotNull
    public Integer getSender(@NotNull String userID, @NotNull InteractionType type) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.interaction_senders WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            ResultSet resultSet = statement.executeQuery();

            resultSet.first();
            return resultSet.getInt(type.getSQLColumn());
        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Gets the amount of {@link InteractionType} that a specified {@link User} has.
     * @param userID The ID of the {@link User} specified.
     * @param type The {@link InteractionType} to look for.
     * @return The amount of times that {@link User} has received the {@link InteractionType}.
     */
    @NotNull
    public Integer getReceiver(@NotNull String userID, @NotNull InteractionType type) {
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "SELECT * FROM cafeBot.interaction_receivers WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(userID));
            ResultSet resultSet = statement.executeQuery();

            resultSet.first();
            return resultSet.getInt(type.getSQLColumn());
        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Updates the {@link InteractionType} for the specified {@link User}.
     * @param userID The ID of the {@link User} specified.
     * @param type The {@link InteractionType} to update.
     * @param amount The new amount to be  updated to.
     * @return Whether or not updating was successful.
     */
    @NotNull
    public Boolean updateSender(@NotNull String userID, @NotNull InteractionType type, @NotNull Integer amount) {
        try {
            checkIfSenderExists(userID);
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Checking if Sender Exists: " + e.getMessage());
            return false;
        }

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.interaction_senders SET " + type.getSQLColumn() + " = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, amount);
            statement.setLong(2, Long.parseLong(userID));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Interaction Sender: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the {@link InteractionType} for the specified {@link User}.
     * @param userID The ID of the {@link User} specified.
     * @param type The {@link InteractionType} to update.
     * @param amount The new amount to be  updated to.
     * @return Whether or not updating was successful.
     */
    @NotNull
    public Boolean updateReceiver(@NotNull String userID, @NotNull InteractionType type, @NotNull Integer amount) {
        try {
            checkIfReceiverExists(userID);
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Checking if Receiver Exists: " + e.getMessage());
            return false;
        }

        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "UPDATE cafeBot.interaction_receivers SET " + type.getSQLColumn() + " = (?) WHERE user_id = (?);";

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setInt(1, amount);
            statement.setLong(2, Long.parseLong(userID));
            statement.execute();
            return true;
        } catch (SQLException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Interaction Receiver: " + e.getMessage());
            return false;
        }
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
    public MessageEmbed actionEmbed(@NotNull String link, @Nullable String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

    /**
     * @param link The image URL for the {@link MessageEmbed}.
     * @param description The {@link String} description for the message embed.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    public MessageEmbed actionWithDescriptionEmbed(@NotNull String link, @NotNull String description, @Nullable String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(link);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("\"").append(description).append("\"");
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

}
