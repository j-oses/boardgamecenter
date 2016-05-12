package es.ucm.fdi.tp.practica6.net;

import es.ucm.fdi.tp.basecode.bgame.model.GameError;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jorge on 10-May-16.
 */
public abstract class AbstractClient {
	//TODO : this whole class has to be divided into GameClient and AbstractClient
	//later we can move some of those default values all the way up to objectEndpoint or even socketEndpoint so both of em share;
	//It is maybe a lil bit too much info for them to know tho);

	private static final int DEFAULT_PORT = 2020;
	private static final String DEFAULT_HOSTNAME = "localhost";
	private static final int DEFAULT_TIMEOUT = 2000;
	private static final Logger log = Logger.getLogger(AbstractClient.class.getSimpleName());

	private Connection serverConnection;
	private String hostname;
	private int port;
	private int timeout;
	private volatile boolean stopped;

	public AbstractClient() {
		this(DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_TIMEOUT);
	}

	public AbstractClient(String hostname, int port, int timeout) {

		this.hostname = hostname;
		this.port = port;
		this.timeout = timeout;
	}

	//TODO next two methods are mainly brainstorming and probably useless
	public boolean isOnline(Socket auxSocket) {
		boolean isOnline = true;
		try {
			InetSocketAddress serverDirection = new InetSocketAddress(hostname, port);
			Socket serverCheck = new Socket();
			serverCheck.connect(serverDirection, 1);//ping with timeout 1sec
			serverCheck.close();
		} catch (Exception e) {
			isOnline = false;
			log.log(Level.WARNING, "Server is offline...");
		}
		return isOnline;
	}

	public boolean hostAvailabilityCheck(Socket auxSocket) {
		try (Socket isConnected = new Socket(hostname, port)) {
			auxSocket = isConnected;
			return true;
		} catch (IOException e) {
			log.log(Level.WARNING, "Server is offline...");
		}
		return false;
	}

	public void connect() throws Exception {
		new Thread(new Runnable() {
			public void run() {

				try {
					serverConnection = new Connection(new Socket(hostname, port));
					serverConnection.sendObject("Connect");
					Object serverResponse = serverConnection.getObject();

					if (serverResponse instanceof Exception) {
						throw (Exception) serverResponse;
					}
					//first check if ok
					//here recieve data and add it to the controller and stuff
				} catch (Exception e) {
					//Log.log(Level.FINE, e.getMessage()); log to the server maybe? in the future
					throw new GameError("Error while connecting to the server: " + e.getMessage());
				}
			}
		}, "Client").start();
	}

	public void start() {
	/*
    add the observers,set the game to running... all of this done in the controller

     */

	}

	protected abstract SocketEndpoint createEndpoint(String name);

	public void stop() {
		stopped = true;
	}
}
