package farmland;

import java.util.Objects;

/**
 * Represents a team in the game.
 */
public class Team {

    public static final int INITIAL_LIFE_POINTS = 8000;
    public static final int MAX_BOARD_UNITS = 5;

    private final String name;
    private final Deck deck;
    private final Hand hand;
    private final FarmerKing king;

    private int lifePoints;
    private int boardUnitCount;

    private boolean ai;   // 🔥 AI FLAG

    public Team(String name, Deck deck) {

        this.name = Objects.requireNonNull(name);
        this.deck = Objects.requireNonNull(deck);
        this.hand = new Hand();
        this.king = new FarmerKing(this);

        this.lifePoints = INITIAL_LIFE_POINTS;
        this.boardUnitCount = 0;
        this.ai = false; // default human
    }

    /* ================= AI SUPPORT ================= */

    public boolean isAI() {
        return ai;
    }

    public void setAI(boolean value) {
        this.ai = value;
    }

    /* ================= GETTERS ================= */

    public String getName() { return name; }

    public int getLifePoints() { return lifePoints; }

    public Deck getDeck() { return deck; }

    public Hand getHand() { return hand; }

    public FarmerKing getKing() { return king; }

    public int getBoardUnitCount() { return boardUnitCount; }

    /* ================= DAMAGE ================= */

    public void takeDamage(int damage) {

        if (damage < 0) {
            throw new IllegalArgumentException("Negative damage.");
        }

        lifePoints = Math.max(0, lifePoints - damage);
    }

    public boolean hasLostByLifePoints() {
        return lifePoints == 0;
    }

    /* ================= DRAW ================= */

    public boolean draw() {

        if (deck.isEmpty()) return false;
        if (hand.isFull()) return false;

        Unit unit = deck.draw();
        unit.setOwner(this);
        hand.add(unit);

        return true;
    }

    public boolean cannotDraw() {
        return deck.isEmpty();
    }

    /* ================= BOARD COUNT ================= */

    public void incrementBoardUnits() {
        boardUnitCount++;
    }

    public void decrementBoardUnits() {
        if (boardUnitCount > 0) boardUnitCount--;
    }

    public boolean hasMaxBoardUnits() {
        return boardUnitCount >= MAX_BOARD_UNITS;
    }
}