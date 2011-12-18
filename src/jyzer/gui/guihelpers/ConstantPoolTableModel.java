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

import javax.swing.table.*;

/**
 * The table model that is used by the constant pool pane.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class ConstantPoolTableModel extends AbstractTableModel {

	// --- Variables --------------------------

	private static final String[] columnNames = {
		"Index", "Type", "Value"
	};

	private Object data[][]; // the filtered data
	private Object fullData[][]; // the full data

	// --- Constructors --------------------------

	/**
	 * Creates a new (empty) model.
	 */
	public ConstantPoolTableModel() {
	}

	/**
	 * Creates a new model with the given data in it.
	 *
	 * @param data the objects that has to be displayed in the table.
	 */
	public ConstantPoolTableModel( Object data[][] ) {
		this.data = data;
		fullData  = data;
	}

	// --- Super methods --------------------------

    /**
     *  Returns false. This is the default implementation for all cells.
     *
     *  @param row the row being queried.
     *  @param col the column being queried.
     *  @return false.
     */
    public boolean isCellEditable(int row, int col) {
		return false;
	}

    /**
     *  Returns a default name for the column.
     *
     * @param col the column being queried.
     * @return a string containing the default name of <code>column</code>.
     */
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

	/**
	 * Returns the number of rows in the table.
	 */
    public int getRowCount() {
		if (null == data) {
			return 0;
		}

		return data.length;
	}

	/**
	 * Returns the number of columns in the table.
	 */
    public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Returns the string from the table at the given position.
	 *
	 * @param row the number of the row.
	 * @param col the number of the col.
	 * @return the string at the specified position.
	 */
    public Object getValueAt(int row, int col) {
		if (null == data) {
			return null;
		}

		// something buggy here ... :-(
		if ( row < 0 || data.length <= row || col < 0 || data[0].length <= col ) {
			return null;
		}

        return data[row][col];
    }

	// --- Own methods --------------------------

	/**
	 * Returns if the model contains nothing.
	 */
	public boolean isEmpty() {
		return (null == data || 0 == data.length);
	}

	/**
	 * Returns the 3rd column as an array of strings. Used for searching.
	 */
    public String[] getDescriptions() {
		if (null == data) {
			return null;
		}

		String back[] = new String[ getRowCount() ];
		for (int i=0; i<data.length; ++i) {
			back[i] = data[i][2].toString();
		}

		return back;
	}

	/**
	 * Returns the number of rows in the full data.
	 */
    public int getFullRowCount() {
		if (null == data) {
			return 0;
		}

		return data.length;
	}

	/**
	 * Returns the available (currently displayed) indexes.
	 */
	public int[] getIndexes() {
		if (null == fullData) {
			return null;
		}

		int back[] = new int[data.length];

		for (int i=0; i<data.length; ++i) {
			back[i] = Integer.parseInt( data[i][0].toString() );
		}

		return back;
	}

	/**
	 * Sets the filter on the table. Can be the special character "*", and it means that no filtering is necessary.
	 *
	 * @param type the string that will be compared to the descriptor of the constantpool entries.
	 */
    public void filterData(String type) {
		if (null == fullData) {
			return;
		}

		if ( "*" == type ) {
			data = fullData;
			return;
		}

		int size = 0;
		for (int i=0; i<fullData.length; ++i) {
			if ( type.equals( fullData[i][1] ) ) {
				size++;
			}
		}

		data = new Object[size][3];
		int act = 0;
		for (int i=0; i<fullData.length; ++i) {
			if ( type.equals( fullData[i][1] ) ) {
				data[act++] = fullData[i];
			}
		}

	}// filterData

}// ConstantPoolTableModel
