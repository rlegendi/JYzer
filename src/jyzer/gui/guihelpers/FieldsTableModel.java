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

import jyzer.items.fields.FieldInfo;

/**
 * The table model that is used by the fields pane.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class FieldsTableModel extends AbstractTableModel {

	// --- Variables --------------------------

	private static final String[] columnNames = {
		"Access Privileges", "Name", "Descriptor"
	};

	private Object data[][];
	private FieldInfo fields[];

	// --- Constructors --------------------------

	/**
	 * Creates a new (empty) model.
	 */
	public FieldsTableModel() {
	}


	/**
	 * Creates a new model with the given data in it. The <code>FieldInfo</code>s
	 * are needed to determine if the field has any attributes.
	 *
	 * @param fields an array of the fieldinfos.
	 * @param data the objects that has to be displayed in the table.
	 */
	public FieldsTableModel( FieldInfo fields[], Object data[][] ) {
		this.data  = data;
		this.fields = fields;
	}

	// --- Super methods --------------------------

    /**
     *  Returns false. This is the default implementation for all cells.
     *
     *  @param row the row being queried.
     *  @param col the column being queried.
     *  @return false
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
	 * Returns the <code>FieldInfo</code> at the specified index.
	 */
	public FieldInfo getField(int index) {
		return fields[index];
	}

	/**
	 * Returns the 2nd and 3rd column as an array of strings. Used for searching.
	 */
    public String[][] getDescriptions() {
		if (null == data) {
			return null;
		}

		String back[][] = new String[ getRowCount() ][2];
		for (int i=0; i<data.length; ++i) {
			back[i][0] = data[i][1].toString();
			back[i][1] = data[i][2].toString();
		}

		return back;
	}

}// class.FieldsTableModel
