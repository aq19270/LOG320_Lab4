package Laboratoire4;

import Laboratoire4.Board;
import Laboratoire4.Client;
import Laboratoire4.Pion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    Board board;

    @Before
    public void init() {
        board = new Board(8, 8);

        int [][] tempBoard = {
                { // X1
                        0,2,2,2,2,2,2,0
                },
                { //X2
                        4,0,0,0,0,0,0,4
                },
                { //X3
                        4,0,0,0,0,0,0,4
                },
                { //X3
                        4,0,0,0,0,0,0,4
                },
                { //X4
                        4,0,0,0,0,0,0,4
                },
                { //X5
                        4,0,0,0,0,0,0,4
                },
                { //X6
                        4,0,0,0,0,0,0,4
                },
                {  //X7
                        4,0,0,0,0,0,0,4
                },
                { //X8
                        0,2,2,2,2,2,2,0
                }
        };

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if(tempBoard[x][y] == 0) {
                    continue;
                }

                board.getCase(x, y).setPion(new Pion(x, y, (tempBoard[x][y] == 4) ? Pion.colors.white : Pion.colors.black ));
            }
        }
    }

    @Test
    public void testPrintBoard(){
        Client.printBoardInConsole(board);
        Assert.assertTrue(true);
    }
}
