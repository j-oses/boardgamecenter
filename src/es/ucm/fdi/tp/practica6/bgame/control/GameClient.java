package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.practica5.bgame.control.VisualPlayer;
import es.ucm.fdi.tp.practica5.bgame.model.MoveGenerator;
import es.ucm.fdi.tp.practica6.net.ConnectionEstablishedMessage;
import es.ucm.fdi.tp.practica6.net.NotificationMessage;
import es.ucm.fdi.tp.practica6.net.ObjectEndpoint;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient extends ObjectEndpoint implements MoveGenerator.MoveListener {

	private static final Logger log = Logger.getLogger(GameClient.class.getSimpleName());

	private ClientController clientController;
	private List<GameObserver> observers;
	private AIAlgorithm localAlgorithm;

	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;
	protected volatile boolean stopped;
	protected String name;

	public GameClient(String name) {
		super(name);
	}

	public GameClient(AIAlgorithm localAlgorithm) {
		super("Client");
		this.localAlgorithm = localAlgorithm;
		this.observers = new ArrayList<>();
	}

	@Override
	public void connectionEstablished() {

	}

	@Override
	public void didGenerateMove(GameMove move) {
		PlayCommand command = new PlayCommand(new VisualPlayer(move));
		sendData(command);
	}

	@Override
	public void dataReceived(Object data) {
		if (data instanceof ConnectionEstablishedMessage) {
			ConnectionEstablishedMessage message = (ConnectionEstablishedMessage)data;
			GameRules rules = message.getGameFactory().gameRules();
			Game g = new Game(rules);
			clientController = new ClientController(g, message.getPieces(), message.getPiece(), this);
			message.createSwingView(g, clientController, localAlgorithm);
			this.observers = clientController.getInternalObservers();
		} else if (data instanceof NotificationMessage) {
			((NotificationMessage)data).notifyObservers(observers);
		}
	}
}
