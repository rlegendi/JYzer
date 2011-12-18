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
package jyzer.items.constantpool;

import java.io.DataInput;
import java.io.IOException;

/**
 * <p> An abstract superclass for all of the <code>constant_pool</code> entries. Unknown
 * entries are handled with <code>CONSTANT_Dummy</code>.</p>
 *
 * <hr>
 *
 * <b>About the tags:</b><br>
 * Here's a little list about the available tags, and with my extension.
 *
 * <p>
 * <table border="1">
 * <tr>
 *   <td>CONSTANT_Class</td>
 *   <td>7</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Fieldref</td>
 *   <td>9</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Methodref</td>
 *   <td>10</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_InterfaceMethodref</td>
 *   <td>11</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_String</td>
 *   <td>8</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Integer</td>
 *   <td>3</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Float</td>
 *   <td>4</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Long</td>
 *   <td>5</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Double</td>
 *   <td>6</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_NameAndType</td>
 *   <td>12</td>
 * </tr>
 * <tr>
 *   <td>CONSTANT_Utf8</td>
 *   <td>1</td>
 * </tr>
 * <tr>
 *   <td><b>CONSTANT_Dummy</b></td>
 *   <td>0</td>
 * </tr>
 * </table>
 * </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 nov 4.
 */
public abstract class ConstantPoolInfo {

	// --- Static Constants -------------------------------------------------------------

	public static final int CONSTANT_Class              = 7;
	public static final int CONSTANT_Fieldref           = 9;
	public static final int CONSTANT_Methodref          = 10;
	public static final int CONSTANT_InterfaceMethodref = 11;
	public static final int CONSTANT_String             = 8;
	public static final int CONSTANT_Integer            = 3;
	public static final int CONSTANT_Float              = 4;
	public static final int CONSTANT_Long               = 5;
	public static final int CONSTANT_Double             = 6;
	public static final int CONSTANT_NameAndType        = 12;
	public static final int CONSTANT_Utf8               = 1;

	/** My extension for unknown tags. */
	public static final int CONSTANT_Dummy              = 0;

	// --- Variables ---------------------------------------------------------------------------

	protected int tag;
	private   int info[]; // needed only here, the others care for their own data ;-)

	/** Prohibit insticiating. */
	protected ConstantPoolInfo() {}

	// --- Static Interface --------------------------------------------------------------------

	/**
	 * Returns the name of the structure, depending on <code>tag</code>.
	 *
	 * @return the string representation.
	 */
	public static String getTagDescriptor(int tag) {
		StringBuilder sb = new StringBuilder();

		switch (tag) {
			case (CONSTANT_Class):              sb.append("CONSTANT_Class"); break;
			case (CONSTANT_Fieldref):           sb.append("CONSTANT_Fieldref"); break;
			case (CONSTANT_Methodref):          sb.append("CONSTANT_Methodref"); break;
			case (CONSTANT_InterfaceMethodref): sb.append("CONSTANT_InterfaceMethodref"); break;
			case (CONSTANT_String):             sb.append("CONSTANT_String"); break;
			case (CONSTANT_Integer):            sb.append("CONSTANT_Integer"); break;
			case (CONSTANT_Float):              sb.append("CONSTANT_Float"); break;
			case (CONSTANT_Long):               sb.append("CONSTANT_Long"); break;
			case (CONSTANT_Double):             sb.append("CONSTANT_Double"); break;
			case (CONSTANT_NameAndType):        sb.append("CONSTANT_NameAndType"); break;
			case (CONSTANT_Utf8):               sb.append("CONSTANT_Utf8"); break;
			case (CONSTANT_Dummy):              sb.append("UNUSED"); break;
			default:                            sb.append("UNKNOWN !!!"); break;
		}

		return sb.toString();
	}

	// --- Getter methods ----------------------------------------------------------------------

	/**
	 * Just for testing reasons only !!!! I'm missing seriously the compilation directives
	 * like C had anno, like #ifdef DEBUG. Maybe I should use a prepocessor ...
	 *
	 * @return the tag.
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * Returns the tag descriptor. Ok, I was just too lazy to call always the static method.
	 *
	 * @return the name of the element.
	 */
	public String getDescription() {
		return getTagDescriptor(tag);
	}

	/**
	 * Tests if this is a <code>CONSTANT_Class</code> structure.
	 */
	public boolean isClassInfo() {
		return (CONSTANT_Class == tag);
	}

	/**
	 * Tests if this is a double sized entry.
	 */
	public boolean isDoubleSized() {
		return ( (CONSTANT_Double == tag) || (CONSTANT_Long == tag) );
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public abstract void getHTMLDescription(StringBuilder sb);

}// class.ConstantPoolInfo
