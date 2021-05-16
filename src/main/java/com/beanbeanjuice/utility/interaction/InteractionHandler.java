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
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/1.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/2.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/3.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/4.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/5.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/6.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/7.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/8.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/9.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/10.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/11.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/12.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/13.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/14.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/15.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/16.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/17.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/18.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/19.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/20.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/21.gif");
        arrayList.add("http://cdn.beanbeanjuice.com/images/hugs/22.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random punch image.
     */
    public String getPunchImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media1.tenor.com/images/31686440e805309d34e94219e4bedac1/tenor.gif?itemid=4790446");
        arrayList.add("https://media.tenor.com/images/8a79543998d6878be573aab94ae86456/tenor.gif");
        arrayList.add("https://i.pinimg.com/originals/d4/17/ce/d417ce25489868ae4bf177de73308aa5.gif");
        arrayList.add("https://thumbs.gfycat.com/AmazingAngryCoelacanth-max-1mb.gif");
        arrayList.add("https://media.tenor.com/images/f7b498a905f3e8c964ad5d97bf176e1f/tenor.gif");
        arrayList.add("https://64.media.tumblr.com/01622a00635a61442a5548e70cd4aff8/a55a2448b1c12be9-0c/s640x960/298f2338ed0eb578a8da10003e4427a3a004a237.gifv");
        arrayList.add("https://media1.tenor.com/images/2c7d6ec3272a369305cc5cf773ae9b04/tenor.gif?itemid=15151443");
        arrayList.add("https://media.tenor.com/images/9ff746e22fbb39ebf28f431c158f769f/tenor.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random kiss image.
     */
    public String getKissImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media0.giphy.com/media/FqBTvSNjNzeZG/source.gif");
        arrayList.add("https://cdn.weeb.sh/images/H1tv2p_Db.gif");
        arrayList.add("https://cdn.weeb.sh/images/HJ5khTOP-.gif");
        arrayList.add("https://i.imgur.com/OE7lSSY.gif");
        arrayList.add("https://media2.giphy.com/media/bGm9FuBCGg4SY/giphy.gif");
        arrayList.add("https://cdn.weeb.sh/images/S1E1npuvb.gif");
        arrayList.add("https://i.pinimg.com/originals/f7/e8/a4/f7e8a4abac5d9e64784bd97480863a19.gif");
        arrayList.add("https://i.pinimg.com/originals/8c/ab/4f/8cab4f4c73547d077c56066461c40a5e.gif");
        arrayList.add("https://cdn.weeb.sh/images/HklBtCvTZ.gif");
        arrayList.add("https://cdn.weeb.sh/images/Sy6Ai6ODb.gif");
        arrayList.add("https://media1.tenor.com/images/becfaa23686fa3b243fb4e0ff29aab39/tenor.gif?itemid=12873719");
        arrayList.add("https://cdn.weeb.sh/images/H1Gx2aOvb.gif");
        arrayList.add("https://media1.tenor.com/images/b4905b7fcf1ab0e4d6c633c0069785ea/tenor.gif?itemid=14589013");
        arrayList.add("https://media1.tenor.com/images/4700f51c48d41104e541459743db42ae/tenor.gif?itemid=17947049");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random bite image.
     */
    public String getBiteImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media1.tenor.com/images/8099a2d3e3f820ddcf96072fc33ad332/tenor.gif?itemid=8231871");
        arrayList.add("https://cdn.weeb.sh/images/rk8illmiW.gif");
        arrayList.add("https://media1.tenor.com/images/6b42070f19e228d7a4ed76d4b35672cd/tenor.gif?itemid=9051585");
        arrayList.add("https://media1.tenor.com/images/0d192209c8e9bcd9826af63ba72fc584/tenor.gif?itemid=15164408");
        arrayList.add("https://i.pinimg.com/originals/69/92/40/69924081e9c60548c2ec6ba42b1ebea9.gif");
        arrayList.add("https://cdn.weeb.sh/images/S1FOllQj-.gif");
        arrayList.add("https://media0.giphy.com/media/OqQOwXiCyJAmA/giphy.gif");
        arrayList.add("https://i.pinimg.com/originals/eb/da/a3/ebdaa31b39a28b3b7a06603687a8a406.gif");
        arrayList.add("https://cdn.weeb.sh/images/rkNgZlXi-.gif");
        arrayList.add("https://i.gifer.com/np4.gif");
        arrayList.add("https://cdn.weeb.sh/images/H1hige7sZ.gif");
        arrayList.add("https://cdn.weeb.sh/images/H1gYelQjZ.gif");
        arrayList.add("https://animesher.com/orig/0/83/836/8362/animesher.com_gif-funny-girl-836208.gif");
        arrayList.add("https://cdn.weeb.sh/images/Sys3xg7jW.gif");
        arrayList.add("https://i.pinimg.com/originals/b0/6d/6a/b06d6a26b08516ac069b7a9acdd001e5.gif");
        arrayList.add("https://cdn.weeb.sh/images/HkutgeXob.gif");
        arrayList.add("https://cdn.weeb.sh/images/S1o6egmjZ.gif");
        arrayList.add("https://cdn.weeb.sh/images/ByWuR1q1M.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random blush image.
     */
    public String getBlushImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://cdn.weeb.sh/images/HkrQM8mwZ.gif");
        arrayList.add("https://media.tenor.com/images/f45f5c5fd72dd7c9ff50976e2bc7133c/tenor.gif");
        arrayList.add("https://media.tenor.com/images/0d4260a30ddf2647a9e1824b8e68defd/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/S1exMIQDb.gif");
        arrayList.add("https://media3.giphy.com/media/ulWUgCk4F1GGA/giphy.gif");
        arrayList.add("https://i.pinimg.com/originals/f6/37/2c/f6372c9a7b66e2c6d0a674cd94164701.gif");
        arrayList.add("https://media.tenor.com/images/75936b8b93269bd293bd184eb209e358/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/Hy-GGIXvb.gif");
        arrayList.add("https://i.pinimg.com/originals/dd/6d/47/dd6d47837ba2952f8ccadfa4167e706b.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJ8lf8Xwb.gif");
        arrayList.add("https://cdn.weeb.sh/images/Hkx8xMLQw-.gif");
        arrayList.add("https://i.imgur.com/hFkeViW.gif");
        arrayList.add("https://i.imgur.com/BVzWdzX.gif");
        arrayList.add("https://media.tenor.com/images/884e3159181d7467bb8d297b313d4aa3/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/BJH4f8mP-.gif");
        arrayList.add("https://64.media.tumblr.com/07ab92aaf9c8be4f56094192ec75d21a/tumblr_ojklcxknQh1tydz8to1_540.gifv");
        arrayList.add("https://media1.tenor.com/images/cbfd2a06c6d350e19a0c173dec8dccde/tenor.gif?itemid=15727535");
        arrayList.add("https://cdn.weeb.sh/images/r1n7M87wW.gif");
        arrayList.add("https://thumbs.gfycat.com/AcidicSecondhandBanteng-size_restricted.gif");
        arrayList.add("https://cdn.weeb.sh/images/B14JM8Qw-.gif");
        arrayList.add("https://i.pinimg.com/originals/c1/18/28/c118281cf06513a78e2fdc731db48c1b.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random cuddle image.
     */
    public String getCuddleImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://cdn.weeb.sh/images/r1VzDkmjW.gif");
        arrayList.add("https://media.tenor.com/images/c81c05cbbb8773ad2a92a7e206d7cab5/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/Hy5y88mPb.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJLkLImPb.gif");
        arrayList.add("https://media.tenor.com/images/1bba1d21ed77763a5216a65221d08298/tenor.gif");
        arrayList.add("https://i.pinimg.com/originals/d8/7c/5c/d87c5cdd0a68caf2b6feeec0f7da7315.gif");
        arrayList.add("https://cdn.weeb.sh/images/HJMv_k7iW.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJn18IXP-.gif");
        arrayList.add("https://i.imgur.com/p2Jt2P5.gif");
        arrayList.add("https://cdn.weeb.sh/images/B1S1I87vZ.gif");
        arrayList.add("https://thumbs.gfycat.com/ShowyObedientCrane-size_restricted.gif");
        arrayList.add("https://64.media.tumblr.com/f2a878657add13aa09a5e089378ec43d/tumblr_n5uovjOi931tp7433o1_500.gif");
        arrayList.add("https://i.imgur.com/eTZ063r.gif?noredirect");
        arrayList.add("https://acegif.com/wp-content/gif/anime-hug-30.gif");
        arrayList.add("https://cdn.weeb.sh/images/rJlMU87vb.gif");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random nom image.
     */
    public String getNomImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media.tenor.com/images/65804047872f835e45b98d57356ae97e/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/ry1JJkYvZ.gif");
        arrayList.add("https://media.tenor.com/images/15f3beb2b31a9cba0b15f35e3c3eb5b8/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/rJCJ1kKDW.gif");
        arrayList.add("https://i.pinimg.com/originals/a5/4d/65/a54d651964d6925292105a38c52e577f.gif");
        arrayList.add("https://cdn.weeb.sh/images/SJPgk1Ywb.gif");
        arrayList.add("https://media.tenor.com/images/e8bbe712a5f36bbe9545930894b08bf9/tenor.gif");
        arrayList.add("https://i.pinimg.com/originals/59/6e/72/596e723a419d19ed784dfb546723018e.gif");
        arrayList.add("https://cdn.weeb.sh/images/H1foR4lOz.gif");
        arrayList.add("https://i.imgur.com/Ryy3D7r.gif");
        arrayList.add("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/8d8aa80d-a00f-4ca5-817d-30fbc6bf33be/d9k1did-e66ef8e4-58b8-4ee6-9236-b7badfd8ae25.gif?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzhkOGFhODBkLWEwMGYtNGNhNS04MTdkLTMwZmJjNmJmMzNiZVwvZDlrMWRpZC1lNjZlZjhlNC01OGI4LTRlZTYtOTIzNi1iN2JhZGZkOGFlMjUuZ2lmIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.krC5SY_lJ9_J8B2F5qVZaYtSH_mg2nJtW8OpX1GPYQY");
        arrayList.add("https://cdn.weeb.sh/images/Hkj7yytD-.gif");
        arrayList.add("https://cdn.weeb.sh/images/SyI7yJKw-.gif");
        arrayList.add("https://media.tenor.com/images/c041d53a96143ec2dee20b5f77de7cfd/tenor.gif");
        arrayList.add("https://linustechtips.com/uploads/gallery/album_2681/gallery_89658_2681_197316.gif");
        arrayList.add("https://i.chzbgr.com/full/8972792576/h27BBD3A2/");
        return arrayList.get(CafeBot.getGeneralHelper().getRandomNumber(0, arrayList.size()-1));
    }

    /**
     * @return A random poke image.
     */
    public String getPokeImage() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://media.tenor.com/images/6d227fd93656bd164985aad517a25c3f/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/rkeaUeJKD-.gif");
        arrayList.add("https://cdn.weeb.sh/images/Hk2HekKD-.gif");
        arrayList.add("https://i.pinimg.com/originals/b4/95/fb/b495fb19f4b9a1b04f48297b676c497b.gif");
        arrayList.add("https://media1.tenor.com/images/0da232de2ee45e1464bf1d5916869a39/tenor.gif?itemid=16935454");
        arrayList.add("https://media0.giphy.com/media/FdinyvXRa8zekBkcdK/200.gif");
        arrayList.add("https://cdn.weeb.sh/images/rJzUe1FwZ.gif");
        arrayList.add("https://media.tenor.com/images/2ff785b647ef22f7110b3b2599e4c847/tenor.gif");
        arrayList.add("https://media.tenor.com/images/3b849449f34e2b7f280a4edc127a1162/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/BJhIekKwb.gif");
        arrayList.add("https://media.tenor.com/images/5307e3f5a44d4d510ae58c9e76991f60/tenor.gif");
        arrayList.add("https://media.tenor.com/images/ac4b4181312d67225619344fcc137ff9/tenor.gif");
        arrayList.add("https://cdn.weeb.sh/images/HkjjLb0rM.gif");
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
