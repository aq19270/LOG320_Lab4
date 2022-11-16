package Laboratoire4;

import java.util.ArrayList;

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

    public static int evaluateBoard(int[][] board, int playerColor) {
        return naiveEvaluateBoard(board, playerColor);
    }

    private static int naiveEvaluateBoard(int[][] board, int playerColor) {
        int playerScore = 0;
        int enemyScore = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.println("x: " + x + " y: "  + y);

                // Empty case
                if(board[x][y] == 0) {
                    continue;
                }

                // Our case
                if(board[x][y] == playerColor) {
                    playerScore += WEIGHT_MATRIX[x][y];
                    continue;
                }

                enemyScore += WEIGHT_MATRIX[x][y];
            }
        }

        return playerScore - enemyScore;
    }

    public static ArrayList<String> generateAllPossibleMoves(int[][] board, int playerColor) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        System.out.println(playerColor);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if(board[x][y] == playerColor) {
                    possibleMoves.addAll(generateAllMoveForPion(board, playerColor, new Pion(x,y)));
                }
            }
        }
        return possibleMoves;
    }

    private static ArrayList<String> generateAllMoveForPion(int[][] board, int playerColor, Pion pion) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(getHorizontalMoves(board, playerColor, pion));
        possibleMoves.addAll(getVerticalMoves(board, playerColor, pion));
        possibleMoves.addAll(getLeftDiagMoves(board, playerColor, pion));
        possibleMoves.addAll(getRightDiagMoves(board, playerColor, pion));
        return possibleMoves;
    }

    private static ArrayList<String> getHorizontalMoves(int[][] board, int playerColor, Pion pion){
        int qtyPionInRow = 0;
        int nearestLeftEnemyPionPos = Integer.MIN_VALUE;
        int nearestRightEnemyPionPos = Integer.MAX_VALUE;

        for (int x = 0; x < pion.getX(); x++) {
            if(board[x][pion.getY()] == 0) {
                continue;
            }

            if(board[x][pion.getY()] != playerColor) {
                nearestLeftEnemyPionPos = x;
            }

            qtyPionInRow ++;
        }

        for (int x = 7; x > pion.getX(); x--) {
            if(board[x][pion.getY()] == 0) {
                continue;
            }

            if(board[x][pion.getY()] != playerColor) {
                nearestRightEnemyPionPos = x;
            }

            qtyPionInRow ++;
        }

        int distance = 1 + qtyPionInRow;
        int leftXMovement = pion.getX() - distance;
        int rightXMovement = pion.getX() + distance;

        ArrayList<String> possibleMoves = new ArrayList<>();
        String currentPos = getStringFromPos(pion.getX(), pion.getY());

        if(leftXMovement > 0 && (nearestLeftEnemyPionPos == Integer.MIN_VALUE || leftXMovement <= nearestLeftEnemyPionPos)) {
            possibleMoves.add(
                    currentPos + getStringFromPos(leftXMovement, pion.getY())
            );
        }

        if(rightXMovement < 8 && (nearestRightEnemyPionPos == Integer.MAX_VALUE || rightXMovement >= nearestRightEnemyPionPos)) {
            possibleMoves.add(
                    currentPos + getStringFromPos(rightXMovement, pion.getY())
            );
        }

        return possibleMoves;
    }
    private static ArrayList<String> getVerticalMoves(int[][] board, int playerColor, Pion pion){
        int qtyPionInCol = 0;
        // TOP of the board is 7 and bottom is 0
        int nearestUpEnemyPionPos = Integer.MAX_VALUE;
        int nearestDownEnemyPionPos = Integer.MIN_VALUE;

        for (int y = 0; y < pion.getY(); y++) {
            if(board[pion.getX()][y] == 0) {
                continue;
            }

            if(board[pion.getX()][y] != playerColor) {
                nearestDownEnemyPionPos = y;
            }

            qtyPionInCol ++;
        }

        for (int y = 7; y > pion.getY(); y--) {
            if(board[pion.getX()][y] == 0) {
                continue;
            }

            if(board[pion.getX()][y] != playerColor) {
                nearestUpEnemyPionPos = y;
            }

            qtyPionInCol ++;
        }

        int distance = 1 + qtyPionInCol;
        int upMovement = pion.getY() + distance;
        int downMovement = pion.getY() - distance;

        ArrayList<String> possibleMoves = new ArrayList<>();
        String currentPos = getStringFromPos(pion.getX(), pion.getY());

        if(downMovement >= 0 && (nearestDownEnemyPionPos == Integer.MIN_VALUE || downMovement <= nearestDownEnemyPionPos)) {
            possibleMoves.add(
                    currentPos + getStringFromPos(pion.getX(), downMovement)
            );
        }

        if(upMovement <= 7 && (nearestUpEnemyPionPos == Integer.MAX_VALUE || upMovement >= nearestUpEnemyPionPos)) {
            possibleMoves.add(
                    currentPos + getStringFromPos(pion.getX(), upMovement)
            );
        }

        return possibleMoves;
    }
    private static ArrayList<String> getLeftDiagMoves(int[][] board, int playerColor, Pion pion){
        int qtyPionInDiag = 0;
        // TOP of the board is 7 and bottom is 0
        int nearestTopLeftPionInc = Integer.MAX_VALUE;
        int nearestDownRightPionInc = Integer.MAX_VALUE;

        int increment = 1;

        while(pion.getX() - increment >= 0 && pion.getY() + increment <= 7) {
            if(board[pion.getX() - increment][pion.getY() + increment] == 0) {
                increment++;
                continue;
            }

            if(board[pion.getX() - increment][pion.getY() + increment] != playerColor && increment < nearestTopLeftPionInc) {
                nearestTopLeftPionInc = increment;
            }

            qtyPionInDiag ++;
            increment++;
        }

        increment = 1;
        while(pion.getX() + increment <= 7 && pion.getY() - increment >= 0) {
            if(board[pion.getX() + increment][pion.getY() - increment] == 0) {
                increment++;
                continue;
            }

            if(board[pion.getX() + increment][pion.getY() - increment] != playerColor && increment < nearestDownRightPionInc) {
                nearestDownRightPionInc = increment;
            }

            qtyPionInDiag ++;
            increment++;
        }

        int distance = 1 + qtyPionInDiag;
        int[] TopLeftPos = new int[]{pion.getX() - distance, pion.getY() + distance};
        int[] BottomRightPos = new int[]{pion.getX() + distance, pion.getY() - distance};

        ArrayList<String> possibleMoves = new ArrayList<>();
        String currentPos = getStringFromPos(pion.getX(), pion.getY());

        if(TopLeftPos[0] >= 0 && TopLeftPos[1] <= 7 && (distance <= nearestTopLeftPionInc)) {
            possibleMoves.add(
                    currentPos + getStringFromPos(TopLeftPos[0], TopLeftPos[1])
            );
        }

        if(BottomRightPos[0] <= 7 && BottomRightPos[1] >= 0 && distance <= nearestDownRightPionInc) {
            possibleMoves.add(
                    currentPos + getStringFromPos(BottomRightPos[0], BottomRightPos[1])
            );
        }

        return possibleMoves;
    }
    private static ArrayList<String> getRightDiagMoves(int[][] board, int playerColor, Pion pion){
        int qtyPionInDiag = 0;
        // TOP of the board is 7 and bottom is 0
        int nearestTopRightPionInc = Integer.MAX_VALUE;
        int nearestDownLeftPionInc = Integer.MAX_VALUE;

        int increment = 1;

        while(pion.getX() - increment >= 0 && pion.getY() - increment >= 0) {
            if(board[pion.getX() - increment][pion.getY() - increment] == 0) {
                increment++;
                continue;
            }

            if(board[pion.getX() - increment][pion.getY() - increment] != playerColor && increment < nearestDownLeftPionInc) {
                nearestDownLeftPionInc = increment;
            }

            qtyPionInDiag ++;
            increment++;
        }

        increment = 1;
        while(pion.getX() + increment <= 7 && pion.getY() + increment <= 7) {
            if(board[pion.getX() + increment][pion.getY() + increment] == 0) {
                increment++;
                continue;
            }

            if(board[pion.getX() + increment][pion.getY() + increment] != playerColor && increment < nearestTopRightPionInc) {
                nearestTopRightPionInc = increment;
            }

            qtyPionInDiag ++;
            increment++;
        }

        int distance = 1 + qtyPionInDiag;
        int[] DownLeftPos = new int[]{pion.getX() - distance, pion.getY() - distance};
        int[] TopRightPos = new int[]{pion.getX() + distance, pion.getY() + distance};

        ArrayList<String> possibleMoves = new ArrayList<>();
        String currentPos = getStringFromPos(pion.getX(), pion.getY());

        if(DownLeftPos[0] >= 0 && DownLeftPos[1] >= 0 && distance <= nearestDownLeftPionInc) {
            possibleMoves.add(
                    currentPos + getStringFromPos(DownLeftPos[0], DownLeftPos[1])
            );
        }

        if(TopRightPos[0] <= 7 && TopRightPos[1] <= 7 && distance <= nearestTopRightPionInc) {
            possibleMoves.add(
                    currentPos + getStringFromPos(TopRightPos[0], TopRightPos[1])
            );
        }

        return possibleMoves;
    }

    public static String getStringFromPos(int x, int y) {
        final int VALUE_OF_A_IN_ASCII = 65;

        StringBuilder string = new StringBuilder();
        string.append(Character.toString(VALUE_OF_A_IN_ASCII + x));
        string.append(y + 1);

        return string.toString();
    }

    public static int[] getPosFromString(String pos) {
        // 65 est la valeur de A qui est notre index 0. 65 - 65 donnerait la case 0;
        final int VALUE_OF_A_IN_ASCII = 65;
        final int VALUE_OF_0_IN_ASCII = 48;

        int x = (int) pos.charAt(0) - VALUE_OF_A_IN_ASCII;
        int y = (int) pos.charAt(1) - VALUE_OF_0_IN_ASCII;
        return new int[]{x, y};
    }

    //    public static boolean ValidateMovement(int[][] board, colors color, String initPos, String finalPos) {
    //        System.out.println(color);
    //
    //
    //        return true;
    //    }
}


