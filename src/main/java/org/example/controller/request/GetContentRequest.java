package org.example.controller.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class GetContentRequest {
    private String partnerId;
    private String contentType;
}
