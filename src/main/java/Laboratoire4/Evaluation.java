package Laboratoire4;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Evaluation {
    final static double MOBILITY_COEFFICIENT = 6;
    final static double CENTRALISATION_COEFFICIENT = 4;

    /**
     * TODO Il faut référencer proprement le code ici pour ne pas perdre de points
     * https://dke.maastrichtuniversity.nl/m.winands/documents/informed_search.pdf P.22
     */
    final static int[][] WEIGHT_MATRIX = {
            /*      1     2       3       4       5       6       7       8  */
            /*A*/ {-80,   -25,    -20,    -20,    -20,    -20,    -25,    -80},
            /*B*/ {-25,   10,     10,     10,     10,     10,     10,     -25},
            /*C*/ {-20,   10,     25,     25,     25,     25,     10,     -20},
            /*D*/ {-20,   10,     25,     50,     50,     25,     10,     -20},
            /*E*/ {-20,   10,     25,     50,     50,     25,     10,     -20},
            /*F*/ {-20,   10,     25,     25,     25,     25,     10,     -20},
            /*G*/ {-25,   10,     10,     10,     10,     10,     10,     -25},
            /*H*/ {-80,   -25,    -20,    -20,    -20,    -20,    -25,    -80},
    };


    public static double evaluateBoard(Board board, int playerColor) {
        return naiveEvaluateBoard(board, playerColor);
    }

    private static double naiveEvaluateBoard(Board board, int playerColor) {
        return (int) evaluateCentralisation(board, playerColor);
    }

    private static double smartEvaluateBoard(Board board, int playerColor) {
        double playerScore = 0;

        playerScore += evaluateMobility(board, playerColor) * MOBILITY_COEFFICIENT;
        playerScore += evaluateCentralisation(board, playerColor) * CENTRALISATION_COEFFICIENT;

        return playerScore;
    }

    public static double evaluateMobility(Board board, int playerColor) {
        final double CAPTURE_MODIFIER = 2.0;
        final double MOVE_VALUE = 1.0;
        final double EDGE_COEFFICIENT = 0.5;

        ArrayList<String> allMoves = Movement.generateAllPossibleMoves(board, playerColor);

        double mobilityValue = 0;
        double maxPossibleValue = allMoves.size() * CAPTURE_MODIFIER;

        Case currentCase;
        int x, y;
        double moveValue;

        for(String move : allMoves) {
            moveValue = MOVE_VALUE;
            int[] position = Movement.getPosFromString(move.substring(2));
            x = position[0];
            y = position[1];

            currentCase = board.getCase(x, y);
            if(!currentCase.isEmpty() && currentCase.getPion().getColorValue() != playerColor) {
                moveValue *= CAPTURE_MODIFIER;
            }

            if(isOnEdge(x, y)) {
                moveValue *= EDGE_COEFFICIENT;
            }

            mobilityValue += moveValue;
        }
        return mobilityValue / maxPossibleValue;
    }

    public static double evaluateCentralisation(Board board, int playerColor) {
        AtomicInteger playerScore = new AtomicInteger();
        ArrayList<Pion> pionPlayer = (playerColor == Pion.colors.white.getValue() ? board.getPionsBlanc() : board.getPionsNoir());

        final double MAX_POSSIBLE_VALUE = 4 * 50 + 8 * 25;

        pionPlayer.forEach(pion -> {
            playerScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        return playerScore.get() / MAX_POSSIBLE_VALUE;
    }


    private static boolean isOnEdge(int x, int y) {
        final int MIN = 0;
        final int MAX = 7;

        return (x == MIN || x == MAX) || (y == MIN || y == MAX);
    }
}
