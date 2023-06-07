package org.example.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Language {
    private String language;
    private EntryDate entryDate;
}
