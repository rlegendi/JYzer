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

/**
 * <p> A class reprecenting an opcode. </p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. nov. 16.
 */
public class CodeInstruction {

	public static final String INSTRUCTIONS[];

	static {
		INSTRUCTIONS = new String[256];

		INSTRUCTIONS[0x00] = "nop";
		INSTRUCTIONS[0x01] = "aconst_null";
		INSTRUCTIONS[0x02] = "iconst_m1";
		INSTRUCTIONS[0x03] = "iconst_0";
		INSTRUCTIONS[0x04] = "iconst_1";
		INSTRUCTIONS[0x05] = "iconst_2";
		INSTRUCTIONS[0x06] = "iconst_3";
		INSTRUCTIONS[0x07] = "iconst_4";
		INSTRUCTIONS[0x08] = "iconst_5";
		INSTRUCTIONS[0x09] = "lconst_0";
		INSTRUCTIONS[0x0a] = "lconst_1";
		INSTRUCTIONS[0x0b] = "fconst_0";
		INSTRUCTIONS[0x0c] = "fconst_1";
		INSTRUCTIONS[0x0d] = "fconst_2";
		INSTRUCTIONS[0x0e] = "dconst_0";
		INSTRUCTIONS[0x0f] = "dconst_1";

		INSTRUCTIONS[0x10] = "bipush";
		INSTRUCTIONS[0x11] = "sipush";
		INSTRUCTIONS[0x12] = "ldc";
		INSTRUCTIONS[0x13] = "ldc_w";
		INSTRUCTIONS[0x14] = "ldc2_w";
		INSTRUCTIONS[0x15] = "iload";
		INSTRUCTIONS[0x16] = "lload";
		INSTRUCTIONS[0x17] = "fload";
		INSTRUCTIONS[0x18] = "dload";
		INSTRUCTIONS[0x19] = "aload";
		INSTRUCTIONS[0x1a] = "iload_0";
		INSTRUCTIONS[0x1b] = "iload_1";
		INSTRUCTIONS[0x1c] = "iload_2";
		INSTRUCTIONS[0x1d] = "iload_3";
		INSTRUCTIONS[0x1e] = "lload_0";
		INSTRUCTIONS[0x1f] = "lload_1";

		INSTRUCTIONS[0x20] = "lload_2";
		INSTRUCTIONS[0x21] = "lload_3";
		INSTRUCTIONS[0x22] = "fload_0";
		INSTRUCTIONS[0x23] = "fload_1";
		INSTRUCTIONS[0x24] = "fload_2";
		INSTRUCTIONS[0x25] = "fload_3";
		INSTRUCTIONS[0x26] = "dload_0";
		INSTRUCTIONS[0x27] = "dload_1";
		INSTRUCTIONS[0x28] = "dload_2";
		INSTRUCTIONS[0x29] = "dload_3";
		INSTRUCTIONS[0x2a] = "aload_0";
		INSTRUCTIONS[0x2b] = "aload_1";
		INSTRUCTIONS[0x2c] = "aload_2";
		INSTRUCTIONS[0x2d] = "aload_3";
		INSTRUCTIONS[0x2e] = "iaload";
		INSTRUCTIONS[0x2f] = "laload";

		INSTRUCTIONS[0x30] = "faload";
		INSTRUCTIONS[0x31] = "daload";
		INSTRUCTIONS[0x32] = "aaload";
		INSTRUCTIONS[0x33] = "baload";
		INSTRUCTIONS[0x34] = "caload";
		INSTRUCTIONS[0x35] = "saload";
		INSTRUCTIONS[0x36] = "istore";
		INSTRUCTIONS[0x37] = "lstore";
		INSTRUCTIONS[0x38] = "fstore";
		INSTRUCTIONS[0x39] = "dstore";
		INSTRUCTIONS[0x3a] = "astore";
		INSTRUCTIONS[0x3b] = "istore_0";
		INSTRUCTIONS[0x3c] = "istore_1";
		INSTRUCTIONS[0x3d] = "istore_2";
		INSTRUCTIONS[0x3e] = "istore_3";
		INSTRUCTIONS[0x3f] = "lstore_0";

		INSTRUCTIONS[0x40] = "lstore_1";
		INSTRUCTIONS[0x41] = "lstore_2";
		INSTRUCTIONS[0x42] = "lstore_3";
		INSTRUCTIONS[0x43] = "fstore_0";
		INSTRUCTIONS[0x44] = "fstore_1";
		INSTRUCTIONS[0x45] = "fstore_2";
		INSTRUCTIONS[0x46] = "fstore_3";
		INSTRUCTIONS[0x47] = "dstore_0";
		INSTRUCTIONS[0x48] = "dstore_1";
		INSTRUCTIONS[0x49] = "dstore_2";
		INSTRUCTIONS[0x4a] = "dstore_3";
		INSTRUCTIONS[0x4b] = "astore_0";
		INSTRUCTIONS[0x4c] = "astore_1";
		INSTRUCTIONS[0x4d] = "astore_2";
		INSTRUCTIONS[0x4e] = "astore_3";
		INSTRUCTIONS[0x4f] = "iastore";

		INSTRUCTIONS[0x50] = "lastore";
		INSTRUCTIONS[0x51] = "fastore";
		INSTRUCTIONS[0x52] = "dastore";
		INSTRUCTIONS[0x53] = "aastore";
		INSTRUCTIONS[0x54] = "bastore";
		INSTRUCTIONS[0x55] = "castore";
		INSTRUCTIONS[0x56] = "sastore";
		INSTRUCTIONS[0x57] = "pop";
		INSTRUCTIONS[0x58] = "pop2";
		INSTRUCTIONS[0x59] = "dup";
		INSTRUCTIONS[0x5a] = "dup_x1";
		INSTRUCTIONS[0x5b] = "dup_x2";
		INSTRUCTIONS[0x5c] = "dup2";
		INSTRUCTIONS[0x5d] = "dup2_x1";
		INSTRUCTIONS[0x5e] = "dup2_x2";
		INSTRUCTIONS[0x5f] = "swap";

		INSTRUCTIONS[0x60] = "iadd";
		INSTRUCTIONS[0x61] = "ladd";
		INSTRUCTIONS[0x62] = "fadd";
		INSTRUCTIONS[0x63] = "dadd";
		INSTRUCTIONS[0x64] = "isub";
		INSTRUCTIONS[0x65] = "lsub";
		INSTRUCTIONS[0x66] = "fsub";
		INSTRUCTIONS[0x67] = "dsub";
		INSTRUCTIONS[0x68] = "imul";
		INSTRUCTIONS[0x69] = "lmul";
		INSTRUCTIONS[0x6a] = "fmul";
		INSTRUCTIONS[0x6b] = "dmul";
		INSTRUCTIONS[0x6c] = "idiv";
		INSTRUCTIONS[0x6d] = "ldiv";
		INSTRUCTIONS[0x6e] = "fdiv";
		INSTRUCTIONS[0x6f] = "ddiv";

		INSTRUCTIONS[0x70] = "irem";
		INSTRUCTIONS[0x71] = "lrem";
		INSTRUCTIONS[0x72] = "frem";
		INSTRUCTIONS[0x73] = "drem";
		INSTRUCTIONS[0x74] = "ineg";
		INSTRUCTIONS[0x75] = "lneg";
		INSTRUCTIONS[0x76] = "fneg";
		INSTRUCTIONS[0x77] = "dneg";
		INSTRUCTIONS[0x78] = "ishl";
		INSTRUCTIONS[0x79] = "lshl";
		INSTRUCTIONS[0x7a] = "ishr";
		INSTRUCTIONS[0x7b] = "lshr";
		INSTRUCTIONS[0x7c] = "jushr";
		INSTRUCTIONS[0x7d] = "lushr";
		INSTRUCTIONS[0x7e] = "iand";
		INSTRUCTIONS[0x7f] = "land";

		INSTRUCTIONS[0x80] = "ior";
		INSTRUCTIONS[0x81] = "lor";
		INSTRUCTIONS[0x82] = "ixor";
		INSTRUCTIONS[0x83] = "lxor";
		INSTRUCTIONS[0x84] = "iinc";
		INSTRUCTIONS[0x85] = "i2l";
		INSTRUCTIONS[0x86] = "i2f";
		INSTRUCTIONS[0x87] = "i2d";
		INSTRUCTIONS[0x88] = "l2i";
		INSTRUCTIONS[0x89] = "l2f";
		INSTRUCTIONS[0x8a] = "l2d";
		INSTRUCTIONS[0x8b] = "f2i";
		INSTRUCTIONS[0x8c] = "f2l";
		INSTRUCTIONS[0x8d] = "f2d";
		INSTRUCTIONS[0x8e] = "d2i";
		INSTRUCTIONS[0x8f] = "d2l";

		INSTRUCTIONS[0x90] = "d2f";
		INSTRUCTIONS[0x91] = "i2b";
		INSTRUCTIONS[0x92] = "i2c";
		INSTRUCTIONS[0x93] = "i2s";
		INSTRUCTIONS[0x94] = "lcmp";
		INSTRUCTIONS[0x95] = "fcmpl";
		INSTRUCTIONS[0x96] = "fcmpg";
		INSTRUCTIONS[0x97] = "dcmpl";
		INSTRUCTIONS[0x98] = "dcmpg";
		INSTRUCTIONS[0x99] = "ifeq";
		INSTRUCTIONS[0x9a] = "ifne";
		INSTRUCTIONS[0x9b] = "iflt";
		INSTRUCTIONS[0x9c] = "ifge";
		INSTRUCTIONS[0x9d] = "ifgt";
		INSTRUCTIONS[0x9e] = "ifle";
		INSTRUCTIONS[0x9f] = "if_icmpeq";

		INSTRUCTIONS[0xa0] = "if_icmpne";
		INSTRUCTIONS[0xa1] = "if_icmplt";
		INSTRUCTIONS[0xa2] = "if_icmpge";
		INSTRUCTIONS[0xa3] = "if_icmpgt";
		INSTRUCTIONS[0xa4] = "if_icmple";
		INSTRUCTIONS[0xa5] = "if_acmpeq";
		INSTRUCTIONS[0xa6] = "if_acmpne";
		INSTRUCTIONS[0xa7] = "goto";
		INSTRUCTIONS[0xa8] = "jsr";
		INSTRUCTIONS[0xa9] = "ret";
		INSTRUCTIONS[0xaa] = "tableswitch";
		INSTRUCTIONS[0xab] = "lookupswitch";
		INSTRUCTIONS[0xac] = "ireturn";
		INSTRUCTIONS[0xad] = "lreturn";
		INSTRUCTIONS[0xae] = "freturn";
		INSTRUCTIONS[0xaf] = "dreturn";

		INSTRUCTIONS[0xb0] = "areturn";
		INSTRUCTIONS[0xb1] = "return";
		INSTRUCTIONS[0xb2] = "getstatic";
		INSTRUCTIONS[0xb3] = "putstatic";
		INSTRUCTIONS[0xb4] = "getfield";
		INSTRUCTIONS[0xb5] = "putfield";
		INSTRUCTIONS[0xb6] = "invokevirtual";
		INSTRUCTIONS[0xb7] = "invokespecial";
		INSTRUCTIONS[0xb8] = "invokestatic";
		INSTRUCTIONS[0xb9] = "invokeinterface";

		INSTRUCTIONS[0xba] = "invokedynamic"; // For historical reasons it wasn't in use. In Tiger (JSE5) it is assigned. BAH!

		INSTRUCTIONS[0xbb] = "new";
		INSTRUCTIONS[0xbc] = "newarray";
		INSTRUCTIONS[0xbd] = "anewarray";
		INSTRUCTIONS[0xbe] = "arraylength";
		INSTRUCTIONS[0xbf] = "athrow";

		INSTRUCTIONS[0xc0] = "checkcast";
		INSTRUCTIONS[0xc1] = "instanceof";
		INSTRUCTIONS[0xc2] = "monitorenter";
		INSTRUCTIONS[0xc3] = "monitorexit";
		INSTRUCTIONS[0xc4] = "wide";
		INSTRUCTIONS[0xc5] = "multianewarray";
		INSTRUCTIONS[0xc6] = "ifnull";
		INSTRUCTIONS[0xc7] = "ifnonnull";
		INSTRUCTIONS[0xc8] = "goto_w";
		INSTRUCTIONS[0xc9] = "jsr_w";
		INSTRUCTIONS[0xca] = "breakpoint";

		// the remaining is unused
		// ...

		// implementation depended instructions
		INSTRUCTIONS[0xfe] = "impdep1";
		INSTRUCTIONS[0xff] = "impdep2";
	}

