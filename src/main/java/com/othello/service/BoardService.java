package com.othello.service;

import com.google.common.collect.ImmutableMap;
import com.othello.exception.OthelloException;
import com.othello.model.Direction;
import com.othello.model.Point;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Board Service
 */
@Service
public class BoardService {

    /**
     * 0 - empty
     * 1 - player X
     * 2 - player O
     */
    private int[][] board = {
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,2,1,0,0,0},
            {0,0,0,1,2,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0}
    };

    private int currentPlayer = 1;

    /**
     * PrintBoardMap is used to convert board from 0,1,2 to "-", "X", "O"
     */
    private final Map<Integer,String> printBoardMap = new ImmutableMap.Builder<Integer,String>()
            .put(1,"X")
            .put(2,"O")
            .put(0,"-")
            .build();


    public BoardService() {
    }

    /**
     * The method which is used to start the game
     */
    public void play() {
        printBoard();
        while (!isGameOver()) {
            try {
                String move = getInputMove();
                Point point = checkInputMove(move);
                playMove(point);
                switchPlayer();
                printBoard();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get move from console
     * @return
     */
    protected String getInputMove() {
        String sPlayer = currentPlayer == 1 ? "X" : "O";
        System.out.print("Player " + sPlayer + " turn : ");
        Scanner input=new Scanner(System.in);
        return input.nextLine();
    }

    /**
     * Switch next player
     */
    protected void switchPlayer() {
        if(!getPossibleMoves().isEmpty()) {
            currentPlayer = getAnotherPlayer();
        }
    }

    /**
     * Get another player
     * @return
     */
    protected int getAnotherPlayer() {
        return currentPlayer == 1 ? 2 : 1;
    }

    /**
     * Check if the move is valid
     * @param move
     * @return
     * @throws OthelloException
     */
    protected Point checkInputMove(String move) throws OthelloException {
        if (StringUtils.isBlank(move) || move.length() != 2) {
            throw new OthelloException("Move should contain 2 letters!!!");
        }

        /**
         * if player X play, the move is started with letter, and end with a number ex: "e6"
         * if player Y play, the move is started with number, and end with a letter ex: "6e"
         * use ascii code to convert x and y between 0 - 7 (board size)
         */
        int x = move.charAt(0);
        int y = move.charAt(1);

        if (currentPlayer == 1) {
            x -= 97;
            y -= 49;
        }

        if (currentPlayer == 2) {
            x -= 49;
            y -= 97;
        }

        if(isMoveOutOfBound(x, y)) {
            throw new OthelloException("Move is not valid ！！！");
        }

        if(board[x][y] != 0) {
            throw new OthelloException("The position is not empty ！！！");
        }

        Set<Point> possibleMoves = getPossibleMoves();
        Point point = new Point(x,y);
        if(!possibleMoves.contains(new Point(x,y))) {
            throw new OthelloException("Move is not valid ！！！");
        }
        return point;
    }

    /**
     * Check if the move exceeds the bound
     * @param x
     * @param y
     * @return
     */
    protected boolean isMoveOutOfBound(int x, int y) {
        return (x < 0 || x >= board.length || y < 0 || y >= board.length);
    }

    /**
     * Get all possible moves for the current player
     * @return the set of possible moves
     */
    protected Set<Point> getPossibleMoves() {
        Set<Point> res = new HashSet<>();
        Set<Point> playerpoints = getPlayerPoints(currentPlayer);
        //for each piece of the player, test all directions, if next point is belong to another player,
        // then we continue, if the next point is empty, this point is possible.
        for (Point point : playerpoints) {
            for (Direction direction : Direction.values()) {
                Point nextPoint = new Point(point.getX() + direction.getX(), point.getY() + direction.getY());
                int anotherPlayer = getAnotherPlayer();
                if(isMoveOutOfBound(nextPoint.getX(), nextPoint.getY()) || board[nextPoint.getX()][nextPoint.getY()] != anotherPlayer){
                    continue;
                }

                while (!isMoveOutOfBound(nextPoint.getX(), nextPoint.getY())) {
                    if (board[nextPoint.getX()][nextPoint.getY()] == currentPlayer) {
                        break;
                    } else if (board[nextPoint.getX()][nextPoint.getY()] == 0) {
                        res.add(nextPoint);
                        break;
                    }
                    nextPoint = new Point(nextPoint.getX() + direction.getX(), nextPoint.getY() + direction.getY());
                }
            }
        }
        return res;
    }

    /**
     * get all pieces of the player
     * @param player
     * @return
     */
    protected Set<Point> getPlayerPoints(int player) {
        Set<Point> points = new HashSet<>();
        for (int x=0; x<board.length; x++) {
            for(int y=0; y<board.length; y++) {
                if (board[x][y] == player) {
                    points.add(new Point(x,y));
                }
            }
        }
        return points;
    }

    /**
     * Place the piece, all 'O's lying on all straight lines between the new 'X' and any existing 'X' are captured
     * @param point
     */
    protected void playMove(Point point) {
        board[point.getX()][point.getY()] = currentPlayer;
        //the set is used to store potential points to be placed
        Set<Point> toBeChangedPointsSet;

        for (Direction direction : Direction.values()) {
            Point nextpoint = new Point(point.getX() + direction.getX(), point.getY() + direction.getY());
            int otherPlay = getAnotherPlayer();
            if(isMoveOutOfBound(nextpoint.getX(), nextpoint.getY()) || board[nextpoint.getX()][nextpoint.getY()] != otherPlay){
                continue;
            }

            toBeChangedPointsSet = new HashSet<>();
            while (board[nextpoint.getX()][nextpoint.getY()] == getAnotherPlayer()) {
                toBeChangedPointsSet.add(nextpoint);
                nextpoint = new Point(nextpoint.getX() + direction.getX(), nextpoint.getY() + direction.getY());
            }
            if(board[nextpoint.getX()][nextpoint.getY()] != 0) {
                toBeChangedPointsSet.forEach(p -> board[p.getX()][p.getY()] = currentPlayer);
            }
        }
    }

    /**
     * Check if the game is over
     * the game is over if the board is full or one player has zero piece
     * @return
     */
    protected boolean isGameOver() {
        if(isFull() || getScore(currentPlayer) == 0 || getScore(getAnotherPlayer()) == 0){
            int scoreX = getScore(1);
            int scoreY = getScore(2);
            if(scoreX > scoreY) {
                System.out.println("Player X wins " + scoreX + " VS " + scoreY);
            } else if (scoreX < scoreY) {
                System.out.println("Player Y wins " + scoreX + " VS " + scoreY);
            } else {
                System.out.println("This game is a tie");
            }
            return true;
        }
        return false;
    }

    /**
     * Check if board is full
     * return
     */
    protected boolean isFull() {
        for (int x=0; x<board.length; x++) {
            for(int y=0; y<board.length; y++) {
                if (board[x][y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * get score of player
     * @param currentPlayer
     * @return
     */
    protected int getScore(int currentPlayer) {
        int count = 0;
        for (int x=0; x<board.length; x++) {
            for(int y=0; y<board.length; y++) {
                if (board[x][y] == currentPlayer) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * print the board
     */
    public void printBoard() {
        System.out.println("==========================");
        Arrays.stream(board).map(this::convertpointToXorO).forEach( e->System.out.println(Arrays.toString(e)));
        System.out.println("==========================\n");
    }

    /**
     * convert board from 0,1,2 to "-", "X", "O" by using printBoardMap
     * @param array
     * @return
     */
    private String[] convertpointToXorO(int[] array) {
        return Arrays.stream(array).mapToObj(printBoardMap::get).toArray(String[]::new);
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
