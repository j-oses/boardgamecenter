
package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.practica6.net.*;
import es.ucm.fdi.tp.practica6.ui.ServerWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Álvaro on 06/05/2016.
 */
public class GameServer extends AbstractServer implements GameObserver, ServerWindow.ServerChangesListener {

	private static final Logger log = Logger.getLogger(GameServer.class.getSimpleName());

	private ServerWindow serverWindow;
	private Controller controller;

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
		serverWindow.pack();
		serverWindow.setVisible(true);
		serverWindow.addLineToStatus("Waiting for clients...");
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
					String warning = "The server received an object which is not a command: " + e;
					log.warning(warning);
					serverWindow.addLineToStatus(warning);
				} catch (GameError e) {
					String warning = "An error happened when executing a command: " + e.getMessage();
					log.warning(warning);
					serverWindow.addLineToStatus(warning);
				}
			}
		};
	}

	// GAME RELATED METHODS
	private void startGame() {
		controller.start();
		serverWindow.addLineToStatus("Game started!");
	}

	// GAME OBSERVER METHODS
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		notifyEndpoints(new NotificationMessage.GameStart(board, gameDesc, pieces, turn));
	}

	@Override
	public void onGameOver(Board board, Game.State state, Piece winner) {
		notifyEndpoints(new NotificationMessage.GameOver(board, state, winner));

		if (state.equals(Game.State.Won)) {
			serverWindow.addLineToStatus("The game finished with winner '" + winner.getId() + "'.");
		} else if (state.equals(Game.State.Draw)) {
			serverWindow.addLineToStatus("The game ended in a draw '" + winner.getId() + "'.");
		}
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
		if (endpoints.size() < maxConnections) {
			endpoints.add(endpoint);
			sendStartupInfoToEndpoint(endpoint);
			serverWindow.addLineToStatus("New client connected (" + endpoints.size() + "/" + maxConnections
					+ "). Assigned piece '" + pieces.get(endpoints.size() - 1) + "'.");
		}

		// Size has changed
		if (endpoints.size() >= maxConnections) {
			log.info("Stop accepting connections. Starting the game...");
			serverWindow.addLineToStatus("Expected number of clients connected. Starting the game.");
			stop();
			startGame();
		}
	}

	private void sendStartupInfoToEndpoint(SocketEndpoint endpoint) {
		ConnectionEstablishedMessage message = new ConnectionEstablishedMessage(pieces.get(endpoints.size() - 1),
				pieces, factory);
		endpoint.sendData(message);
	}

	@Override
	public void onQuitButtonPressed() {
		serverWindow.addLineToStatus("Stopping games and closing...");
		controller.stop();
		serverWindow.closeWindow();
	}
}


