package org.example.httpapiapp.controller;

import org.example.httpapi.dto.ShortenRequest;
import org.example.httpapi.dto.ShortenResponse;
import org.example.httpapi.service.ShortUrlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShortUrlController {

    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ShortenResponse shorten(@RequestBody ShortenRequest request) {
        return new ShortenResponse(
                service.shorten(request.getUrl(), request.getAlias())
        );
    }
}
