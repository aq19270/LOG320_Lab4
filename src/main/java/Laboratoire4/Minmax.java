package Laboratoire4;

import java.util.ArrayList;

public class Minmax {
    public static String findBestMove(Board board, int depth) {
        Node root = Node.buildTree(board, board.getPlayerColor().getValue(), depth);
        ScoreNode value = alphabeta(root, depth, Double.MIN_VALUE, Double.MAX_VALUE, true);
        System.out.println(value.node.getMove());
        return value.node.getMove();
    }

    public static ScoreNode alphabeta(Node node, int depth, double alpha, double beta, boolean maximizing) {
        double value;
        if (depth == 0 || node.isLeaf()) {
            return new ScoreNode(Evaluation.evaluateBoard(node.getBoard()), node);
        }
        if (maximizing) {
            value = Double.MIN_VALUE;
            for (Node child : node.getChildren()) {
                value = Math.max(value, alphabeta(child, depth - 1, alpha, beta, false).score);
                if (value >= beta)
                    break;
                alpha = Math.max(alpha, value);
            }
            return new ScoreNode(value, node);
        } else {
            value = Double.MAX_VALUE;
            for (Node child : node.getChildren()) {
                value = Math.min(value, alphabeta(child, depth - 1, alpha, beta, true).score);
                if (value <= alpha)
                    break;
                beta = Math.min(beta, value);
            }
            return new ScoreNode(value, node);
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
