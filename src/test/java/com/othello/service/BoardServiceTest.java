package com.othello.service;

import com.othello.exception.OthelloException;
import com.othello.model.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    @Test
    void switchPlayer() {
        BoardService boardService = new BoardService();
        boardService.setCurrentPlayer(1);
        boardService.switchPlayer();
        assertEquals(boardService.getCurrentPlayer(), 2);
    }

    @Test
    void getAnotherPlayer() {
        BoardService boardService = new BoardService();
        boardService.setCurrentPlayer(1);
        int otherPlayer = boardService.getAnotherPlayer();
        assertEquals(otherPlayer, 2);
    }

    @Test
    void checkInputMove() throws OthelloException {
        BoardService boardService = new BoardService();
        boardService.checkInputMove("e6");

        Assertions.assertThrows(OthelloException.class, () -> {
            boardService.checkInputMove("d6");
        });
    }

    @Test
    void isMoveOutOfBound() {
        BoardService boardService = new BoardService();
        boolean moveOutOfBound = boardService.isMoveOutOfBound(8, 8);
        assertTrue(moveOutOfBound);
        moveOutOfBound = boardService.isMoveOutOfBound(-1, -1);
        assertTrue(moveOutOfBound);
        moveOutOfBound = boardService.isMoveOutOfBound(-1, 8);
        assertTrue(moveOutOfBound);
        moveOutOfBound = boardService.isMoveOutOfBound(8, -1);
        assertTrue(moveOutOfBound);
        moveOutOfBound = boardService.isMoveOutOfBound(5, 5);
        assertFalse(moveOutOfBound);
    }


    @Test
    void getPossibleMoves() {
        BoardService boardService = new BoardService();
        Set<Point> possibleMoves = boardService.getPossibleMoves();
        assertEquals(possibleMoves.size(), 4);
    }

    @Test
    void getPlayerPoints() {
        BoardService boardService = new BoardService();
        Set<Point> playerPoints = boardService.getPlayerPoints(1);
        assertEquals(playerPoints.size(), 2);
    }

    @Test
    void isGameOver() {
        BoardService boardService = new BoardService();
        boolean bGameOver = boardService.isGameOver();
        assertFalse(bGameOver);

        int[][] board = {
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
        };
        boardService.setBoard(board);
        bGameOver = boardService.isGameOver();
        assertTrue(bGameOver);

        int[][] board2 = {
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,0},
        };
        boardService.setBoard(board2);
        bGameOver = boardService.isGameOver();
        assertTrue(bGameOver);

        int[][] board3 = {
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {2,2,2,2,2,2,2,2},
        };
        boardService.setBoard(board3);
        bGameOver = boardService.isGameOver();
        assertTrue(bGameOver);
    }
}