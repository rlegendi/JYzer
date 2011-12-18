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

import edu.lro.web.HTMLFilter;

/**
 * <p> An entry of the ExceptionTable (used by the <code>CodeAttribute</code>). </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public final class ExceptionTableEntry {

	// u2
	private int startPc;
	private int endPc;
	private int handlerPc;
	private int catchType;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 * Needs severe testing ;-)
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ExceptionTableEntry(DataInput di) throws IOException {
		startPc   = di.readUnsignedShort();
		endPc     = di.readUnsignedShort();
		handlerPc = di.readUnsignedShort();
		catchType = di.readUnsignedShort();
	}

	/** Returns the catch type of this exception entry. */
	private String getCatchTypeString() {
		if ( 0 == catchType ) {
			return "ALL/finally";
		}

		return ConstantPool.getClassName(catchType);
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("StartPc   : ").append(startPc).append("<BR>");
		sb.append("EndPc     : ").append(endPc).append("<BR>");
		sb.append("HandlerPc : ").append(handlerPc).append("<BR>");
		sb.append("Catch Type: ").append(catchType).append(" <FONT color=\"blue\">// ").
				append( HTMLFilter.filter( getCatchTypeString() ) ).append("</FONT><BR>");
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("StartPc   : ").append(startPc);
		sb.append("EndPc     : ").append(endPc);
		sb.append("HandlerPc : ").append(handlerPc);
		sb.append("Catch Type: ").append(catchType);

		return sb.toString();
	}

}// class.ExceptionTableEntry
