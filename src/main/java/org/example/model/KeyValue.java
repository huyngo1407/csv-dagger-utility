package org.example.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class KeyValue {
    private String key;
    private String value;
}
