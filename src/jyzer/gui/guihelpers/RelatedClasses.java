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
package jyzer.gui.guihelpers;

import java.util.Vector;

import jyzer.items.ConstantPool;

/**
 * This is a container object that gahters releated classnames, and stores them. Stores <u>only</u> the classname,
 * it truncates the package name if it is visible by default (it is in the java.lang package, or the class is in
 * the same package where the classfile is).
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class RelatedClasses {

	// --- Variables -----------------------------------------------------------------------------------------------------

	private Vector<String> releatedClasses = new Vector<String>();

	// --- Constructors --------------------------------------------------------------------------------------------------

	public RelatedClasses(String thisClassName) {
		for (int i=0; i<ConstantPool.getConstantPoolCount(); ++i) {
			if ( ConstantPool.getElement(i).isClassInfo() ) {
				String className = ConstantPool.getClassName(i);

				if ( ! className.equals(thisClassName) && // own class
					 ! isDefaultlyVisible(className) && // visible by default
					 ! className.contains("$") && // synthetic --- classfiles created by the compiler
					 ! areInSamePackage(thisClassName, className) &&
					 className.contains(".") /* if not an inner-class */ ) {
					releatedClasses.add(className);
				}
			}
		}

		sort();
	}

	// --- Own methods ---------------------------------------------------------------------------------------------------

	/**
	 * Determines if the class is defaulty visible (ie. is in the java.lang package).
	 *
	 * @param the descriptor of the classname.
	 * @return true, if the class is defaulty visible.
	 */
	private boolean isDefaultlyVisible(String descriptor) {
		if ( descriptor.contains("java.lang.") ) {
			return ( ! descriptor.substring( descriptor.indexOf("java.lang.") + ("java.lang.").length() ).contains(".") );
		}

		return false;
	}

	/**
	 * Determines if the given classname is in the same package where the classfile is.
	 *
	 * @param the descriptor of the classname.
	 * @return true, if the class is defaulty visible.
	 */
	private boolean areInSamePackage(String name1, String name2) {
		if ( ( ! name1.contains(".") && name2.contains(".") ) ||
			 ( name1.contains(".") && ! name2.contains(".") ) ) {
			return false;
		}

		if ( ! name1.contains(".") && ! name2.contains(".") ) {
			return true;
		}

		String pack1 = name1.substring(0, name1.lastIndexOf('.') );
		String pack2 = name2.substring(0, name2.lastIndexOf('.') );

		return pack1.equals(pack2);
	}// areInSamePackage

	/**
	 * Returns the vector containing the names of the releated classes.
	 */
	public Vector<String> getRelatedClassNames() {
		return releatedClasses;
	}

	/**
	 * Returns the name of the package of the element at the given index.
	 *
	 * @param i the index.
	 * @return the package name.
	 */
	private String getPackageName(int i) {
		return releatedClasses.get(i).substring(0, releatedClasses.get(i).lastIndexOf('.') );
	}

	/**
	 * Sorting the releated classnames lexicographically.
	 */
	private void sort() {
		Vector<String> tmp = new Vector<String>();

		for (int i=0; i<releatedClasses.size(); ++i) {
			String  pName1  = getPackageName( i );
			boolean wasSame = false;

			for (int j=(i+1); j<releatedClasses.size(); ++j) {
				String pName2 = getPackageName( j );

				if ( pName1.equals(pName2) && ! wasSame ) {
					tmp.add(pName1 + ".*");
					releatedClasses.remove(j--);
					wasSame = true;
				} else if ( pName1.equals(pName2) && wasSame ) {
					releatedClasses.remove(j--);
				}
			}

			if ( ! wasSame ) {
				tmp.add( releatedClasses.get(i) );
			}
		}

		releatedClasses = tmp;
		java.util.Collections.sort(releatedClasses);
	}// sort

	// --- Super methods --------------------------------------------------------------------

	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (String s : releatedClasses) {
			sb.append(s).append('\n');
		}

		return sb.toString();
	}

}// class.RelatedClasses
