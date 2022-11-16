import java.io.*;
import java.net.*;
import java.util.Arrays;

import Laboratoire4.Movement;
import Laboratoire4.Pion;
import Laboratoire4.Minmax;

class Client {
    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        try {
            MyClient = new Socket("localhost", 8888);

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            int[][] board = new int[8][8];
            // ArrayList<Pion> pionsBlanc = new ArrayList<>();
            // ArrayList<Pion> pionsNoir = new ArrayList<>();
            Pion.colors playerColor = Pion.colors.none;

            while (true) {
                char cmd = 0;

                cmd = (char) input.read();
                System.out.println(cmd);

                switch (cmd) {
                    // Debut de la partie en joueur blanc
                    case '1' -> {
                        // pionsBlanc = getInitialWhitePion();
                        // pionsNoir = getInitialBlackPion();
                        playerColor = Pion.colors.white;
                        board = handleNewGameWhite(input, output, console, board, playerColor.getValue());
                    }

                    // Debut de la partie en joueur Noir
                    case '2' -> {
                        // pionsBlanc = getInitialWhitePion();
                        // pionsNoir = getInitialBlackPion();
                        playerColor = Pion.colors.black;
                        board = handleNewGameBlack(input, output, console, board);
                    }

                    // Le serveur demande le prochain coup
                    // Le message contient aussi le dernier coup joue.
                    case '3' -> {
                        String move = handleServerRequestUpdate(input, output, console, board, playerColor.getValue());
                        // int[][] board2 = board.clone();
                        // String move = Minmax.alphabeta(board2, 4, playerColor.getValue());
                        // Ici on assume que notre coup sera toujours valide puisque la fonction a été
                        // valider
                        int[] initPos = Movement.getPosFromString(move.substring(0, 2));
                        int startIndex = 2;
                        if (move.length() == 5) {
                            startIndex = 3;
                        }
                        int[] nextPos = Movement.getPosFromString(move.substring(startIndex));

                        board[initPos[0]][initPos[1] - 1] = 0;
                        board[nextPos[0]][nextPos[1] - 1] = playerColor.getValue();
                    }

                    // Le dernier coup est invalide
                    case '4' -> handleLastMoveInvalid(input, output, console);

                    // La partie est terminée
                    case '5' -> handleGameOver(input, output, console);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private static int[][] handleNewGameWhite(BufferedInputStream input, BufferedOutputStream output,
            BufferedReader console, int[][] board, int playerColor) throws IOException {
        byte[] aBuffer = new byte[1024];

        int size = input.available();
        // System.out.println("size " + size);
        input.read(aBuffer, 0, size);
        String s = new String(aBuffer).trim();
        System.out.println(s);
        String[] boardValues;
        boardValues = s.split(" ");
        int x = 0, y = 0;
        for (int i = 0; i < boardValues.length; i++) {
            Integer value = Integer.parseInt(boardValues[i]);
            board[x][y] = value;
            x++;
            if (x == 8) {
                x = 0;
                y++;
            }
        }
        printBoardInConsole(board);
        int[][] boardCopy = new int[board.length][];
        for (int i = 0; i < boardCopy.length; i++) {
            boardCopy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
        String move = Minmax.alphabeta(boardCopy, 4, playerColor);
        // move = console.readLine();
        String m = move.substring(0, 2) + " " + move.substring(2);
        int[] start, end;
        start = Movement.getPosFromString(move.substring(0, 2));
        end = Movement.getPosFromString(move.substring(2));
        board[start[0]][start[1] - 1] = 0;
        board[end[0]][end[1] - 1] = playerColor;
        // output.write(move.getBytes(), 0, move.length());
        output.write(m.getBytes(), 0, m.length());
        output.flush();
        return board;
    }

    private static int[][] handleNewGameBlack(BufferedInputStream input, BufferedOutputStream output,
            BufferedReader console, int[][] board) throws IOException {
        System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
        byte[] aBuffer = new byte[1024];

        int size = input.available();
        // System.out.println("size " + size);
        input.read(aBuffer, 0, size);
        String s = new String(aBuffer).trim();
        System.out.println(s);
        String[] boardValues;
        boardValues = s.split(" ");
        int x = 0, y = 0;
        for (int i = 0; i < boardValues.length; i++) {
            board[x][y] = Integer.parseInt(boardValues[i]);
            x++;
            if (x == 8) {
                x = 0;
                y++;
            }
        }

        printBoardInConsole(board);
        return board;
    }

    private static String handleServerRequestUpdate(BufferedInputStream input, BufferedOutputStream output,
            BufferedReader console, int[][] board, int playerColor) throws IOException {
        byte[] aBuffer = new byte[16];

        int size = input.available();
        System.out.println("size :" + size);
        input.read(aBuffer, 0, size);

        String s = new String(aBuffer);
        // update board
        s = s.trim().replace(" ", "").replace("-", "");
        int[] start, end;
        start = Movement.getPosFromString(s.substring(0, 2));
        end = Movement.getPosFromString(s.substring(2));
        board[start[0]][start[1] - 1] = 0;
        board[end[0]][end[1] - 1] = playerColor == Pion.colors.black.getValue() ? Pion.colors.white.getValue()
                : Pion.colors.black.getValue();
        System.out.println("Dernier coup :" + s);
        System.out.println("Entrez votre coup : ");
        int[][] boardCopy = new int[board.length][];
        for (int i = 0; i < boardCopy.length; i++) {
            boardCopy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        String move = Minmax.alphabeta(boardCopy, 10, playerColor);
        // move = console.readLine();
        String m = move.substring(0, 2) + " " + move.substring(2);
        // output.write(move.getBytes(), 0, move.length());
        output.write(m.getBytes(), 0, m.length());
        output.flush();
        return m;
        // return move;
    }

    private static void handleLastMoveInvalid(BufferedInputStream input, BufferedOutputStream output,
            BufferedReader console) throws IOException {
        System.out.println("Coup invalide, entrez un nouveau coup : ");
        String move = null;
        move = console.readLine();
        output.write(move.getBytes(), 0, move.length());
        output.flush();
    }

    private static void handleGameOver(BufferedInputStream input, BufferedOutputStream output, BufferedReader console)
            throws IOException {
        byte[] aBuffer = new byte[16];
        int size = input.available();
        input.read(aBuffer, 0, size);
        String s = new String(aBuffer);
        System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
        String move = null;
        move = console.readLine();
        output.write(move.getBytes(), 0, move.length());
        output.flush();
    }

    public static void printBoardInConsole(int[][] board) {
        System.out.println("―――――――――――――――――――――――――――――――――――――");
        System.out.println(" \t  A   B   C   D   E   F   G   H  ");
        StringBuilder nextRowString = new StringBuilder();
        for (int y = 7; y >= 0; y--) {
            nextRowString.append(y + 1).append("\t|");
            for (int x = 0; x < 8; x++) {
                nextRowString.append(" ").append(board[x][y]).append(" |");
            }
            System.out.println(nextRowString);
            nextRowString.setLength(0);
        }
        System.out.println("―――――――――――――――――――――――――――――――――――――");
    }

    // public static ArrayList<Pion> getInitialWhitePion() {
    // ArrayList<Pion> Pions = new ArrayList<Pion>();
    //
    // Pions.add(new Pion(1, 2));
    // Pions.add(new Pion(1, 3));
    // Pions.add(new Pion(1, 4));
    // Pions.add(new Pion(1, 5));
    // Pions.add(new Pion(1, 6));
    // Pions.add(new Pion(1, 7));
    // Pions.add(new Pion(8, 2));
    // Pions.add(new Pion(8, 3));
    // Pions.add(new Pion(8, 4));
    // Pions.add(new Pion(8, 5));
    // Pions.add(new Pion(8, 6));
    // Pions.add(new Pion(8, 7));
    //
    // return Pions;
    // }
    //
    // public static ArrayList<Pion> getInitialBlackPion() {
    // ArrayList<Pion> Pions = new ArrayList<Pion>();
    //
    // Pions.add(new Pion(2, 1));
    // Pions.add(new Pion(3, 1));
    // Pions.add(new Pion(4, 1));
    // Pions.add(new Pion(5, 1));
    // Pions.add(new Pion(6, 1));
    // Pions.add(new Pion(7, 1));
    // Pions.add(new Pion(2, 8));
    // Pions.add(new Pion(3, 8));
    // Pions.add(new Pion(4, 8));
    // Pions.add(new Pion(5, 8));
    // Pions.add(new Pion(6, 8));
    // Pions.add(new Pion(7, 8));
    //
    // return Pions;
    // }
}
