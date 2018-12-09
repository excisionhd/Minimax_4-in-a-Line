//CS4200: Artificial Intelligence 4-In-A-Line Minimax Algorithm
//With Alpha-Beta Pruning
//By: Amir Sotoodeh

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Response{
    protected State state;
    protected double score;

    public Response(State state, double score){
        this.state = state;
        this.score = score;
    }

    public State getState(){
        return state;
    }

    public double getScore(){
        return score;
    }
}

class AI{
    protected int depth;
    protected char SYMBOL;
    protected char opponentSYMBOL = 'O';
    protected State currentState;
    protected State savedState;
    protected Hashtable<Integer, Integer> memoizedStates;
    protected int[] bestMove;
    protected boolean foundFirst;

    public AI(int depth, char SYMBOL, State state){
        this.depth = depth;
        this.SYMBOL = SYMBOL;
        this.currentState = state;
        this.memoizedStates = new Hashtable<>();
        this.bestMove = new int[3];
        this.foundFirst = false;
        this.savedState = new State(8,8,currentState.board);
    };

    //IDS, not in use; Found that the AI played less intelligent and could not figure out why...
    int[] IDSMinimax() {
        savedState = currentState.getCopyState();

        Thread t1 = new Thread(new Runnable() {

            private boolean exit = false;

            @Override
            public void run() {
                for(int i = 0; i <= depth; i++){
                    bestMove = minimax(depth, SYMBOL, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    //System.out.println(i);
                }
            }
        });
        //t1.start();

        try{
            t1.start();
            t1.join(25000);
            if (t1.isAlive()) {
                t1.interrupt();
                currentState.board = savedState.board;

            }


        }
        catch(InterruptedException e){
            e.getMessage();
        }

        //System.out.println(bestMove[1] + " " + bestMove[2]);
        return new int[] {bestMove[1], bestMove[2]};
    }


    int[] move() {
        List<String> nextMoves = currentState.getAvailableMoves();

        //Check if there is a win state in the current available moves.
        for (String move : nextMoves) {
            int row = move.charAt(0) - '0';
            int col = move.charAt(1) - '0';
            currentState.board[row][col] = SYMBOL;
            if(currentState.checkWin(currentState.getBoard(), 'X', 4) == 100){
                return new int[] { row, col };
            }
            else{
                currentState.board[row][col] = '-';
            }
        }


        int[] result = minimax(depth, SYMBOL, Integer.MIN_VALUE, Integer.MAX_VALUE);

        //Return the best move's row and col.
        return new int[] {result[1], result[2]};

    }

    private int[] minimax(int depth, char symbol, int alpha, int beta) {
        List<String> nextMoves = currentState.getAvailableMoves();
        int score = 0;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            score = currentState.evaluationFunction();
            return new int[] {score, bestRow, bestCol};

        } else {
            for (String move : nextMoves) {
                //Attempt the move
                int row = move.charAt(0) - '0';
                int col = move.charAt(1) - '0';
                currentState.board[row][col] = symbol;
                int hashcode = currentState.getHashCode();

                if (symbol == SYMBOL) {  //MAX
                    if(memoizedStates.containsKey(hashCode())){
                        score = memoizedStates.get(hashcode);
                    }else{
                        score = minimax(depth - 1, opponentSYMBOL, alpha, beta)[0];
                        memoizedStates.put(hashcode, score);
                    }

                    if (score > alpha) {
                        alpha = score;
                        bestRow = row;
                        bestCol = col;
                    }
                } else {  //MIN
                    if(memoizedStates.containsKey(hashCode())){
                        score = memoizedStates.get(hashcode);
                    }
                    else{
                        score = minimax(depth - 1, SYMBOL, alpha, beta)[0];
                    }

                    if (score < beta) {
                        beta = score;
                        bestRow = row;
                        bestCol = col;
                    }
                }

                currentState.board[row][col] = '-';
                if (alpha >= beta) break;
            }
            return new int[] {(symbol == SYMBOL) ? alpha : beta, bestRow, bestCol};
        }
    }

}

class State{
    protected int ROWS, COLS;
    protected char[][] board;
    protected int numX, numO;


    public State(int rows, int cols, char[][] board){
        this.ROWS = rows;
        this.COLS = cols;
        this.board = new char[rows][cols];
        this.numX = 0;
        this.numO = 0;


        for (int i = 0; i<board.length;i++){
            for (int j = 0; j<board[i].length;j++){
                this.board[i][j] = board[i][j];
            }
        }
    }

