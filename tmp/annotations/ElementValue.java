package jyzer.items.attributes.helpers;

import java.io.DataInputStream;
import java.io.IOException;

public final class ElementValue {

	// u2
	private int elementNameIndex;
	private ElementValue element;

	public ElementValue(DataInputStream dis) throws IOException {
		codeLength = dis.readInt();
		element = new ElementValue(dis);
	}

}
