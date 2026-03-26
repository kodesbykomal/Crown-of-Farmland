package farmland;

import java.util.Objects;

/**
 * Represents a single field on the 7x7 board.
 * A field has a fixed position and may contain one unit.
 * It may also be selected.
 *
 * @author uk1234
 * @version 1.0
 */
public class Field {

    private final Position position;
    private Unit unit;
    private boolean selected;

    /**
     * Creates a field for the given position.
     *
     * @param position board position
     */
    public Field(Position position) {
        this.position = Objects.requireNonNull(position);
        this.unit = null;
        this.selected = false;
    }

    /**
     * Returns the position of this field.
     *
     * @return position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the unit on this field.
     *
     * @return unit or null
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Places a unit on this field.
     *
     * @param unit unit to place
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Removes and returns the unit from this field.
     *
     * @return removed unit or null
     */
    public Unit removeUnit() {
        Unit removed = unit;
        unit = null;
        return removed;
    }

    /**
     * Returns true if field is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return unit == null;
    }

    /**
     * Marks this field as selected.
     */
    public void select() {
        selected = true;
    }

    /**
     * Unselects this field.
     */
    public void unselect() {
        selected = false;
    }

    /**
     * Returns true if field is selected.
     *
     * @return true if selected
     */
    public boolean isSelected() {
        return selected;
    }
}