package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20211103001L;

	static final int NUM_SPACES = 64;

	enum CellState {
		EMPTY, WHITE, BLACK
	}

	private CellState[][] board = new CellState[8][8];
	private int turn;

	/**
	 * initializes a new game instance
	 */
	public Game() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = CellState.EMPTY;
			}
		}
		// Set the 4 starting stones
		board[4][3] = CellState.BLACK;
		board[3][4] = CellState.BLACK;
		board[3][3] = CellState.WHITE;
		board[4][4] = CellState.WHITE;

		turn = 0;
	}

	public int countWhite() {
		int white = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (isWhite(x, y)) {
					white++;
				}
			}
		}
		return white;
	}

	public int countBlack() {
		int black = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (isBlack(x, y)) {
					black++;
				}
			}
		}
		return black;
	}

	/**
	 * Returns the current player if game is still in play
	 * 
	 * @return the current player if game is not a draw
	 */
	public CellState getCurrentPlayer() {
		return isOver() ? null : turn % 2 == 0 ? CellState.BLACK : CellState.WHITE;

	}

	/**
	 * finds other player
	 * 
	 */
	public CellState getOtherPlayer() {
		return getCurrentPlayer() == CellState.BLACK ? CellState.WHITE : CellState.BLACK;

	}

	/**
	 * Returns true iff able to place the mark of the current player into the
	 * specified grid location.
	 * 
	 * @param loc the specified board location
	 * @return {@code true} iff mark was successfully placed
	 */
	public boolean placeMark(int x, int y) {
		return placeMark(getCurrentPlayer(), x, y);
	}

	private boolean placeMark(CellState playerMark, int x, int y) {
		boolean placed = false;

		if (playerMark != null && checkLegalPlay(x, y)) {
			board[x][y] = playerMark;
			Point start = new Point(x, y);
			for (Point step : ALL_DIRECTIONS) {
				// handler is stateful so create new for each direction
				FlipCellHandler flipCell = new FlipCellHandler(getCurrentPlayer(), getOtherPlayer());
				iterateCells(start, step, flipCell);
			}
			placed = true;
			if (!isOver()) {
				turn++;
			}
		}

		return placed;
	}

	/**
	 * returns true iff the game is over--either won or drawn
	 * 
	 * @return {@code true} iff game is over
	 */
	public boolean isOver() {

		return turn == NUM_SPACES - 4;

	}

	private boolean isOnBoard(int pos) {
		return pos >= 0 && pos < 8;
	}

	public boolean isEmpty(int x, int y) {
		return board[x][y] == CellState.EMPTY;
	}

	public boolean isWhite(int x, int y) {
		return board[x][y] == CellState.WHITE;
	}

	public boolean isBlack(int x, int y) {
		return board[x][y] == CellState.BLACK;
	}

	public CellState getCellState(int x, int y) {
		if (isBlack(x, y)) {
			return CellState.BLACK;
		}
		if (isWhite(x, y)) {
			return CellState.WHITE;
		} else
			return CellState.EMPTY;
	}

	/**
	 * Returns a copy of the playing grid of this game
	 * 
	 * @return a copy of the playing grid
	 */
	public CellState[][] getBoard() {
		return board.clone();
	}

	/**
	 * Returns the mark of the winner, or {@code null} if game is not won
	 * 
	 * @return the {@link Marks} of the winner or {@code null} if game is not won
	 */
	public CellState getWinner() {
		CellState winner = null;
		if (isOver()) {
			if (countWhite() > countBlack())
				winner = CellState.WHITE;
			else
				winner = CellState.BLACK;
		}
		return winner;
	}

	public CellState setCellState(CellState cell) {
		return cell;
	}

	static class Point {
		public final int row;
		public final int col;

		public Point(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	private static final Point[] ALL_DIRECTIONS = new Point[] { new Point(1, 0), new Point(1, 1), new Point(0, 1),
			new Point(-1, 1), new Point(-1, 0), new Point(-1, -1), new Point(0, -1), new Point(1, -1), };

	interface CellHandler {
		boolean handleCell(int row, int col, CellState icon);
	}

	void iterateCells(Point start, Point step, CellHandler handler) {
		for (int row = start.row + step.row, col = start.col + step.col; isOnBoard(row)
				&& isOnBoard(col); row += step.row, col += step.col) {
			CellState icon = getCellState(row, col);
			// empty cell
			if (icon == CellState.EMPTY)
				break;
			// handler can stop iteration
			if (!handler.handleCell(row, col, icon))
				break;

		}
	}

	static class CheckCellHandler implements CellHandler {
		private final CellState otherIcon;
		private boolean hasOtherPieces = false;
		private boolean endsWithMine = false;

		public CheckCellHandler(CellState otherIcon) {
			this.otherIcon = otherIcon;
		}

		@Override
		public boolean handleCell(int row, int column, CellState icon) {
			if (icon == otherIcon) {
				hasOtherPieces = true;
				return true;
			} else {
				endsWithMine = true;
				return false;
			}
		}

		public boolean isGoodMove() {
			return hasOtherPieces && endsWithMine;
		}
	}

	class FlipCellHandler implements CellHandler {
		private final CellState myIcon;
		private final CellState otherIcon;
		private final List<Point> currentFlipList = new ArrayList<Point>();

		public FlipCellHandler(CellState myIcon, CellState otherIcon) {
			this.myIcon = myIcon;
			this.otherIcon = otherIcon;
		}

		@Override
		public boolean handleCell(int row, int column, CellState icon) {
			if (icon == myIcon) {
				// flip all cells
				for (Point p : currentFlipList) {
					board[p.row][p.col] = setCellState(myIcon);
				}
				return false;
			} else {
				currentFlipList.add(new Point(row, column));
				return true;
			}
		}
	}

	private boolean checkLegalPlay(int row, int col) {
		CellState otherIcon = getOtherPlayer();
		Point start = new Point(row, col);
		for (Point step : ALL_DIRECTIONS) {
			// handler is stateful so create new for each direction
			CheckCellHandler checkCellHandler = new CheckCellHandler(otherIcon);
			iterateCells(start, step, checkCellHandler);
			if (checkCellHandler.isGoodMove())
				return true;
		}
		return false;
	}

}