package es.ucm.fdi.tp.practica6.ataxx.evaluator;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.util.List;

/**
 * A evaluator whose evaluation function only returns the number of pieces compared
 * with the total pieces. Testing yields 615 wins in 1.000 games of this algorithm against
 * a single random player, on a 5x5 board.
 */
public class PieceCountingEvaluator extends AtaxxEvaluator {
	public PieceCountingEvaluator() {
		super();
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		int total = totalPieces(board, pieces);
		int pCount = (board.getPieceCount(p) != null) ? board.getPieceCount(p) : 0;

		// We normalize the value between 0 and 2, then subtract 1 for having it in a -1,1 range
		if (total != 0) {
			return ((double) pCount / (double) total) * 2.0 - 1.0;
		} else {
			return 0;
		}
	}
}
