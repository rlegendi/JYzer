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
package jyzer;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;

import jyzer.items.*;
import jyzer.items.accessflags.AccessFlags;
import jyzer.items.attributes.Attributes;
import jyzer.items.attributes.types.helpers.InnerClass;
import jyzer.items.constantpool.ConstantPoolInfo;
import jyzer.items.fields.FieldInfo;
import jyzer.items.methods.MethodInfo;
import jyzer.items.exceptions.*;

/**
 * This class represents a classfile object. The definition of the classfile structure is:
 * <p><code>
 * ClassFile:
 * <ul>
 * <li> u4 magic
 * <li> u2 minor_verion
 * <li> u2 major_version
 * <li> u2 constant_pool_count
 * <li> cp_info constant_pool[constant_pool_count-1]
 * <li> u2 access flags
 * <li> u2 this class
 * <li> u2 super class
 * <li> u2 interfaces_count
 * <li> u2 interfaces[interfaces_count]
 * <li> u2 fields_count
 * <li> field_info fields[fields_count]
 * <li> u2 methods_count
 * <li> methdo_info methods[methods_count]
 * <li> u2 attributes_count
 * <li> attribute_info attributes[attributes_count]
 * </ul>
 * </code></p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005.07.30
 */
public final class ClassFile {

	// --- Variables -----------------------------------------------------------------------------------------------------------

	// static constans

	/** The magic number that has to be the first 4 bytes of a classfile. */
	public static final int   MAGIC = 0xCAFEBABE;

	/** The maximum major version that the program works properly with. */
	public static final short MAX_MAJOR_VERSION = 49;
	/** The maximum minor version that the program works properly with. */
	public static final short MAX_MINOR_VERSION = 0;

	// class variables
	private int magic;
	private short minorVersion;
	private short majorVersion;
	private ConstantPool constantPool;
	private AccessFlags accessFlags;
	private ClassObject thisClass;
	private ClassObject superClass;
	private Interfaces interfaces;
	private Fields fields;
	private Methods methods;
	private Attributes classAttributes;

	// own variables
	private JFrame owner;
	private String fileName = null;
	private DataInputStream dis;

	// --- Constructors ---------------------------------------------------------------------------------------------------------

	/**
	 * Constructor, creates a new instance of ClassFile.
	 *
	 * @param fileName the path of the classfile.
	 */
	public ClassFile(String fileName) {
		this.fileName = fileName;
		owner = null;
	}// constr:ClassFile(String)

	/**
	 * Constructor, creates a new instance of ClassFile. The owner has to be known for the <code>ProgressMonitorInputStream</code>
	 * for processing the classfile.
	 *
	 * @param fileName the path of the classfile.
	 * @param owner a JFrame that uses the classfile.
	 */
	public ClassFile(String fileName, JFrame owner) {
		this.fileName = fileName;
		this.owner    = owner;
	}// constr:ClassFile(String, JFrame)

	// --- Getter methods ---------------------------------------------------------------------------------------------------------

	/**
	 * Returns if the classfile is an interface.
	 */
	public boolean isInterface() {
		return accessFlags.isInterface();
	}

	/**
	 * Returns if the classfile has any interface.
	 */
	public boolean hasInterfaces() {
		return ( ! interfaces.isEmpty() );
	}

	/**
	 * Returns if the classfile has any inner classes.
	 */
	public boolean hasInnerClasses() {
		return classAttributes.hasInnerClasses();
	}

	/**
	 * Returns the first 4 bytes that is read from the target file.
	 */
	public int getMagic() {
		return magic;
	}

	/**
	 * Returns the minor version of the compiler that was used to create the classfile.
	 */
	public short getMinorVersion() {
		return minorVersion;
	}

	/**
	 * Returns the major version of the compiler that was used to create the classfile.
	 */
	public short getMajorVersion() {
		return majorVersion;
	}

	/**
	 * Returns the accessflags of this classfiles.
	 */
	public String getAccessString() {
		return accessFlags.getAccessString();
	}

	/**
	 * Returns the real modifiers of the class.
	 */
	public String getRealModifierString() {
		return accessFlags.getRealModifierString();
	}

	/**
	 * Returns the name of the class.
	 */
	public String getThisClassName() {
		return thisClass.getClassName();
	}

	/**
	 * Returns the name of the superclass.
	 */
	public String getSuperClassName() {
		return superClass.getClassName();
	}

	/**
	 * Returns the number of the entries in the constantpool.
	 */
	public int getConstantPoolCount() {
		return constantPool.getConstantPoolCount();
	}

	/**
	 * Returns the <code>ConstantPool</code> of the class.
	 */
	public ConstantPool getConstantPoolItem() {
		return constantPool;
	}

	/**
	 * Returns an array that contains the names of the interfaces that are implemented by the analyzed class.
	 */
	public String[] getInterfaceNames() {
		return interfaces.getInterfaceNames();
	}

	/**
	 * Returns the <code>Fields</code> of the class.
	 */
	public Fields getFieldItem() {
		return fields;
	}

	/**
	 * Returns the <code>InnerClass</code>es of the class in an array.
	 */
	public InnerClass[] getInnerClasses() {
		return classAttributes.getInnerClasses();
	}

