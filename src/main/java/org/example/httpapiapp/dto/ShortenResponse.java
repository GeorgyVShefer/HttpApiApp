package org.example.httpapiapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenResponse {

    private String shortUrl;

    public ShortenResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
