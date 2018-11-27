package com.semestr2.bartek.androidzadanie1.books;

public class Book {
    private String title;
    private String Author;
    private String genre;
    private byte[] cover;
    private byte[] altCover;
    private double price;

    public Book(String title, String author, String genre, byte[] cover, byte[] altCover, double price) {
        this.title = title;
        Author = author;
        this.genre = genre;
        this.cover = cover;
        this.altCover = altCover;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public byte[] getAltCover() {
        return altCover;
    }

    public void setAltCover(byte[] altCover) {
        this.altCover = altCover;
    }
}
