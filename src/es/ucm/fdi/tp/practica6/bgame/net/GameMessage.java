package es.ucm.fdi.tp.practica6.bgame.net;

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
public abstract class GameMessage implements Serializable {
	public void notifyObservers(ArrayList<GameObserver> observers) {
		for (GameObserver o : observers) {
			notifyObserver(o);
		}
	}

	protected abstract void notifyObserver(GameObserver observer);

	public class GameStartMessage extends GameMessage {
		private Board board;
		private String gameDesc;
		private List<Piece> pieces;
		private Piece turn;

		public GameStartMessage(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
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

	public class GameOverMessage extends GameMessage {
		private Board board;
		private Game.State state;
		private Piece winner;

		public GameOverMessage(Board board, Game.State state, Piece winner) {
			this.board = board;
			this.state = state;
			this.winner = winner;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onGameOver(board, state, winner);
		}
	}

	public class MoveStartGameMessage extends GameMessage {
		private Board board;
		private Piece turn;

		public MoveStartGameMessage(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onMoveStart(board, turn);
		}
	}

	public class MoveEndGameMessage extends GameMessage {
		private Board board;
		private Piece turn;
		private boolean success;

		public MoveEndGameMessage(Board board, Piece turn, boolean success) {
			this.board = board;
			this.turn = turn;
			this.success = success;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onMoveEnd(board, turn, success);
		}
	}

	public class ChangeTurnGameMessage extends GameMessage {
		private Board board;
		private Piece turn;

		public ChangeTurnGameMessage(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onChangeTurn(board, turn);
		}
	}

	public  class ErrorGameMessage extends GameMessage {
		private String errorMessage;

		public ErrorGameMessage(String message) {
			this.errorMessage = message;
		}

		@Override
		protected void notifyObserver(GameObserver observer) {
			observer.onError(errorMessage);
		}
	}
}
