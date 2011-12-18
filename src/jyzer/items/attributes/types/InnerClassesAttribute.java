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

import jyzer.items.attributes.types.helpers.InnerClass;

/**
 * The InnerClasses attribute5 is a variable-length attribute in the attributes table of the ClassFile
 * structure. If the constant pool of a class or interface refers to any class or interface that is not
 * a member of a package, its ClassFile structure must have exactly one InnerClasses attribute in its attributes table.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class InnerClassesAttribute extends AttributeInfo {

	// u2
	private int numberOfClasses;
	private InnerClass classes[];

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public InnerClassesAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws IOException {
		super(attributeNameIndex, attributeLength);

		numberOfClasses = di.readUnsignedShort();

		classes = new InnerClass[numberOfClasses];
		for (int i=0; i<numberOfClasses; ++i) {
			classes[i] = new InnerClass(di);
		}

	}

	/**
	 * Returns all of the inner classes names.
	 */
	public InnerClass[] getInnerClasses() {
		return classes;
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>InnerClassesAttribute:</B><BR>");
		sb.append("Number Of Inner Classes: ").append(numberOfClasses);

		sb.append("<OL>");

		for (InnerClass innerClass : classes) {
			sb.append("<LI> : ");
			innerClass.getHTMLDescription(sb);
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

		sb.append("<B>InnerClassesAttribute:</B><BR>\n");
		sb.append("Number Of Inner Classes: ").append(numberOfClasses).append('\n');

		int i=0;
		for (InnerClass innerClass : classes) {
			sb.append("[").append(i++).append("] : ").append(innerClass);
		}

		return sb.toString();
	}

}// class.InnerClassesAttribute
