package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.control.VisualController;
import es.ucm.fdi.tp.practica5.views.BoardJPanel;
import es.ucm.fdi.tp.practica5.views.FiniteRectBoardJPanel;

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

	public AtaxxFactoryExt(Integer dim, Integer obstacles) {
		super(dim, obstacles);
	}

	public AtaxxFactoryExt(Integer obstacles) {
		super(obstacles);
	}

	public AtaxxFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
								Piece viewPiece, Player randPlayer, Player aiPlayer) {
		VisualController visualCtrl = (VisualController) ctrl;
		BoardJPanel boardPanel = new FiniteRectBoardJPanel();
		boardPanel.setMoveGenerator(new AtaxxMoveGenerator(visualCtrl));
		visualCtrl.addGameWindowForPieces(boardPanel, randPlayer, aiPlayer,
				viewPiece, game);
	}
}
