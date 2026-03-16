package org.example.httpapiapp.service;

import org.example.httpapiapp.exception.InvalidAliasFormatException;
import org.example.httpapiapp.exception.ReservedAliasException;
import org.example.httpapiapp.exception.ShortUrlNotFoundException;
import org.example.httpapiapp.entity.ShortUrl;
import org.example.httpapiapp.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ShortUrlService {

    @Value("${app.base-url}")
    private String baseUrl;

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


            ShortUrl entity = new ShortUrl();
            entity.setShortCode(alias);
            entity.setOriginalUrl(originalUrl);

            try {
                repository.save(entity);
                code = alias;
            } catch (Exception e) {
                return "Alias already exists";
            }
        } else {

            code = generateUniqueCode(originalUrl);
        }

        return baseUrl + code;
    }

    public String getOriginalUrl(String code) throws ShortUrlNotFoundException {
        return repository.findByShortCode(code)
                .orElseThrow(() -> new ShortUrlNotFoundException(code))
                .getOriginalUrl();
    }

    private String generateUniqueCode(String originalUrl) {

        String code = generateCode();

        while (true) {

            ShortUrl entity = new ShortUrl();
            entity.setShortCode(code);
            entity.setOriginalUrl(originalUrl);

            try {

                repository.save(entity);
                break;
            } catch (Exception e) {

                code = generateCode();
            }
        }

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
            throw new InvalidAliasFormatException(alias);
        }

        if (RESERVED_ALIASES.contains(alias.toLowerCase())) {
            throw new ReservedAliasException(alias);
        }
    }
}
