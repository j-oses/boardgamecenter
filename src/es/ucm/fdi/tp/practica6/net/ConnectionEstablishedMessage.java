package es.ucm.fdi.tp.practica6.net;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.io.Serializable;

/**
 * Created by Álvaro on 07/05/2016.
 */
public class ConnectionEstablishedMessage implements Serializable {
	private Piece piece;
	private GameFactory factory;

	public ConnectionEstablishedMessage(Piece piece, GameFactory factory) {
		this.piece = piece;
		this.factory = factory;
	}
	public Piece getPiece(){
		return piece;
	}
	public GameFactory getGameFactory(){
		return factory;
	}
	public void createSwingView(Game game, Controller controller, AIAlgorithm aiAlgorithm) {
		factory.createSwingView(game, controller, piece, factory.createRandomPlayer(), factory.createAIPlayer(aiAlgorithm));
	}
}
