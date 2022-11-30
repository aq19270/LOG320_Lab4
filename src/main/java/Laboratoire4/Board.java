package Laboratoire4;

import java.util.ArrayList;

public class Board {
    private Case[][] board;
    private ArrayList<Pion> pionsBlanc = new ArrayList<>();
    private ArrayList<Pion> pionsNoir = new ArrayList<>();


    public Board(int sizeX, int sizeY) {
        board = new Case[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                board[x][y] = new Case(x, y, null);
            }
        }
    }

    public Case getCase(int x, int y) {
        if(!inBound(x, 0, board.length) || !inBound(y, 0, board[0].length)) {
            throw new ArrayIndexOutOfBoundsException("The requested index is out of range of this board: " + x + "-" + y);
        }

        return board[x][y];
    }

    public void addPionBlanc(Pion pion) {
        this.pionsBlanc.add(pion);
    }

    public void addPionNoir(Pion pion) {
        this.pionsNoir.add(pion);
    }

    public void removePionBlanc(Pion pion) {
        this.pionsBlanc.remove(pion);
    }

    public void removePionNoir(Pion pion) {
        this.pionsNoir.remove(pion);
    }

    public ArrayList<Pion> getPionsBlanc() {
        return this.pionsBlanc;
    }

    public ArrayList<Pion> getPionsNoir() {
        return this.pionsNoir;
    }

    private boolean inBound(int val, int min, int max) {
        return (val >= min && val <= max);
    }

    public Board clone() {
        Board newBoard = new Board(this.board.length, this.board[0].length);

        for (int x = 0; x < this.board.length; x++) {
            for (int y = 0; y < this.board[0].length; y++) {
                newBoard.board[x][y] = this.board[x][y].clone();
                if (newBoard.board[x][y].isEmpty()) {
                    continue;
                }

                if (newBoard.board[x][y].getPion().getColor() == Pion.colors.white) {
                    newBoard.pionsBlanc.add(newBoard.board[x][y].getPion());
                    continue;
                }

                newBoard.getPionsNoir().add(newBoard.board[x][y].getPion());
            }
        }

        return newBoard;
    }
}