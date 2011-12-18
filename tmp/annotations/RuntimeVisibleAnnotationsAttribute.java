package jyzer.items.attributes;

import java.io.DataInputStream;
import java.io.IOException;

import jyzer.items.attributes.helpers.InnerClass;

public final class RuntimeVisibleAnnotationsAttribute extends AttributeInfo {

	private int numAnnotations;
	private Annotation annotations[];

	public RuntimeVisibleAnnotationsAttribute(DataInputStream dis) throws IOException {
		super(dis);

		numAnnotations = dis.readUnsignedShort();
		annotations = new Annotation[numAnnotations];

		for (int i=0; i<numAnnotations; ++i) {
			annotations[i] = new Annotation(dis);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("*** RuntimeVisibleAnnotationsAttribute: ***\n");
		sb.append("Attribute Name Index: " + attributeNameIndex + '\n');
		sb.append("Attribute Length: " + attributeLength + '\n');

		return sb.toString();
	}

}
