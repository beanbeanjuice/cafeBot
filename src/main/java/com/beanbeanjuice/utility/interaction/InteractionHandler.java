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
        arrayList.add("https://media0.giphy.com/media/Zau0yrl17uzdK/giphy.gif");
        arrayList.add("https://cdn.weeb.sh/images/ByTR7kFwW.gif");
        arrayList.add("https://cdn.weeb.sh/images/rkpAXa5bG.gif");
        arrayList.add("https://i.pinimg.com/originals/fc/e1/2d/fce12d3716f05d56549cc5e05eed5a50.gif");
        arrayList.add("https://i.pinimg.com/originals/4e/9e/a1/4e9ea150354ad3159339b202cbc6cad9.gif");
        arrayList.add("https://cdn.weeb.sh/images/HyPjmytDW.gif");
        arrayList.add("https://media.tenor.com/images/9e7a8a7473f6535081805f0e85b7a09f/tenor.gif");
        arrayList.add("https://media1.tenor.com/images/b6d8a83eb652a30b95e87cf96a21e007/tenor.gif?itemid=10426943");
        arrayList.add("https://cdn.weeb.sh/images/HkskD56OG.gif");
        arrayList.add("https://i.pinimg.com/originals/fe/39/cf/fe39cfc3be04e3cbd7ffdcabb2e1837b.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJ-CQytvW.gif");
        arrayList.add("https://i.imgur.com/fm49srQ.gif");
        arrayList.add("https://i.pinimg.com/originals/65/57/f6/6557f684d6ffcd3cd4558f695c6d8956.gif");
        arrayList.add("https://i.imgur.com/o2SJYUS.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random stab image.
     */
    public String getStabImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://i0.wp.com/blackandyellowotakugamers.com/wp-content/uploads/2017/06/ayase-stab.gif?resize=640%2C360&ssl=1");
        arrayList.add("https://lh4.googleusercontent.com/proxy/rJcn_Dbvigo9RcC7cG2roccmEFsn2bjHahIjWrcnXQopZN83m0K7GSDC1vRuxvxUrs91vgJjhai6ttfeB8s2JjZ0BgNucrLVaN9u3owHNS-T=s0-d");
        arrayList.add("https://i.gifer.com/EJEW.gif");
        arrayList.add("https://images6.fanpop.com/image/photos/38400000/Anime-stabbing-the-floor-anime-38465649-500-376.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random hmph image.
     */
    public String getHmphImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://i.pinimg.com/originals/15/a0/52/15a05247ac88d901524adde074651bb6.gif");
        arrayList.add("https://cdn.weeb.sh/images/BkdB9PuLz.gif");
        arrayList.add("https://i.pinimg.com/originals/b7/e1/32/b7e132fd3f4e110ea54ef8aa8f4eebbe.gif");
        arrayList.add("https://media.tenor.com/images/bae7c01893337837f652ef4932225bb3/tenor.gif");
        arrayList.add("https://image.myanimelist.net/ui/D7ahedOlctZp9mcCPCIwK_Ecb3sRiVT2GZ6rB8qrAL2ic1o1A2V6r2f6t1ptoq0MAS2D_e7xg-yS3K4Rrlu-_vnKZO0LjfyjIJ61jzQErjuWrt3R2lud0fbmwAO4owMv");
        arrayList.add("https://thumbs.gfycat.com/ThornySizzlingAtlanticsharpnosepuffer-max-1mb.gif");
        arrayList.add("https://cdn.weeb.sh/images/Sy8IMlqJM.gif");
        arrayList.add("https://i.imgur.com/UILjVng.gif");
        arrayList.add("https://64.media.tumblr.com/490445f667b1d1a9592bc0e865cb04c5/tumblr_pkgg86Msfy1th206io1_640.gifv");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random pout image.
     */
    public String getPoutImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://i.imgur.com/Z3Hdtos.gif");
        arrayList.add("https://cdn.weeb.sh/images/ryO3obrSf.gif");
        arrayList.add("https://media1.tenor.com/images/1e3dd3aaafa7481aa7f47e0157fc91c9/tenor.gif?itemid=12390498");
        arrayList.add("https://cdn.weeb.sh/images/Sk7CeJtwZ.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJmcekKvW.gif");
        arrayList.add("https://i.pinimg.com/originals/e5/6e/1a/e56e1ae197ea11668756e6e82407e5c5.gif");
        arrayList.add("https://media.tenor.com/images/fb76ab83c98779c3dae8dc1afef7b5df/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJL3gytvb.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJKTlkYD-.gif");
        arrayList.add("https://i.pinimg.com/originals/e8/a3/9c/e8a39c41b8e2eacdf0fd4b5889a5f323.gif");
        arrayList.add("https://media2.giphy.com/media/X3VrxPijowGC4/giphy.gif");
        arrayList.add("https://cutewallpaper.org/21/anime-pout-face/Moms-Pout-GIF-by-Shumatsu.gif");
        arrayList.add("https://media.tenor.com/images/8f0fd194a5af38663c43d0c5b6d7042b/tenor.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random throw image.
     */
    public String getThrowImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://i.pinimg.com/originals/8d/ab/29/8dab296aed2cbe25af8ebb4703517356.gif");
        arrayList.add("https://media1.tenor.com/images/9ff361ceb7cb526cd7874d10095dfd76/tenor.gif?itemid=16842604");
        arrayList.add("https://media1.tenor.com/images/189e189c645be7debf05bc17026cd915/tenor.gif?itemid=16221358");
        arrayList.add("https://image.myanimelist.net/ui/q-babdSqGwiJF-zQZJJ0ecZzOC34ttAGza624-gyjeV9rLaBHOyfSoLcrPA8RpZuyM35RqFzOcnaeZZIU0G4GDQxeStT_B6QcDPdAKG2OT8");
        arrayList.add("https://i.pinimg.com/originals/56/55/d3/5655d367d927ec5920f26c143c50072c.gif");
        arrayList.add("https://thumbs.gfycat.com/LargeScratchyFinch-size_restricted.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random smile image.
     */
    public String getSmileImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://pa1.narvii.com/6314/69057088d88cccf7cce70f19192e112994cc055b_hq.gif");
        arrayList.add("https://cdn.weeb.sh/images/HyC_4ytD-.gif");
        arrayList.add("https://i.pinimg.com/originals/82/b3/9c/82b39c323ca376e9bb5844a54973fc42.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJq9EJKwZ.gif");
        arrayList.add("https://cdn.weeb.sh/images/B1-UN1KPb.gif");
        arrayList.add("https://66.media.tumblr.com/7216949e7f3662b24c28f3d700c9f096/tumblr_plfhtpnwzP1th206io1_400.gif");
        arrayList.add("https://cdn.weeb.sh/images/rkqL4ktDZ.gif");
        arrayList.add("https://media.tenor.com/images/7a15e5fa64b4b2b4dde8554339d3cfa0/tenor.gif");
        arrayList.add("https://64.media.tumblr.com/tumblr_m8wm9nCUGf1rrftqho1_500.gif");
        arrayList.add("https://cdn.weeb.sh/images/ry4U4JFwW.gif");
        arrayList.add("https://cdn.weeb.sh/images/HJ0DEytPZ.gif");
        arrayList.add("https://64.media.tumblr.com/4107c390b3cb3b8a215bcd4c5f1b1dc3/tumblr_o3x6o2REG91tydz8to1_540.gifv");
        arrayList.add("https://i.pinimg.com/originals/98/11/96/981196752db77080887150e27b18c70e.gif");
        arrayList.add("https://cdn.weeb.sh/images/BJ5YNktD-.gif");
        arrayList.add("https://64.media.tumblr.com/245e1dd0a0522ce16a011878726896e4/tumblr_inline_nl5noj41HV1sptc1c.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random stare image.
     */
    public String getStareImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://i.pinimg.com/originals/88/42/35/8842356c82360ddf9e586dcc45f5e097.gif");
        arrayList.add("https://cdn.weeb.sh/images/Hye2wIJtPb.gif");
        arrayList.add("https://cdn.weeb.sh/images/BJThp4xuM.gif");
        arrayList.add("https://media1.tenor.com/images/ec2187a26ef99212ede5a4408c7bfc9d/tenor.gif?itemid=14577429");
        arrayList.add("https://i.pinimg.com/originals/61/04/ef/6104efe6d51767e707cb7b6495a73525.gif");
        arrayList.add("https://media.tenor.com/images/f56bc9a14b1b1a7d8df2ea7b2c33f148/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/BJ88vLvd-.gif");
        arrayList.add("https://cdn.weeb.sh/images/Sk9jLJKvZ.gif");
        arrayList.add("https://thumbs.gfycat.com/EnchantingHandyCusimanse-max-1mb.gifhttps://thumbs.gfycat.com/EnchantingHandyCusimanse-max-1mb.gif");
        arrayList.add("https://thumbs.gfycat.com/InsecureAgedGemsbok-max-1mb.gif");
        arrayList.add("https://cdn.weeb.sh/images/H1jnI1KPZ.gif");
        arrayList.add("https://64.media.tumblr.com/3cffc28ab387fe143f233f393827ebc2/13d82faf5dbd0d68-97/s1280x1920/a8d604b710c6131345e8eba9ce9f4537a0421da9.gifv");
        arrayList.add("https://media0.giphy.com/media/RXrwFW84gDCcU/giphy.gif");
        arrayList.add("https://64.media.tumblr.com/c7d4d3466fb934c16f22045f3126427e/ae81ba1971e1d5e9-69/s1280x1920/f9d18e4d0fb5bd2be67c6f821a231f33c233d2d5.gifv");
        arrayList.add("https://cdn.weeb.sh/images/rk23UyYP-.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random tickle image.
     */
    public String getTickleImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://cdn.weeb.sh/images/Byj7LJmiW.gif");
        arrayList.add("https://64.media.tumblr.com/059123ca5dd69c267042e5a0ba2acf37/tumblr_o58hfaC3Cy1vpbklao3_540.gifv");
        arrayList.add("https://64.media.tumblr.com/ba913c0d21916e57b846e93b783cab39/tumblr_o505jxtnWk1vpbklao4_500.gifv");
        arrayList.add("https://cdn.weeb.sh/images/rkPzIyQi-.gif");
        arrayList.add("https://media1.tenor.com/images/05a64a05e5501be2b1a5a734998ad2b2/tenor.gif?itemid=11379130");
        arrayList.add("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/7bbbe46f-5285-46b1-804e-337939538ae7/dbj9hfz-7c5064ae-7edb-4002-8a1f-8f967f20f386.gif?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzdiYmJlNDZmLTUyODUtNDZiMS04MDRlLTMzNzkzOTUzOGFlN1wvZGJqOWhmei03YzUwNjRhZS03ZWRiLTQwMDItOGExZi04Zjk2N2YyMGYzODYuZ2lmIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.dRclMcNywM8FZm-seA1jPzzrZ9qF4EQE-O1trA5WLLc");
        arrayList.add("https://cdn.weeb.sh/images/SyGQIk7i-.gif");
        arrayList.add("https://i.pinimg.com/originals/d3/85/54/d38554c6e23b86c81f8d4a3764b38912.gif");
        arrayList.add("https://media1.tenor.com/images/eaef77278673333265da087f65941e48/tenor.gif?itemid=16574823");
        arrayList.add("https://i.pinimg.com/originals/fe/a7/9f/fea79fed0168efcaf1ddfb14d8af1a6d.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random rage image.
     */
    public String getRageImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://i.pinimg.com/originals/19/97/28/199728c74eb00a12d2d2c8a1ad440574.gif");
        arrayList.add("https://data.whicdn.com/images/185977769/original.gif");
        arrayList.add("https://i.pinimg.com/originals/0e/09/1a/0e091a4895308fa0d2c7940ec2ad21ed.gif");
        arrayList.add("https://media.tenor.com/images/f1c73d36e72343d0e26d4da210155796/tenor.gif");
        arrayList.add("https://memestatic.fjcdn.com/gifs/Angry++for+those+who+want+to+express+their+anger_9de5df_6465993.gif");
        arrayList.add("https://thumbs.gfycat.com/CommonFrightenedEastsiberianlaika-max-1mb.gif");
        arrayList.add("https://cdn78.picsart.com/198005884000202.gif?to=min&r=640");
        arrayList.add("https://media.tenor.com/images/abe8bc8b0b76b8ffe6694fa8b8f48853/tenor.gif");
        arrayList.add("https://media.tenor.com/images/ef795689617a8c97379b297fd7557235/tenor.gif");
        arrayList.add("https://64.media.tumblr.com/1bd0a91503c985d20460aebe3c32c626/a060f146972d4ae9-8b/s640x960/68a078f5628f80d861029feda95ea5ad833afa04.gifv");
        arrayList.add("https://media.tenor.com/images/8a3797292089d4f76dc7d08abfbb2e51/tenor.gif");
        arrayList.add("https://media.tenor.com/images/88b219548ba55c6dfbbaf62502f4ba0a/tenor.gif");
        arrayList.add("https://i.pinimg.com/originals/65/65/bc/6565bcf12edb458915ee8890861e3e01.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random yell image.
     */
    public String getYellImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://thumbs.gfycat.com/UniqueSoggyBluebottle-small.gif");
        arrayList.add("https://media.tenor.com/images/5d95d35aa4321d9d143ce83d3de81741/tenor.gif");
        arrayList.add("https://media.tenor.com/images/a8f1de53b2b50a54ff330a807a4588eb/tenor.gif");
        arrayList.add("https://media.tenor.com/images/b07017c1bec2c5ef9e243d20427125c4/tenor.gif");
        arrayList.add("https://media.tenor.com/images/607fab7d42d9af04ee462ca2705f5e14/tenor.gif");
        arrayList.add("https://i.pinimg.com/originals/37/21/25/372125c59de3e08571a4f99cb2a19a9a.gif");
        arrayList.add("https://image.myanimelist.net/ui/ZPVPz_CObDpnH08hnJF81OfXLbu4yKz7EE0wsUNM1bOrCna2msI38gi_ghcFnzgK8OSWKkxoQsrMnM9ZIS7hh9C4UH9H6UMxkZbqJnGICjrrpQkuiZtokmZXUw4Tb6pM");
        arrayList.add("https://media.tenor.com/images/f31079f190147358ea1ad3f2b8de5db1/tenor.gif");
        arrayList.add("https://64.media.tumblr.com/1cd5c2f81a26414762d19a3a634f55be/tumblr_ps8zexUjW51sk6fb9_1280.gifv");
        arrayList.add("https://media0.giphy.com/media/11WojR0GhjExlm/giphy.gif");
        arrayList.add("http://1.bp.blogspot.com/-CdYIp9oXWBY/VRlScMx-JCI/AAAAAAAABJA/in44lljKjt4/s1600/bi70X9p.gif");
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
