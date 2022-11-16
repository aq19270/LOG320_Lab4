import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    int[][] board = new int[8][8];

    @Before
    public void init() {
        board = new int[][]{
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
    }

    @Test
    public void testPrintBoard(){
        Client.printBoardInConsole(board);
        Assert.assertTrue(true);
    }
}
