package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.practica6.aiautomatedplay.GameGenerator;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.AtaxxEvaluator;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

0.380, 0.120
0.25, 0.25
*/
public class TournamentMain {
	private static class TournamentMaker implements GameGenerator.GameGeneratorListener {
		static final int dim = 7;
		static final int depth = 2;

		private int matchInRound;
		private List<AtaxxEvaluator> matches;
		private List<AtaxxEvaluator> results;

		private void playTournament() {
			matchInRound = 0;
			results = new ArrayList<>();

			generateGames();

			while (matches.size() >= 2) {
				if (matches.size() == 8) {
					System.out.println("STARTING QUARTERFINALS");
				} else if (matches.size() == 4) {
					System.out.println("STARTING SEMIFINALS");
				} else if (matches.size() == 2) {
					System.out.println("STARTING FINALS");
				} else {
					System.out.println("STARTING ROUND OF " + matches.size());
				}

				matchInRound = 0;
				playRound();

				matches = results;
				results = new ArrayList<>();
			}

			System.out.println("THE WINNER IS:\r\n" + matches.get(0));
		}

		private void generateGames() {
			matches = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j + i < 9; j++) {
					double weight1 = (double) i / 8.0;
					double weight2 = (double) j / 8.0;
					matches.add(new ComplexEvaluator(weight1, weight2));
				}
			}

			while (matches.size() < 64) {
				double weight1 = Math.random();
				double weight2 = Math.random() * (1 - weight1);
				matches.add(new ComplexEvaluator(weight1, weight2));
			}

			Collections.shuffle(matches);
		}

		private void playRound() {
			while (matchInRound * 2 + 1 < matches.size()) {
				AtaxxEvaluator ev1 = matches.get(2 * matchInRound);
				AtaxxEvaluator ev2 = matches.get(2 * matchInRound + 1);
				playMatch(ev1, ev2);
				matchInRound++;
			}
		}

		private void playMatch(AtaxxEvaluator ev1, AtaxxEvaluator ev2) {
			GameGenerator gameGenerator = new GameGenerator(dim, depth, 50, 8, ev1, ev2);
			gameGenerator.setListener(this);
			boolean firstWon = gameGenerator.playGames();
			results.add(firstWon ? ev1 : ev2);
		}

		@Override
		public void gameFinished(int played, int wonByPlayer1, int draws) {

		}

		@Override
		public void didEndPlayingGames(int played, int wonByPlayer1, int draws) {
			System.out.println("Match " + (matchInRound + 1) + " finished with score " + wonByPlayer1 + "-"
					+ (played - draws - wonByPlayer1));
		}
	}

	public static void main(String args[]) {
		TournamentMaker statsMaker = new TournamentMaker();
		statsMaker.playTournament();
	}
}
