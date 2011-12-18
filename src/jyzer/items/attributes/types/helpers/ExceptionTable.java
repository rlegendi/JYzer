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
package jyzer.items.attributes.types.helpers;

import java.io.DataInput;
import java.io.IOException;

/**
 * <p> A table of exceptions that can be thrown by methods. </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class ExceptionTable {

	// u2
	private int exceptionTableLength;
	private ExceptionTableEntry exceptionTable[];

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 * Needs severe testing ;-)
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ExceptionTable(DataInput di) throws IOException {
		exceptionTableLength = di.readUnsignedShort();
		exceptionTable       = new ExceptionTableEntry[exceptionTableLength];

		for (int i=0; i<exceptionTableLength; ++i) {
			exceptionTable[i] = new ExceptionTableEntry(di);
		}
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		if (0 == exceptionTableLength) {
			sb.append("Exception Table: NONE");
			return;
		}

		sb.append("<UL>");
		sb.append("<LI> Exception Table Length: ").append(exceptionTableLength);
		sb.append("<LI> Exception Table: ");
		sb.append("<OL>");

		for (ExceptionTableEntry exceptionTableEntry : exceptionTable) {
			sb.append("<LI>");
			exceptionTableEntry.getHTMLDescription(sb);
		}

		sb.append("</OL>");
		sb.append("</UL>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		if (0 == exceptionTableLength) {
			return "Exception Table: NONE\n";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("Exception Table Length: " + exceptionTableLength + '\n');
		sb.append("Exception Table: ");

		for (ExceptionTableEntry exceptionTableEntry : exceptionTable) {
			sb.append(exceptionTableEntry).append('\n');
		}

		sb.append('\n');
		return sb.toString();
	}

}// class.ExceptionTable
