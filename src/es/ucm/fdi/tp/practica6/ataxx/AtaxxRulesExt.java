package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxRules;

import java.util.List;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class AtaxxRulesExt extends AtaxxRules {
	public AtaxxRulesExt(int dim) {
		super(dim);
	}

	public AtaxxRulesExt(int dim, int obstacles) {
		super(dim, obstacles);
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		return mildlyIntelligentEvaluate(board, pieces, turn, p);
	}

	private double mildlyIntelligentEvaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		int total = 0;
		int pCount = (board.getPieceCount(p) != null) ? board.getPieceCount(p) : 0 ;
		for (Piece piece: pieces) {
			Integer count = board.getPieceCount(piece);	// Integer, just in case it is null
			total += (count != null) ? count : 0;
		}

		// We normalize the value between 0 and 2, then subtract 1 for having it in a -1,1 range
		if (total != 0) {
			return ((double) pCount / (double) total) * 2.0 - 1.0;
		} else {
			return 0;
		}
	}

	private double reallyIntelligentEvaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		// FIXME: not implemented yet
		return 0;
	}
}
