package org.example.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Record {
    private String partnerId;
    private String language;
    private String key;
    private String value;
}