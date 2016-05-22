package es.ucm.fdi.tp.practica6.net;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Álvaro on 02/05/2016.
 */
public abstract class NotificationMessage implements Serializable {
	public abstract void notifyObserver(GameObserver observer);

	public static class GameStart extends NotificationMessage {
		private Board board;
		private String gameDesc;
		private List<Piece> pieces;
		private Piece turn;

		public GameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
			this.board = board;
			this.gameDesc = gameDesc;
			this.pieces = pieces;
			this.turn = turn;
		}

		@Override
		public void notifyObserver(GameObserver observer) {
			observer.onGameStart(board, gameDesc, pieces, turn);
		}
	}

	public static class GameOver extends NotificationMessage {
		private Board board;
		private Game.State state;
		private Piece winner;

		public GameOver(Board board, Game.State state, Piece winner) {
			this.board = board;
			this.state = state;
			this.winner = winner;
		}

		@Override
		public void notifyObserver(GameObserver observer) {
			observer.onGameOver(board, state, winner);
		}
	}

	public static class MoveStart extends NotificationMessage {
		private Board board;
		private Piece turn;

		public MoveStart(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		public void notifyObserver(GameObserver observer) {
			observer.onMoveStart(board, turn);
		}
	}

	public static class MoveEnd extends NotificationMessage {
		private Board board;
		private Piece turn;
		private boolean success;

		public MoveEnd(Board board, Piece turn, boolean success) {
			this.board = board;
			this.turn = turn;
			this.success = success;
		}

		@Override
		public void notifyObserver(GameObserver observer) {
			observer.onMoveEnd(board, turn, success);
		}
	}

	public static class ChangeTurn extends NotificationMessage {
		private Board board;
		private Piece turn;

		public ChangeTurn(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		public void notifyObserver(GameObserver observer) {
			observer.onChangeTurn(board, turn);
		}
	}

	public static class Error extends NotificationMessage {
		private String errorMessage;

		public Error(String message) {
			this.errorMessage = message;
		}

		@Override
		public void notifyObserver(GameObserver observer) {
			observer.onError(errorMessage);
		}
	}
}
