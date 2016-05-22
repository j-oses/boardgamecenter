package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExtExt;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this game is to play ataxx a given number of times and to output the victory stats of, for example,
 * the mildly intelligent player against the really intelligent player.
 */
public class StatsMain {
	private static class StatsMaker extends Player implements GameObserver {
		static final int dim = 7;
		static final int depth = 2;
		static final int gamesToPlay = 100;
		static final int obstacles = 8;

		private int won;
		private int draws;
		private int played;

		void generateStats() {
			won = 0;
			draws = 0;
			played = 0;

			playGame();
		}

		private boolean playing = false;
		private Board board;
		private GameMove nextMove;

		private void playGame() {
			// We don't need to see it, so it's console mode
			AtaxxFactoryExtExt factory = new AtaxxFactoryExtExt(dim, obstacles);
			factory.setEvaluator(new ComplexEvaluator(0.0, 0.875));
			AtaxxFactoryExtExt secondFactory = new AtaxxFactoryExtExt(dim, obstacles);
			secondFactory.setEvaluator(new ComplexEvaluator(0.48, 0.002));
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
				g.restart();
				while (playing) {
					try {
						nextMove = players.get(0).requestMove(pieces.get(0), board, g.getPlayersPieces(), r1);
						g.makeMove(this);
					} catch (GameError e) {
						// If it cannot generate more moves, no problem
					}

					if (!playing) break;

					try {
						nextMove = players.get(1).requestMove(pieces.get(1), board, g.getPlayersPieces(), r1);
						g.makeMove(this);
					} catch (GameError e) {
						// If it cannot generate more moves, no problem
					}
				}
				played++;
			}

			System.out.println("\r\n\r\n TESTING FINISHED: THE MORE INTELLIGENT PLAYER HAS WON " +
					won + "/" + played + " times " + " (with " + draws + " draws)\r\n\r\n");
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

			System.out.print("\r\nPlayed " + played + " games, with " + won + "AI victories\r\n");
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

	public static void main(String args[]) {
		StatsMaker statsMaker = new StatsMaker();
		statsMaker.generateStats();
	}
}
