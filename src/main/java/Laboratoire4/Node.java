package Laboratoire4;

import java.util.ArrayList;

public class Node {
    private Board board;
    private String move;
    private ArrayList<Node> children;

    public Node() {
        this.children = new ArrayList<>();
    }

    public Node(Board board, int nbOfChildren) {
        this.setBoard(board);
        this.children = new ArrayList<>();
    }

    public static Node buildTree(Board board, int player, int depth) {
        ArrayList<String> moves = Movement.generateAllPossibleMoves(board, player);
        Node node = new Node(board, moves.size());
        if (depth == 0)
            return node;
        int j = 0;
        for (String move : moves) {
            Movement.executeMove(move, node.board);
            node.setMove(move);
            node.addChildAt(j++, buildTree(node.board, board.changePlayer(player), depth - 1));
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
    public void addChildAt(int index, Node node) {
        this.children.add(index, node);
    }
}
