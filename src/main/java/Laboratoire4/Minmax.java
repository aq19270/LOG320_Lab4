package Laboratoire4;

import java.util.ArrayList;
import java.util.Iterator;

public class Minmax {
    public static String alphabeta(Board board, int depth, int playerColor) {
        String bestMove = "";
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double bestScore = alpha;
        double score;
        ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);
        for (String move : possibleMoves) {
            Movement.executeMove(move, board);
            score = minmax(board, depth - 1, false, playerColor, alpha, beta);
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
    public static double minmax(Board board, int depth, Boolean isMaximizing, int playerColor, double alpha, double beta) {
        if (depth == 0) {
            return Movement.evaluateBoard(board, playerColor);
        }
        double score;
        if (isMaximizing) {
            double bestScore = Double.NEGATIVE_INFINITY;
            ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);

            for (String move : possibleMoves) {
                Movement.executeMove(move, board);
                score = minmax(board, depth - 1,false, playerColor, alpha, beta);
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
                Movement.executeMove(move, board);
                score = minmax(board,depth - 1,true, playerColor, alpha, beta);
                worstScore = Math.min(worstScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return worstScore;
        }
    }
}
