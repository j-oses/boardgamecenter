package es.ucm.fdi.tp.practica6.connectn;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
import es.ucm.fdi.tp.practica6.model.MoveGenerator;
import es.ucm.fdi.tp.practica6.views.BoardJPanel;

/**
 * A move generator which translates board events into ConnectN moves.
 * 
 * @author �lvaro
 *
 */
public class ConnectNMoveGenerator extends MoveGenerator {
	public ConnectNMoveGenerator(MoveListener listener) {
		super(listener);
	}

	@Override
	public void pieceClickedAtPosition(int clickedRow, int clickedCol, boolean isEmpty,
			BoardJPanel boardPanel) {
		ConnectNMove move = new ConnectNMove(clickedRow, clickedCol, piece);
		setLastMove(move);
	}

	@Override
	public void reset() {
		// Does nothing
	}

	@Override
	public String moveHelp(Board board) {
		return "Click on an empty square to add a piece";
	}
}
