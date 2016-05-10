package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxFactoryExt;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class AtaxxFactoryExtExt extends AtaxxFactoryExt {
	public AtaxxFactoryExtExt(Integer dim, Integer obstacles) {
		super(dim, obstacles);
	}

	public AtaxxFactoryExtExt(Integer obstacles) {
		super(obstacles);
	}

	public AtaxxFactoryExtExt() {
		super();
	}

	@Override
	public GameRules gameRules() {
		return new AtaxxRulesExt(dim, obstacles);
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		return new AIPlayer(alg);
	}
}
