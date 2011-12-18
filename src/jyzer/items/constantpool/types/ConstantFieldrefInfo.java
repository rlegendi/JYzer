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

/**
 * <p> Contains the following information:
 * <ul>
 *
 * <li> u2 class_index <br>
 *		An index into the <code>constant_pool</code> that is a <code>CONSTANT_Class_info</code>
 * 		that has the field or method as a member.
 *
 * <li> method_info mehtods[] <br>
 *		Must be a valid index into the <code>constant_pool</code> table. The entry at that index
 *		must be a <code>CONSTANT_NameAndType_info</code> structure. This entry indicates the name
 *		and descriptor of the field or method.
 *
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantFieldrefInfo extends AbstractReference {

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantFieldrefInfo(DataInput di) throws IOException {
		super(di, CONSTANT_Fieldref);
	}

}// class.ConstantFieldrefInfo
