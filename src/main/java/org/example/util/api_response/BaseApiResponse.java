package org.example.util.api_response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class BaseApiResponse {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private String message;
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Object> errors;
}
