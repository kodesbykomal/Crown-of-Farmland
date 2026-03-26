package farmland;

import java.util.Objects;

/**
 * Represents a unit on the board.
 */
public class Unit {

    private final String qualifier;
    private final String role;

    private int attack;
    private int defense;

    private boolean revealed;
    private boolean blocked;
    private boolean movedThisTurn;

    private Team owner;

    /* =====================================================
       CONSTRUCTOR (5 PARAMETERS — REQUIRED)
       ===================================================== */

    public Unit(String qualifier,
                String role,
                int attack,
                int defense,
                Team owner) {

        this.qualifier = Objects.requireNonNull(qualifier);
        this.role = Objects.requireNonNull(role);
        this.attack = attack;
        this.defense = defense;
        this.owner = owner;

        this.revealed = false;
        this.blocked = false;
        this.movedThisTurn = false;
    }

    /* ================= OWNER ================= */

    public Team getOwner() {
        return owner;
    }

    public void setOwner(Team owner) {
        this.owner = owner;
    }

    /* ================= BASIC GETTERS ================= */

    public String getQualifier() {
        return qualifier;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return qualifier + " " + role;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    /* ================= STATE ================= */

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        revealed = true;
    }

    public void hide() {
        revealed = false;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void block() {
        blocked = true;
        movedThisTurn = true;
    }

    public boolean hasMovedThisTurn() {
        return movedThisTurn;
    }

    public void markMoved() {
        movedThisTurn = true;
        blocked = false;
    }

    public void resetTurn() {
        movedThisTurn = false;
        blocked = false;
    }

    /* =====================================================
       MERGE COMPATIBILITY (kept here for MergeEngine)
       ===================================================== */

    public boolean canMerge(Unit other) {

        if (other == null) return false;
        if (owner != other.owner) return false;
        if (getName().equals(other.getName())) return false;

        return true; // real rules handled in MergeEngine
    }
}