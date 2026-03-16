package org.example.httpapiapp.controller;

import org.example.httpapiapp.dto.ShortenRequest;
import org.example.httpapiapp.dto.ShortenResponse;
import org.example.httpapiapp.exception.InvalidAliasFormatException;
import org.example.httpapiapp.exception.ReservedAliasException;
import org.example.httpapiapp.service.ShortUrlService;
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
    public ShortenResponse shorten(@RequestBody ShortenRequest request) throws InvalidAliasFormatException, ReservedAliasException {
        return new ShortenResponse(
                service.shorten(request.getUrl(), request.getAlias())
        );
    }
}