	/**
	 * Returns the <code>Methods</code> of the class.
	 */
	public Methods getMethodItem() {
		return methods;
	}

	/**
	 * Validation. Not implemented perfectly yet.
	 */
	 /*
	public void validate() throws ValidationException {
		accessFlags.validate();
	}
	*/

	// --- Functions --------------------------------------------------------------------------------------------------------

	/**
	 * The parsing of the classfile. Makes the requied tests, and notifies if not a classfile was opened, or if
	 * its version number is above the suggested maximum.
	 *
	 * @throws IOException if problem occured with processing the file.
	 * @throws ParsingException if the given file is not a real classfile.
	 */
	public void parse() throws IOException, ParsingException {
		try {

			if ( null == owner ) {
				dis = new DataInputStream ( new FileInputStream(fileName) );
			} else {
				dis = new DataInputStream (
					new ProgressMonitorInputStream(
						owner, "Reading file: " + fileName, new FileInputStream(fileName)
					)
				);
			}

			magic = dis.readInt();

			if ( MAGIC != magic) {
				throw new ParsingException("ClassFile", "Not a classfile!\nThe magic number must be equal to 0xCAFEBABE!");
			}

			minorVersion = dis.readShort();
			majorVersion = dis.readShort();

			if ( MAX_MAJOR_VERSION < majorVersion || ( MAX_MAJOR_VERSION == majorVersion && MAX_MINOR_VERSION < minorVersion) ) {
				int back = JOptionPane.showConfirmDialog(
							owner,
							"Not a supported classfile version (" + majorVersion + "." + minorVersion + ")!\n" +
							"There could be some errors, proceed although?",
							"Warning:",
							JOptionPane.YES_NO_OPTION
						   );
				if ( JOptionPane.YES_OPTION != back ) {
					throw new ParsingException(
						"ClassFile",
						"User interruption.\nCause: unsupported classfile version (" + majorVersion + "." + minorVersion + ")."
					);
				}
			}

			constantPool = new ConstantPool(dis);
			accessFlags  = new AccessFlags(dis, AccessFlags.Type.CLASS);
			thisClass    = new ClassObject(dis);
			superClass   = new ClassObject(dis);
			interfaces   = new Interfaces(dis);

			fields  = new Fields(dis, accessFlags.isInterface() );
			methods = new Methods(dis);
			classAttributes = new Attributes(dis);
		} finally {
			if ( dis != null) {
				try { dis.close(); } catch (IOException ioe) { System.err.println( ioe.getMessage() ); }
			}
		}

	}// parse

	/**
	 * Returns the description of the classfile as a html text. It is used to display on the Summary pane, that can be saved.
	 * Calls recursively the <code>getHTMLDescription</code> of the components.
	 *
	 * @return the description of the classfile in <code>HTML</code>.
	 */
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();

		sb.append("<html>");
		sb.append("<body>");

		sb.append("<center>");
		sb.append("<H1>").append( fileName.substring( fileName.lastIndexOf(File.separator) + 1, fileName.length() ) ).append("</H1>");

		sb.append("<H2>Summary:</H2>");
		sb.append("<table border=\"1\" width=\"90%\">");

		sb.append("<tr>");
		sb.append("<td align=\"center\" bgcolor=\"green\">");
		sb.append("General Info\n");
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");

		sb.append("<ul>");
		sb.append("<li>Magic Number: 0x").append( Integer.toHexString(magic).toUpperCase() ).append('\n');
		sb.append("<li>Major Classfile Version: ").append(majorVersion).append('\n');
		sb.append("<li>Minor Classfile Version: ").append(minorVersion).append('\n');
		sb.append("<li>This class: ");
		thisClass.getHTMLDescription(sb);
		sb.append("<li>Super class: ");
		superClass.getHTMLDescription(sb);
		sb.append("<li>Access flags: ");
		accessFlags.getHTMLDescription(sb);
		sb.append("</ul>");

		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td align=\"center\" bgcolor=\"green\">");
		sb.append("Class Attributes").append('\n');
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");
		classAttributes.getHTMLDescription(sb);
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<A name=\"CONST_POOL\">");
		sb.append("<tr>");
		sb.append("<td align=\"center\" bgcolor=\"green\">");
		sb.append("<A name=\"CONST_POOL\">");
		sb.append("Constant Pool:").append('\n');
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");
		constantPool.getHTMLDescription(sb);
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td align=\"center\" bgcolor=\"green\">");
		sb.append("Interfaces").append('\n');
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");
		interfaces.getHTMLDescription(sb);
		sb.append("</td>");
		sb.append("</tr>");


		sb.append("<tr>");
		sb.append("<td align=\"center\" bgcolor=\"green\">");
		sb.append("Fields").append('\n');
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");
		fields.getHTMLDescription(sb);
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td align=\"center\" bgcolor=\"green\">");
		sb.append("Methods").append('\n');
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>");
		methods.getHTMLDescription(sb);
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("</table>");
		sb.append("</center>");

		sb.append("</body>");
		sb.append("</html>");

		return sb.toString();
	}// getHTMLDescription

}// class.ClassFile
