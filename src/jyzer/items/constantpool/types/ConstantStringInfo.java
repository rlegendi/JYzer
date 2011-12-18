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

import edu.lro.web.HTMLFilter;

/**
 * <p> The CONSTANT_String_info structure is used to represent constant objects of the type String. </p>
 *
 * <p> Contains the following information:
 * <ul>
 *
 * <li> u2 string_index <br>
 *		The value of the <code>string_index</code> item must be a valid index into the <code>constant_pool</code> table.
 *		The <code>constant_pool</code> entry at that index must be a <code>CONSTANT_Utf8_info</code> structure representing
 *		the sequence of characters to which the String object is to be initialized.
 *
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantStringInfo extends ConstantPoolInfo {

	// u2
	private int stringIndex;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantStringInfo(DataInput di) throws IOException {
		tag         = CONSTANT_String;
		stringIndex = di.readUnsignedShort();
	}

	/**
	 * Returns the string stored in this entry.
	 */
	public String getValue() {
		return ConstantPool.getUtf8String(stringIndex);
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>").append( getTagDescriptor(tag) ).append("</B><BR>");

		sb.append("String Index: ").append(stringIndex);
		sb.append(" <FONT color=\"blue\">// ").append( HTMLFilter.filter( ConstantPool.getUtf8String(stringIndex) ) ).append("</FONT>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ("String Index: " + stringIndex);
	}

}// class.ConstantStringInfo
