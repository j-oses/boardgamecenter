package es.ucm.fdi.tp.practica6.aiautomatedplay;

import es.ucm.fdi.tp.practica6.ataxx.evaluator.AtaxxEvaluator;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class plays a tournament between 64 different ComplexEvaluators, and logs the winner. Although the results are
 * too disperse, the average of 10 tournaments has been the parameters (0.2, 0.5)
 */
public class TournamentMain {
	/**
	 * The class which actually plays the tournament.
	 */
	private static class TournamentMaker implements GameGenerator.GameGeneratorListener {
		static final int dim = 7;
		static final int depth = 2;

		private int matchInRound;
		private List<AtaxxEvaluator> matches;
		private List<AtaxxEvaluator> results;

		/**
		 * Plays a whole tournament.
		 */
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

		/**
		 * Generates the evaluators which will be playing. 45 of this players are generated in a regular fashion,
		 * so a minimum density of games in every subset of the parametric space is guaranteed. The remaining players
		 * are generated randomly.
		 */
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

		/**
		 * Plays a tournament round.
		 */
		private void playRound() {
			while (matchInRound * 2 + 1 < matches.size()) {
				AtaxxEvaluator ev1 = matches.get(2 * matchInRound);
				AtaxxEvaluator ev2 = matches.get(2 * matchInRound + 1);
				playMatch(ev1, ev2);
				matchInRound++;
			}
		}

		/**
		 * Plays a certain 1vs1 match, with 50 games.
		 * @param ev1 the first evaluator to play.
		 * @param ev2 the second evaluator to play.
		 */
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
