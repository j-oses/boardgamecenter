package es.ucm.fdi.tp.practica5.bgame.model;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.views.BoardJPanel;

/**
 * This class managed the generation of moves for a game. It is intended to be
 * subclassed by each game to provide game-specific move generation.
 * 
 * @author Álvaro
 *
 */
public abstract class MoveGenerator {
	private MoveListener listener;
	private GameMove move;
	protected Piece piece;

	/**
	 * The listener for a move generation event. It is notified when a new move
	 * is generated.
	 * 
	 * @author Álvaro
	 *
	 */
	public interface MoveListener {
		/**
		 * Notifies the receiver that a move has been generated.
		 * 
		 * @param move
		 *            the generated move.
		 */
		void didGenerateMove(GameMove move);
	}

	/**
	 * Creates a move generator with the given listener.
	 * 
	 * @param listener
	 *            a listener.
	 */
	public MoveGenerator(MoveListener listener) {
		this.listener = listener;
	}

	/**
	 * This method should get called when a piece on the board is clicked.
	 * 
	 * @param clickedRow
	 *            the row of the piece.
	 * @param clickedCol
	 *            the column of the piece.
	 * @param isEmpty
	 *            true if there is nothing in that position, false otherwise.
	 * @param boardPanel
	 *            the board panel, which can be selected. The move generator
	 *            should manage the appropriate selections.
	 * @param board
	 *            the board associated with the panel, which is provided because
	 *            most games need to know the state of the board to generate
	 *            moves.
	 */
	abstract public void pieceClickedAtPosition(int clikedRow, int clickedCol, boolean isEmpty,
			BoardJPanel boardPanel);

	/**
	 * Sets the last move of the generator. This notifies the listener.
	 * 
	 * @param move
	 *            the new move.
	 */
	protected void setLastMove(GameMove move) {
		this.move = move;

		if (listener != null) {
			listener.didGenerateMove(move);
		}
	}

	/**
	 * Resets the move generator.
	 */
	abstract public void reset();

	/**
	 * Get the last move.
	 * 
	 * @return the last move.
	 */
	public GameMove getLastMove() {
		return this.move;
	}

	/**
	 * Sets the piece used for generating movements.
	 * 
	 * @param piece
	 *            the new piece.
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	/**
	 * Sets the listener.
	 * 
	 * @param listener
	 *            the new listener.
	 */
	public void setListener(MoveListener listener) {
		this.listener = listener;
	}

	/**
	 * Get a help message to make the move.
	 * 
	 * @param board
	 *            some games need to check the game state for custom messages.
	 * @return the help message.
	 */
	abstract public String moveHelp(Board board);
}
