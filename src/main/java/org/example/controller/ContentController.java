package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.request.GetContentRequest;
import org.example.controller.request.GetImageRequest;
import org.example.service.ContentService;
import org.example.util.api_response.ApiResponseUtil;
import org.example.value_object.ResponseMessageCode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Map;

@Controller
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    //    public ResponseEntity<Object> getAll(@PathVariable("partner-id") String partnerId, @PathVariable("content-type") String contentType) {
    @GetMapping("/{partnerId}/incremental-data/{contentType}")
    public ResponseEntity<Object> getContents(@PathVariable String partnerId, @PathVariable String contentType) {
        GetContentRequest getContentRequest = GetContentRequest.builder()
                .partnerId(partnerId)
                .contentType(contentType)
                .build();
        Map<String, Object> contentToPartnerId = contentService.getContents(getContentRequest);
        return ApiResponseUtil.build(HttpStatus.OK, ResponseMessageCode.CsvDagger.Success.GET_DATA, contentToPartnerId);
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
}