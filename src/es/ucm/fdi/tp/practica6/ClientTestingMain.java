package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.bgame.control.GameClient;

import java.net.Socket;

/**
 * Created by Álvaro on 10/05/2016.
 */
public class ClientTestingMain {
    public static void main(String args[]) {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            System.err.println("Let me fucking sleep man. Not funny.");
        }

        GameClient client = new GameClient(new MinMax());

        try {
            Socket sockety = new Socket("localhost", 2020);
            client.start(sockety, 2000);
        } catch (Exception e) {
            System.out.println("Sorry");
        }

    }
}
