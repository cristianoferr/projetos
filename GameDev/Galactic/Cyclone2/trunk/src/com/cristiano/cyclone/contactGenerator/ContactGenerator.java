package com.cristiano.cyclone.contactGenerator;

import java.util.Vector;

import com.cristiano.cyclone.contact.Contact;


public interface ContactGenerator {
	
	    
	        /**
	         * Fills the given contact structure with the generated
	         * contact. The contact pointer should point to the first
	         * available contact in a contact array, where limit is the
	         * maximum number of contacts in the array that can be written
	         * to. The method returns the number of contacts that have
	         * been written.
	         */
	
	
		public int addContact(Vector<Contact> contact, int limit);

}
