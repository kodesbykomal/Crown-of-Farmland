package farmland;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a team's hand (max 5 units).
 * External indexing is 1-based.
 *
 * @author uk1234
 * @version 1.1
 */
public class Hand {

    public static final int MAX_HAND_SIZE = 5;

    private final List<Unit> units;

    public Hand() {
        this.units = new ArrayList<>();
    }

    public void add(Unit unit) {
        if (units.size() < MAX_HAND_SIZE) {
            units.add(unit);
        }
    }

    public int size() {
        return units.size();
    }

    public boolean isFull() {
        return units.size() == MAX_HAND_SIZE;
    }

    public Unit get(int index) {
        validateIndex(index);
        return units.get(index - 1);
    }

    public Unit remove(int index) {
        validateIndex(index);
        return units.remove(index - 1);
    }

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    private void validateIndex(int index) {
        if (index < 1 || index > units.size()) {
            throw new IllegalArgumentException("Invalid hand index.");
        }
    }
}