    public void placeMove(int row, int col, char val){
        this.board[row][col] = val;
        if(val == 'X'){
            numX++;
        }
        else if(val == 'O'){
            numO++;
        }
    }

    public State getCopyState(){
        char[][] copy = new char[ROWS][COLS];

        for (int i = 0; i<this.board.length;i++){
            for (int j = 0; j< this.board[i].length ;j++){
                copy[i][j] = this.board[i][j];
            }
        }

        return new State(8,8, copy);
    }

    public List<String> getAvailableMoves(){

        List<String> availableMoves = new ArrayList<String>();

        for (int i = 0; i<this.board.length;i++){
            for (int j=0; j<this.board[i].length;j++){
                if( this.board[i][j] == '-'){
                    int startPosX = (i - 1 < 0) ? i : i-1;
                    int startPosY = (j - 1 < 0) ? j : j-1;
                    int endPosX =   (i + 1 > 7) ? i : i+1;
                    int endPosY =   (j + 1 > 7) ? j : j+1;

                    boolean hasNeighbors = false;
                    for (int rowNum=startPosX; rowNum<=endPosX; rowNum++) {
                        for (int colNum=startPosY; colNum<=endPosY; colNum++) {
                            if (this.board[rowNum][colNum] == 'X' || this.board[rowNum][colNum] == 'O'){
                                hasNeighbors = true;
                            }
                        }
                    }
                    if(hasNeighbors || this.getHashCode() == 1956590592){
                        availableMoves.add(Integer.toString(i) + Integer.toString(j));
                    }

                }
            }
        }

        if(this.getHashCode() == 1956590592){
            Random r = new Random();
            String rMove = availableMoves.get(r.nextInt(availableMoves.size()));
            List<String> randomElement = new ArrayList<>();
            randomElement.add(rMove);
            return randomElement;
        }
        else{
            return availableMoves;
        }

    }

    public int getHashCode(){
        String s = "";
        for (int i = 0; i<board.length;i++){
            for (int j = 0; j<board[i].length;j++){
                s += String.valueOf(board[i][j]);
            }
        }
        return s.hashCode();
    }

    public int checkWin(char[][] board, char player, int x) {
        int numWins = 0;

        if (x==4){
            // horizontalCheck
            for (int j = 0; j < 8 - (x-1); j++) {
                for (int i = 0; i < 8; i++) {
                    int count = 0;
                    for (int y = 0; y < x; y++){
                        if (board[i][j + y] == player){
                            count++;
                        }
                        if (count == x){
                            return 100;
                        }
                    }
                }
            }
            // verticalCheck
            for (int i = 0; i < 8 - (x-1); i++) {
                for (int j = 0; j < 8; j++) {
                    int count = 0;
                    for (int y = 0; y < x; y++){
                        if (board[i+y][j] == player){
                            count++;
                        }
                        if (count == x){
                            return 100;
                        }
                    }
                }
            }
        }
        else{
            // horizontalCheck
            for (int j = 0; j < 8 - (x-1); j++) {
                for (int i = 0; i < 8; i++) {
                    int count = 0;
                    for (int y = 0; y < x; y++){
                        if (board[i][j + y] == player){
                            count++;
                        }
                        if (count == x){
                            numWins++;
                        }
                    }
                }
            }
            // verticalCheck
            for (int i = 0; i < 8 - (x-1); i++) {
                for (int j = 0; j < 8; j++) {
                    int count = 0;
                    for (int y = 0; y < x; y++){
                        if (board[i+y][j] == player){
                            count++;
                        }
                        if (count == x){
                            numWins++;
                        }
                    }
                }
            }
        }

        return numWins;

    }

    public int evaluationFunction(){

        int totalScore = 0;

        if (checkWin(this.board, 'X', 4) == 100){
            totalScore += 2500;
            //return totalScore;
        }
        if (checkWin(this.board, 'O', 4) == 100) {
            totalScore -= 4000;
            //return totalScore;
        }
        if (checkWin(this.board, 'O', 3) == 2) {
            totalScore -= 500;
        }
        if (checkWin(this.board, 'X', 3) == 2){
            totalScore += 600;
        }
        if (checkWin(this.board, 'O', 3) == 1) {
            totalScore -= 300;
        }
        if (checkWin(this.board, 'X', 3) == 1){
            totalScore += 200;
        }
        if (checkWin(this.board, 'X', 3) == 1 && numX > numO){
            totalScore += 300;
        }
        if (checkWin(this.board, 'X', 2) == 1 || checkWin(this.board, 'X', 2) == 2){
            totalScore += 5;
        }

        return totalScore;
    }


