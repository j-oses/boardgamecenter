package es.ucm.fdi.tp.practica6.views;

import java.awt.Color;

import javax.swing.JTable;

/**
 * A JTable subclass to make painting rows easier.
 * 
 * @author Jorge
 *
 */
public class CustomJTable extends JTable {
	private static final long serialVersionUID = 1L;

	/**
	 * Makes the user unable to edit cells.
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/**
	 * Contains the colors which will be used to paint the table by row
	 * index(the index of this array).
	 */
	private Color[] rowColors;

	public Color[] getRowColors() {
		return rowColors;
	}

	public void setRowColors(Color[] rowColors) {
		this.rowColors = rowColors;
	}

	/**
	 * Changes the color at a given index.
	 * 
	 * @param row
	 *            index.
	 * @param color
	 *            the new color.
	 */
	public void setRowColor(int row, Color color) {
		rowColors[row] = color;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(2, columnIndex).getClass();
	}
}
