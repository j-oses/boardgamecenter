package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;

import java.util.List;

/**
 * Created by Álvaro on 06/05/2016.
 */
public class ServerController extends VisualController {
	private GameServer server;

	public ServerController(Game game, List<Piece> pieces, Piece owner) {
		super(game, pieces, owner);
	}


}
