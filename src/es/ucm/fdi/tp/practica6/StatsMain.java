package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.ConsoleCtrlMVC;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExtExt;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxRulesExt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The purpose of this game is to play ataxx a given number of times and to output the victory stats of, for example,
 * the mildly intelligent player against the really intelligent player.
 */
public class StatsMain {
	private static class StatsMaker implements GameObserver {
		static final int dim = 7;
		static final int depth = 3;
		static final int gamesToPlay = 500;

		private int won;
		private int draws;
		private int played;
		// private int movementsDone;
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

		private void playGame() {
			// We don't need to see it, so it's console mode
			GameFactory factory = new AtaxxFactoryExtExt(dim, 0, AtaxxRulesExt.AILevel.REALLY_INTELLIGENT);
			// GameFactory mildlyFactory = new AtaxxFactoryExtExt(dim, 0, AtaxxRulesExt.AILevel.MILDLY_INTELLIGENT);
			Game g = new Game(factory.gameRules());
			g.addObserver(this);

			ArrayList<Player> players = new ArrayList<>();
			players.add(factory.createAIPlayer(new MinMax(depth)));
			// players.add(mildlyFactory.createAIPlayer(new MinMax(depth)));
			players.add(factory.createRandomPlayer());

			ArrayList<Piece> pieces = new ArrayList<>();
			pieces.add(new Piece("I")); // Intelligent
			pieces.add(new Piece("R")); // Random

			String command = "play" + System.lineSeparator();
			Scanner inScanner = new Scanner(repeat(command.getBytes(), 10000000));

			final Controller c = new ConsoleCtrlMVC(g, pieces, players, inScanner);
			// Uncomment the following line to show the play
			// factory.createConsoleView(g, c);

			// movementsDone = 0;

			new Thread(new Runnable() {
				@Override
				public void run() {
					c.start();
				}
			}).start();
		}

		@Override
		public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
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

			if (played < gamesToPlay) {
				playGame();
			} else {
				stdout.println("\r\n\r\n TESTING FINISHED: THE MORE INTELLIGENT PLAYER HAS WON " +
						won + "/" + played + " times " + " (with " + draws + " draws)\r\n\r\n");
			}
		}

		@Override
		public void onMoveStart(Board board, Piece turn) {
		}

		@Override
		public void onMoveEnd(Board board, Piece turn, boolean success) {
			/* movementsDone++;
			stdout.print("Done " + movementsDone + " movements\r\n");
			if (movementsDone % 5 == 0) {
				stdout.println("Board:\r\n" + board.toString() + "\r\n");
			} */
		}

		@Override
		public void onChangeTurn(Board board, Piece turn) {
		}

		@Override
		public void onError(String msg) {
			throw new GameError("Some error occurred when playing automatically: " + msg);
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
