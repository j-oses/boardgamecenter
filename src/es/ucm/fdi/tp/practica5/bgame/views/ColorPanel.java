package es.ucm.fdi.tp.practica5.bgame.views;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

/**
 * A panel which features a color chooser and a confirm button.
 * 
 * @author Jorge
 *
 */
public class ColorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JColorChooser colorChooser;

	public JColorChooser getColorChooser() {
		return colorChooser;
	}

	public ColorPanel() {
		super(new BorderLayout());

		// Set up color chooser for setting Piece color
		colorChooser = new JColorChooser();
		colorChooser.setBorder(BorderFactory
				.createTitledBorder("Choose Player Color"));
		colorChooser.setPreviewPanel(new JPanel());

		add(colorChooser, BorderLayout.CENTER);
	}
};
