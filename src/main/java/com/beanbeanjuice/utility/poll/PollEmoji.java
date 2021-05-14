package com.beanbeanjuice.utility.poll;

import org.jetbrains.annotations.NotNull;

/**
 * A custom emoji enum.
 *
 * @author beanbeanjuice
 */
public enum PollEmoji {

    A(":a:", "\uD83C\uDDE6"),
    B(":b:", "\uD83C\uDDE7"),
    C(":c:", "\uD83C\uDDE8"),
    D(":d:", "\uD83C\uDDE9"),
    E(":e:", "\uD83C\uDDEA"),
    F(":f:", "\uD83C\uDDEB"),
    G(":g:", "\uD83C\uDDEC"),
    H(":h:", "\uD83C\uDDED"),
    I(":i:", "\uD83C\uDDEE"),
    J(":j:", "\uD83C\uDDEF"),
    K(":k:", "\uD83C\uDDF0"),
    L(":l:", "\uD83C\uDDF1"),
    M(":m:", "\uD83C\uDDF2"),
    N(":n:", "\uD83C\uDDF3"),
    O(":o:", "\uD83C\uDDF4"),
    P(":p:", "\uD83C\uDDF5"),
    Q(":q:", "\uD83C\uDDF6"),
    R(":r:", "\uD83C\uDDF7"),
    S(":s:", "\uD83C\uDDF8"),
    T(":t:", "\uD83C\uDDF9"),
    U(":u:", "\uD83C\uDDFA"),
    V(":v:", "\uD83C\uDDFB"),
    W(":w:", "\uD83C\uDDFC"),
    X(":x:", "\uD83C\uDDFD"),
    Y(":y:", "\uD83C\uDDFE"),
    Z(":z:", "\uD83C\uDDFF");

    private final String discordString;
    private final String unicode;

    PollEmoji(@NotNull String discordString, @NotNull String unicode) {
        this.discordString = discordString;
        this.unicode = unicode;
    }

    public String getDiscordString() {
        return discordString;
    }

    public String getUnicode() {
        return unicode;
    }

}
