package Laboratoire4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import Laboratoire4.Board;
import Laboratoire4.Movement;
import Laboratoire4.Pion;
import Laboratoire4.Minmax;

class Client {
    static Socket MyClient;
    static BufferedInputStream input;
    static BufferedOutputStream output;
    private static Board board = new Board(8, 8);

    public static void main(String[] args) {
                try {
            MyClient = new Socket("localhost", 8888);

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Pion.colors playerColor = Pion.colors.none;

            while (true) {
                char cmd = 0;

                cmd = (char) input.read();
                System.out.println(cmd);

                switch (cmd) {
                    // Debut de la partie en joueur blanc
                    case '1' -> {
                        playerColor = Pion.colors.white;
                        handleNewGameWhite(input, output, console, playerColor.getValue());
                        System.out.println("HERE");
                    }

                    // Debut de la partie en joueur Noir
                    case '2' -> {
                        // pionsBlanc = getInitialWhitePion();
                        // pionsNoir = getInitialBlackPion();
                        playerColor = Pion.colors.black;
                        handleNewGameBlack(input, output, console, playerColor.getValue());
                    }

                    // Le serveur demande le prochain coup
                    // Le message contient aussi le dernier coup joue.
                    case '3' -> {
                        System.out.println("I'm the board being passed");
                        printBoardInConsole(board);

                        handleServerRequestUpdate(input, output, playerColor.getValue());
                        // int[][] board2 = board.clone();
                        // String move = Minmax.alphabeta(board2, 4, playerColor.getValue());
                        // Ici on assume que notre coup sera toujours valide puisque la fonction a été
                        // valider
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

    private static void handleNewGameWhite(BufferedInputStream input, BufferedOutputStream output,
            BufferedReader console, int playerColor) throws IOException {

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

            if(value == Pion.colors.black.getValue()) {
                Pion pion = new Pion(x, y, Pion.colors.black);
                board.getCase(x, y).setPion(pion);
                board.addPionNoir(pion);
            }

            if(value == Pion.colors.white.getValue()) {
                Pion pion = new Pion(x, y, Pion.colors.white);
                board.getCase(x, y).setPion(pion);
                board.addPionBlanc(pion);
            }

            x++;
            if (x == 8) {
                x = 0;
                y++;
            }
        }

        printBoardInConsole(board);

        System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");

        String dataToSend = executeNextMove(board, playerColor);
        printBoardInConsole(board);

        // output.write(move.getBytes(), 0, move.length());
        output.write(dataToSend.getBytes(), 0, dataToSend.length());
        output.flush();
    }

    private static void handleNewGameBlack(BufferedInputStream input, BufferedOutputStream output,
            BufferedReader console, int playerColor) throws IOException {
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
            Integer value = Integer.parseInt(boardValues[i]);

            if(value == Pion.colors.black.getValue()) {
                Pion pion = new Pion(x, y, Pion.colors.black);
                board.getCase(x, y).setPion(pion);
                board.addPionNoir(pion);
            }

            if(value == Pion.colors.white.getValue()) {
                Pion pion = new Pion(x, y, Pion.colors.white);
                board.getCase(x, y).setPion(pion);
                board.addPionBlanc(pion);
            }

            x++;
            if (x == 8) {
                x = 0;
                y++;
            }
        }

        String dataToSend = executeNextMove(board, playerColor);

        // output.write(move.getBytes(), 0, move.length());
        output.write(dataToSend.getBytes(), 0, dataToSend.length());
        output.flush();
    }

    private static void handleServerRequestUpdate(BufferedInputStream input, BufferedOutputStream output, int playerColor) throws IOException {
        byte[] aBuffer = new byte[16];

        int size = input.available();
        System.out.println("size :" + size);
        input.read(aBuffer, 0, size);

        String move = new String(aBuffer);

        // update board
        move = move.trim().replace(" ", "").replace("-", "");

        Movement.executeMove(move, board);

        System.out.println("Dernier coup :" + move);
        printBoardInConsole(board);


        System.out.println("Entrez votre coup : ");
        String dataToSend = executeNextMove(board, playerColor);
        printBoardInConsole(board);

        output.write(dataToSend.getBytes(), 0, dataToSend.length());
        output.flush();
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

    public static void printBoardInConsole(Board board) {
        System.out.println("―――――――――――――――――――――――――――――――――――――");
        System.out.println(" \t  A   B   C   D   E   F   G   H  ");
        StringBuilder nextRowString = new StringBuilder();

        for (int y = 7; y >= 0; y--) {
            nextRowString.append(y + 1).append("\t|");
            for (int x = 0; x < 8; x++) {
                int caseVal = 0;
                if(!board.getCase(x, y).isEmpty()) {
                    caseVal = board.getCase(x, y).getPion().getColorValue();
                }

                nextRowString.append(" ").append(caseVal).append(" |");
            }
            System.out.println(nextRowString);
            nextRowString.setLength(0);
        }
        System.out.println("―――――――――――――――――――――――――――――――――――――");
    }

    private static String executeNextMove(Board currentBoard, int playerColor) {
        System.out.println("BOARD BEFORE CLONE");
        printBoardInConsole(currentBoard);

        Board boardCopy = currentBoard.clone();

        System.out.println("BOARD AFTER CLONE");
        printBoardInConsole(currentBoard);
        printBoardInConsole(boardCopy);

        String nextMove = Minmax.alphabeta(boardCopy, 4, playerColor);
        String dataToSend = nextMove.substring(0, 2) + " " + nextMove.substring(2);

        System.out.println("BOARD BEFORE MOVE");
        printBoardInConsole(currentBoard);

        Movement.executeMove(nextMove, currentBoard);

        System.out.println("BOARD AFTER MOVE");
        printBoardInConsole(currentBoard);

        return dataToSend;
    }
}
