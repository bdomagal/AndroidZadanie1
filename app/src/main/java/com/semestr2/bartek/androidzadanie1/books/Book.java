package com.semestr2.bartek.androidzadanie1.books;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String genre;
    private byte[] cover;
    private byte[] altCover;
    private double price;
    private int amount;
    private boolean liked;

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Book(String title, String author, String genre, byte[] cover, byte[] altCover, double price) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.cover = cover;
        this.altCover = altCover;
        this.price = price;
    }

    public Book(Book b, int amount){
        this.title = b.title;
        author = b.author;
        this.genre = b.genre;
        this.cover = b.cover;
        this.altCover = b.altCover;
        this.price = b.price;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getPriceAsString() {
        return price+"";
    }

    public double getPrice() {
        return price;
    }
}
