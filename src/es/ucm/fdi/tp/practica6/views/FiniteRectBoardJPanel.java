package es.ucm.fdi.tp.practica6.views;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

import java.awt.*;

/**
 * Manages and displays a finite rectangular board in the screen.
 * @author Álvaro
 *
 */
public class FiniteRectBoardJPanel extends BoardJPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public FiniteRectBoardJPanel() {
		super();
		this.setBackground(Color.darkGray);
		this.setPreferredSize(new Dimension(400, 400));
	}
	
	@Override
	public void update() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece p = board.getPosition(i, j);
				if (p != null) {
					pieceViews[i][j].setColor(appearanceMap.getColorFor(p));
					pieceViews[i][j].setShape(appearanceMap.getShapeFor(p));
				} else {
					pieceViews[i][j].setShape(null);
				}
			}
		}
		repaint();
	}
	
	@Override
	public void setBoard(Board board) {
		super.setBoard(board);
		removeAll();
		pieceViews = new PieceJPanel[board.getRows()][board.getCols()];
		this.setLayout(new GridLayout(board.getRows(), board.getCols(), 4, 4));
		
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				pieceViews[i][j] = new PieceJPanel(i, j);
				this.add(pieceViews[i][j]);
				pieceViews[i][j].setSelectionListener(this);
			}
		}
	}
}
