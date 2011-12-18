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
package jyzer.items.exceptions;

/**
 * The onliest subclass of ParsingException. This exception is thrown when
 * it seems that the classfile possibly corrupted.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 okt 27
 */
public class CorruptedClassfileException extends ParsingException {

	/**
	 * The constructor. Just calls super.
	 *
	 * @param where the name of the phase where the exception was risen.
	 * @param message other useful info.
	 */
	public CorruptedClassfileException(String where, String message) {
		super(where, message);
	}

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this Throwable instance (not null).
	 */
	public String getMessage() {
		return "A classfile structure error has occured in " + where + ".\n" + message;
	}

}// class.ValidationException
