package Laboratoire4;

import java.util.ArrayList;
import java.util.List;

public class Node<NodeData> {
    private NodeData data;
    private Node<NodeData> parent;
    private List<Node<NodeData>> children;

    public Node<NodeData> generateTree(ArrayList<String> moves, int depth, Board board) {
        Node<NodeData> root;
    }




    private class NodeData {
        private int color;
        private Board board;
    }
}


