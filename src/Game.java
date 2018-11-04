//CS4200: Artificial Intelligence 4-In-A-Line Minimax Algorithm
//With Alpha-Beta Pruning
//By: Amir Sotoodeh

import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    char[][] board;
    final static char PLAYER1 = 'X';
    final static char PLAYER2 = 'O';


    public Game(char[][] board){
        this.board = board;
    }

    public static void main(String[] args) {
        char[][] b = generateEmptyBoard();
        boolean done = false;

        int currentPlayer = 1;

        Scanner sc = new Scanner(System.in);

        System.out.println("CS4200: 4-in-a-Line");
        System.out.println("Player 1 Move:");

        while (!done){

            String choice = sc.next();

            while(!processChoice(choice, b)){
                if (currentPlayer == 1){
                    System.out.println("Player 1 Move:");
                }
                else{
                    System.out.println("Player 2 Move:");
                }
                choice = sc.next();
            }

            System.out.println(choice.charAt(0) + " " + choice.charAt(1));

            if (currentPlayer == 1){
                placeMove(choice.charAt(0), Integer.parseInt(Character.toString(choice.charAt(1))), PLAYER1, b);
            }
            else{
                placeMove(choice.charAt(0), Integer.parseInt(Character.toString(choice.charAt(1))), PLAYER2, b);
            }

            currentPlayer *= -1;
            printBoard(b);

            if (currentPlayer == 1){
                System.out.println("Player 1 Move:");
            }
            else{
                System.out.println("Player 2 Move:");
            }

        }
        printBoard(b);
        placeMove('a',1,'X', b);
        placeMove('a',2,'X', b);
        placeMove('a',3,'X', b);
        placeMove('a',4,'X', b);
        placeMove('a',5,'X', b);
        placeMove('a',6,'X', b);
        placeMove('a',7,'O', b);
        placeMove('a',8,'X', b);
        placeMove('b',8,'X', b);
        placeMove('c',8,'X', b);
        placeMove('d',8,'-', b);
        placeMove('e',8,'O', b);
        printBoard(b);

        System.out.println(checkWin(b));

    }

    private static boolean processChoice(String choice, char[][] board) {


        String pattern = "^[a-h][1-8]$";
        String line = choice;

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);

        if (m.find()){
            int row = choice.charAt(0) - 97;
            int col = Integer.parseInt(Character.toString(choice.charAt(1))) - 1;
            System.out.println(col);
            if (board[row][col] == 'X' || board[row][col] == 'O'){
                return false;
            }
            else {
                return true;
            }
        }

        return false;
    }

    public static char[][] generateEmptyBoard(){
        char[][] board = new char[8][8];

        for(int i = 0; i<board.length;i++){
            for(int j =0;j<board[i].length;j++){
                board[i][j] = '-';
            }
        }
        return board;
    }

    public static void printBoard(char[][] board){
        int ALPHABET = 65;

        System.out.println("  1 2 3 4 5 6 7 8");

        for (int i = 0; i<board.length; i++){
            //print rows A-Z
            System.out.print((char)ALPHABET + " ");

            //print characters at positions
            for (int j = 0; j<board[i].length;j++){

                System.out.print(board[i][j] + " ");

            }
            System.out.println();
            ALPHABET++;
        }

    }

    public static boolean checkWin(char[][] board) {
        return checkHorizontal(board)
                || checkVertical(board);
    }

    private static void placeMove(char row, int col, char player, char[][] board){
        try {
            int newRow = (int) row - 97;
            board[newRow][col - 1] = player;
        }

        catch(ArrayIndexOutOfBoundsException e){
            System.out.println(e.getMessage());
        }
    }

    private static boolean checkHorizontal(char[][] board) {

        Hashtable<Character, Integer> table = new Hashtable<>();
        table.put('X', 0);
        table.put('O', 0);

        for (int row = 0; row < board.length; row++) {

            for (int col = 0; col < board[row].length; col++) {
                char element = board[row][col];

                if (element == 'X') {
                    table.put(element, table.get(element) + 1);
                    table.put('O', 0);

                    if (table.get(element) == 4) {
                        return true;
                    }
                }
                else if(element == 'O'){
                    table.put(element, table.get(element) + 1);
                    table.put('X', 0);

                    if (table.get(element) == 4) {
                        return true;
                    }
                }
                else {
                    table.put('X', 0);
                    table.put('O', 0);
                }
            }
        }
        return false;
    }

    private static boolean checkVertical(char[][] board) {
        Hashtable<Character, Integer> table = new Hashtable<>();
        table.put('X', 0);
        table.put('O', 0);

        for (int j = 0; j<board[0].length; j++) {
            for (int i = 0; i < board.length; i++) {
                char element = board[i][j];

                if (element == 'X') {
                    table.put(element, table.get(element) + 1);
                    table.put('O', 0);

                    if (table.get(element) == 4) {
                        return true;
                    }
                }
                else if(element == 'O'){
                    table.put(element, table.get(element) + 1);
                    table.put('X', 0);

                    if (table.get(element) == 4) {
                        return true;
                    }
                }
                else {
                    table.put('X', 0);
                    table.put('O', 0);
                }
            }
        }

        return false;
    }


}
