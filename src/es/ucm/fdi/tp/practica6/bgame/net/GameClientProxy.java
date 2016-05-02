/*
 * Created by Álvaro on 02/05/2016.
 */
package es.ucm.fdi.tp.practica6.bgame.net;

import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;

import java.util.ArrayList;

/**
 * A class that subclasses game but instead of playing an actual game
 * waits to be informed of the observable events by a server.
 */
public class GameClientProxy extends Game {
	/**
	 * List of observers.
	 * <p>
	 * <p>
	 * Lista de observadores.
	 */
	private ArrayList<GameObserver> observers;

	/**
	 * Creates a new proxy game.
	 *
	 * @param rules The game rules.
	 */
	public GameClientProxy(GameRules rules) {
		super(rules);
	}

	/**
	 * Adds a game observer. If an observer is added when the game has started
	 * already, we should notify it with the current state.
	 * <p>
	 * <p>
	 * Añade un observador del juego. Si se añade un observador a un juego que
	 * ya ha comenzado, se notifica el inicio de juego con el estado actual.
	 *
	 * @param o A game observer.
	 *          <p>
	 *          Observador del juego.
	 */
	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);
		if (state != State.Starting) { // i
			notifyGameStart(o);
		}
	}

	/**
	 * Removes a game observer.
	 * <p>
	 * <p>
	 * Elimina un observador del juego.
	 *
	 * @param o A game observer.
	 *          <p>
	 *          Observador del juego.
	 */

	@Override
	public void removeObserver(GameObserver o) {
		observers.remove(o);
	}

}
