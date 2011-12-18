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

import jyzer.items.ConstantPool;

/**
 * <p> An entry in the <code>LocalVariableTable</code>. </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class LocalVariableTableEntry {

	// u2
	private int startPc, length, nameIndex, descriptorIndex, index;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 * Needs severe testing ;-)
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public LocalVariableTableEntry(DataInput di) throws IOException {
		startPc   = di.readUnsignedShort();
		length    = di.readUnsignedShort();
		nameIndex = di.readUnsignedShort();
		descriptorIndex = di.readUnsignedShort();
		index     = di.readUnsignedShort();
	}

	/** Returns the descriptor of the entry. */
	public String getDescriptorString() {
		return ConstantPool.getDescriptorString(descriptorIndex);
	}

	/** Returns the name of the entry. */
	public String getNameString() {
		return ConstantPool.getUtf8String(nameIndex);
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("Start Pc: ").append(startPc).append("<BR>");
		sb.append("Length: ").append(length).append("<BR>");

		sb.append("Name Index: ").append(nameIndex);
		sb.append(" <FONT color=\"blue\">// ").append( ConstantPool.getUtf8String(nameIndex) ).append("<BR>").append("</FONT>");

		sb.append("Descriptor Index: ").append(descriptorIndex);
		sb.append(" <FONT color=\"blue\">// ").append( ConstantPool.getDescriptorString(descriptorIndex) ).append("<BR>").append("</FONT>");

		sb.append("Index: ").append(index);
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[Local Variable Table Entry: ");
		sb.append("Start Pc: " + startPc + '\n');
		sb.append("Length: " + length + '\n');
		sb.append("Name Index: " + nameIndex + '\n');
		sb.append("Descriptor Index: " + descriptorIndex + '\n');
		sb.append("Index: " + index + "]\n");

		return sb.toString();
	}

}// class.LocalVariableTableEntry
