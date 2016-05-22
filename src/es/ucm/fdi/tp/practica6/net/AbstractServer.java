package es.ucm.fdi.tp.practica6.net;

import es.ucm.fdi.tp.basecode.bgame.model.GameError;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple multithreaded server. Makes no assumptions regarding data
 * that gets exchanged between servers and clients.
 */
public abstract class AbstractServer {

	private static final Logger log = Logger.getLogger(AbstractServer.class.getSimpleName());

	private int port;
	private int timeout;
	private volatile boolean stopped;

	public AbstractServer() {
		this(2020, 2000);
	}

	public AbstractServer(int port, int timeout) {
		this.port = port;
		this.timeout = timeout;
	}

	/**
	 * Starts accepting clients
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int numConnections = 0;
				try {
					ServerSocket server = new ServerSocket(port);
					server.setSoTimeout(timeout);
					log.info("AbstractServer waiting for connections");
					while (!stopped) {
						Socket s = null;
						try {
							s = server.accept();
							numConnections++;
						} catch (SocketTimeoutException ste) {
							log.log(Level.FINE, "Timeout; checking stop flag and re-accepting");
							continue;
						}

						log.info("Connection " + numConnections + " accepted by server");
						SocketEndpoint endpoint = createEndpoint("ServerCon-" + numConnections);
						endpoint.start(s, timeout);
					}
					server.close();
				} catch (IOException | GameError e) {
					log.log(Level.WARNING, "Error while receiving connections", e);
				}
			}
		}, "Server").start();
	}

	/**
	 * Creates a new endpoint with the provided name.
	 * @param name the endpoint's name.
	 * @return the new endpoint.
	 */
	protected abstract SocketEndpoint createEndpoint(String name);

	/**
	 * Stops the server
	 */
	public void stop() {
		stopped = true;
	}
}
