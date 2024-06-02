package com.example.Task3.controller;

import com.example.Task3.model.Book;
import com.example.Task3.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("В пошуках Марсу");
        book.setAuthor("Ніл Армстронг");
    }

    @Test
    @WithMockUser(username = "Taras", password = "Herman", roles = "USER")
    public void testGetAllBooks() throws Exception {
        when(bookService.findAll()).thenReturn(Arrays.asList(book));

        mockMvc.perform(get("/api/books")
                        .with(httpBasic("Taras", "Herman")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("В пошуках Марсу"));
    }

    @Test
    @WithMockUser(username = "Taras", password = "Herman", roles = "USER")
    public void testGetBookById() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1")
                        .with(httpBasic("Taras", "Herman")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("В пошуках Марсу"));
    }

    @Test
    @WithMockUser(username = "Taras", password = "Herman", roles = "USER")
    public void testCreateBook() throws Exception {
        when(bookService.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(2L);
            return savedBook;
        });

        mockMvc.perform(post("/api/books")
                        .with(httpBasic("Taras", "Herman"))
                        .with(csrf()) // Add CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Кося і Мося\", \"author\": \"Тарас Герман\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Кося і Мося"))
                .andExpect(jsonPath("$.author").value("Тарас Герман"))
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    @WithMockUser(username = "Taras", password = "Herman", roles = "USER")
    public void testUpdateBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookService.save(any(Book.class))).thenAnswer(invocation -> {
            Book updatedBook = invocation.getArgument(0);
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            return book;
        });

        mockMvc.perform(put("/api/books/1")
                        .with(httpBasic("Taras", "Herman"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Кося і Мося 2\", \"author\": \"Тарас Германюк\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "Taras", password = "Herman", roles = "USER")
    public void testDeleteBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(delete("/api/books/1")
                        .with(httpBasic("Taras", "Herman"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
