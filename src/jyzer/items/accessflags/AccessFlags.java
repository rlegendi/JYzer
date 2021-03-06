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
package jyzer.items.accessflags;

import java.io.DataInput;
import java.io.IOException;

// import jyzer.items.exceptions.ValidationException;

/**
 * <p> This is the representation of an accessflag. Only classes, fields, methods and nested classes can have
 * accessflags. The accessflag is a 2 byte length hexa number, and its value is depending on which structure
 * has it. </p>
 *
 * <hr>
 *
 * <p> If you're not familiar with handling flags (oh yeah, the good ol' C times ... :-) ), here's a little
 * example:
 *
 * <p> Among other things, bitwise manipulations are useful for managing sets of boolean flags. Suppose, for example,
 * that your program had several boolean flags that indicated the state of various components in your program: is
 * it visible, is it draggable, and so on. Rather than define a separate boolean variable to hold each flag, you
 * could define a single variable, flags, for all of them. Each bit within flags would represent the current state
 * of one of the flags. You would then use bit manipulations to set and to get each flag. First, set up constants
 * that indicate the various flags for your program. These flags should each be a different power of 2 to ensure
 * that each bit is used by only one flag. Define a variable, flags, whose bits would be set according to the current
 * state of each flag. The following code sample initializes flags to 0, which means that all flags are false
 * (none of the bits are set).</p>
 *
 * <p><code>
 * static final int VISIBLE = 1; <br>
 * static final int DRAGGABLE = 2; <br>
 * static final int SELECTABLE = 4; <br>
 * static final int EDITABLE = 8; <br>
 * ... <br>
 * int flags = 0;
 * </code></p>
 *
 * <p> To set the "visible" flag when something became visible you would use this statement:</p>
 *
 * <p><code>
 * flags = flags | VISIBLE;
 * </code></p>
 *
 * <p> To test for visibility, you could then write: </p>
 * <p><code>
 * if ((flags & VISIBLE) == VISIBLE) { <br>
 *     ... <br>
 * }
 * </code></p>
 *
 * <hr>
 *
 * <b>About the <i>real</i> accessflags:</b><br>
 * I've separated the accessflags to 2 set: one is the set of the <i>real access flags</i>, and the other.
 * Real access flags contains access flags that are present in the sourcecode, the other ones are in the other set.
 *
 * <ul>
 * <li> <b>Real access flags are</b> public, private, protected, static, final, synchronized, varargs, volatile, native, interface,
 *		abstract, annotation, enum
 * <li> <b>Other access flags are</b> super, bridge, transient, strict, synthetic
 * </ul>
 *
 * @author Legendi Richard Oliver
 * @version 1.01, 2005 sept 27.
 */
public class AccessFlags {

	// --- Static constants ---------------------------------------------------------------------------

	/** Visibility is public. */
	public static final int ACC_PUBLIC = 0x0001;
	/** Visibility is private. */
	public static final int ACC_PRIVATE = 0x0002;
	/** Visibility is protected. */
	public static final int ACC_PROTECTED = 0x0004;

	/** Declared static. */
	public static final int ACC_STATIC = 0x008;
	/** Declared final. */
	public static final int ACC_FINAL = 0x0010;

	/** Treat superclass methods specially. */
	public static final int ACC_SUPER = 0x0020;
	/** Declared synchronized. */
	public static final int ACC_SYNCHRONIZED = 0x0020;

	/** Declared volatile. */
	public static final int ACC_VOLATILE = 0x0040;
	/** Generated by the compiler. */
	public static final int ACC_BRIDGE = 0x0040;

	/** Declared transient. */
	public static final int ACC_TRANSIENT = 0x0080;
	/** Declared with variable number of arguments. */
	public static final int ACC_VARARGS = 0x0080;

	/** Declared native. */
	public static final int ACC_NATIVE = 0x0100;
	/** Was an interface. */
	public static final int ACC_INTERFACE = 0x0200;
	/** Marked or implicitly abstract in code. */
	public static final int ACC_ABSTRACT = 0x0400;
	/** Declared strictfp. */
	public static final int ACC_STRICT = 0x0800;
	/** Declared synthetic, not present in the source code. */
	public static final int ACC_SYNTHETIC = 0x1000;
	/** Declared as an annotation type. */
	public static final int ACC_ANNOTATION = 0x2000;
	/** Declared as an enum type. */
	public static final int ACC_ENUM = 0x4000;

