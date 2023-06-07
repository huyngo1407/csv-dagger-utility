package org.example.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class EntryDate {
    private String entryDate;
    private String key;
    private String value;
}
