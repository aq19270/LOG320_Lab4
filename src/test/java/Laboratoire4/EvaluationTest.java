package Laboratoire4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EvaluationTest {
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
    public void evaluateCentralisation() {
        Assert.assertEquals(0, Evaluation.evaluateCentralisation(board), 0.001);

        updateBoard(board);

        Assert.assertEquals(-0.0375, Evaluation.evaluateCentralisation(board), 0.001);
    }

    @Test
    public void evaluateMobility() {
        Assert.assertEquals(0, Evaluation.evaluateMobility(board), 0.001);

        updateBoard(board);

        Assert.assertEquals(0.029411764705882353, Evaluation.evaluateMobility(board), 0.001);
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