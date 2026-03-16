package org.example.httpapiapp.repository;

import org.example.httpapiapp.entity.ShortUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@SpringBootTest(properties = {"app.base-url=http://localhost:8080/"})
class ShortUrlRepositoryTest {

    @Autowired
    private ShortUrlRepository repository;

    @Test
    void testSaveAndFindByShortCode() {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortCode("abc123");
        shortUrl.setOriginalUrl("https://example.com");

        repository.save(shortUrl);

        Optional<ShortUrl> found = repository.findByShortCode("abc123");
        assertThat(found).isPresent();
        assertThat(found.get().getOriginalUrl()).isEqualTo("https://example.com");
    }

    @Test
    void testExistsByShortCode() {

        ShortUrl url = new ShortUrl();
        url.setShortCode("xyz789");
        url.setOriginalUrl("https://example.com/test");
        repository.saveAndFlush(url);

        boolean exists = repository.existsByShortCode("xyz789");
        assertTrue(exists);

        boolean notExists = repository.existsByShortCode("notexist");
        assertFalse(notExists);
    }

    @Test
    void testShortCodeReturnUnique() {

        ShortUrl first = new ShortUrl();
        first.setShortCode("abc123");
        first.setOriginalUrl("https://example.com/1");
        repository.save(first);

        ShortUrl duplicate = new ShortUrl();
        duplicate.setShortCode("abc123");
        duplicate.setOriginalUrl("https://example.com/2");

        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(duplicate);
        });
    }
}