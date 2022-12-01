package Laboratoire4;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Evaluation {
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

    public static int evaluateBoard(Board board, int playerColor) {
        return naiveEvaluateBoard(board, playerColor);
    }

    private static int naiveEvaluateBoard(Board board, int playerColor) {
        AtomicInteger playerScore = new AtomicInteger();
        AtomicInteger enemyScore = new AtomicInteger();
        ArrayList<Pion> pionPlayer = (playerColor == Pion.colors.white.getValue() ? board.getPionsBlanc()
                : board.getPionsNoir());
        ArrayList<Pion> pionEnemy = (playerColor == Pion.colors.white.getValue() ? board.getPionsNoir()
                : board.getPionsBlanc());

        pionPlayer.forEach(pion -> {
            playerScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        pionEnemy.forEach(pion -> {
            enemyScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        return playerScore.get() - enemyScore.get();
    }

    private static float smartEvaluateBoard(Board board, int playerColor) {
        float score = 0;
        int quadsWeight = 1;
        float quadsScore = evaluateQuads(board, playerColor);
        score += (quadsScore * quadsWeight);
        return score;
    }

    private static float evaluateQuads(Board board, int playerColor) {
        int nbQuads = 0;
        Case centreOfMass = board.getCase(4, 4); // TO DO remplacer par la bonne fonction de détermination du centre
        int minX = centreOfMass.getPion().getX() - 2 >= 0 ? centreOfMass.getPion().getX() : 0;
        int maxX = centreOfMass.getPion().getX() + 2 <= 7 ? centreOfMass.getPion().getX() : 7;
        int minY = centreOfMass.getPion().getY() - 2 >= 0 ? centreOfMass.getPion().getY() : 0;
        int maxY = centreOfMass.getPion().getY() + 2 <= 7 ? centreOfMass.getPion().getY() : 7;
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                int nbCase = 0;
                if (board.getCase(i, j).getPion().getColorValue() == playerColor) {
                    nbCase += 1;
                }
                if (board.getCase(i + 1, j).getPion().getColorValue() == playerColor) {
                    nbCase += 1;
                }
                if (board.getCase(i, j + 1).getPion().getColorValue() == playerColor) {
                    nbCase += 1;
                }
                if (board.getCase(i + 1, j + 1).getPion().getColorValue() == playerColor) {
                    nbCase += 1;
                }
                if (nbCase >= 3) {
                    nbQuads += 1;
                }
            }
        }
        return nbQuads / 10; // retourne une valeur entre 0 et 1 mais souvent plus proche de 0
    };

}
