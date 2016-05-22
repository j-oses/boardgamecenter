package es.ucm.fdi.tp.practica6.control;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.util.List;

/**
 * The client controller is a Controller that only knows that he shouldn't make moves.
 * When a move is generated it passes it to another moveListener, which will typically be
 * the GameClient. The game stop event is also intercepted (to warn the server).
 */
public class ClientController extends Controller {
	/**
	 * To be implemented by the object that uses the intercepted moves.
	 */
	public interface MoveMaker {
		/**
		 * Warns the receiver that a move should have been made.
		 * @param player the player to make the move.
		 */
		void makeMove(Player player);
	}

	/**
	 * To be implemented by the object interested in the game stop events.
	 */
	public interface StoppingListener {
		/**
		 * Warns the receiver that the controller will stop the game.
		 */
		void gameStopped();
	}

	/**
	 * The associated move maker.
	 */
	private MoveMaker moveMaker;

	/**
	 * The associated stopping listener.
	 */
	private StoppingListener stoppingListener;

	/**
	 * Creates a new controller with the necessary objects and the listeners.
	 * @param game the game which will be played. Since most events are intercepted, the game doesn't has lots of
	 *             responsibilities.
	 * @param pieces the pieces of the game.
	 * @param moveMaker the associated move maker.
	 * @param stoppingListener the associated stopping listener.
	 */
	public ClientController(Game game, List<Piece> pieces, MoveMaker moveMaker, StoppingListener stoppingListener) {
		super(game, pieces);
		this.moveMaker = moveMaker;
		this.stoppingListener = stoppingListener;
	}

	@Override
	public void makeMove(Player p) {
		moveMaker.makeMove(p);
	}

	@Override
	public void stop() {
		stoppingListener.gameStopped();
		super.stop();
	}
}
