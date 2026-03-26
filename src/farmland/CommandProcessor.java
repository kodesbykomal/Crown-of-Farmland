package farmland;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandProcessor {

    private final GameState state;
    private final Board board;
    private final DuelEngine duelEngine;
    private final TurnManager turnManager;

    private Position selected;

    CommandProcessor(GameState state,
                     Board board,
                     DuelEngine duelEngine,
                     TurnManager turnManager) {

        this.state = state;
        this.board = board;
        this.duelEngine = duelEngine;
        this.turnManager = turnManager;
    }

    /* ================= SELECT ================= */

    public void select(String fieldStr) {

        try {
            selected = Position.fromString(fieldStr);
            BoardRenderer.render(board, selected, false);
            show();
        } catch (Exception e) {
            OutputFormatter.error("Invalid field.");
        }
    }

    /* ================= MOVE ================= */

    public void move(String fieldStr) {

        if (selected == null) {
            OutputFormatter.error("No field selected.");
            return;
        }

        Position target;

        try {
            target = Position.fromString(fieldStr);
        } catch (Exception e) {
            OutputFormatter.error("Invalid field.");
            return;
        }

        Field fromField = board.getField(selected);

        if (!MoveValidator.validate(selected,
                target,
                fromField)) {

            OutputFormatter.error("Illegal move.");
            return;
        }

        Field to = board.getField(target);
        Unit unit = fromField.getUnit();

        if (to.isEmpty()) {

            fromField.removeUnit();
            to.setUnit(unit);
            unit.markMoved();

            System.out.println(unit.getName()
                    + " moves to "
                    + target + ".");

        } else {

            Unit defender = to.getUnit();

            if (defender.getOwner() == unit.getOwner()) {

                MergeResult result =
                        MergeEngine.attemptMerge(unit, defender);

                if (result.isSuccess()) {
                    to.setUnit(result.getMergedUnit());
                    fromField.removeUnit();
                }

            } else {
                duelEngine.execute(unit, defender, fromField, to);
            }
        }

        BoardRenderer.render(board, selected, false);
        show();
    }

    /* ================= PLACE ================= */

    public void place(List<Integer> indices) {

        if (!PlacementValidator.validate(
                board,
                state,
                selected,
                indices)) {

            OutputFormatter.error("Invalid placement.");
            return;
        }

        if (turnManager.hasPlacedThisTurn()) {
            OutputFormatter.error(
                    "Units already placed this turn.");
            return;
        }

        turnManager.markPlaced();

        Team team = state.getCurrentTeam();
        Hand hand = team.getHand();
        Field field = board.getField(selected);

        List<Integer> copy =
                new ArrayList<>(indices);
        Collections.sort(copy,
                Collections.reverseOrder());

        for (Integer idx : copy) {

            Unit unit = hand.remove(idx);

            OutputFormatter.printHeader(
                    team.getName()
                            + " places "
                            + unit.getName()
                            + " on "
                            + selected + ".");

            if (field.isEmpty()) {

                field.setUnit(unit);
                team.incrementBoardUnits();

            } else {

                MergeResult result =
                        MergeEngine.attemptMerge(
                                unit,
                                field.getUnit());

                if (result.isSuccess()) {
                    field.setUnit(result.getMergedUnit());
                }
            }
        }

        BoardRenderer.render(board, selected, false);
        show();
    }

    /* ================= YIELD ================= */

    public void yield(Integer idx) {
        selected = null;
        turnManager.endTurn(idx);
    }

    /* ================= STATE ================= */

    public void state() {
        StateRenderer.render(state);
        BoardRenderer.render(board, selected, false);
    }

    /* ================= SHOW ================= */

    public void show() {

        if (selected == null) {
            return;
        }

        Field field = board.getField(selected);

        if (field.isEmpty()) {
            System.out.println("<no unit>");
            return;
        }

        Unit unit = field.getUnit();

        System.out.println(unit.getName());
        System.out.println("ATK: "
                + unit.getAttack());
        System.out.println("DEF: "
                + unit.getDefense());
    }

    /* ================= BLOCK ================= */

    public void block() {

        if (selected == null) {
            OutputFormatter.error("No field selected.");
            return;
        }

        Field field = board.getField(selected);

        if (field.isEmpty()) {
            OutputFormatter.error("No unit on selected field.");
            return;
        }

        Unit unit = field.getUnit();

        if (unit.hasMovedThisTurn()) {
            OutputFormatter.error("Cannot block.");
            return;
        }

        unit.block();

        System.out.println(unit.getName()
                + " blocks on "
                + selected + ".");

        BoardRenderer.render(board, selected, false);
    }

    /* ===================================================== */
    /* ================= FLIP ============================== */
    /* ===================================================== */

    public void flip() {

        if (selected == null) {
            OutputFormatter.error("No field selected.");
            return;
        }

        Field field = board.getField(selected);

        if (field.isEmpty()) {
            OutputFormatter.error("No unit on selected field.");
            return;
        }

        Unit unit = field.getUnit();

        if (unit.isRevealed() || unit.hasMovedThisTurn()) {
            OutputFormatter.error("Cannot flip.");
            return;
        }

        unit.reveal();

        System.out.println(unit.getName()
                + " (" + unit.getAttack()
                + "/" + unit.getDefense()
                + ") was flipped on "
                + selected + "!");

        BoardRenderer.render(board, selected, false);
    }




    public void hand() {

        Team team = state.getCurrentTeam();

        for (int i = 1; i <= team.getHand().size(); i++) {

            Unit u = team.getHand().get(i);

            System.out.println("[" + i + "] "
                    + u.getName()
                    + " (" + u.getAttack()
                    + "/" + u.getDefense() + ")");
        }
    }

    /* ===================================================== */
    /* ================= BOARD ============================= */
    /* ===================================================== */

    public void board() {
        renderBoard();
    }

    private void renderBoard() {
        board.print(selected);
    }

    private void error(String msg) {
        System.out.println("ERROR: " + msg);
    }
}


