package es.ucm.fdi.tp.practica5.control;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica5.model.MoveGenerator.MoveListener;
import es.ucm.fdi.tp.practica5.views.BoardJPanel;
import es.ucm.fdi.tp.practica5.views.GameWindow;
import es.ucm.fdi.tp.practica5.views.GameWindow.GameChangesListener;
import es.ucm.fdi.tp.practica5.views.SettingsPanel;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This controller manages a game which is played with a GUI. Each controller
 * manages a single window.
 *
 * @author Álvaro & Jorge
 */
public class VisualController extends Controller implements MoveListener,
		GameObserver, GameChangesListener {
	protected GameWindow window;
	private Piece owner;
	private HashMap<Piece, PlayerMode> players;
	private HashMap<PlayerMode, Player> playerForMode;
	private Player currentPlayer;
	private PlayerMode currentMode;
	private boolean isTurn;
	private boolean hasFocus;

	/**
	 * An enum which describes the three possible player modes: manual, random
	 * and AI.
	 *
	 * @author Álvaro
	 */
	public enum PlayerMode {
		MANUAL("Manual"), RANDOM("Random"), AI("Intelligent");

		private String mode;

		PlayerMode(String mode) {
			this.mode = mode;
		}

		public String toString() {
			return mode;
		}

		/**
		 * Initializes a new PlayerMode with the given string.
		 *
		 * @param text the string related with the mode.
		 * @return a new mode.
		 */
		public static PlayerMode fromString(String text) {
			if (text != null) {
				for (PlayerMode mode : PlayerMode.values()) {
					if (text.equalsIgnoreCase(mode.mode)) {
						return mode;
					}
				}
			}
			throw new IllegalArgumentException("No constant with text " + text
					+ " found");
		}
	}

	/**
	 * Constructs a new controller.
	 *
	 * @param game   the game which this controller will manage.
	 * @param pieces the players in the game
	 * @param owner  the player to which the associated window will belong.
	 *               {@code null} if this controller manages multiple players.
	 */
	public VisualController(Game game, List<Piece> pieces, Piece owner) {
		super(game, pieces);
		this.owner = owner;

		players = new HashMap<>();
		for (Piece p : pieces) {
			this.players.put(p, PlayerMode.MANUAL);
		}

		playerForMode = new HashMap<>();

		game.addObserver(this);
	}

	/**
	 * Requests a move to the player provided and executes it.
	 *
	 * @param p the player to which the move will be requested.
	 */
	@Override
	public void makeMove(final Player p) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					game.makeMove(p);
				} catch (GameError e) {
					// It's not a good practice to leave a catch block empty,
					// but we do this due to the known error in the basecode.
					// The exception catched here is already catched inside the
					// Game class and this controller already reacts to the
					// error when notified as a GameObserver.
				}
			}
		});
	}

	/**
	 * Starts the controller, checking the correctness of the game and the
	 * window.
	 */
	@Override
	public void start() {
		if (game == null || pieces == null) {
			throw new GameError("There is no game or pieces to start");
		} else if (window == null) {
			throw new GameError("There is no window to start");
		}

		super.start();
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
							Piece turn) {
		isTurn = (owner == null) || owner.equals(turn);
		currentMode = players.get(turn);
		currentPlayer = playerForMode.get(currentMode);

		window.setVisible(true);
		window.setCurrentlyManual(currentMode.equals(PlayerMode.MANUAL));
		toFrontIfNeeded();

		if (isTurn && currentPlayer != null) {
			makeMove(currentPlayer);
		} // else wait until move generated
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		if (state == State.Stopped) {
			window.setVisible(false);
			window.dispose();
		}
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {

	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		isTurn = (owner == null) || owner.equals(turn);
		currentMode = players.get(turn);
		currentPlayer = playerForMode.get(currentMode);

		window.setCurrentlyManual(currentMode.equals(PlayerMode.MANUAL));

		toFrontIfNeeded();

		if (isTurn && currentPlayer != null) {
			makeMove(currentPlayer);
		} // else wait until move generated
	}

	@Override
	public void onError(String msg) {

	}

	public void didGenerateMove(GameMove move) {
		// It's not strictly necessary to check for current player, but it's
		// checked just in case
		if (isTurn && currentPlayer == null) {
			Player wrapperPlayer = new VisualPlayer(move);
			makeMove(wrapperPlayer);
		}
	}

	@Override
	public void randomMoveButtonPressed() {
		Player randomPlayer = playerForMode.get(PlayerMode.RANDOM);
		if (randomPlayer != null) {
			makeMove(randomPlayer);
		}
	}

	@Override
	public void aiMoveButtonPressed() {
		Player aiPlayer = playerForMode.get(PlayerMode.AI);
		if (aiPlayer != null) {
			makeMove(aiPlayer);
		}
	}

	@Override
	public void quitButtonPressed() {
		game.stop();
	}

	@Override
	public void restartButtonPressed() {
		game.restart();
	}

	@Override
	public void selectedNewGameMode(String pieceId, String mode) {
		PlayerMode playerMode = PlayerMode.fromString(mode);
		players.put(new Piece(pieceId), playerMode);
	}

	/**
	 * Creates a new game window with the provided board panel, players and
	 * piece.
	 *
	 * @param boardPanel   the panel which presents the board.
	 * @param randomPlayer the player to generate random moves.
	 * @param aiPlayer     the player to generate AI moves.
	 * @param viewPiece    the player to which the associated window will belong.
	 *                     {@code null} if this controller manages multiple players.
	 * @param observable   the Observable of GameObserver which will be registered for the events. Usually the game
	 *                     but may be a proxy.
	 */
	public void addGameWindowForPieces(BoardJPanel boardPanel, Player randomPlayer, Player aiPlayer, Piece viewPiece,
									   Observable<GameObserver> observable) {
		ArrayList<String> auxPlayerString = new ArrayList<>();

		for (Piece key : players.keySet()) {
			auxPlayerString.add(key.toString());
		}

		String auxString[] = new String[auxPlayerString.size()];
		auxString = auxPlayerString.toArray(auxString);

		playerForMode.put(PlayerMode.RANDOM, randomPlayer);
		playerForMode.put(PlayerMode.AI, aiPlayer);

		String auxOwner;
		if (owner == null) {
			auxOwner = null;
		} else {
			auxOwner = owner.getId();
		}

		window = new GameWindow(boardPanel, new SettingsPanel(auxString,
				(randomPlayer == null), (aiPlayer == null), auxOwner), owner);
		window.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				hasFocus = true;
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				hasFocus = false;
			}
		});
		window.setPieces(pieces);
		window.setGameChangesListener(this);
		owner = viewPiece;
		observable.addObserver(this);
		observable.addObserver(window);

		if (owner == null) {
			window.setTitle(game.gameDesc());
		} else {
			window.setTitle(game.gameDesc() + ": " + owner.getId());
		}
	}

	/**
	 * Sends the window to front if it is its turn.
	 */
	private void toFrontIfNeeded() {
		if (isTurn && !hasFocus) {
			window.toFront();
			window.requestFocus();
		}
	}
}
