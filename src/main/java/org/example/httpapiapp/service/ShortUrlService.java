package org.example.httpapiapp.service;

import org.example.httpapi.model.ShortUrl;
import org.example.httpapi.repository.ShortUrlRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ShortUrlService {

    private static final String BASE_URL = "http://localhost:8080/";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    private static final Pattern ALIAS_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,32}$");

    private static final Set<String> RESERVED_ALIASES = Set.of("api", "swagger", "swagger-ui", "v3", "actuator");

    private final ShortUrlRepository repository;
    private final Random random = new SecureRandom();

    public ShortUrlService(ShortUrlRepository repository) {
        this.repository = repository;
    }

    public String shorten(String originalUrl) {
        return shorten(originalUrl, null);
    }

    public String shorten(String originalUrl, String alias) {

        String code;

        if (alias != null && !alias.isBlank()) {
            validateAlias(alias);

            if (repository.existsByShortCode(alias)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Alias already exists"
                );
            }

            code = alias;
        } else {
            code = generateUniqueCode();
        }

        ShortUrl entity = new ShortUrl();
        entity.setShortCode(code);
        entity.setOriginalUrl(originalUrl);

        repository.save(entity);
        return BASE_URL + code;
    }

    public String getOriginalUrl(String code) {
        return repository.findByShortCode(code)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Short URL not found"
                        )
                )
                .getOriginalUrl();
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (repository.existsByShortCode(code));
        return code;
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private void validateAlias(String alias) {

        if (!ALIAS_PATTERN.matcher(alias).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid alias format"
            );
        }

        if (RESERVED_ALIASES.contains(alias.toLowerCase())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Alias is reserved"
            );
        }
    }
}
