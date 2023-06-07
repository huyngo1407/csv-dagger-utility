package org.example.controller.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class GetImageRequest {
    private String partnerId;
    private String imageKey;
}
