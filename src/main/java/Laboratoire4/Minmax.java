package Laboratoire4;

import java.util.ArrayList;
import java.util.Iterator;

public class Minmax {
    public static String alphabeta(int[][] board, int depth, int playerColor) {
        String bestMove = "";
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double bestScore = alpha;
        double score;
        ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);
        for (String move : possibleMoves) {
            score = minmax(executeMove(move, playerColor, board), depth - 1, false, playerColor, alpha, beta);
            bestScore = Math.max(bestScore, score);
            if (score == bestScore) {
                bestMove = move;
            }
            alpha = Math.max(alpha, score);
            if (beta <= alpha) {
                break;
            }
        }
        return bestMove;
    }
    public static double minmax(int[][] board, int depth, Boolean isMaximizing, int playerColor, double alpha, double beta) {
        if (depth == 0) {
            return Movement.evaluateBoard(board, playerColor);
        }
        double score;
        if (isMaximizing) {
            double bestScore = Double.NEGATIVE_INFINITY;
            ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);

            for (String move : possibleMoves) {
                score = minmax(executeMove(move, playerColor, board),depth - 1,false, playerColor, alpha, beta);
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestScore;
        } else {
            int oppositeColor = playerColor == Pion.colors.black.getValue() ? Pion.colors.white.getValue() : Pion.colors.black.getValue();
            double worstScore = Double.POSITIVE_INFINITY;
            ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, oppositeColor);
            for (String move : possibleMoves) {
                score = minmax(executeMove(move, oppositeColor, board),depth - 1,true, playerColor, alpha, beta);
                worstScore = Math.min(worstScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return worstScore;
        }
    }

    public static int[][] executeMove(String move, int color, int[][] board) {
        int[] start = Movement.getPosFromString(move.substring(0, 2));
        int[] end = Movement.getPosFromString(move.substring(2));
        board[start[0]][start[1] - 1] = 0;
        board[end[0]][end[1] - 1] = color;
        return board;
    }
}
