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
 * Created by Álvaro on 22/05/2016.
 */
public class GameGenerator extends Player implements GameObserver {
	public interface GameGeneratorListener {
		void gameFinished(int played, int wonByPlayer1, int draws);
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

	public GameGenerator(int dim, int depth, int gamesToPlay, int obstacles, AtaxxEvaluator evaluator1,
						 AtaxxEvaluator evaluator2) {
		this.dim = dim;
		this.depth = depth;
		this.gamesToPlay = gamesToPlay;
		this.obstacles = obstacles;
		this.ev1 = evaluator1;
		this.ev2 = evaluator2;
	}

	public void setListener(GameGeneratorListener listener) {
		this.listener = listener;
	}

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

		return won >= played - draws - won;
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