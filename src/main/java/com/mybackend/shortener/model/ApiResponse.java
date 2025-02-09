package com.mybackend.shortener.model;


public class ApiResponse {
    private String hash;

    // Constructor
    public ApiResponse(String hash) {
        this.hash = hash;
    }

    // Default constructor (needed for JSON serialization)
    public ApiResponse() {}

    // Getter and Setter
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
