package farmland;

import java.util.Objects;

/**
 * Represents an immutable position on the 7x7 game board.
 * Columns range from A to G.
 * Rows range from 1 to 7.
 * Internally, zero-based indices (0–6) are used.
 */
public final class Position {

    public static final int BOARD_SIZE = 7;

    private static final char MIN_COLUMN = 'A';
    private static final char MAX_COLUMN = 'G';
    private static final int MIN_ROW = 1;
    private static final int MAX_ROW = 7;

    private final int columnIndex;
    private final int rowIndex;

    /**
     * Creates a position using zero-based indices.
     *
     * @param columnIndex column index (0–6)
     * @param rowIndex    row index (0–6)
     */
    public Position(int columnIndex, int rowIndex) {

        if (!isValidIndex(columnIndex) || !isValidIndex(rowIndex)) {
            throw new IllegalArgumentException("Invalid board indices.");
        }

        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    /**
     * Parses a position string (e.g. "D5").
     *
     * @param input position string
     * @return Position object
     */
    public static Position fromString(String input) {

        Objects.requireNonNull(input, "Position input cannot be null.");

        if (input.length() != 2) {
            throw new IllegalArgumentException("Invalid position format.");
        }

        char columnChar = Character.toUpperCase(input.charAt(0));
        char rowChar = input.charAt(1);

        if (columnChar < MIN_COLUMN || columnChar > MAX_COLUMN) {
            throw new IllegalArgumentException("Invalid column.");
        }

        if (!Character.isDigit(rowChar)) {
            throw new IllegalArgumentException("Invalid row.");
        }

        int rowNumber = Character.getNumericValue(rowChar);

        if (rowNumber < MIN_ROW || rowNumber > MAX_ROW) {
            throw new IllegalArgumentException("Row out of range.");
        }

        int columnIndex = columnChar - MIN_COLUMN;
        int rowIndex = rowNumber - 1;

        return new Position(columnIndex, rowIndex);
    }

    private static boolean isValidIndex(int index) {
        return index >= 0 && index < BOARD_SIZE;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Manhattan distance.
     */
    public int distanceTo(Position other) {
        Objects.requireNonNull(other);
        return Math.abs(columnIndex - other.columnIndex)
                + Math.abs(rowIndex - other.rowIndex);
    }

    /**
     * Returns true if exactly one square away
     * horizontally or vertically (no diagonal).
     */
    public boolean isOrthogonallyAdjacent(Position other) {
        Objects.requireNonNull(other);

        int columnDiff = Math.abs(columnIndex - other.columnIndex);
        int rowDiff = Math.abs(rowIndex - other.rowIndex);

        return (columnDiff == 1 && rowDiff == 0)
                || (columnDiff == 0 && rowDiff == 1);
    }

    /**
     * Returns true if adjacent in any of the 8 directions.
     */
    public boolean isAdjacent(Position other) {
        Objects.requireNonNull(other);

        int columnDiff = Math.abs(columnIndex - other.columnIndex);
        int rowDiff = Math.abs(rowIndex - other.rowIndex);

        return columnDiff <= 1
                && rowDiff <= 1
                && !(columnDiff == 0 && rowDiff == 0);
    }

    /**
     * Returns true if diagonally adjacent.
     */
    public boolean isDiagonallyAdjacent(Position other) {
        Objects.requireNonNull(other);

        int columnDiff = Math.abs(columnIndex - other.columnIndex);
        int rowDiff = Math.abs(rowIndex - other.rowIndex);

        return columnDiff == 1 && rowDiff == 1;
    }

    @Override
    public String toString() {
        char column = (char) (MIN_COLUMN + columnIndex);
        int row = rowIndex + 1;
        return column + Integer.toString(row);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Position other)) {
            return false;
        }

        return columnIndex == other.columnIndex
                && rowIndex == other.rowIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnIndex, rowIndex);
    }
}