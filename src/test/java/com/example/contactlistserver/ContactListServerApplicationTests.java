package com.example.contactlistserver;

import static org.junit.jupiter.api.Assertions.*;

import com.example.contactlistserver.controller.ContactsController;
import com.example.contactlistserver.controller.UsersController;
import com.example.contactlistserver.model.Contact;
import com.example.contactlistserver.model.PhoneNumber;
import com.example.contactlistserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ContactListServerApplicationTests {

	@Autowired
	private UsersController usersController;
	@Autowired
	private ContactsController contactsController;

	@Test
	void phoneTest() {
		PhoneNumber a = new PhoneNumber("123.456.789");
		PhoneNumber b = new PhoneNumber("123 45-67-89");
		PhoneNumber c = new PhoneNumber("1 2 3 5 4 6 7 8 9");
		assertEquals(a, b);
		assertNotEquals(b, c);
	}

	@Test
	void contextLoads() {
		assertNotEquals(null, usersController);
		assertNotEquals(null, contactsController);
	}

	@Test
	void endpoints() {
		// Adding users.
		assertEquals(1, usersController.addUser("Alexey").getBody());
		assertEquals(2, usersController.addUser("Boris").getBody());
		assertEquals(3, usersController.addUser("Ciri").getBody());
		assertEquals(4, usersController.addUser("Daniil").getBody());

		// Acquiring users.
		// Get by id.
		assertEquals("Ciri", usersController.getUser(3).getBody().getName());
		// Get all.
		assertEquals(4, usersController.getAllUsers().size());
		// Search in name.
		assertEquals(3, usersController.searchInName("i").size());
		assertEquals(2, usersController.searchInName("a").size());
		assertEquals(0, usersController.searchInName("u").size());

		// Manipulating users:
		// Change.
		assertEquals(200, usersController.changeUser(2, "Борис").getStatusCodeValue());
		assertEquals(200, usersController.changeUser(3, "Цири").getStatusCodeValue());
		assertEquals(404, usersController.changeUser(15, "Волк").getStatusCodeValue());
		// Remove
		assertEquals(5, usersController.addUser("Vladimir").getBody());
		assertEquals(200, usersController.removeUser(5).getStatusCodeValue());
		assertEquals(404, usersController.removeUser(5).getStatusCodeValue());
		assertEquals(200, usersController.removeUser(4).getStatusCodeValue());

		// Search in cyrillic names.
		assertEquals(2, usersController.searchInName("и").size());
		assertEquals(1, usersController.searchInName("Б").size());

		// Adding contacts.
		// These users don't exist.
		assertEquals(404, contactsController.addUserContact(0, "Mikhail", "123-567-890").getStatusCodeValue());
		assertEquals(404, contactsController.addUserContact(4, "Mikhail", "123-567-890").getStatusCodeValue());
		// These users exist.
		assertEquals(1, contactsController.addUserContact(1, "Elena", "123456-789").getBody());
		assertEquals(2, contactsController.addUserContact(3, "Dimitri", "987 654 321").getBody());
		assertEquals(3, contactsController.addUserContact(3, "Dimitri", "987 65-43-21").getBody());
		assertEquals(4, contactsController.addUserContact(1, "Dimitri", "987 654 321").getBody());

		// Getting contacts.
		// Get all.
		assertEquals(4, contactsController.getAllContacts().size());
		// Get by id.
		assertEquals("Dimitri", contactsController.getContact(2).getBody().getName());
		// Get for specific user.
		assertEquals(2, contactsController.getAllUserContacts(3).size());
		assertEquals(0, contactsController.getAllUserContacts(4).size());
		assertEquals(0, contactsController.getAllUserContacts(2).size());
		// Search by phone number.
		assertEquals(2, contactsController.searchByPhoneNumber(3, "987654321").size());
		assertEquals(1, contactsController.searchByPhoneNumber(1, "987654321").size());

		// Changing contacts.
		assertEquals(404, contactsController.changeContact(6, "sfsdf", "142345").getStatusCodeValue());
		assertEquals(400, contactsController.changeContact(2, null, null).getStatusCodeValue());
		assertEquals(200, contactsController.changeContact(2, "Changed", null).getStatusCodeValue());
		assertEquals(200, contactsController.changeContact(2, null, "111-111").getStatusCodeValue());
		// Checking the changes.
		ResponseEntity<Contact> resp = contactsController.getContact(2);
		assertEquals(200, resp.getStatusCodeValue());
		assertEquals("Changed", resp.getBody().getName());
		assertEquals(new PhoneNumber("111111"), resp.getBody().getPhoneNumber());

		// Removing contacts.
		assertEquals(200, contactsController.removeContact(1).getStatusCodeValue());
		assertEquals(404, contactsController.removeContact(1).getStatusCodeValue());
		assertEquals(404, contactsController.removeContact(10).getStatusCodeValue());
		assertEquals(3, contactsController.getAllContacts().size());
	}
}
