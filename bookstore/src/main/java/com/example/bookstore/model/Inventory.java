package com.example.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stock;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Inventory(Book book, int stock) {
        this.book = book;
        this.stock = stock;
    }

    public Inventory() {
    }

    public Long getBookId() {
        return id;
    }

    public void setBookId(Long id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
