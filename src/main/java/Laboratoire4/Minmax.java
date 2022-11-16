package Laboratoire4;

import Laboratoire4.Movement;

import java.util.ArrayList;
import java.util.Iterator;

public class Minmax {
    public static int minmax(int[][] board, int depth, Boolean isMaximizing, int playerColor) {
        // if (gameOver) {
        // return +/- infini;
        // }
        if (depth == 0) {
            return Movement.evaluateBoard(board, playerColor);
        }
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // String[] possibleMove = availableMove(board);
            ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);
            Iterator<String> iterator = possibleMoves.iterator();

            // for (int i=0;i<possibleMove.length;i++){
            while (iterator.hasNext()) {
                //TODO Faire le move i sur le board

                // score = minmax(board,depth-1,false)
                int score = minmax(board,depth-1,false, playerColor);

                //TODO Undo le move i sur le board

                bestScore = Math.max(bestScore, score);
            }
            return bestScore;
        }

        int worstScore = Integer.MAX_VALUE;
        // String[] possibleMove = availableMove(board);
        ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);
        Iterator<String> iterator = possibleMoves.iterator();

        // for (int i=0;i<possibleMove.length;i++){
        while (iterator.hasNext()) {
            //TODO Faire le move i sur le board

            // score = minmax(board,depth-1,true)
            int score = minmax(board,depth-1,false, playerColor);

            //TODO Undo le move i sur le board

            worstScore = Math.min(worstScore, score);
        }
        return worstScore;
    }
}
