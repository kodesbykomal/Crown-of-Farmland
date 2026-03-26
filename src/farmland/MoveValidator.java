package farmland;

public class MoveValidator {

    public static boolean validate(Position from,
                                   Position to,
                                   Field fromField) {

        if (from == null || to == null) {
            return false;
        }

        if (fromField == null || fromField.isEmpty()) {
            return false;
        }

        if (!from.equals(to)
                && !from.isOrthogonallyAdjacent(to)) {
            return false;
        }

        return true;
    }
}