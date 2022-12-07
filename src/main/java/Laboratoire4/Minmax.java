package Laboratoire4;

public class Minmax {
    public static String findBestMove(Board board, int depth) {
        ScoreNode ab;
        Node tree = Node.buildTree(board, board.getPlayerColor().getValue(), depth);
        ab = alphabeta(tree, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
        return ab.move;
    }

    public static ScoreNode alphabeta(Node node, int depth, double alpha, double beta, boolean maximizing) {
        double value;
        ScoreNode sn;
        String bestMove = "";
        if (depth == 0 || node.isLeaf() || Evaluation.isPlayerWinning(node.getBoard())) {
            return new ScoreNode(Evaluation.evaluateBoard(node.getBoard()), node, bestMove);
        }
        if (maximizing) {
            value = Double.NEGATIVE_INFINITY;
            for (Node child : node.getChildren()) {
                sn = alphabeta(child, depth - 1, alpha, beta, false);
                if (value < sn.score)
                    bestMove = sn.node.getMove();
                value = Math.max(value, sn.score);
                if (value >= beta)
                    break;
                alpha = Math.max(alpha, value);
            }
            node.score = value;
            return new ScoreNode(value, node, bestMove);
        } else {
            value = Double.POSITIVE_INFINITY;
            for (Node child : node.getChildren()) {
                sn = alphabeta(child, depth - 1, alpha, beta, true);
                if (value > sn.score)
                    bestMove = sn.node.getMove();
                value = Math.min(value, sn.score);
                if (value <= alpha)
                    break;
                beta = Math.min(beta, value);
            }
            node.score = value;
            return new ScoreNode(value, node, bestMove);
        }
    }
    public static class ScoreNode {
        public Double score;
        public Node node;
        public String move;
        public ScoreNode(Double score, Node node, String bestMove) {
            this.score = score;
            this.node = node;
            this.move = bestMove;
        }
    }
}
