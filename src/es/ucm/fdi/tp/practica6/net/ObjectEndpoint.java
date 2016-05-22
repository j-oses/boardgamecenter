package es.ucm.fdi.tp.practica6.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A SocketEndpoint that sends and receives objects via serialization, making no assumptions about the data
 * to be listened to or sent.
 */
public abstract class ObjectEndpoint implements SocketEndpoint {
	protected static final Logger log = Logger.getLogger(AbstractServer.class.getSimpleName());

	/**
	 * The stream which will be sent to the endpoint on the other side of the connection.
	 */
	private ObjectOutputStream oos;

	/**
	 * The stream which will be read from the endpoint on the other side of the connection.
	 */
	private ObjectInputStream ois;

	/**
	 * Whether the endpoint has stopped reading.
	 */
	protected volatile boolean stopped;

	/**
	 * The name of this endpoint.
	 */
	protected String name;

	/**
	 * Creates a new object endpoint.
	 * @param name the endpoint name.
	 */
	public ObjectEndpoint(String name) {
		this.name = name;
	}

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
							stopped = true;
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
	public synchronized void sendData(Object o) {
		try {
			oos.writeObject(o);
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
		this.stopped = true;
	}
}
