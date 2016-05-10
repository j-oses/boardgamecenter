package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxRules;

import java.util.List;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class AtaxxRulesExt extends AtaxxRules {
	public enum EvaluationProwess {
		MILDLY_INTELLIGENT, REALLY_INTELLIGENT
	}

	private EvaluationProwess prowess;

	public AtaxxRulesExt(int dim) {
		this(dim, EvaluationProwess.MILDLY_INTELLIGENT);
	}

	public AtaxxRulesExt(int dim, EvaluationProwess prowess) {
		super(dim);
		this.prowess = prowess;
	}

	public AtaxxRulesExt(int dim, int obstacles) {
		this(dim, obstacles, EvaluationProwess.MILDLY_INTELLIGENT);
	}

	public AtaxxRulesExt(int dim, int obstacles, EvaluationProwess prowess) {
		super(dim, obstacles);
		this.prowess = prowess;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		switch (prowess) {
			case REALLY_INTELLIGENT:
				return reallyIntelligentEvaluate(board, pieces, turn, p);
			default: // MILDLY_INTELLIGENT
				return mildlyIntelligentEvaluate(board, pieces, turn, p);
		}
	}

	/**
	 * Evaluates how close the player {@link p} is to winning (1) or losing
	 * (-1). To be used in the automatic players. Its called mildly intelligent
	 * because the evaluation function only calculates the number of pieces compared
	 * with the total pieces. A simple random sample of size 1.000 yields 615 wins of
	 * the mildly intelligent player against a single random player, on a 5x5 board.
	 * <p>
	 * The method must be called with a board that is InPlay, it cannot be
	 * called with a finished game.
	 * <p>
	 *
	 * @param board  The current board of the game.
	 * @param pieces The list of pieces involved in the game (each correspond to a
	 *               player).
	 * @param turn   The piece to be played next.
	 * @param p      The piece w.r.t which we want to make the evaluation
	 * @return How close the player {@link turn} is to winning (1) or losing
	 * (-1). The value 0 is neutral.
	 */
	private double mildlyIntelligentEvaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		int total = 0;
		int pCount = (board.getPieceCount(p) != null) ? board.getPieceCount(p) : 0;
		for (Piece piece : pieces) {
			Integer count = board.getPieceCount(piece);    // Integer, just in case it is null
			total += (count != null) ? count : 0;
		}

		// We normalize the value between 0 and 2, then subtract 1 for having it in a -1,1 range
		if (total != 0) {
			return ((double) pCount / (double) total) * 2.0 - 1.0;
		} else {
			return 0;
		}
	}

	/**
	 * Evaluates how close the player {@link p} is to winning (1) or losing
	 * (-1). To be used in the automatic players. We hope it can win against
	 * a mildly intelligent AI, at least, but no tests have been made (yet).
	 * <p>
	 * The method must be called with a board that is InPlay, it cannot be
	 * called with a finished game.
	 * <p>
	 *
	 * @param board  The current board of the game.
	 * @param pieces The list of pieces involved in the game (each correspond to a
	 *               player).
	 * @param turn   The piece to be played next.
	 * @param p      The piece w.r.t which we want to make the evaluation
	 * @return How close the player {@link turn} is to winning (1) or losing
	 * (-1). The value 0 is neutral.
	 */
	private double reallyIntelligentEvaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		// FIXME: not implemented yet
		return 0;
	}
}
