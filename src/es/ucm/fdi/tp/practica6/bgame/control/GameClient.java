package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.practica5.bgame.model.MoveGenerator;
import es.ucm.fdi.tp.practica6.net.ConnectionEstablishedMessage;
import es.ucm.fdi.tp.practica6.net.NotificationMessage;
import es.ucm.fdi.tp.practica6.net.SocketEndpoint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient implements SocketEndpoint, MoveGenerator.MoveListener {

	private static final Logger log = Logger.getLogger(GameClient.class.getSimpleName());

	private ClientController clientController;
	private ArrayList<GameObserver> observers;
	private AIAlgorithm localAlgorithm;


	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;
	protected volatile boolean stopped;
	protected String name;

	public GameClient(String name) {
		this.name = name;
	}

	public GameClient(AIAlgorithm localAlgorithm) {
		this.name = "Client";
		this.localAlgorithm = localAlgorithm;
		this.observers = new ArrayList<>();
	}

	@Override
	public void didGenerateMove(GameMove move) {
		sendData(move);
	}

	//TODO SocketEndpoint Methods
	@Override
	public void start(final Socket socket, final int timeout) {
		try {
			socket.setSoTimeout(timeout);
			oos = new ObjectOutputStream(socket.getOutputStream());

			new Thread(new Runnable() {
				public void run() {
					try {
						ois = new ObjectInputStream(socket.getInputStream());

					} catch (IOException e) {
						log.log(Level.WARNING, "Failed to read: could not create object input stream");
					}
					while (!stopped) {
						try {
							dataReceived(ois.readObject());
						} catch (SocketTimeoutException ste) {
							log.log(Level.FINE, "Failed to read; will retry");
						} catch (IOException | ClassNotFoundException se) {
							log.log(Level.WARNING, "Failed to read: bad serialization");
							stop();
						}
					}
					log.log(Level.INFO, "Client exiting gracefully");
				}
			}, name + "Listener").start();

			connectionEstablished();
		} catch (IOException e) {
			log.log(Level.WARNING, "Error while handling client connection", e);
		}
	}

	@Override
	public void connectionEstablished() {
	}

	@Override
	public void dataReceived(Object data) {

		if (data instanceof ConnectionEstablishedMessage) {

			Game g = new Game(((ConnectionEstablishedMessage) data).getGameFactory().gameRules());
			clientController = new ClientController(g, g.getPlayersPieces(), ((ConnectionEstablishedMessage) data).getPiece());
			g.addObserver(clientController);
			observers.add(clientController);
			((ConnectionEstablishedMessage) data).createSwingView(g, clientController, localAlgorithm);

		} else if (data instanceof NotificationMessage) {
			((NotificationMessage) data).notifyObservers(observers);
		}
	}

	@Override
	public void sendData(Object data) {
		try {
			oos.writeObject(data);
			oos.flush();
			oos.reset();
		} catch (SocketTimeoutException ste) {
			log.log(Level.INFO, "Failed to write; target must be full!");
		} catch (IOException ioe) {
			log.log(Level.WARNING, "Failed to write: bad serialization");
		}
	}

	@Override
	public void stop() {
		stopped = true;
	}
}
