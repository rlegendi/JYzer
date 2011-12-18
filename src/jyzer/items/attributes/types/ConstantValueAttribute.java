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

import jyzer.items.ConstantPool;
import edu.lro.web.HTMLFilter;

/**
 * A class representing the constant value attribute. The ConstantValue attribute is a fixed-length attribute used
 * in the attributes table of the field_info structures. A ConstantValue attribute represents the value of a
 * constant field that must be (explicitly or implicitly) static; that is, the ACC_STATIC bit in the flags item of
 * the field_info structure must be set. There can be no more than one ConstantValue attribute in the attributes table
 * of a given field_info structure. The constant field represented by the field_info structure is assigned the value
 * referenced by its ConstantValue attribute as part of the initialization of the class or interface declaring the constant
 * field. This occurs immediately prior to the invocation of the class or interface initialization method of that class or interface.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 12.
 */
public final class ConstantValueAttribute extends AttributeInfo {

	// u2
	private int constantValueIndex;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantValueAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		super(attributeNameIndex, attributeLength);

		constantValueIndex = di.readUnsignedShort();
	}

	/** Returns the constant value as a string. */
	public String getConstantValueString() {
		return ConstantPool.getValueString(constantValueIndex);
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>Constant Value Attribute:</B><BR>");
		sb.append("Constant Value Index: " + constantValueIndex + " <FONT color=\"blue\">// " +
					HTMLFilter.filter( ConstantPool.getValueString(constantValueIndex) ) + "</FONT>" );
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Constant Value Attribute:\n");
		sb.append("Constant Value Index: ").append(constantValueIndex);

		return sb.toString();
	}

}// class.ConstantValueAttribute
