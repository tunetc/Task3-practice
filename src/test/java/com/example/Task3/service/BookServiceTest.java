package com.example.Task3.service;

import com.example.Task3.model.Book;
import com.example.Task3.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testFindAll() {
        Book book1 = new Book();
        book1.setTitle("Кося і Мося");
        Book book2 = new Book();
        book2.setTitle("Кося і Мося 2");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.findAll();
        assertEquals(2, books.size());
        assertEquals("Кося і Мося", books.get(0).getTitle());
    }

    @Test
    public void testFindById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Кося і Мося");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.findById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals("Кося і Мося", foundBook.get().getTitle());
    }

    @Test
    public void testSave() {
        Book book = new Book();
        book.setTitle("Кося і Мося 3");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.save(book);
        assertEquals("Кося і Мося 3", savedBook.getTitle());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
