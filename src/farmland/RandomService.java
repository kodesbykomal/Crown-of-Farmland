package farmland;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomService {

    private static Random random;

    private RandomService() {
        // Prevent instantiation
    }

    /**
     * Initializes the global random instance.
     * Must be called exactly once at program startup.
     */
    public static void initialize(int seed) {
        random = new Random(seed);
    }

    /**
     * Returns random integer in range [origin, bound)
     * (origin inclusive, bound exclusive)
     */
    public static int nextInt(int origin, int bound) {
        if (random == null) {
            throw new IllegalStateException("RandomService not initialized.");
        }
        return random.nextInt(origin, bound);
    }

    /**
     * Shuffles a list using deterministic seeded random.
     */
    public static void shuffle(List<?> list) {
        if (random == null) {
            throw new IllegalStateException("RandomService not initialized.");
        }
        Collections.shuffle(list, random);
    }
}