package farmland;

public class StateRenderer {

    public static void render(GameState state) {

        Team t1 = state.getTeam1();
        Team t2 = state.getTeam2();

        String header = t1.getName() + " vs " + t2.getName();
        System.out.println(OutputFormatter.formatToWidth(header));

        String lifeLine =
                t1.getLifePoints() + "/8000 LP   "
                        + t2.getLifePoints() + "/8000 LP";

        System.out.println(OutputFormatter.formatToWidth(lifeLine));

        String deckLine =
                "DC: " + t1.getDeck().size() + "/40   "
                        + "DC: " + t2.getDeck().size() + "/40";

        System.out.println(OutputFormatter.formatToWidth(deckLine));

        String boardLine =
                "BC: " + t1.getBoardUnitCount() + "/5   "
                        + "BC: " + t2.getBoardUnitCount() + "/5";

        System.out.println(OutputFormatter.formatToWidth(boardLine));
    }
}