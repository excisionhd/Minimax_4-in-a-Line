import java.util.Hashtable;

public class Game {

    char[][] board;
    final char PLAYER1 = 'X';
    final char PLAYER2 = 'O';

    public Game(char[][] board){
        this.board = board;
    }

    public static void main(String[] args) {
        char[][] b = generateEmptyBoard();
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
        System.out.println(checkHorizontal(b));
        System.out.println(checkVertical(b));
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

/*    public boolean checkWin(char badge) {
        return checkHorizontalStreaks(board, badge)
                || checkVerticalStreaks(board, badge);
    }*/

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
