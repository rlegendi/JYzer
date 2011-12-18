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

/**
 * A class representing the deprecated attribute. The Deprecated attribute is an optional fixed-length attribute in the
 * attributes table of ClassFile, field_info, and method_info structures. A class, interface, method, or field may be
 * marked using a Deprecated attribute to indicate that the class, interface, method, or field has been superseded. A
 * runtime interpreter or tool that reads the class file format, such as a compiler, can use this marking to advise the
 * user that a superseded class, interface, method, or field is being referred to. The presence of a Deprecated attribute
 * does not alter the semantics of a class or interface.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 12.
 */
public final class DeprecatedAttribute extends AttributeInfo {

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	public DeprecatedAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		// DI NEM KELL !!!
		super(attributeNameIndex, attributeLength);
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>Deprecated Attribute</B>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Deprecated Attribute");
		return sb.toString();
	}

}// class.DeprecatedAttribute
