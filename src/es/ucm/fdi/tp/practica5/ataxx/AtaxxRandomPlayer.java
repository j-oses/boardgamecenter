package es.ucm.fdi.tp.practica5.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxRandomPlayer extends Player {
	private static final long serialVersionUID = 1L;

	@Override
	/**
	 * @param board the board
	 * @param p the piece associated with the player
	 * @param pieces the pieces owned by that player
	 * @param rules the board
	 * Makes a random move when the pieces and the rules are provided
	 */
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		if (board.isFull()) {
			throw new GameError(
					"The board is full, cannot make a random move!!");
		}

		List<GameMove> possibleMoves = rules.validMoves(board, pieces, p);

		if (possibleMoves.size() == 0) {
			throw new GameError(
					"Cannot make a random move, there are no possible moves!!");
		}

		int randomIndex = Utils.randomInt(possibleMoves.size());

		return possibleMoves.get(randomIndex);
	}

}
