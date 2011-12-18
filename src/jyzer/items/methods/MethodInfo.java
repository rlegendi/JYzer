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
package jyzer.items.methods;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.accessflags.AccessFlags;
import jyzer.items.attributes.Attributes;
import jyzer.items.attributes.types.AttributeInfo;
import jyzer.items.attributes.types.helpers.CodeInstruction;
import jyzer.items.ConstantPool;
import jyzer.items.exceptions.CorruptedClassfileException;
import jyzer.items.factories.AttributeFactory;

import edu.lro.web.HTMLFilter;

/**
 * <p>This class represents a method structure and all the rest.
 *
 * <p>Contains the following information:
 * <ul>
 * <li> Access flags<br>
 * <li> name_index <br>
 * 		Must be a valid index into the <code>constant_pool</code> table. The entry at that index
 * 		must be a <code>CONSTANT_Utf8_info</code> structure, which must represent a valid unqualified name
 * 		denoting a field.<br>
 * <li> descriptor_index <br>
 * 		Must be a valid index into the <code>constant_pool</code> table. The entry at that index
 * 		must be a <code>CONSTANT_Utf8_info</code> structure, which must represent a valid field descriptor.<br>
 * <li> Attributes
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov 3.
 */
public final class MethodInfo {

	// --- Structure -----------------------------------------------------------------------------

	private AccessFlags accessFlags;
	// u2
	private int nameIndex;
	// u4
	private int descriptorIndex;
	private Attributes methodAttributes;

	// --- Constructors --------------------------------------------------------------------------


	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws CorruptedClassfileException if the classfile seems to be corrupted.
	 * @throws IOException if an I/O error occurs.
	 */
	public MethodInfo(DataInput di) throws CorruptedClassfileException, IOException {
		accessFlags = new AccessFlags(di, AccessFlags.Type.METHOD);
		nameIndex   = di.readUnsignedShort();
		descriptorIndex = di.readUnsignedShort();

		methodAttributes = new Attributes(di);
	}

	// --- Getter methods -------------------------------------------------------------------------

	/** Returns true if this is a bridge method. */
	public boolean isBridge() {
		return accessFlags.isBridge();
	}

	/** Returns true if this is a class initializer method. */
	public boolean isClassInitializer() {
		return ConstantPool.getUtf8String(nameIndex).equals("<clinit>");
	}

	/** Returns true if this is a synthetic method. */
	public boolean isSynthetic() {
		return accessFlags.isSynthetic() || methodAttributes.hasSynthetic();
	}

	/** Tests if the method has any attributes. */
	public boolean hasAttributes() {
		return ( ! methodAttributes.isEmpty() );
	}

	/** Returns the number of attributes. */
	public int getAttributesCount() {
		return methodAttributes.getAttributesCount();
	}

	/** Returns the attributes. */
	public Attributes getAttributes() {
		return methodAttributes;
	}

	/** Tests if the method throws any exceptions (ie. has any exception attributes). */
	public boolean hasExceptions() {
		return methodAttributes.hasExceptions();
	}

	/** Tests if the method has any local variables attributes (ie. has the local variable table attribute). */
	public boolean hasTheCodeLocalVariables() {
		return methodAttributes.hasTheCodeLocalVariableTable();
	}

	/** Returns the descriptor string of this method.*/
	public String getDescriptorString() {
		return ConstantPool.getUtf8String(descriptorIndex);
	}

	/** Returns the disassembled code instructions of this code. */
	public java.util.Vector<CodeInstruction> getDisassembledCode() {
		return methodAttributes.getDisassembledCode();
	}

	/** Returns the full access string of this component. */
	public String getFullAccessString() {
		String afi = Integer.toHexString( accessFlags.getAccessInt() );
		String afs = accessFlags.getAccessString() + accessFlags.getRealModifierString();

		if ( afs.equals("") ) {
			afs = "N/A";
		}

		return  "0x" + afi + " [" + afs.trim() + "]";
	}

