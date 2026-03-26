package farmland;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI Engine implementing weighted decision-making.
 * Deterministic based on provided Random seed.
 */
public class AIEngine {

    private final GameState state;
    private final Board board;
    private final Random random;

    public AIEngine(GameState state,
                    Board board,
                    Random random) {

        this.state = state;
        this.board = board;
        this.random = random;
    }

    /* =====================================================
       MAIN AI TURN
       ===================================================== */

    public void executeTurn(CommandProcessor processor) {

        Team team = state.getCurrentTeam();

        // 1️⃣ Placement
        attemptPlacement(processor, team);

        // 2️⃣ Movement
        attemptMovement(processor, team);

        // 3️⃣ Block
        attemptBlock(processor, team);

        // 4️⃣ Discard if needed (inverse weighted)
        attemptDiscardIfNeeded(team);

        // 5️⃣ End turn
        processor.yield(null);
    }

    /* =====================================================
       PLACEMENT LOGIC
       ===================================================== */

    private void attemptPlacement(CommandProcessor processor,
                                  Team team) {

        if (team.getHand().size() == 0) return;

        Position kingPos = findKing(team);
        if (kingPos == null) return;

        List<Position> candidates = board.getAllNeighbors(kingPos);
        List<ScoredPosition> scored = new ArrayList<>();

        for (Position pos : candidates) {

            Field field = board.getField(pos);

            if (!field.isEmpty()
                    && field.getUnit().getOwner() != team) {
                continue;
            }

            int enemyPressure = board.countAllEnemies(pos, team);

            int score = 5 + (enemyPressure * 4);

            scored.add(new ScoredPosition(pos, score));
        }

        if (scored.isEmpty()) return;

        Position chosen = weightedPosition(scored);

        List<Integer> indices = new ArrayList<>();
        indices.add(1); // place first card for now

        processor.select(chosen.toString());
        processor.place(indices);
    }

    /* =====================================================
       MOVEMENT LOGIC
       ===================================================== */

    private void attemptMovement(CommandProcessor processor,
                                 Team team) {

        List<ScoredMove> moves = new ArrayList<>();

        for (int r = 0; r < Position.BOARD_SIZE; r++) {
            for (int c = 0; c < Position.BOARD_SIZE; c++) {

                Position fromPos = new Position(c, r);
                Field fromField = board.getField(fromPos);

                if (fromField.isEmpty()) continue;

                Unit unit = fromField.getUnit();
                if (unit.getOwner() != team) continue;
                if (unit.hasMovedThisTurn()) continue;

                for (Position neighbor :
                        board.getOrthogonalNeighbors(fromPos)) {

                    Field target = board.getField(neighbor);

                    int score = 1;

                    if (!target.isEmpty()
                            && target.getUnit().getOwner() != team) {

                        Unit enemy = target.getUnit();
                        score = unit.getAttack()
                                - enemy.getAttack()
                                + 10;
                    }

                    score += kingSafetyScore(team);

                    moves.add(
                            new ScoredMove(
                                    fromPos,
                                    neighbor,
                                    Math.max(score, 1)));
                }
            }
        }

        if (moves.isEmpty()) return;

        ScoredMove chosen = weightedMove(moves);

        processor.select(chosen.from.toString());
        processor.move(chosen.to.toString());
    }

    /* =====================================================
       BLOCK LOGIC
       ===================================================== */

    private void attemptBlock(CommandProcessor processor,
                              Team team) {

        for (int r = 0; r < Position.BOARD_SIZE; r++) {
            for (int c = 0; c < Position.BOARD_SIZE; c++) {

                Position pos = new Position(c, r);
                Field field = board.getField(pos);

                if (field.isEmpty()) continue;

                Unit unit = field.getUnit();
                if (unit.getOwner() != team) continue;
                if (unit.hasMovedThisTurn()) continue;

                int enemyCount =
                        board.countAllEnemies(pos, team);

                if (enemyCount >= 2) {
                    processor.select(pos.toString());
                    processor.block();
                    return;
                }
            }
        }
    }

    /* =====================================================
       INVERSE WEIGHTED DISCARD
       ===================================================== */

    private void attemptDiscardIfNeeded(Team team) {

        if (!team.getHand().isFull()) return;

        List<Unit> units = team.getHand().getUnits();
        List<Integer> weights = new ArrayList<>();

        for (Unit u : units) {

            int value = u.getAttack() + u.getDefense();

            // Inverse weight: weaker cards more likely discarded
            int weight = Math.max(1, 10000 / value);

            weights.add(weight);
        }

        int idx = weightedIndex(weights);

        team.getHand().remove(idx + 1);
    }

    /* =====================================================
       KING SAFETY SCORE
       ===================================================== */

    private int kingSafetyScore(Team team) {

        Position kingPos = findKing(team);
        if (kingPos == null) return 0;

        int enemies =
                board.countAllEnemies(kingPos, team);

        return -enemies * 3;
    }

    /* =====================================================
       WEIGHTED HELPERS
       ===================================================== */

    private Position weightedPosition(
            List<ScoredPosition> list) {

        List<Integer> weights = new ArrayList<>();

        for (ScoredPosition s : list) {
            weights.add(s.score);
        }

        int idx = weightedIndex(weights);
        return list.get(idx).position;
    }

    private ScoredMove weightedMove(
            List<ScoredMove> list) {

        List<Integer> weights = new ArrayList<>();

        for (ScoredMove s : list) {
            weights.add(s.score);
        }

        int idx = weightedIndex(weights);
        return list.get(idx);
    }

    private int weightedIndex(List<Integer> weights) {

        int total = 0;
        for (int w : weights) total += w;

        int r = random.nextInt(total);

        int cumulative = 0;
        for (int i = 0; i < weights.size(); i++) {

            cumulative += weights.get(i);

            if (r < cumulative) return i;
        }

        return 0;
    }

    /* =====================================================
       FIND KING
       ===================================================== */

    private Position findKing(Team team) {

        for (int r = 0; r < Position.BOARD_SIZE; r++) {
            for (int c = 0; c < Position.BOARD_SIZE; c++) {

                Position pos = new Position(c, r);
                Field field = board.getField(pos);

                if (!field.isEmpty()
                        && field.getUnit() instanceof FarmerKing
                        && field.getUnit().getOwner() == team) {
                    return pos;
                }
            }
        }

        return null;
    }

    /* =====================================================
       HELPER STRUCTS
       ===================================================== */

    private static class ScoredPosition {
        Position position;
        int score;
        ScoredPosition(Position p, int s) {
            position = p;
            score = s;
        }
    }

    private static class ScoredMove {
        Position from;
        Position to;
        int score;
        ScoredMove(Position f, Position t, int s) {
            from = f;
            to = t;
            score = s;
        }
    }
}