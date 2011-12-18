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
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import jyzer.gui.guihelpers.HexaViewTableModel;

/**
 * A class for make the (selected+16)th row colored.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
class ColorRenderer extends JLabel implements TableCellRenderer {

    public ColorRenderer() {
        setOpaque(true); //MUST do this for background to show up.
    }

    public Component getTableCellRendererComponent( JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(Color.blue);
        return this;
    }

}// class.ColorRenderer

/**
 * This component is a hexaview pane. For performance reasons it would be useful to use only a simple textarea instead of the table,
 * but I want to make it clear that which byte is at which position & what is its value.
 *
 * <p>TODO: make the selected + 16th cell bordered/colored.</p>
 * <p>TODO: make visible rows at indexes 1, 9, 17, 25.</p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class HexaViewPane extends JComponent {

	// --- Own variables -------------------------------------------

	private int selectedRow, selectedColumn;

	// --- Constructors --------------------------------------------

	/**
	 * Constructor, creates a new instance of HexaViewPane.
	 */
	public HexaViewPane() {
		super();

		buildUpGUI();
		setDefaultValues();
	}

	// --- GUI building ---------------------------------------------

	/**
	 * Building up the core GUI.
	 */
	private void buildUpGUI() {
		setLayout( new BorderLayout() );
		add(mainPanel, BorderLayout.CENTER);

		mainPanel.setLayout( new BorderLayout() );
		mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		//Ask to be notified of selection changes.
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					return; //no rows are selected
				} else {
					//selectedRow is selected
					selectedRow = lsm.getMinSelectionIndex();
					selectedColumn = e.getFirstIndex();
				}
			}
		});

	}// buildUpGUI

	/**
	 * To set the default values of the components.
	 */
	private void setDefaultValues() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(false);
	}

	// --- Helper methods ----------------------------------------------------------

	/**
	 * Clears the displayed data on the pane.
	 */
	public void clear() {
		table.setModel( new HexaViewTableModel() );
		refresh();
	}

	/**
	 * Sets the table column widths after refreshing the table contents.
	 */
	private void refresh() {
		if ( 0 == table.getColumnCount() ) {
			return;
		}

		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(0).setMaxWidth(350);
	}

	/**
	 * Setting the information displayed in the table.
	 */
	public void setData(File file) throws IOException {
		HexaViewTableModel model = new HexaViewTableModel(file);
		table.setModel(model);
		refresh();
	}

	// --- GUI things ----------------------------------------------------------------

	private JPanel mainPanel = new JPanel();
	private ColorRenderer colorRenderer = new ColorRenderer();
	private JTable table = new JTable() {
		/*
		// Doesn't working yet ...
		public TableCellRenderer getCellRenderer(int row, int column) {
			if ( (row == (selectedRow+16) ) && (column == selectedColumn) ) {
				return colorRenderer;
			}

			return super.getCellRenderer(row, column);
		}
		*/
	};

}// class.HexaViewPane
