package farmland;

/**
 * Result of a merge attempt.
 */
public final class MergeResult {

    private final Unit mergedUnit;
    private final MergeType type;

    public MergeResult(Unit mergedUnit, MergeType type) {
        this.mergedUnit = mergedUnit;
        this.type = type;
    }

    public boolean isSuccess() {
        return mergedUnit != null;
    }

    public Unit getMergedUnit() {
        return mergedUnit;
    }

    public MergeType getType() {
        return type;
    }
}