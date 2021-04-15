package com.example.contactlistserver;

import static org.junit.jupiter.api.Assertions.*;

import com.example.contactlistserver.model.Contact;
import com.example.contactlistserver.model.PhoneNumber;
import com.example.contactlistserver.model.User;
import com.example.contactlistserver.wrapper.ContactListWrapper;
import com.example.contactlistserver.wrapper.UserListWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebLayerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();

    private boolean requestAndCheck(HttpMethod httpMethod, String uri, Object body,
                            HttpStatus expectedStatus, Object expectedBody) {
        BodyInserter bodyInserter = BodyInserters.empty();
        if (body != null) {
            bodyInserter = BodyInserters.fromValue(body);
        }
        WebTestClient.ResponseSpec temp = client.method(httpMethod)
                .uri(uri)
                .body(bodyInserter)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);

        if (expectedBody == null) {
            return temp.returnResult(Boolean.class).getStatus().equals(expectedStatus);
        }
        return temp.expectBody(expectedBody.getClass()).returnResult().getResponseBody().equals(expectedBody);
    }

    @Test
    public void WebLayerTests() throws Exception {
        // Adding users.
        assertTrue(requestAndCheck(HttpMethod.POST, "/users", new User(null, "Alexey"), HttpStatus.OK, 1L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/users", new User(null, "Boris"), HttpStatus.OK, 2L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/users", new User(null, "Ciri"), HttpStatus.OK, 3L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/users", new User(null, "Daniil"), HttpStatus.OK, 4L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/users", null, HttpStatus.BAD_REQUEST, null));
        assertTrue(requestAndCheck(HttpMethod.POST, "/users", new User(null, ""), HttpStatus.BAD_REQUEST, null));

        // Acquiring users.
        // Get by id.
        assertTrue(requestAndCheck(HttpMethod.GET, "/users/3", null, HttpStatus.OK, new User(3L, "Ciri")));
        assertTrue(requestAndCheck(HttpMethod.GET, "/users/100", null, HttpStatus.NOT_FOUND, null));
        // Search in name.
        assertTrue(client.get()
                .uri("/users?substring=i")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(UserListWrapper.class).returnResult().getResponseBody().getUsers()
                .size() == 3);
        // Get all.
        assertTrue(client.get()
                .uri("/users")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(UserListWrapper.class).returnResult().getResponseBody().getUsers()
                .size() == 4);

        // Manipulating users.
        // Change.
        assertTrue(requestAndCheck(HttpMethod.PATCH, "/users/2", new User(null, "Борис"), HttpStatus.OK, null));
        assertTrue(requestAndCheck(HttpMethod.PATCH, "/users/3", new User(null, "Цири"), HttpStatus.OK, null));
        // Remove.
        assertTrue(requestAndCheck(HttpMethod.DELETE, "/users/4", null, HttpStatus.OK, null));
        // Trying to change user names with wrong parameters.
        assertTrue(requestAndCheck(HttpMethod.PATCH, "/users/1", new User(null, ""), HttpStatus.BAD_REQUEST, null));
        assertTrue(requestAndCheck(HttpMethod.PATCH, "/users/1", new User(1L, "Vlad"), HttpStatus.BAD_REQUEST, null));
        // Non-existent user.
        assertTrue(requestAndCheck(HttpMethod.PATCH, "/users/100", new User(null, "Alexander"), HttpStatus.NOT_FOUND, null));
        assertTrue(requestAndCheck(HttpMethod.DELETE, "/users/15", null, HttpStatus.NOT_FOUND, null));

        // Adding contacts.
        // These users exist.
        assertTrue(requestAndCheck(HttpMethod.POST, "/contacts",
                new Contact(null, 1L, "Elena", new PhoneNumber("123456-789")), HttpStatus.OK, 1L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/contacts",
                new Contact(null, 3L, "Dimitri", new PhoneNumber("987 654 321")), HttpStatus.OK, 2L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/contacts",
                new Contact(null, 3L, "Dimitri", new PhoneNumber("987 65-43-21")), HttpStatus.OK, 3L));
        assertTrue(requestAndCheck(HttpMethod.POST, "/contacts",
                new Contact(null, 1L, "Dimitri",new PhoneNumber( "987 654 321")), HttpStatus.OK, 4L));
        // These users don't exist.
        assertTrue(requestAndCheck(HttpMethod.POST, "/contacts",
                new Contact(null, 100L, "Mikhail", new PhoneNumber("123-567-890")), HttpStatus.NOT_FOUND, null));
        assertTrue(requestAndCheck(HttpMethod.POST, "/contacts",
                new Contact(null, 0L, "Mikhail", new PhoneNumber("123-567-890")), HttpStatus.NOT_FOUND, null));

        // Getting contacts.
        // Get all.
        assertEquals(client.get()
                .uri("/contacts")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(ContactListWrapper.class).returnResult().getResponseBody().getContacts()
                .size(), 4);
        // Get by id.
        assertTrue(requestAndCheck(HttpMethod.GET, "/contacts/2", null, HttpStatus.OK,
                new Contact(2L, 3L, "Dimitri", new PhoneNumber("987 654 321"))));
        // Get for specific user.
        assertTrue(requestAndCheck(HttpMethod.GET, "/users/4/contacts", null, HttpStatus.NOT_FOUND, null));
        assertEquals(client.get()
                .uri("/users/3/contacts")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(ContactListWrapper.class).returnResult().getResponseBody().getContacts()
                .size(), 2);
    }
}
