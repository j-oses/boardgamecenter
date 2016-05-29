package es.ucm.fdi.tp.practica5.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

/**
 * A panel which paints a piece.
 * 
 * @author �lvaro
 *
 */
public class PieceJPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * The listener for clicks on pieces.
	 * 
	 * @author �lvaro
	 *
	 */
	public interface PieceSelectionListener {
		/**
		 * Warns the listener that a piece has been clicked.
		 * 
		 * @param row
		 *            the row of the clicked piece.
		 * @param col
		 *            the column of the clicked piece.
		 * @param isEmpty
		 *            wether there is a piece in that position.
		 */
		void pieceClickedAtPosition(int row, int col, boolean isEmpty);
	}

	private PieceSelectionListener selectionListener;
	private Color color;
	private RectangularShape shape;
	private int row;
	private int column;
	private boolean selected;
	private Color unselectedBackground;

	/**
	 * Creates an empty piece panel for a given position.
	 * 
	 * @param row
	 *            in which the piece is.
	 * @param col
	 *            in which the piece is.
	 */
	public PieceJPanel(int row, int col) {
		this.color = Color.black;
		this.shape = null;
		this.row = row;
		this.column = col;
		commonInit();
	}

	/**
	 * Creates a circular piece panel with the given color and for a given
	 * position.
	 * 
	 * @param color
	 *            the fill color.
	 * @param row
	 *            in which the piece is.
	 * @param col
	 *            in which the piece is.
	 */
	public PieceJPanel(Color color, int row, int col) {
		this.color = color;
		this.shape = new Ellipse2D.Double(0, 0, 0, 0);
		this.row = row;
		this.column = col;
		commonInit();
	}

	/**
	 * Creates a circular piece panel with the given color and shape.
	 * 
	 * @param color
	 *            the fill color.
	 * @param shape
	 *            the shape.
	 * @param row
	 *            in which the piece is.
	 * @param col
	 *            in which the piece is.
	 */
	public PieceJPanel(Color color, RectangularShape shape, int row, int col) {
		this.color = color;
		this.shape = shape;
		this.row = row;
		this.column = col;
		commonInit();
	}

	/**
	 * Groups actions which should be performed by each constructor.
	 */
	private void commonInit() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					// The left button is the only one which generates
					// movements, and which is listened to.
					notifyListener();
				}
			}
		});

		this.setBackground(Color.lightGray);
	}

	/**
	 * Modifies the color the piece is painted with.
	 * 
	 * @param color
	 *            the new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Modifies the shape the piece is painted with.
	 * 
	 * @param shape
	 *            the new shape.
	 */
	public void setShape(RectangularShape shape) {
		this.shape = shape;
	}

	@Override
	public void setBackground(Color backgroundColor) {
		super.setBackground(backgroundColor);
		unselectedBackground = new Color(backgroundColor.getRGB());
	}

	/**
	 * Visually selects a piece.
	 * 
	 * @param selected
	 *            wether the piece is selected or not.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			super.setBackground(unselectedBackground.darker());
		} else {
			super.setBackground(unselectedBackground);
		}
		repaint();
	}

	/**
	 * Get the selection state.
	 * 
	 * @return wether the piece is selected or not.
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * Sets the selection listener.
	 * 
	 * @param selectionListener
	 *            the new listener. Should not be {@code null}.
	 */
	public void setSelectionListener(PieceSelectionListener selectionListener) {
		if (selectionListener == null) {
			throw new NullPointerException("Null Observer");
		}

		this.selectionListener = selectionListener;
	}

	/**
	 * Warns the listener that a piece has been clicked.
	 */
	private void notifyListener() {
		if (selectionListener != null) {
			selectionListener
					.pieceClickedAtPosition(row, column, shape == null);
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (shape != null) {
			Graphics2D g2d = (Graphics2D) g;

			shape.setFrame(1, 1, getWidth() - 3, getHeight() - 3);

			// Setup antialiasing
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Paint the piece with the specified shape, black border and filled
			// by the specified color.
			g2d.setColor(color);
			g2d.fill(shape);
			g2d.setColor(Color.black);
			g2d.draw(shape);
		}
	}
}