	/** Type definitions. */
	public enum Type { CLASS, FIELD, METHOD, NESTED_CLASS };

	// --- Structure -----------------------------------------------------------------------------

	// u2
	protected int accessFlags;
	private final Type type;

	// for fields ...
	private boolean isInterfaceField = false;

	// --- Constructors --------------------------------------------------------------------------

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param type the type of the accessflag.
	 * @throws IOException if an I/O error occurs.
	 */
	public AccessFlags(DataInput di, Type type) throws IOException {
		this.type   = type;
		accessFlags = di.readUnsignedShort();
	}

	/**
	 * This constructor reads the needed information from the given input, and creates the object.
	 * JUST FOR FIELDS !!!
	 *
	 * @param di an opened input (should be a <code>DataInputStream</code>, but can be exchanged
	 *			with any subclass).
	 * @param type the type of the accessflag.
	 * @param isInterfaceField true if it is an accessflag of a field in an interface; false otherwise.
	 * @throws IOException if an I/O error occurs.
	 */
	public AccessFlags(DataInput di, Type type, boolean isInterfaceField) throws IOException {
		this(di, type);

		if ( Type.FIELD != type ) {
			System.err.println("Wrong access flag constructor call!"); // doesn't really matters ...
			return;
		} else {
			this.isInterfaceField = isInterfaceField;
		}
	}

	// --- Getter methods -------------------------------------------------------------------------

	/**
	 * Returns a String describing the access privileges.
	 */
	public String getAccessString() {
		String back = null;

		switch (type) {
			case CLASS : back = getClassAccessString(); break;
			case FIELD : back = getFieldAccessString(); break;
			case METHOD : back =  getMethodAccessString(); break;
			case NESTED_CLASS : back = getNestedClassAccessString(); break;
		}

		return back;
	}

	/**
	 * Returns a String describing the modifiers.
	 */
	public String getModifierString() {
		String back = null;

		switch (type) {
			case CLASS : back = getClassModifierString(); break;
			case FIELD : back = getFieldModifierString(); break;
			case METHOD : back =  getMethodModifierString(); break;
			case NESTED_CLASS : back = getNestedClassModifierString(); break;
		}

		return back;
	}

	/**
	 * Returns the access flags as an integer (<i>should be converted to hex</i> to use it properly).
	 */
	public int getAccessInt() {
		return accessFlags;
	}

	/**
	 * Returns a String describing the real modifiers. See class description for more information
	 */
	public String getRealModifierString() {
		String back = getModifierString();

		back = back.replace("super", "").replace("synthetic", "").replace("bridge", "").replace("strict", "");

		if ( back.equals("") ) {
			return back;
		} else {
			back = back.trim();
			back += " ";
		}

		return back;
	}

	/**
	 * Returns a String describing the class access modifiers.
	 */
	private String getClassAccessString() {
		StringBuilder sb = new StringBuilder();

		if ( isPublic() ) {
			sb.append("public ");
		} else {
			return "";
		}

		return sb.toString();
	}

	/**
	 * Returns a String describing a field access modifiers.
	 */
	private String getFieldAccessString() {
		StringBuilder sb = new StringBuilder();

		if ( isPublic() ) {
			sb.append("public ");
		} else if ( isPrivate() ) {
			sb.append("private ");
		} else if ( isProtected() ) {
			sb.append("protected ");
		}

		return sb.toString();
	}

	/**
	 * Returns a String describing a method access modifiers.
	 */
	private String getMethodAccessString() {
		StringBuilder sb = new StringBuilder();

		if ( isPublic() ) {
			sb.append("public ");
		} else if ( isPrivate() ) {
			sb.append("private ");
		} else if ( isProtected() ) {
			sb.append("protected ");
		}

		return sb.toString();
	}

	/**
	 * Returns a String describing a nested class access modifiers.
	 */
	private String getNestedClassAccessString() {
		StringBuilder sb = new StringBuilder();

		if ( isPublic() ) {
			sb.append("public ");
		} else if ( isPrivate() ) {
			sb.append("private ");
		} else if ( isProtected() ) {
			sb.append("protected ");
		}

		return sb.toString();
	}

