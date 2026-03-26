package farmland;

import java.util.List;

public class PlacementValidator {

    public static boolean validate(Board board,
                                   GameState state,
                                   Position selected,
                                   List<Integer> indices) {

        if (selected == null || indices == null || indices.isEmpty()) {
            return false;
        }

        Team team = state.getCurrentTeam();
        Field field = board.getField(selected);

        if (!field.isEmpty()
                && field.getUnit().getOwner() != team) {
            return false;
        }

        Position kingPos = board.findKing(team);

        if (!(selected.isOrthogonallyAdjacent(kingPos)
                || selected.isDiagonallyAdjacent(kingPos))) {
            return false;
        }

        Hand hand = team.getHand();
        int size = hand.size();
        boolean[] used = new boolean[size + 1];

        for (Integer idx : indices) {

            if (idx == null || idx < 1 || idx > size) {
                return false;
            }

            if (used[idx]) {
                return false;
            }

            used[idx] = true;
        }

        return true;
    }
}