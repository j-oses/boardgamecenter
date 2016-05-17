package es.ucm.fdi.tp.practica5.bgame.views;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController.PlayerMode;
import es.ucm.fdi.tp.practica5.bgame.views.BoardJPanel.PieceAppearanceMap;
import es.ucm.fdi.tp.practica5.bgame.views.SettingsPanel.SettingsListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.HashMap;
import java.util.List;

/**
 * The frame which manages the whole game UI.
 * 
 * @author Jorge & Álvaro
 *
 */
public class GameWindow extends JFrame implements GameObserver,
		SettingsListener {
	private static final long serialVersionUID = 1L;

	private JSplitPane contents;
	private BoardJPanel boardPanel;
	private SettingsPanel settingsPanel;
	private List<Piece> pieces;
	private Piece owner;
	private Piece currentlyPlaying;
	private GameChangesListener changesListener;
	private boolean currentlyManual;
	private JPanel panel1;

	public interface GameChangesListener {
		/**
		 * Notifies the receiver of the random move button pressing.
		 */
		void randomMoveButtonPressed();

		/**
		 * Notifies the receiver of the AI move button pressing.
		 */
		void aiMoveButtonPressed();

		/**
		 * Notifies the receiver of the quit button being pressed.
		 */
		void quitButtonPressed();

		/**
		 * Notifies the receiver of the restart button being pressed.
		 */
		void restartButtonPressed();

		/**
		 * Notifies the receiver that a new mode has been selected for a player.
		 * 
		 * @param pieceId
		 *            the id of the player whose mode should be changed.
		 * @param mode
		 *            the new player mode.
		 */
		void selectedNewGameMode(String pieceId, String mode);
	}

	/**
	 * Creates a gameWindow from a board panel, a settings panel and the owner.
	 * 
	 * @param boardPanel
	 *            the panel that shows the board.
	 * @param otherSettings
	 *            the panel that shows the settings.
	 * @param owner
	 *            the player to which the window belongs. {@code null} if this
	 *            window manages multiple players.
	 */
	public GameWindow(BoardJPanel boardPanel, SettingsPanel otherSettings,
			Piece owner) {
		contents = new JSplitPane();
		contents.setBounds(100, 100, 600, 400);
		contents.setPreferredSize(new Dimension(600, 400));
		contents.setBorder(new EmptyBorder(5, 5, 5, 5));
		contents.setResizeWeight(1.0);

		this.setContentPane(contents);

		this.boardPanel = boardPanel;
		JPanel boardContainer = new JPanel(new GridBagLayout());
		boardContainer.add(boardPanel);
		contents.setLeftComponent(boardContainer);

		this.settingsPanel = otherSettings;// contains all the other pannels
		contents.setRightComponent(settingsPanel);
		settingsPanel.setSettingsListener(this);

		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				onQuit();
			}
		});

		this.owner = owner;
		this.currentlyManual = true;
	}

	/**
	 * Change the list of pieces.
	 * 
	 * @param pieces
	 *            the new pieces.
	 */
	public void setPieces(List<Piece> pieces) {
		this.pieces = pieces;
	}

	/**
	 * Sets the game changes listener.
	 * 
	 * @param changesListener
	 *            the new listener.
	 */
	public void setGameChangesListener(GameChangesListener changesListener) {
		this.changesListener = changesListener;
	}

	/**
	 * Set the boolean which marks if the mode of the current player is manual.
	 * Calling this method will enable or disable the panels as needed. The
	 * GameWindow cannot work it out by itself in the multiviews option, because
	 * it knows nothing about how the game is going. This method is intended to
	 * be called by the controller.
	 * 
	 * @param currentlyManual
	 *            the updated value
	 */
	public void setCurrentlyManual(boolean currentlyManual) {
		this.currentlyManual = currentlyManual;
		enableUserInput(currentlyPlaying);
	}

	/**
	 * Disables user input that affects other windows aside from his own during
	 * moves
	 */
	public void disableUserInput() {
		boardPanel.setEnabled(false);
		settingsPanel.setPanelsEnabled(false);
	}

	/**
	 * Enables the necessary user input, depending on whose turn it is and on
	 * the settings.
	 * 
	 * @param turn
	 *            the piece which is currently playing.
	 */
	public void enableUserInput(Piece turn) {
		boolean enableMoveInput = ((owner == null) || owner.equals(turn))
				&& currentlyManual;
		boardPanel.setEnabled(enableMoveInput);
		settingsPanel.setPanelsEnabled(enableMoveInput);
	}

	// GAME OBSERVER METHODS
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		this.pieces = pieces;
		boardPanel.setAppearanceMap(pieceAppearanceMap("", null));
		boardPanel.setMoveGeneratorPiece(turn);
		boardPanel.setBoard(board);

		currentlyPlaying = turn;
		String you = (turn.equals(owner) ? " (you) " : " ");
		settingsPanel.addLineToStatus("It's " + currentlyPlaying.toString()
				+ "'s" + you + "turn");
		
		if (((owner == null) || owner.equals(turn)) && currentlyManual) {
			settingsPanel.addLineToStatus(boardPanel.getMoveHelp());
		}

		for (Piece p : pieces) {
			int playerRow = settingsPanel.getPlayerRow(p.toString());
			Integer count = boardPanel.getBoard().getPieceCount(p);
			if (count != null) {
				settingsPanel.updatePlayerScore(
						settingsPanel.getPlayerRow(p.toString()), count);
			}
			settingsPanel.updatePlayerColor(playerRow,
					boardPanel.getColorForPiece(p));
		}

		boardPanel.update();

		enableUserInput(turn);

		pack();
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		if (state == State.Won) {
			JOptionPane.showMessageDialog(this, winner.toString()
					+ " is the winner!");
			settingsPanel
					.addLineToStatus(winner.toString() + " is the winner!");
		} else if (state == State.Draw) {
			JOptionPane.showMessageDialog(this, "The game ended in a draw!");
			settingsPanel.addLineToStatus("The game ended in a draw!");
		}
		
		settingsPanel.setQuitPanelEnabled(true);
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		disableUserInput();
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		boardPanel.setBoard(board);
		boardPanel.update();
		revalidate();

		enableUserInput(turn);

		for (Piece p : pieces) {
			Integer count = boardPanel.getBoard().getPieceCount(p);
			if (count != null) {
				settingsPanel.updatePlayerScore(
						settingsPanel.getPlayerRow(p.toString()), count);
			}
		}
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		boardPanel.setBoard(board);
		boardPanel.update();
		revalidate();

		currentlyPlaying = turn;
		
		String you = (turn.equals(owner) ? " (you) " : " ");
		boardPanel.setMoveGeneratorPiece(turn);
		boardPanel.deselectAll();

		if (owner != null) {
			settingsPanel.setAutoMovesEnabled(false);
		}

		settingsPanel.addLineToStatus("It's " + currentlyPlaying.toString()
				+ "'s" + you + "turn");

		enableUserInput(turn);

		if (((owner == null) || owner.equals(turn)) && currentlyManual) {
			settingsPanel.addLineToStatus(boardPanel.getMoveHelp());
		}
	}

	@Override
	public void onError(String msg) {
		if (owner == null || owner.equals(currentlyPlaying)) {
			settingsPanel.addLineToStatus(msg);
			JOptionPane.showMessageDialog(this, msg);
		}
	}

	
	// SETTINGS LISTENER METHODS
	@Override
	public void onRandomMove() {
		if (changesListener != null) {
			disableUserInput();
			changesListener.randomMoveButtonPressed();
		}
	}

	@Override
	public void onAiMove() {
		if (changesListener != null) {
			disableUserInput();
			changesListener.aiMoveButtonPressed();
		}
	}

	@Override
	public void onQuit() {
		int confirmQuitButton = JOptionPane.YES_NO_OPTION;
		int result = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Warning", confirmQuitButton);
		if (result == JOptionPane.YES_OPTION) {
			settingsPanel.addLineToStatus(currentlyPlaying.toString()
					+ " has quit!");

			if (changesListener != null) {
				changesListener.quitButtonPressed();
			}
		}
	}

	@Override
	public void onRestart() {
		changesListener.restartButtonPressed();
	}

	@Override
	public void onColorChange(String pieceId, Color color) {
		boardPanel.setAppearanceMap(pieceAppearanceMap(pieceId, color));
		settingsPanel.updatePlayerColor(
				settingsPanel.getPlayerRow(pieceId.toString()), color);

		boardPanel.update();

	}

	@Override
	public void onModeChange(String mode, String pieceId) {
		settingsPanel.updatePlayerMode(
				settingsPanel.getPlayerRow(pieceId.toString()), mode);

		if (changesListener != null) {
			changesListener.selectedNewGameMode(pieceId, mode);
		}

		currentlyManual = (mode == PlayerMode.MANUAL.toString());
	}

	/**
	 * Generate a new piece appearance map, which differs with the previous one
	 * only by the color of one piece.
	 * 
	 * @param pieceId
	 *            the piece whose color is to be changed.
	 * @param newColor
	 *            the new color.
	 * @return the new appearance map.
	 */
	private PieceAppearanceMap pieceAppearanceMap(String pieceId, Color newColor) {
		final HashMap<Piece, Color> colorHash = new HashMap<>();
		for (Piece p : pieces) {
			if (p.getId() == pieceId) {
				colorHash.put(p, newColor);
			} else {
				colorHash.put(p, boardPanel.getColorForPiece(p));
			}
		}

		return new PieceAppearanceMap() {
			@Override
			public Color getColorFor(Piece piece) {
				Color color = colorHash.get(piece);
				if (color == null) {
					// A color for unknown pieces
					return new Color(204, 82, 0);
				}
				return new Color(color.getRGB());
			}

			@Override
			public RectangularShape getShapeFor(Piece piece) {
				Color color = colorHash.get(piece);
				if (color == null) {
					// A shape for unknown (non-player) pieces
					return new Rectangle2D.Double();
				}
				// All known (player) pieces are circular
				return new Ellipse2D.Double();
			}
		};
	}

}
