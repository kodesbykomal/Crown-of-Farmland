package farmland;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a stack (deck) of units.
 * A deck must contain exactly 40 units at initialization.
 * The top of the deck is at index 0.
 *
 * @author uk1234
 * @version 1.0
 */
public class Deck {

    /** Required number of units in a deck. */
    public static final int REQUIRED_DECK_SIZE = 40;

    /** Internal list storing units (top = index 0). */
    private final List<Unit> units;

    /**
     * Creates a deck containing exactly 40 units.
     *
     * @param units list of units
     * @throws NullPointerException if units is null
     * @throws IllegalArgumentException if size != 40
     */
    public Deck(List<Unit> units) {

        Objects.requireNonNull(units, "Units list must not be null.");

        if (units.size() != REQUIRED_DECK_SIZE) {
            throw new IllegalArgumentException(
                    "Deck must contain exactly "
                            + REQUIRED_DECK_SIZE
                            + " units."
            );
        }

        this.units = new ArrayList<>(units);
    }

    /**
     * Shuffles the deck using the given random generator.
     *
     * @param random random generator
     */
    public void shuffle(Random random) {

        Objects.requireNonNull(random, "Random must not be null.");
        Collections.shuffle(units, random);
    }

    /**
     * Draws and removes the top unit from the deck.
     *
     * @return the drawn unit
     * @throws IllegalStateException if deck is empty
     */
    public Unit draw() {

        if (units.isEmpty()) {
            throw new IllegalStateException("Deck is empty.");
        }

        return units.removeFirst();
    }

    /**
     * Returns true if the deck has no remaining units.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return units.isEmpty();
    }

    /**
     * Returns the number of remaining units in the deck.
     *
     * @return size
     */
    public int size() {
        return units.size();
    }
}