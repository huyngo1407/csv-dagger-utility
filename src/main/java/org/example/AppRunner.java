package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.ContentMapper;
import org.example.service.content.ContentService;
import org.example.util.YamlUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements CommandLineRunner {
    private final CacheManager cacheManager;
    @Value("${file.path.image}")
    private String imagePath;
    @Value("${file.path.content}")
    private String contentPath;

    @Override
    public void run(String... args) {
        log.info("Start fetching data from yaml file");
        try {
            Map<String, Object> imageData = YamlUtil.read(imagePath);
            Map<String, Object> contentData = YamlUtil.read(contentPath);
            ContentService.CONTENT_TO_PARTNER_ID = ContentMapper.toMap(imageData, contentData);
            cacheContents();
        } catch (FileNotFoundException e) {
            log.error("Can not find yaml file");
        } catch (JsonProcessingException e) {
            log.error("Error while processing json: " + e.getMessage());
        }
        log.info("Finish fetching data from csv file");
    }

    private void cacheContents() {
        ContentService.CONTENT_TO_PARTNER_ID.entrySet()
                .stream()
                .forEach(
                        record -> cacheManager.getCache("contents")
                                .put(record.getKey(), record.getValue())
                );
    }
}
