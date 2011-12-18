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
 * <p> The class representing <code>CONSTANT_Double_info</code> structure. Almost the same as the
 * <code>CONSTANT_Long_info</code>. </p>
 *
 * <p> Contains the following information:
 * <ul>
 *
 * <li> u2 high_bytes, low_bytes <br>
 *
 * <p>The unsigned high_bytes and low_bytes items of the CONSTANT_Long_info structure together
 * represent the value of the long constant ((long) high_bytes << 32) + low_bytes, where the
 * bytes of each of high_bytes and low_bytes are stored in big-endian (high byte first) order.</p>
 *
 * <p>The high_bytes and low_bytes items of the CONSTANT_Double_info structure together represent
 * the double value in IEEE 754 floating-point double format (§3.3.2). The bytes of each item
 * are stored in big-endian (high byte first) order.</p>
 *
 * <p>The value represented by the CONSTANT_Double_info structure is determined as follows. The
 * high_bytes and low_bytes items are first converted into the long constant bits, which is equal
 * to ((long) high_bytes << 32) + low_bytes. Then:</p>
 *
 * <ul>
 * <li>If bits is 0x7ff0000000000000L, the double value will be positive infinity.
 * <li>If bits is 0xfff0000000000000L, the double value will be negative infinity.
 * <li>If bits is in the range 0x7ff0000000000001L through 0x7fffffffffffffffL or in the range
 * 0xfff0000000000001L through 0xffffffffffffffffL, the double value will be NaN.
 *
 * <li>In all other cases, let s, e, and m be three values that might be computed from bits:<br>
 *     	int s = ((bits >> 63) == 0) ? 1 : -1;<br>
 *    	int e = (int)((bits >> 52) & 0x7ffL);<br>
 *    	long m = (e == 0) ?<br>
 *    		(bits & 0xfffffffffffffL) << 1 :<br>
 *    		(bits & 0xfffffffffffffL) | 0x10000000000000L;<br>
 * </ul>
 * Then the floating-point value equals the double value of the mathematical expression s*m*2^(e-1075).
 *
 * </ul></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov. 7.
 */
public final class ConstantLongInfo extends ConstantPoolInfo {

	private long value;

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantLongInfo(DataInput di) throws IOException {
		tag = CONSTANT_Long;
		value = di.readLong();
	}

	/**
	 * Returns the long value of this entry.
	 */
	public long getValue() {
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

}// class.ConstantLongInfo
