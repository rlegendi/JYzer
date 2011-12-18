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
package jyzer.items;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.exceptions.CorruptedClassfileException;
import jyzer.items.methods.MethodInfo;

/**
 * <p> Cares for the <i>method</i> entries of the classfile. </p>
 *
 * <p> Contains the following information:
 * <ul>
 *
 * <li> u2 methods_count <br>
 *		Gives the number of <code>method_info</code> structures in the methods table.
 *
 * <li> method_info mehtods[] <br>
 *		Each value in the table must be a <code>method_info</code> structure, giving a
 *		complete description of a method in this class or interface. If the method is not
 *		<b>native</b> or <b>abstract</b>, the JVM instructions implementing the method are
 *		also supplied.
 *
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 okt 12.
 */
public final class Methods {

	// --- Structure -----------------------------------------------------------------------------

	// u2
	private int methodsCount;
	private MethodInfo methods[];

	// --- Constructors --------------------------------------------------------------------------

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 * @throws CorruptedClassfileException if the classfile seems to be corrupted.
	 */
	public Methods(DataInput di) throws CorruptedClassfileException, IOException {
		methodsCount = di.readUnsignedShort();
		methods = new MethodInfo[methodsCount];

		for (int i=0; i<methodsCount; ++i) {
			methods[i] = new MethodInfo(di);
		}

		for (int i=0; i<methodsCount; ++i) {
			if ( methods[i].isClassInitializer() ) {
				sort(i);
				break;
			}
		}
	}

	// --- Getter methods -------------------------------------------------------------------------

	/**
	 * Returns the <code>methods[]</code> structure.
	 *
	 * @return methods[] as a MethodInfo array.
	 */
	public MethodInfo[] getMethods() {
		return methods;
	}

	/**
	 * Returns a 2 dimension representation of the methods (to put it into a table).
	 *
	 * @see jyzer.items.accessflags.AccessFlags
	 * @param showOnlyRealModifiers if should care only with real modifiers.
	 * @return the method data in a two dimensin array.
	 */
	public Object[][] getMethodData(boolean showOnlyRealModifiers) {
		Object back[][] = new Object[methodsCount][5];

		for (int i=0; i<back.length; ++i) {

				if (showOnlyRealModifiers) {
					back[i][0] = methods[i].getFullRealAccessString();
				} else {
					back[i][0] = methods[i].getFullAccessString();
				}

				back[i][1] = methods[i].getFullNameString();
				back[i][2] = methods[i].getFullDescriptorString();
		}

		return back;
	}

	// --- Own methods -----------------------------------------------------------------------------

	/**
	 * <p> Used for beautification: the class initializer block should be the first displayed method
	 * (this is just one of my habbit). </p>
	 * <p> Puts the given element to the first place in the <code>methods</code> array.
	 *
	 * @param index the index of the element that has to be put to the first place in the array.
	 */
	private void sort(int index) {
		MethodInfo tmp = methods[index];
		for(int i=index; i>0; --i) { // shifting
			methods[i]=methods[i-1];
		}
		methods[0] = tmp;
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {

		if ( 0 == methodsCount ) {
			sb.append("<UL><LI>Methods: NONE</UL>");
			return;
		}

		sb.append("<UL>");
		sb.append("<LI>Method Count: ").append(methodsCount);
		sb.append("<LI>Methods:");
		sb.append("<OL>");

		for (MethodInfo minfo: methods) {
			sb.append("<LI>");
			minfo.getHTMLDescription(sb);
		}

		sb.append("</OL>");
		sb.append("</UL>");
	}

	// --- Super methods ----------------------------------------------------------------------------

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Method Count: ").append(methodsCount).append('\n');

		if (methodsCount != 0) {
			for (MethodInfo minfo: methods) {
				sb.append(minfo).append("\n");
			}
		}

		return sb.toString();
	}

}// class.Methods
