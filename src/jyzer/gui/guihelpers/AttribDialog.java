/*
 * JYzer - A Java Bytecode Analyzer.
 * Copyright (C) 2005 Legendi Richard Oliver
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jyzer.gui.guihelpers;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 * A dialog to display any method/field attributes.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class AttribDialog extends JDialog {

	/**
	 * Creates a new modal AttribDialog.
	 *
	 * @param parent the owner of this dialog.
	 * @param message the message that has to be displayed.
	 * @param title the title of the dialog.
	 */
	public AttribDialog(JFrame parent, String message, String title) {
		add( new JScrollPane(ep) );

		Action exitAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), "pressed");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE,  0, false), "pressed");
		getRootPane().getActionMap().put("pressed", exitAction);

		StringBuilder sb = new StringBuilder();
		sb.append("<HTML>");
		sb.append(message);
		sb.append("</HTML>");
		ep.setText( sb.toString() );

		ep.setCaretPosition(0);
		setModal(true);
		ep.setEditable(false);
		setTitle(title);
		setResizable(false);

		pack();

		Dimension dim = getSize();
		if (dim.width > 500) {
			dim.width = 500;
		}

		if ( dim.height > 500) {
			dim.height = 500;
		}

		setSize(dim);
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	// the only GUI component
	private JEditorPane ep = new JEditorPane("text/html", null);

}// class.AttribDialog
