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

/* MODLOG:
 * -------------------------------------------------------------------------
 * 1.00 Initial release.
 *
 * 1.01 setClassFile	2005 nov 4.
 *			I wasn't able to stomach the header string creation any longer. It
 *			was so irritating to have more spaces between the keywords! So a new
 *			StringBuilder was used to create the string and then a regexp is used
 *			to clear those nasty whitespaces!
 */

package jyzer.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import jyzer.ClassFile;
import jyzer.items.attributes.types.helpers.CodeInstruction;
import jyzer.items.attributes.types.helpers.InnerClass;
import jyzer.items.fields.FieldInfo;
import jyzer.items.methods.MethodInfo;
import jyzer.gui.guihelpers.*;
import jyzer.JYzer;

/**
 * This class cares for the disassemble pane. Creates and displays the disassembled source. I've a lot of work in it,
 * but don't know if it works perfectly. I've tested it on ower 50+ classfiles, and given a good output. So I hope it
 * works fine.
 *
 * <p>TODO: make the syntax highlighting work.</p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class DisassemblePane extends JComponent {

	// --- Own variables -----------------------------------------

	private int lineNumber = 0;
	private RelatedClasses relatedClasses = null;
	private String inset = "";
	private static final String INCREASE = "    ";

	private boolean showBridge = true;
	private boolean showSynthetic = true;
	private boolean showDisassembledCode = true;
	private boolean showAnonymusInnerClasses = true;

	/** Had a little workaround to prevent the flickering of the screen. */
	private StringBuilder content = new StringBuilder();

	// --- Constructors ------------------------------------------

	/**
	 * Creates a new instance of DisassemblePane with nothing on it.
	 */
	public DisassemblePane() {
		buildUpGUI();
		setDefaultValues();
	}

	// --- GUI building ------------------------------------------

	/**
	 * Building up the core GUI.
	 */
	private void buildUpGUI() {
		setLayout( new BorderLayout() );

		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(25);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(25);
		add(scrollPane);

		mainPanel.setLayout( new BorderLayout() );
		mainPanel.add(ta);
		mainPanel.add(lineLabel, BorderLayout.WEST);

/*
		// Syntax highlighting 'll like something like this ...
	    JavaEditorKit kit = new JavaEditorKit();
	    ta.setEditorKitForContentType("text/java", kit);
	    ta.setContentType("text/java");
	    ta.setBackground(Color.white);
	    ta.setFont(new Font("Courier", 0, 12));
*/

	}// buildUpGUI

	/**
	 * Setting the default values of the GUI components.
	 */
	private void setDefaultValues() {
		Font font = new Font("monospaced", Font.PLAIN, 12);
		ta.setFont(font);
		ta.setEditable(false);

		lineLabel.setEditable(false);
		lineLabel.setFont(font);

		lineLabel.setBorder( BorderFactory.createEmptyBorder(0, 3, 0, 3) );
		lineLabel.setBackground( new Color(204, 204, 255) );
	}// setDefaultValues

	// --- Helper methods -------------------------------------------------

	/**
	 * Clears the displayed data on the pane.
	 */
	public void clear() {
		inset = "";
		ta.setText("");
		lineLabel.setText("0");
		lineNumber = 0;
	}

	/**
	 * Increases the actual insets with the constant defined in the header (by default it equals to 4 space).
	 */
	private void increaseInset() {
		inset += INCREASE;
	}

	/**
	 * Decreases the actual insets with the constant defined in the header (by default it equals to 4 space).
	 */
	private void decreaseInset() {
		inset = inset.substring( 0, inset.length() - 4 );
	}

	/**
	 * Returns the disassembled source.
	 */
	public String getText() {
		return ta.getText();
	}

	/**
	 * Adds a new line to the end of the content & the line label.
	 */
	private void newLine() {
		content.append('\n');
		lineLabel.append( "" + ++lineNumber + '\n');
	}

	/**
	 * Appends the given string to the end of the contents.
	 *
	 * @param str the string that has to be appended.
	 */
	private void append(String str) {
		content.append(str);
	}

	/**
	 * Appends the given string to the end of the contents, and terminates the actual line.
	 *
	 * @param str the string that has to be appended.
	 */
	private void addLine(String line) {
		content.append(inset + line + '\n');
		lineLabel.append( "" + ++lineNumber + '\n');
	}

	/**
	 * Truncates the given field's name to just the name, and drops away the remaining (the package name, ...)
	 *
	 * @param fieldDescriptor the descriptor of the field.
	 * @return the truncated name.
	 */
	private String getDescriptorString(String fieldDescriptor) {
		if ( fieldDescriptor.contains(".") ) { // class descriptor
			return fieldDescriptor.substring( fieldDescriptor.lastIndexOf(".") + 1 );
		}

		return fieldDescriptor;
	}// getDescriptorString

	/**
	 * This method makes the disassembling.
	 *
	 * @param cf the classfile that has to be disassemled.
	 */
	public void setClassFile( ClassFile cf ) {
		content = new StringBuilder();
		clear();
		lineLabel.setText("");
		relatedClasses = new RelatedClasses( cf.getThisClassName() );

		addLine("/*");
		addLine(" * This file was disassembled by JYzer v" + JYzer.VERSION);
		addLine(" * Copyright(C) 2005 Legendi Richard Oliver");
		addLine(" * Come to http://leriaat.web.elte.hu/ for more Java programs!");
		addLine(" *");
		addLine(" * This program is distributed in the hope that it will be useful,");
    	addLine(" * but WITHOUT ANY WARRANTY; without even the implied warranty of");
    	addLine(" * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
    	addLine(" * GNU General Public License for more details. ");
		addLine(" */");

		String thisClassName = cf.getThisClassName();

		//package ...
		if ( cf.getThisClassName().contains(".") ) {
			addLine("package " + cf.getThisClassName().substring( 0, cf.getThisClassName().lastIndexOf('.') ) + ";");
			thisClassName = cf.getThisClassName().substring( cf.getThisClassName().lastIndexOf('.') + 1);
		}

		addLine("");


		// import ...
		Vector<String> relatedClassNames = relatedClasses.getRelatedClassNames();
		for (int i=0; i<relatedClassNames.size(); ++i) {
			addLine("import " + relatedClassNames.get(i) + ";");
			if (relatedClassNames.size() == i+1) addLine("");
		}

		// class header
		// -- modifiers + classname
		StringBuilder header = new StringBuilder();;
		if ( cf.isInterface() ) {
			header.append(cf.getAccessString() + "interface " + thisClassName);
		} else {
			header.append(cf.getAccessString() + cf.getRealModifierString() + "class "+ thisClassName);
		}

		// -- super classname
		if ( ! cf.getSuperClassName().equals("java.lang.Object") ) {
			header.append( " extends " + getDescriptorString( cf.getSuperClassName() ) );
		}

		// -- interfaces (if any)
		if ( cf.hasInterfaces() ) {
			header.append(" implements ");
			String names[] = cf.getInterfaceNames();

			for ( int i=0; i<names.length; ++i) {
				header.append( getDescriptorString( names[i] ) + "");
				if (i+1 != names.length) header.append(", ");
			}
		}

		append( header.toString().replaceAll("\\s+"," ") );
		addLine(" {");
		addLine("");
		increaseInset();

		// fields
		FieldInfo fields[] = cf.getFieldItem().getFields();
		for (int i=0; i<fields.length; ++i) {
			String constantValue = "";
			String variableName  = getDescriptorString( fields[i].getDescriptorString() );

			if ( fields[i].hasConstantValue() ) {
				constantValue = " = " + fields[i].getConstantValueString();
			}

			if ( fields[i].hasSynthetic() ) {
				addLine("/* SYNTHETIC field - not present in the source");
			} else if ( variableName.contains("$") ) { // Inner class variable type (?)
				variableName = variableName.substring( variableName.lastIndexOf('$') + 1);
			}

			addLine( fields[i].getAccessString() + fields[i].getRealModifierString() +
					 variableName + " " + fields[i].getNameString() + constantValue + ";" );

			if ( fields[i].hasSynthetic() ) {
				addLine("*/");
			}

			if (fields.length == i+1) addLine("");
		}

		// inner classes
		if ( cf.hasInnerClasses() ) {
			InnerClass innerClasses[] = cf.getInnerClasses();

			for (int i=0; i<innerClasses.length; ++i) {
				String innerClassName = "";

				if ( innerClasses[i].isAnonymus() && showAnonymusInnerClasses ) {
					addLine("/* ANONYMUS inner class");
					innerClassName = innerClasses[i].getInnerClassInfo();
				} else if ( innerClasses[i].isAnonymus() && ! showAnonymusInnerClasses ) {
					continue;
				} else {
					innerClassName = innerClasses[i].getClassName();
				}

				addLine( innerClasses[i].getAccessString() + innerClasses[i].getRealModifierString() + "class "+ innerClassName + " {");
				addLine( "}" );

				if ( innerClasses[i].isAnonymus() && showAnonymusInnerClasses ) {
					addLine("*/");
				}

				addLine( ""  );

				/*
					No hope to make them work :-(
					// -- super classname
					// -- interfaces (if any)
					// ...
				 */

			}
		}

		// methods
		MethodInfo methods[] = cf.getMethodItem().getMethods();
		for (int i=0; i<methods.length; ++i) {

			if ( methods[i].isBridge() && showBridge ) {
				addLine("/* BRIDGE method - created by the compiler");
				addLine("");
			} else if ( methods[i].isSynthetic() && showSynthetic ) {
				addLine("/* SYNTHETIC method - not present in the source");
				addLine("");
			} else if ( ( methods[i].isBridge()    && ! showBridge ) ||
						( methods[i].isSynthetic() && ! showSynthetic) ) {
				continue;
			}

			if ( methods[i].isClassInitializer() ) {
				addLine("/**");
				addLine(" * Class initializer.");
				addLine(" */");
			}

			append( inset + "" + methods[i].getPureSignature(thisClassName) );
			if ( methods[i].hasExceptions() ) {
				append(" throws ");

				String exceptionNames[] = methods[i].getPureExceptionNames(thisClassName);
				for (int j=0; j<exceptionNames.length; ++j) {
					append( exceptionNames[j] );
					if (j+1<exceptionNames.length) append(", ");
				}
			}
			append(" {");
			newLine();

			increaseInset();

			// local variables
			if ( methods[i].hasTheCodeLocalVariables() ) {
				addLine("// Local variables");
				for ( String locVar : methods[i].getTheCodeLocalVariablesString() ) {
					addLine(locVar);
				}

				if (showDisassembledCode) {
					addLine("");
				}
			}

			// disassembled code
			if ( showDisassembledCode ) {
				if ( ! methods[i].isBridge() && ! methods[i].isSynthetic() ) {
					addLine("/*");
				}

				addLine("Disassembled Code:");

				Vector<CodeInstruction> code = methods[i].getDisassembledCode();
				for (CodeInstruction ci : code) {
					addLine( ci.toString() );
				}

				if ( ! methods[i].isBridge() && ! methods[i].isSynthetic() ) {
					addLine("*/");
				}
			}

			decreaseInset();

			addLine( "}" );
			addLine( " ");

			if ( methods[i].isBridge() || methods[i].isSynthetic() ) {
				addLine("*/");
			}
		}

		// end :-)
		decreaseInset();
		addLine("}");
		addLine("");

		ta.setText( content.toString() );
		ta.setCaretPosition(0);
	}// setClassFile

	// --- GUI variables --------------------------------------------------------------

	private JPanel mainPanel = new JPanel();
	private JEditorPane ta = new JEditorPane();
	private JTextArea lineLabel = new JTextArea("0");

}// class.DisassemblePane
