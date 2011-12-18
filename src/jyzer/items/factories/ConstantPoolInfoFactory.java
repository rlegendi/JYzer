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

import jyzer.items.constantpool.ConstantPoolInfo;
import jyzer.items.constantpool.types.*;

import static jyzer.items.constantpool.ConstantPoolInfo.*;

/**
 * A <i>singleton factory</i> to create instances of constantpool entries.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 1.
 */
public class ConstantPoolInfoFactory {

	/**
	 * First of all - prevent anyone to create any instance of this class.
	 * This is the Java way :-)
	 */
	private ConstantPoolInfoFactory() {
	}

	/**
	 * A static method to create a constantpool entry from the given <code>DataInput</code>.
	 *
	 * @return the created <code>ConstantPoolInfo</code>.
	 * @throws IOException if an I/O error occurs.
	 */
	public static ConstantPoolInfo create(DataInput di) throws IOException {
		ConstantPoolInfo back = null;
		int tag = di.readUnsignedByte();

		switch (tag) {
			case (CONSTANT_Class):              back = new ConstantClassInfo(di); break;
			case (CONSTANT_Fieldref):           back = new ConstantFieldrefInfo(di); break;
			case (CONSTANT_Methodref):          back = new ConstantMethodrefInfo(di); break;
			case (CONSTANT_InterfaceMethodref): back = new ConstantInterfaceMethodrefInfo(di); break;
			case (CONSTANT_String):             back = new ConstantStringInfo(di); break;
			case (CONSTANT_Integer):            back = new ConstantIntegerInfo(di); break;
			case (CONSTANT_Float):              back = new ConstantFloatInfo(di); break;
			case (CONSTANT_Long):               back = new ConstantLongInfo(di); break;
			case (CONSTANT_Double):             back = new ConstantDoubleInfo(di); break;
			case (CONSTANT_NameAndType):        back = new ConstantNameAndTypeInfo(di); break;
			case (CONSTANT_Utf8):               back = new ConstantUtf8Info(di); break;

			default: back = new ConstantDummyInfo("UNIDENTIFIED TAG: [" + tag + "]"); break;
		}

		return back;
	}

}// class.ConstantPoolInfoFactory
