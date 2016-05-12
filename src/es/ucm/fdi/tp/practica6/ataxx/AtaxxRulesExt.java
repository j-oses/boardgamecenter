package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxRules;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.AtaxxEvaluator;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;

import java.util.List;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class AtaxxRulesExt extends AtaxxRules {
	private AtaxxEvaluator evaluator;

	public AtaxxRulesExt(int dim) {
		this(dim, new ComplexEvaluator());
	}

	public AtaxxRulesExt(int dim, AtaxxEvaluator evaluator) {
		super(dim);
		this.evaluator = evaluator;
	}

	public AtaxxRulesExt(int dim, int obstacles) {
		this(dim, obstacles, new ComplexEvaluator());
	}

	public AtaxxRulesExt(int dim, int obstacles, AtaxxEvaluator evaluator) {
		super(dim, obstacles);
		this.evaluator = evaluator;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		return evaluator.evaluate(board, pieces, turn, p);
	}
}
