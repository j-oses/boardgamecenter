package es.ucm.fdi.tp.practica6.aiautomatedplay;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExtExt;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.AtaxxEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which plays a certain number of Ataxx games with the given conditions.
 */
public class GameGenerator extends Player implements GameObserver {
	/**
	 * Transmits the game results.
	 */
	public interface GameGeneratorListener {
		/**
		 * Notifies the receiver that a single game has finished.
		 * @param played the number of games played.
		 * @param wonByPlayer1 the number of games won by player 1.
		 * @param draws the number of draws.
		 */
		void gameFinished(int played, int wonByPlayer1, int draws);

		/**
		 * Notifies the receiver that all the games has finished.
		 * @param played the number of games played.
		 * @param wonByPlayer1 the number of games won by player 1.
		 * @param draws the number of draws.
		 */
		void didEndPlayingGames(int played, int wonByPlayer1, int draws);
	}

	// We have to limit the possible movements, because in certain matches the ai player stalls to prevent losing.
	private static final int maxMovements = 400;

	private int dim;
	private int depth;
	private int gamesToPlay;
	private int obstacles;
	private AtaxxEvaluator ev1;
	private AtaxxEvaluator ev2;
	private GameGeneratorListener listener;

	private int won;
	private int draws;
	private int played;
	private int numMovements;

	private boolean playing = false;
	private Board board;
	private GameMove nextMove;

	/**
	 * Creates a new game generator.
	 * @param dim the dimension of the board.
	 * @param depth the depth of the minmax algorithm.
	 * @param gamesToPlay the number of games to be played.
	 * @param obstacles the number of obstacles in each board.
	 * @param evaluator1 the evaluator for the first player.
	 * @param evaluator2 the evaluator for the second player.
	 */
	public GameGenerator(int dim, int depth, int gamesToPlay, int obstacles, AtaxxEvaluator evaluator1,
						 AtaxxEvaluator evaluator2) {
		this.dim = dim;
		this.depth = depth;
		this.gamesToPlay = gamesToPlay;
		this.obstacles = obstacles;
		this.ev1 = evaluator1;
		this.ev2 = evaluator2;
	}

	/**
	 * Sets the listener for the game results.
	 * @param listener the new listener.
	 */
	public void setListener(GameGeneratorListener listener) {
		this.listener = listener;
	}

	/**
	 * Plays a series of games.
	 * @return wether the first player won.
	 */
	public boolean playGames() {
		won = 0;
		draws = 0;
		played = 0;

		AtaxxFactoryExtExt factory = new AtaxxFactoryExtExt(dim, obstacles);
		factory.setEvaluator(ev1);
		AtaxxFactoryExtExt secondFactory = new AtaxxFactoryExtExt(dim, obstacles);
		secondFactory.setEvaluator(ev2);

		Game g = new Game(factory.gameRules());
		g.addObserver(this);

		GameRules r1 = factory.gameRules();
		GameRules r2 = secondFactory.gameRules();

		ArrayList<Player> players = new ArrayList<>();
		players.add(new AIPlayer(new MinMax(depth)));
		players.add(new AIPlayer(new MinMax(depth)));
		// players.add(factory.createRandomPlayer());

		ArrayList<Piece> pieces = new ArrayList<>();
		pieces.add(new Piece("I")); // Intelligent
		pieces.add(new Piece("R")); // Random

		g.start(pieces);
		while (played < gamesToPlay) {
			playing = true;
			numMovements = 0;
			g.restart();

			while (playing && numMovements < maxMovements) {
				try {
					nextMove = players.get(0).requestMove(pieces.get(0), board, g.getPlayersPieces(), r1);
					g.makeMove(this);
					numMovements++;
				} catch (GameError e) {
					// If it cannot generate more moves, no problem
				}

				if (!playing) break;

				try {
					nextMove = players.get(1).requestMove(pieces.get(1), board, g.getPlayersPieces(), r2);
					g.makeMove(this);
					numMovements++;
				} catch (GameError e) {
					// If it cannot generate more moves, no problem
				}
			}

			if (numMovements < maxMovements) {
				played++;
			}
		}

		if (listener != null) {
			listener.didEndPlayingGames(played, won, draws);
		}

		return won > played - draws - won;	// In case of a tie, the win goes to the second player.
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.board = board;
	}

	@Override
	public void onGameOver(Board board, Game.State state, Piece winner) {
		if (state.equals(Game.State.Won)) {
			if (winner.getId().equals("I")) {
				won++;
			}
		} else if (state.equals(Game.State.Draw)) {
			draws++;
		}

		if (listener != null) {
			listener.gameFinished(played, won, draws);
		}
		playing = false;
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
		// throw new GameError("Some error occurred when playing automatically: " + msg);
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return nextMove;
	}
}