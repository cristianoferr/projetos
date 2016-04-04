/*******************************************************************************
 * Copyright (c) 2010 Ricardo Juan Palma Durán.
 * 
 * This source file is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, version 3 of
 * the License. The text of the GNU Lesser General Public License 
 * is included with this application in the file LICENSE.TXT.
 * 
 * This source file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 ******************************************************************************/
package jbt.tools.bteditor.util;

import java.io.Serializable;

/**
 * Pair represents a pair of objects
 * 
 * @author Wikipedia
 * @param <T>
 *            type of the first element of the pair.
 * @param <S>
 *            type of the second element of the pair.
 */
public class Pair<T, S> implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 * First element of the pair.
	 */
	private T first;
	/*
	 * Second element of the pair.
	 */
	private S second;

	/**
	 * Constructs a Pair.
	 * 
	 * @param f
	 *            first element of the pair.
	 * @param s
	 *            second element of the pair.
	 */
	public Pair(T f, S s) {
		first = f;
		second = s;
	}

	/**
	 * Returns the first element of the pair.
	 * 
	 * @return the first element of the pair.
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * Returns the second element of the pair.
	 * 
	 * @return the second element of the pair.
	 */
	public S getSecond() {
		return second;
	}

	/**
	 * Sets the value of the first element of the pair.
	 * 
	 * @param f
	 *            value for the first element of the pair.
	 */
	public void setFirst(T f) {
		first = f;
	}

	/**
	 * Sets the value of the second element of the pair.
	 * 
	 * @param s
	 *            value for the second element of the pair.
	 */
	public void setSecond(S s) {
		second = s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + first.toString() + ", " + second.toString() + ")";
	}
	
	public boolean equals(Object o){
		if(this==o)
			return true;
		
		if(o instanceof Pair){
			return first.equals(((Pair)o).first) && second.equals(((Pair)o).second); 
		}
		else{
			return false;
		}
	}
	
	public int hashCode(){
		return first.hashCode()+second.hashCode();
	}
}
