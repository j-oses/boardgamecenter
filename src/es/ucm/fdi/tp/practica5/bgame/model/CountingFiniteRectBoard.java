package es.ucm.fdi.tp.practica5.bgame.model;

import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A CountingFiniteRectBoard implements a FiniteRectBoard which counts the
 * pieces when they are added or removed.
 * 
 * @author Álvaro
 *
 */
public class CountingFiniteRectBoard extends FiniteRectBoard {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param rows
	 * @param cols
	 */
	public CountingFiniteRectBoard(int rows, int cols) {
		super(rows, cols);
	}

	@Override
	/**
	 * Given a certain position on the Board puts a piece of the type given in it 
	 */
	public void setPosition(int row, int col, Piece p) {
		// We may be deleting a piece
		Piece deleted = getPosition(row, col);
		if (deleted != null) {
			decreasePieceCount(deleted);
		}

		// Count the new piece
		if (p != null) {
			increasePieceCount(p);
		}

		super.setPosition(row, col, p);
	}

	/**
	 * Increments the number of pieces
	 * 
	 * @param p
	 */
	private void increasePieceCount(Piece p) {
		Integer count = getPieceCount(p);
		if (count == null) {
			setPieceCount(p, 1);
		} else {
			setPieceCount(p, count + 1);
		}
	}

	/**
	 * Decreses the number of pieces
	 * 
	 * @param p
	 */
	private void decreasePieceCount(Piece p) {
		Integer count = getPieceCount(p);
		if (count != null) {
			if (count > 1) {
				setPieceCount(p, count - 1);
			} else {
				setPieceCount(p, 0);
			}
		}
	}

	/**
	 * Generates a string that represents the board. The symbols used to print
	 * the board are the first characters of the piece identifier.
	 * 
	 * <p>
	 * Genera un string que representa el tablero. El simbolo utilizado para
	 * cada ficha es el primer caracter de su id.
	 * 
	 * @return A string representation of the board.
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
