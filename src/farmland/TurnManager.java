package farmland;

import java.util.Objects;

/**
 * Handles turn transitions, drawing and deck-loss rule.
 */
public class TurnManager {

    private final GameState state;
    private final Board board;

    private boolean placedThisTurn;

    public TurnManager(GameState state, Board board) {
        this.state = Objects.requireNonNull(state);
        this.board = Objects.requireNonNull(board);
        this.placedThisTurn = false;
    }

    /* ================= Placement Flag ================= */

    public boolean hasPlacedThisTurn() {
        return placedThisTurn;
    }

    public void markPlaced() {
        placedThisTurn = true;
    }

    private void resetPlacement() {
        placedThisTurn = false;
    }

    /* ================= Turn End ================= */

    public void endTurn(Integer discardIndex) {

        Team current = state.getCurrentTeam();
        Hand hand = current.getHand();

        int handSize = hand.size();

        // If hand full and no discard given
        if (handSize == Hand.MAX_HAND_SIZE
                && discardIndex == null) {

            System.out.println("ERROR: "
                    + current.getName()
                    + "'s hand is full!");
            return;
        }

        // If discard given but hand not full
        if (discardIndex != null
                && handSize < Hand.MAX_HAND_SIZE) {

            System.out.println("ERROR: Invalid discard.");
            return;
        }

        // Perform discard
        if (discardIndex != null) {

            if (discardIndex < 1
                    || discardIndex > handSize) {

                System.out.println("ERROR: Invalid discard.");
                return;
            }

            Unit discarded = hand.remove(discardIndex);

            System.out.println(current.getName()
                    + " discarded "
                    + discarded.getName()
                    + " ("
                    + discarded.getAttack()
                    + "/"
                    + discarded.getDefense()
                    + ").");
        }

        resetPlacement();
        state.switchTurn();

        Team next = state.getCurrentTeam();

        System.out.println("It is "
                + next.getName()
                + "'s turn!");

        boolean drew = next.draw();

        if (!drew) {

            System.out.println(next.getName()
                    + " has no cards left in the deck!");

            Team winner = state.getOpponentOf(next);

            System.out.println(winner.getName()
                    + " wins!");

            state.endGame();
            return;
        }

        state.startTurn(board);
    }
}