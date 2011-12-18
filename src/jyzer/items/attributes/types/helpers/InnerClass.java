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
package jyzer.items.attributes.types.helpers;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.ConstantPool;
import jyzer.items.accessflags.AccessFlags;

/**
 * <p> The class representing an inner-class. </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class InnerClass {

	//u2
	private int innerClassInfoIndex, outerClassInfoIndex, innerNameIndex;
	private AccessFlags accessFlags;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 * Needs severe testing ;-)
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public InnerClass(DataInput di) throws IOException {
		innerClassInfoIndex = di.readUnsignedShort();
		outerClassInfoIndex = di.readUnsignedShort();
		innerNameIndex = di.readUnsignedShort();
		accessFlags = new AccessFlags(di, AccessFlags.Type.NESTED_CLASS);
	}

	/** Tests if the inner class is an anonym class. */
	public boolean isAnonymus() {
		return ( 0 == innerNameIndex );
	}

	/** Returns the name of the inner class. */
	private String getInnerClassName() {
		String innerClassName = "";

		if ( 0 == innerNameIndex ) {
			innerClassName = "Anonymus";
		} else {
			innerClassName = ConstantPool.getUtf8String(innerNameIndex);
		}

		return innerClassName;
	}

	/** Returns the name of the outer class. */
	private String getOuterClassName() {
		String outerClassName = "";

		if ( 0 == outerClassInfoIndex ) {
			outerClassName = "Not a member";
		} else {
			outerClassName = ConstantPool.getClassName(outerClassInfoIndex);
		}

		return outerClassName;
	}

	/** Returns the accessflags of this innerclass. */
	public String getAccessString() {
		return accessFlags.getAccessString();
	}

	/** Returns the <code>innerClassInfoIndex</code> of this innerclass. */
	public String getInnerClassInfo() {
		return ConstantPool.getClassName(innerClassInfoIndex);
	}

	/** Returns the name of the class. */
	public String getClassName() {
		return getInnerClassName();
	}

	/** Returns the <i>real</i> modifiers of the class. */
	public String getRealModifierString() {
		return accessFlags.getRealModifierString();
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		String outerClassName = getOuterClassName();
		String innerClassName = getInnerClassName();

		sb.append("Inner Class Info Index: ").append(innerClassInfoIndex);
		sb.append(" <FONT color=\"blue\">// ").append( ConstantPool.getClassName(innerClassInfoIndex) ).append("</FONT><BR>");

		sb.append("Outer Class Info Index: ").append(outerClassInfoIndex);
		sb.append(" <FONT color=\"blue\">// ").append(outerClassName).append("</FONT><BR>");

		sb.append("Inner Name Index: ").append(innerNameIndex);
		sb.append(" <FONT color=\"blue\">// ").append(innerClassName).append("</FONT><BR>");

		sb.append("Inner Class Access Flags: ");
		accessFlags.getHTMLDescription(sb);
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String outerClassName = getOuterClassName();
		String innerClassName = getInnerClassName();

		sb.append("InnerClass:<BR>\n");

		sb.append("Inner Class Info Index: " + innerClassInfoIndex + '\n');
		sb.append("Outer Class Info Index: " + outerClassInfoIndex + '\n');
		sb.append("Inner Name Index: " + innerNameIndex + '\n');
		sb.append("Inner Class Access Flags: " + accessFlags + '\n');

		return sb.toString();
	}

}// class.InnerClass
