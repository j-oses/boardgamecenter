package es.ucm.fdi.tp.practica6.net;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Álvaro on 07/05/2016.
 */
public class ConnectionEstablishedMessage implements Serializable {
	private Piece piece;
	private List<Piece> pieces;
	private GameFactory factory;
	private AIAlgorithm algorithm;

	public ConnectionEstablishedMessage(Piece piece, List<Piece> pieces, GameFactory factory, AIAlgorithm algorithm) {
		this.piece = piece;
		this.factory = factory;
		this.pieces = pieces;
		this.algorithm = algorithm;
	}

	public List<Piece> getPieces() {
		return pieces;
	}

	public Piece getPiece() {
		return piece;
	}

	public GameFactory getGameFactory() {
		return factory;
	}

	public void createSwingView(Observable<GameObserver> game, Controller controller) {
		factory.createSwingView(game, controller, piece, factory.createRandomPlayer(), factory.createAIPlayer(algorithm));
	}
}
