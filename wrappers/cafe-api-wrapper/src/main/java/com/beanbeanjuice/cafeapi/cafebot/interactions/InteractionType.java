package com.beanbeanjuice.cafeapi.cafebot.interactions;

import com.beanbeanjuice.cafeapi.cafebot.interactions.users.Interaction;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * An enum used for {@link Interaction Interaction} types.
 *
 * @author beanbeanjuice
 * @since 1.3.1
 */
public enum InteractionType {
    HUG ("hug_amount", "hug"),
    PUNCH ("punch_amount", "punch"),
    KISS ("kiss_amount", "kiss"),
    BITE ("bite_amount", "bite"),
    BLUSH ("blush_amount", "blush"),
    CUDDLE ("cuddle_amount", "cuddle"),
    NOM ("nom_amount", "nom"),
    POKE ("poke_amount", "poke"),
    SLAP ("slap_amount", "slap"),
    STAB ("stab_amount", null),
    HMPH ("hmph_amount", null),
    POUT ("pout_amount", "pout"),
    THROW ("throw_amount", null),
    SMILE ("smile_amount", "smile"),
    STARE ("stare_amount", "stare"),
    TICKLE ("tickle_amount", "tickle"),
    RAGE ("rage_amount", null),
    YELL ("yell_amount", "scream"),
    HEADPAT ("headpat_amount", "pat"),
    CRY ("cry_amount", "cry"),
    DANCE ("dance_amount", "dance"),
    DAB ("dab_amount", null),
    BONK ("bonk_amount", null),
    SLEEP ("sleep_amount", "sleepy"),
    DIE ("die_amount", "die"),
    WELCOME ("welcome_amount", null),
    LICK ("lick_amount", "lick"),
    SHUSH ("shush_amount", null),
    WAVE ("wave_amount", "wave"),
    SHOOT ("shoot_amount", "shoot"),
    AMAZED ("amazed_amount", "amazing"),
    ASK ("ask_amount", "ask"),
    BOOP ("boop_amount", "boop"),
    LOVE ("love_amount", "love"),
    NOSEBLEED ("nosebleed_amount", "nosebleed"),
    OK ("ok_amount", "ok"),
    UWU ("uwu_amount", "uwu"),
    WINK ("wink_amount", "wink");

    @Getter private final String type;
    private final String kawaiiAPIString;

    /**
     * Creates a new {@link InteractionType} static object.
     * @param type The {@link String type} of {@link Interaction Interaction}.
     */
    InteractionType(String type, @Nullable String kawaiiAPIString) {
        this.type = type;
        this.kawaiiAPIString = kawaiiAPIString;
    }

    /**
     * @return The {@link String} for retrieving the {@link com.beanbeanjuice.KawaiiAPI KawaiiAPI} image.
     */
    public Optional<String> getKawaiiAPIString() {
        return Optional.ofNullable(kawaiiAPIString);
    }

}
