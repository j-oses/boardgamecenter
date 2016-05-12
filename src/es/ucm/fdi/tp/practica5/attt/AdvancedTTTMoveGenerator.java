package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.practica5.bgame.model.MoveGenerator;
import es.ucm.fdi.tp.practica5.bgame.views.BoardJPanel;

/**
 * A move generator which translates board events into Advanced Tic Tac Toe
 * moves.
 * 
 * @author Álvaro
 *
 */
public class AdvancedTTTMoveGenerator extends MoveGenerator {
	private int iniRow = -1;
	private int iniCol = -1;

	public AdvancedTTTMoveGenerator(MoveListener listener) {
		super(listener);
	}

	@Override
	public void pieceClickedAtPosition(int clickedRow, int clickedCol,
			boolean isEmpty, BoardJPanel boardPanel) {
		if (iniRow != -1) {
			boardPanel.selectPiece(iniRow, iniCol, false);
		}

		if (isEmpty) {
			AdvancedTTTMove move = new AdvancedTTTMove(iniRow, iniCol,
					clickedRow, clickedCol, piece);
			setLastMove(move);
			iniRow = -1;
			iniCol = -1;
		} else {
			iniRow = clickedRow;
			iniCol = clickedCol;
			boardPanel.selectPiece(clickedRow, clickedCol, true);
		}
	}

	@Override
	public void reset() {
		iniRow = -1;
		iniCol = -1;
	}

	@Override
	public String moveHelp(Board board) {
		int count = board.getPieceCount(piece);
		// For some unknown reason, the advanced tic tac toe doesn't count the
		// pieces in the board but 3 - #pieces.
		if (count > 0) {
			return "Click on an empty square to add a piece";
		} else {
			return "Click on a piece to select it, and on an empty square to move it";
		}
	}
}
