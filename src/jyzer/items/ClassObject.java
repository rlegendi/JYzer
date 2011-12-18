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

/* MODLOG:
 * -------------------------------------------------------------------------
 * 1.00 Initial release.
 *
 * 1.01 getClassName	2005 sept 27.
 *			now it natively returns "Object" if the index is zero (this
 * 			can happens only if it is a superclassname, because the 0th
 *			entry of the constantpool is unused by the specification).
 */

package jyzer.items;

import java.io.DataInput;
import java.io.IOException;

// my own lib :-)
import edu.lro.web.HTMLFilter;

/**
 * <p>This class represents a simple class entry of the constant pool, it was just created to
 * handle it easily. I could have simply ask the classname from the constantpool, but I thought
 * this way is a little cleaner.</p>
 *
 * <p>Cares for <i>this_class</i> & <i>super_class</i> entries of the classfile.</p>
 *
 * <p>Contains the following information:
 * <ul>
 * <li> u2 index<br>
 * 		Must be a valid index into the <code>constant_pool</code> table. The entry at that index
 * 		must be a <code>CONSTANT_Class_info</code> structure.<br>
 *		If the index is zero then this class must represent the class <code>java.lang.Object</code>
 *		in case this is the superclass.<br>
 *		For an interface, the <code>CONSTANT_Class_info</code> represents the class <code>java.lang.Object</code>
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.01, 2005 sept 27.
 */
public final class ClassObject {

	// --- Structure -----------------------------------------------------------------------------

	/** u2 */
	private int index;

	// --- Constructors --------------------------------------------------------------------------

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ClassObject(DataInput di) throws IOException {
		index = di.readUnsignedShort();
	}

	// --- Getter methods -------------------------------------------------------------------------

	/**
	 * Returns the index in the <code>constant_pool</code>.
	 *
	 * @return u2 index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the name of the class. Simply asks it from the constant_pool, because this information is
	 * needed <i>mostly once</i> with each class, and it wasn't necessary to store this info as a member.
	 *
	 * @return the name of the class.
	 */
	public String getClassName() {
		return ( (0 != index) ? ConstantPool.getClassName(index) : "Object" );
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
		sb.append("Name index: ").append(index).append(" <FONT color=\"blue\">// ");
		sb.append( HTMLFilter.filter( ConstantPool.getClassName(index) ) ).append("</FONT>");
	}

	// --- Super methods ----------------------------------------------------------------------------

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ( "Name index: " + index + ConstantPool.getClassName(index) );
	}

}// class.ClassObject
