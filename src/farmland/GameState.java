package farmland;

import java.util.Objects;

/**
 * Represents the overall state of the game.
 * Manages teams, turn order, and win conditions.
 *
 * No duel or deck logic is implemented here.
 *
 * @author uk1234
 * @version 1.1
 */
public class GameState {

    private final Team team1;
    private final Team team2;

    private Team currentTeam;
    private boolean gameOver;

    /**
     * Creates a new game state.
     *
     * @param team1 first team
     * @param team2 second team
     */
    public GameState(Team team1, Team team2) {
        this.team1 = Objects.requireNonNull(team1);
        this.team2 = Objects.requireNonNull(team2);
        this.currentTeam = team1;
        this.gameOver = false;
    }

    /* ================= BASIC GETTERS ================= */

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public Team getOpponentTeam() {
        return currentTeam == team1 ? team2 : team1;
    }

    public Team getOpponentOf(Team team) {
        if (team == null) {
            return null;
        }
        return team == team1 ? team2 : team1;
    }

    /* ================= TURN MANAGEMENT ================= */

    public void switchTurn() {
        currentTeam = getOpponentTeam();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Called when a team reaches 0 life points.
     *
     * @return winner or null
     */
    public Team checkLifePointWinner() {

        if (gameOver) {
            return null;
        }

        if (team1.hasLostByLifePoints()) {
            gameOver = true;
            return team2;
        }

        if (team2.hasLostByLifePoints()) {
            gameOver = true;
            return team1;
        }

        return null;
    }

    /**
     * Ends the game manually (used by TurnManager
     * when deck draw fails).
     */
    public void endGame() {
        gameOver = true;
    }

    /* ================= TURN RESET ================= */

    public void startTurn(Board board) {
        resetUnits(board, currentTeam);
    }

    private void resetUnits(Board board, Team team) {

        for (int row = 0; row < Position.BOARD_SIZE; row++) {
            for (int col = 0; col < Position.BOARD_SIZE; col++) {

                Position position = new Position(col, row);
                Field field = board.getField(position);

                if (!field.isEmpty()
                        && field.getUnit().getOwner() == team) {
                    field.getUnit().resetTurn();
                }
            }
        }
    }
}