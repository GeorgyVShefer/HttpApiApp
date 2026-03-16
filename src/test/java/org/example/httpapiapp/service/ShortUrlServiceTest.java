package org.example.httpapiapp.service;

import org.example.httpapiapp.entity.ShortUrl;
import org.example.httpapiapp.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository repository;

    @InjectMocks
    private ShortUrlService service;

    @Test
    void shouldCreateShortUrlWithoutAlias() {
        when(repository.existsByShortCode(anyString())).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String result = service.shorten("https://google.com");

        assertThat(result).startsWith("http://localhost:8080/");
        verify(repository).save(any(ShortUrl.class));
    }

    @Test
    void shouldCreateShortUrlWithAlias() {
        when(repository.existsByShortCode("spring")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String result = service.shorten(
                "https://spring.io",
                "spring"
        );

        assertThat(result).isEqualTo("http://localhost:8080/spring");
    }

    @Test
    void shouldThrowExceptionIfAliasExists() {
        when(repository.existsByShortCode("spring")).thenReturn(true);

        assertThatThrownBy(() ->
                service.shorten("https://spring.io", "spring")
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Alias already exists");
    }

    @Test
    void shouldThrowExceptionForInvalidAlias() {
        assertThatThrownBy(() ->
                service.shorten("https://spring.io", "!!!")
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Invalid alias format");
    }
}
