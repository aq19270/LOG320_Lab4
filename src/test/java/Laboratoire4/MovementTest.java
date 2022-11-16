package Laboratoire4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class MovementTest {
    int[][] board;

    @Before
    public void init() {
        board = new int[][]{
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
    }

    @Test
    public void evaluateBoard() {
        Assert.assertEquals(0, Movement.evaluateBoard(board, Pion.colors.white.getValue()));

        board = new int[][]{
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

        Assert.assertEquals(-15, Movement.evaluateBoard(board, Pion.colors.white.getValue()));
    }

    @Test
    public void generateAllPossibleMoves() {
        board = new int[][]{
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

        ArrayList<String> allPossibleMoves = Movement.generateAllPossibleMoves(board, Pion.colors.white.getValue());
        Iterator<String> iterator = allPossibleMoves.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        Assert.assertTrue(true);
    }

    @Test
    public void getStringFromPos() {
        Assert.assertEquals("D5", Movement.getStringFromPos(3,4));
    }

    @Test
    public void getPosFromString() {
        Assert.assertArrayEquals(new int[]{3,4}, Movement.getPosFromString("D4"));
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
}
