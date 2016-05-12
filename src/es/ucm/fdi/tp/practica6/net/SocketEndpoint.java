package es.ucm.fdi.tp.practica6.net;

import java.net.Socket;

/**
 * A very simple interface for listening and responding
 * to data on sockets.
 */
public interface SocketEndpoint {
	/**
	 * Call this to start listening on the socket. Any incoming data will result in a call to dataReceived.
	 * @param socket
	 * @param timeout
	 */
	void start(Socket socket, int timeout);

	/**
	 * Will be called when communication is first established.
	 */
	void connectionEstablished();

	/**
	 * Call this to stop communication.
	 */
	void stop();

	/**
	 * Will be called when data is received.
	 * @param data
	 */
	void dataReceived(Object data);

	/**
	 * Call this to send data to the other side.
	 * @param data
	 */
	void sendData(Object data);
}
