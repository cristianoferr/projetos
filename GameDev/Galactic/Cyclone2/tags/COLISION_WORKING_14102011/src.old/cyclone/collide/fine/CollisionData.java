package cyclone.collide.fine;

import java.util.Vector;

import cyclone.contact.Contact;

public class CollisionData {
    /**
     * A helper structure that contains information for the detector to use
     * in building its contact data.
     */
        /**
         * Holds the base of the collision data: the first contact
         * in the array. This is used so that the contact pointer (below)
         * can be incremented each time a contact is detected, while
         * this pointer points to the first contact found.
         */
      //  Contact contactArray;

        /** Holds the contact array to write into. */
        Vector<Contact> contacts=new Vector<Contact>();

        /** Holds the maximum number of contacts the array can take. */
        int contactsLeft;

        /** Holds the number of contacts found so far. */
        int contactCount;

        /** Holds the friction value to write into any collisions. */
        double friction;

        /** Holds the restitution value to write into any collisions. */
        double restitution;

        /**
         * Holds the collision tolerance, even uncolliding objects this
         * close should have collisions generated.
         */
        double tolerance;

        /**
         * Checks if there are more contacts available in the contact
         * data.
         */
        boolean hasMoreContacts()
        {
            return contactsLeft > 0;
        }

        /**
         * Resets the data so that it has no used contacts recorded.
         */
        void reset(int maxContacts)
        {
            contactsLeft = maxContacts;
            contactCount = 0;
            contacts.clear();// = contactArray;
        }

        /**
         * Notifies the data that the given number of contacts have
         * been added.
         */
        void addContacts(int count)
        {
            // Reduce the number of contacts remaining, add number used
            contactsLeft -= count;
            contactCount += count;

            // Move the array forward
           // contacts += count;
        }
    }

