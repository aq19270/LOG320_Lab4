package Laboratoire4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Evaluation {
    final static double CONCENTRATION_COEFFICIENT = 15;
    final static double MOBILITY_COEFFICIENT = 12;
    final static double QUADS_COEFFICIENT = 9;
    final static double CENTRALISATION_COEFFICIENT = 7;

    //////////////////
    // MOBILITY CONST
    /////////////////
    final static double CAPTURE_MODIFIER = 1.5;
    final static double MOVE_VALUE = 1.0;
    final static double EDGE_COEFFICIENT = 0.3;

    /**
     * TODO Il faut référencer proprement le code ici pour ne pas perdre de points
     * https://dke.maastrichtuniversity.nl/m.winands/documents/informed_search.pdf
     * P.22
     */
    final static int[][] WEIGHT_MATRIX = {
            /* 1 2 3 4 5 6 7 8 */
            /* A */ { -80, -25, -20, -20, -20, -20, -25, -80 },
            /* B */ { -25, 10, 10, 10, 10, 10, 10, -25 },
            /* C */ { -20, 10, 25, 25, 25, 25, 10, -20 },
            /* D */ { -20, 10, 25, 50, 50, 25, 10, -20 },
            /* E */ { -20, 10, 25, 50, 50, 25, 10, -20 },
            /* F */ { -20, 10, 25, 25, 25, 25, 10, -20 },
            /* G */ { -25, 10, 10, 10, 10, 10, 10, -25 },
            /* H */ { -80, -25, -20, -20, -20, -20, -25, -80 },
    };

    public static double evaluateBoard(Board board) {
        // return naiveEvaluateBoard(board);
        return smartEvaluateBoard(board);
    }

    private static double naiveEvaluateBoard(Board board) {
        return (int) evaluateCentralisation(board);
    }

    private static double smartEvaluateBoard(Board board) {
        double playerScore = 0;

        boolean isWinning = isPlayerWinning(board);
        boolean isLosing = isEnnemyWinning(board);

        if (isWinning && isLosing) {
            return 0; // DRAW
        }

        if (isWinning) {
            return Double.POSITIVE_INFINITY;
        }

        if (isLosing) {
            return Double.NEGATIVE_INFINITY;
        }

        playerScore += evaluateMobility(board) * MOBILITY_COEFFICIENT;
        playerScore += evaluateCentralisation(board) * CENTRALISATION_COEFFICIENT;
        playerScore += evaluateConcentration(board) * CONCENTRATION_COEFFICIENT;
        playerScore += evaluateQuads(board) * QUADS_COEFFICIENT;
        return playerScore;
    }

    public static double evaluateMobility(Board board) {
        ArrayList<String> allPlayerMove = Movement.generateAllPossibleMoves(board, board.getPlayerColor().getValue());
        ArrayList<String> allEnnemyMove = Movement.generateAllPossibleMoves(board, board.getEnnemyColor().getValue());

        double maxPossibleValue = Math.max(allPlayerMove.size(), allEnnemyMove.size()) * CAPTURE_MODIFIER;
        double playerMobilityValue = getMovesValue(board, allPlayerMove, board.getPlayerColor());
        double ennemyMobilityValue = getMovesValue(board, allEnnemyMove, board.getEnnemyColor());

        return (playerMobilityValue - ennemyMobilityValue) / maxPossibleValue;
    }

    private static double getMovesValue(Board board, ArrayList<String> moves, Pion.colors color) {
        Case currentCase;
        int x, y;
        double moveValue;
        double mobilityValue = 0;

        for (String move : moves) {
            moveValue = MOVE_VALUE;
            int[] position = Movement.getPosFromString(move.substring(2));
            x = position[0];
            y = position[1];

            currentCase = board.getCase(x, y);
            if (!currentCase.isEmpty() && currentCase.getPion().getColor() != color) {
                moveValue *= CAPTURE_MODIFIER;
            }

            if (isOnEdge(x, y)) {
                moveValue *= EDGE_COEFFICIENT;
            }

            mobilityValue += moveValue;
        }
        return mobilityValue;
    }

    public static double evaluateCentralisation(Board board) {
        AtomicInteger playerScore = new AtomicInteger();
        AtomicInteger ennemyScore = new AtomicInteger();

        final double MAX_POSSIBLE_VALUE = 4 * 50 + 8 * 25;

        board.getPlayerPions().forEach(pion -> {
            playerScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        board.getEnnemyPions().forEach(pion -> {
            ennemyScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        return (playerScore.get() - ennemyScore.get()) / MAX_POSSIBLE_VALUE;
    }

    private static boolean isOnEdge(int x, int y) {
        final int MIN = 0;
        final int MAX = 7;

        return (x == MIN || x == MAX) || (y == MIN || y == MAX);
    }

    public static boolean isPlayerWinning(Board board) {
        LinkedList<Pion> visited = new LinkedList<>();
        LinkedList<Pion> toVisit = new LinkedList<>();
        toVisit.push(board.getPlayerPions().get(0));

        Pion currentPion = null;
        while (toVisit.size() > 0) {
            currentPion = toVisit.pop();
            visited.push(currentPion);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0) {
                        continue; // don't evaluate itself
                    }

                    int caseX = x + currentPion.getX();
                    int caseY = y + currentPion.getY();

                    if (!Board.inBound(caseX, 0, 7) || !Board.inBound(caseY, 0, 7)) {
                        continue;
                    }

                    if (!board.getCase(caseX, caseY).isEmpty()
                            && board.getCase(caseX, caseY).getPion().getColor() == board.getPlayerColor()
                            && !visited.contains(board.getCase(caseX, caseY).getPion())
                            && !toVisit.contains(board.getCase(caseX, caseY).getPion())) {
                        toVisit.push(board.getCase(caseX, caseY).getPion());
                    }
                }
            }
        }

        return visited.size() == board.getPlayerPions().size();
    }

    public static boolean isEnnemyWinning(Board board) {
        LinkedList<Pion> visited = new LinkedList<>();
        LinkedList<Pion> toVisit = new LinkedList<>();
        toVisit.push(board.getEnnemyPions().get(0));

        Pion currentPion = null;
        while (toVisit.size() > 0) {
            currentPion = toVisit.pop();
            visited.push(currentPion);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0) {
                        continue; // don't evaluate itself
                    }

                    int caseX = x + currentPion.getX();
                    int caseY = y + currentPion.getY();

                    if (!Board.inBound(caseX, 0, 7) || !Board.inBound(caseY, 0, 7)) {
                        continue;
                    }

                    if (!board.getCase(caseX, caseY).isEmpty()
                            && board.getCase(caseX, caseY).getPion().getColor() == board.getEnnemyColor()
                            && !visited.contains(board.getCase(caseX, caseY).getPion())
                            && !toVisit.contains(board.getCase(caseX, caseY).getPion())) {
                        toVisit.push(board.getCase(caseX, caseY).getPion());
                    }
                }
            }
        }

        return visited.size() == board.getEnnemyPions().size();
    }

    private static float evaluateConcentration(Board board) {
        Pion com = getCentreOfMass(board);
        int difrow;
        int difcol;
        int sumDistances = 0;
        ArrayList<Pion> pionPlayer = board.getPlayerPions();
        for (Pion p : pionPlayer) {
            difrow = Math.abs(com.getY() - p.getY());
            difcol = Math.abs(com.getX() - p.getX());
            sumDistances += Math.max(difrow, difcol);
        }
        int sumMinDistances = pionPlayer.size() - 1 <= 8 ? pionPlayer.size() - 1
                : 8 + ((pionPlayer.size() - 1) % 8 * 2);
        int surplus = sumDistances - sumMinDistances;
        return surplus == 0 ? 1 : 1 / surplus;
    }

    public static Pion getCentreOfMass(Board board) {
        ArrayList<Pion> pionPlayer = board.getPlayerPions();
        int x = 0;
        int y = 0;
        for (Pion p : pionPlayer) {
            x += p.getX();
            y += p.getY();
        }
        x /= pionPlayer.size();
        y /= pionPlayer.size();
        return new Pion(x, y, Pion.colors.white);
    }

    private static double evaluateQuads(Board board) {
        int playerColor = board.getPlayerColor().getValue();
        int enemyColor = board.getEnnemyColor().getValue();
        int nbQuadsPlayer = 0;
        int nbQuadsEnemy = 0;
        Pion centreOfMass = getCentreOfMass(board);
        int minX = centreOfMass.getX() - 2 >= 0 ? centreOfMass.getX() - 2 : 0;
        int maxX = centreOfMass.getX() + 2 <= 6 ? centreOfMass.getX() + 2 : 6;
        int minY = centreOfMass.getY() - 2 >= 0 ? centreOfMass.getY() - 2 : 0;
        int maxY = centreOfMass.getY() + 2 <= 6 ? centreOfMass.getY() + 2 : 6;
        int nbCasePlayer;
        int nbCaseEnemy;
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                nbCasePlayer = 0;
                nbCaseEnemy = 0;
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        if (!board.getCase(i + k, j + l).isEmpty()) {
                            if (board.getCase(i + k, j + l).getPion().getColorValue() == playerColor) {
                                nbCasePlayer++;
                            }
                            if (board.getCase(i + k, j + l).getPion().getColorValue() == enemyColor) {
                                nbCaseEnemy++;
                            }
                        }
                    }
                }
                if (nbCasePlayer >= 3) {
                    nbQuadsPlayer++;
                }
                if (nbCaseEnemy >= 3) {
                    nbQuadsEnemy++;
                }
            }
        }
        return (nbQuadsPlayer - nbQuadsEnemy) / 10.0;
    };

}
