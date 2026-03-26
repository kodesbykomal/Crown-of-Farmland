package farmland;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Board board;
    private final GameState state;
    private final CommandProcessor processor;
    private final AIEngine aiEngine;

    private boolean running;

    public Game(Team team1, Team team2, int seed) {

        this.board = new Board();
        this.state = new GameState(team1, team2);

        Random random = new Random(seed);

        DuelEngine duelEngine =
                new DuelEngine(state, board);

        TurnManager turnManager =
                new TurnManager(state, board);

        this.processor =
                new CommandProcessor(
                        state,
                        board,
                        duelEngine,
                        turnManager
                );

        this.aiEngine =
                new AIEngine(state, board, random);

        this.running = true;

        initialize(random);
    }

    private void initialize(Random random) {

        state.getTeam1().getDeck().shuffle(random);
        state.getTeam2().getDeck().shuffle(random);

        for (int i = 0; i < 4; i++) {
            state.getTeam1().draw();
            state.getTeam2().draw();
        }

        board.placeUnit(Position.fromString("D1"),
                state.getTeam1().getKing());

        board.placeUnit(Position.fromString("D7"),
                state.getTeam2().getKing());

        System.out.println("Game started.");
        StateRenderer.render(state);
        BoardRenderer.render(board, null, false);
    }

    public void run() {

        Scanner scanner = new Scanner(System.in);

        while (running && !state.isGameOver()) {

            Team current = state.getCurrentTeam();

            if (current.isAI()) {
                aiEngine.executeTurn(processor);
                continue;
            }

            System.out.print(current.getName() + "> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                running = false;
                continue;
            }

            dispatch(input);
        }

        scanner.close();
    }

    private void dispatch(String input) {

        String[] parts = input.split("\\s+");
        String command = parts[0].toLowerCase();

        switch (command) {

            case "board" -> processor.board();
            case "select" -> processor.select(parts[1]);
            case "move" -> processor.move(parts[1]);
            case "flip" -> processor.flip();
            case "block" -> processor.block();
            case "hand" -> processor.hand();
            case "place" -> processor.place(parseIndices(parts));
            case "yield" -> processor.yield(
                    parts.length > 1
                            ? Integer.parseInt(parts[1])
                            : null);
            case "state" -> processor.state();
            case "show" -> processor.show();
            default -> System.out.println("ERROR: Unknown command.");
        }
    }

    private java.util.List<Integer> parseIndices(String[] parts) {

        java.util.List<Integer> list =
                new java.util.ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            list.add(Integer.parseInt(parts[i]));
        }

        return list;
    }
}