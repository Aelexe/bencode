package com.aelchemy.bencode.exception;

/**
 * Thrown if an attempt to decode Bencoded data fails due to it being in an invalid format.
 * 
 * @author Aelexe
 *
 */
public class InvalidFormatException extends Exception {

	private static final long serialVersionUID = -7158918635090896196L;

	public InvalidFormatException() {
		super();
	}

	public InvalidFormatException(final String message) {
		super(message);
	}

	public InvalidFormatException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
