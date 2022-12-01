package Laboratoire4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MovementTest {
    Board board;

    @Before
    public void init() {
        board = new Board(8, 8);
        board.setPlayerColor(Pion.colors.white);
        board.setEnnemyColor(Pion.colors.black);

        int [][] tempBoard = {
                { // X1
                        0,4,4,4,4,4,4,0
                },
                { //X2
                        2,0,0,0,0,0,0,2
                },
                { //X3
                        2,0,0,0,0,0,0,2
                },
                { //X4
                        2,0,0,0,0,0,0,2
                },
                { //X5
                        2,0,0,0,0,0,0,2
                },
                { //X6
                        2,0,0,0,0,0,0,2
                },
                {  //X7
                        2,0,0,0,0,0,0,2
                },
                { //X8
                        0,4,4,4,4,4,4,0
                }
        };

        board.getPionsBlanc().clear();
        board.getPionsNoir().clear();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if(tempBoard[x][y] == 0) {
                    board.getCase(x, y).emptyCase();
                    continue;
                }

                if(tempBoard[x][y] == 4) {
                    Pion pion = new Pion(x, y, Pion.colors.white);
                    board.addPionBlanc(pion);
                    board.getCase(x, y).setPion(pion);
                    continue;
                }

                Pion pion = new Pion(x, y, Pion.colors.black);
                board.addPionNoir(pion);
                board.getCase(x, y).setPion(pion);
            }
        }
    }

    @Test
    public void generateAllPossibleMoves() {
        updateBoard(board);

        ArrayList<String> allPossibleMoves = Movement.generateAllPossibleMoves(board, Pion.colors.white.getValue());
        Assert.assertArrayEquals(new String[]{
                "A2B2",
                "A2A6",
                "A2B3",
                "A3A7",
                "A3C1",
                "A3E7",
                "A4C4",
                "A4A8",
                "A4B3",
                "A4C6",
                "A5A1",
                "A5C3",
                "A5B6",
                "C5C2",
                "D4B4",
                "D4F4",
                "D4D7",
                "D4B2",
                "D6G6",
                "D6D3",
                "F5F2",
                "F5F8",
                "F5D7",
                "G1G2",
                "G1H2",
                "H3H7",
                "H3F1",
                "H5H1",
                "H5G6",
                "H5G4",
                "H6E6",
                "H6H2",
                "H6F8",
                "H6E3",
        }, allPossibleMoves.toArray());
    }

    @Test
    public void getStringFromPos() {
        Assert.assertEquals("D5", Movement.getStringFromPos(3,4));
    }

    @Test
    public void getPosFromString() {
        Assert.assertArrayEquals(new int[]{3,3}, Movement.getPosFromString("D4"));
    }

    @Test
    public void TestValidMovementsWhite(){
    }

    @Test
    public void TestInvalidMovementsWhite(){

    }

    @Test
    public void TestValidMovementsBlack(){

    }

    @Test
    public void TestInvalidMovementsBlack(){

    }


    private void updateBoard(Board board) {
        int [][] tempBoard = {
                { // X1
                        0,4,4,4,4,0,0,0
                },
                { //X2
                        2,0,0,0,0,0,0,2
                },
                { //X3
                        2,0,0,0,4,2,0,0
                },
                { //X4
                        0,0,2,4,0,4,0,0
                },
                { //X5
                        2,0,2,0,2,0,0,0
                },
                { //X6
                        2,0,0,0,4,0,0,2
                },
                {  //X7
                        4,0,0,0,0,0,0,0
                },
                { //X8
                        0,0,4,0,4,4,2,0
                }
        };

        board.getPionsBlanc().clear();
        board.getPionsNoir().clear();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if(tempBoard[x][y] == 0) {
                    board.getCase(x, y).emptyCase();
                    continue;
                }

                if(tempBoard[x][y] == 4) {
                    Pion pion = new Pion(x, y, Pion.colors.white);
                    board.addPionBlanc(pion);
                    board.getCase(x, y).setPion(pion);
                    continue;
                }

                Pion pion = new Pion(x, y, Pion.colors.black);
                board.addPionNoir(pion);
                board.getCase(x, y).setPion(pion);
            }
        }
    }
}