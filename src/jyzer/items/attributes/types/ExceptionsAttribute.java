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

/**
 * The Exceptions attribute is a variable-length attribute used in the attributes table of a method_info
 * structure. The Exceptions attribute indicates which checked exceptions a method may throw. There may
 * be at most one Exceptions attribute in each method_info structure.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class ExceptionsAttribute extends AttributeInfo {

	// u2
	private int numberOfExceptions;
	private int exceptionIndexTable[];

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 */
	public ExceptionsAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		super(attributeNameIndex, attributeLength);

		numberOfExceptions = di.readUnsignedShort();

		exceptionIndexTable = new int[numberOfExceptions];

		for (int i=0; i<numberOfExceptions; ++i) {
			exceptionIndexTable[i] = di.readUnsignedShort();
		}

	}

	/**
	 * Returns an Exception object that is the indexth in the constantpool.
	 */
	public String getExceptionName(int index) {
		return ConstantPool.getClassName(index);
	}

	/**
	 * Returns the pure name of the exception.
	 *
	 * @param enclosingClassName the name of the enclosing class.
	 * @param index the given index.
	 */
	public String getPureExceptionName(String enclosingClassName, int index) {
		String name = ConstantPool.getClassName(index);

		if (name.contains("$") ) {
			String prefix = name.substring( 0, name.indexOf('$') );

			if ( prefix.equals(enclosingClassName) ) {
				// making it self-sensitive (classA { ... classB... } should be simple classB instead of classA.classB)
				name = name.substring( name.indexOf('$') + 1).replaceAll("$", ".");
			} else {
				name = name.replace('$', '.');
			}
		} else if ( name.contains(".") ) {
			// don't have to care anything : everything is visible due the imports ...
			name = name.substring( name.lastIndexOf('.') + 1);
		}

		return name;
	}

	/**
	 * Returns all of the exception names.
	 *
	 * @param enclosingClassName the name of the enclosing class.
	 */
	public String[] getPureExceptionNames(String enclosingClassName) {
		String[] back = new String[numberOfExceptions];

		for (int i=0; i<numberOfExceptions; ++i) {
			back[i] = getPureExceptionName( enclosingClassName, exceptionIndexTable[i] );
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
		sb.append("<B>Exceptions Attribute:</B><BR>");
		sb.append("Number Of Exceptions: ").append(numberOfExceptions).append("<BR>");

		sb.append("<OL>");
		for (int index : exceptionIndexTable) {
			sb.append("Element: ").append(index).append(" <FONT color=\"blue\">// ").append( ConstantPool.getClassName(index) );
		}
		sb.append("</OL>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Exceptions Attribute:\n");
		sb.append("Number Of Exceptions: ").append(numberOfExceptions).append('\n');

		for (int index : exceptionIndexTable) {
			sb.append("Element: ").append(index);
		}

		return sb.toString();
	}

}// class.ExceptionsAttribute
