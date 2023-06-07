package org.example.controller.request;

import lombok.Data;

@Data
public class CreateCsvDaggerRequest {
    private String entryDate;
    private String partnerId;
    private String language;
    private String key;
    private String value;
}
