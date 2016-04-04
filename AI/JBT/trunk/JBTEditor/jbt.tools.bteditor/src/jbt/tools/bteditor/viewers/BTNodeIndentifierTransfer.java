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
package jbt.tools.bteditor.viewers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jbt.tools.bteditor.model.BTNode;
import jbt.tools.bteditor.model.BTNode.Identifier;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * This transfer class transfers BTNode objects. However, only the BTNode identifier is
 * transfered.
 * 
 * @author Ricardo Juan Palma Durán
 *
 */
public class BTNodeIndentifierTransfer extends ByteArrayTransfer {
	private static final String TYPENAME = "BTNodeIndentifierTransfer";
	private static final int TYPEID = registerType(TYPENAME);
	
	private static BTNodeIndentifierTransfer instance = null;

	private BTNodeIndentifierTransfer(){}

	public static BTNodeIndentifierTransfer getInstance(){
		if(instance == null){
			instance = new BTNodeIndentifierTransfer();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String[] getTypeNames(){
		return new String[]{TYPENAME};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int[] getTypeIds(){
		return new int[]{TYPEID};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#validate(java.lang.Object)
	 */
	protected boolean validate(Object object){
		return(object != null && object instanceof BTNode);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object,
	 * org.eclipse.swt.dnd.TransferData)
	 */
	public void javaToNative(Object object, TransferData transferData){
		if(!validate(object)){
			DND.error(DND.ERROR_INVALID_DATA);
		}
		
		if( !isSupportedType(transferData)){
			DND.error(DND.ERROR_INVALID_DATA);
		}
		
		BTNode node=(BTNode)object;
		
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writeOut = new ObjectOutputStream(out);
	
			writeOut.writeObject(node.getID());
			
			byte[] buffer = out.toByteArray();
			writeOut.close();
			out.close();
			super.javaToNative(buffer, transferData);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd
	 * .TransferData)
	 */
	public Object nativeToJava(TransferData transferData){
		if(!isSupportedType(transferData)){
			return null;
		}
		byte[] buffer = (byte[])super.nativeToJava(transferData);
		if(buffer == null)
			return null;
		try{
			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			ObjectInputStream readIn = new ObjectInputStream(in);
			Identifier id=(Identifier)readIn.readObject();
			
			readIn.close();
			in.close();
			return id;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}
