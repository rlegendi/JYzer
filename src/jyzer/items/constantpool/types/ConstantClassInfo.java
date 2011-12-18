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
package jyzer.items.constantpool.types;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.ConstantPool;
import jyzer.items.constantpool.ConstantPoolInfo;

/**
 * <p> Contains the following information:
 * <ul>
 *
 * <li> u2 name_index <br>
 *		Must be a valid index into the <code>constant_pool</code> table. The entry at that index
 *		must be a <code>CONSTANT_Utf8_info</code> structure representing a valid fully qualified
 *		class or interface name encoded in internal form.
 *
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 okt 12.
 */
public final class ConstantClassInfo extends ConstantPoolInfo {

	// u2
	private int nameIndex;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantClassInfo(DataInput di) throws IOException {
		tag       = CONSTANT_Class;
		nameIndex = di.readUnsignedShort();
	}

	/**
	 * Returns the <code>name_index</code> of this entry.
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * Returns the name of this class.
	 */
	public String getNameString() {
		return ConstantPool.getUtf8String(nameIndex).replace('/', '.');
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>").append(getTagDescriptor(tag)).append("</B><BR>");
		sb.append("Name Index: ").append(nameIndex).append(" <FONT color=\"blue\">// ").append( getNameString() ).append("</FONT>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ("Name Index: " + nameIndex + " [" + getNameString() + "]");

	}

}// class.ConstantClassInfo
