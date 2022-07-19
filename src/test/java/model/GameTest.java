package model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.Game.CellState;

class GameTest {

	@Test
	void testConstructed() {
		Game g = new Game();
		var board = g.getBoard();
		assertEquals(board[3][4], CellState.BLACK);
		assertEquals(board[0][0], CellState.EMPTY);
		assertTrue(g.placeMark(2, 3));
		assertEquals(g.countBlack(), 4);
	}

}
