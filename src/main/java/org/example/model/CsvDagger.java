package org.example.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class CsvDagger {
    private List<Record> records;
}
