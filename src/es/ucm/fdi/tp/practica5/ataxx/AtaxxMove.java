package es.ucm.fdi.tp.practica5.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxMove extends GameMove {
	private static final long serialVersionUID = 1L;

	protected int oriRow;
	protected int oriCol;

	protected int row;
	protected int col;

	/**
	 * This constructor should be used only for generating an instance to access
	 * the {@link #fromString(Piece, String)} method. E.g., it is used in
	 * {@link ConsolePlayer}. A move that is created using this constructor will
	 * have {@link #piece} equals to {@code null}, and thus cannot be executed.
	 */
	public AtaxxMove() {
	}

	/**
	 * This constructor creates a movement from an initial position and a final
	 * position.
	 * 
	 * @param oriRow
	 *            the initial position row.
	 * @param oriCol
	 *            the initial position column.
	 * @param row
	 *            the final position row.
	 * @param col
	 *            the final position column.
	 * @param p
	 *            the piece corresponding to the player which is associated with
	 *            this movement.
	 */
	public AtaxxMove(int oriRow, int oriCol, int row, int col, Piece p) {
		super(p);
		this.row = row;
		this.col = col;
		this.oriRow = oriRow;
		this.oriCol = oriCol;
	}

	/**
	 * Moves the piece from the original position to the final position, if the
	 * piece in that position is of the player associated with the movement.
	 * 
	 * @param board
	 *            the board of the game
	 * @param pieces
	 *            the list of players
	 * @throws a
	 *             GameError if the movement is not valid
	 */
	public void execute(Board board, List<Piece> pieces) {
		if (board.getPosition(oriRow, oriCol) == null) {
			throw new GameError("there is no piece in (" + oriRow + ","
					+ oriCol + ")!");
		}

		if (board.getPosition(oriRow, oriCol) != getPiece()) {
			throw new GameError("the piece in (" + oriRow + "," + oriCol
					+ ") is not yours!");
		}

		if (isValidDist()) {
			if (board.getPosition(row, col) == null) {
				board.setPosition(row, col, getPiece());// ponemos una pieza;
				if (!shortMove()) {
					board.setPosition(oriRow, oriCol, null);
				}
				convert(board);
			} else {
				throw new GameError("position (" + row + "," + col
						+ ") is already occupied!");
			}
		} else {
			throw new GameError("position (" + row + "," + col
					+ ") is too far!");
		}
	}

	/**
	 * Checks if the distance is valid for a move.
	 * 
	 * @return true if a square of side 4 centered on the initialPosition
	 *         contains the final position.
	 */
	protected Boolean isValidDist() {
		int distRow = Math.abs(row - oriRow);
		int distCol = Math.abs(col - oriCol);
		return (distRow <= 2 && distCol <= 2 && (distRow > 0 || distCol > 0));
	}

	/**
	 * Checks if a move is short.
	 * 
	 * @return true if a square of side 2 centered on the initialPosition
	 *         contains the final position.
	 */
	protected Boolean shortMove() {
		return (Math.abs(oriCol - col) <= 1 && Math.abs(oriRow - row) <= 1);
	}

	/**
	 * Converts all the pieces surrounding another piece to this piece.
	 * 
	 * @param b
	 *            the board.
	 */
	public void convert(Board b) {
		int startI = Math.max(0, row - 1);
		int startJ = Math.max(0, col - 1);
		int endI = Math.min(b.getRows() - 1, row + 1);
		int endJ = Math.min(b.getCols() - 1, col + 1);

		for (int i = startI; i <= endI; i++) {
			for (int j = startJ; j <= endJ; j++) {
				if ((i != row || j != col)
						&& b.getPosition(i, j) != null
						&& b.getPosition(i, j).getId() != AtaxxRules.OBSTACLE_IDENTIFIER) {
					b.setPosition(i, j, getPiece());
				}
			}
		}
	}

	/**
	 * Generates a move from a string of the form: oriRow oriColumn row column.
	 */
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}

		try {
			int oriRow, oriCol, row, col;
			oriRow = Integer.parseInt(words[0]);
			oriCol = Integer.parseInt(words[1]);
			row = Integer.parseInt(words[2]);
			col = Integer.parseInt(words[3]);
			return createMove(oriRow, oriCol, row, col, p);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	/**
	 * Convenience method for creating a move. Calls the constructor.
	 * 
	 * @param oriRow
	 *            the initial position row.
	 * @param oriCol
	 *            the initial position column.
	 * @param row
	 *            the final position row.
	 * @param col
	 *            the final position column.
	 * @param p
	 *            the piece corresponding to the player which is associated with
	 *            this movement.
	 * @return the moves.
	 */
	protected GameMove createMove(int originalRow, int originalCol, int row,
			int col, Piece p) {
		return new AtaxxMove(originalRow, originalCol, row, col, p);
	}

	@Override
	/**
	 * Returns the help string.
	 */
	public String help() {
		return "'oriRow oriColumn row column', to place the piece from the oriPosition at the corresponding position.";
	}

	@Override
	public String toString() {
		if (getPiece() == null) {
			return help();
		} else {
			return "Place a piece '" + getPiece() + "' at (" + row + "," + col
					+ ")";
		}
	}

}
