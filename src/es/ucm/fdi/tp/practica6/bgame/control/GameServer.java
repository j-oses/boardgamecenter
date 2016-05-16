
package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;
import es.ucm.fdi.tp.practica6.net.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Álvaro on 06/05/2016.
 */
public class GameServer extends AbstractServer implements GameObserver {
	private static final Logger log = Logger.getLogger(GameServer.class.getSimpleName());

	private VisualController controller;

	private List<SocketEndpoint> endpoints;
	private int maxConnections;
	private List<Piece> pieces;
	private GameFactory factory;

	public GameServer(VisualController controller, List<Piece> pieces, GameFactory factory, int port, int timeout) {
		super(port, timeout);

		this.controller = controller;
		this.maxConnections = pieces.size() - 1;	// The first piece is the server one
		this.endpoints = new ArrayList<>();
		this.pieces = pieces;
		this.factory = factory;
	}

	// ABSTRACT SERVER METHODS
	@Override
	protected SocketEndpoint createEndpoint(String name) {
		return new ObjectEndpoint(name) {
			@Override
			public void connectionEstablished() {
				connectionEstablishedToEndpoint(this);
			}

			@Override
			public void dataReceived(Object data) {
				try {
					Command command = (Command) data;
					command.execute(controller);
				} catch (ClassCastException e) {
					log.warning("The server received an object which is not a command: " + e);
				}
			}
		};
	}

	// GAME RELATED METHODS
	private void startGame() {
		controller.start();
	}

	// GAME OBSERVER METHODS
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		notifyEndpoints(new NotificationMessage.GameStart(board, gameDesc, pieces, turn));
	}

	@Override
	public void onGameOver(Board board, Game.State state, Piece winner) {
		notifyEndpoints(new NotificationMessage.GameOver(board, state, winner));
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		notifyEndpoints(new NotificationMessage.MoveStart(board, turn));
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		notifyEndpoints(new NotificationMessage.MoveEnd(board, turn, success));
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		notifyEndpoints(new NotificationMessage.ChangeTurn(board, turn));
	}

	@Override
	public void onError(String msg) {
		notifyEndpoints(new NotificationMessage.Error(msg));
	}

	// CONNECTION HELPING METHODS
	private void notifyEndpoints(NotificationMessage message) {
		for (SocketEndpoint endpoint : endpoints) {
			endpoint.sendData(message);
		}
	}

	private void connectionEstablishedToEndpoint(SocketEndpoint endpoint) {
		if (endpoints.size() < pieces.size() - 1) {
			endpoints.add(endpoint);
			sendStartupInfoToEndpoint(endpoint);
		}

		// Size has changed
		if (endpoints.size() >= pieces.size() - 1) {
			// Don't accept more connections and start the game
			log.info("Stop accepting connections. Starting the game");
			stop();
			startGame();
		}
	}

	private void sendStartupInfoToEndpoint(SocketEndpoint endpoint) {
		ConnectionEstablishedMessage message = new ConnectionEstablishedMessage(pieces.get(endpoints.size()), pieces, factory);
		endpoint.sendData(message);
	}
}


