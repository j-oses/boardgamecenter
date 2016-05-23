package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.practica6.control.GameClient;

import java.net.Socket;

/**
 * This main class is used to create a client with testing purposes.
 */
public class ClientTestingMain {
    public static void main(String args[]) {
        GameClient client = new GameClient();

        try {
            Socket sockety = new Socket("localhost", 2020);
            client.start(sockety, 2000);
        } catch (Exception e) {
            System.out.println("Sorry, something wrong happened: " + e);
        }

    }
}
