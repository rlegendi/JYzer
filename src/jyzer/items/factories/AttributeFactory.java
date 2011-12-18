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
package jyzer.items.factories;

import java.io.DataInput;
import java.io.IOException;

import jyzer.items.attributes.types.*;
import jyzer.items.constantpool.types.ConstantDummyInfo;
import jyzer.items.ConstantPool;
import jyzer.items.exceptions.CorruptedClassfileException;

/**
 * A <i>singleton factory</i> to create instances of attributes. Every object that has
 * any attributes should delegate the request here to create an attribute. To create
 * them on your own is permitted, but use them at your own risk!
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 oct 17
 */
public class AttributeFactory {

	// --- Constant declarations ----------------------------------------------------------

	public static final String CONSTANT_VALUE_NAME = "ConstantValue";
	public static final int CONSTANT_VALUE_LENGTH  = 2;

	public static final String CODE_NAME = "Code";

	public static final String EXCEPTIONS_NAME = "Exceptions";

	public static final String INNER_CLASSES_NAME = "InnerClasses";

	public static final String ENCLOSING_METHOD_NAME = "EnclosingMethod";
	public static final int ENCLOSING_METHOD_LENGTH = 4;

	public static final String SYNTHETIC_NAME = "Synthetic";
	public static final int SYNTHETIC_LENGTH = 0;

	public static final String SIGNATURE_NAME = "Signature";
	public static final int SIGNATURE_LENGTH = 2;

	public static final String SOURCE_FILE_NAME = "SourceFile";
	public static final int SOURCE_FILE_LENGTH = 2;

	public static final String SOURCE_DEBUG_EXTENSION_NAME = "SourceDebugExtension";

	public static final String LINE_NUMBER_TABLE_NAME = "LineNumberTable";

	public static final String LOCAL_VARIABLE_TABLE_NAME = "LocalVariableTable";

	public static final String LOCAL_VARIABLE_TYPE_TABLE_NAME = "LocalVariableTypeTable";

	public static final String DEPRECATED_NAME = "Deprecated";

	// --- Methods ----------------------------------------------------------------------

	/**
	 * First of all - prevent anyone to create any instance of this class.
	 * This is the Java way :-)
	 */
	private AttributeFactory() {}

	/**
	 * A static method to create an attribute from the given <code>DataInput</code>.
	 *
	 * @return the created <code>AttributeInfo</code>.
	 * @throws IOException if an I/O error occurs.
	 * @throws CorruptedClassfileException when the required attribute lenght is not equal to the read length.
	 */
	public static AttributeInfo create(DataInput di) throws IOException, CorruptedClassfileException {
		AttributeInfo back = null;

		int attributeNameIndex = di.readUnsignedShort();
		int attributeLength = di.readInt();

		String name = ConstantPool.getUtf8String(attributeNameIndex);

		// ConstantValueAttribute
		if ( name.equals(CONSTANT_VALUE_NAME) ) {
			if ( CONSTANT_VALUE_LENGTH != attributeLength ) {
				throw new CorruptedClassfileException("Attribute Factory", "CONSTANT_VALUE_LENGTH is corrupted!");
			}

			back = new ConstantValueAttribute(di, attributeNameIndex, attributeLength);


		// CodeAttribute
		} else if ( name.equals(CODE_NAME) ) {
			back = new CodeAttribute(di, attributeNameIndex, attributeLength);


		// ExceptionsAttribute
		} else if ( name.equals(EXCEPTIONS_NAME) ) {
			back = new ExceptionsAttribute(di, attributeNameIndex, attributeLength);


		// InnerClassesAttribute
		} else if ( name.equals(INNER_CLASSES_NAME) ) {
			back = new InnerClassesAttribute(di, attributeNameIndex, attributeLength);


		// EnclosingMethodAttribute
		} else if ( name.equals(ENCLOSING_METHOD_NAME) ) {
			if ( ENCLOSING_METHOD_LENGTH != attributeLength ) {
				throw new CorruptedClassfileException("Attribute Factory", "ENCLOSING_METHOD_LENGTH is corrupted!");
			}

			back = new EnclosingMethodAttribute(di, attributeNameIndex, attributeLength);


		// SyntheticAttribute
		} else if ( name.equals(SYNTHETIC_NAME) ) {
			if ( SYNTHETIC_LENGTH != attributeLength ) {
				throw new CorruptedClassfileException("Attribute Factory", "SYNTHETIC_LENGTH is corrupted!");
			}

			back = new SyntheticAttribute(di, attributeNameIndex, attributeLength);


		// SignatureAttribute
		} else if ( name.equals(SIGNATURE_NAME) ) {
			if ( SIGNATURE_LENGTH != attributeLength ) {
				throw new CorruptedClassfileException("Attribute Factory", "SIGNATURE_LENGTH is corrupted!");
			}

			back = new SignatureAttribute(di, attributeNameIndex, attributeLength);


		// SourceFileAttribute
		} else if ( name.equals(SOURCE_FILE_NAME) ) {
			if ( SOURCE_FILE_LENGTH != attributeLength ) {
				throw new CorruptedClassfileException("Attribute Factory", "SOURCE_FILE_LENGTH is corrupted!");
			}

			back = new SourceFileAttribute(di, attributeNameIndex, attributeLength);


		// SourceDebugExtensionAttribute
		} else if ( name.equals(SOURCE_DEBUG_EXTENSION_NAME) ) {
			back = new SourceDebugExtensionAttribute(di, attributeNameIndex, attributeLength);


		// LineNumberTableAttribute
		} else if ( name.equals(LINE_NUMBER_TABLE_NAME) ) {
			back = new LineNumberTableAttribute(di, attributeNameIndex, attributeLength);


		// LocalVariableTableAttribute
		} else if ( name.equals(LOCAL_VARIABLE_TABLE_NAME) ) {
			back = new LocalVariableTableAttribute(di, attributeNameIndex, attributeLength);


		// LocalVariableTableTypeAttribute
		} else if ( name.equals(LOCAL_VARIABLE_TYPE_TABLE_NAME) ) {
			back = new LocalVariableTypeTableAttribute(di, attributeNameIndex, attributeLength);


		// DeprecatedAttribute
		} else if ( name.equals(DEPRECATED_NAME) ) {
			back = new DeprecatedAttribute(di, attributeNameIndex, attributeLength);

		// Unknown tag - for compatibility in the future
		} else {
			back = new DummyAttribute("<< UNIDENTIFIED TAG: [" + attributeNameIndex + "] >>");
		}

		return back;
	}// create

}// class.AttributeFactory
