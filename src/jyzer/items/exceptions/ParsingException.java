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
 * A main class for the exceptions that can be risen while processing.
 * Although, at the moment it has no subclasses... :-) But you never know.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005 sept 06.
 */
public class ParsingException extends Exception {

	/** Where has the exception raised. */
	protected String where;
	/** Other comment. */
	protected String message;

	/**
	 * The constructor.
	 *
	 * @param where the name of the phase where the exception was risen.
	 * @param message other useful info.
	 */
	public ParsingException(String where, String message) {
		this.where = where;
		this.message = message;
	}

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this Throwable instance (not null).
	 */
	public String getMessage() {
		return "Problem was found in " + where + ".\n" + message;
	}

	/**
	 * For debugging reasons. I was intrested to overwrite this method.
	 *
	 * @return the string representation of this object.
	 */
	public String toString() {
		return ( getClass() + "[where:" + where + "]" + "[message:" + message + "]" );
	}

}// class.ParsingException
