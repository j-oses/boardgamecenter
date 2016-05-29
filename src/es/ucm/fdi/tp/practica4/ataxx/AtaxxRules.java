package es.ucm.fdi.tp.practica4.ataxx;

import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica4.model.CountingFiniteRectBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AtaxxRules implements GameRules {
	protected final Pair<State, Piece> gameInPlayResult = new Pair<State, Piece>(
			State.InPlay, null);

	/**
	 * The default string identifier for a obstacle's piece.
	 */
	public static final String OBSTACLE_IDENTIFIER = "*";
	/**
	 * The piece that is associated with the obstacles
	 */
	protected Piece obstacle;

	/**
	 * Number of obstacles
	 */
	protected int numObstacles;
	/**
	 * Board size, also represents rows and columns (size = dimXdim)
	 */
	private int dim;

	/**
	 * Constructs the AtaxxRules object with a dimension
	 * 
	 * @param dim
	 *            the dimension of the board
	 */
	public AtaxxRules(int dim) {
		this(dim, 0);
	}

	/**
	 * Constructs the AtaxxRules object with a dimension and some obstacles
	 * 
	 * @param dim
	 *            the dimension of the board
	 * @param obstacles
	 *            the number of obstacles
	 */
	public AtaxxRules(int dim, int obstacles) {
		this.dim = dim;
		this.obstacle = new Piece(OBSTACLE_IDENTIFIER);
		this.numObstacles = obstacles;
	}

	@Override
	/**
	 * Suited for Ataxx info
	 */
	public String gameDesc() {
		return "Ataxx " + dim + "x" + dim;
	}

	@Override
	/**
	 * Creates different boards depending on the number of players
	 */
	public Board createBoard(List<Piece> pieces) {
		if (pieces.size() == 2) {
			return create2PlayerBoard(pieces);
		} else if (pieces.size() == 3) {
			return create3PlayerBoard(pieces);
		} else if (pieces.size() == 4) {
			return create4PlayerBoard(pieces);
		}
		return null; // This should never be reached
	}

	/**
	 * 
	 * @param stan
	 *            the row/column given
	 * @return the mirrored row/column of stan
	 */
	private int mirror(int stan) {
		return dim - 1 - stan;
	}

	/**
	 * Crea un board con obstaculo
	 * 
	 * @return
	 */
	private Board createBoardWithObstacles() {
		// A CountingFiniteRectBoard handles the counting automagically
		Board auxBoard = new CountingFiniteRectBoard(dim, dim);
		ArrayList<Integer> list = new ArrayList<Integer>();

		int boardSize = (dim - 1) / 2;

		for (int i = 1; i < boardSize * boardSize; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		for (int i = 0; i < numObstacles / 4; i++) {
			int randy = list.get(i) % boardSize;
			int marsh = list.get(i) / boardSize;

			auxBoard.setPosition(randy, marsh, obstacle);
			auxBoard.setPosition(mirror(randy), marsh, obstacle);
			auxBoard.setPosition(randy, mirror(marsh), obstacle);
			auxBoard.setPosition(mirror(randy), mirror(marsh), obstacle);
		}

		return auxBoard;
	}

	/**
	 * 
	 * @param pieces
	 *            the pieces that have to be set on the board it returns
	 * @return a new Board suited for two players
	 */
	private Board create2PlayerBoard(List<Piece> pieces) {
		Board auxBoard = createBoardWithObstacles();

		auxBoard.setPosition(0, 0, pieces.get(0));
		auxBoard.setPosition(dim - 1, dim - 1, pieces.get(0));
		auxBoard.setPosition(0, dim - 1, pieces.get(1));
		auxBoard.setPosition(dim - 1, 0, pieces.get(1));
		return auxBoard;
	}

	/**
	 * 
	 * @param pieces
	 *            the pieces that have to be set on the board it returns
	 * @return a new Board suited for three players
	 */
	private Board create3PlayerBoard(List<Piece> pieces) {
		Board auxBoard = create2PlayerBoard(pieces);

		auxBoard.setPosition((dim - 1) / 2, 0, pieces.get(2));
		auxBoard.setPosition((dim - 1) / 2, dim - 1, pieces.get(2));
		return auxBoard;
	}

	/**
	 * 
	 * @param pieces
	 *            the pieces that have to be set on the board it returns
	 * @return a new Board suited for four players
	 */
	private Board create4PlayerBoard(List<Piece> pieces) {
		Board auxBoard = create3PlayerBoard(pieces);

		auxBoard.setPosition(0, (dim - 1) / 2, pieces.get(3));
		auxBoard.setPosition(dim - 1, (dim - 1) / 2, pieces.get(3));
		return auxBoard;
	}

	/**
	 * 
	 */
	public Piece initialPlayer(Board board, List<Piece> pieces) {
		return pieces.get(0);
	}

	/**
	 * The minimum number of players
	 */
	public int minPlayers() {
		return 2;
	}

	/**
	 * The maximum number of players
	 */
	@Override
	public int maxPlayers() {
		return 4;
	}

	/**
	 * Updates the state of the game to the current state of the game(won,draw)
	 */
	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces,
			Piece turn) {
		if (board.isFull()) {
			return winnerOrDrawForFullBoard(board, pieces);
		} else {
			return winnerOrInPlayForNonFullBoard(board, pieces);
		}
	}

	/**
	 * 
	 * @param board has to be full
	 * @param pieces
	 * @return the winner(if not draw) or the state of draw(if draw)
	 */
	private Pair<State, Piece> winnerOrDrawForFullBoard(Board board,
			List<Piece> pieces) {
		// Check who has wonnered very good englando
		boolean severalWinners = false;
		Piece winner = null;
		int maxScore = 0;
		for (Piece piece : pieces) {
			int count = board.getPieceCount(piece);

			if (count > maxScore) {
				severalWinners = false;
				winner = piece;
				maxScore = count;
			} else if (count == maxScore) {
				severalWinners = true;
			}
		}

		if (severalWinners) {
			return new Pair<State, Piece>(State.Draw, null);
		} else {
			return new Pair<State, Piece>(State.Won, winner);
		}
	}

	/**
	 * 
	 * @param board
	 *            which is not full
	 * @param pieces
	 * @return the result of the game for a board which is not full
	 */
	private Pair<State, Piece> winnerOrInPlayForNonFullBoard(Board board,
			List<Piece> pieces) {
		// Check if only one player has pieces left
		int num0left = 0;
		Piece leftPiece = null;
		for (Piece piece : pieces) {
			if (board.getPieceCount(piece) == null
					|| board.getPieceCount(piece) == 0) {
				num0left++;
			} else {
				leftPiece = piece;
			}
		}

		if (num0left == pieces.size() - 1) {
			return new Pair<State, Piece>(State.Won, leftPiece);
		} else {
			return gameInPlayResult;
		}
	}

	@Override
	/**
	 * Adapted for Ataxx
	 */
	public Piece nextPlayer(Board board, List<Piece> pieces, Piece lastPlayer) {
		int i = pieces.indexOf(lastPlayer);
		Piece next = pieces.get((i + 1) % pieces.size());

		while (!board.isFull() && validMoves(board, pieces, next).size() == 0) {
			i++;
			next = pieces.get((i + 1) % pieces.size());
		}

		return next;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		return 0;
	}

	@Override
	/** 
	 * @return moves which contains the valid moves of a certain player
	 */
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces,
			Piece turn) {
		List<GameMove> moves = new ArrayList<GameMove>();

		if (!board.isFull()) {
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getCols(); j++) {
					if (board.getPosition(i, j) != null && turn.equals(board.getPosition(i, j))) {
						moves.addAll(validMoves(board, playersPieces, turn, i, j));
					}
				}
			}
		}

		return moves;
	}

	/**
	 * Valid moves for a certain piece in a certain position
	 * 
	 * @param board
	 * @param playersPieces
	 * @param turn
	 * @param posI
	 * @param posJ
	 * @return
	 */
	private List<GameMove> validMoves(Board board, List<Piece> playersPieces,
			Piece turn, int posI, int posJ) {
		List<GameMove> moves = new ArrayList<GameMove>();
		int startI = Math.max(0, posI - 2);
		int startJ = Math.max(0, posJ - 2);
		int endI = Math.min(board.getRows() - 1, posI + 2);
		int endJ = Math.min(board.getCols() - 1, posJ + 2);

		for (int i = startI; i <= endI; i++) {
			for (int j = startJ; j <= endJ; j++) {
				if ((i != posI || j != posJ) && board.getPosition(i, j) == null) {
					moves.add(new AtaxxMove(posI, posJ, i, j, turn));
				}
			}
		}

		return moves;
	}
}
