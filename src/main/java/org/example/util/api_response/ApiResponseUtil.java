package org.example.util.api_response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {
    private ApiResponseUtil() {
    }

    public static ResponseEntity<Object> build(HttpStatus httpStatus, String message) {
        BaseApiResponse baseApiResponse = BaseApiResponse.builder()
                .message(message)
                .build();
        return new ResponseEntity<>(baseApiResponse, httpStatus);
    }

    public static ResponseEntity<Object> build(HttpStatus httpStatus, String message, Object data) {
        BaseApiResponse baseApiResponse = BaseApiResponse.builder()
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(baseApiResponse, httpStatus);
    }

    public static ResponseEntity<Object> build(HttpStatus httpStatus, String message, Object data, HttpHeaders headers) {
        BaseApiResponse baseApiResponse = BaseApiResponse.builder()
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(baseApiResponse, headers, httpStatus);
    }

    public static ResponseEntity<Object> build(HttpStatus httpStatus, Object data, HttpHeaders headers) {
        return new ResponseEntity<>(data, headers, httpStatus);
    }
}
