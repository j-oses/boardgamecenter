package es.ucm.fdi.tp.practica6.model;

import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.practica6.net.NotificationMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Álvaro on 22/05/2016.
 */
public class ProxyObservable implements Observable<GameObserver>, GameObserver {
	private ArrayList<GameObserver> observers;
	private NotificationMessage.GameStart gameStartNotification;

	public ProxyObservable() {
		observers = new ArrayList<>();
	}

	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);

		// Mimics the behaviour of a game, which sends an onGameStart notification if the game has already
		// started to the newly registered observers.
		if (gameStartNotification != null) {
			gameStartNotification.notifyObserver(o);
		}
	}

	@Override
	public void removeObserver(GameObserver o) {
		observers.remove(o);
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		for (GameObserver o: observers) {
			o.onGameStart(board, gameDesc, pieces, turn);
		}
		gameStartNotification = new NotificationMessage.GameStart(board, gameDesc, pieces, turn);
	}

	@Override
	public void onGameOver(Board board, Game.State state, Piece winner) {
		for (GameObserver o: observers) {
			o.onGameOver(board, state, winner);
		}

		// Make sure the new observers don't get notified of a game start when the game is over.
		gameStartNotification = null;
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		for (GameObserver o: observers) {
			o.onMoveStart(board, turn);
		}
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		for (GameObserver o: observers) {
			o.onMoveEnd(board, turn, success);
		}
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		for (GameObserver o: observers) {
			o.onChangeTurn(board, turn);
		}
	}

	@Override
	public void onError(String msg) {
		for (GameObserver o: observers) {
			o.onError(msg);
		}
	}
}
