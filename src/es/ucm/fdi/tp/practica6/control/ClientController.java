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

	public interface StoppingListener {
		void gameStopped();
	}

	private MoveMaker moveMaker;
	private StoppingListener stoppingListener;

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
