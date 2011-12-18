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
 * The Signature attribute is an optional fixed-length attribute in the attributes
 * table of the ClassFile, field_info and method_info structures.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class SignatureAttribute extends AttributeInfo {

	// u2
	private int signatureIndex;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	public SignatureAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		super(attributeNameIndex, attributeLength);

		signatureIndex = di.readUnsignedShort();
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>Signature Attribute:</B><BR>");
		sb.append("Signature Index: ").append(signatureIndex).append(" <FONT color=\"blue\">// " +
					HTMLFilter.filter( ConstantPool.getUtf8String(signatureIndex) ) + "</FONT>" );
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Signature Attribute:\n");
		sb.append("Signature Index: ").append(signatureIndex).append('\n');

		return sb.toString();
	}

}// class.SignatureAttribute
