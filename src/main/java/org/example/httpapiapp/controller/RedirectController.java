package org.example.httpapiapp.controller;

import org.example.httpapiapp.exception.ShortUrlNotFoundException;
import org.example.httpapiapp.service.ShortUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@Controller
public class RedirectController {

     private final ShortUrlService service;

     public RedirectController(ShortUrlService service) {
        this.service = service;
    }

     @GetMapping("/{code}")
     public ResponseEntity<Void> redirect(@PathVariable String code) throws ShortUrlNotFoundException {
        String url = service.getOriginalUrl(code);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
