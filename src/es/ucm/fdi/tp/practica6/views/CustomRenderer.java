package es.ucm.fdi.tp.practica6.views;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A custom renderer to make changing the table appearance easier.
 * 
 * @author Jorge
 *
 */
public class CustomRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		table = (CustomJTable) table;
		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		c.setBackground(((CustomJTable) table).getRowColors()[row]);

		return c;
	}

}