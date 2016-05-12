package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxRules;

import java.util.List;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class AtaxxRulesExt extends AtaxxRules {
	public enum AILevel {
		MILDLY_INTELLIGENT, REALLY_INTELLIGENT
	}

	private AILevel level;

	public AtaxxRulesExt(int dim) {
		this(dim, AILevel.MILDLY_INTELLIGENT);
	}

	public AtaxxRulesExt(int dim, AILevel level) {
		super(dim);
		this.level = level;
	}

	public AtaxxRulesExt(int dim, int obstacles) {
		this(dim, obstacles, AILevel.MILDLY_INTELLIGENT);
	}

	public AtaxxRulesExt(int dim, int obstacles, AILevel level) {
		super(dim, obstacles);
		this.level = level;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		switch (level) {
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
	 * (-1). To be used in the automatic players. Testing yielded a surprising
	 * 1.000 victories in 1.000 games against the mildly intelligent player.
	 * However, testing against the random player yields 839 victories in 1.000
	 * games (The possible reasons for this are explained in Leeme.txt). Both
	 * tests were done in a 5x5 board, with 2 players.
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
		// Takes into account different factors and makes a weighted average to generate the score.
		double numPiecesEval = mildlyIntelligentEvaluate(board, pieces, turn, p);
		double proximityEval;	// We want to measure how close the pieces are. The closer, the better.
		double numHolesEval;		// The more "holes" (empty spaces surrounded by allies), the worse. Counts
									// "almost holes" too.

		double pieceProximityTotalScore = 0.0;
		int pCount = (board.getPieceCount(p) != null) ? board.getPieceCount(p) : 0;
		int numHoles = 0;
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (board.getPosition(i, j) == null) {
					if (numberOfPiecesSurrounding(board, turn, i, j) > 6) {
						numHoles++;
					}
				} else if (turn.equals(board.getPosition(i, j))) {
					int number = 0;
					int startI = Math.max(0, i - 1);
					int startJ = Math.max(0, j - 1);
					int endI = Math.min(board.getRows() - 1, i + 1);
					int endJ = Math.min(board.getCols() - 1, j + 1);

					for (int k = startI; k <= endI; k++) {
						for (int l = startJ; l <= endJ; l++) {
							if ((k != i || l != j) && !turn.equals(board.getPosition(k, l))) {
								pieceProximityTotalScore += 0.125;
							}
						}
					}
				}
			}
		}

		proximityEval = pieceProximityTotalScore / (double)pCount;
		numHolesEval = Math.max(-1, -((double)numHoles * 10 / (double)(board.getCols() * board.getCols())));

		return 0.4 * numPiecesEval + 0.3 * proximityEval + 0.3 * numHolesEval;
	}

	private int numberOfPiecesSurrounding(Board board, Piece surroundingType, int row, int col) {
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
}
