package Laboratoire4;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Evaluation {
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


    public static int evaluateBoard(Board board, int playerColor) {
        return naiveEvaluateBoard(board, playerColor);
    }

    private static int naiveEvaluateBoard(Board board, int playerColor) {
        AtomicInteger playerScore = new AtomicInteger();
        AtomicInteger enemyScore = new AtomicInteger();
        ArrayList<Pion> pionPlayer = (playerColor == Pion.colors.white.getValue() ? board.getPionsBlanc() : board.getPionsNoir());
        ArrayList<Pion> pionEnemy = (playerColor == Pion.colors.white.getValue() ? board.getPionsNoir() : board.getPionsBlanc());

        pionPlayer.forEach(pion -> {
            playerScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        pionEnemy.forEach(pion -> {
            enemyScore.addAndGet(WEIGHT_MATRIX[pion.getX()][pion.getY()]);
        });

        return playerScore.get() - enemyScore.get();
    }

    private static int smartEvaluateBoard(Board board, int playerColor) {
        return 0;
    }

    private static float concentration(Board board, int playerColor) {
        Pion com = getCentreOfMass(board, playerColor);
        int difrow;
        int difcol;
        int sumDistances = 0;
        ArrayList<Pion> pionPlayer = (playerColor == Pion.colors.white.getValue() ? board.getPionsBlanc() : board.getPionsNoir());
        for (Pion p : pionPlayer) {
            difrow = Math.abs(com.getY() - p.getY());
            difcol = Math.abs(com.getX() - p.getX());
            sumDistances += Math.max(difrow, difcol);
        }
        int sumMinDistances = pionPlayer.size() - 1 <= 8 ? pionPlayer.size() - 1 : 8 + ((pionPlayer.size() - 1) % 8 * 2);
        int surplus = sumDistances - sumMinDistances;
        return 1 / surplus;
    }

    public static Pion getCentreOfMass(Board board, int playerColor) {
        ArrayList<Pion> pionPlayer = (playerColor == Pion.colors.white.getValue() ? board.getPionsBlanc() : board.getPionsNoir());
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

    public static float centreOfMassPosition(Board board, int playerColor) {

        return 0;
    }
}