	// EOF: Instruction table definition

	private int index;
	private int instruction;
	private int numberOfParams;
	private int params[];
	private int additionalDataLength;

	// optional info
	int paddingLength, defaultInt, lowInt, highInt, npairs;

	private static int counter = 0;
	private int offset;
	// To determine the instruction offset (ie. for lookupswitch)
	{
		offset = counter++;
	}

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 * Needs severe testing ;-)
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @throws IOException if an I/O error occurs.
	 */
	public CodeInstruction(DataInput di, int index) throws IOException {

		this.index  = index;
		instruction = di.readUnsignedByte();

		switch (instruction) {

			case (0x00) : // nop
			case (0x01) : // aconst_null
			case (0x02) : case (0x03) : case (0x04) : case (0x05) : case (0x06) : case (0x07) : case (0x08) : // iconst_<i>
			case (0x09) : case (0x0a) : // lconst_l
			case (0x0b) : case (0x0c) : case (0x0d) : // fconst_<f>
			case (0x0e) : case (0x0f) : // dconst_<d>
			case (0x1a) : case (0x1b) : case (0x1c) : case (0x1d) : // iload_<n>
			case (0x1e) : case (0x1f) : case (0x20) : case (0x21) : // lload_<n>
			case (0x22) : case (0x23) : case (0x24) : case (0x25) : // fload_<n>
			case (0x26) : case (0x27) : case (0x28) : case (0x29) : // dload_<n>
			case (0x2a) : case (0x2b) : case (0x2c) : case (0x2d) : // aload_<n>
			case (0x2e) : // iaload
			case (0x2f) : // laload
			case (0x30) : // faload
			case (0x31) : // daload
			case (0x32) : // aaload
			case (0x33) : // baload
			case (0x34) : // caload
			case (0x35) : // saload
			case (0x3b) : case (0x3c) : case (0x3d) : case (0x3e) : // istore_<n>
			case (0x3f) : case (0x40) : case (0x41) : case (0x42) : // lstore_<n>
			case (0x43) : case (0x44) : case (0x45) : case (0x46) : // fstore_<n>
			case (0x47) : case (0x48) : case (0x49) : case (0x4a) : // dstore_<n>
			case (0x4b) : case (0x4c) : case (0x4d) : case (0x4e) : // astore_<n>
			case (0x4f) : // iastore
			case (0x50) : // lastore
			case (0x51) : // fastore
			case (0x52) : // dastore
			case (0x53) : // aastore
			case (0x54) : // bastore
			case (0x55) : // castore
			case (0x56) : // sastore
			case (0x57) : // pop
			case (0x58) : // pop2
			case (0x59) : // dup
			case (0x5a) : // dup_x1
			case (0x5b) : // dup_x2
			case (0x5c) : // dup2
			case (0x5d) : // dup2_x1
			case (0x5e) : // dup2_x2
			case (0x5f) : // swap
			case (0x60) : // iadd
			case (0x61) : // ladd
			case (0x62) : // fadd
			case (0x63) : // dadd
			case (0x64) : // isub
			case (0x65) : // lsub
			case (0x66) : // fsub
			case (0x67) : // dsub
			case (0x68) : // imul
			case (0x69) : // lmul
			case (0x6a) : // fmul
			case (0x6b) : // dmul
			case (0x6c) : // idiv
			case (0x6d) : // ldiv
			case (0x6e) : // fdiv
			case (0x6f) : // ddiv
			case (0x70) : // irem
			case (0x71) : // lrem
			case (0x72) : // frem
			case (0x73) : // drem
			case (0x74) : // ineg
			case (0x75) : // lneg
			case (0x76) : // fneg
			case (0x77) : // dneg
			case (0x78) : // ishl
			case (0x79) : // lshl
			case (0x7a) : // ishr
			case (0x7b) : // lshr
			case (0x7c) : // iushr
			case (0x7d) : // lushr
			case (0x7e) : // iand
			case (0x7f) : // land
			case (0x80) : // ior
			case (0x81) : // lor
			case (0x82) : // ixor
			case (0x83) : // lxor
			case (0x85) : // i2l
			case (0x86) : // i2f
			case (0x87) : // i2d
			case (0x88) : // l2i
			case (0x89) : // l2f
			case (0x8a) : // l2d
			case (0x8b) : // f2i
			case (0x8c) : // f2l
			case (0x8d) : // f2d
			case (0x8e) : // d2i
			case (0x8f) : // d2l
			case (0x90) : // d2f
			case (0x91) : // i2b
			case (0x92) : // i2c
			case (0x93) : // i2s
			case (0x94) : // lcmp
			case (0x95) : case (0x96) : // fcmp<op>
			case (0x97) : case (0x98) : // dcmp<op>
			case (0xac) : // ireturn
			case (0xad) : // lreturn
			case (0xae) : // freturn
			case (0xaf) : // dreturn
			case (0xb0) : // areturn
			case (0xb1) : // return
			case (0xbe) : // arraylength
			case (0xbf) : // athrow
			case (0xc2) : // monitorenter
			case (0xc3) : // monitorexit
				numberOfParams = 0;
				additionalDataLength = 0;
				break;

			case (0x10) : // bipush
			case (0x12) : // ldc
			case (0x15) : // iload
			case (0x16) : // llong
			case (0x17) : // fload
			case (0x18) : // dload
			case (0x19) : // aload
			case (0x36) : // istore
			case (0x37) : // lstore
			case (0x38) : // fstore
			case (0x39) : // dstore
			case (0x3a) : // astore
			case (0xa9) : // ret
			case (0xbc) : // newarray
				numberOfParams = 1;
				params = new int[numberOfParams];
				params[0] = di.readUnsignedByte(); // index, byte, atype
				additionalDataLength = 1;
				break;

			case (0x11) : // sipush
			case (0x13) : // ldc_w
			case (0x14) : // ldc2_w
			case (0x99) : case (0x9a) : case (0x9b) : case (0x9c) : case (0x9d) : case (0x9e) : // if<cond>
			case (0x9f) : case (0xa0) : case (0xa1) : case (0xa2) : case (0xa3) : case (0xa4) : // if_icmp<cond>
			case (0xa5) : case (0xa6) : // if_acmp<cond>
			case (0xa7) : // goto
			case (0xa8) : // jsr
			case (0xb2) : // getstatic
			case (0xb3) : // putstatic
			case (0xb4) : // getfield
			case (0xb5) : // putfield
			case (0xb6) : // invokevirtual
			case (0xb7) : // invokespecial
			case (0xb8) : // invokestatic
			case (0xba) : // invokedynamic
			case (0xbb) : // new
			case (0xbd) : // anewarray
			case (0xc0) : // checkcast
			case (0xc1) : // instanceof
			case (0xc6) : // ifnull
			case (0xc7) : // ifnonnull
				numberOfParams = 1;
				params = new int[numberOfParams];
				params[0] = di.readUnsignedShort(); // index, branch, byte
				additionalDataLength = 2;
				break;

			case (0x84) : // iinc
				numberOfParams = 2;
				params = new int[numberOfParams];
				params[0] = di.readUnsignedByte(); // index
				params[1] = di.readUnsignedByte(); // const
				additionalDataLength = 2;
				break;

			// TODO: TEST !!! SERIOUS !!!
			case (0xaa) : // tableswitch
			{
				// Something nasty happens here with this index+1 ...
				paddingLength = (index+1) % 4;

				if (paddingLength > 0) {
					paddingLength = 4-paddingLength;
				}

				// TODO: correct exception-handling ... :-)
				if ( paddingLength != di.skipBytes(paddingLength) ) {
					System.err.println("CodeInstruction [aa]--- eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeror!");
					System.exit(-1);
				}

				defaultInt = di.readInt();
				lowInt  = di.readInt();
				highInt = di.readInt();

				if (highInt - lowInt + 1 > 0) {
					numberOfParams = highInt - lowInt + 1;
					params = new int[numberOfParams];

					for (int i=0; i<numberOfParams; ++i) {
						params[i] = di.readInt();
					}
				}

				additionalDataLength = paddingLength + 12 + 4*numberOfParams;
				break;
			}

			// SEVERE: need testing !!!
			case (0xab) : // lookupswitch
				paddingLength = offset % 4;
				// TODO: correct exception-handling ... :-)
				if ( paddingLength != di.skipBytes(paddingLength) ) {
					System.err.println("CodeInstruction [ab]--- eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeror!");
					System.exit(-1);
				}

				defaultInt = di.readInt();
				npairs = di.readInt();
				numberOfParams = npairs * 2;

				params = new int[numberOfParams];
				this.defaultInt = defaultInt;
				this.npairs = npairs;

				for (int i=0; i < params.length; i += 2) {
					int match   = di.readInt();
					int offset  = di.readInt();
					params[i]   = match;
					params[i+1] = offset;
				}
										//padding + (defaultByte + npairs) + (match+offset)*npairs
				additionalDataLength = paddingLength + 8 + 8*npairs;
				break;

			case (0xb9) : // invokeinterface
				numberOfParams = 3;
				params = new int[numberOfParams];
				params[0] = di.readUnsignedShort(); // index
				params[1] = di.readUnsignedByte();  // count
				params[2] = di.readUnsignedByte();  // 0 :-)
				additionalDataLength = 4;
				break;

			case (0xc4) : // wide
				int opcode = di.readUnsignedByte();
				if (0x84 == opcode) { // iinc
					numberOfParams = 3;
					params = new int[numberOfParams];
					params[0] = opcode;
					params[1] = di.readUnsignedShort(); // index
					params[2] = di.readUnsignedShort(); // const
					additionalDataLength = 5;
				} else { // Xload, Xstore or ret
					numberOfParams = 2;
					params = new int[numberOfParams];
					params[0] = opcode;
					params[1] = di.readUnsignedShort(); // index
					additionalDataLength = 3;
				}
				break;

			case (0xc5) : // multianewarray
				numberOfParams = 2;
				params = new int[numberOfParams];
				params[0] = di.readUnsignedShort(); // index
				params[1] = di.readUnsignedByte();  // dimensions
				additionalDataLength = 3;
				break;

			case (0xc8) : // goto_w
			case (0xc9) : // jsr_w
				numberOfParams = 1;
				params = new int[numberOfParams];
				params[0] = di.readInt(); // branch
				additionalDataLength = 4;
				break;
		}

	}// CodeInstruction

	/** Returns the additional data lenght. */
	public int getAdditionalDataLength() {
		return additionalDataLength;
	}

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append( "[").append(index).append("] ").append( INSTRUCTIONS[instruction] );

		if (params != null && 0xaa != instruction && 0xab != instruction ) {
			for (int param : params) {
				sb.append(" #").append(param);
			}
		}

		if (0xaa == instruction) {

			sb.append(" { // Padding Length: ").append(paddingLength);
			sb.append(", from ").append(lowInt);
			sb.append(" to ").append(highInt).append("<BR>");

			for (int i=0; i<params.length; ++i) {
				sb.append("&nbsp &nbsp &nbsp &nbsp ").append(i).append(" : ").append( params[i] ).append("<BR>");
			}

			sb.append("&nbsp &nbsp &nbsp &nbsp default: ").append(defaultInt).append(" }");
			return;

		} else if (0xab == instruction) {
			sb.append(" // PaddingLength: ").append(paddingLength);
			sb.append(" DefaultInt: ").append(defaultInt);
			sb.append(" NPairs: ").append(npairs);

			if (params != null) {
				for (int i=0; i < params.length; i += 2) {
					sb.append("<BR>");
					sb.append( "Match:  ").append( params[i] );
					sb.append( "Offset: ").append( params[i+1] );
				}
			}

			return;
		}

	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append( "[" + index + "] " + INSTRUCTIONS[instruction] );
		if (params != null && 0xab != instruction ) {
			for (int param : params) {
				sb.append(" #" + param);
			}
		}

		if (0xaa == instruction) {

			sb.append(" // PaddingLength: ").append(paddingLength);
			sb.append(" DefaultInt: ").append(defaultInt);
			sb.append(" LowInt: ").append(lowInt);
			sb.append(" HighInt: ").append(highInt);

		} else if (0xab == instruction) {

			sb.append(" // PaddingLength: ").append(paddingLength);
			sb.append(", DefaultInt: ").append(defaultInt);
			sb.append(", NPairs: ").append(npairs).append('\n');

			if (params != null) {
				for (int i=0; i < params.length; i += 2) {
					sb.append( "Match:  ").append( params[i] );
					sb.append( "Offset: ").append( params[i+1] );
				}
			}

			return sb.toString();
		}

		return sb.toString();
	}

}// class.Code
