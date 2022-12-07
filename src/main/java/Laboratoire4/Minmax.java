package Laboratoire4;

public class Minmax {
    public static String findBestMove(Board board, int depth) {
        Double value = Double.NEGATIVE_INFINITY;
        ScoreNode ab;
        String move = "";
        Node tree = Node.buildTree(board, board.getPlayerColor().getValue(), depth);
        for (Node node : tree.getChildren()) {
            ab = alphabeta(node, depth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
            if (ab.score > value) {
                move = ab.node.getMove();
            }
            value = Math.max(value, ab.score);
            node.score = ab.score;
        }
        if (move.length() == 0)
        {
            int a = 0;
        }
        return move;
    }

    public static ScoreNode alphabeta(Node node, int depth, double alpha, double beta, boolean maximizing) {
        double value;
        if (depth == 0 || node.isLeaf() || Evaluation.isPlayerWinning(node.getBoard())) {
            return new ScoreNode(Evaluation.evaluateBoard(node.getBoard()), node);
        }
        if (maximizing) {
            value = Double.NEGATIVE_INFINITY;
            for (Node child : node.getChildren()) {
                value = Math.max(value, alphabeta(child, depth - 1, alpha, beta, false).score);
                if (value >= beta)
                    break;
                alpha = Math.max(alpha, value);
            }
            node.score = value;
            return new ScoreNode(value, node);
        } else {
            value = Double.POSITIVE_INFINITY;
            for (Node child : node.getChildren()) {
                value = Math.min(value, alphabeta(child, depth - 1, alpha, beta, true).score);
                if (value <= alpha)
                    break;
                beta = Math.min(beta, value);
            }
            node.score = value;
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
