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
import java.util.Vector;

import jyzer.items.attributes.Attributes;
import jyzer.items.exceptions.CorruptedClassfileException;
import jyzer.items.attributes.types.helpers.Code;
import jyzer.items.attributes.types.helpers.CodeInstruction;
import jyzer.items.attributes.types.helpers.ExceptionTable;

/**
 * A class representing the code attribute. The Code attribute is a variable-length attribute
 * used in the attributes table of method_info structures. A Code attribute contains the Java
 * virtual machine instructions and auxiliary information for a single method, instance
 * initialization method, or class or interface initialization method. Every Java virtual
 * machine implementation must recognize Code attributes. If the method is either native or
 * abstract, its method_info structure must not have a Code attribute. Otherwise, its method_info
 * structure must have exactly one Code attribute.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 12.
 */
public final class CodeAttribute extends AttributeInfo {

	// u2
	private int maxStack;
	private int maxLocals;

	private Code code;
	private ExceptionTable exceptionTable;
	private Attributes attributesOfCode;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param attributeLength the length of the attribute.
	 * @throws IOException if an I/O error occurs.
	 * @throws CorruptedClassfileException if the classfile seems to be corrupted.
	 */
	public CodeAttribute(DataInput di, int attributeNameIndex, int attributeLength) throws CorruptedClassfileException, IOException {
		super(attributeNameIndex, attributeLength);

		maxStack  = di.readUnsignedShort();
		maxLocals = di.readUnsignedShort();
		code = new Code(di);
		exceptionTable = new ExceptionTable(di);
		attributesOfCode = new Attributes(di);
	}

	/** Tests if the code has a <code>LocalVariableTable</code>. */
	public boolean hasLocalVariableTable() {
		return attributesOfCode.hasLocalVariableTable();
	}

	/**
	 * Returns the names of the local variables.
	 */
	public String[] getLocalVariablesString() {
		return attributesOfCode.getLocalVariablesString();
	}

	/**
	 * Returns the attributes of the code.
	 */
	public Attributes getAttributes() {
		return attributesOfCode;
	}

	/**
	 * Returns the disassembled code.
	 */
	public Vector<CodeInstruction> getDisassembledCode() {
		return code.getDisassembledCode();
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<B>Code Attribute:</B><BR>");
		sb.append("Max Stack: ").append(maxStack).append("<BR>");
		sb.append("Max Locals: ").append(maxLocals).append("<BR>");
		sb.append("<BR>");
		code.getHTMLDescription(sb);
		sb.append("<BR><BR>");
		exceptionTable.getHTMLDescription(sb);
		sb.append("<BR>");
		attributesOfCode.getHTMLDescription(sb);
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Code Attribute:\n");
		sb.append("Max Stack: ").append(maxStack).append('\n');
		sb.append("Max Locals: ").append(maxLocals).append('\n');
		sb.append(code).append("\n");

		sb.append(exceptionTable).append('\n');
		sb.append(attributesOfCode).append('\n');

		return sb.toString();
	}

}// class.CodeAttribute
