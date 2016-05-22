package es.ucm.fdi.tp.practica6.control;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.util.List;

/**
 * The client controller is a SwingView that only knows that he shouldn't make moves.
 * When a move is generated it passes it to another moveListener, which will typically be
 * the GameClient.
 */
public class ClientController extends Controller {
	public interface MoveMaker {
		void makeMove(Player player);
	}

	private MoveMaker moveMaker;

	public ClientController(Game game, List<Piece> pieces, MoveMaker moveMaker) {
		super(game, pieces);
		this.moveMaker = moveMaker;
	}

	@Override
	public void makeMove(Player p) {
		moveMaker.makeMove(p);
	}
}
