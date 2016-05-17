
package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.net.*;
import es.ucm.fdi.tp.practica6.ui.ServerWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Álvaro on 06/05/2016.
 */
public class GameServer extends AbstractServer implements GameObserver,ServerWindow.ServerChangesListener {

	private static final Logger log = Logger.getLogger(GameServer.class.getSimpleName());

<<<<<<< HEAD
	private VisualController controller;
	private ServerWindow serverWindow;
=======
	private Controller controller;

>>>>>>> origin/master
	private List<SocketEndpoint> endpoints;
	private int maxConnections;
	private List<Piece> pieces;
	private GameFactory factory;

	public GameServer(Controller controller, List<Piece> pieces, GameFactory factory, int port, int timeout) {
		super(port, timeout);

		this.controller = controller;
		this.maxConnections = pieces.size();
		this.endpoints = new ArrayList<>();
		this.pieces = pieces;
		this.factory = factory;
		this.serverWindow = new ServerWindow("Server Window", this);
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
		if (endpoints.size() < pieces.size()) {
			endpoints.add(endpoint);
			sendStartupInfoToEndpoint(endpoint);
		}

		// Size has changed
		if (endpoints.size() >= pieces.size()) {
			// Don't accept more connections and start the game
			log.info("Stop accepting connections. Starting the game");
			stop();
			startGame();
		}
	}

	private void sendStartupInfoToEndpoint(SocketEndpoint endpoint) {
		ConnectionEstablishedMessage message = new ConnectionEstablishedMessage(pieces.get(endpoints.size() - 1), pieces, factory);
		endpoint.sendData(message);
	}

	@Override
	public void onQuitButtonPressed() {
		//TODO here notify endpoints or some fancy shiet, up 2 u
		serverWindow.closeWindow();
	}
}


