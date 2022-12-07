package Laboratoire4;

import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private NodeData data;
    private Node parent;
    private ArrayList<Node> children;
    private int nbOfChildren;

    public Node() {
        this.data = new NodeData();
        this.children = new ArrayList<>();
        this.nbOfChildren = 0;
    }

    public Node(Board board, int nbOfChildren) {
        this.data = new NodeData();
        this.data.setBoard(board);
        this.children = new ArrayList<>();
        this.nbOfChildren = nbOfChildren;
    }

    public static Node buildTree(Board board, int player, int depth) {
        ArrayList<String> moves = Movement.generateAllPossibleMoves(board, player);
        Node node = new Node(board, moves.size());
        if (depth == 0)
            return node;
        int j = 0;
        for (int i = 0; i < moves.size(); i++) {
            Movement.executeMove(moves.get(i), node.data.board);
            node.getNodeData().setMove(moves.get(i));
            node.addChildAt(j++, buildTree(node.data.board, board.changePlayer(player), depth - 1));
        }
        return node;
    }

    public Node generateTree(Board board, int depth) {
        Node root = new Node();
        root.data.setBoard(board);
        generateChildren(root, depth);
        return root;
    }

    public static void generateChildren(Node node, int depth) {
        Board board = node.data.board;
        ArrayList<String> moves = Movement.generateAllPossibleMoves(board, board.getPlayerColor().getValue());
        for (String move : moves) {
            Movement.executeMove(move, board);
            Node child = new Node();
            child.data.setBoard(board);
            child.setParent(node);
            node.addChild(child);
            generateChildren(child, depth);
        }
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public NodeData getNodeData() {
        return data;
    }

    public void setNodeData(NodeData data) {
        this.data = data;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChidren(ArrayList<Node> children) {
        this.children = children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
    public void addChildAt(int index, Node node) {
        this.children.add(index, node);
    }

    public static class NodeData {
        private Board board;
        private double evaluation;
        private String move;
        private Double score;
        public Board getBoard() {
            return board;
        }
        public void setBoard(Board board) {
            this.board = board.clone();
        }
        public double getEvaluation() {
            return evaluation;
        }
        public void setEvaluation(double evaluation) {
            this.evaluation = evaluation;
        }
        public String getMove() {
            return move;
        }
        public void setMove(String move) {
            this.move = move;
        }
        public Double getScore() {
            return score;
        }
        public void setScore(Double score) {
            this.score = score;
        }
    }
}


