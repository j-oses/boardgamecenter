package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.model.*;

import java.util.List;

/**
 * Created by Álvaro on 13/05/2016.
 */
public class EvaluatorAIPlayer extends AIPlayer {
	private GameRules rules;

	public EvaluatorAIPlayer(AIAlgorithm alg, GameRules rules) {
		super(alg);
		this.rules = rules;
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return super.requestMove(p, board, pieces, this.rules);
	}
}
