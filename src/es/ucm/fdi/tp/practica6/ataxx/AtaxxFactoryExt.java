package es.ucm.fdi.tp.practica6.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.DummyAIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.AtaxxEvaluator;
import es.ucm.fdi.tp.practica6.views.BoardJPanel;
import es.ucm.fdi.tp.practica6.views.FiniteRectBoardJPanel;
import es.ucm.fdi.tp.practica6.views.SwingView;

/**
 * A class which provides the functionality intended for {@link AtaxxFactory}.
 * This is necessary due to not having permission to change the practica4
 * package.
 *
 * @author Álvaro
 *
 */
public class AtaxxFactoryExt extends AtaxxFactory {
	private static final long serialVersionUID = 1L;

	private AtaxxEvaluator evaluator;

	public AtaxxFactoryExt(Integer dim, Integer obstacles) {
		super(dim, obstacles);
	}

	public AtaxxFactoryExt(Integer obstacles) {
		super(obstacles);
	}

	public AtaxxFactoryExt() {
		super();
	}

	public void setEvaluator(AtaxxEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public GameRules gameRules() {
		if (evaluator != null) {
			return new AtaxxRules(dim, obstacles, evaluator);
		} else {
			return new AtaxxRules(dim, obstacles);
		}
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		if (alg != null) {
			return new AIPlayer(alg);
		} else {
			return new DummyAIPlayer(createRandomPlayer(), 1000);
		}
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
								Piece viewPiece, Player randPlayer, Player aiPlayer) {
		SwingView view = new SwingView(ctrl, viewPiece);
		BoardJPanel boardPanel = new FiniteRectBoardJPanel();
		boardPanel.setMoveGenerator(new AtaxxMoveGenerator(view));
		view.addGameWindowForPieces(boardPanel, randPlayer, aiPlayer,
				viewPiece, game);
	}
}
