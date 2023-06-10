package org.example.service.convert;

import org.example.mapper.KeyValueMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class ConvertService {
    public Map<String, LinkedHashMap> contentCsvToYaml(List<String[]> csvData) {
        Map<String, LinkedHashMap> contentToPartnerId = new HashMap<>();
        for (String[] line : csvData) {
            String content = line[0];
            Integer entryDate = Integer.valueOf(line[1]);
            String partnerId = line[2];
            String language = line[3];

            LinkedHashMap keyValue = KeyValueMapper.forContent(line);

            if (ObjectUtils.isEmpty(contentToPartnerId.get(content))) {
                List<LinkedHashMap> keyValues = List.of(keyValue);

                LinkedHashMap entryDateToKeyValue = new LinkedHashMap();
                entryDateToKeyValue.put(entryDate, keyValues);

                LinkedHashMap languageToEntryDate = new LinkedHashMap();
                languageToEntryDate.put(language, entryDateToKeyValue);

                LinkedHashMap partnerIdToLanguage = new LinkedHashMap();
                partnerIdToLanguage.put(partnerId, languageToEntryDate);

                contentToPartnerId.put(content, partnerIdToLanguage);
                continue;
            }

            Map<String, LinkedHashMap> partnerIdToLanguage = contentToPartnerId.get(content);
            if (ObjectUtils.isEmpty(partnerIdToLanguage.get(partnerId))) {
                List<LinkedHashMap> keyValues = List.of(keyValue);

                LinkedHashMap entryDateToKeyValue = new LinkedHashMap();
                entryDateToKeyValue.put(entryDate, keyValues);

                LinkedHashMap languageToEntryDate = new LinkedHashMap();
                languageToEntryDate.put(language, entryDateToKeyValue);

                partnerIdToLanguage.put(partnerId, languageToEntryDate);
                continue;
            }

            Map<String, LinkedHashMap> languageToEntryDate = partnerIdToLanguage.get(partnerId);
            if (ObjectUtils.isEmpty(languageToEntryDate.get(language))) {
                List<LinkedHashMap> keyValues = List.of(keyValue);

                LinkedHashMap entryDateToKeyValue = new LinkedHashMap();
                entryDateToKeyValue.put(entryDate, keyValues);

                languageToEntryDate.put(language, entryDateToKeyValue);
                continue;
            }

            Map<Integer, List<LinkedHashMap>> entryDateToKeyValue = languageToEntryDate.get(language);
            if (ObjectUtils.isEmpty(entryDateToKeyValue.get(entryDate))) {
                List<LinkedHashMap> keyValues = List.of(keyValue);

                entryDateToKeyValue.put(entryDate, keyValues);
                continue;
            }
            List<LinkedHashMap> keyValues = new ArrayList<>(entryDateToKeyValue.get(entryDate));
            keyValues.add(keyValue);
            entryDateToKeyValue.put(entryDate, keyValues);
        }
        return contentToPartnerId;
    }

    public Map<String, List<LinkedHashMap>> imageCsvToYaml(List<String[]> csvData) {
        Map<String, List<LinkedHashMap>> partnerIdToKeyValue = new HashMap<>();
        for (String[] line : csvData) {
            String partnerId = line[0];

            LinkedHashMap keyValue = KeyValueMapper.forImage(line);

            if (ObjectUtils.isEmpty(partnerIdToKeyValue.get(partnerId))) {
                List<LinkedHashMap> keyValues = List.of(keyValue);

                partnerIdToKeyValue.put(partnerId, keyValues);

                continue;
            }
            List<LinkedHashMap> keyValues = new ArrayList<>(partnerIdToKeyValue.get(partnerId));
            keyValues.add(keyValue);
            partnerIdToKeyValue.put(partnerId, keyValues);
        }
        return partnerIdToKeyValue;
    }
}
