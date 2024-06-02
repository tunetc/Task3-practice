package com.example.Task3;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Task3Application.class})
public class IntegrationTest {
    private int port=80;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/books";
    }

    @Test
    public void testGetAllBooks() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("Taras", "Herman");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBookById() {
        Long bookId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("Taras", "Herman");
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/" + bookId, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreateBook() {
        // Assuming JSON request body for creating a new book
        String requestBody = "{\"title\": \"New Book\", \"author\": \"New Author\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("Taras", "Herman");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        String requestBody = "{\"title\": \"Updated Book\", \"author\": \"Updated Author\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("Taras", "Herman");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/" + bookId, HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("Taras", "Herman");
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/" + bookId, HttpMethod.DELETE, entity, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
