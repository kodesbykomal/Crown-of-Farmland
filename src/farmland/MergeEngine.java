package farmland;

/**
 * Handles unit merging according to merge rules.
 */
public final class MergeEngine {

    private MergeEngine() {
        // utility class
    }

    public static MergeResult attemptMerge(Unit mover,
                                           Unit target) {

        if (!mover.canMerge(target)) {
            return new MergeResult(null, null);
        }

        // Create merged unit manually
        int newAttack =
                mover.getAttack() + target.getAttack();

        int newDefense =
                mover.getDefense() + target.getDefense();

        String newQualifier =
                target.getQualifier() + " "
                        + mover.getQualifier();

        String newRole =
                target.getRole();

        Unit merged =
                new Unit(
                        newQualifier,
                        newRole,
                        newAttack,
                        newDefense,
                        target.getOwner()
                );

        // Hidden state propagation
        if (!mover.isRevealed()
                || !target.isRevealed()) {
            // remains hidden
        } else {
            merged.reveal();
        }

        return new MergeResult(merged,
                MergeType.SYMBIOSIS);
    }
}