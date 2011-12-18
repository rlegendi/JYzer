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

import jyzer.items.exceptions.CorruptedClassfileException;
import jyzer.items.fields.FieldInfo;

/**
 * <p> Cares for the <i>field</i> entries of the classfile. </p>
 *
 * <p> Contains the following information:
 * <ul>
 * <li> u2 fields_count <br>
 *		Gives the number of <code>field_info</code> structures in the fields table.
 *		The <code>filed_info</code> structures represent all fields, both class variables,
 *		declared by this class or interface type.
 * <li> field_info fields[] <br>
 *		Each value in the table must be a <code>field_info</code> structure, giving a
 *		complete description of a field in this class or interface. It doesn't include
 *		items representing fields that are inherited from superclasses or superinterfaces.
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 okt 12.
 */
public final class Fields {

	// --- Structure -----------------------------------------------------------------------------

	// u2
	private int fieldsCount;
	private FieldInfo fields[];

	// needed to create field attributes
	private boolean isInterfaceField;

	// --- Constructors --------------------------------------------------------------------------

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws CorruptedClassfileException if the classfile seems to be corrupted.
	 * @throws IOException if an I/O error occurs.
	 */
	public Fields(DataInput di, boolean isInterfaceField) throws CorruptedClassfileException, IOException {
		fieldsCount = di.readUnsignedShort();
		fields = new FieldInfo[fieldsCount];

		for (int i=0; i<fieldsCount; ++i) {
			fields[i] = new FieldInfo(di, isInterfaceField);
		}

		this.isInterfaceField = isInterfaceField;
	}

	// --- Getter methods -------------------------------------------------------------------------

	/**
	 * Returns the <code>fields[]</code> structure.
	 *
	 * @return fields[] as a FieldInfo array
	 */
	public FieldInfo[] getFields() {
		return fields;
	}

	/**
	 * Returns a 2 dimension representation of the fields (to put it into a table).
	 *
	 * @see jyzer.items.accessflags.AccessFlags
	 * @param showOnlyRealModifiers if should care only with real modifiers.
	 * @return the field data in a two dimensin array
	 */
	public Object[][] getFieldData(boolean showOnlyRealModifiers) {
		Object back[][] = new Object[fieldsCount][5];

		for (int i=0; i<back.length; ++i) {

				if (showOnlyRealModifiers) {
					back[i][0] = fields[i].getFullRealAccessString();
				} else {
					back[i][0] = fields[i].getFullAccessString();
				}

				back[i][1] = fields[i].getFullNameString();
				back[i][2] = fields[i].getFullDescriptorString();
		}

		return back;
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
		if ( 0 == fieldsCount ) {
			sb.append("<UL><LI>Fields: NONE</UL>");
			return;
		}

		sb.append("<UL>");
		sb.append("<LI>Fields Count: ").append(fieldsCount);
		sb.append("<LI>Fields:");

		sb.append("<OL>");
		for (FieldInfo finfo: fields) {
			sb.append("<LI>");
			finfo.getHTMLDescription(sb);
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
		StringBuilder sb = new StringBuilder();

		sb.append("Fields Count: ").append(fieldsCount).append('\n');

		for (int i=0; i<fieldsCount; ++i) {
			sb.append(i).append( fields[i] ).append('\n');
		}

		return sb.toString();
	}

}// class.Fields
