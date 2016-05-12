package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.practica5.bgame.model.MoveGenerator;
import es.ucm.fdi.tp.practica5.bgame.views.BoardJPanel;

/**
 * A move generator which translates board events into Ataxx moves.
 * 
 * @author Álvaro
 *
 */
public class AtaxxMoveGenerator extends MoveGenerator {
	private int iniRow = -1;
	private int iniCol = -1;

	public AtaxxMoveGenerator(MoveListener listener) {
		super(listener);
	}

	@Override
	public void pieceClickedAtPosition(int clickedRow, int clickedCol, boolean isEmpty,
			BoardJPanel boardPanel) {
		if (iniRow != -1) {
			boardPanel.selectPiece(iniRow, iniCol, false);
			if (isEmpty) {
				AtaxxMove move = new AtaxxMove(iniRow, iniCol, clickedRow, clickedCol, piece);
				setLastMove(move);
				iniRow = -1;
				iniCol = -1;
			} else {
				iniRow = clickedRow;
				iniCol = clickedCol;
				boardPanel.selectPiece(clickedRow, clickedCol, true);
			}
		} else {
			if (!isEmpty) {
				iniRow = clickedRow;
				iniCol = clickedCol;
				boardPanel.selectPiece(clickedRow, clickedCol, true);
			}
		}
	}

	@Override
	public void reset() {
		iniRow = -1;
		iniCol = -1;
	}

	public String moveHelp(Board board) {
		return "Select a piece and then click on an empty square to move it";
	}
}
