package Laboratoire4;

public class Movement {

    /**
     * TODO Il faut référencer proprement le code ici pour ne pas perdre de points
     * https://dke.maastrichtuniversity.nl/m.winands/documents/informed_search.pdf P.22
     */
    final static int[][] WEIGHT_MATRIX = {
      /*      0     1       2       3       4       5       6       7  */
      /*0*/ {-80,   -25,    -20,    -20,    -20,    -20,    -25,    -80},
      /*1*/ {-25,   10,     10,     10,     10,     10,     10,     -25},
      /*2*/ {-20,   10,     25,     25,     25,     25,     10,     -20},
      /*3*/ {-20,   10,     25,     50,     50,     25,     10,     -20},
      /*4*/ {-20,   10,     25,     50,     50,     25,     10,     -20},
      /*5*/ {-20,   10,     25,     25,     25,     25,     10,     -20},
      /*6*/ {-25,   10,     10,     10,     10,     10,     10,     -25},
      /*7*/ {-80,   -25,    -20,    -20,    -20,    -20,    -25,    -80},
    };

    public static int evaluateBoard(int[][] board, Pion.colors playerColor) {
        return naiveEvaluateBoard(board, playerColor);
    }

    private static int naiveEvaluateBoard(int[][] board, Pion.colors playerColor) {
        int playerScore = 0;
        int enemyScore = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // Empty case
                if(board[x][y] == 0) {
                    continue;
                }

                // Our case
                if(board[x][y] == playerColor.getValue()) {
                    playerScore += WEIGHT_MATRIX[x][y];
                    continue;
                }

                enemyScore += WEIGHT_MATRIX[x][y];
            }
        }

        return playerScore - enemyScore;
    }

    //    public static boolean ValidateMovement(int[][] board, colors color, String initPos, String finalPos) {
    //        System.out.println(color);
    //
    //
    //        return true;
    //    }
}


