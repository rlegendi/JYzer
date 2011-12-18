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
package jyzer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

/**
 * This component is used to display general information about the analyzed classfile, that is fancy a bit & can be saved into a file.
 *
 * <p>TODO: make a navbar at the bottom of the page.</p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class SummaryPane extends JComponent {

	/**
	 * This 'll be used for the navigation in the future.
	 */
	class Hyperactive implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				JEditorPane pane = (JEditorPane) e.getSource();
				if (e instanceof HTMLFrameHyperlinkEvent) {
					HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
					HTMLDocument doc = (HTMLDocument)pane.getDocument();
					doc.processHTMLFrameHyperlinkEvent(evt);
				} else {
					try {
						 pane.setPage(e.getURL());
					} catch (Throwable t) {
						 t.printStackTrace();
					}
				}
			}
		}
	}// class.Hyperactive

	// --- Constructors ------------------------------------------

	/**
	 * Creates a new instance of SummaryPane with an empty document.
	 */
	public SummaryPane() {
		buildUpGUI();
		setDefaultValues();
		//summaryEditorPane.addHyperlinkListener( new Hyperactive() );
	}

	// --- GUI building ------------------------------------------

	/**
	 * Building up the core GUI.
	 */
	private void buildUpGUI() {
		setLayout( new BorderLayout() );
		add(new JScrollPane( summaryEditorPane) );
	}

	/**
	 * Setting the default values of the components.
	 */
	private void setDefaultValues() {
		summaryEditorPane.setContentType("text/html; charset=iso-8859-2");
		summaryEditorPane.setEditable(false);
	}

	// --- Helper methods -------------------------------------------------

	/**
	 * Clears the displayed data on the pane.
	 */
	public void clear() {
		// setText() is buggy due html formatting :-(
		// need this little workaround
		summaryEditorPane.setDocument( summaryEditorPane.getEditorKit().createDefaultDocument() );
	}

	/**
	 * Gives the description.
	 *
	 * @return the text of the editorpane.
	 */
	public String getText() {
		return summaryEditorPane.getText();
	}

	/**
	 * Sets the text that is displayed at the editorpane.
	 */
	public void setText(final String text) {
		clear();
		summaryEditorPane.setText(text);
		summaryEditorPane.setCaretPosition(0);
	}

	// --- GUI variables --------------------------------------------------------------

	private JEditorPane summaryEditorPane = new JEditorPane();

}// class.SummaryPane
