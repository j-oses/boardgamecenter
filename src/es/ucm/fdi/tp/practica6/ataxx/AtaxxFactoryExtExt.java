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
	AtaxxRulesExt.AILevel level;
	public AtaxxFactoryExtExt(Integer dim, Integer obstacles, AtaxxRulesExt.AILevel level) {
		super(dim, obstacles);
		this.level = level;
	}

	public AtaxxFactoryExtExt(Integer dim, Integer obstacles) {
		this(dim, obstacles, AtaxxRulesExt.AILevel.REALLY_INTELLIGENT);
	}

	public AtaxxFactoryExtExt(Integer obstacles) {
		super(obstacles);
		level = AtaxxRulesExt.AILevel.REALLY_INTELLIGENT;
	}

	public AtaxxFactoryExtExt() {
		super();
		level = AtaxxRulesExt.AILevel.REALLY_INTELLIGENT;
	}

	@Override
	public GameRules gameRules() {
		return new AtaxxRulesExt(dim, obstacles, level);
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		return new AIPlayer(alg);
	}
}
