package Laboratoire4;

import java.io.*;
import java.net.*;
import java.util.Arrays;

class Client {
    private static Board board = new Board(8, 8);

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8888;

        if(args.length > 0) {
            hostname = args[0];
        }

        if(args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;

        try {
            MyClient = new Socket(hostname, port);

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
                        board.setPlayerColor(Pion.colors.white);
                        board.setEnnemyColor(Pion.colors.black);
                        handleNewGameWhite(input, output);
                        System.out.println("HERE");
                    }

                    // Debut de la partie en joueur Noir
                    case '2' -> {
                        board.setPlayerColor(Pion.colors.black);
                        board.setEnnemyColor(Pion.colors.white);
                        handleNewGameBlack(input, output);
                    }

                    // Le serveur demande le prochain coup
                    // Le message contient aussi le dernier coup joue.
                    case '3' -> {
                        System.out.println("I'm the board being passed");
                        printBoardInConsole(board);

                        handleServerRequestUpdate(input, output);
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

    private static void handleNewGameWhite(BufferedInputStream input, BufferedOutputStream output) throws IOException {

        byte[] aBuffer = new byte[1024];

        int size = input.available();
        // System.out.println("size " + size);
        input.read(aBuffer, 0, size);
        String s = new String(aBuffer).trim();
        setupBoard(s);


        printBoardInConsole(board);

        System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");

        String dataToSend = executeNextMove(board, board.getPlayerColor().getValue(), false);
        printBoardInConsole(board);

        // output.write(move.getBytes(), 0, move.length());
        output.write(dataToSend.getBytes(), 0, dataToSend.length());
        output.flush();
    }

    private static void handleNewGameBlack(BufferedInputStream input, BufferedOutputStream output) throws IOException {
        System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
        byte[] aBuffer = new byte[1024];

        int size = input.available();
        // System.out.println("size " + size);
        input.read(aBuffer, 0, size);
        String s = new String(aBuffer).trim();
        setupBoard(s);
        printBoardInConsole(board);

        output.flush();
    }

    private static void handleServerRequestUpdate(BufferedInputStream input, BufferedOutputStream output) throws IOException {
        byte[] aBuffer = new byte[16];

        int size = input.available();
        System.out.println("size :" + size);
        input.read(aBuffer, 0, size);

        String move = new String(aBuffer);


        // update board
        move = move.trim().replace(" ", "").replace("-", "");
        boolean captured = Movement.lastMoveCaptured(move, board);

        Movement.executeMove(move, board);

        System.out.println("Dernier coup :" + move);
        printBoardInConsole(board);


        System.out.println("Entrez votre coup : ");
        String dataToSend = executeNextMove(board, board.getPlayerColor().getValue(), captured);
        System.out.println("PION Joueur restant: " + board.getPlayerPions().size());
        System.out.println("PION Ennemi restant: " + board.getEnnemyPions().size());
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
                if (!board.getCase(x, y).isEmpty()) {
                    caseVal = board.getCase(x, y).getPion().getColorValue();
                }

                nextRowString.append(" ").append(caseVal).append(" |");
            }
            System.out.println(nextRowString);
            nextRowString.setLength(0);
        }
        System.out.println("―――――――――――――――――――――――――――――――――――――");
    }

    private static String executeNextMove(Board currentBoard, int playerColor, boolean captured) {
        Board boardCopy = currentBoard.clone();

        String nextMove = Minmax.findBestMove(boardCopy, 3, captured);
        String dataToSend = nextMove.substring(0, 2) + " " + nextMove.substring(2);

        Movement.executeMove(nextMove, currentBoard);

        return dataToSend;
    }

    private static void setupBoard(String s) {
        System.out.println(s);
        String[] boardValues;
        boardValues = s.split(" ");
        int x = 0, y = 0;
        for (int i = 0; i < boardValues.length; i++) {
            Integer value = Integer.parseInt(boardValues[i]);

            if (value == Pion.colors.black.getValue()) {
                Pion pion = new Pion(x, y, Pion.colors.black);
                board.getCase(x, y).setPion(pion);
                board.addPionNoir(pion);
            }

            if (value == Pion.colors.white.getValue()) {
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
    }
}
