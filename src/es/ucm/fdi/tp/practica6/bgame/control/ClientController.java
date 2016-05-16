package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;
import es.ucm.fdi.tp.practica5.bgame.model.MoveGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * The client controller is a VisualController that only knows that he shouldn't make moves.
 * When a move is generated it passes it to another moveListener, which will typically be
 * the GameClient.
 */
public class ClientController extends VisualController {
	private MoveGenerator.MoveListener moveListener;

	public ClientController(Game game, List<Piece> pieces, Piece owner, MoveGenerator.MoveListener moveListener) {
		super(game, pieces, owner);
		this.moveListener = moveListener;
	}

	@Override
	public void didGenerateMove(GameMove move) {
		// It's not strictly necessary to check for current player, but it's
		// checked just in case
		if (isTurn && currentPlayer == null) {
			moveListener.didGenerateMove(move);
		}
	}

	/**
	 * We need to get every internal component which is a game observer (including the controller itself).
	 * @return
	 */
	public List<GameObserver> getInternalObservers() {
		List<GameObserver> observers = new ArrayList<>();
		observers.add(this);
		if (window != null) {
			observers.add(window);
		}
		return observers;
	}
}
