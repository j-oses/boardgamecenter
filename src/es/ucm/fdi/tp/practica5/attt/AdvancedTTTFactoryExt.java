package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.control.VisualController;
import es.ucm.fdi.tp.practica5.views.BoardJPanel;
import es.ucm.fdi.tp.practica5.views.FiniteRectBoardJPanel;

/**
 * A class which provides the functionality intended for
 * {@link AdvancedTTTFactory}. This is necessary due to not having permission to
 * change the basecode package.
 * 
 * @author �lvaro
 *
 */
public class AdvancedTTTFactoryExt extends AdvancedTTTFactory {
	private static final long serialVersionUID = 1L;

	public AdvancedTTTFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer) {
		VisualController visualCtrl = (VisualController) ctrl;
		BoardJPanel boardPanel = new FiniteRectBoardJPanel();
		boardPanel.setMoveGenerator(new AdvancedTTTMoveGenerator(visualCtrl));
		visualCtrl.addGameWindowForPieces(boardPanel, randPlayer, aiPlayer,
				viewPiece);
	}
}
