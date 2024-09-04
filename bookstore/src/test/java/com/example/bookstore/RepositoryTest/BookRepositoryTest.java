package com.example.bookstore.RepositoryTest;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testFindAll() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testSaveAndFindById() {
        Book book = new Book();
        book.setTitle("Alice in Wonderland");
        book.setAuthor("Lewis Carroll");
        book.setPrice(50.0);

        Book savedBook = bookRepository.save(book);

        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Alice in Wonderland");
        assertThat(foundBook.getAuthor()).isEqualTo("Lewis Carroll");
        assertThat(foundBook.getPrice()).isEqualTo(50.0);
    }

    @Test
    public void testDelete() {
        Book book = new Book();
        book.setTitle("Alice in Wonderland");
        book.setAuthor("Lewis Carroll");
        book.setPrice(50.0);

        Book savedBook = bookRepository.save(book);
        bookRepository.delete(savedBook);

        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertThat(foundBook).isNull();
    }
}