	/**
	 * Returns a String describing the class modifiers.
	 */
	private String getClassModifierString() {
		StringBuilder sb = new StringBuilder();

		if ( isFinal() )      sb.append("final ");
		if ( isSuper() )      sb.append("super ");
		if ( isInterface() )  sb.append("interface ");
		if ( isAbstract() )   sb.append("abstract ");
		if ( isSynthetic() )  sb.append("synthetic ");
		if ( isAnnotation() ) sb.append("annotation ");
		if ( isEnum() )       sb.append("enum ");

		return sb.toString().trim();
	}

	/**
	 * Returns a String describing a field's modifiers.
	 */
	private String getFieldModifierString() {
		StringBuilder sb = new StringBuilder();

		if ( isStatic() )    sb.append("static ");
		if ( isFinal() )     sb.append("final ");
		if ( isVolatile() )  sb.append("volatile ");
		if ( isTransient() ) sb.append("transient ");
		if ( isSynthetic() ) sb.append("synthetic ");
		if ( isEnum() )      sb.append("enum ");

		return sb.toString().trim();
	}

	/**
	 * Returns a String describing a method's modifiers.
	 */
	private String getMethodModifierString() {
		StringBuilder sb = new StringBuilder();

		if ( isStatic() )       sb.append("static ");
		if ( isFinal() )        sb.append("final ");
		if ( isSynchronized() ) sb.append("synchronized ");
		if ( isBridge() )       sb.append("bridge ");
		if ( isVarargs() )      sb.append("varargs ");
		if ( isNative() )       sb.append("native ");
		if ( isAbstract() )     sb.append("abstract ");
		if ( isStrict() )       sb.append("strict ");
		if ( isSynthetic() )    sb.append("synthetic ");

		return sb.toString().trim();
	}

	/**
	 * Returns a String describing a nested class' modifiers.
	 */
	private String getNestedClassModifierString() {
		StringBuilder sb = new StringBuilder();

		if ( isStatic() )     sb.append("static ");
		if ( isFinal() )      sb.append("final ");
		if ( isInterface() )  sb.append("interface ");
		if ( isAbstract() )   sb.append("abstract ");
		if ( isSynthetic() )  sb.append("synthetic ");
		if ( isAnnotation() ) sb.append("annotation ");
		if ( isEnum() )       sb.append("enum ");

		return sb.toString().trim();
	}

	// --- Checker methods ---------------------------------------------------

	/**
	 * Tests if the accessflag is an accessflag of a field in an interface.
	 */
	public boolean isInterfaceField() {
		return isInterfaceField;
	}

