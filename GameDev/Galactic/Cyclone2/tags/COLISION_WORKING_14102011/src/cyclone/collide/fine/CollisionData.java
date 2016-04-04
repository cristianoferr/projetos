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
        private Vector<Contact> contacts=new Vector<Contact>();

        /** Holds the maximum number of contacts the array can take. */
        int contactsLeft;

        /** Holds the number of contacts found so far. */
        int contactCount;

        /** Holds the friction value to write into any collisions. */
        private double friction;

        /** Holds the restitution value to write into any collisions. */
        private double restitution;

        /**
         * Holds the collision tolerance, even uncolliding objects this
         * close should have collisions generated.
         */
        private double tolerance;

        /**
         * Checks if there are more contacts available in the contact
         * data.
         */
        public boolean hasMoreContacts()
        {
            return true;//getContacts().size() > 0;
        }

        /**
         * Resets the data so that it has no used contacts recorded.
         */
        public void reset(int maxContacts)
        {
            contactsLeft = maxContacts;
            contactCount = 0;
            getContacts().clear();// = contactArray;
        }

        /**
         * Notifies the data that the given number of contacts have
         * been added.
         */
        void addContacts(Contact contact)
        {
            // Reduce the number of contacts remaining, add number used
            contactsLeft --;
            contactCount ++;
            contacts.add(contact);
          //  if ((contact.body[0]!=null) && (contact.body[1]!=null))
           // System.out.println("Contact created");

            // Move the array forward
           // contacts += count;
        }

		public void setFriction(double friction) {
			this.friction = friction;
		}

		public double getFriction() {
			return friction;
		}

		public void setRestitution(double restitution) {
			this.restitution = restitution;
		}

		public double getRestitution() {
			return restitution;
		}

		public void setTolerance(double tolerance) {
			this.tolerance = tolerance;
		}

		public double getTolerance() {
			return tolerance;
		}

		public void setContacts(Vector<Contact> contacts) {
			this.contacts = contacts;
		}

		public Vector<Contact> getContacts() {
			return contacts;
		}
    }