	/** Returns the full access string of this component, but only the <i>real</i> ones. */
	public String getFullRealAccessString() {
		return accessFlags.getAccessInt() + " [" + accessFlags.getModifierString() + "]";
	}

	/** Returns the name of this method. */
	public String getNameString() {
		return ConstantPool.getUtf8String(nameIndex);
	}

	/** Returns the descriptor of the name of this method. */
	public String getFullNameString() {
		return nameIndex + " [" + getNameString() + "]";
	}

	/** Returns the full descriptor of the descriptor of this method. :-)*/
	public String getFullDescriptorString() {
		return descriptorIndex + " [" + getDescriptorString() + "]";
	}

	/**
	 * Returns the pure names of the exceptions.
	 *
	 * @param enclosingClass the name of the enclosingClass.
	 */
	public String[] getPureExceptionNames(String enclosingClass) {
		return methodAttributes.getPureExceptionNames(enclosingClass);
	}

	/** Yeah, a bit ugly name :-) Returns the name of the local variables in the code. */
	public String[] getTheCodeLocalVariablesString() {
		return methodAttributes.getTheCodeLocalVariablesString();
	}

	/**
	 * Returns the signature of the method, used for constructors.
	 *
	 * @param enclosingClass the name of the enclosingClass.
	 */
	public String getSignature(String enclosingClass) {
		String returnType = ConstantPool.getMethodReturnString(descriptorIndex);

		if ( isClassInitializer() ) {
			return "static";
		} else if ( ConstantPool.getUtf8String(nameIndex).equals("<init>") ) {
			returnType = "";
		} else {
			returnType += " ";
		}

		return accessFlags.getAccessString() +
			   accessFlags.getRealModifierString() +
			   returnType +
			   ConstantPool.getMethodName(nameIndex, enclosingClass) +
			   ConstantPool.getMethodParamsString(descriptorIndex);
	}

	/**
	 * Returns the signature of the method, used for constructors.
	 * Was nedded to get the return type correctly (& params ?!?)
	 *
	 * @param enclosingClass the name of the enclosingClass.
	 */
	public String getPureSignature(String enclosingClass) {
		// for constructors ...
		String returnType = ConstantPool.getMethodReturnString(descriptorIndex);

		if ( isClassInitializer() ) {
			return "static";
		} else if ( ConstantPool.getUtf8String(nameIndex).equals("<init>") ) {
			returnType = "";
		} else {
			returnType += " ";
		}

		String pureReturnType = returnType;
		if ( returnType.contains(".") ) {
			pureReturnType = returnType.substring( returnType.lastIndexOf(".") + 1 );
		} else {
			pureReturnType = returnType;
		}

		return accessFlags.getAccessString() +
			   accessFlags.getRealModifierString() +
			   pureReturnType +
			   ConstantPool.getMethodName(nameIndex, enclosingClass) +
			   ConstantPool.getMethodParamsString(descriptorIndex);
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
		sb.append("Access Flags: ");
		accessFlags.getHTMLDescription(sb);
		sb.append("<BR>");

		sb.append("Name Index: ").append(nameIndex);
		sb.append(" <FONT color=\"blue\">// ").append( HTMLFilter.filter( ConstantPool.getUtf8String(nameIndex) ) ).append("</FONT><BR>");

		sb.append("Descriptor Index: ").append(descriptorIndex);
		sb.append(" <FONT color=\"blue\">// ").append( ConstantPool.getUtf8String(descriptorIndex) ).append("</FONT><BR>");

		methodAttributes.getHTMLDescription(sb);
	}

	// --- Super methods ----------------------------------------------------------------------------

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("MethodInfo:\n");
		sb.append("Access Flags: ").append(accessFlags).append('\n');
		sb.append("Name Index: ").append(nameIndex).append('\n');
		sb.append("Descriptor Index: ").append(descriptorIndex).append('\n');

		sb.append("\nMethod Attributes:\n");
		sb.append(methodAttributes);

		sb.append("\n\n");

		return sb.toString().trim();
	}

}// class.MethodInfo