	/**
	 * Tests if the accessflag has the public flag set.
	 */
	public boolean isPublic() {
		return ( ACC_PUBLIC == ( ACC_PUBLIC & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the private flag set.
	 */
	public boolean isPrivate() {
		return ( ACC_PRIVATE == ( ACC_PRIVATE & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the protected flag set.
	 */
	public boolean isProtected() {
		return ( ACC_PROTECTED == ( ACC_PROTECTED & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the static flag set.
	 */
	public boolean isStatic() {
		return ( ACC_STATIC == ( ACC_STATIC & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the final flag set.
	 */
	public boolean isFinal() {
		return ( ACC_FINAL == ( ACC_FINAL & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the super flag set.
	 */
	public boolean isSuper() {
		return ( ACC_SUPER == (ACC_SUPER & accessFlags) );
	}

	/**
	 * Tests if the accessflag has the synchronized flag set.
	 */
	public boolean isSynchronized() {
		return ( ACC_SYNCHRONIZED == (ACC_SYNCHRONIZED & accessFlags) );
	}

	/**
	 * Tests if the accessflag has the volatile flag set.
	 */
	public boolean isVolatile() {
		return ( ACC_VOLATILE == (ACC_VOLATILE & accessFlags) );
	}

	/**
	 * Tests if the accessflag has the bridge flag set.
	 */
	public boolean isBridge() {
		return ( ACC_BRIDGE == (ACC_BRIDGE & accessFlags) );
	}

	/**
	 * Tests if the accessflag has the transient flag set.
	 */
	public boolean isTransient() {
		return ( ACC_TRANSIENT == (ACC_TRANSIENT & accessFlags) );
	}

	/**
	 * Tests if the accessflag has the varargs flag set.
	 */
	public boolean isVarargs() {
		return ( ACC_VARARGS == (ACC_VARARGS & accessFlags) );
	}

	/**
	 * Tests if the accessflag has the native flag set.
	 */
	public boolean isNative() {
		return ( ACC_NATIVE == ( ACC_NATIVE & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the interface flag set.
	 */
	public boolean isInterface() {
		return ( ACC_INTERFACE == ( ACC_INTERFACE & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the abstract flag set.
	 */
	public boolean isAbstract() {
		return ( ACC_ABSTRACT == ( ACC_ABSTRACT & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the strict flag set.
	 */
	public boolean isStrict() {
		return ( ACC_STRICT == ( ACC_STRICT & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the synthetic flag set.
	 */
	public boolean isSynthetic() {
		return ( ACC_SYNTHETIC == ( ACC_SYNTHETIC & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the annotation flag set.
	 */
	public boolean isAnnotation() {
		return ( ACC_ANNOTATION == ( ACC_ANNOTATION & accessFlags ) );
	}

	/**
	 * Tests if the accessflag has the enum flag set.
	 */
	public boolean isEnum() {
		return ( ACC_ENUM == ( ACC_ENUM & accessFlags ) );
	}

	// --- Validation methods ----------------------------

	/* // DON'T WORRY, I'LL FINISH IT :-)

	private void classValidation() throws ValidationException {
		if ( isInterface() && ! isAbstract() && isFinal() && isSuper() && isSynthetic() && isAnnotation() && isEnum() ) {
			throw new ValidationException("ClassAccessFlags", "An interface must be abstract, and can have no other flags (except public)!");
		}

		if ( isAnnotation() && ! isInterface() ) {
			throw new ValidationException("ClassAccessFlags", "An annotation class must have the interface flag set!");
		}

		if ( ! isInterface() && isAnnotation() ) {
			throw new ValidationException("ClassAccessFlags", "A non-interface cannot have its annotation flag set!");
		}

		if ( isAbstract() && isFinal() ) {
			throw new ValidationException("ClassAccessFlags", "A class cannot have both of its abstract and final flags set!");
		}

	}

	private void fieldValidation() throws ValidationException {

		if ( ( isPublic() && isPrivate() ) ||
			 ( isPublic() && isProtected() ) ||
			 ( isPrivate() && isProtected() ) ) {

			throw new ValidationException("FieldAccessFlags", "A field can have only one access privilege!");

		}

		if ( isFinal() && isVolatile() ) {
			throw new ValidationException("FieldAccessFlags", "A field cannot have both of its final and volatile flags set!");
		}

		if ( isInterfaceField() && ( ! isPublic() || ! isStatic() || ! isFinal() ) && ( isVolatile() || isTransient() || isEnum() ) ) {
			throw new ValidationException("FieldAccessFlags", "A field of an interface must have its public, static and final flags set, " +
					"and cannot have any other flags (except synthetic).");
		}

	}

	private void methodValidation() throws ValidationException {
	}

	private void nestedClassValidation() throws ValidationException {
	}

	public void validate() throws ValidationException {
		switch (type) {
			case CLASS : classValidation(); break;
			case FIELD : fieldValidation(); break;
			case METHOD : methodValidation(); break;
			case NESTED_CLASS : nestedClassValidation(); break;
		}
	}

	*/

	/**
	 * Concatenates a HTML string that represents this class ( looks cool! ) to the end of
	 * the given <code>StringBiulder</code> object. Using this method for improve efficiency,
	 * cause I had a lot of problems with displaying such a long HTML text in an <code>EditorPane</code>.
	 *
	 * @param sb a <code>StringBuilder<code> that contains the string representation of the full classfile.
	 */
	public void getHTMLDescription(StringBuilder sb) {
		sb.append("0x").append(Integer.toHexString(accessFlags));
		sb.append(" <FONT color=\"blue\">// ");
		sb.append( ( (0x00 == accessFlags) ? "N/A" : getAccessString().toUpperCase() + getModifierString() ) );
		sb.append("</FONT>" );
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ("0x" + Integer.toHexString(accessFlags) + " // " +
			   ( (0x00 == accessFlags) ? "N/A" : getAccessString().toUpperCase() + getModifierString() ) );
	}

}// class.AccessFlags
