package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;

import java.util.ArrayList;
import java.util.List;

/**
 * The client controller is a VisualController that only knows that he shouldn't make moves.
 * When a move is generated it passes it to another moveListener, which will typically be
 * the GameClient.
 */
public class ClientController extends VisualController {
	public interface MoveMaker {
		void makeMove(Player player);
	}

	private MoveMaker moveMaker;

	public ClientController(Game game, List<Piece> pieces, Piece owner, MoveMaker moveMaker) {
		super(game, pieces, owner);
		this.moveMaker = moveMaker;
	}

	/**
	 * We need to get every internal component which is a game observer (including the controller itself).
	 * @return the list of components.
	 */
	public List<GameObserver> getInternalObservers() {
		List<GameObserver> observers = new ArrayList<>();
		observers.add(this);
		if (window != null) {
			observers.add(window);
		}
		return observers;
	}

	@Override
	public void makeMove(Player p) {
		moveMaker.makeMove(p);
	}
}
