package com.beanbeanjuice.utility.sections.interaction;

import org.jetbrains.annotations.NotNull;

public enum InteractionType {

    HUG("hug_amount"),
    PUNCH("punch_amount"),
    KISS("kiss_amount"),
    BITE("bite_amount"),
    BLUSH("blush_amount"),
    CUDDLE("cuddle_amount"),
    NOM("nom_amount"),
    POKE("poke_amount"),
    SLAP("slap_amount"),
    STAB("stab_amount"),
    HMPH("hmph_amount"),
    POUT("pout_amount"),
    THROW("throw_amount"),
    SMILE("smile_amount"),
    STARE("stare_amount"),
    TICKLE("tickle_amount"),
    RAGE("rage_amount"),
    YELL("yell_amount"),
    HEADPAT("headpat_amount"),
    CRY("cry_amount"),
    DANCE("dance_amount"),
    DAB("dab_amount"),
    BONK("bonk_amount"),
    SLEEP("sleep_amount"),
    DIE("die_amount"),
    WELCOME("welcome_amount"),
    LICK("lick_amount"),
    SHUSH("shush_amount");

    private final String sqlColumn;

    InteractionType(@NotNull String sqlColumn) {
        this.sqlColumn = sqlColumn;
    }

    @NotNull
    public String getSQLColumn() {
        return sqlColumn;
    }

}