    public char[][] getBoard() {
        return board;
    }
}

public class Game {

    char[][] board;
    final static char PLAYER1 = 'O';
    final static char PLAYER2 = 'X';
    static List<String> pastMoves = new ArrayList<>();
    static boolean opponentFirst;


    public Game(char[][] board){
        this.board = board;
    }

    public static void main(String[] args) {

        State state = new State(8,8, generateEmptyBoard());
        AI ai = new AI(5, 'X', state);

        boolean done = false;
        int currentPlayer = 0;

        System.out.println("CS4200: 4-in-a-Line");
        System.out.println("Who Goes First: Computer (C) or Opponent (O)?");
        Scanner play = new Scanner(System.in);
        String player = play.next();

        boolean doneChoosing = false;

        while (!doneChoosing){
            if (player.toUpperCase().equals("C")){
                currentPlayer = -1;
                pastMoves.add("Player vs. Opponent\n");
                doneChoosing=true;
            }
            else if(player.toUpperCase().equals("O")){
                currentPlayer = 1;
                pastMoves.add("Opponent vs. Player\n");
                doneChoosing=true;
            }
            else{
                System.out.println("Who Goes First: Computer (C) or Opponent (O)?");
                player = play.next();
            }
        }



        Scanner sc = new Scanner(System.in);
        while (!done){

            if (currentPlayer == 1){
                System.out.println("Player 1 Move:");
                String choice = sc.next();
                while(!processChoice(choice, state.getBoard())) {
                    System.out.println("Player 1 Move:");
                    choice = sc.next();
                }
                placeMove(choice.charAt(0), Integer.parseInt(Character.toString(choice.charAt(1))), PLAYER1, state.board);
                if(pastMoves.get(pastMoves.size() -1).length() == 2){
                    pastMoves.set(pastMoves.size() - 1, pastMoves.get(pastMoves.size() -1) + "\t" + choice.charAt(0) + Character.toString(choice.charAt(1)) + "");
                }
                else{
                    pastMoves.add(choice.charAt(0) + Character.toString(choice.charAt(1)));
                }
            }
            else{
                System.out.println("Player 2 Move:");
                int[] response = ai.move();
                state.placeMove(response[0], response[1], ai.SYMBOL);
                char x = (char) (response[0] + 97);
                String y = Integer.toString((response[1] + 1));
                if(pastMoves.get(pastMoves.size() -1).length() == 2){
                    pastMoves.set(pastMoves.size() - 1, pastMoves.get(pastMoves.size()-1) + "\t" + x + y + "");
                }
                else{
                    pastMoves.add(x+y);
                }
            }


            currentPlayer *= -1;
            printBoard(state.getBoard());

            if(state.checkWin(state.getBoard(),'X', 4) == 100){
                System.out.println("Game Over... AI wins!");
                break;
            }
            else if(state.checkWin(state.getBoard(),'O', 4) == 100){
                System.out.println("Game Over... Player wins!");
                break;
            }


        }


    }


    private static boolean processChoice(String choice, char[][] board) {


        String pattern = "^[a-h][1-8]$";
        String line = choice;

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);

        if (m.find()){
            int row = choice.charAt(0) - 97;
            int col = Integer.parseInt(Character.toString(choice.charAt(1))) - 1;
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

        System.out.print("  1 2 3 4 5 6 7 8\t");
        System.out.print(pastMoves.get(0));



        for (int i = 0; i<board.length; i++){
            //print rows A-Z
            System.out.print((char)ALPHABET + " ");

            //print characters at positions
            for (int j = 0; j<board[i].length;j++){

                System.out.print(board[i][j] + " ");

            }


            if(i+1 < pastMoves.size()){
                System.out.printf("\t\t%d. %s", i+1, pastMoves.get(i+1));
            }

            System.out.println();

            ALPHABET++;
        }
        if(pastMoves.size()>8){
            for(int x = 9; x<pastMoves.size();x++){
                System.out.println("\t\t\t\t\t\t" + (x) +". " +  pastMoves.get(x));
            }
        }
        System.out.println();

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


}
