package jyzer.items.attributes.helpers;

import java.io.DataInputStream;
import java.io.IOException;

public final class ElementValuePair {

	// u2
	private int elementNameIndex;
	private ElementValue element;

	public ElementValuePair(DataInputStream dis) throws IOException {
		codeLength = dis.readInt();
		element = new ElementValue(dis);
	}

}
