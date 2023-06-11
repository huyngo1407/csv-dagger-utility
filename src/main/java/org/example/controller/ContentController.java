package org.example.controller;

import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.example.controller.request.GetContentRequest;
import org.example.controller.request.GetImageRequest;
import org.example.model.Content;
import org.example.service.content.ContentService;
import org.example.service.convert.ConvertService;
import org.example.service.image.ImageService;
import org.example.util.CsvUtil;
import org.example.util.YamlUtil;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private final ImageService imageService;
    private final ConvertService convertService;

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
        File file = imageService.get(getImageRequest);
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        URLConnection connection = file.toURL().openConnection();
        String mimeType = connection.getContentType();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(inputStreamResource);
    }

    @GetMapping("/content-csv-to-yaml")
    @ResponseBody
    public ResponseEntity<Object> contentCsvToYaml() throws IOException, CsvException {
        List<String[]> csvLabels = CsvUtil.read("src/main/resources/document/label-csv-to-yaml.csv");
        List<String[]> csvErrors = CsvUtil.read("src/main/resources/document/error-csv-to-yaml.csv");
        Map<String, LinkedHashMap> convertedCsvLabel = convertService.contentCsvToYaml(csvLabels);
        Map<String, LinkedHashMap> convertedCsvError = convertService.contentCsvToYaml(csvErrors);
        Map<String, Object> contentToPartnerId = new HashMap<>();
        contentToPartnerId.put("labels", convertedCsvLabel);
        contentToPartnerId.put("errors", convertedCsvError);
        YamlUtil.write(contentToPartnerId, "src/main/resources/document/content-csv-to-yaml.yaml");
        return ApiResponseUtil.build(HttpStatus.OK, "Content csv to yaml successful", contentToPartnerId);
    }

    @GetMapping("/image-csv-to-yaml")
    @ResponseBody
    public ResponseEntity<Object> imageCsvToYaml() throws IOException, CsvException {
        List<String[]> csvImages = CsvUtil.read("src/main/resources/document/image-csv-to-yaml.csv");
        Map<String, List<LinkedHashMap>> partnerToKeyValue = convertService.imageCsvToYaml(csvImages);
        YamlUtil.write(partnerToKeyValue, "src/main/resources/document/image-csv-to-yaml.yaml");
        return ApiResponseUtil.build(HttpStatus.OK, "Image csv to yaml successful", partnerToKeyValue);
    }
}
