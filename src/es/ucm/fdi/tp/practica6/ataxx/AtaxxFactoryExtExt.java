package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxFactoryExt;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.AtaxxEvaluator;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class AtaxxFactoryExtExt extends AtaxxFactoryExt {
	AtaxxEvaluator evaluator;

	public AtaxxFactoryExtExt(Integer dim, Integer obstacles) {
		super(dim, obstacles);
	}

	public AtaxxFactoryExtExt(Integer obstacles) {
		super(obstacles);
	}

	public AtaxxFactoryExtExt() {
		super();
	}

	public void setEvaluator(AtaxxEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public GameRules gameRules() {
		if (evaluator != null) {
			return new AtaxxRulesExt(dim, obstacles, evaluator);
		} else {
			return new AtaxxRulesExt(dim, obstacles);
		}
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		return new AIPlayer(alg);
	}
}
