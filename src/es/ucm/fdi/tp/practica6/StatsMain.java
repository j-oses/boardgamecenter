package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.practica6.aiautomatedplay.GameGenerator;
import es.ucm.fdi.tp.practica6.ataxx.evaluator.ComplexEvaluator;

/**
 * The purpose of this game is to play ataxx a given number of times and to output the victory stats of, for example,
 * the mildly intelligent player against the really intelligent player.
 */
public class StatsMain {
	static final int dim = 7;
	static final int depth = 2;
	static final int gamesToPlay = 100;
	static final int obstacles = 8;

	public static void main(String args[]) {
		GameGenerator generator = new GameGenerator(dim, depth, gamesToPlay, obstacles,
				new ComplexEvaluator(0.0, 0.875), new ComplexEvaluator(0.48, 0.002));
		generator.setListener(new GameGenerator.GameGeneratorListener() {
			@Override
			public void gameFinished(int played, int wonByPlayer1, int draws) {
				System.out.print("\r\nPlayed " + played + " games, with " + wonByPlayer1 + "first player victories\r\n");
			}

			@Override
			public void didEndPlayingGames(int played, int wonByPlayer1, int draws) {
				System.out.println("\r\n\r\n TESTING FINISHED: THE FIRST PLAYER HAS WON " +
						wonByPlayer1 + "/" + played + " times " + " (with " + draws + " draws)\r\n\r\n");
			}
		});
		generator.playGames();
	}
}
