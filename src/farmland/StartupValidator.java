package farmland;

import java.io.File;
import java.util.*;

/**
 * Validates and parses program startup arguments (A.3).
 *
 * Expected format:
 * key=value key=value key=value ...
 *
 * Mandatory keys:
 * seed
 * units
 * deck1
 * deck2
 *
 * Enforces:
 * - No duplicate keys
 * - Mandatory keys present
 * - Seed numeric
 * - Files exist
 * - Deck size = 40
 */
public final class StartupValidator {

    private static final Set<String> VALID_KEYS =
            Set.of("seed", "units", "deck1", "deck2", "mode", "symbols");

    private static final List<String> REQUIRED_KEYS =
            List.of("seed", "units", "deck1", "deck2");

    private StartupValidator() {
        // utility class
    }

    /* =====================================================
       PUBLIC ENTRY
       ===================================================== */

    public static StartupConfig validate(String[] args) {

        if (args == null || args.length == 0) {
            terminate("No startup arguments provided.");
        }

        Map<String, String> values = new LinkedHashMap<>();

        for (String arg : args) {

            if (!arg.contains("=")) {
                terminate("Invalid argument format: " + arg);
            }

            String[] parts = arg.split("=", 2);

            String key = parts[0].trim();
            String value = parts[1].trim();

            if (!VALID_KEYS.contains(key)) {
                terminate("Unknown argument key: " + key);
            }

            if (values.containsKey(key)) {
                terminate("Duplicate argument key: " + key);
            }

            if (value.isEmpty()) {
                terminate("Missing value for key: " + key);
            }

            values.put(key, value);
        }

        // Check required keys
        for (String required : REQUIRED_KEYS) {
            if (!values.containsKey(required)) {
                terminate("Missing required argument: " + required);
            }
        }

        int seed = parseSeed(values.get("seed"));

        File unitsFile = validateFile(values.get("units"));
        File deck1File = validateFile(values.get("deck1"));
        File deck2File = validateFile(values.get("deck2"));

        return new StartupConfig(
                seed,
                unitsFile,
                deck1File,
                deck2File,
                values.getOrDefault("mode", "all"),
                values.getOrDefault("symbols", "standard")
        );
    }

    /* =====================================================
       HELPERS
       ===================================================== */

    private static int parseSeed(String seedStr) {

        try {
            return Integer.parseInt(seedStr);
        } catch (NumberFormatException e) {
            terminate("Seed must be numeric.");
            return 0; // unreachable
        }
    }

    private static File validateFile(String path) {

        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            terminate("File not found: " + path);
        }

        return file;
    }

    private static void terminate(String message) {

        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}