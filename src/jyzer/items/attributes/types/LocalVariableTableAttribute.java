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

import jyzer.items.attributes.types.helpers.LocalVariableTableEntry;

/**
 * The LocalVariableTable attribute is an optional variable-length attribute of a Code attribute. It
 * may be used by debuggers to determine the value of a given local variable during the execution of
 * a method. If LocalVariableTable attributes are present in the attributes table of a given Code
 * attribute, then they may appear in any order. There may be no more than one LocalVariableTable
 * attribute per local variable in the Code attribute.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class LocalVariableTableAttribute extends AttributeInfo {

	// u2
	private int localVariableTableLength;
	private LocalVariableTableEntry localVariableTable[];

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	public LocalVariableTableAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		super(attributeNameIndex, attributeLength);

		localVariableTableLength= di.readUnsignedShort();
		localVariableTable = new LocalVariableTableEntry[localVariableTableLength];

		for (int i=0; i<localVariableTableLength; ++i) {
			localVariableTable[i] = new LocalVariableTableEntry(di);
		}
	}

	/**
	 * Returns the name of the local variables of the method in an array of strings.
	 */
	public String[] getLocalVariablesString() {
		String[] back = new String[localVariableTable.length];

		int i=0;
		for (LocalVariableTableEntry entry : localVariableTable) {
			String type = entry.getDescriptorString();
			String name = entry.getNameString();

			if ( type.contains(".") ) {
				type = type.substring( type.lastIndexOf('.') + 1 );
			}

			if ( name.equals("this") ) {
				back[i++] = type + " " + type.toLowerCase() + " = " + name + ";";
			} else {
				back[i++] = type + " " + name + ";";
			}

		}

		return back;
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>Local Variable Table Attribute:</B><BR>");

		if ( 0 == localVariableTableLength ) {
			sb.append("NONE");
			return;
		}

		sb.append("Local Variable Table Length: ").append(localVariableTableLength).append("<BR>");
		sb.append("Local Variable Table:");
		sb.append("<OL>");
		for (LocalVariableTableEntry entry : localVariableTable) {
			sb.append("<LI>");
			entry.getHTMLDescription(sb);
		}
		sb.append("</OL>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Local Variable Table Attribute:\n");

		sb.append("Local Variable Table Length: ").append(localVariableTableLength).append('\n');
		sb.append("Local Variable Table:\n");

		int i=0;
		for (LocalVariableTableEntry entry : localVariableTable) {
			sb.append("[").append(i).append("] : ").append(entry).append('\n');
		}

		return sb.toString();
	}

}// class.LocalVariableTableAttribute
