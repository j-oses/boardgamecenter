package es.ucm.fdi.tp.practica6.ataxx.evaluator;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Álvaro on 12/05/2016.
 */
public abstract class AtaxxEvaluator {
	public abstract double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p);

	// IMPLEMENTATION OF SOME USEFUL METHODS FOR SUBCLASSES
	protected static int numberOfPiecesSurrounding(Board board, Piece surroundingType, int row, int col) {
		int number = 0;
		int startI = Math.max(0, row - 1);
		int startJ = Math.max(0, col - 1);
		int endI = Math.min(board.getRows() - 1, row + 1);
		int endJ = Math.min(board.getCols() - 1, col + 1);

		for (int i = startI; i <= endI; i++) {
			for (int j = startJ; j <= endJ; j++) {
				if ((i != row || j != col) && surroundingType.equals(board.getPosition(i, j))) {
					number++;
				}
			}
		}
		return number;
	}

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

	protected static int totalPieces(Board board, List<Piece> pieces) {
		int total = 0;
		for (Piece piece : pieces) {
			Integer count = board.getPieceCount(piece);    // Integer, just in case it is null
			total += (count != null) ? count : 0;
		}
		return total;
	}
}
