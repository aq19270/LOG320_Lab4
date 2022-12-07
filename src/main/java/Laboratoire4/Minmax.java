package Laboratoire4;

import java.util.AbstractMap;
import java.util.ArrayList;

public class Minmax {
    public static String alphabeta(Board board, int depth, int playerColor) {
        Node root = Node.buildTree(board, board.getPlayerColor().getValue(), 3);
        ScoreNode value = realalphabeta(root, 3, Double.MIN_VALUE, Double.MAX_VALUE, true);
//        root.getNodeData().setBoard(board);
//        ArrayList<String> moves = Movement.generateAllPossibleMoves(board, board.getPlayerColor().getValue());
//        for (String move : moves) {
//            Movement.executeMove(move, board.clone());
//            Node.generateChildren(root, depth);
//        }
        System.out.println(value.node.getNodeData().getMove());
        return value.node.getNodeData().getMove();
//        String bestMove = "";
//        double alpha = Double.NEGATIVE_INFINITY;
//        double beta = Double.POSITIVE_INFINITY;
//        double bestScore = alpha;
//        double score;
//        ArrayList<String> possibleMoves = Movement.generateAllPossibleMoves(board, playerColor);
//        for (String move : possibleMoves) {
//            Movement.executeMove(move, board);
//            score = minmax(board, depth - 1, false, playerColor, alpha, beta);
//            bestScore = Math.max(bestScore, score);
//            if (score == bestScore) {
//                bestMove = move;
//            }
//            alpha = Math.max(alpha, score);
//            if (beta <= alpha) {
//                break;
//            }
//        }
//        return bestMove;
    }

    public static ScoreNode realalphabeta(Node node, int depth, double alpha, double beta, boolean maximizing) {
        double value;
        if (depth == 0 || node.isLeaf()) {
            return new ScoreNode(Evaluation.evaluateBoard(node.getNodeData().getBoard()), node);
        }
        if (maximizing) {
            value = Double.MIN_VALUE;
            for (Node child : node.getChildren()) {
                value = Math.max(value, realalphabeta(child, depth - 1, alpha, beta, false).score);
                if (value >= beta)
                    break;
                alpha = Math.max(alpha, value);
            }
            return new ScoreNode(value, node);
        } else {
            value = Double.MAX_VALUE;
            for (Node child : node.getChildren()) {
                value = Math.min(value, realalphabeta(child, depth - 1, alpha, beta, true).score);
                if (value <= alpha)
                    break;
                beta = Math.min(beta, value);
            }
            return new ScoreNode(value, node);
        }
    }

    public static double minmax(Board board, int depth, Boolean isMaximizing, int playerColor, double alpha, double beta) {
        if (depth == 0) {
            return Evaluation.evaluateBoard(board);
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
    public static class ScoreNode {
        public Double score;
        public Node node;
        public ScoreNode(Double score, Node node) {
            this.score = score;
            this.node = node;
        }
    }
}
