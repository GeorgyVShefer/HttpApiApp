package org.example.httpapiapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenRequest {

    private String url;
    private String alias;
}