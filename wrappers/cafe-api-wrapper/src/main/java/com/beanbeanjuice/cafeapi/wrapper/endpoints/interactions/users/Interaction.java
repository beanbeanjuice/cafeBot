package com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.users;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import lombok.Getter;

import java.io.InvalidClassException;
import java.util.HashMap;

/**
 * A class used to hold {@link Interaction} information.
 *
 * @author beanbeanjuice
 */
public class Interaction {

    private final HashMap<InteractionType, Integer> interactionMap;

    /**
     * Creates a new {@link Interaction object.}
     *
     * @param interactionMap The hashmap containing {@link InteractionType} as keys and the {@link Integer amount} as values.
     *
     * @throws NullPointerException When there is a mismatch between provided hashmap and guild information types.
     */
    public Interaction(HashMap<InteractionType, Integer> interactionMap) throws NullPointerException {
        if (interactionMap.size() != InteractionType.values().length) throw new NullPointerException("Mismatch between provided hashmap and guild information types.");

        this.interactionMap = interactionMap;
    }

    public int getInteractionAmount(final InteractionType type) {
        return interactionMap.get(type);
    }

}
