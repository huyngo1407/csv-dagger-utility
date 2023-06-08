package org.example.controller.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ContentRequest {
    private String partnerId;
    private String contentType;
    private String lang;
    private String lastFetchDate;
}
