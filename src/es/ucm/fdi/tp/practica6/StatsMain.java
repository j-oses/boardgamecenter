package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExtExt;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;
import es.ucm.fdi.tp.practica6.control.EvaluatorAIPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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

		private int won;
		private int draws;
		private int played;
		private PrintStream stdout;

		public void generateStats() {
			won = 0;
			draws = 0;
			played = 0;

			stdout = System.out;
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(int arg0) throws IOException { }
			}));

			playGame();
		}

		private boolean playing = false;
		private Board board;
		private GameMove nextMove;

		private void playGame() {
			// We don't need to see it, so it's console mode
			AtaxxFactoryExtExt factory = new AtaxxFactoryExtExt(dim, 8);
			factory.setEvaluator(new ComplexEvaluator(0.0, 0.875));
			AtaxxFactoryExtExt secondFactory = new AtaxxFactoryExtExt(dim, 8);
			secondFactory.setEvaluator(new ComplexEvaluator(0.48, 0.002));
			Game g = new Game(factory.gameRules());
			g.addObserver(this);

			GameRules r1 = factory.gameRules();
			GameRules r2 = secondFactory.gameRules();

			ArrayList<Player> players = new ArrayList<>();
			players.add(new EvaluatorAIPlayer(new MinMax(depth), r1));
			players.add(new EvaluatorAIPlayer(new MinMax(depth), r2));
			// players.add(factory.createRandomPlayer());

			ArrayList<Piece> pieces = new ArrayList<>();
			pieces.add(new Piece("I")); // Intelligent
			pieces.add(new Piece("R")); // Random

			while (played < gamesToPlay) {
				playing = true;
				g.start(pieces);
				while (playing) {
					nextMove = players.get(0).requestMove(pieces.get(0), board, g.getPlayersPieces(), r1);
					g.makeMove(this);
					if ( ! playing) break;
					nextMove = players.get(1).requestMove(pieces.get(1), board, g.getPlayersPieces(), r2);
					g.makeMove(this);
				}
				played++;
			}
			stdout.println("\r\n\r\n TESTING FINISHED: THE MORE INTELLIGENT PLAYER HAS WON " +
					won + "/" + played + " times " + " (with " + draws + " draws)\r\n\r\n");
		}

		@Override
		public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
			this.board = board;
			System.err.println("Started");
		}

		@Override
		public void onGameOver(Board board, Game.State state, Piece winner) {
			if (state.equals(Game.State.Won)) {
				played++;    // Ignore draws
				if (winner.getId().equals("I")) {
					won++;
				}
			} else if (state.equals(Game.State.Draw)) {
				played++;
				draws++;
			}

			// Back to the default stream
			stdout.print("\r\nPlayed " + played + " games, with " + won + "AI victories\r\n");
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
			throw new GameError("Some error occurred when playing automatically: " + msg);
		}

		@Override
		public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
			return nextMove;
		}
	}

	private static InputStream repeat(final byte[] sample, final int times) {
		return new InputStream() {
			private long pos = 0;
			private final long total = (long) sample.length * times;

			public int read() throws IOException {
				return pos < total ? sample[(int) (pos++ % sample.length)] : -1;
			}
		};
	}

	public static void main(String args[]) {
		StatsMaker statsMaker = new StatsMaker();
		statsMaker.generateStats();
	}
}
