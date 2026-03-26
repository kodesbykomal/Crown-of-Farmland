package farmland;

/**
 * Handles duel execution according to A.1.4.
 */
public class DuelEngine {

    private final GameState state;
    private final Board board;

    public DuelEngine(GameState state, Board board) {
        this.state = state;
        this.board = board;
    }

    public void execute(Unit attacker,
                        Unit defender,
                        Field from,
                        Field to) {

        Position attackPos = from.getPosition();
        Position defendPos = to.getPosition();

        Team attackingTeam = state.getCurrentTeam();
        Team defendingTeam = state.getOpponentTeam();

        /* ================= ATTACK LINE ================= */

        String defenderName = defender.isRevealed()
                ? defender.getName()
                : "???";

        System.out.println(attacker.getName()
                + " (" + attacker.getAttack()
                + "/" + attacker.getDefense()
                + ") attacks "
                + defenderName
                + " on " + defendPos + "!");

        /* ================= FLIP ATTACKER ================= */

        if (!attacker.isRevealed()) {
            attacker.reveal();
            System.out.println(attacker.getName()
                    + " (" + attacker.getAttack()
                    + "/" + attacker.getDefense()
                    + ") was flipped on "
                    + attackPos + "!");
        }

        /* ================= FLIP DEFENDER ================= */

        if (!defender.isRevealed()) {
            defender.reveal();
            System.out.println(defender.getName()
                    + " (" + defender.getAttack()
                    + "/" + defender.getDefense()
                    + ") was flipped on "
                    + defendPos + "!");
        }

        /* ================= FARMER KING ================= */

        if (defender instanceof FarmerKing) {

            int damage = attacker.getAttack();
            defendingTeam.takeDamage(damage);

            if (damage > 0) {
                System.out.println(defendingTeam.getName()
                        + " takes " + damage + " damage!");
            }

            checkLife(defendingTeam);
            return;
        }

        /* ================= BLOCK CASE ================= */

        if (defender.isBlocked()) {

            int atk = attacker.getAttack();
            int def = defender.getDefense();

            if (atk > def) {
                eliminate(defender, defendPos);
            } else if (atk < def) {

                attackingTeam.takeDamage(def);

                if (def > 0) {
                    System.out.println(attackingTeam.getName()
                            + " takes " + def + " damage!");
                }

                checkLife(attackingTeam);
            }

            return;
        }

        /* ================= NORMAL DUEL ================= */

        int atkA = attacker.getAttack();
        int atkD = defender.getAttack();

        if (atkA > atkD) {

            int damage = atkA - atkD;

            eliminate(defender, defendPos);

            defendingTeam.takeDamage(damage);

            if (damage > 0) {
                System.out.println(defendingTeam.getName()
                        + " takes " + damage + " damage!");
            }

            moveAttacker(attacker, from, to);
            checkLife(defendingTeam);

        } else if (atkA < atkD) {

            int damage = atkD - atkA;

            eliminate(attacker, attackPos);

            attackingTeam.takeDamage(damage);

            if (damage > 0) {
                System.out.println(attackingTeam.getName()
                        + " takes " + damage + " damage!");
            }

            checkLife(attackingTeam);

        } else {

            /* Draw → defender eliminated first */
            eliminate(defender, defendPos);
            eliminate(attacker, attackPos);
        }
    }

    private void moveAttacker(Unit attacker,
                              Field from,
                              Field to) {

        from.removeUnit();
        to.setUnit(attacker);

        System.out.println(attacker.getName()
                + " moves to "
                + to.getPosition() + ".");
    }

    private void eliminate(Unit unit,
                           Position pos) {

        System.out.println(unit.getName() + " was eliminated!");

        board.getField(pos).removeUnit();
        unit.getOwner().decrementBoardUnits();
    }

    private void checkLife(Team team) {

        Team winner = state.checkLifePointWinner();

        if (winner != null) {
            System.out.println(team.getName()
                    + "'s life points dropped to 0!");
            System.out.println(winner.getName()
                    + " wins!");
        }
    }
}