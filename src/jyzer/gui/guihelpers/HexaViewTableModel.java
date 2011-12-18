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

import java.io.*;
import javax.swing.table.*;

/**
 * The table model that is used by the hexaview pane.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class HexaViewTableModel extends AbstractTableModel {

	// --- Variables -----------------------------

	private Object data[][];

	// --- Constructors --------------------------

	/**
	 * Creates a new (empty) model.
	 */
	public HexaViewTableModel() {
	}

	/**
	 * Creates a new model with the given data in it. The <code>File</code>
	 * is needed because this method re-opens the file, and reads its contents
	 * with a <code>DataInputStream</code>.
	 *
	 * @param file the file that has to be processed.
	 * @throws IOException if an I/O error occurs.
	 */
	public HexaViewTableModel( File file ) throws IOException {
		DataInputStream dis = new DataInputStream( new FileInputStream(file) );

		final int size = ( (int) (file.length() / 16) ) + 1;
		data = new Object[size][1 + 16 + 16];

		int lineNum = 0;
		int neededZeros = Long.toHexString( file.length() ).length();

		// Printing line numbers nicely
		for (int i=0; i<size; ++i) {
			StringBuilder sb = new StringBuilder();
			sb.append("0x");
			String line = Integer.toHexString(lineNum).toUpperCase();
			for (int j=0; j<neededZeros-line.length(); ++j) {
				sb.append("0");
			}
			sb.append(line).append(':');
			data[i][0] = sb.toString();
			lineNum += 16;
		}

		// Filling in the dump
		outer : for (int i=0; i<size; ++i) {
			for (int j=1; j<17; ++j) {
				int actVal = dis.readUnsignedByte();
				String act = Integer.toHexString( actVal ).toUpperCase();

				data[i][j] = ( act.length() < 2 ) ? "0" + act : act;

				// if it is a whitespace char, should display a rectangle, like '(char)' 0 has ...
				char actChar = (char) actVal;
				if ( Character.isWhitespace( actChar ) ) {
					data[i][j+16] = (char) 0;
				} else {
					data[i][j+16] = actChar;
				}

				if ( file.length() == ( (long) i ) * 16 + j ) {
					break outer;
				}

			}
		}

		dis.close();
	}// constr.HexaViewTableModel

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
		return null;
    }

	/**
	 * Returns the number of rows in the table.
	 */
    public int getRowCount() {
		if ( isEmpty() ) {
			return 0;
		}

		return data.length;
	}

	/**
	 * Returns the number of columns in the table.
	 */
	public int getColumnCount() {
		if ( isEmpty() ) {
			return 0;
		}

		return data[0].length;
	}

	/**
	 * Returns the string from the table at the given position.
	 *
	 * @param row the number of the row.
	 * @param col the number of the col.
	 * @return the string at the specified position.
	 */
	public Object getValueAt(int row, int col) {
		if ( isEmpty() ) {
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

}// class.HexaViewTableModel
