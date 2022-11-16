package Laboratoire4;

public class Movement {

    // https://dke.maastrichtuniversity.nl/m.winands/documents/informed_search.pdf P.22
    final static int[][] weightMatrix = {
            {-80, -25, -20, -20, -20, -20, -25, -80},
            {-25, 10, 10, 10, 10, 10, 10, -25},
            {-20, 10, 25, 25, 25, 25, 10, -20},
            {-20, 10, 25, 50, 50, 25, 10, -20},
            {-20, 10, 25, 50, 50, 25, 10, -20},
            {-20, 10, 25, 25, 25, 25, 10, -20},
            {-25, 10, 10, 10, 10, 10, 10, -25},
            {-80, -25, -20, -20, -20, -20, -25, -80},
    };
    public enum colors {
        black(2),
        white(4);

        private int value;
        private colors(int val) {
            value = val;
        }
    }

    public static boolean ValidateMovement(int[][] board, colors color, String initPos, String finalPos) {
        System.out.println(color);


        return true;
    }
}


