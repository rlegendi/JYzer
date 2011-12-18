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

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This is an universal filefilter object, to prevent those nasty <code>FileFilter</code>
 * definitions ...
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 *
 */
public class UniversalFileFilter extends FileFilter {

	// --- Variables ------------------------------------------------------------------------

	private String suffix, description;

	// --- Constructors ---------------------------------------------------------------------

	/**
	 * Creates a new instance of UniversalFileFilter, that filters for the given
	 * suffix. The descriptor will be an empty string ("").
	 *
	 * @param suffix the extension of the files.
	 */
	public UniversalFileFilter(String suffix) {
		this(suffix, null);
	}

	/**
	 * Creates a new instance of UniversalFileFilter, that filters for the given
	 * suffix.
	 *
	 * @param suffix the extension of the files.
	 * @param description the description for the given extension.
	 */
	public UniversalFileFilter(String suffix, String description) {
		this.suffix      = suffix;
		this.description = description;
	}

	// --- Super methods --------------------------------------------------------------------

	/**
	 * To determine what kind of files wish I display in a filechooser dialog.
	 *
	 * @param f the file that has to be examined.
	 */
	public boolean accept(File f) {
		return ( f.getName().toLowerCase().endsWith(suffix) || f.isDirectory() );
	}

	/**
	 * A human readable description of the file. For example, a file named jag.jpg
	 * might have a description that read: "A JPEG image file of James Gosling's face".
	 * <p>This was pasted here from the referece of <code>FileView</code>. Correct and funny,
	 * isn't it? :-)</p>
	 */
	public String getDescription() {
		return (null == description) ? "" : description;
	}

}// class.UniversalFileFilter
