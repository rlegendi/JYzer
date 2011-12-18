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

import jyzer.items.constantpool.ConstantPoolInfo;

import edu.lro.web.HTMLFilter;

/**
 * <p> Undefined tags MUST be ignored. Although, I had to handle them somehow. The unified structure of
 * the <code>constant_pool</code> entries allow it.</p>
 *
 * <p> Also used to indicate those structures that uses up 2 etries in the pool, like
 * <code>CONSTANT_Double_info</code>.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantDummyInfo extends ConstantPoolInfo {

	// --- Structure -----------------------------------------------------------------------------

	public enum Type {
		DOUBLE("double"),
		LONG("long");

		private final String name;

		Type(String name) {
			this.name = name;
		}

		public String toString() {
			return "<< This " + name + " takes up 2 entries in the constant pool >>";
		}
	} // enum.Type

	private Type type = null;
	private String message = null;

	// --- Constructors --------------------------------------------------------------------------

	/**
	 * Initializing an empty Dummy entry.
	 */
	public ConstantDummyInfo() {
		tag  = CONSTANT_Dummy;
	}

	/**
	 * Initializing a Dummy entry that describes a double-length entry.
	 *
	 * @param type the type of the double-length entry.
	 */
	public ConstantDummyInfo(Type type) {
		this();
		this.type = type;
	}

	/**
	 * Initializing a Dummy entry with a special message.
	 *
	 * @param message the descriptor.
	 */
	public ConstantDummyInfo(String message) {
		this();
		this.message = message;
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
		if ( type != null ) {
			sb.append( HTMLFilter.filter(type.toString()) );
			return;
		}

		if ( message != null ) {
			sb.append( HTMLFilter.filter(message) );
			return;
		}

		sb.append( HTMLFilter.filter( "<< UNUSED by the specification >>" ) );
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		if ( type != null ) {
			return type.toString();
		}

		if ( message != null ) {
			return message;
		}

		return "<< UNUSED by the specification >>";
	}

}// class.ConstantDummyInfo