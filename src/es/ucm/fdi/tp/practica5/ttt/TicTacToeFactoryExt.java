package es.ucm.fdi.tp.practica5.ttt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.ttt.TicTacToeFactory;
import es.ucm.fdi.tp.practica5.connectn.ConnectNMoveGenerator;
import es.ucm.fdi.tp.practica5.views.SwingView;
import es.ucm.fdi.tp.practica5.views.BoardJPanel;
import es.ucm.fdi.tp.practica5.views.FiniteRectBoardJPanel;

/**
 * A class which provides the functionality intended for
 * {@link TicTacToeFactory}. This is necessary due to not having permission to
 * change the basecode package.
 * 
 * @author �lvaro
 *
 */
public class TicTacToeFactoryExt extends TicTacToeFactory {
	private static final long serialVersionUID = 1L;

	public TicTacToeFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer) {
		SwingView view = new SwingView(ctrl, viewPiece);
		BoardJPanel boardPanel = new FiniteRectBoardJPanel();
		boardPanel.setMoveGenerator(new ConnectNMoveGenerator(view));
		view.addGameWindowForPieces(boardPanel, randPlayer, aiPlayer,
				viewPiece, game);
	}
}
