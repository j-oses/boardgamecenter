package es.ucm.fdi.tp.practica6.control;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.practica6.model.ProxyObservable;
import es.ucm.fdi.tp.practica6.net.ConnectionEstablishedMessage;
import es.ucm.fdi.tp.practica6.net.NotificationMessage;
import es.ucm.fdi.tp.practica6.net.ObjectEndpoint;

import java.util.logging.Logger;


/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient extends ObjectEndpoint implements ClientController.MoveMaker {
	private static final Logger log = Logger.getLogger(GameClient.class.getSimpleName());

	private ProxyObservable proxyGame;
	private AIAlgorithm localAlgorithm;

	protected volatile boolean stopped;
	protected String name;

	public GameClient(String name) {
		super(name);
	}

	public GameClient(AIAlgorithm localAlgorithm) {
		super("Client");
		this.localAlgorithm = localAlgorithm;
	}

	@Override
	public void connectionEstablished() {}

	@Override
	public void dataReceived(Object data) {
		if (data instanceof ConnectionEstablishedMessage) {
			ConnectionEstablishedMessage message = (ConnectionEstablishedMessage)data;
			GameRules rules = message.getGameFactory().gameRules();
			Game g = new Game(rules);
			proxyGame = new ProxyObservable();

			ClientController ctrl = new ClientController(g, message.getPieces(), this);
			message.createSwingView(proxyGame, ctrl, localAlgorithm);
		} else if (data instanceof NotificationMessage) {
			((NotificationMessage)data).notifyObserver(proxyGame);
		}
	}

	@Override
	public void makeMove(Player player) {
		PlayCommand command = new PlayCommand(player);
		sendData(command);
	}
}
