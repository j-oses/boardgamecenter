<<<<<<< HEAD
package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;
import es.ucm.fdi.tp.practica6.net.SocketEndpoint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient implements GameObserver, SocketEndpoint {

    private static final int DEFAULT_PORT = 2020;
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final int DEFAULT_TIMEOUT = 2000;
    private static final Logger log = Logger.getLogger(GameClient.class.getSimpleName());

    public VisualController clientController;

    private String hostname;
    private int port;
    private int timeout;

    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    protected volatile boolean stopped;
    protected String name;

    public GameClient() {

        this(DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_TIMEOUT);
    }
    public GameClient(String name){
        this.name = name;
    }
    public GameClient(String hostname, int port, int timeout) {
        this.name = "Client";
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
    }
    //might be useless
    public void connect() throws Exception {
        new Thread(new Runnable() {
            public void run() {

                try {
                    Socket serverConnection = new Socket(hostname, port);
                    oos = new ObjectOutputStream(serverConnection.getOutputStream());
                    ois = new ObjectInputStream(serverConnection.getInputStream());
                    Object serverResponse = ois.readObject();

                    if (serverResponse instanceof Exception) {
                        throw (Exception) serverResponse;
                    }
                    //first check if ok
                    //here recieve data and add it to the controller and stuff
                } catch (Exception e) {
                    throw new GameError("Error while connecting to the server: " + e.getMessage());
                }
            }
        }, "Client").start();
    }

    public void sendObject(Object r) throws IOException {
        oos.writeObject(r);
        oos.flush();
        oos.reset();
    }

    public Object getObject() throws ClassNotFoundException,IOException {
        return ois.readObject();
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

                            dataReceived(getObject());
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
    public void connectionEstablished() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void dataReceived(Object data) {

    }

    @Override
    public void sendData(Object data) {

    }

    //TODO GameObserver Methods
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
}
=======
package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;
import es.ucm.fdi.tp.practica6.net.AbstractClient;
import es.ucm.fdi.tp.practica6.net.SocketEndpoint;

import java.util.List;

/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient extends AbstractClient implements GameObserver {
    public VisualController clientController;


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

    @Override
    protected SocketEndpoint createEndpoint(String name) {
        return null;
    }
}
>>>>>>> 8795c77ec24ab3d28858de0518bb95cad6fbb5d5
