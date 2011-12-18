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
package jyzer.items.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.Vector;

import jyzer.items.attributes.types.*;
import jyzer.items.attributes.types.helpers.*;
import jyzer.items.exceptions.CorruptedClassfileException;
import jyzer.items.factories.AttributeFactory;

/**
 * A class representing a set of atrributes. Used in several ways, like classes, methods, ... can
 * have attributes.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 12.
 */
public class Attributes {

	private int attributesCount;
	private AttributeInfo attributes[];


	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 * @throws CorruptedClassfileException if the classfile seems to be corrupted.
	 */
	public Attributes(DataInput di) throws IOException, CorruptedClassfileException {
		attributesCount = di.readUnsignedShort();

		attributes = new AttributeInfo[attributesCount];
		for (int i=0; i<attributesCount; ++i) {
			AttributeInfo newOne = AttributeFactory.create(di);
			attributes[i] = newOne;
		}
	}

	/**
	 * Returns true if the given class does not have any attributes (the set is empty).
	 */
	public boolean isEmpty() {
		return ( 0 == attributesCount );
	}

	/**
	 * Returns true if the attributes has the <code>CodeAttribute</code>.
	 */
	public boolean hasCode() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof CodeAttribute ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the attributes has the <code>ConstantValueAttribute</code>.
	 */
	public boolean hasConstantValue() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof ConstantValueAttribute ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the attributes has the <code>ExceptionsAttribute</code>.
	 */
	public boolean hasExceptions() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof ExceptionsAttribute ) return true;
		}

		return false;
	}

	/**
	 * Returns true if the attributes has the <code>InnerClassesAttribute</code>.
	 */
	public boolean hasInnerClasses() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof InnerClassesAttribute ) return true;
		}

		return false;
	}

	/**
	 * Returns true if the attributes has the <code>LocalVariableTableAttribute</code>.
	 */
	public boolean hasLocalVariableTable() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof LocalVariableTableAttribute ) return true;
		}

		return false;
	}

	/**
	 * Returns true if the code (if any) has the <code>LocalVariableTableAttribute</code>.
	 */
	public boolean hasTheCodeLocalVariableTable() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof CodeAttribute ) {
				return ( (CodeAttribute) attributes[i] ).hasLocalVariableTable();
			}
		}

		return false;
	}

	/**
	 * Returns true if the attributes has the <code>SyntheticAttribute</code>.
	 */
	public boolean hasSynthetic() {
		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof SyntheticAttribute ) return true;
		}

		return false;
	}

	/**
	 * Returns the number of attributes.
	 */
	public int getAttributesCount() {
		return attributesCount;
	}

	/**
	 * Returns the constant value as astring; returns null if there's no <code>ConstantValueAttribute</code>.
	 */
	public String getConstantValueString() {
		String back = null;

		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof ConstantValueAttribute ) {
				back = ( (ConstantValueAttribute ) attributes[i] ).getConstantValueString();
			}
		}

		return back;
	}

	/**
	 * Returns the disassembled code; returns null if there's no <code>CodeAttribute</code>.
	 */
	public Vector<CodeInstruction> getDisassembledCode() {
		Vector<CodeInstruction> back = null;

		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof CodeAttribute ) {
				back = ( (CodeAttribute) attributes[i] ).getDisassembledCode();
			}
		}

		return back;
	}

	/**
	 * Returns the name of the exceptions from the <code>ExceptionsAttribute</code> as an array of strings;
	 * returns null if there's no <code>ExceptionsAttribute</code>.
	 *
	 * @param enclosingClassName the name of the enclosing class.
	 */
	public String[] getPureExceptionNames(String enclosingClassName) {
		String back[] = null;

		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof ExceptionsAttribute ) {
				back = ( (ExceptionsAttribute) attributes[i] ).getPureExceptionNames(enclosingClassName);
			}
		}

		return back;
	}

	/**
	 * Returns an array of innerclasses; returns null if there's no <code>InnerClassesAttribute</code>.
	 */
	public InnerClass[] getInnerClasses() {
		InnerClass back[] = null;

		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof InnerClassesAttribute ) {
				back = ( (InnerClassesAttribute) attributes[i] ).getInnerClasses();
			}
		}

		return back;
	}

	/**
	 * Returns the name of the local variables <b>from the code</b>; returns null if there's no <code>CodeAttribute</code>.
	 */
	public String[] getTheCodeLocalVariablesString() {
		String back[] = null;

		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof CodeAttribute) {
				back = ( (CodeAttribute) attributes[i] ).getLocalVariablesString();
			}
		}

		return back;
	}

	/**
	 * Returns the name of the local variables; returns null if there's no <code>CodeAttribute</code>.
	 */
	public String[] getLocalVariablesString() {
		String back[] = null;

		for (int i=0; i<attributesCount; ++i) {
			if ( attributes[i] instanceof LocalVariableTableAttribute ) {
				back = ( (LocalVariableTableAttribute) attributes[i] ).getLocalVariablesString();
			}
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
		if ( 0 == attributesCount ) {
			sb.append("Attributes: NONE");
			return;
		}

		sb.append("<ul>");
		sb.append("<li> Attributes Count: ").append(attributesCount).append("</li>");
		sb.append("<li> Attributes: </li>");
		sb.append("<ol>");

		for (AttributeInfo attribInfo : attributes) {
			sb.append("<li>");
			attribInfo.getHTMLDescription(sb);
			sb.append("</li>");
		}

		sb.append("</ol>");
		sb.append("</ul>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		if ( 0 == attributesCount ) {
			return "Attributes: NONE\n";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("Attributes Count: ").append(attributesCount).append('\n');

		for (AttributeInfo attribInfo : attributes) {
			sb.append(attribInfo);
		}

		return sb.toString();
	}

}// class.Attributes
