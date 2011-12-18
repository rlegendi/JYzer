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
package jyzer.items.constantpool.types;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.constantpool.ConstantPoolInfo;

import edu.lro.web.HTMLFilter;

/**
 * <p> The <code>CONSTANT_Utf8_info</code> structure is used to represent constant string values. </p>
 *
 * <p> Contains the following information:</p>
 * <ul>
 *
 * <li> length <br>
 * The value of the length item gives the number of bytes in the bytes array (not the length of the resulting string). The
 * strings in the CONSTANT_Utf8_info structure are not null-terminated.
 *
 * <li> bytes[] <br>
 * The bytes array contains the bytes of the string. No byte may have the value (byte)0 or lie in the range (byte)0xf0-(byte)0xff.
 *
 * </ul>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantUtf8Info extends ConstantPoolInfo {

	// u2
	private int length;
	private String value;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantUtf8Info(DataInput di) throws IOException {
		tag    = CONSTANT_Utf8;
		value  = di.readUTF();
		length = value.length();
	}

	/**
	 * Returns the return type of the method described by this string (a CONSTANT_NameAndType_info must point to this structure
	 * as a 'descriptor').
	 */
	public String getMethodReturnString() {
		String returnType   = value.substring( value.lastIndexOf(')') + 1 );
		StringBuilder array = new StringBuilder();
		int i = 0;

		while ( '[' == returnType.charAt(i) ) {
			array.append("[]");
			i++;
		}

		StringBuilder back = new StringBuilder();
		switch ( returnType.charAt(i) ) {
			case ('B') : back.append("byte"); break;
			case ('C') : back.append("char"); break;
			case ('D') : back.append("double"); break;
			case ('F') : back.append("float"); break;
			case ('I') : back.append("int"); break;
			case ('J') : back.append("long"); break;
			case ('S') : back.append("short"); break;
			case ('Z') : back.append("boolean"); break;
			case ('V') : back.append("void"); break;

			case ('L') : back.append( returnType.substring( returnType.lastIndexOf('L') + 1, returnType.lastIndexOf(';') ).replace('/', '.') );
					break;
		}

		back.append(array);

		return back.toString();
	}

	/**
	 * Used by getMethodParamsString() to determine <i>one</i> parameter.
	 *
	 * @param descriptor the actual descriptor.
	 * @param actIndex the actual position where we are.
	 */
	private Object[] getType(String descriptor, int actIndex) {
		StringBuilder array = new StringBuilder();
		int i = actIndex;

		while ( '[' == descriptor.charAt(i) ) {
			array.append("[]");
			i++;
		}

		StringBuilder back = new StringBuilder();
		switch ( descriptor.charAt(i) ) {
			case ('B') : back.append("byte b"); break;
			case ('C') : back.append("char c"); break;
			case ('D') : back.append("double d"); break;
			case ('F') : back.append("float f"); break;
			case ('I') : back.append("int i"); break;
			case ('J') : back.append("long l"); break;
			case ('S') : back.append("short s"); break;
			case ('Z') : back.append("boolean b"); break;

			case ('L') : {
					String className = descriptor.substring( descriptor.indexOf('L', i) + 1, descriptor.indexOf(';', i) ).replace('/', '.');
					i += className.length() + 1; // dependig
					// getting rid of the prefix
					className = className.substring( className.lastIndexOf('.') + 1); // from this !!!
					String variable = className.toLowerCase();
					back.append( className + " " + variable);
					break;
				}
		}

		back.append(array);

		Object objs[] = new Object[2];
		objs[0] = back.toString();
		objs[1] = i;
		return objs;
	}

	/**
	 * Returns the parameters of the method described by this string (a CONSTANT_NameAndType_info must point to this structure
	 * as a 'descriptor').
	 */
	public String getMethodParamsString() {
		String fullParams = value.substring( value.indexOf('(') + 1, value.lastIndexOf(')') );
		StringBuilder back = new StringBuilder();
		back.append("(");

		for (int i=0; i<fullParams.length(); ++i) {
			Object objs[] = getType(fullParams, i);
			back.append( objs[0] );
			i = (Integer) objs[1];
			if ( (i+1) < fullParams.length() ) {
				back.append(", ");
			}
		}

		return back.append(")").toString();
	}

	/**
	 * Returns the descriptor of this string (a CONSTANT_NameAndType_info must point to this structure
	 * as a 'descriptor').
	 */
	public String getDescriptorString() {
		int i = 0;
		StringBuilder array = new StringBuilder();
		while ( '[' == value.charAt(i) ) {
			array.append("[]");
			i++;
		}

		StringBuilder back = new StringBuilder();
		switch ( value.charAt(i) ) {
			case ('B') : back.append("byte"); break;
			case ('C') : back.append("char"); break;
			case ('D') : back.append("double"); break;
			case ('F') : back.append("float"); break;
			case ('I') : back.append("int"); break;
			case ('J') : back.append("long"); break;
			case ('S') : back.append("short"); break;
			case ('Z') : back.append("boolean"); break;
			case ('V') : back.append("void"); break;

			case ('L') : back.append( value.substring( value.indexOf('L') + 1, value.indexOf(';') ).replace('/', '.') );
					break;
		}

		back.append(array);

		return back.toString();
	}

	/**
	 * Returns the value stored in this entry.
	 */
	public String getUtf8String() {
		return value;
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>").append( getTagDescriptor(tag) ).append("</B><BR>");
		sb.append("Value: ").append( HTMLFilter.filter( value ) );
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ("Value: " + value);
	}

}// class.ConstantUtf8Info
