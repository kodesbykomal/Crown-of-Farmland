package farmland;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Entry point of Crown of Farmland.
 */
public final class Main {

    private Main() { }

    public static void main(String[] args) {

        /*
         * DEVELOPMENT MODE:
         * If no arguments are provided, use default local files.
         * For submission, remove this block and pass arguments properly.
         */
        if (args.length == 0) {
            args = new String[]{
                    "units=default_units.txt",
                    "seed=1",
                    "deck=default_deck.txt"
            };
        }

        Map<String, String> parameters = parseArguments(args);
        validateRequired(parameters);

        String unitsFile = parameters.get("units");
        String seedValue = parameters.get("seed");

        long seed = parseSeed(seedValue);

        try {

            // Print units file BEFORE processing
            printFile(unitsFile);

            // IMPORTANT: maintain ordered list for numeric references
            List<Unit> orderedUnits = new ArrayList<>();
            Map<String, Unit> unitPool = loadUnits(unitsFile, orderedUnits);

            Deck deck1;
            Deck deck2;

            if (parameters.containsKey("deck")) {

                String deckFile = parameters.get("deck");
                printFile(deckFile);

                deck1 = buildDeck(deckFile, unitPool, orderedUnits);
                deck2 = buildDeck(deckFile, unitPool, orderedUnits);

            } else {

                String deckFile1 = parameters.get("deck1");
                String deckFile2 = parameters.get("deck2");

                printFile(deckFile1);
                printFile(deckFile2);

                deck1 = buildDeck(deckFile1, unitPool, orderedUnits);
                deck2 = buildDeck(deckFile2, unitPool, orderedUnits);
            }

            Team team1 = new Team("Team1", deck1);
            Team team2 = new Team("Team2", deck2);

            Game game = new Game(team1, team2, (int) seed);

            System.out.println(
                    "Use one of the following commands: " +
                            "select, board, move, flip, block, hand, place, show, yield, state, quit."
            );

            game.run();

        } catch (IOException e) {
            System.out.println("ERROR: Unable to read file.");
        }
    }

    /* ================= ARGUMENT PARSING ================= */

    private static Map<String, String> parseArguments(String[] args) {

        Map<String, String> map = new HashMap<>();

        for (String arg : args) {

            if (!arg.contains("=")) {
                throw new IllegalArgumentException("Invalid argument format.");
            }

            String[] parts = arg.split("=", 2);

            if (map.containsKey(parts[0].toLowerCase())) {
                throw new IllegalArgumentException("Duplicate argument: " + parts[0]);
            }

            map.put(parts[0].toLowerCase(), parts[1]);
        }

        return map;
    }

    private static void validateRequired(Map<String, String> map) {

        if (!map.containsKey("units")) {
            throw new IllegalArgumentException("Missing units file.");
        }

        if (!map.containsKey("seed")) {
            throw new IllegalArgumentException("Missing seed value.");
        }

        boolean singleDeck = map.containsKey("deck");
        boolean dualDeck = map.containsKey("deck1") && map.containsKey("deck2");

        if (!singleDeck && !dualDeck) {
            throw new IllegalArgumentException("Missing deck configuration.");
        }
    }

    private static long parseSeed(String seedValue) {

        try {
            return Long.parseLong(seedValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid seed value.");
        }
    }

    /* ================= FILE PRINTING ================= */

    private static void printFile(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Path.of(filePath));

        for (String line : lines) {
            System.out.println(line);
        }
    }

    /* ================= UNIT LOADING ================= */

    private static Map<String, Unit> loadUnits(String filePath,
                                               List<Unit> orderedUnits)
            throws IOException {

        Map<String, Unit> unitMap = new HashMap<>();
        List<String> lines = Files.readAllLines(Path.of(filePath));

        for (String line : lines) {

            if (line.isBlank()) continue;

            String[] parts = line.split(";");

            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid unit format.");
            }

            String qualifier = parts[0].trim();
            String role = parts[1].trim();
            int attack = Integer.parseInt(parts[2].trim());
            int defense = Integer.parseInt(parts[3].trim());

            Unit unit = new Unit(
                    qualifier,
                    role,
                    attack,
                    defense,
                    null
            );

            unitMap.put(qualifier, unit);
            orderedUnits.add(unit);  // maintain order
        }

        return unitMap;
    }

    /* ================= DECK BUILDING ================= */

    private static Deck buildDeck(String deckFile,
                                  Map<String, Unit> unitPool,
                                  List<Unit> orderedUnits)
            throws IOException {

        List<String> lines = Files.readAllLines(Path.of(deckFile));
        List<Unit> units = new ArrayList<>();

        for (String line : lines) {

            if (line.isBlank()) continue;

            String value = line.trim();

            Unit base;

            // Numeric index (1-based)
            if (value.matches("\\d+")) {

                int index = Integer.parseInt(value);

                if (index < 1 || index > orderedUnits.size()) {
                    throw new IllegalArgumentException("Invalid unit index in deck.");
                }

                Unit original = orderedUnits.get(index - 1);

                base = new Unit(
                        original.getQualifier(),
                        original.getRole(),
                        original.getAttack(),
                        original.getDefense(),
                        null
                );

            } else {

                if (!unitPool.containsKey(value)) {
                    throw new IllegalArgumentException("Unknown unit in deck.");
                }

                Unit original = unitPool.get(value);

                base = new Unit(
                        original.getQualifier(),
                        original.getRole(),
                        original.getAttack(),
                        original.getDefense(),
                        null
                );
            }

            units.add(base);
        }

        if (units.size() != 40) {
            throw new IllegalArgumentException("Deck must contain exactly 40 units.");
        }

        return new Deck(units);
    }
}