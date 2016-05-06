package es.ucm.fdi.tp.practica6.net;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Álvaro on 02/05/2016.
 */
public abstract class NotificationMessage implements Serializable {
	public void notifyObservers(ArrayList<GameObserver> observers) {
		for (GameObserver o : observers) {
			notifyObserver(o);
		}
	}

	protected abstract void notifyObserver(GameObserver observer);

	public class GameStartNotificationMessage extends NotificationMessage {
		private Board board;
		private String gameDesc;
		private List<Piece> pieces;
		private Piece turn;

		public GameStartNotificationMessage(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
			this.board = board;
			this.gameDesc = gameDesc;
			this.pieces = pieces;
			this.turn = turn;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onGameStart(board, gameDesc, pieces, turn);
		}
	}

	public class GameOverNotificationMessage extends NotificationMessage {
		private Board board;
		private Game.State state;
		private Piece winner;

		public GameOverNotificationMessage(Board board, Game.State state, Piece winner) {
			this.board = board;
			this.state = state;
			this.winner = winner;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onGameOver(board, state, winner);
		}
	}

	public class MoveStartNotificationMessage extends NotificationMessage {
		private Board board;
		private Piece turn;

		public MoveStartNotificationMessage(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onMoveStart(board, turn);
		}
	}

	public class MoveEndNotificationMessage extends NotificationMessage {
		private Board board;
		private Piece turn;
		private boolean success;

		public MoveEndNotificationMessage(Board board, Piece turn, boolean success) {
			this.board = board;
			this.turn = turn;
			this.success = success;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onMoveEnd(board, turn, success);
		}
	}

	public class ChangeTurnNotificationMessage extends NotificationMessage {
		private Board board;
		private Piece turn;

		public ChangeTurnNotificationMessage(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onChangeTurn(board, turn);
		}
	}

	public  class ErrorNotificationMessage extends NotificationMessage {
		private String errorMessage;

		public ErrorNotificationMessage(String message) {
			this.errorMessage = message;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onError(errorMessage);
		}
	}
}
