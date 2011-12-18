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

import jyzer.items.ConstantPool;
import jyzer.items.constantpool.ConstantPoolInfo;

// My own lib :-)
import edu.lro.web.HTMLFilter;

/**
 * <p> The CONSTANT_NameAndType_info structure is used to represent a field or method, without
 * indicating which class or interface type it belongs to. </p>
 *
 * <p> Contains the following information:</p>
 * <ul>
 *
 * <li> name_index <br>
 * The value of the <code>name_index</code> item must be a valid index into the <code>constant_pool</code> table. The <code>constant_pool</code>
 * entry at that index must be a <code>CONSTANT_Utf8_info</code> structure representing either a valid field or method name stored as a
 * simple name, that is, as a Java programming language identifier or as the special method name init or clinit.
 *
 * <li> descriptor_index <br>
 * The value of the <code>descriptor_index</code> item must be a valid index into the <code>constant_pool</code> table. The <code>constant_pool</code>
 * entry at that index must be a <code>CONSTANT_Utf8_info</code> structure representing a valid field descriptor or method descriptor.
 * </ul>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantNameAndTypeInfo extends ConstantPoolInfo {

	// u2
	private int nameIndex;
	private int descriptorIndex;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantNameAndTypeInfo(DataInput di) throws IOException {
		tag             = CONSTANT_NameAndType;
		nameIndex       = di.readUnsignedShort();
		descriptorIndex = di.readUnsignedShort();
	}

	/**
	 * Used to determine the return type. WILL BE GENERALISED SOON !!!
	 */
	private String getTypeString(String descriptor) {
		int i = 0;
		StringBuilder array = new StringBuilder();
		while ( '[' == descriptor.charAt(i) ) {
			array.append("[]");
			i++;
		}

		StringBuilder back = new StringBuilder();
		switch ( descriptor.charAt(i) ) {
			case ('B') : back.append("byte"); break;
			case ('C') : back.append("char"); break;
			case ('D') : back.append("double"); break;
			case ('F') : back.append("float"); break;
			case ('I') : back.append("int"); break;
			case ('J') : back.append("long"); break;
			case ('S') : back.append("short"); break;
			case ('Z') : back.append("boolean"); break;
			case ('V') : back.append("void"); break;

			case ('L') :
					back.append( descriptor.substring( descriptor.indexOf('L', i-1) + 1, descriptor.indexOf(';', i-1) ).replace('/', '.') );
					break;
		}

		back.append(array);
		return back.toString();
	}

	/**
	 * Returns the descriptor of this method.
	 */
	private String getDescriptorString() {
		return ConstantPool.getUtf8String(descriptorIndex);
	}

	/**
	 * Returns the name of the method.
	 */
	private String getNameString() {
		return ConstantPool.getUtf8String(nameIndex);
	}

	/**
	 * Returns the string representation of the return type of the method.
	 */
 	private String getReturnTypeString(String descriptor) {
		String desc = descriptor.substring( descriptor.lastIndexOf(')') + 1 );

		return getTypeString(desc);
	}

	/**
	 * Returns the string representation of the parameterlist.
	 */
	public String getParamsString(String descriptor) {
					// TEST this !!!

		StringBuilder back  = new StringBuilder("( ");
		StringBuilder array = new StringBuilder();

		try {
			String desc = descriptor.substring( descriptor.lastIndexOf('(') + 1, descriptor.lastIndexOf(')') );

			// IF there's no formal parameters ( ie. ()V )
			// '()' is better than '( )' :-)
			if (0 == desc.length() ) {
				return "()";
			}

			for (int i=0; i<desc.length(); ++i) {

				while ( '[' == desc.charAt(i) ) {
					array.append("[]");
					i++;
				}

				switch ( desc.charAt(i) ) {
					case ('B') : back.append("byte b"); break;
					case ('C') : back.append("char c"); break;
					case ('D') : back.append("double d"); break;
					case ('F') : back.append("float f"); break;
					case ('I') : back.append("int i"); break;
					case ('J') : back.append("long l"); break;
					case ('S') : back.append("short s"); break;
					case ('Z') : back.append("boolean b"); break;

					// needed ?!?
					//case ('V') : back.append("void"); break;

					case ('L') : {

						String formalParameter = descriptor.
										substring( descriptor.indexOf('L', i-1) + 1, descriptor.indexOf(';', i-1) ).
										replace('/', '.');

						back.append( formalParameter );
						back.append( " obj");
						i += formalParameter.length() + 2;
						break;
					}
				}

				back.append(array);
				array.delete( 0, array.length() );
			}

			back.append(" )");

		} catch (StringIndexOutOfBoundsException sioobe) {
			// IF it is a fieldreference info
			return "";
		}

		return back.toString();
	}// getParamsString

	/**
	 * Returns the full signature of the method.
	 */
	public String getSignatureString() {
		String descriptorString = getDescriptorString();

		return getReturnTypeString(descriptorString) + " " +
				getNameString() +
				getParamsString(descriptorString);
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

		sb.append("Name Index: ").append(nameIndex);
		sb.append(" <FONT color=\"blue\">// ").append( HTMLFilter.filter( ConstantPool.getUtf8String(nameIndex) ) ).append("</FONT><BR>");

		sb.append("Descriptor Index: ").append(descriptorIndex);
		sb.append(" <FONT color=\"blue\">// ").append( HTMLFilter.filter( ConstantPool.getUtf8String(descriptorIndex) ) ).append("</FONT>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ("Name Index: " + nameIndex       + " [" + ConstantPool.getUtf8String(nameIndex) + "], " +
				"Descriptor Index: " + descriptorIndex + " [" + ConstantPool.getUtf8String(descriptorIndex) + "]" );
	}

}// class.ConstantNameAndTypeInfo
