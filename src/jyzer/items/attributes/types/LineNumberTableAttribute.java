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
package jyzer.items.attributes.types;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.attributes.types.helpers.LineNumberTableEntry;

/**
 * The LineNumberTable attribute is an optional variable-length attribute in the attributes table of a Code attribute. It may be
 * used by debuggers to determine which part of the Java virtual machine code array corresponds to a given line number in the original
 * source file. If LineNumberTable attributes are present in the attributes table of a given Code attribute, then they may appear in any
 * order. Furthermore, multiple LineNumberTable attributes may together represent a given line of a source file; that is, LineNumberTable
 * attributes need not be one-to-one with source lines.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class LineNumberTableAttribute extends AttributeInfo {

	// u2
	private int lineNumberTableLength;
	private LineNumberTableEntry lineNumberTable[];

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	public LineNumberTableAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		super(attributeNameIndex, attributeLength);

		lineNumberTableLength = di.readUnsignedShort();
		lineNumberTable = new LineNumberTableEntry[lineNumberTableLength];
		for (int i=0; i<lineNumberTableLength; ++i) {
			lineNumberTable[i] = new LineNumberTableEntry(di);
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
		sb.append("<B>Line Number Table Attribute:</B><BR>");

		if ( 0 == lineNumberTableLength ) {
			sb.append("NONE");
			return;
		}

		sb.append("Line Number Table Length: ").append(lineNumberTableLength).append("<BR>");
		sb.append("Line Number Table:<BR>");

		sb.append("<TABLE border=\"0\">");
		for (LineNumberTableEntry entry : lineNumberTable) {
			entry.getHTMLDescription(sb);
		}
		sb.append("</TABLE>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Line Number Table Attribute:\n");

		sb.append("Line Number Table Length: ").append(lineNumberTableLength).append('\n');
		sb.append("Line Number Table:\n");

		int i=0;
		for (LineNumberTableEntry entry : lineNumberTable) {
			sb.append("[").append(i).append("] : ").append(entry).append('\n');
		}

		return sb.toString();
	}

}// class.LineNumberTableAttribute
