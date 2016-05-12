package es.ucm.fdi.tp.practica5.bgame.control;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * This is a Player subclass which simply wraps an already generated move. 
 * @author Álvaro
 *
 */
public class VisualPlayer extends Player {
	private static final long serialVersionUID = 1L;
	private GameMove move;
	
	/**
	 * Create a visual player with a move.
	 * @param move the move to be returned on request.
	 */
	public VisualPlayer(GameMove move) {
		this.move = move;
	}
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		
		return move;
	}

}
