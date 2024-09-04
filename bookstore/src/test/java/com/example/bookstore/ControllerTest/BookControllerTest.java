package com.example.bookstore.ControllerTest;

import com.example.bookstore.controller.BookController;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void testGetAllBooks() throws Exception {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, "Book 1", "Author 1", 10.0));
        when(bookService.getAllBooks()).thenReturn(bookList);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[0].author").value("Author 1"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book(1L, "Book 1", "Author 1", 10.0);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Book 1")))
                .andExpect(jsonPath("$.author", is("Author 1")));
    }
}
