package org.example.httpapiapp.exception;

public class ShortUrlNotFoundException extends Exception{

    public ShortUrlNotFoundException(String code) {
        super("Short URL not found: " + code);
    }
}
