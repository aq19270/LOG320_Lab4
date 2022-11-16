package Laboratoire4;

public class Minmax {
    public static double minmax(int[][] board, int depth, Boolean isMaximizing) {
        // if (gameOver) {
        // return +/- infini;
        // }
        if (depth == 0) {
            return 0;
            // return evaluation(board)
        }
        if (isMaximizing) {
            double bestScore = Double.MIN_VALUE;
            // String[] possibleMove = availableMove(board);
            // for (int i=0;i<possibleMove.length;i++){
            // Faire le move i sur le board
            // score = minmax(board,depth-1,false)
            // Undo le move i sur le board
            // bestScore = max(score,bestScore)
            // }
            return bestScore;
        } else {
            double bestScore = Double.MAX_VALUE;
            // String[] possibleMove = availableMove(board);
            // for (int i=0;i<possibleMove.length;i++){
            // Faire le move i sur le board
            // score = minmax(board,depth-1,true)
            // Undo le move i sur le board
            // bestScore = min(score,bestScore)
            // }
            return bestScore;
        }
    }
}
