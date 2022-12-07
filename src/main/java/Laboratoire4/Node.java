package Laboratoire4;

import java.util.ArrayList;

public class Node {
    private Board board;
    private String move;
    private ArrayList<Node> children;
    public double score;

    public Node(Board board) {
        this.board = board;
//        this.setBoard(board);
        this.children = new ArrayList<>();
    }

    public static Node buildTree(Board board, int player, int depth) {
        if (depth < 0)
            return null;
        Node node = new Node(board);
        ArrayList<String> moves = Movement.generateAllPossibleMoves(board, player);
        int j = 0;
        Board copy;
        for (String move : moves) {
            copy = node.board.clone();
            Movement.executeMove(move, copy);
            node.addChildAt(j++, buildTree(copy, board.changePlayer(player), depth - 1), move);
        }
        return node;
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board.clone();
    }
    public String getMove() {
        return move;
    }
    public void setMove(String move) {
        this.move = move;
    }
    public ArrayList<Node> getChildren() {
        return children;
    }
    public void addChildAt(int index, Node node, String move) {
        if (node != null) {
            node.setMove(move);
            this.children.add(index, node);
        }
    }
}
