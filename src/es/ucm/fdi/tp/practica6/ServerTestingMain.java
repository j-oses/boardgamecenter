package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxFactoryExt;
import es.ucm.fdi.tp.practica6.bgame.control.GameServer;
import es.ucm.fdi.tp.practica6.bgame.control.ServerController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class ServerTestingMain {
	public static void main(String args[]) {
		GameFactory factory = new AtaxxFactoryExt();
		AIAlgorithm algorithm = new MinMax();
		Game g = new Game(factory.gameRules());
		List<Piece> pieces = new ArrayList<Piece>();

		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		pieces.add(new Piece("A"));

		ServerController v = new ServerController(g, pieces, null);
		// here we set appropriate game-based board and option-based
		// settings
		factory.createSwingView(g, v, null,
				factory.createRandomPlayer(),
				factory.createAIPlayer(algorithm));
		GameServer server = new GameServer(v, pieces, factory, 2020, 2000);
		server.start();
	}
}
