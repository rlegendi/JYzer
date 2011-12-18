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

/**
 * <p> Cares for the <i>interfaces</i> entries of the classfile. </p>
 *
 * <p> Contains the following information:
 * <ul>
 *
 * <li> u2 interfaces_count <br>
 *		Gives the number of direct superinterfaces of this class or interface type.
 *
 * <li> u2 interfaces[] <br>
 *		Each value in the table must be a valid index into the <code>constant_pool</code>
 *		table. Each entry must be a <code>CONSTANT_Class_info</code> structure representing
 *		an interface.
 *
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 okt 12.
 */
public class Interfaces {

	// --- Structure -----------------------------------------------------------------------------

	// u2
	private int interfacesCount;

	// u2
	private int interfaces[];

	// --- Constructors --------------------------------------------------------------------------

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public Interfaces(DataInput di) throws IOException {
		interfacesCount = di.readUnsignedShort();
		interfaces      = new int[interfacesCount];

		for (int i=0; i<interfacesCount; ++i) {
			interfaces[i] = di.readUnsignedShort();
		}
	}

	// --- Getter methods -------------------------------------------------------------------------

	/**
	 * Tests if the classfile has no direct superinterfaces.
	 *
	 * @return true if and only if this classfile has no components, that is, its size is zero; false otherwise.
	 */
	public boolean isEmpty() {
		return ( 0 == interfacesCount );
	}

	/**
	 * Returns the number of interfaces.
	 *
	 * @return the <code>interfaces_count</code> value of the classfile.
	 */
	public int getInterfacesCount() {
		return interfacesCount;
	}

	/**
	 * Returns the name of the interfaces in an array of Strings, in left-to-right order as they were written
	 * in the original sourcefile.
	 */
	public String[] getInterfaceNames() {
		String names[]  = new String[interfacesCount];

		for (int i=0; i<interfacesCount; ++i) {
			names[i] = ConstantPool.getClassName(interfaces[i]);
		}

		return names;
	}

	// --- Own methods -----------------------------------------------------------------------------

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		if (0 == interfacesCount) {
			sb.append("<UL><LI>Interfaces: NONE</UL>");
			return;
		}

		sb.append("<UL>");
		sb.append("<LI>Interfaces count: ").append(interfacesCount);
		sb.append("<LI>Interfaces:");

		sb.append("<OL>");
		for (int i : interfaces) {
			sb.append("<LI>Interface index: ").append(" <FONT color=\"blue\">// ").append( ConstantPool.getClassName(i) ).append("</FONT>");
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
		if (0 == interfacesCount) {
			return "Interfaces: NONE";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("Interfaces count: ").append(interfacesCount).append('\n');
		for (int i : interfaces) {
			sb.append("Interface index: ").append(i).append(" // ").append( ConstantPool.getClassName(i) ).append('\n');
		}

		return sb.toString();
	}

}// class.Interfaces
