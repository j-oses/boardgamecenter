package es.ucm.fdi.tp.practica6.net;
import
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by Jorge on 10-May-16.
 */
public abstract class AbstractClient extends ObjectEndpoint {
    private static final Logger log = Logger.getLogger(AbstractServer.class.getSimpleName());

    private String hostname;
    private int port;
    private int timeout;
    public AbstractClient(String name) {
        super(name);
    }
    public AbstractClient() throws IOException {
        this("localhost", 2020, 2000);
    }

    public AbstractClient(String hostname, int port, int timeout) throws IOException {
        super("Client");
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
    }

    public void start() throws IOException {
        start(new Socket(hostname, port), timeout);
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
    public abstract void connectionEstablished();

    public abstract void dataReceived(Object data);
}
