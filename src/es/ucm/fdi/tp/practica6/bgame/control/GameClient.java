package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.practica5.bgame.control.VisualController;
import es.ucm.fdi.tp.practica6.net.AbstractClient;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient extends AbstractClient {
    public VisualController clientController;

	public GameClient(String name) {
		super(name);
	}

	public GameClient(String hostname, int port, int timeout) throws IOException {
		super(hostname, port, timeout);
	}

	public GameClient() throws IOException {
		super();
	}

	@Override
    public void start() throws IOException {
        super.start();
    }

    @Override
    public void start(Socket socket, int timeout) {
        super.start(socket, timeout);
    }

    @Override
    public synchronized void sendData(Object o) {
        super.sendData(o);
    }

    @Override
    public void stop() {
        super.stop();
    }

	@Override
	public void connectionEstablished() {

	}

	@Override
    public void dataReceived(Object data) {

    }
}
