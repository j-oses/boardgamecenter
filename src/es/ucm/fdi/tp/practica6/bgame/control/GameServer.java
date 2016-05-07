package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.net.AbstractServer;
import es.ucm.fdi.tp.practica6.net.NotificationMessage;
import es.ucm.fdi.tp.practica6.net.ObjectEndpoint;
import es.ucm.fdi.tp.practica6.net.SocketEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Álvaro on 06/05/2016.
 */
public abstract class GameServer extends AbstractServer implements GameObserver {
	private static final Logger log = Logger.getLogger(GameServer.class.getSimpleName());

	private ArrayList<SocketEndpoint> endpoints;
	private int maxConnections;

	public GameServer(int port, int timeout, int maxConnections) {
		super(port, timeout);

		this.maxConnections = maxConnections;
		this.endpoints = new ArrayList<>();
	}

	// ABSTRACT SERVER METHODS
	@Override
	protected SocketEndpoint createEndpoint(String name) {
		return new ObjectEndpoint(name) {
			@Override
			public void connectionEstablished() {
				endpoints.add(this);

			}

			@Override
			public void dataReceived(Object data) {
				try {
					Command command = (Command) data;
				} catch (ClassCastException e) {
					log.warning("The server received an object which is not a command: " + e);
				}
			}
		};
	}

	// GAME OBSERVER METHODS
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {

	}

	@Override
	public void onGameOver(Board board, Game.State state, Piece winner) {

	}

	@Override
	public void onMoveStart(Board board, Piece turn) {

	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {

	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {

	}

	@Override
	public void onError(String msg) {

	}

	private void notifyEndpoints(NotificationMessage message) {
		for (SocketEndpoint endpoint: endpoints) {
			endpoint.sendData(message);
		}
	}

	private void sendStartupInfoToEndpoint(SocketEndpoint endpoint) {
		Object info = startupInfoForEndpoint(endpoints.size());
		endpoint.sendData(info);
	}

	protected abstract Object startupInfoForEndpoint(int index);
}
