package org.example.httpapiapp.controller;

import org.example.httpapiapp.entity.ShortUrl;
import org.example.httpapiapp.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortUrlService shortUrlService;

    private ShortUrl sample;

    @BeforeEach
    void setUp() {
        sample = new ShortUrl();
        sample.setShortCode("google");
        sample.setOriginalUrl("https://google.com");
    }

    @Test
    void shouldCreateShortUrlWithAlias() throws Exception {

        when(shortUrlService.shorten(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("http://localhost:8080/google");

        String json = "{\"url\":\"https://google.com\",\"alias\":\"google\"}";

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/google"));
    }
}

