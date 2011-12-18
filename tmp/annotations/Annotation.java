package jyzer.items.attributes.helpers;

import java.io.DataInputStream;
import java.io.IOException;

public final class Annotation {

	// u2
	private int typeIndex, numElementValuePairs;
	private int code[];

	public Annotation(DataInputStream dis) throws IOException {
		codeLength = dis.readInt();

		for (int i=0; i<codeLength; ++i) {
			code[i] = dis.readUnsignedByte();
		}
	}

}
