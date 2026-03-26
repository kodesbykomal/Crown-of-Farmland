package farmland;

import java.util.List;

public final class WeightedRandomSelector {

    private WeightedRandomSelector() {
    }

    /**
     * Standard weighted selection.
     * Returns index of selected element.
     * Returns -1 if all weights are zero or negative.
     */
    public static int select(List<Integer> weights) {

        int totalWeight = 0;

        for (int weight : weights) {
            if (weight > 0) {
                totalWeight += weight;
            }
        }

        if (totalWeight <= 0) {
            return -1;
        }

        int randomValue = RandomService.nextInt(1, totalWeight + 1);

        int cumulative = 0;

        for (int i = 0; i < weights.size(); i++) {

            int weight = Math.max(weights.get(i), 0);
            cumulative += weight;

            if (randomValue <= cumulative) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Inverse weighted selection.
     * Higher original weight → lower probability.
     */
    public static int selectInverse(List<Integer> weights) {

        int maxWeight = 0;

        for (int weight : weights) {
            if (weight > maxWeight) {
                maxWeight = weight;
            }
        }

        int totalWeight = 0;
        int[] transformed = new int[weights.size()];

        for (int i = 0; i < weights.size(); i++) {
            transformed[i] = Math.max(maxWeight - weights.get(i), 0);
            totalWeight += transformed[i];
        }

        if (totalWeight <= 0) {
            return -1;
        }

        int randomValue = RandomService.nextInt(1, totalWeight + 1);

        int cumulative = 0;

        for (int i = 0; i < transformed.length; i++) {

            cumulative += transformed[i];

            if (randomValue <= cumulative) {
                return i;
            }
        }

        return -1;
    }
}