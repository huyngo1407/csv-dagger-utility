package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.request.CreateCsvDaggerRequest;
import org.example.model.CsvDagger;
import org.example.service.csv.dagger.CsvDaggerService;
import org.example.util.api_response.ApiResponseUtil;
import org.example.value_object.ResponseMessageCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/csv-daggers")
@RequiredArgsConstructor
public class CsvDaggerController {
    private final CsvDaggerService csvDaggerService;


    // partnerId (pathParam), language (RequestParam) default en
    @GetMapping("")
    public ResponseEntity<Object> getAll(@RequestParam(name = "fetch-date") String fetchDate) {
        List<CsvDagger> records = csvDaggerService.getAll(fetchDate);
        return ApiResponseUtil.build(HttpStatus.OK, ResponseMessageCode.CsvDagger.Success.GET_DATA, records);
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody CreateCsvDaggerRequest createCsvDaggerRequest) {
        csvDaggerService.create(createCsvDaggerRequest);
        return ApiResponseUtil.build(HttpStatus.CREATED, ResponseMessageCode.CsvDagger.Success.CREATE);
    }
}
