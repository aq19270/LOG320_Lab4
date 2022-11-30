package Laboratoire4;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Movement {

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

    public static ArrayList<String> generateAllPossibleMoves(Board board, int playerColor) {
        ArrayList<String> possibleMoves = new ArrayList<>();

        ArrayList<Pion> pions = (playerColor == Pion.colors.white.getValue()) ? board.getPionsBlanc() : board.getPionsNoir();

        pions.forEach(pion -> {
            possibleMoves.addAll(generateAllMoveForPion(board, playerColor, pion));
        });

        return possibleMoves;
    }

    private static ArrayList<String> generateAllMoveForPion(Board board, int playerColor, Pion pion) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(getHorizontalMoves(board, playerColor, pion));
        possibleMoves.addAll(getVerticalMoves(board, playerColor, pion));
        possibleMoves.addAll(getLeftDiagMoves(board, playerColor, pion));
        possibleMoves.addAll(getRightDiagMoves(board, playerColor, pion));
        return possibleMoves;
    }

    private static ArrayList<String> getHorizontalMoves(Board board, int playerColor, Pion pion){
        int qtyPionInRow = 0;
        int nearestLeftEnemyPionPos = Integer.MIN_VALUE;
        int nearestRightEnemyPionPos = Integer.MAX_VALUE;

        for (int x = 0; x < pion.getX(); x++) {
            if(board.getCase(x, pion.getY()).isEmpty()) {
                continue;
            }

            if(board.getCase(x, pion.getY()).getPion().getColorValue() != playerColor) {
                nearestLeftEnemyPionPos = x;
            }

            qtyPionInRow ++;
        }

        for (int x = 7; x > pion.getX(); x--) {
            if(board.getCase(x, pion.getY()).isEmpty()) {
                continue;
            }

            if(board.getCase(x, pion.getY()).getPion().getColorValue() != playerColor) {
                nearestRightEnemyPionPos = x;
            }

            qtyPionInRow ++;
        }

        int distance = 1 + qtyPionInRow;
        int leftXMovement = pion.getX() - distance;
        int rightXMovement = pion.getX() + distance;

        ArrayList<String> possibleMoves = new ArrayList<>();
        String currentPos = getStringFromPos(pion.getX(), pion.getY());

        if(
                leftXMovement > 0
                    && (nearestLeftEnemyPionPos == Integer.MIN_VALUE || leftXMovement >= nearestLeftEnemyPionPos)
                    && (board.getCase(leftXMovement,pion.getY()).isEmpty() || board.getCase(leftXMovement,pion.getY()).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(leftXMovement, pion.getY())
            );
        }

        if(
                rightXMovement < 8
                    && (nearestRightEnemyPionPos == Integer.MAX_VALUE || rightXMovement <= nearestRightEnemyPionPos)
                    && (board.getCase(rightXMovement, pion.getY()).isEmpty() || board.getCase(rightXMovement, pion.getY()).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(rightXMovement, pion.getY())
            );
        }

        return possibleMoves;
    }
    private static ArrayList<String> getVerticalMoves(Board board, int playerColor, Pion pion){
        int qtyPionInCol = 0;
        // TOP of the board is 7 and bottom is 0
        int nearestUpEnemyPionPos = Integer.MAX_VALUE;
        int nearestDownEnemyPionPos = Integer.MIN_VALUE;

        for (int y = 0; y < pion.getY(); y++) {
            if(board.getCase(pion.getX(), y).isEmpty()) {
                continue;
            }

            if(board.getCase(pion.getX(), y).getPion().getColorValue() != playerColor) {
                nearestDownEnemyPionPos = y;
            }

            qtyPionInCol ++;
        }

        for (int y = 7; y > pion.getY(); y--) {
            if(board.getCase(pion.getX(), y).isEmpty()) {
                continue;
            }

            if(board.getCase(pion.getX(), y).getPion().getColorValue() != playerColor) {
                nearestUpEnemyPionPos = y;
            }

            qtyPionInCol ++;
        }

        int distance = 1 + qtyPionInCol;
        int upMovement = pion.getY() + distance;
        int downMovement = pion.getY() - distance;

        ArrayList<String> possibleMoves = new ArrayList<>();
        String currentPos = getStringFromPos(pion.getX(), pion.getY());

        if(
                downMovement >= 0
                    && (nearestDownEnemyPionPos == Integer.MIN_VALUE || downMovement >= nearestDownEnemyPionPos)
                    && (board.getCase(pion.getX(), downMovement).isEmpty() || board.getCase(pion.getX(), downMovement).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(pion.getX(), downMovement)
            );
        }

        if(
                upMovement <= 7
                    && (nearestUpEnemyPionPos == Integer.MAX_VALUE || upMovement <= nearestUpEnemyPionPos)
                    && (board.getCase(pion.getX(), upMovement).isEmpty() || board.getCase(pion.getX(), upMovement).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(pion.getX(), upMovement)
            );
        }

        return possibleMoves;
    }
    private static ArrayList<String> getLeftDiagMoves(Board board, int playerColor, Pion pion){
        int qtyPionInDiag = 0;
        // TOP of the board is 7 and bottom is 0
        int nearestTopLeftPionInc = Integer.MAX_VALUE;
        int nearestDownRightPionInc = Integer.MAX_VALUE;

        int increment = 1;

        while(pion.getX() - increment >= 0 && pion.getY() + increment <= 7) {
            if(board.getCase(pion.getX() - increment, pion.getY() + increment).isEmpty()) {
                increment++;
                continue;
            }

            if(
                    board.getCase(pion.getX() - increment, pion.getY() + increment).getPion().getColorValue() != playerColor
                    && increment < nearestTopLeftPionInc
            ) {
                nearestTopLeftPionInc = increment;
            }

            qtyPionInDiag ++;
            increment++;
        }

        increment = 1;
        while(pion.getX() + increment <= 7 && pion.getY() - increment >= 0) {
            if(board.getCase(pion.getX() + increment, pion.getY() - increment).isEmpty()) {
                increment++;
                continue;
            }

            if(
                    board.getCase(pion.getX() + increment, pion.getY() - increment).getPion().getColorValue() != playerColor
                            && increment < nearestDownRightPionInc
            ) {
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

        if(
                TopLeftPos[0] >= 0
                        && TopLeftPos[1] <= 7 && (distance <= nearestTopLeftPionInc)
                        && (board.getCase(TopLeftPos[0], TopLeftPos[1]).isEmpty() || board.getCase(TopLeftPos[0], TopLeftPos[1]).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(TopLeftPos[0], TopLeftPos[1])
            );
        }

        if(
                BottomRightPos[0] <= 7
                        && BottomRightPos[1] >= 0
                        && distance <= nearestDownRightPionInc
                        && (board.getCase(BottomRightPos[0], BottomRightPos[1]).isEmpty() || board.getCase(BottomRightPos[0], BottomRightPos[1]).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(BottomRightPos[0], BottomRightPos[1])
            );
        }

        return possibleMoves;
    }
    private static ArrayList<String> getRightDiagMoves(Board board, int playerColor, Pion pion){
        int qtyPionInDiag = 0;
        // TOP of the board is 7 and bottom is 0
        int nearestTopRightPionInc = Integer.MAX_VALUE;
        int nearestDownLeftPionInc = Integer.MAX_VALUE;

        int increment = 1;

        while(pion.getX() - increment >= 0 && pion.getY() - increment >= 0) {
            if(board.getCase(pion.getX() - increment, pion.getY() - increment).isEmpty()) {
                increment++;
                continue;
            }

            if(
                    board.getCase(pion.getX() - increment, pion.getY() - increment).getPion().getColorValue() != playerColor
                    && increment < nearestDownLeftPionInc
            ) {
                nearestDownLeftPionInc = increment;
            }

            qtyPionInDiag ++;
            increment++;
        }

        increment = 1;
        while(pion.getX() + increment <= 7 && pion.getY() + increment <= 7) {
            if(board.getCase(pion.getX() + increment, pion.getY() + increment).isEmpty()) {
                increment++;
                continue;
            }

            if(
                    board.getCase(pion.getX() + increment, pion.getY() + increment).getPion().getColorValue() != playerColor
                    && increment < nearestTopRightPionInc
            ) {
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

        if(
                DownLeftPos[0] >= 0
                        && DownLeftPos[1] >= 0
                        && distance <= nearestDownLeftPionInc
                        && (board.getCase(DownLeftPos[0], DownLeftPos[1]).isEmpty() || board.getCase(DownLeftPos[0], DownLeftPos[1]).getPion().getColorValue() != playerColor)
        ) {
            possibleMoves.add(
                    currentPos + getStringFromPos(DownLeftPos[0], DownLeftPos[1])
            );
        }

        if(
                TopRightPos[0] <= 7
                        && TopRightPos[1] <= 7
                        && distance <= nearestTopRightPionInc
                        && (board.getCase(TopRightPos[0], TopRightPos[1]).isEmpty() || board.getCase(TopRightPos[0], TopRightPos[1]).getPion().getColorValue() != playerColor)
        ) {
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

    public static void executeMove(String move, Board board) {
        int[] start = Movement.getPosFromString(move.substring(0, 2));
        int[] end = Movement.getPosFromString(move.substring(2, 4));

        Case oldCase = board.getCase(start[0], start[1] - 1);
        if(oldCase.isEmpty()) {
           return;
        }

        Pion pion = oldCase.getPion();
        board.getCase(end[0], end[1] - 1).setPion(pion);
        oldCase.emptyCase();
    }

    //    public static boolean ValidateMovement(int[][] board, colors color, String initPos, String finalPos) {
    //        System.out.println(color);
    //
    //
    //        return true;
    //    }
}


