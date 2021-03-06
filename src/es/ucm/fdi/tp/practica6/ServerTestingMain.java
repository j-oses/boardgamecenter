package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExt;
import es.ucm.fdi.tp.practica6.control.GameServer;

import java.util.ArrayList;
import java.util.List;

/**
 * This main class is used to create a server with testing purposes.
 */
public class ServerTestingMain {
	public static void main(String args[]) {
		GameFactory factory = new AtaxxFactoryExt();
		AIAlgorithm algorithm = new MinMax(3);
		Game g = new Game(factory.gameRules());
		List<Piece> pieces = new ArrayList<>();

		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		// pieces.add(new Piece("A"));

		Controller v = new Controller(g, pieces);

		// here we set appropriate game-based board and option-based settings
		GameServer server = new GameServer(v, pieces, factory, algorithm, 2020, 2000);
		g.addObserver(server);
		server.start();
	}
}
