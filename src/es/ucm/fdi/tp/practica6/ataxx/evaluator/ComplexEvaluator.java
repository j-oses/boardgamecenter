package es.ucm.fdi.tp.practica6.ataxx.evaluator;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Álvaro on 12/05/2016.
 */
public class ComplexEvaluator extends AtaxxEvaluator {
	private double pieceCountWeight;
	private double proximityWeight;
	private double holesWeight;

	public ComplexEvaluator() {
		this(0.2, 0.5);
	}

	public ComplexEvaluator(double pieceCountWeight, double proximityWeight) {
		if (pieceCountWeight < 0.0 || proximityWeight < 0.0) {
			throw new IllegalArgumentException("The weights must be positive");
		} else if ((pieceCountWeight + proximityWeight) > 1.0) {
			throw new IllegalArgumentException("The weights sum cannot be greater than 1");
		}

		this.pieceCountWeight = pieceCountWeight;
		this.proximityWeight = proximityWeight;
		this.holesWeight = 1.0 - pieceCountWeight - proximityWeight;
	}

	public ComplexEvaluator(Pair<Double, Double> weights) {
		this(weights.getFirst(), weights.getSecond());
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		// SEGUNDO
		// If the board is full, we don't want to use the algorithm.
		if (board.isFull()) {
			if (board.getPieceCount(p) > (board.getCols() * board.getRows() / 2.0)) {
				return 1;
			} else {
				return -1;
			}
		}

		// Takes into account different factors and makes a weighted average to generate the score.
		double numPiecesEval;
		double proximityEval;    // We want to measure how close the pieces are. The closer, the better.
		double numHolesEval;    // The more "holes" (empty spaces surrounded by allies), the worse. Count "almost holes" too.

		double pieceProximityTotalScore = 0.0;
		int totalAllies = (board.getPieceCount(p) != null) ? board.getPieceCount(p) : 0;
		int total = totalPieces(board, pieces);
		int totalEnemies = total - totalAllies;
		int allyHoles = 0;
		int enemyHoles = 0;
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece center = board.getPosition(i, j);
				HashMap<Piece, Integer> surrounding = piecesSurrounding(board, i, j);

				if (center == null) {
					for (Piece piece : pieces) {
						if (surrounding.get(piece) != null && surrounding.get(piece) >= 7) {
							if (piece.equals(p)) {
								allyHoles++;
							} else {
								enemyHoles++;
							}
							break;
						}
					}
				} else if (center.equals(p)) {
					if (surrounding.get(p) != null) {
						pieceProximityTotalScore += surrounding.get(p) * 0.125;
					}
				}
			}
		}

		// Checks if the ally or the enemy may lost in a turn
		if (allyHoles > 0 && totalAllies < 8) {
			return -1;
		} else if (enemyHoles > 0 && totalEnemies < 8) {
			return 1;
		}

		numPiecesEval = (double)totalAllies / (double)total;
		proximityEval = pieceProximityTotalScore / (double) totalAllies;
		numHolesEval = ((double) enemyHoles / ((double) totalEnemies / 7.0)) - (double) allyHoles / ((double) totalAllies / 7.0);
		numHolesEval = Math.max(numHolesEval, -1);
		numHolesEval = Math.min(numHolesEval, 1);

		return pieceCountWeight * numPiecesEval + proximityWeight * proximityEval + holesWeight * numHolesEval;
	}

	@Override
	public String toString() {
		return "Ataxx complex evaluator with parameters (" + pieceCountWeight + ", " + proximityWeight + ", "
				+ holesWeight + ")";
	}
}
