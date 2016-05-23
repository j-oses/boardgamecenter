package es.ucm.fdi.tp.practica6.ataxx.evaluator;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.util.HashMap;
import java.util.List;

/**
 * A class which evaluates a game state (board, pieces, turn, piece) for the minmax algorithm. It also provides some
 * useful methods that subclasses may want to use for their evaluations.
 */
public abstract class AtaxxEvaluator {
	/**
	 * Evaluates how close the player {@link p} is to winning (1) or losing
	 * (-1). To be used in the automatic players. This method should at least
	 * return the neutral value (0) if there is no actual underlying algorithm
	 * that evaluates.
	 * <p>
	 * <p>
	 * The method must be called with a board that is InPlay, it cannot be
	 * called with a finished game.
	 *
	 * @param board  The current board of the game.
	 * @param pieces The list of pieces involved in the game (each correspond to a
	 *               player).
	 * @param turn   The piece to be played next.
	 * @param p      The piece w.r.t which we want to make the evaluation
	 * @return How close the player {@link turn} is to winning (1) or losing
	 * (-1). The value 0 is neutral.
	 */
	public abstract double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p);

	// IMPLEMENTATION OF SOME USEFUL METHODS FOR SUBCLASSES

	/**
	 * Calculates the number of pieces of a type surrounding the position given.
	 * @param board the board.
	 * @param surroundingType the type whose pieces will be counted.
	 * @param row the row of the position.
	 * @param col the column of the position.
	 * @return the number of pieces surrounding.
	 */
	protected static int numberOfPiecesSurrounding(Board board, Piece surroundingType, int row, int col) {
		Integer surrounding = piecesSurrounding(board, row, col).get(surroundingType);
		return surrounding != null ? surrounding : 0;
	}

	/**
	 * Calculates the number of pieces surrounding the position given, for each player.
	 * @param board the board.
	 * @param row the row of the position.
	 * @param col the column of the position.
	 * @return a hashmap with pieces as keys and number of pieces in the board as values.
	 */
	protected static HashMap<Piece, Integer> piecesSurrounding(Board board, int row, int col) {
		HashMap<Piece, Integer> result = new HashMap<>();
		int startI = Math.max(0, row - 1);
		int startJ = Math.max(0, col - 1);
		int endI = Math.min(board.getRows() - 1, row + 1);
		int endJ = Math.min(board.getCols() - 1, col + 1);

		for (int i = startI; i <= endI; i++) {
			for (int j = startJ; j <= endJ; j++) {
				Piece p = board.getPosition(row, col);
				if ((i != row || j != col) && p != null) {
					int current = result.get(p) != null ? result.get(p) : 0;
					result.put(p, current + 1);
				}
			}
		}
		return result;
	}

	/**
	 * Counts the total pieces on the board.
	 * @param board the board.
	 * @param pieces the list of possible pieces.
	 * @return the total number of pieces.
	 */
	protected static int totalPieces(Board board, List<Piece> pieces) {
		int total = 0;
		for (Piece piece : pieces) {
			Integer count = board.getPieceCount(piece);    // Integer, just in case it is null
			total += (count != null) ? count : 0;
		}
		return total;
	}
}
