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

import java.io.DataInputStream;
import java.io.IOException;

/**
 * <p> The superclass of the attributes. All attributes have the following general format:</p>
 *
 * <p>
 *   attribute_info {<br>
 *   	u2 attribute_name_index;<br>
 *   	u4 attribute_length;<br>
 *   	u1 info[attribute_length];<br>
 *   }
 * </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 12.
 */
public abstract class AttributeInfo {

	// u2
	protected int attributeNameIndex;
	// u4
	protected int attributeLength;
	private   int info[]; // needed only here, the others care for their own specific data

	/** Default constructor. */
	protected AttributeInfo() {
		attributeLength = 0;
		attributeNameIndex = 0;
	}

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	protected AttributeInfo(int attributeNameIndex, int attributeLength) throws IOException {
		this.attributeNameIndex = attributeNameIndex;
		this.attributeLength    = attributeLength;
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public abstract void getHTMLDescription(StringBuilder sb);

}// class.AttributeInfo
