package farmland;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the 7x7 game board.
 * The board manages fields and unit placement.
 *
 * No duel or game logic is implemented here.
 *
 * @author uk1234
 * @version 1.0
 */
public class Board {

    private final Field[][] fields;
    private Field selectedField;

    /**
     * Creates a 7x7 board.
     */
    public Board() {
        fields = new Field[Position.BOARD_SIZE][Position.BOARD_SIZE];

        for (int row = 0; row < Position.BOARD_SIZE; row++) {
            for (int col = 0; col < Position.BOARD_SIZE; col++) {
                fields[row][col] = new Field(new Position(col, row));
            }
        }
    }

    /**
     * Returns field at given position.
     *
     * @param position position
     * @return field
     */
    public Field getField(Position position) {
        Objects.requireNonNull(position);
        return fields[position.getRowIndex()][position.getColumnIndex()];
    }

    /**
     * Places unit at position.
     *
     * @param position position
     * @param unit unit
     */
    public void placeUnit(Position position, Unit unit) {
        getField(position).setUnit(unit);
    }


    public Position findKing(Team team) {

        for (int row = 0; row < Position.BOARD_SIZE; row++) {
            for (int col = 0; col < Position.BOARD_SIZE; col++) {

                Field field = fields[row][col];

                if (!field.isEmpty()
                        && field.getUnit() instanceof FarmerKing
                        && field.getUnit().getOwner() == team) {

                    return field.getPosition();
                }
            }
        }

        return null;
    }

    public void print(Position selected) {

        System.out.println();
        System.out.println("  A B C D E F G");

        for (int row = Position.BOARD_SIZE - 1; row >= 0; row--) {

            System.out.print((row + 1) + " ");

            for (int col = 0; col < Position.BOARD_SIZE; col++) {

                Field field = fields[row][col];
                String symbol = ".";

                if (!field.isEmpty()) {

                    Unit u = field.getUnit();

                    if (u instanceof FarmerKing) {
                        symbol = "K";
                    } else if (!u.isRevealed()) {
                        symbol = "H";
                    } else {
                        symbol = "U";
                    }
                }

                Position pos = field.getPosition();

                if (selected != null && selected.equals(pos)) {
                    System.out.print("[" + symbol + "]");
                } else {
                    System.out.print(" " + symbol + " ");
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    /**
     * Removes unit at position.
     *
     * @param position position
     * @return removed unit
     */
    public Unit removeUnit(Position position) {
        return getField(position).removeUnit();
    }

    /**
     * Selects a field.
     *
     * @param position position
     */
    public void select(Position position) {
        clearSelection();
        selectedField = getField(position);
        selectedField.select();
    }

    /**
     * Clears current selection.
     */
    public void clearSelection() {
        if (selectedField != null) {
            selectedField.unselect();
            selectedField = null;
        }
    }

    /**
     * Returns selected field or null.
     *
     * @return selected field
     */
    public Field getSelectedField() {
        return selectedField;
    }

    /**
     * Returns orthogonally adjacent positions.
     *
     * @param position position
     * @return list of positions
     */
    public List<Position> getOrthogonalNeighbors(Position position) {
        List<Position> neighbors = new ArrayList<>();
        int col = position.getColumnIndex();
        int row = position.getRowIndex();

        addIfValid(neighbors, col, row + 1);
        addIfValid(neighbors, col + 1, row);
        addIfValid(neighbors, col, row - 1);
        addIfValid(neighbors, col - 1, row);

        return neighbors;
    }

    /**
     * Returns all adjacent positions (8 directions).
     *
     * @param position position
     * @return list of positions
     */
    public List<Position> getAllNeighbors(Position position) {
        List<Position> neighbors = new ArrayList<>();
        int col = position.getColumnIndex();
        int row = position.getRowIndex();

        for (int dc = -1; dc <= 1; dc++) {
            for (int dr = -1; dr <= 1; dr++) {
                if (dc == 0 && dr == 0) {
                    continue;
                }
                addIfValid(neighbors, col + dc, row + dr);
            }
        }

        return neighbors;
    }

    /**
     * Counts enemy units in four directions.
     *
     * @param position center
     * @param team team perspective
     * @return count
     */
    public int countOrthogonalEnemies(Position position, Team team) {
        int count = 0;
        for (Position neighbor : getOrthogonalNeighbors(position)) {
            Field field = getField(neighbor);
            if (!field.isEmpty()
                    && field.getUnit().getOwner() != team) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts enemy units in eight directions.
     *
     * @param position center
     * @param team team perspective
     * @return count
     */
    public int countAllEnemies(Position position, Team team) {
        int count = 0;
        for (Position neighbor : getAllNeighbors(position)) {
            Field field = getField(neighbor);
            if (!field.isEmpty()
                    && field.getUnit().getOwner() != team) {
                count++;
            }
        }
        return count;
    }

    private void addIfValid(List<Position> list, int col, int row) {
        if (col >= 0 && col < Position.BOARD_SIZE
                && row >= 0 && row < Position.BOARD_SIZE) {
            list.add(new Position(col, row));
        }
    }
}