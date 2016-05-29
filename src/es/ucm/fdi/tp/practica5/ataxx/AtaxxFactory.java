package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.*;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AtaxxFactory implements GameFactory {
	private static final long serialVersionUID = 1L;
	
	private static final int MIN_DIM = 5;
	private static final int DEFAULT_OBSTACLES = 0;// for ez change
	protected int obstacles;
	protected int dim;

	/**
	 * Constructs a factory with a default dimension and obstacles.
	 */
	public AtaxxFactory() {
		this(MIN_DIM, DEFAULT_OBSTACLES);
	}

	/**
	 * Constructs a factory with a default dimension.
	 * 
	 * @param obstacles
	 *            the number of obstacles.
	 */
	public AtaxxFactory(int obstacles) {
		this(MIN_DIM, obstacles);
	}

	/**
	 * Constructs a factory with a provided number of obstacles/dimension.
	 * 
	 * @param dim
	 *            the dimension.
	 * @param obstacles
	 *            the obstacles.
	 */
	public AtaxxFactory(int dim, int obstacles) {
		if (dim < MIN_DIM) {
			throw new GameError("Dimension must be at least " + MIN_DIM + ": "
					+ dim);
		} else if (dim % 2 == 0) {
			throw new GameError("Dimension must be an odd number :" + dim);
		} else {
			this.dim = dim;
		}
		if (obstacles % 4 != 0) {
			throw new GameError("Invalid number for obstacles :" + obstacles
					+ ", must be a multiple of 4");
		} else if (!(obstacles >= 0 && obstacles < (dim * dim)
				- (dim + dim - 1 + 4))) {
			throw new GameError("Invalid number for obstacles :" + obstacles
					+ " must be a positive" + "number lower than "
					+ ((dim * dim) - (dim + dim - 1 + 4)));
		} else {
			this.obstacles = obstacles;
		}

	}

	@Override
	/**
	 * Returns the game rules.
	 * @return the game rules.
	 */
	public GameRules gameRules() {
		return new AtaxxRules(dim, obstacles);
	}

	@Override
	/**
	 * Creates a new player which plays from console.
	 * @return the console player.
	 */
	public Player createConsolePlayer() {
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	/**
	 * Creates a player which plays random.
	 * @return the random player.
	 */
	public Player createRandomPlayer() {
		return new AtaxxRandomPlayer();
	}

	@Override
	/**
	 * Creates an AI player (AI not implemented).
	 * @return the AI player.
	 */
	public Player createAIPlayer(AIAlgorithm alg) {
		return new DummyAIPlayer(createRandomPlayer(), 1000);
	}

	@Override
	/**
	 * By default, we have two players, X and O.
	 * <p>
	 * Por defecto, hay dos jugadores, X y O.
	 */
	public List<Piece> createDefaultPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	/**
	 * Creates a console view.
	 */
	public void createConsoleView(Observable<GameObserver> game, Controller ctrl) {
		new GenericConsoleView(game, ctrl);
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer) {
		throw new UnsupportedOperationException("There is no swing view");
	}

}
