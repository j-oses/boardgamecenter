package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.ConsoleCtrlMVC;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExtExt;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;
import es.ucm.fdi.tp.practica6.bgame.control.EvaluatorAIPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
RESULTS:
0.0, 0.875
0.125, 0.75
0.375, 0.625
0.173, 0.107
0.625, 0.375
0.0, 0.375
0.125, 0.125
0.25, 0.75
0.375, 0.25
*/

/**
 * Created by Álvaro on 12/05/2016.
 */
public class TournamentMain {
	private static class TournamentMaker implements GameObserver {
		static final int dim = 7;
		static final int depth = 2;

		private int won1;
		private int won2;
		private int draws;
		private int played;
		private int movements;
		private PrintStream stdout;
		private int playingIndex;
		private List<Pair<Double, Double>> matches;
		private List<Pair<Double, Double>> results;
		private Controller controller;

		public void playTournament() {
			won1 = 0;
			won2 = 0;
			draws = 0;
			played = 0;
			playingIndex = 2;
			results = new ArrayList<>();

			stdout = System.out;
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(int arg0) throws IOException { }
			}));

			generateGames();
			playNextGame();
		}

		private void generateGames() {
			matches = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j + i < 9; j++) {
					double weight1 = (double)i / 8.0;
					double weight2 = (double)j / 8.0;
					matches.add(new Pair<>(weight1, weight2));
				}
			}

			while (matches.size() < 64) {
				double weight1 = Math.random();
				double weight2 = Math.random() * (1 - weight1);
				matches.add(new Pair<>(weight1, weight2));
			}

			Collections.shuffle(matches);

			stdout.println("STARTING ROUND OF " + matches.size());
		}

		private void playNextGame() {
			if (played < 51) {
				playGame(matches.get(playingIndex - 2), matches.get(playingIndex - 1));
			} else if (playingIndex + 1 < matches.size()) {
				played = 0;
				playGame(matches.get(playingIndex), matches.get(playingIndex + 1));
				playingIndex += 2;
			} else if (results.size() > 1) {
				played = 0;
				matches = results;
				results = new ArrayList<>();
				playingIndex = 2;

				if (matches.size() == 8) {
					stdout.println("STARTING QUARTERFINALS");
				} else if (matches.size() == 4) {
					stdout.println("STARTING SEMIFINALS");
				} else if (matches.size() == 2) {
					stdout.println("STARTING FINALS");
				} else {
					stdout.println("STARTING ROUND OF " + matches.size());
				}

				playNextGame();
			} else {
				stdout.println("THE WINNER IS (" + results.get(0).getFirst() + ", " + results.get(0).getSecond() + ")");
			}
		}

		private void playGame(Pair<Double, Double> weightsPlayer1, Pair<Double, Double> weightsPlayer2) {
			// We don't need to see it, so it's console mode
			AtaxxFactoryExtExt player1Factory = new AtaxxFactoryExtExt(dim, 8);
			player1Factory.setEvaluator(new ComplexEvaluator(weightsPlayer1.getFirst(), weightsPlayer1.getSecond()));
			AtaxxFactoryExtExt player2Factory = new AtaxxFactoryExtExt(dim, 8);
			player2Factory.setEvaluator(new ComplexEvaluator(weightsPlayer2.getFirst(), weightsPlayer2.getSecond()));
			Game game = new Game(player1Factory.gameRules());
			game.addObserver(this);

			ArrayList<Player> players = new ArrayList<>();
			players.add(new EvaluatorAIPlayer(new MinMax(depth, true), player1Factory.gameRules()));
			players.add(new EvaluatorAIPlayer(new MinMax(depth, true), player2Factory.gameRules()));

			ArrayList<Piece> pieces = new ArrayList<>();
			pieces.add(new Piece("1")); // Intelligent
			pieces.add(new Piece("2")); // Random

			String command = "play" + System.lineSeparator();
			Scanner inScanner = new Scanner(repeat(command.getBytes(), 10000000));

			final Controller ctrl = new ConsoleCtrlMVC(game, pieces, players, inScanner);
			this.controller = ctrl;

			played++;
			movements = 0;

			new Thread(new Runnable() {
				@Override
				public void run() {
					ctrl.start();
				}
			}).start();
		}

		@Override
		public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		}

		@Override
		public void onGameOver(Board board, Game.State state, Piece winner) {
			if (state.equals(Game.State.Won)) {
				if (played < 51) {
					if (winner.getId().equals("1")) {
						won1++;
					} else {
						won2++;
					}
					stdout.println("Game " + played + " finished (" + won1 + "-" + won2 + ")");
				} else {
					if (won1 >= won2) {
						results.add(matches.get(playingIndex - 2));
						stdout.println("Match " + ((playingIndex - 2) / 2) + " finished with winner 1 (" + won1 + ")");
					} else {
						results.add(matches.get(playingIndex - 1));
						stdout.println("Match " + ((playingIndex - 2) / 2) + " finished with winner 2 (" + won2 + ")");
					}
					won1 = 0;
					won2 = 0;
				}
			} else if (state.equals(Game.State.Stopped)) {
				played--;	// Don't count the stopped game
				stdout.println("Game stopped (+300 moves)");
			} // It's impossible to have a draw (odd number of cells and even number of players)

			playNextGame();
		}

		@Override
		public void onMoveStart(Board board, Piece turn) {
		}

		@Override
		public void onMoveEnd(Board board, Piece turn, boolean success) {
			movements++;
			if (movements > 300) {
				controller.stop();
			}
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
		TournamentMaker statsMaker = new TournamentMaker();
		statsMaker.playTournament();
	}

}
