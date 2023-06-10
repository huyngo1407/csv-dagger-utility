package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.request.GetContentRequest;
import org.example.controller.request.GetImageRequest;
import org.example.model.Content;
import org.example.service.content.ContentService;
import org.example.util.api_response.ApiResponseUtil;
import org.example.value_object.ResponseMessageCode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@Controller
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/{partnerId}/incremental-data/{contentType}")
    public ResponseEntity<Object> getContents(@PathVariable String partnerId, @PathVariable String contentType,
                                              @RequestParam("lang") String language, @RequestParam("lastFetchDate") String lastFetchDate) {
        GetContentRequest getContentRequest = GetContentRequest.builder()
                .partnerId(partnerId)
                .contentType(contentType)
                .language(language)
                .lastFetchDate(lastFetchDate)
                .build();
        List<Content> contents = contentService.getContents(getContentRequest);
        return ApiResponseUtil.build(HttpStatus.OK, ResponseMessageCode.CsvDagger.Success.GET_DATA, contents);
    }

    @GetMapping("/{partnerId}/images/{imageKey}")
    @ResponseBody
    public ResponseEntity<Object> getImage(@PathVariable String partnerId, @PathVariable String imageKey) throws IOException {
        GetImageRequest getImageRequest = GetImageRequest.builder()
                .partnerId(partnerId)
                .imageKey(imageKey)
                .build();
        File file = contentService.getImage(getImageRequest);
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        URLConnection connection = file.toURL().openConnection();
        String mimeType = connection.getContentType();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(inputStreamResource);
    }

//    @GetMapping("/csv-to-yaml")
//    @ResponseBody
//    public ResponseEntity<Object> csvToYaml() throws IOException, CsvException {
//        List<String[]> csvData = CsvUtil.read("src/main/resources/document/csv-to-yaml.csv");
//        Map<String, LinkedHashMap> contentToPartnerId = new HashMap<>();
////        Map<String, List<LinkedHashMap>> entryDateToKeyValue = new LinkedHashMap();
//        for (String[] line : csvData) {
//            String content = line[0];
//            String entryDate = line[1];
//            String partnerId = line[2];
//            String language = line[3];
//            String key = line[4];
//            String value = line[5];
//            String version = line[6];
//
//
//            if (ObjectUtils.isEmpty(contentToPartnerId.get(content))) {
//                LinkedHashMap keyValue = new LinkedHashMap();
//                keyValue.put("version", version);
//                keyValue.put("key", key);
//                keyValue.put("value", value);
//
//                LinkedHashMap entryDateToKeyValue = new LinkedHashMap();
//                entryDateToKeyValue.put(entryDate, keyValue);
//
//                LinkedHashMap languageToEntryDate = new LinkedHashMap();
//                languageToEntryDate.put(language, entryDateToKeyValue);
//
//                LinkedHashMap partnerIdToLanguage = new LinkedHashMap();
//                partnerIdToLanguage.put(partnerId, languageToEntryDate);
//
//                contentToPartnerId.put(content, partnerIdToLanguage);
//            } else {
//
//            }
//
//
//            LinkedHashMap keyToValue = new LinkedHashMap();
//            keyToValue.put("version", version);
//            keyToValue.put("key", key);
//            keyToValue.put("value", value);
//
//
//            if (!ObjectUtils.isEmpty(entryDateToKeyValue.get(entryDate))) {
//                List<LinkedHashMap> keyToValues = entryDateToKeyValue.get(entryDate);
//                LinkedHashMap keyToValue = new LinkedHashMap();
//                keyToValue.put("version", version);
//                keyToValue.put("key", key);
//                keyToValue.put("value", value);
//                keyToValues.add(keyToValue);
//                entryDateToKeyValue.put(entryDate, keyToValues);
//            } else {
//                List<LinkedHashMap> keyToValues = new ArrayList<>();
//                LinkedHashMap keyToValue = new LinkedHashMap();
//                keyToValue.put("version", version);
//                keyToValue.put("key", key);
//                keyToValue.put("value", value);
//                keyToValues.add(keyToValue);
//                entryDateToKeyValue.put(entryDate, keyToValues);
//            }
//
//
//            LinkedHashMap languageToEntryDate = new LinkedHashMap();
//            languageToEntryDate.put(language, entryDateToKeyValue);
//
//            LinkedHashMap partnerIdToLanguage = new LinkedHashMap();
//            partnerIdToLanguage.put(partnerId, languageToEntryDate);
//
//            contentToPartnerId.put(content, partnerIdToLanguage);
//        }
//        return ApiResponseUtil.build(HttpStatus.OK, "Csv to yaml successful", contentToPartnerId);
//    }
}
