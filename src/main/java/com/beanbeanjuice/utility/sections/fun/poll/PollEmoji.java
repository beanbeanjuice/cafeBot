package com.beanbeanjuice.utility.sections.fun.poll;

import org.jetbrains.annotations.NotNull;

/**
 * A custom emoji enum.
 *
 * @author beanbeanjuice
 */
public enum PollEmoji {

    A(":regional_indicator_a:", "\uD83C\uDDE6"),
    B(":regional_indicator_b:", "\uD83C\uDDE7"),
    C(":regional_indicator_c:", "\uD83C\uDDE8"),
    D(":regional_indicator_d:", "\uD83C\uDDE9"),
    E(":regional_indicator_e:", "\uD83C\uDDEA"),
    F(":regional_indicator_f:", "\uD83C\uDDEB"),
    G(":regional_indicator_g:", "\uD83C\uDDEC"),
    H(":regional_indicator_h:", "\uD83C\uDDED"),
    I(":regional_indicator_i:", "\uD83C\uDDEE"),
    J(":regional_indicator_j:", "\uD83C\uDDEF"),
    K(":regional_indicator_k:", "\uD83C\uDDF0"),
    L(":regional_indicator_l:", "\uD83C\uDDF1"),
    M(":regional_indicator_m:", "\uD83C\uDDF2"),
    N(":regional_indicator_n:", "\uD83C\uDDF3"),
    O(":regional_indicator_o:", "\uD83C\uDDF4"),
    P(":regional_indicator_p:", "\uD83C\uDDF5"),
    Q(":regional_indicator_q:", "\uD83C\uDDF6"),
    R(":regional_indicator_r:", "\uD83C\uDDF7"),
    S(":regional_indicator_s:", "\uD83C\uDDF8"),
    T(":regional_indicator_t:", "\uD83C\uDDF9"),
    U(":regional_indicator_u:", "\uD83C\uDDFA"),
    V(":regional_indicator_v:", "\uD83C\uDDFB"),
    W(":regional_indicator_w:", "\uD83C\uDDFC"),
    X(":regional_indicator_x:", "\uD83C\uDDFD"),
    Y(":regional_indicator_y:", "\uD83C\uDDFE"),
    Z(":regional_indicator_z:", "\uD83C\uDDFF");

    private final String discordString;
    private final String unicode;

    /**
     * Creates a new instance of the {@link PollEmoji}.
     * @param discordString The Discord {@link String} to send the {@link PollEmoji}.
     * @param unicode The unicode {@link String} of the {@link PollEmoji}.
     */
    PollEmoji(@NotNull String discordString, @NotNull String unicode) {
        this.discordString = discordString;
        this.unicode = unicode;
    }

    /**
     * @return The Discord {@link String} for the {@link PollEmoji}.
     */
    @NotNull
    public String getDiscordString() {
        return discordString;
    }

    /**
     * @return The unicode {@link String} for the {@link PollEmoji}.
     */
    @NotNull
    public String getUnicode() {
        return unicode;
    }

}
