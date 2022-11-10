import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    int[][] board = new int[8][8];

    @Before
    public void init() {
        board = new int[][]{
                { // X1
                    1,2,3,4,5,6,7,8
                },
                { //X2
                    1,2,3,4,5,6,7,8
                },
                { //X3
                    1,2,3,4,5,6,7,8
                },
                { //X3
                    1,2,3,4,5,6,7,8
                },
                { //X4
                    1,2,3,4,5,6,7,8
                },
                { //X5
                    1,2,3,4,5,6,7,8
                },
                { //X6
                    1,2,3,4,5,6,7,8
                },
                {  //X7
                    1,2,3,4,5,6,7,8
                },
                { //X8
                    1,2,3,4,5,6,7,8
                }
        };
    }

    @Test
    public void testPrintBoard(){
        Client.printBoardInConsole(board);
        Assert.assertTrue(true);
    }
}
