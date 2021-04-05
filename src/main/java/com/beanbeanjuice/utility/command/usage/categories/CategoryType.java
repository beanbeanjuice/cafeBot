package com.beanbeanjuice.utility.command.usage.categories;

import org.jetbrains.annotations.NotNull;

/**
 * A static class used for handling command category types.
 *
 * @author beanbeanjuice
 */
public enum CategoryType {

    MODERATION("Commands used for moderation.", "https://pbs.twimg.com/media/DtvXNI9XcAUQ2Bs.png"),
    FUN("Commands used for fun.", "https://i.pinimg.com/originals/99/73/22/99732222bc5b966da674a76a98e7f76e.jpg"),
    GENERIC("Generic commands.", "https://pbs.twimg.com/media/DkWx4OeX4AAkFUG.png"),
    MUSIC("Commands used for music.", "https://res.cloudinary.com/teepublic/image/private/s--u1qzrQ1z--/t_Resized%20Artwork/c_fit,g_north_west,h_954,w_954/co_484849,e_outline:48/co_484849,e_outline:inner_fill:48/co_ffffff,e_outline:48/co_ffffff,e_outline:inner_fill:48/co_bbbbbb,e_outline:3:1000/c_mpad,g_center,h_1260,w_1260/b_rgb:eeeeee/c_limit,f_auto,h_630,q_90,w_630/v1571447830/production/designs/6381309_0.jpg"),
    TWITCH("Commands used for twitch.", "https://lh3.googleusercontent.com/proxy/L1j09rF74Mbxv1Sng4IDEPMiJ0yQzykGdpNwwPKKsEHZRonY5JNe89tJ6L9dcUZxSBXLSBNSnPx7-i1ya_h5prR8FzPHjcmh");

    private final String message;
    private final String link;

    CategoryType(@NotNull String message, @NotNull String link) {
        this.message = message;
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

}
