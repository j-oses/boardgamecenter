package es.ucm.fdi.tp.practica6.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Jorge on 12-May-16.
 */

public class Connection {

        Socket connection;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        public Connection(Socket connection) throws IOException {
            this.connection = connection;
            this.oos = new ObjectOutputStream(connection.getOutputStream());
            this.ois = new ObjectInputStream(connection.getInputStream());
        }

        public void sendObject(Object r) throws IOException {
            oos.writeObject(r);
            oos.flush();
            oos.reset();
        }

        public Object getObject() throws ClassNotFoundException,IOException {
            return ois.readObject();
        }

        public void stop() throws IOException {
            connection.close();
        }
}

