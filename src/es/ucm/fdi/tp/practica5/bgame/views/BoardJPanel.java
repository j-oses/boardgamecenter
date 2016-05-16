package es.ucm.fdi.tp.practica5.bgame.views;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.model.MoveGenerator;
import es.ucm.fdi.tp.practica5.bgame.views.PieceJPanel.PieceSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

/**
 * A panel which manages a board. It provides basic properties & methods which
 * every board panel should implement. This panel doesn't actually paint
 * anything.
 * 
 * @author Álvaro
 *
 */
abstract public class BoardJPanel extends JPanel implements
		PieceSelectionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * An interface which dictates how should each piece be painted, determining
	 * both its color and its shape.
	 * 
	 * @author Álvaro
	 *
	 */
	public interface PieceAppearanceMap {
		/**
		 * The painting color for the given piece.
		 * 
		 * @param piece
		 *            the piece.
		 * @return the color the piece should be painted with.
		 */
		Color getColorFor(Piece piece);

		/**
		 * The shape for the given piece.
		 * 
		 * @param piece
		 *            the piece.
		 * @return the shape the piece should be painted with.
		 */
		RectangularShape getShapeFor(Piece piece);
	}

	protected Board board;
	protected PieceAppearanceMap appearanceMap;
	protected PieceJPanel[][] pieceViews;
	protected MoveGenerator moveGenerator;

	/**
	 * Creates an empty board panel with a default appearance map.
	 */
	public BoardJPanel() {
		this.appearanceMap = defaultAppearanceMap();

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (isEnabled()) {
						onRightMouseButtonClick();
					}
				}
			}
		});
	}

	/**
	 * Updates the board which is being shown.
	 */
	abstract public void update();

	/**
	 * Sets the board.
	 * 
	 * @param board
	 *            the new board.
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Get the board.
	 * 
	 * @return the board.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Sets the appearance map.
	 * 
	 * @param appearanceMap
	 *            the new appearance map.
	 */
	public void setAppearanceMap(PieceAppearanceMap appearanceMap) {
		this.appearanceMap = appearanceMap;
	}

	/**
	 * Get the color the appearance map returns for a given piece.
	 * 
	 * @param piece
	 *            the piece.
	 * @return the color.
	 */
	public Color getColorForPiece(Piece piece) {
		return new Color(appearanceMap.getColorFor(piece).getRGB());
	}

	/**
	 * Sets the move generator.
	 * 
	 * @param generator
	 *            the new move generator.
	 */
	public void setMoveGenerator(MoveGenerator generator) {
		this.moveGenerator = generator;
	}

	/**
	 * Sets the piece of the move generator.
	 * 
	 * @param piece
	 *            the new piece.
	 */
	public void setMoveGeneratorPiece(Piece piece) {
		moveGenerator.setPiece(piece);
	}
	
	/**
	 * Get a help message to make the move.
	 * @return the message.
	 */
	public String getMoveHelp() {
		return moveGenerator.moveHelp(board);
	}

	@Override
	public void pieceClickedAtPosition(int row, int col, boolean isEmpty) {
		if (moveGenerator != null && isEnabled()) {
			moveGenerator.pieceClickedAtPosition(row, col, isEmpty, this);
		}
	}

	/**
	 * If there is a piece in the given position, marks it as selected.
	 * 
	 * @param row
	 *            the row of the piece.
	 * @param col
	 *            the column of the piece.
	 * @param selected
	 *            wether the piece is selected.
	 */
	public void selectPiece(int row, int col, boolean selected) {
		if (board != null) {
			if (isEnabled() && board.getPosition(row, col) != null) {
				pieceViews[row][col].setSelected(selected);
			}
		}
	}

	/**
	 * If the piece is selected, deselects it; else, selects it.
	 * 
	 * @param row
	 *            the row of the piece.
	 * @param col
	 *            the column of the piece.
	 */
	public void togglePieceSelection(int row, int col) {
		selectPiece(row, col, !pieceViews[row][col].isSelected());
	}

	/**
	 * Deselects all pieces on the board and resets the move generator.
	 */
	public void deselectAll() {
		if (board != null) {
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getCols(); j++) {
					selectPiece(i, j, false);
				}
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (!enabled) {
			deselectAll();
		}
	}

	/**
	 * Reset the move generator and deselects all pieces. To be called when the
	 * right mouse button is clicked over the board.
	 */
	private void onRightMouseButtonClick() {
		deselectAll();
		moveGenerator.reset();
	}

	/**
	 * Generates a default appearance map which returns circular pieces whose
	 * color is generated from the piece id.
	 * 
	 * @return the appearance map.
	 */
	private PieceAppearanceMap defaultAppearanceMap() {
		return new PieceAppearanceMap() {
			public Color getColorFor(Piece piece) {
				char first = piece.getId().charAt(0);
				double value = ((((int) first) % 16) * 16.0);
				return Color.getHSBColor((float) (value / 256.0), (float) 0.8,
						(float) 0.8);
			}

			public RectangularShape getShapeFor(Piece piece) {
				return new Ellipse2D.Double();
			}
		};
	}

	@Override
	public Dimension getPreferredSize() {
		// Returns a square which fits the superpanel
		Dimension dimension = super.getPreferredSize();
		Container parent = getParent();
		if (parent != null) {
			dimension = parent.getSize();
		} else {
			return new Dimension(10, 10);
		}
		int width = (int) dimension.getWidth();
		int height = (int) dimension.getHeight();
		int side = (width < height ? width : height);
		return new Dimension(side, side);
	}
}
