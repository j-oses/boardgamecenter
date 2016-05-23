package es.ucm.fdi.tp.practica5.views;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica5.control.VisualPlayer;
import es.ucm.fdi.tp.practica5.model.MoveGenerator.MoveListener;

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
public class SwingView implements MoveListener,
		GameObserver, GameWindow.MoveGenerationChangesListener, GameWindow.QuitRestartListener, GameWindow.GameModeListener {
	private Controller controller;
	private GameWindow window;
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
	 * Creates a swing view with the associated controller and owner.
	 * @param controller the controller which manages the game.
	 * @param owner the owner of the window. {@code null} if is a multiview window.
	 */
	public SwingView(Controller controller, Piece owner) {
		this.controller = controller;
		this.owner = owner;
		playerForMode = new HashMap<>();
		players = new HashMap<>();
	}

	/**
	 * Requests a move to the player provided and executes it.
	 *
	 * @param p the player to which the move will be requested.
	 */
	public void makeMove(final Player p) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							controller.makeMove(p);
							return null;
						}
					}.execute();
					// controller.makeMove(p);
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

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
							Piece turn) {
		players = new HashMap<>();
		for (Piece p : pieces) {
			this.players.put(p, PlayerMode.MANUAL);
		}
		window.setPieces(pieces);

		addSettingsToWindow();

		isTurn = (owner == null) || owner.equals(turn);
		currentMode = players.get(turn);
		currentPlayer = playerForMode.get(currentMode);

		if (owner == null) {
			window.setTitle(gameDesc);
		} else {
			window.setTitle(gameDesc + ": " + owner.getId());
		}

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
		final Player aiPlayer = playerForMode.get(PlayerMode.AI);
		if (aiPlayer != null) {
			makeMove(aiPlayer);
		}
	}

	@Override
	public void quitButtonPressed() {
		controller.stop();
	}

	@Override
	public void restartButtonPressed() {
		controller.restart();
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
		playerForMode.put(PlayerMode.RANDOM, randomPlayer);
		playerForMode.put(PlayerMode.AI, aiPlayer);

		window = new GameWindow(boardPanel, owner);
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

		window.setGameModeListener(this);
		window.setQuitRestartListener(this);
		window.setMoveGenerationChangesListener(this);

		owner = viewPiece;
		observable.addObserver(this);
		observable.addObserver(window);
	}

	/**
	 * Adds the setingsPanel to the window when it has the necessary info(pieces)
	 */
	private void addSettingsToWindow() {
		String auxOwner;
		if (owner == null) {
			auxOwner = null;
		} else {
			auxOwner = owner.getId();
		}

		ArrayList<String> auxPlayerString = new ArrayList<>();
		for (Piece key : players.keySet()) {
			auxPlayerString.add(key.toString());
		}

		String auxString[] = new String[auxPlayerString.size()];
		auxString = auxPlayerString.toArray(auxString);

		SettingsPanel settings = new SettingsPanel(auxString,
				(playerForMode.get(PlayerMode.RANDOM) == null),
				(playerForMode.get(PlayerMode.AI) == null), auxOwner);
		window.setSettingsPanel(settings);
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
