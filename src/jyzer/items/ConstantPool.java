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

import jyzer.items.factories.ConstantPoolInfoFactory;
import jyzer.items.constantpool.ConstantPoolInfo;
import jyzer.items.constantpool.types.*;

/**
 * <p> Gee, this class is one of the most important classes in this program. In general, the <code>constant_pool</code>
 * is a table of structures representing various string constants, class and interface names, field names, methodreferences
 * and other constants that are referred to within the <code>ClassFile</code> structure and its substructures. The format
 * of each entry is indicated by its first "tag" byte.<br>
 * The pool is indexed from 1 to <code>constant_pool_count</code>-1.</p>
 *
 * <p>TODO: find a better datastructure for improve access/creation efficency.</p>
 * <p>TODO: implement the logging if a wrong index is given to the static getters.</p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public final class ConstantPool {

	// --- Structure -----------------------------------------------------------------------------------

	private static int constantPoolCount;
	private static ConstantPoolInfo constantPool[];

	// --- Static interface ----------------------------------------------------------------------------

	/**
	 * Returns the <code>constant_pool_count</code>.
	 */
	public static int getConstantPoolCount() {
		return constantPoolCount;
	}

	/**
	 * Gets the string of an Utf8 entry from the pool.
	 *
	 * @param index index of a <code>CONSTANT_Utf8_info</code> structure.
	 * @return the value of the given structure.
	 */
	public static String getUtf8String(int index) {// LOG OTHERWISE !!!
		String back = null;

		if (constantPool[index] instanceof ConstantUtf8Info) {
			back = ( (ConstantUtf8Info) constantPool[index] ).getUtf8String();
		}

		return back;
	}// getUtf8String

	/**
	 * Gets the return value of a given method described by an Utf8 entry in the pool.
	 *
	 * @param index index of a <code>CONSTANT_Utf8_info</code> structure.
	 * @return the return value of the method.
	 */
	public static String getMethodReturnString(int index) {
		String back = null;

		if (constantPool[index] instanceof ConstantUtf8Info) {
			back = ( (ConstantUtf8Info) constantPool[index] ).getMethodReturnString();
		}

		return back;
	}// getMethodReturnString

	/**
	 * Gets the name of a given method described by an Utf8 entry in the pool. The name
	 * of the enclosingClass is needed to give back the same name if the method is a constructor.
	 * That can be detected if the name equals to "<init>". Accessed from <code>MethodInfo</code>.
	 *
	 * @param index index of a <code>CONSTANT_Utf8_info</code> structure.
	 * @param enclosingClass the name of the enclosing class.
	 * @return the name of the method.
	 */
	public static String getMethodName(int index, String enclosingClass) {
		String back = null;

		if (constantPool[index] instanceof ConstantUtf8Info) {
			back = ( (ConstantUtf8Info) constantPool[index] ).getUtf8String();

			if ( back.equals("<init>") ) {
				back = new String( enclosingClass );
			}
		}

		return back;
	}// getMethodName

	/**
	 * Gets the parameters of a given method described by an Utf8 entry in the pool.
	 *
	 * @param index index of a <code>CONSTANT_Utf8_info</code> structure.
	 * @return the formal parameters of a method in a string.
	 */
	public static String getMethodParamsString(int index) {
		String back = null;

		if (constantPool[index] instanceof ConstantUtf8Info) {
			back = ( (ConstantUtf8Info) constantPool[index] ).getMethodParamsString();
		}

		return back;
	}// getMethodParamsString

	/**
	 * Gets the signature of a given method described by a ConstantNameAndTypeInfo entry in the pool.
	 * These are not member methods, but methods that are invoked during the execution of the code like
	 * "System.out.println(...)".
	 *
	 * @param index index of a <code>CONSTANT_NameAndType_info</code> structure.
	 * @return the signature of the method (return type + name + parameters).
	 */
	public static String getSignatureString(int index) {
		String back = null;

		if (constantPool[index] instanceof ConstantNameAndTypeInfo) {
			back = ( (ConstantNameAndTypeInfo) constantPool[index] ).getSignatureString();
		}

		return back;
	}// getSignatureString

	/**
	 * Returns the descriptor (type) of a field, or the type of a parameter of a function.
	 *
	 * @param index index of a <code>CONSTANT_Utf8_info</code> structure.
	 * @return the formal parameters of a method in a string.
	 */
	public static String getDescriptorString(int index) {
		String back = null;

		if (constantPool[index] instanceof ConstantUtf8Info) {
			back = ( (ConstantUtf8Info) constantPool[index] ).getDescriptorString();
		}

		return back;
	}// getDescriptorString

	/**
	 * Returns the name of the class described by the given <code>CONSTANT_Class_info</code>.
	 *
	 * @param index index of a <code>ConstantClassInfo</code> structure.
	 * @return the formal parameters of a method in a string.
	 */
	public static String getClassName(int index) {
		String back = null;

		if (constantPool[index] instanceof ConstantClassInfo) {
			back = ( (ConstantClassInfo) constantPool[index] ).getNameString();
		}

		return back;
	}// getClassName

	/**
	 * Returns the value stored in the structure of the given index. The structure could be either a
	 * <code>CONSTANT_Long_info</code>, <code>CONSTANT_Float_info</code>, <code>CONSTANT_Double_info</code>,
	 * <code>CONSTANT_Integer_info</code> or a <code>CONSTANT_String_info</code>.
	 *
	 * @param index index of a structure (see above).
	 * @return the formal parameters of a method in a string.
	 */
	public static String getValueString(int index) {
		String back = null;

		if (constantPool[index] instanceof ConstantLongInfo) {
			back = String.valueOf( ( (ConstantLongInfo) constantPool[index] ).getValue() );
		} else if (constantPool[index] instanceof ConstantFloatInfo) {
			back = String.valueOf( ( (ConstantFloatInfo) constantPool[index] ).getValue() );
		} else if (constantPool[index] instanceof ConstantDoubleInfo) {
			back = String.valueOf( ( (ConstantDoubleInfo) constantPool[index] ).getValue() );
		} else if (constantPool[index] instanceof ConstantIntegerInfo) {
			back = String.valueOf( ( (ConstantIntegerInfo) constantPool[index] ).getValue() );
		} else if (constantPool[index] instanceof ConstantStringInfo) {
			back = "\"" + String.valueOf( ( (ConstantStringInfo) constantPool[index] ).getValue() ) + "\"";
		}

		return back;
	}// getValueString


	/**
	 * Returns the element at the given index of the <code>constant_pool</code>.
	 */
	public static ConstantPoolInfo getElement(int index) {
		return constantPool[index];
	}

	// --- Constructors -----------------------------------------------------------------------------

	/**
	 * The constructor, creates a new instance of ConstantPool. Builds up the table with creating
	 * the necessary entries.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public ConstantPool(DataInput di) throws IOException {
		constantPoolCount = di.readUnsignedShort();
		constantPool = new ConstantPoolInfo[constantPoolCount];

		// It is unused, says the specification.
		constantPool[0] = new ConstantDummyInfo();

		for (int i=1; i<constantPoolCount; ++i) {
			ConstantPoolInfo newOne = ConstantPoolInfoFactory.create(di);
			constantPool[i] = newOne;

			if ( newOne instanceof ConstantDoubleInfo) {
				constantPool[++i] = new ConstantDummyInfo(ConstantDummyInfo.Type.DOUBLE);
			}

			if ( newOne instanceof ConstantLongInfo ) {
				constantPool[++i] = new ConstantDummyInfo(ConstantDummyInfo.Type.LONG);
			}
		}

	}// ConstantPool(DataInput)

	// --- Own methods -------------------------------------------------------------------------------

	/**
	 * To get the information to the Constant Pool pane, to fill up the table.
	 *
	 * @return the array of string to fill a table.
	 */
	public String[][] getConstantPoolData() {

		String data[][] = new String[constantPoolCount][3];

		for (int i=0; i<constantPoolCount; ++i) {
			data[i][0] = String.valueOf(i);
			data[i][1] = constantPool[i].getDescription();
			data[i][2] = constantPool[i].toString();
		}

		return data;
	}// getConstantPoolData

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("<UL>");
		sb.append("<LI>Constant Pool Count: ").append(constantPoolCount);
		sb.append("<LI>Constant Pool Infos: ");

		sb.append("<OL>");
		for (int i=1; i<constantPoolCount; ++i) {
			sb.append("<LI>");
			constantPool[i].getHTMLDescription(sb);
		}

		sb.append("</OL>");
		sb.append("</UL>");
	}// getHTMLDescription

	// --- Super methods ---------------------------------------------------------------------------------

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Constant Pool Count: " + constantPoolCount + '\n');
		sb.append("Constant Pool Infos: " + '\n');

		for (int i=0; i<constantPoolCount; ++i) {
			sb.append("    ").append(i).append(": ").append( constantPool[i] );
			if (i+1<constantPoolCount) sb.append('\n');
		}

		return sb.toString();
	}// toString

}// class.ConstantPool
