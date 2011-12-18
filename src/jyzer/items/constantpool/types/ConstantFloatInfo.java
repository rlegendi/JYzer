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
 * <p> The CONSTANT_Integer_info and CONSTANT_Float_info structures represent 4-byte numeric (int and float) constants.</p>
 *
 * <p> Contains the following information:
 * <ul>
 * <li>bytes
 *
 * <p>The bytes item of the CONSTANT_Integer_info structure represents the value of the int constant. The bytes of the
 * value are stored in big-endian (high byte first) order.</p>
 *
 * <p>The bytes item of the CONSTANT_Float_info structure represents the value of the float constant in IEEE 754
 * floating-point single format (§3.3.2). The bytes of the single format representation are stored in big-endian (high byte first) order.</p>
 *
 * <p>The value represented by the CONSTANT_Float_info structure is determined as follows. The bytes of the value are first
 * converted into an int constant bits. Then: </p>
 *
 * <ul>
 * <li>If bits is 0x7f800000, the float value will be positive infinity.
 * <li>If bits is 0xff800000, the float value will be negative infinity.
 * <li>If bits is in the range 0x7f800001 through 0x7fffffff or in the range 0xff800001 through 0xffffffff, the float value will be NaN.
 * <li>In all other cases, let s, e, and m be three values that might be computed from bits:<br>
 *    	int s = ((bits >> 31) == 0) ? 1 : -1;<br>
 *    	int e = ((bits >> 23) & 0xff);<br>
 *    	int m = (e == 0) ?<br>
 *    			(bits & 0x7fffff) << 1 :<br>
 *    			(bits & 0x7fffff) | 0x800000;
 * </ul>
 *
 * <p>Then the float value equals the result of the mathematical expression s*m*2^(e-150).</p>
 * </ul>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantFloatInfo extends ConstantPoolInfo {

	// high & low bytes
	private float value;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantFloatInfo(DataInput di) throws IOException {
		tag   = CONSTANT_Float;
		value = di.readFloat();
	}

	/**
	 * Returns the value stored in this entry.
	 */
	public float getValue() {
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
		sb.append("Value: ").append(value);
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ("Value: " + value);
	}

}// class.ConstantFloatInfo
