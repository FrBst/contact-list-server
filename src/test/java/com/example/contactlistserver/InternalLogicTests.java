package com.example.contactlistserver;

import static org.junit.jupiter.api.Assertions.*;

import com.example.contactlistserver.controller.ContactsController;
import com.example.contactlistserver.controller.UsersController;
import com.example.contactlistserver.exception.IllegalRequestException;
import com.example.contactlistserver.model.Contact;
import com.example.contactlistserver.model.PhoneNumber;
import com.example.contactlistserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class InternalLogicTests {

	@Autowired
	private UsersController usersController;
	@Autowired
	private ContactsController contactsController;

	@Test
	public void phoneTest() {
		PhoneNumber a = new PhoneNumber("123.456.789");
		PhoneNumber b = new PhoneNumber("123 45-67-89");
		PhoneNumber c = new PhoneNumber("1 2 3 5 4 6 7 8 9");
		assertEquals(a, b);
		assertNotEquals(b, c);
	}

	@Test
	public void contextLoads() {
		assertNotEquals(null, usersController);
		assertNotEquals(null, contactsController);
	}

	@Test
	public void illegalRequestTest() {
		// In users.
		assertThrows(IllegalRequestException.class, () -> {
				usersController.addUser(new User(null, ""));
		});
		assertThrows(IllegalRequestException.class, () -> {
			usersController.addUser(new User(1L, "Victor"));
		});

		// In contacts.
		assertThrows(IllegalRequestException.class, () -> {
			contactsController.addContact(new Contact(null, 1L, "Elena", null));
		});
		assertThrows(IllegalRequestException.class, () -> {
			contactsController.addContact(new Contact(null, 1L, null, new PhoneNumber("123456-789")));
		});
		assertThrows(IllegalRequestException.class, () -> {
			contactsController.addContact(new Contact(2L, null, null, new PhoneNumber("123456-789")));
		});
	}

	@Test
	public void controllersTest() {
		// Adding users
		assertEquals(1, usersController.addUser(new User(null, "Алексей")).getBody());
		assertEquals(2, usersController.addUser(new User(null, "Мария")).getBody());
		assertEquals(3, usersController.addUser(new User(null, "Marth")).getBody());
		assertEquals(4, usersController.addUser(new User(null, "Alex")).getBody());

		// Searching substrings in cyrillic.
		assertEquals(2, usersController.searchInName("а").getBody().size());
		assertEquals(1, usersController.searchInName("М").getBody().size());

		contactsController.addContact(new Contact(null, 1L, "Elena", new PhoneNumber("123456-789")));
		contactsController.addContact(new Contact(null, 3L, "Dimitri", new PhoneNumber("987 654 321")));
		contactsController.addContact(new Contact(null, 3L, "Dimitri", new PhoneNumber("987 65-43-21")));
		contactsController.addContact(new Contact(null, 1L, "Dimitri",new PhoneNumber( "987 654 321")));

		// Search by phone number.
		assertEquals(2, contactsController.searchByPhoneNumber(3, "987654321").getBody().size());
		assertEquals(1, contactsController.searchByPhoneNumber(1, "987654321").getBody().size());

		// Changing contacts.
		assertEquals(200, contactsController.changeContact(2, new Contact(null, null, "Changed", null)).getStatusCodeValue());
		assertEquals(200, contactsController.changeContact(2, new Contact(null, null, null, new PhoneNumber("111-111"))).getStatusCodeValue());
		assertThrows(IllegalRequestException.class, () -> {
			contactsController.changeContact(2, new Contact(null, 1L, null, new PhoneNumber("111-111")));
		});
		// Checking the changes.
		ResponseEntity<Contact> resp = contactsController.getContact(2);
		assertEquals(200, resp.getStatusCodeValue());
		assertEquals("Changed", resp.getBody().getName());
		assertEquals(new PhoneNumber("111111"), resp.getBody().getPhoneNumber());
	}
}
