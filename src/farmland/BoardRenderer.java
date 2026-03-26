package farmland;

public class BoardRenderer {

    private static final String SYMBOL_SET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123";

    public static void render(Board board,
                              Position selected,
                              boolean compactMode) {

        for (int row = Position.BOARD_SIZE - 1; row >= 0; row--) {

            StringBuilder line = new StringBuilder();

            for (int col = 0; col < Position.BOARD_SIZE; col++) {

                Position pos = new Position(col, row);
                Field field = board.getField(pos);

                String symbol = resolveSymbol(field);

                if (selected != null && selected.equals(pos)) {
                    symbol = "*" + symbol;
                }

                line.append(symbol);

                if (!compactMode) {
                    line.append(" ");
                }
            }

            System.out.println(
                    OutputFormatter.formatToWidth(
                            line.toString().trim()));
        }
    }

    private static String resolveSymbol(Field field) {

        if (field.isEmpty()) {
            return ".";
        }

        Unit unit = field.getUnit();

        String baseSymbol;

        if (unit instanceof FarmerKing) {
            baseSymbol = "K";
        } else if (!unit.isRevealed()) {
            baseSymbol = "H";
        } else {
            int index =
                    Math.abs(unit.getName().hashCode())
                            % SYMBOL_SET.length();
            baseSymbol =
                    String.valueOf(SYMBOL_SET.charAt(index));
        }

        if (unit.isBlocked()) {
            baseSymbol += "b";
        }

        return baseSymbol;
    }
}